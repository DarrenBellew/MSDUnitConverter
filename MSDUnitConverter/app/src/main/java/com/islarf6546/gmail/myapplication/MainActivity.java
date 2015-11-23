package com.islarf6546.gmail.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {


    ArrayList<String> categoriesList = new ArrayList<>();//new ArrayAdapter<String>();
    Map<String, Integer> categoryMap = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("tag", "text");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DBManager dbm = new DBManager(this);

        try {
            dbm.open();

            Cursor c = dbm.selectSomething("category", "", new String[]{"categoryId", "CategoryName"});
            while (!c.isAfterLast()) {
                categoryMap.put(c.getString(1), c.getInt(0));
                categoriesList.add(c.getString(1));
                c.moveToNext();
            }
        }
        catch (SQLException e)  {
            e.getStackTrace();
            categoriesList.add("DATABASE READ CATEGORIES ERROR!");
            MyUtilities.makeLToast(this, "No categories exist? try reinstall the app completely!");
            finish();
        }
        finally {
            dbm.close();
        }
        //categories.add("Categories");

        //ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, categories);
        ArrayAdapter<String> myAdapter = new MyAdapter(
                this,
                categoriesList);

        ListView l = (ListView) findViewById(android.R.id.list);

        l.setAdapter(myAdapter);

        l.setOnItemClickListener(new AdapterView.OnItemClickListener()  {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                String itemPicked = String.valueOf(parent.getItemAtPosition(position));

                Intent i = new Intent(getApplicationContext(), SelectCategories.class);
                i.putExtra("itemPicked", itemPicked);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            MyUtilities.makeSToast(this, "Settings Button");
            return true;
        }
        else if(id == R.id.create_conversion)  {
            Intent i = new Intent(getApplicationContext(), CreateConversion.class);

            i.putExtra("idsKey", "categoryId: ");
            for(int j=0; j<categoriesList.size(); j++)  {
                i.putExtra("category Id: "+ j, categoryMap.get(categoriesList.get(j)));
            }
            i.putExtra("namesKey", "category ");
            for(int j=0; j<categoriesList.size(); j++)  {
                i.putExtra("category "+j, categoriesList.get(j));
            }
            i.putExtra("size", categoriesList.size());
            startActivity(i);

        }
        return super.onOptionsItemSelected(item);
    }
}
