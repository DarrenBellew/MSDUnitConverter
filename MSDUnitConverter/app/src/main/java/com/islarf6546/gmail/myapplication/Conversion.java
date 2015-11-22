package com.islarf6546.gmail.myapplication;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//On text change infinite loop fix reference: http://stackoverflow.com/questions/7222944/changing-text-in-android-on-text-change-causes-overflow-error

public class Conversion extends Activity {

    EditText value1;
    TextView answer;
    Button swap;
    boolean convSwapped = false;
    String valName1;
    String valName2;
    String toFormula;
    String fromFormula;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversion);
        valName1 = getIntent().getExtras().getString("unit1Name");
        valName2 = getIntent().getExtras().getString("unit2Name");
        toFormula = getIntent().getExtras().getString("toFormula");
        fromFormula = getIntent().getExtras().getString("fromFormula");

        value1 = (EditText) findViewById(R.id.edit_text_value_1);
        value1.setHint(valName1);
        answer = (TextView) findViewById(R.id.conv);
        answer.setHint(valName2);
        swap = (Button) findViewById(R.id.button_swap_conversion);
        swap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String temp;
                temp = value1.getText().toString();
                value1.setText(answer.getText());
                answer.setText(temp);
                temp = value1.getHint().toString();
                value1.setHint(answer.getHint());
                answer.setHint(temp);
                swap.setText(temp);
                convSwapped = !convSwapped;
            }
        });
        //change listeners
        value1.addTextChangedListener(new TextWatcher(){
            String old;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //value1.addTextChangedListener(this);
                old = s.toString();
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().isEmpty()) {
                    Log.i("REACHES ON", "YES");
                    double value;
                    value = (Float.valueOf(s.toString()));
                    if (Double.isInfinite(value)) {
                        value1.setText(old);
                        MyUtilities.makeSToast(getApplicationContext(), "Number too large");
                    }
                    else  {
                        DecimalFormat df = new DecimalFormat("#.########");
                        String val = df.format(value);

                        if (convSwapped) {
                            answer.setText(Double.toString(MathsParser.calculate(toFormula, val)));
                        } else {
                            answer.setText(Double.toString(MathsParser.calculate(fromFormula, val)));
                        }
                    }

                }
                else  {

                    answer.setText("");
                }
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //value1.removeTextChangedListener(this);
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
    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        return false;
    }
    /*
    private float getAnswer(float var1, float var2, String formula)  {
        MathsParser m = new MathsParser(var1, var2, formula);
    }


    */
}
