package com.islarf6546.gmail.myapplication;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

//On text change infinite loop fix reference: http://stackoverflow.com/questions/7222944/changing-text-in-android-on-text-change-causes-overflow-error

public class Conversion extends Activity {

    EditText value1;
    EditText value2;

    Map<String, String> conversionData = new HashMap<>();
    String valName1;
    String valName2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversion);
        final int conversionId = getIntent().getExtras().getInt("conversionId");
        valName1 = getIntent().getExtras().getString("unit1Name");
        valName2 = getIntent().getExtras().getString("unit2Name");
        value1 = (EditText) findViewById(R.id.edit_text_value_1);
        value2 = (EditText) findViewById(R.id.edit_text_value_2);

        //change listeners
        value1.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                value1.addTextChangedListener(this);
            }
            @Override
            public void afterTextChanged(Editable s) {
                value1.removeTextChangedListener(this);

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                value1.removeTextChangedListener(this);
                value2.removeTextChangedListener(this);
                Log.i("REACHES ON", "YES");
                float value;
                value = (Float.valueOf(s.toString()));
                String form = conversionData.get("toFormula");
                value2.setText(Double.toString(MathsParser.calculate("a/100", value)));
                value1.addTextChangedListener(this);
                value2.addTextChangedListener(this); // you register again for listener callbacks
            }
        });

        value2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                value2.addTextChangedListener(this);
            }
            @Override
            public void afterTextChanged(Editable s) {
                value2.removeTextChangedListener(this);
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                value1.removeTextChangedListener(this);
                value2.removeTextChangedListener(this);
                Log.i("REACHES ON", "YES");
                float value;
                value = (Float.valueOf(s.toString()));
                //String form = conversionData.get("fromFormula");
                value1.setText(Double.toString(MathsParser.calculate("a*100", value)));
                value1.addTextChangedListener(this);
                value2.addTextChangedListener(this); // you register again for listener callbacks
            }
        });



        Cursor c;
        try {
            c = MyUtilities.selectAdvanced(this, "select * from conversion where conversionid = ?", new String[]{"" + conversionId});
            if (conversionData instanceof HashMap) {
                conversionData = MyUtilities.assignCursorToMap(MyUtilities.getColumns(this, "conversion"), c);
            }

            value1.setHint(valName1);
            value2.setHint(valName2);

        } catch (SQLException e) {
            e.getStackTrace();
            MyUtilities.makeSToast(this, "Unknown database error");
            finish();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_conversion, menu);
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
    /*
    private float getAnswer(float var1, float var2, String formula)  {
        MathsParser m = new MathsParser(var1, var2, formula);
    }


    */
}
