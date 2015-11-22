package com.islarf6546.gmail.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class CreateConversion extends Activity implements AdapterView.OnItemSelectedListener {

    Spinner spinCategories;
    EditText categoryName;
    EditText unit1Name;
    EditText unit2Name;
    TextView toFormula;
    TextView fromFormula;
    Spinner nextToFormula;
    Spinner nextFromFormula;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_conversion);
        spinCategories = (Spinner) findViewById(R.id.spinner_categories);
        categoryName = (EditText) findViewById(R.id.conv_category_name);
        unit1Name = (EditText) findViewById(R.id.edit_text_unit_1);
        unit2Name = (EditText) findViewById(R.id.edit_text_unit_2);
        toFormula = (TextView) findViewById(R.id.text_view_from_formula);
        fromFormula = (TextView) findViewById(R.id.text_view_to_formula);
        nextToFormula = (Spinner) findViewById(R.id.nextToFormula);
        nextFromFormula = (Spinner) findViewById(R.id.nextFromFormula);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.new_category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinCategories.setAdapter(adapter);
        spinCategories.setOnItemSelectedListener(this);


        adapter = ArrayAdapter.createFromResource(this,
                R.array.formulaCreator, android.R.layout.simple_spinner_item);
        nextToFormula.setAdapter(adapter);
        nextFromFormula.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_conversion, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        if(item == "New")  {
            categoryName.setVisibility(View.VISIBLE);
        }
        else  {
            categoryName.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
