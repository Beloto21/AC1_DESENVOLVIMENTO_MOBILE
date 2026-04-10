package com.example.ac1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "contacts.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_CONTACTS = "contacts";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_CITY = "city";
    public static final String COLUMN_FAVORITE = "favorite";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_CONTACTS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_PHONE + " TEXT, " +
                    COLUMN_EMAIL + " TEXT, " +
                    COLUMN_CATEGORY + " TEXT, " +
                    COLUMN_CITY + " TEXT, " +
                    COLUMN_FAVORITE + " INTEGER" +
                    ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(db);
    }

    public long addContact(Contato contato) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, contato.getName());
        values.put(COLUMN_PHONE, contato.getPhone());
        values.put(COLUMN_EMAIL, contato.getEmail());
        values.put(COLUMN_CATEGORY, contato.getCategory());
        values.put(COLUMN_CITY, contato.getCity());
        values.put(COLUMN_FAVORITE, contato.isFavorite() ? 1 : 0);
        long id = db.insert(TABLE_CONTACTS, null, values);
        db.close();
        return id;
    }

    public int updateContact(Contato contato) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, contato.getName());
        values.put(COLUMN_PHONE, contato.getPhone());
        values.put(COLUMN_EMAIL, contato.getEmail());
        values.put(COLUMN_CATEGORY, contato.getCategory());
        values.put(COLUMN_CITY, contato.getCity());
        values.put(COLUMN_FAVORITE, contato.isFavorite() ? 1 : 0);
        return db.update(TABLE_CONTACTS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(contato.getId())});
    }

    public void deleteContact(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public List<Contato> getAllContacts(String filterCategory) {
        List<Contato> contatoList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_CONTACTS;
        if (filterCategory != null && !filterCategory.equals("Todos") && !filterCategory.isEmpty()) {
            selectQuery += " WHERE " + COLUMN_CATEGORY + " = '" + filterCategory + "'";
        }
        selectQuery += " ORDER BY " + COLUMN_NAME + " ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Contato contato = new Contato();
                contato.setId(cursor.getInt(0));
                contato.setName(cursor.getString(1));
                contato.setPhone(cursor.getString(2));
                contato.setEmail(cursor.getString(3));
                contato.setCategory(cursor.getString(4));
                contato.setCity(cursor.getString(5));
                contato.setFavorite(cursor.getInt(6) == 1);
                contatoList.add(contato);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return contatoList;
    }
}
