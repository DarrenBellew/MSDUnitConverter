package com.islarf6546.gmail.myapplication;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateConversion extends Activity implements OnItemSelectedListener, View.OnClickListener  {

    Spinner spinCategories;
    EditText categoryName;
    EditText unit1Name;
    EditText unit2Name;
    Spinner unit1Spinner;
    Spinner unit2Spinner;
    TextView toFormula;
    TextView fromFormula;
    Spinner nextToFormula;
    Spinner nextFromFormula;
    Button createConversion;
    Button addToFormula;
    Button addFromFormula;
    Button undoToFormula;
    Button undoFromFormula;
    //I set it as Name - Id (with name as key), as that is what is in the Spinners; so efficient access.
    Map<String, Integer> unitsIds = new HashMap<>();
    Map<String, Integer> categoryIds = new HashMap<>();



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
        unit1Spinner = (Spinner) findViewById(R.id.spinner_unit_1);
        unit2Spinner = (Spinner) findViewById(R.id.spinner_unit_2);
        addToFormula.setOnClickListener(this);
        addFromFormula.setOnClickListener(this);
        undoToFormula.setOnClickListener(this);
        undoFromFormula.setOnClickListener(this);
        createConversion.setOnClickListener(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item);
        //also assigns Ids to the hashmap
        adapter.addAll(getCategories());
        adapter.add("New");
        spinCategories.setAdapter(adapter);
        spinCategories.setOnItemSelectedListener(this);
        //this also sets the Map declared above.
        ArrayAdapter<String> adapterUnits = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item);
        adapterUnits.addAll(getUnits());
        adapterUnits.add("New");
        unit1Spinner.setAdapter(adapterUnits);
        unit1Spinner.setOnItemSelectedListener(this);
        unit2Spinner.setAdapter(adapterUnits);
        unit2Spinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapterForm = ArrayAdapter.createFromResource(this,
                R.array.formulaCreator, android.R.layout.simple_spinner_item);
        nextToFormula.setAdapter(adapterForm);
        nextFromFormula.setAdapter(adapterForm);



    }

    public ArrayList<String> getCategories()  {
        ArrayList<String> categories = new ArrayList<>();
        String key = getIntent().getExtras().getString("namesKey");
        String idKey = getIntent().getExtras().getString("idsKey");
        //also assigns ids. Its better to do this, (may it look a little messy), than to run another query.
        for(int i=0; i<getIntent().getExtras().getInt("size"); i++)  {
            String temp = getIntent().getExtras().getString(key+i);
            categories.add(temp);
            int temp2 = getIntent().getExtras().getInt(idKey+i);
            categoryIds.put(temp, temp2);
        }
        return categories;

    }

    public ArrayList<String> getUnits()  {
        ArrayList<String> units = new ArrayList<>();
        try {
            Cursor c = MyUtilities.selectSomething(this, "units", "", new String[]{"unitid", "unitname"});
            while (!c.isAfterLast()) {
                String t = c.getString(1);
                units.add(t);
                unitsIds.put(t, c.getInt(0));
                c.moveToNext();
            }

        }
        catch(SQLException e)  {
            e.printStackTrace();
            MyUtilities.makeSToast(this, "SQL ERROR, UNITS");
            units.add("");//fail-safe
            finish();
        }
        return units;
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
        Log.i("Item at position: " + position, item);

        /*
        +    @Override
+    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
+        String item = parent.getItemAtPosition(position).toString();
+        if(item == "New")  {
+            categoryName.setVisibility(View.VISIBLE);
+        }
+        else  {
+            categoryName.setVisibility(View.GONE);
+        }
+    }
         */
        switch(parent.getId()) {
            case (R.id.spinner_categories): {
                if (item.equals("New")) {
                    Log.i("Category name set", "Visible");
                    categoryName.setVisibility(View.VISIBLE);
                } else {
                    Log.i("Category name set", "INVISIBLE");
                    categoryName.setVisibility(View.INVISIBLE);
                }
                break;
            }
            case (R.id.spinner_unit_1): {
                if (item.equals("New"))  {
                    Log.i("unit 1 was set to: ", "Visible");
                    unit1Name.setVisibility(View.VISIBLE);
                }
                else  {
                    unit1Name.setVisibility(View.INVISIBLE);
                }
                break;
            }
            case (R.id.spinner_unit_2):  {
                if(item.equals("New"))  {
                    Log.i("unit 2 was set to: ", "Visible");
                    unit2Name.setVisibility(View.VISIBLE);
                }
                else  {
                    unit2Name.setVisibility(View.INVISIBLE);
                }
                break;
            }
            default:  {
                Log.i("Something Happened: ",""+view.getId());
            }
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
                //Code to check that inputs are valid

                String name;
                boolean valid = true;
                name = spinCategories.getSelectedItem().toString();
                if(name.matches("New") && categoryName.getText().toString().matches(""))  {
                    valid = false;
                }
                name = unit1Spinner.getSelectedItem().toString();
                if(name.matches("New") && unit1Name.getText().toString().matches(""))  {
                    valid = false;
                }
                name = unit2Name.getText().toString();
                if(name.matches("New") && unit2Name.getText().toString().matches(""))  {
                    valid = false;
                }
                name = toFormula.getText().toString();
                if(name.matches(""))  {
                    valid = false;
                }
                name = fromFormula.getText().toString();
                if(name.matches(""))  {
                    valid = false;
                }

                if(!valid)  {
                    MyUtilities.makeSToast(this, "New Names / Formulas are empty, please enter or change .");
                }
                else  {
                    Map<String, String> items = new HashMap<>();
                    String tempName;
                    int nextId = MyUtilities.getNextId(this, "conversion", "conversionid", 1);
                    items.put("conversionid", Integer.toString(nextId));


                    tempName = unit1Spinner.getSelectedItem().toString();
                    //if new name for the 1st id; insert to new1Id
                    if(tempName.equals("New"))  {
                        try  {
                            tempName = unit1Name.getText().toString();
                            nextId = MyUtilities.getNextId(this, "units", "unitid", 1);
                            MyUtilities.insertSomething(this, "units", new String[]{"unitid", "unitname"}, new String[]{Integer.toString(nextId), tempName});
                            items.put("unit1id", Integer.toString(nextId));
                        }
                        catch(SQLException e)  {
                            e.printStackTrace();
                            MyUtilities.makeSToast(this, "SQL ERROR");
                            finish();
                        }
                    }
                    //if not, get the id and put it into the HashMap.
                    else  {
                        nextId = unitsIds.get(tempName);
                        items.put("unit1id", Integer.toString(nextId));
                    }
                    //repeat for unit2name.
                    tempName = unit2Spinner.getSelectedItem().toString();
                    if(tempName.equals("New"))  {
                        try  {
                            tempName = unit2Name.getText().toString();
                            nextId = MyUtilities.getNextId(this, "units", "unitid", 1);
                            //newUnitQuery = "insert into units(unitid, unitname) values (?, ?)";
                            MyUtilities.insertSomething(this, "units", new String[]{"unitid", "unitname"}, new String[]{Integer.toString(nextId), tempName});
                            items.put("unit2id", Integer.toString(nextId));
                        }
                        catch(SQLException e)  {
                            e.printStackTrace();
                            MyUtilities.makeSToast(this, "SQL ERROR");
                            finish();
                        }
                    }
                    else  {
                        nextId = unitsIds.get(tempName);
                        items.put("unit2id", Integer.toString(nextId));
                    }
                    items.put("toformula", toFormula.getText().toString());
                    items.put("fromformula", fromFormula.getText().toString());
                    tempName = spinCategories.getSelectedItem().toString();
                    if(tempName.equals("New"))  {
                        try  {
                            tempName = categoryName.getText().toString();
                            nextId = MyUtilities.getNextId(this, "units", "unitid", 1);
                            MyUtilities.insertSomething(this, "category", new String[]{"categoryid", "categoryname"}, new String[]{Integer.toString(nextId), tempName});
                            items.put("categoryid", Integer.toString(nextId));
                        }
                        catch(SQLException e)  {
                            e.printStackTrace();
                            MyUtilities.makeSToast(this, "SQL ERROR");
                            finish();
                        }
                    }
                    else  {
                        nextId = unitsIds.get(tempName);
                        items.put("categoryid", Integer.toString(nextId));
                    }


                    //END ASSIGNING HASHMAP
                    String[] itemsToPut = {items.get("conversionid"), items.get("unit1id"), items.get("unit2id"), items.get("toformula"), items.get("fromformula"), items.get("categoryid")};
                    try {
                        MyUtilities.insertSomething(this, "conversion", new String[]{"conversionid", "Unit1Id", "Unit2Id", "toFormula", "fromFormula", "CategoryId"}, itemsToPut);
                        MyUtilities.makeSToast(this, "Conversion successfully created");
                        finish();
                    }
                    catch(SQLException e)  {
                        e.printStackTrace();
                        MyUtilities.makeSToast(this, "ERRORS WERE MADE! New Units/Conversions may have been entered");
                        finish();
                    }
                }



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
