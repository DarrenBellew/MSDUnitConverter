package com.islarf6546.gmail.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import org.apache.commons.lang3.StringUtils;

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
    public DatabaseHelper DBHelper;
    public SQLiteDatabase db;



    public DBManager (Context context)  {

        this.context = context;
        DBHelper = new DatabaseHelper(context, this);
    }

    public static class DatabaseHelper extends SQLiteOpenHelper {
        private Resources resources;
        DBManager dbm;
        public DatabaseHelper(Context context, DBManager dbm) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            resources = context.getResources();
            this.dbm = dbm;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //try  {
                sqlExecutor(new Scanner(resources.openRawResource(R.raw.createtables)),db);
            //}
            //catch (SQLException e)  {
            //    e.getStackTrace();
            //}
            //try {
                sqlExecutor(new Scanner(resources.openRawResource(R.raw.insertdata)), db);
            //}
            //catch (SQLException e)  {
            //    e.getStackTrace();
            //}
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                sqlExecutor(new Scanner(resources.openRawResource(R.raw.createtables)), db);
        }

        public void sqlExecutor(Scanner s, SQLiteDatabase db) {
            StringBuilder commands = new StringBuilder();

            while (s.hasNext()) {
                String line = s.nextLine();
                commands.append(line + "\n");
                if (line.endsWith(");")) {
                    db.execSQL(commands.toString());
                    commands = new StringBuilder();
                }
            }
        }
    }
    //open and close
    public DBManager open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public void close()  { db.close(); }


    public long insertAny(String THIS_TABLE, String[] cols, String[] data) throws SQLException {
        ContentValues vals = new ContentValues();

        for(int j=0; j<cols.length; j++)  {
            vals.put(cols[j], data[j]);
        }

        return db.insert(THIS_TABLE, null, vals);
    }

    public Cursor selectSomething(String THIS_TABLE, String constraint, String[] columns) throws SQLException  {

        Cursor c = db.query(true, THIS_TABLE, columns, constraint,
                null, null, null, null, null);
        c.moveToFirst();

        return c;
    }

    public Cursor selectAdvanced(String query, String[] params) throws SQLException {
        Cursor c;
        if (StringUtils.countMatches(query, "?") == params.length) {
            c = db.rawQuery(query, params);
            c.moveToFirst();
        }
        else {
            throw new SQLException("Incorrect amount of ?:params ratio");

        }
        return c;
    }

    public ArrayList<String> getColumns(String THIS_TABLE) throws SQLException {
        //Code Snippet (i)
        ArrayList<String> tableNames = new ArrayList<String>();

        String query = "PRAGMA table_info("+THIS_TABLE+")";

        Cursor c = db.rawQuery(query, null);
        if(c.moveToFirst())  {
            int i=1;
            do  {
                tableNames.add(c.getString(1));
                i+=2;
            } while(c.moveToNext());
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



    //new version; So when i run it, it'll have a database
    //the method that makes a new version of the database/builds it.


}
