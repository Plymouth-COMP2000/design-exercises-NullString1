package uk.ac.plymouth.danielkern.comp2000.data;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MenuDatabaseSingleton {

    private static MenuDatabaseSingleton instance;
    public final MenuDatabaseHelper db;

    private final List<DBObserver> observers = new ArrayList<>();

    public void addObserver(DBObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(DBObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(DBObserver.Operation operation) {
        for (DBObserver observer : observers) {
            observer.onDatabaseChanged("menu.db", operation);
        }
    }

    private SQLiteDatabase writableDb, readableDb;

    public void openDB() {
        if (writableDb == null || !writableDb.isOpen()) {
            writableDb = db.getWritableDatabase();
        }
        if (readableDb == null || !readableDb.isOpen()) {
            readableDb = db.getReadableDatabase();
        }
    }

    public void closeDB() {
        if (writableDb != null && writableDb.isOpen()) {
            writableDb.close();
        }
        if (readableDb != null && readableDb.isOpen()) {
            readableDb.close();
        }
    }

    private MenuDatabaseSingleton(Context context) {
        db = new MenuDatabaseHelper(context);
    }

    public static MenuDatabaseSingleton getInstance(Context context) {
        if (instance == null) {
            instance = new MenuDatabaseSingleton(context);
        }
        return instance;
    }

    public static class MenuDatabaseHelper extends SQLiteOpenHelper {
        public static final String DATABASE_NAME = "menu.db";
        public static final int DATABASE_VERSION = 1;

        public MenuDatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS menu (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT," +
                    "description TEXT," +
                    "price REAL," +
                    "image BLOB," +
                    "category_id INTEGER," +
                    "FOREIGN KEY(category_id) REFERENCES categories(id)" +
                    ")");
            db.execSQL("CREATE TABLE IF NOT EXISTS categories (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT" +
                    ")");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS menu");
            db.execSQL("DROP TABLE IF EXISTS categories");
            onCreate(db);
        }

        public void insertCategory(String name) {
            SQLiteDatabase db = MenuDatabaseSingleton.instance.writableDb;
            String sql = "INSERT INTO categories (name) VALUES (?)";
            db.execSQL(sql, new Object[]{name});
        }

        public void insertItem(String name, String description, float price, byte[] image, int categoryId) {
            SQLiteDatabase db = MenuDatabaseSingleton.instance.writableDb;
            String sql = "INSERT INTO menu (name, description, price, image, category_id) VALUES (?, ?, ?, ?, ?)";
            db.execSQL(sql, new Object[]{name, description, price, image, categoryId});
            MenuDatabaseSingleton.instance.notifyObservers(DBObserver.Operation.INSERT_MENU_ITEM);

        }

        public void insertItem(String name, String description, float price, BitmapDrawable image, String categoryName) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            image.getBitmap().compress(Bitmap.CompressFormat.WEBP_LOSSY, 100, stream);
            byte[] imageBytes = stream.toByteArray();
            SQLiteDatabase db = MenuDatabaseSingleton.instance.readableDb;
            String sql = "SELECT id FROM categories WHERE name = ?";
            Cursor cursor = db.rawQuery(sql, new String[]{categoryName});
            int categoryId = -1;
            if (cursor.moveToFirst()) {
                categoryId = cursor.getInt(0);
            }
            cursor.close();
            if (categoryId == -1) {
                insertCategory(categoryName);
                cursor = db.rawQuery("SELECT id FROM categories WHERE name = ?", new String[]{categoryName});
                if (cursor.moveToFirst()) {
                    categoryId = cursor.getInt(0);
                }
                cursor.close();
            }
            insertItem(name, description, price, imageBytes, categoryId);
        }

        public int countItems() {
            SQLiteDatabase db = MenuDatabaseSingleton.instance.readableDb;
            String sql = "SELECT COUNT(*) FROM menu";
            Cursor cursor = db.rawQuery(sql, null);
            int count = 0;
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            cursor.close();
            return count;
        }

        public String[] getCategories() {
            SQLiteDatabase db = MenuDatabaseSingleton.instance.readableDb;
            String sql = "SELECT name FROM categories";
            Cursor cursor = db.rawQuery(sql, null);
            String[] categories = new String[cursor.getCount()];
            if (cursor.moveToFirst()) {
                do {
                    String name = cursor.getString(0);
                    categories[cursor.getPosition()] = name;
                } while (cursor.moveToNext());
            }
            cursor.close();
            return categories;
        }

        public void insertItem(MenuItem item) {
            insertItem(item.getName(), item.getDescription(), item.getPrice(), (BitmapDrawable) item.getImage(), item.getCategoryName());
        }

        public MenuItem getItem(String name) {
            SQLiteDatabase db = MenuDatabaseSingleton.instance.readableDb;
            String sql = "SELECT menu.id,menu.name,description,price,image,cat.name FROM menu INNER JOIN categories AS cat ON menu.category_id = cat.id WHERE menu.name = ?";
            Cursor cursor = db.rawQuery(sql, new String[]{name});
            if (cursor.moveToFirst()) {
                int itemId = cursor.getInt(0);
                String itemName = cursor.getString(1);
                String description = cursor.getString(2);
                float price = cursor.getFloat(3);
                byte[] image = cursor.getBlob(4);
                String categoryName = cursor.getString(5);
                BitmapDrawable drawable = new BitmapDrawable(Resources.getSystem(), BitmapFactory.decodeByteArray(image, 0, image.length));
                return new MenuItem(itemId, itemName, description, price, drawable, categoryName);
            }
            cursor.close();
            return null;
        }

        public void insertItems(MenuItem[] items) {
            for (MenuItem item : items) {
                insertItem(item.getName(), item.getDescription(), item.getPrice(), (BitmapDrawable) item.getImage(), item.getCategoryName());
            }
        }

        public void deleteItem(int id) {
            SQLiteDatabase db = MenuDatabaseSingleton.instance.writableDb;
            String sql = "SELECT menu.category_id, COUNT(*) FROM menu INNER JOIN menu AS m2 ON menu.category_id = m2.category_id WHERE menu.id = ? GROUP BY menu.category_id";
            Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(id)});
            int categoryId = -1;
            int itemCountInCategory = 0;
            if (cursor.moveToFirst()) {
                categoryId = cursor.getInt(0);
                itemCountInCategory = cursor.getInt(1);
            }
            cursor.close();
            sql = "DELETE FROM menu WHERE id = ?";
            db.execSQL(sql, new Object[]{id});
            if (itemCountInCategory <= 1 && categoryId != -1) {
                String deleteCatSql = "DELETE FROM categories WHERE id = ?";
                db.execSQL(deleteCatSql, new Object[]{categoryId});
            }
            MenuDatabaseSingleton.instance.notifyObservers(DBObserver.Operation.DELETE_MENU_ITEM);
        }

        public MenuItem[] getItems() {
            SQLiteDatabase db = MenuDatabaseSingleton.instance.readableDb;
            String sql = "SELECT menu.id,menu.name,description,price,image,cat.name FROM menu INNER JOIN categories AS cat ON menu.category_id = cat.id";
            Cursor cursor = db.rawQuery(sql, null);
            MenuItem[] items = new MenuItem[cursor.getCount()];
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    String name = cursor.getString(1);
                    String description = cursor.getString(2);
                    float price = cursor.getFloat(3);
                    byte[] image = cursor.getBlob(4);
                    String categoryName = cursor.getString(5);
                    BitmapDrawable drawable = new BitmapDrawable(Resources.getSystem(), BitmapFactory.decodeByteArray(image, 0, image.length));
                    MenuItem item = new MenuItem(id, name, description, price, drawable, categoryName);
                    items[cursor.getPosition()] = item;
                } while (cursor.moveToNext());
            }
            cursor.close();
            return items;
        }

        public MenuItem getItem(int id) {
            SQLiteDatabase db = MenuDatabaseSingleton.instance.readableDb;
            String sql = "SELECT menu.id,menu.name,description,price,image,cat.name FROM menu INNER JOIN categories AS cat ON menu.category_id = cat.id WHERE menu.id = ?";
            Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(id)});
            if (cursor.moveToFirst()) {
                int itemId = cursor.getInt(0);
                String itemName = cursor.getString(1);
                String description = cursor.getString(2);
                float price = cursor.getFloat(3);
                byte[] image = cursor.getBlob(4);
                String categoryName = cursor.getString(5);
                BitmapDrawable drawable = new BitmapDrawable(Resources.getSystem(), BitmapFactory.decodeByteArray(image, 0, image.length));
                return new MenuItem(itemId, itemName, description, price, drawable, categoryName);
            }
            cursor.close();
            return null;
        }

        public void updateItem(MenuItem item) {
            SQLiteDatabase db = MenuDatabaseSingleton.instance.writableDb;
            String sql = "UPDATE menu SET name = ?, description = ?, price = ?, image = ? WHERE id = ?";
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ((BitmapDrawable) item.getImage()).getBitmap().compress(Bitmap.CompressFormat.WEBP_LOSSY, 100, stream);
            byte[] imageBytes = stream.toByteArray();
            db.execSQL(sql, new Object[]{item.getName(), item.getDescription(), item.getPrice(), imageBytes, item.getId()});
            MenuDatabaseSingleton.instance.notifyObservers(DBObserver.Operation.UPDATE_MENU_ITEM);
        }


        public MenuItem[] getItemsByCategory(String categoryName) {
            SQLiteDatabase db = MenuDatabaseSingleton.instance.readableDb;
            String sql = "SELECT menu.id,menu.name,description,price,image,cat.name FROM menu INNER JOIN categories AS cat ON menu.category_id = cat.id WHERE cat.name = ?";
            Cursor cursor = db.rawQuery(sql, new String[]{categoryName});
            MenuItem[] items = new MenuItem[cursor.getCount()];
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    String name = cursor.getString(1);
                    String description = cursor.getString(2);
                    float price = cursor.getFloat(3);
                    byte[] image = cursor.getBlob(4);
                    String catName = cursor.getString(5);
                    BitmapDrawable drawable = new BitmapDrawable(Resources.getSystem(), BitmapFactory.decodeByteArray(image, 0, image.length));
                    MenuItem item = new MenuItem(id, name, description, price, drawable, catName);
                    items[cursor.getPosition()] = item;
                } while (cursor.moveToNext());
            }
            cursor.close();
            return items;
        }
    }
}
