package com.islarf6546.gmail.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by YamiVegeta on 15/11/2015.
 */
public class MyUtilities {

    public static void makeSToast(Context context, String text)  {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void makeLToast(Context context, String text)  {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    public DialogFragment makeDialog(Context context, String title, String message, String posButton, String negButton)  {
        Bundle b = new Bundle();
        b.putString("title", title);
        b.putString("message", message);
        b.putString("posButton", posButton);
        b.putString("negButton", negButton);

        DialogFragment m=new DialogMaker();
        m.setArguments(b);
        return m;
    }

    public static Cursor selectSomething(Context context, String table, String constraint, String columns[]) throws SQLException {
        DBManager dbm = new DBManager(context);
        dbm.open();
        Cursor cursor = dbm.selectSomething(table, constraint, columns);
        dbm.close();
        return cursor;
    }

    public static Cursor selectAdvanced(Context context, String query, String[] params) throws SQLException {
        DBManager dbm = new DBManager(context);
        dbm.open();
        Cursor cursor = dbm.selectAdvanced(query, params);
        dbm.close();
        return cursor;
    }

    public static ArrayList<String> getColumns(Context context, String tableName) throws SQLException {
        DBManager dbm = new DBManager(context);
        ArrayList<String> columns = dbm.getColumns(context, tableName);
        return columns;
    }

    public static Map<String, String> assignAListToMap(ArrayList<String> keys, ArrayList<String> values) {
        Map<String, String> data = new HashMap<String, String>();

        for(int i=0; i<values.size(); i++)  {
            String temp = values.get(i);
            data.put(keys.get(i), temp);
            //Log.i("Data: " + keys.get(i), " Value: " + temp);
        }
        return data;
    }



}
