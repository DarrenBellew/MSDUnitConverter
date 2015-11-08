package com.islarf6546.gmail.myapplication;

import android.app.DialogFragment;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ListActivity {

    private String[] values = {"Measurements", "Liquids", "Currency Exchange"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        ArrayAdapter<String> myAdapter = new MyAdapter(
                this,
                values);
        setListAdapter(myAdapter);
        */
    }

    protected void onListItemClick(ListView l, View v, int position, long id)  {
        String itemPicked = String.valueOf(l.getItemAtPosition(position));
        Toast.makeText(this, "You clicked: " + itemPicked,
                Toast.LENGTH_SHORT).show();
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
            /*
            DialogFragment settingsDialog = new MyDialogFragment();
            myFragment.show(getFragmentManager(), "The Dialog");
            */
            DialogFragment settingDialog = new SettingsDialog();
            settingDialog.show(getFragmentManager(), "Settings Dialog");
            return true;
        } else if(id == R.id.exit_the_app)  {
            DialogFragment exitDialog = new ExitDialog();
            exitDialog.show(getFragmentManager(), "Exit Dialog");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
