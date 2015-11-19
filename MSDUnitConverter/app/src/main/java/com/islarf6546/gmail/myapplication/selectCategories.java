package com.islarf6546.gmail.myapplication;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by YamiVegeta on 15/11/2015.
 */
public class selectCategories extends ListActivity {

    ArrayList<String> items = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        //String itemPicked = Intent.getExtras().getString("itemPicked");
        //System.out.println(itemPicked);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DBManager dbm = new DBManager(this);

        try {
            dbm.open();

            //String clause = getIntent().getExtras().getString("itemPicked");
            String item = getIntent().getExtras().getString("itemPicked");

            String clause = "Select * from categories " +
                    "inner join units using categoryid " +
                    "inner join conversion on units.unitid = conversion.unit1id " +
                    "where categoryname = ?";

            //String clause = "Select * from units where exists (select * from category where categoryname = ?)";
            //String clause = "Select * from conversion" +
            //  " inner join units on units.unitId = conversion.unit1Id" +
            //  " where EXISTS (select * from category where categoryname = ?)";





            try {
                Cursor c = dbm.selectAdvanced(clause, new String[]{item});
                while (!c.isAfterLast()) {

                    items.add(c.getString(1) + " <-> " + c.getString(2));
                    c.moveToNext();
                }
            }
            catch (NullPointerException np)  {
                np.printStackTrace();
                finish();
            }

        }
        catch (SQLException e)  {
            e.printStackTrace();
            items.add("DATABASE READ CATEGORIES ERROR!");
            finish();
        }
        finally {
            dbm.close();
        }
        //categories.add("Categories");

        //ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, categories);
        ArrayAdapter<String> myAdapter = new MyAdapter(
                this,
                items);

        ListView l = (ListView) findViewById(android.R.id.list);

        l.setAdapter(myAdapter);

        l.setOnItemClickListener(new AdapterView.OnItemClickListener()  {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                String itemPicked = String.valueOf(parent.getItemAtPosition(position));

                MyUtilities.makeSToast(getApplicationContext(),"you clicked: "+itemPicked);

                Intent i = new Intent(getApplicationContext(), selectCategories.class);
                i.putExtra("itemPicked", itemPicked);
                startActivity(i);
            }
        });

    }
}
