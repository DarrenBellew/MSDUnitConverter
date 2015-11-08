package com.islarf6546.gmail.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by YamiVegeta on 08/11/2015.
 */

//Creating and using an SQLite database
public class DBManager {

    /*
    Database attributes
    Map for default table columns
    */
    public static String[][] tables;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "UnitConverterDB";

    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;



    public DBManager (Context context)  {
        this.context = context;
        DBHelper = new DatabaseHelper(context);
        tables = new String[][] {
                {"Category"},
                {"CategoryId number(5) primary key",
                    "CategoryName varchar2(50)"},
                {"Units"},
                {"UnitID number(5)",
                    "UnitName varchar2(50)",
                    "UnitType varchar2(30)",
                    "CategoryId Number(5)",
                    "Foreign key (CategoryId) fk_cat_unit references category (CategoryId)"},//FK
                {"Conversion"},
                {"ConversionId number(5)",
                    "UnitName number(5)",
                    "Unit1Id number(5)",//FK
                    "Unit2Id number(5)",//FK
                    "toFormula varchar2(50)",
                    "UnitType varchar2(10)",//FK
                    "fromFormula varchar2(50)",
                    "Foreign key (UnitName) fk_conv_unit references Units (UnitName)",
                    "Foreign key (UnitId) fk_conv_unit references Units (UnitId)",
                    "Foreign key (UnitId) fk_conv_unit references Units (UnitId)"}
                };
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(createDatabase());
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //update database structure
        }

        public String createDatabase() {
            String sql = "";
            for (int i = 0; i < tables.length; i++) {
                sql += "create table " + tables[i][0] + "(";
                for (int j = 0; j < tables[i].length; j++) {
                    sql += tables[i][j];
                    if (j != tables[i].length - 1) {
                        sql += ",";
                    }
                }
                sql += ");";
            }
            return sql;
        }
    }
    //open and close
    public DBManager open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public void close()  {
        this.close();
    }

    public long insertAny(String THIS_TABLE, String[] cols, String[] data)  {
        ContentValues vals = new ContentValues();

        for(int j=0; j<cols.length; j++)  {
            vals.put(cols[j], data[j]);
        }

        return db.insert(THIS_TABLE, null, vals);
    }

    public Cursor selectSomething(String THIS_TABLE, String[] columns, String constraint)  {
        Cursor c = db.query(true, THIS_TABLE, columns, constraint,
                null, null, null, null, null);
        c.moveToFirst();
        return c;
    }

    public ArrayList<String> getColumns(String THIS_TABLE)  {
        //Code Snippet (i)
        ArrayList<String> tableNames = new ArrayList<String>();

        String query = "PRAGMA table_info("+THIS_TABLE+")";
        Cursor c = db.rawQuery(query, null);
        if(c.moveToFirst())  {
            int i=1;
            do  {
                tableNames.add(c.getString(1));
                i+=2;
            } while(true);
        }
        c.close();
        return tableNames;
    }

    public ArrayList<String> getTables()  {
        ArrayList<String> tableNames = new ArrayList<String>();

        for(int i=0; i<tables.length; i++) {
            tableNames.add(tables[i][0]);
        }
        return tableNames;
    }
}
