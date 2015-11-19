package com.islarf6546.gmail.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by YamiVegeta on 15/11/2015.
 * References:
 * http://www.newthinktank.com/2014/06/make-android-apps-4/
 * http://stackoverflow.com/questions/10905312/receive-result-from-dialogfragment
 */
public class DialogMaker extends DialogFragment {
    static boolean result = false;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle mArgs = getArguments();
        savedInstanceState = this.getArguments();

        final AlertDialog.Builder theDialog = new AlertDialog.Builder(getActivity());

        theDialog.setTitle(mArgs.getString("title"));

        theDialog.setMessage(mArgs.getString("message"));
        theDialog.setPositiveButton(mArgs.getString("posButton"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                result = true;

            }
        });

        theDialog.setNegativeButton(mArgs.getString("negButton"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                result = false;
            }
        });

        return theDialog.create();
    }

    public static boolean getResult() {
        if(result)  {
            result = false;
            return true;
        }

        return result;
    }


}
