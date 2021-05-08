package ethioteacher.dejen.ethioteachers;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateAccount extends Activity {

    private EditText oldusername, oldpassword, newusername, newpassword, confirmpassword;

    private TextView display, notfication;

    private Button reset, update;

    private SQLiteDatabase account;

    private Cursor cursor;

    private TextView textView, textView2, textView3, textView4, textView5;

    private SharedPreferences preferences;

    private int size = 13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_update_account);

        oldusername = (EditText) findViewById(R.id.editText52);
        oldpassword = (EditText) findViewById(R.id.editText53);
        newusername = (EditText) findViewById(R.id.editText54);
        newpassword = (EditText) findViewById(R.id.editText55);
        confirmpassword = (EditText) findViewById(R.id.editText56);

        display = (TextView) findViewById(R.id.textView52);
        notfication = (TextView) findViewById(R.id.textView64);
        textView = (TextView) findViewById(R.id.textView53);
        textView2 = (TextView) findViewById(R.id.textView54);
        textView3 = (TextView) findViewById(R.id.textView55);
        textView4 = (TextView) findViewById(R.id.textView56);
        textView5 = (TextView) findViewById(R.id.textView63);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        try {
            size = Integer.parseInt(preferences.getString("size", "13"));
        } catch (Exception e) {

        }

        display.setTextSize(size + 4);
        textView.setTextSize(size + 1);
        textView2.setTextSize(size + 1);
        textView3.setTextSize(size + 1);
        textView4.setTextSize(size + 1);
        textView5.setTextSize(size + 1);
        notfication.setTextSize(size + 1);

        oldpassword.setTextSize(size);
        oldusername.setTextSize(size);
        newpassword.setTextSize(size);
        newusername.setTextSize(size);
        confirmpassword.setTextSize(size);

        display.setText("Please write your current \n\tAND\nnew password & username");

        reset = (Button) findViewById(R.id.button10);
        update = (Button) findViewById(R.id.button9);

        account = new AccountHolder(this).getWritableDatabase();

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldpassword.setText("");
                oldusername.setText("");
                newusername.setText("");
                newpassword.setText("");
                confirmpassword.setText("");
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cursor = account.rawQuery("select * from AccountLogin where password  = '" + oldpassword.getText().toString()
                        + "' and userName = '" + oldusername.getText().toString() + "'", null);
                if (cursor.moveToFirst()) {
                    if (newusername.getText().toString().length() < 1) {
                        notfication.setText("new username can't be null");
                        oldpassword.setText("");
                        oldusername.setText("");
                        newusername.setText("");
                        newpassword.setText("");
                        confirmpassword.setText("");
                    } else if (newpassword.getText().toString().length() < 4 || confirmpassword.getText().toString().length() < 4) {
                        notfication.setText("new pssword length must be greater than 4");
                        oldpassword.setText("");
                        oldusername.setText("");
                        newusername.setText("");
                        newpassword.setText("");
                        confirmpassword.setText("");
                    } else if (!newpassword.getText().toString().equals(confirmpassword.getText().toString())) {
                        notfication.setText("new password and confirm new password not equal");
                        oldpassword.setText("");
                        oldusername.setText("");
                        newusername.setText("");
                        newpassword.setText("");
                        confirmpassword.setText("");
                    } else {
                        account.execSQL("update AccountLogin set userName ='" + newusername.getText().toString()
                                + "',password = '" + newpassword.getText().toString() + "' where userName ='"
                                + oldusername.getText().toString() + "' and password = '"
                                + oldpassword.getText().toString() + "'");

                        Toast.makeText(UpdateAccount.this, "Account update sucessfuly!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UpdateAccount.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    }
                } else {
                    notfication.setText("unknown old account");
                    oldpassword.setText("");
                    oldusername.setText("");
                    newusername.setText("");
                    newpassword.setText("");
                    confirmpassword.setText("");
                }

            }
        });

    }
}