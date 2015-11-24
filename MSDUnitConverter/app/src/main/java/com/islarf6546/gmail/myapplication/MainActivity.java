package com.islarf6546.gmail.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//Reference to Refresh Pull: http://www.androidhive.info/2015/05/android-swipe-down-to-refresh-listview-tutorial/
//Reference to Dialog Box: (Android studio website) and: http://stackoverflow.com/questions/13268302/alternative-setbutton

//This is the main activity of the application, it lists the categories in the application;
public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    SwipeRefreshLayout mySwipeRefreshLayout;
    ArrayList<String> categoriesList = new ArrayList<>();
    Map<String, Integer> categoryMap = new HashMap<>();
    ListView l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mySwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mySwipeRefreshLayout.setOnRefreshListener(this);

        fetchCategories();
    }

    //Setup category ArrayList, map and setup the List.
    public void fetchCategories()  {
        categoryMap.clear();
        categoriesList.clear();
        String error = "No categories, refresh or create one!";
        try {
            Cursor c = MyUtilities.selectSomething(this, "category", "", new String[]{"categoryId", "CategoryName"});
            while (!c.isAfterLast()) {
                categoryMap.put(c.getString(1), c.getInt(0));
                categoriesList.add(c.getString(1));
                c.moveToNext();
            }
        }
        catch (SQLException e)  {
            e.getStackTrace();
            categoriesList.add(error);
            MyUtilities.makeLToast(this, "No categories exist? try reinstall the app completely!");
            finish();
        }

        ArrayAdapter<String> myAdapter = new MyAdapter(this, categoriesList);
        l = (ListView) findViewById(android.R.id.list);
        l.setAdapter(myAdapter);
        //Don't setup the listener if there are no categories
        if(!categoriesList.get(0).equals(error)) {
            l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    String itemPicked = String.valueOf(parent.getItemAtPosition(position));
                    Intent i = new Intent(getApplicationContext(), SelectCategories.class);
                    i.putExtra("itemPicked", itemPicked);
                    startActivity(i);
                }
            });
            registerForContextMenu(l);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
        /*getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;*/
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
                i.putExtra("categoryId: "+ j, categoryMap.get(categoriesList.get(j)));
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

    @Override
    public void onRefresh() {
        refreshAnimation(true);
        fetchCategories();
        refreshAnimation(false);
    }
    public void refreshAnimation(boolean set)  {
        mySwipeRefreshLayout.setRefreshing(false);

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)  {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater mInflater = getMenuInflater();
        mInflater.inflate(R.menu.list_context_menu, menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item)  {

        AdapterView.AdapterContextMenuInfo menuAdapterInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId())  {
            case(R.id.delete_item):  {
                String category = l.getItemAtPosition(menuAdapterInfo.position).toString();
                String catId = Integer.toString(categoryMap.get(category));

                String table = "conversion";
                String where = "categoryid = ?";

                String[] whereClause = {catId};
                try {
                    MyUtilities.removeSomething(this, table, where, whereClause);

                    table = "category";
                    where = "categoryid = ?";

                    MyUtilities.removeSomething(this, table, where, whereClause);
                    MyUtilities.makeSToast(this, "Delete success");
                    fetchCategories();
                }
                catch(SQLException e)  {
                    e.printStackTrace();
                    MyUtilities.makeSToast(this, "Unable to delete, sql error occurred");
                }
            }
            case(R.id.rename_item):  {
                renameDialog(l.getItemAtPosition(menuAdapterInfo.position).toString());
            }
        }
        return super.onContextItemSelected(item);
    }

    public void renameDialog(final String item)  {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);
        input.setHint("New Name");
        alertDialog.setTitle(item)
                .setMessage("Enter new name: ")
                .setCancelable(false)
                .setView(input)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //    public static int updateSomething(Context context, String table, String[] cols, String[] newData, String whereClause, String[] whereAguments) throws SQLException {
                        String newName = input.getText().toString();
                        if (!newName.isEmpty()) {
                            String table = "Category";
                            String[] cols = {"categoryname"};
                            String[] newData = {newName};
                            String whereClause = "categoryid = ?";
                            String[] whereArguments = {Integer.toString(categoryMap.get(item))};

                            try {
                                MyUtilities.updateSomething(getApplicationContext(), table, cols, newData, whereClause, whereArguments);
                                //Refresh...
                                fetchCategories();
                            } catch (SQLException e) {
                                e.printStackTrace();
                                MyUtilities.makeSToast(getApplicationContext(), "Rename Error!");
                                dialog.cancel();
                            }

                        } else {
                            MyUtilities.makeSToast(getApplicationContext(), "Please enter a name");
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                }
            });
        AlertDialog alert = alertDialog.create();
        alert.show();


    }

}
