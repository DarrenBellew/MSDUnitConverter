package com.islarf6546.gmail.myapplication;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//Created by Darren Bellew on 15/11/2015.

//This Activity is what appears after the user selects a category on the Main Activity.
//If a category has no conversions, they will be sent back to the main activity, along with a toast appearing.
public class SelectCategories extends ListActivity {

    ArrayList<String> items = new ArrayList<String>();
    Map<String, Integer> conversionids = new HashMap<String, Integer>();

    String valName1 = "";
    String valName2 = "";
    String toFormula = "";
    String fromFormula = "";
    ListView l;
    String item;
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        //String itemPicked = Intent.getExtras().getString("itemPicked");
        //System.out.println(itemPicked);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_list_layout);


        item = getIntent().getExtras().getString("itemPicked");
        fetchConversions();

    }

    public void fetchConversions() {
        items.clear();
        try {
            String clause = "select con.conversionId as Conversion, " +
                    "(select u1.unitName from units as u1 where u1.unitId = con.unit1Id) as Unit1Name, " +
                    "(select u2.unitName from units as u2 where u2.unitId = con.unit2Id) as Unit2Name, " +
                    "con.toFormula, con.fromFormula from conversion as con " +
                    "inner join category as cat on (cat.categoryId = con.categoryId) " +
                    "where cat.categoryname = ?";


            try {
                Cursor c = MyUtilities.queryAdvanced(this, clause, new String[]{item});
                //MyUtilities.makeSToast(this, "" + c.getString(0));

                while (!c.isAfterLast()) {
                    valName1 = c.getString(1);
                    valName2 = c.getString(2);
                    String val = valName2 + " <-> " + valName1;
                    toFormula = c.getString(3);
                    fromFormula = c.getString(4);
                    Log.i("to Formula: ", toFormula);
                    Log.i("from Formula: ", fromFormula);


                    conversionids.put(val, c.getInt(0));
                    items.add(val);
                    c.moveToNext();
                }
            } catch (CursorIndexOutOfBoundsException e) {
                MyUtilities.makeLToast(this, "No conversions exist under: " + item);
                e.printStackTrace();
                finish();
            }

        } catch (SQLException e) {
            MyUtilities.makeLToast(this, "An unexpected SQL exception occurred");
            e.printStackTrace();
            finish();
        }
        if (items.isEmpty()) {
            MyUtilities.makeSToast(this, "No conversions exist");
            finish();
        }

        else  {
            ArrayAdapter<String> myAdapter = new MyAdapter(
                    this,
                    items);


            l = (ListView) findViewById(android.R.id.list);
            l.setAdapter(myAdapter);

            l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {

                    int categoryId = conversionids.get(String.valueOf(parent.getItemAtPosition(position)));


                    MyUtilities.makeSToast(getApplicationContext(), "you clicked conversion: " + categoryId);

                    Intent i = new Intent(getApplicationContext(), Conversion.class);
                    //Send it via intent; why run a query to select data I've selecting already in this activity :)
                    i.putExtra("unit1Name", valName1);
                    i.putExtra("unit2Name", valName2);
                    i.putExtra("fromFormula", fromFormula);
                    i.putExtra("toFormula", toFormula);
                    startActivity(i);
                }
            });
            registerForContextMenu(l);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)  {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater mInflater = getMenuInflater();
        mInflater.inflate(R.menu.list_context_menu, menu);
        //first item
        menu.getItem(0).setVisible(false);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item)  {

        AdapterView.AdapterContextMenuInfo menuAdapterInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId())  {
            case(R.id.delete_item):  {
                String conversion = l.getItemAtPosition(menuAdapterInfo.position).toString();
                String convId = Integer.toString(conversionids.get(conversion));

                String table = "conversion";
                String where = "conversionid = ?";
                String[] whereClause = {convId};
                try {
                    MyUtilities.removeSomething(this, table, where, whereClause);
                    MyUtilities.makeSToast(this, "Delete success");
                    fetchConversions();
                }
                catch(SQLException e)  {
                    e.printStackTrace();
                    MyUtilities.makeSToast(this, "Unable to delete, sql eror occurred");
                    finish();
                }
            }
        }
        return super.onContextItemSelected(item);
    }
}

/*
Notes for fix:


 */
