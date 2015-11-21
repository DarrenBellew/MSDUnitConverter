package com.islarf6546.gmail.myapplication;

import android.app.Activity;
import android.app.DialogFragment;
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
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;

public class MainActivity extends Activity {


    ArrayList<String> categories = new ArrayList<String>();//new ArrayAdapter<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("tag", "text");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DBManager dbm = new DBManager(this);

        try {
            dbm.open();

            Cursor c = dbm.selectSomething("category", "", new String[]{"CategoryName"});
            while (!c.isAfterLast()) {
                categories.add(c.getString(0));
                c.moveToNext();
            }
        }
        catch (SQLException e)  {
            e.getStackTrace();
            categories.add("DATABASE READ CATEGORIES ERROR!");
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
                categories);

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
        MyUtilities m = new MyUtilities();
        DialogFragment d;
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            /*d= m.makeDialog(this, "Settings Dialog", "This is a pracitce settings response", "ok", "Cancel");
            d.show(getFragmentManager(), "Settings Dialog");
            if(DialogMaker.getResult())  {
                MyUtilities.makeSToast(this, "You pressed ok");
            }
            else  {
                MyUtilities.makeSToast(this, "You pressed cancel");
            }*/

            MyUtilities.makeSToast(this, "Settings Button");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
