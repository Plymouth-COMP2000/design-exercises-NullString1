package uk.ac.plymouth.danielkern.comp2000.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ReservationsDatabaseSingleton {

    private static ReservationsDatabaseSingleton instance;
    public final ReservationsDatabaseHelper db;

    private ReservationsDatabaseSingleton(Context context) {
        db = new ReservationsDatabaseHelper(context);
    }

    public static ReservationsDatabaseSingleton getInstance(Context context) {
        if (instance == null) {
            instance = new ReservationsDatabaseSingleton(context);
        }
        return instance;
    }

    public static class ReservationsDatabaseHelper extends SQLiteOpenHelper {
        public static final String DATABASE_NAME = "reservations.db";
        public static final int DATABASE_VERSION = 1;

        public ReservationsDatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS reservation (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "customer_first_name TEXT," +
                    "customer_last_name TEXT," +
                    "total_guests TINYINT," +
                    "num_children TINYINT," +
                    "num_highchairs TINYINT," +
                    "datetime TEXT" +
                    ")");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS reservation");
            onCreate(db);
        }

        public void addReservation(ReservationItem reservation) {
            SQLiteDatabase db = this.getWritableDatabase();
            Object[] values = {
                    reservation.getCustomerFirstName(),
                    reservation.gettCustomerLastName(),
                    reservation.getNumberOfGuests(),
                    reservation.getNumberOfChildren(),
                    reservation.getNumberOfHighChairs(),
                    reservation.getReservationTime().toString()
            };
            db.execSQL("INSERT INTO reservation (customer_first_name, customer_last_name, total_guests, num_children, num_highchairs, datetime) VALUES (?, ?, ?, ?, ?, ?)", values);
            db.close();
        }

        public ReservationItem[] getReservationsByDate(LocalDate date) {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT * FROM reservation WHERE datetime BETWEEN ? AND ?";
            String dayStart = date.atStartOfDay().withHour(0).withMinute(0).withSecond(0).withNano(0).toString();
            String dayEnd = date.atStartOfDay().withHour(23).withMinute(59).withSecond(59).withNano(999999).toString();
            Cursor cursor = db.rawQuery(query, new String[]{dayStart, dayEnd});
            ReservationItem[] reservations = new ReservationItem[cursor.getCount()];
            int i = 0;
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String customerFirstName = cursor.getString(1);
                String customerLastName = cursor.getString(2);
                int totalGuests = cursor.getInt(3);
                int numChildren = cursor.getInt(4);
                int numHighChairs = cursor.getInt(5);
                LocalDateTime reservationTime = LocalDateTime.parse(cursor.getString(6));
                reservations[i++] = new ReservationItem(id, customerFirstName, customerLastName, reservationTime, totalGuests, numChildren, numHighChairs);
            }
            cursor.close();
            db.close();
            return reservations;
        }

        public ReservationItem[] getReservations() {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT id, customer_first_name, customer_last_name, total_guests, num_children, num_highchairs, datetime FROM reservation";
            Cursor cursor = db.rawQuery(query, null);
            ReservationItem[] reservations = new ReservationItem[cursor.getCount()];
            int i = 0;
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String customerFirstName = cursor.getString(1);
                String customerLastName = cursor.getString(2);
                int totalGuests = cursor.getInt(3);
                int numChildren = cursor.getInt(4);
                int numHighChairs = cursor.getInt(5);
                LocalDateTime reservationTime = LocalDateTime.parse(cursor.getString(6));
                reservations[i++] = new ReservationItem(id, customerFirstName, customerLastName, reservationTime, totalGuests, numChildren, numHighChairs);
            }
            cursor.close();
            db.close();
            return reservations;
        }

        public void updateReservation(ReservationItem reservation) {
            SQLiteDatabase db = this.getWritableDatabase();
            String query = "UPDATE reservation SET customer_first_name = ?, customer_last_name = ?, total_guests = ?, num_children = ?, num_highchairs = ?, datetime = ? WHERE id = ?";
            Object[] values = {
                    reservation.getCustomerFirstName(),
                    reservation.gettCustomerLastName(),
                    reservation.getNumberOfGuests(),
                    reservation.getNumberOfChildren(),
                    reservation.getNumberOfHighChairs(),
                    reservation.getReservationTime().toString(),
                    reservation.getReservationId()
            };
            db.execSQL(query, values);
            db.close();
        }

        public ReservationItem getReservationById(int id) {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT * FROM reservation WHERE id = ?";
            Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});
            if (cursor.moveToFirst()) {
                String customerFirstName = cursor.getString(1);
                String customerLastName = cursor.getString(2);
                int totalGuests = cursor.getInt(3);
                int numChildren = cursor.getInt(4);
                int numHighChairs = cursor.getInt(5);
                LocalDateTime reservationTime = LocalDateTime.parse(cursor.getString(6));
                cursor.close();
                db.close();
                return new ReservationItem(id, customerFirstName, customerLastName, reservationTime, totalGuests, numChildren, numHighChairs);
            }
            cursor.close();
            db.close();
            return null;
        }
    }
}
