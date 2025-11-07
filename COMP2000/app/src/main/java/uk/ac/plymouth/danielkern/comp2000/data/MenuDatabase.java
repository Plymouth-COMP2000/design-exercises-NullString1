package uk.ac.plymouth.danielkern.comp2000.data;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;

public class MenuDatabase extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "menu.db";
    public static final int DATABASE_VERSION = 1;

    public MenuDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS menu (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "description TEXT," +
                "price REAL," +
                "image BLOB" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS menu");
        onCreate(db);
    }

    public void insertItem(String name, String description, float price, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "INSERT INTO menu (name, description, price, image) VALUES (?, ?, ?, ?)";
        db.execSQL(sql, new Object[]{name, description, price, image});
        db.close();
    }

    public void insertItem(String name, String description, float price, BitmapDrawable image) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.getBitmap().compress(Bitmap.CompressFormat.WEBP_LOSSY, 100, stream);
        byte[] imageBytes = stream.toByteArray();
        insertItem(name, description, price, imageBytes);
    }

    public void deleteItem(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "DELETE FROM menu WHERE name = ?";
        db.execSQL(sql, new Object[]{name});
        db.close();
    }

    public MenuItem[] getItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM menu";
        Cursor cursor = db.rawQuery(sql, null);
        MenuItem[] items = new MenuItem[cursor.getCount()];
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(1);
                String description = cursor.getString(2);
                float price = cursor.getFloat(3);
                byte[] image = cursor.getBlob(4);
                Drawable drawable = new BitmapDrawable(Resources.getSystem(), BitmapFactory.decodeByteArray(image, 0, image.length));
                MenuItem item = new MenuItem(name, description, price, drawable);
                items[cursor.getPosition()] = item;
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return items;
    }
}
