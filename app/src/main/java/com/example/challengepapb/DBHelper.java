package com.example.challengepapb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class  {
    DBHelper myhelper;
    public DBAdapter(Context context)
    {
        myhelper = new DBHelper(context);
    }

    public long insertData(String latitude, String longitude, String suhu, String cuaca, String kota)
    {
        SQLiteDatabase dbb = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.LATITUDE, latitude);
        contentValues.put(DBHelper.LONGITUDE, longitude);
        contentValues.put(DBHelper.SUHU, suhu);
        contentValues.put(DBHelper.CUACA, cuaca);
        contentValues.put(DBHelper.KOTA, kota);
        long id = dbb.insert(DBHelper.TABLE_NAME, null , contentValues);
        return id;
    }

    public String getData()
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {DBHelper.LATITUDE,DBHelper.LONGITUDE,DBHelper.SUHU,DBHelper.CUACA,DBHelper.KOTA};
        Cursor cursor =db.query(DBHelper.TABLE_NAME,columns,null,null,null,null,null);
        StringBuffer buffer= new StringBuffer();
        while (cursor.moveToNext())
        {
            String latitude = cursor.getString(cursor.getColumnIndex(DBHelper.LATITUDE));
            String longitude =cursor.getString(cursor.getColumnIndex(DBHelper.LONGITUDE));
            String suhu =cursor.getString(cursor.getColumnIndex(DBHelper.SUHU));
            String cuaca =cursor.getString(cursor.getColumnIndex(DBHelper.CUACA));
            String kota =cursor.getString(cursor.getColumnIndex(DBHelper.KOTA));
        }
        return buffer.toString();
    }


    static class DBHelper extends SQLiteOpenHelper
    {
        private static final String DATABASE_NAME = "dbweather";    // Database Name
        private static final String TABLE_NAME = "weather";   // Table Name
        private static final int DATABASE_Version = 1;    // Database Version
        private static final String LATITUDE ="latitude";     // Column I (Primary Key)
        private static final String LONGITUDE = "longitude";    //Column II
        private static final String SUHU = "suhu";    // Column III
        private static final String CUACA = "cuaca";    // Column IV
        private static final String KOTA = "kota";    // Column V
        private static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+
                " ("+LATITUDE+" TEXT, "+LONGITUDE+" TEXT, "+SUHU+" TEXT, "+CUACA+" TEXT,"+KOTA+" TEXT);";
        private static final String DROP_TABLE ="DROP TABLE IF EXISTS "+TABLE_NAME;
        private Context context;

        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_Version);
            this.context=context;
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DROP_TABLE);
            onCreate(db);
        }
    }

}


