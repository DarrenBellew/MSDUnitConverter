package com.islarf6546.gmail.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by YamiVegeta on 15/11/2015.
 */
public class MyUtilities {

    public static void makeSToast(Context context, String text)  {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void makeLToast(Context context, String text)  {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    public DialogFragment makeDialog(Context context, String title, String message, String posButton, String negButton)  {
        Bundle b = new Bundle();
        b.putString("title", title);
        b.putString("message", message);
        b.putString("posButton", posButton);
        b.putString("negButton", negButton);

        DialogFragment m=new DialogMaker();
        m.setArguments(b);
        return m;
    }



}
