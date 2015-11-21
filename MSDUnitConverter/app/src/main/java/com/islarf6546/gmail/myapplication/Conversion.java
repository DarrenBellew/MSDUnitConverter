package com.islarf6546.gmail.myapplication;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Conversion extends Activity {

    EditText value1;
    EditText value2;
    Button swapConversion;
    Button calculate;
    TextView answer;

    Map<String, String> conversionData = new HashMap<String, String>();
    boolean convSwitched = false;
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
        value2.setFocusable(false);
        value1.addTextChangedListener(new TextWatcher(){
            @Override
            public void afterTextChanged(Editable arg0)  {
            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3)  {
            }
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3)  {
                float value;
                String text = value1.getText().toString();
                value = Float.valueOf(text);
                MathsParser m;
                String form = conversionData.get("toFormula");
                m = new MathsParser(form, value);
                value2.setText(Double.toString(m.eval()));
            }
        });
        swapConversion = (Button) findViewById(R.id.button_swap_conversion);
        calculate = (Button) findViewById(R.id.button_calculate);
        answer = (TextView) findViewById(R.id.text_view_answer);


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



        swapConversion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //already switched, setting back
                if(convSwitched)  {
                    value2.setFocusableInTouchMode(false);
                    value1.setFocusable(true);
                }
                //not switched, making switched
                else  {
                    value2.setFocusable(true);
                    value1.setFocusableInTouchMode(false);
                }
                //changing the boolean
                convSwitched = !convSwitched;

            }
        });

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
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
