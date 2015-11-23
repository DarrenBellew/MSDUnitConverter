package com.islarf6546.gmail.myapplication;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;

public class CreateConversion extends Activity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    Spinner spinCategories;
    EditText categoryName;
    EditText unit1Name;
    EditText unit2Name;
    TextView toFormula;
    TextView fromFormula;
    Spinner nextToFormula;
    Spinner nextFromFormula;
    Button createConversion;
    Button addToFormula;
    Button addFromFormula;
    Button undoToFormula;
    Button undoFromFormula;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_conversion);
        spinCategories = (Spinner) findViewById(R.id.spinner_categories);
        categoryName = (EditText) findViewById(R.id.conv_category_name);
        unit1Name = (EditText) findViewById(R.id.edit_text_unit_1);
        unit2Name = (EditText) findViewById(R.id.edit_text_unit_2);
        toFormula = (TextView) findViewById(R.id.text_view_to_formula);
        fromFormula = (TextView) findViewById(R.id.text_view_from_formula);
        nextToFormula = (Spinner) findViewById(R.id.nextToFormula);
        nextFromFormula = (Spinner) findViewById(R.id.nextFromFormula);
        createConversion = (Button) findViewById(R.id.button_create_conversion);
        addToFormula = (Button) findViewById(R.id.add_to_form);
        addFromFormula = (Button) findViewById(R.id.add_from_form);
        undoToFormula = (Button) findViewById(R.id.undo_to_form);
        undoFromFormula = (Button) findViewById(R.id.undo_from_form);
        addToFormula.setOnClickListener(this);
        addFromFormula.setOnClickListener(this);
        undoToFormula.setOnClickListener(this);
        undoFromFormula.setOnClickListener(this);
        createConversion.setOnClickListener(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item);
        adapter.addAll(getCategories());
        adapter.add("New");

        spinCategories.setAdapter(adapter);
        spinCategories.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapterForm = ArrayAdapter.createFromResource(this,
                R.array.formulaCreator, android.R.layout.simple_spinner_item);
        nextToFormula.setAdapter(adapterForm);
        nextFromFormula.setAdapter(adapterForm);



    }

    public ArrayList<String> getCategories()  {
        ArrayList<String> categories = new ArrayList<>();
        String temp;
        String key = getIntent().getExtras().getString("key");
        for(int i=0; i<getIntent().getExtras().getInt("size"); i++)  {
            temp = getIntent().getExtras().getString(key+i);
            categories.add(temp);
        }

        return categories;

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
    public boolean onPrepareOptionsMenu (Menu menu) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        if(item.equals("New"))  {
            categoryName.setVisibility(View.VISIBLE);
        }
        else  {
            categoryName.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    @Override
    public void onClick(View v) {
        /*
        createConversion = (Button) findViewById(R.id.button_create_conversion);
        addToFormula = (Button) findViewById(R.id.add_to_form);
        addFromFormula = (Button) findViewById(R.id.add_from_form);
        undoToFormula = (Button) findViewById(R.id.undo_to_form);
        undoFromFormula = (Button) findViewById(R.id.undo_from_form);
         */

        switch(v.getId())  {
            case (R.id.button_create_conversion):  {
                //createConversion;
                break;
            }
            case (R.id.add_to_form):  {
                //addToFormula;
                String toAdd = nextToFormula.getSelectedItem().toString();
                String current = toFormula.getText().toString();
                if(current.length() >= Integer.MAX_VALUE)  {
                    MyUtilities.makeSToast(this, "Too much characters in formula");
                }
                else {
                    String temp = current + convItem(toAdd);
                    toFormula.setText(temp);
                }
                break;
            }
            case (R.id.add_from_form):  {
                //addFromFormula;
                String toAdd = nextFromFormula.getSelectedItem().toString();
                String current = fromFormula.getText().toString();
                if(current.length() >= Integer.MAX_VALUE)  {
                    MyUtilities.makeSToast(this, "Too much characters in formula");
                }
                else  {
                    String temp = current + convItem(toAdd);
                    fromFormula.setText(temp);
                }
                break;
            }
            case(R.id.undo_to_form):  {
                //undoToFormula;
                String temp = toFormula.getText().toString();
                if (temp.length() > 0) {
                    temp = temp.substring(0, temp.length() - 1);
                }
                toFormula.setText(temp);
                break;
            }
            case(R.id.undo_from_form):  {
                //undoFromFormula;
                String temp = fromFormula.getText().toString();
                if (temp.length() > 0) {
                    temp = temp.substring(0, temp.length() - 1);
                }
                fromFormula.setText(temp);
                break;
            }
        }
    }
    public String convItem(String item)  {
        String toReturn;
        switch(item)  {
            case("Decimal"):  {
                toReturn = ".";
                break;
            }
            case("Multiply"):  {
                toReturn = "*";
                break;
            }
            case("Divide"):  {
                toReturn = "/";
                break;
            }
            case("Addition"):  {
                toReturn = "+";
                break;
            }
            case("Subtract"):  {
                toReturn = "-";
                break;
            }
            case("Power"):  {
                toReturn = "^";
                break;
            }
            case("Open bracket"):  {
                toReturn = "(";
                break;
            }
            case("Close bracket"):  {
                toReturn = ")";
                break;
            }
            case("Unit"): {
                toReturn = "a";
                break;
            }
            default:  {
                return item;
            }
        }
        return toReturn;

    }
}
