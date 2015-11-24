package com.islarf6546.gmail.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyUtilities {
    //private static Toast toast;
    public static void makeSToast(Context context, String text)  {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
    /*public static void makeSToast(Context context, String text, int duration)  {
        Toast.makeText(context, text, duration).show();
    }*/
    public static void makeLToast(Context context, String text)  {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    /*public static void makeLToast(Context context, String text, int duration)  {
        Toast.makeText(context, text, duration).show();
    }*/

    //Doesn't work, was for testing, couldn't get this to work.
    /*public DialogFragment makeDialog(Context context, String title, String message, String posButton, String negButton)  {
        Bundle b = new Bundle();
        b.putString("title", title);
        b.putString("message", message);
        b.putString("posButton", posButton);
        b.putString("negButton", negButton);

        DialogFragment m=new DialogMaker();
        m.setArguments(b);
        return m;
    }*/

    public static void insertSomething(Context context, String tableName, String[] columns, String[] data) throws SQLException  {
        DBManager dbm = new DBManager(context);
        dbm.open();
        dbm.insertAny(tableName, columns, data);
        dbm.close();

    }

    public static Cursor selectSomething(Context context, String table, String constraint, String columns[]) throws SQLException {
        DBManager dbm = new DBManager(context);
        dbm.open();
        Cursor cursor = dbm.selectSomething(table, constraint, columns);
        dbm.close();
        return cursor;
    }

    public static int updateSomething(Context context, String table, String[] cols, String[] newData, String whereClause, String[] whereAguments) throws SQLException {
        DBManager dbm = new DBManager(context);
        int toReturn;
        dbm.open();
            toReturn = dbm.updateSomething(table, cols, newData, whereClause, whereAguments);
        dbm.close();
        return toReturn;
    }

    public static Cursor queryAdvanced(Context context, String query, String[] params) throws SQLException {
        DBManager dbm = new DBManager(context);
        dbm.open();
        Cursor cursor = dbm.queryAdvanced(query, params);
        dbm.close();
        return cursor;
    }

    public static int removeSomething(Context context, String table, String whereClause, String[] whereArgs) throws SQLException  {
        DBManager db = new DBManager(context);
        db.open();
        int result = db.removeSomething(table, whereClause, whereArgs);
        db.close();
        return result;
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

    public static ArrayList<String> assignMaptoAList(Map<String, String> map, String[] keys)  {
        ArrayList<String> toReturn = new ArrayList<String>();

        for(int i=0; i<map.size(); i++)  {
            toReturn.add(map.get(keys[i]));
        }

        return toReturn;
    }

    public static int getNextId(Context context, String table, String col, int tries)  {
        String[] column = {col};
        Cursor c;
        try {
            c = MyUtilities.selectSomething(context, table, "", column);
            //c = queryAdvanced(context, table, column);
        }
        catch (SQLException e)  {
            e.printStackTrace();
            return -1;
        }

        int last = 0;
        for(int i=0; i<tries; i++) {
            while (!c.isAfterLast()) {
                int temp = c.getInt(0);
                if (temp - last > 1) {
                    break;
                }
                last = temp;
                c.moveToNext();

            }
            last++;
        }
        return last;
    }
}
