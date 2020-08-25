package net.ekhdemni.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import net.ekhdemni.R;
import android.widget.TextView;

/**
 * Created by X on 2/25/2018.
 */

public class AlertUtils {

    public abstract static class Action {
        public String title = "";
        public String message = "";
        public String positive = "";
        public String negative = "";
        public abstract void doFunction(Object o);
    }


    public static void toast(Activity context, String text){
        LayoutInflater inflater = context.getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast,
                (ViewGroup) context.findViewById(R.id.toast_layout_root));

        /*ImageView image = (ImageView) layout.findViewById(R.id.image);
        image.setImageResource(R.drawable.overview);*/
        TextView tv = (TextView) layout.findViewById(R.id.text);
        tv.setText(text);

        Toast toast = new Toast(context);
        toast.setGravity(Gravity.BOTTOM, 0, 60);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }


    public static void alert(Context context, final Action action){
        AlertDialog.Builder alert;
        //action.title = (action.title.isEmpty())? "" : action.title;
        action.message = (action.message.isEmpty())? context.getString(R.string.are_you_sure) : action.message;
        action.positive = (action.positive.isEmpty())? context.getString(android.R.string.yes) : action.positive;
        action.negative = (action.negative.isEmpty())? context.getString(android.R.string.no) : action.negative;
        alert = new AlertDialog.Builder(context);
        alert.setTitle(action.title);
        alert.setMessage(action.message);
        alert.setPositiveButton(action.positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                action.doFunction("");
                dialog.dismiss();
            }
        });
        alert.setNegativeButton(action.negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    public static void report(Context context, final Action action){
        AlertDialog.Builder alert;
        //action.title = (action.title.isEmpty())? "" : action.title;
        action.message = (action.message.isEmpty())? context.getString(R.string.are_you_sure) : action.message;
        action.positive = (action.positive.isEmpty())? context.getString(android.R.string.yes) : action.positive;
        action.negative = (action.negative.isEmpty())? context.getString(android.R.string.no) : action.negative;
        alert = new AlertDialog.Builder(context);
        alert.setTitle(action.title);
        alert.setMessage(action.message);
        TextView text = new TextView(context);
        text.setText(action.title);
        alert.setCustomTitle(text);
        final EditText input = new EditText(context);
        input.setHint(context.getText(R.string.report_cause));
        alert.setView(input);
        alert.setPositiveButton(action.positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                action.doFunction(input.getText().toString());
                dialog.dismiss();
            }
        });
        alert.setNegativeButton(action.negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        alert.show();
    }
}
