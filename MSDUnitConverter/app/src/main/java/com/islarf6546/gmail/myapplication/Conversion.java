package com.islarf6546.gmail.myapplication;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

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
        value1.addTextChangedListener(new TextWatcher(){
            @Override
            public void afterTextChanged(Editable s)  {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)  {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int arg3)  {
                float value;
                String text = value1.getText().toString();
                value = Float.valueOf(text);
                MathsParser m;
                String form = conversionData.get("toFormula");
                m = new MathsParser(form, value);
                value2.setText(Double.toString(m.eval()));
            }
        });
        value2 = (EditText) findViewById(R.id.edit_text_value_2);
        value2.setFocusable(false);
        value2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void afterTextChanged(Editable s) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                float value;
                String text = value1.getText().toString();
                value = Float.valueOf(text);
                MathsParser m;
                String form = conversionData.get("toFormula");
                m = new MathsParser(form, value);
                value2.setText(Double.toString(m.eval()));
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
