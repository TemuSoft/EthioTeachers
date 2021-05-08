package ethioteacher.dejen.ethioteachers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CreateAccount extends AppCompatActivity {

    private TextView display, notfication, textView, textView2, textView3;
    private EditText editText, editText2, editText3;
    private Button reset, signin;
    private SQLiteDatabase account;
    private Cursor cursor;

    private SharedPreferences preferences;
    private int size = 13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_create_account);

        this.setTitle("Create your account!");
         preferences = PreferenceManager.getDefaultSharedPreferences(this);
        try {
            size = Integer.parseInt(preferences.getString("size", "13"));
        } catch (Exception e) {

        }

        display = (TextView) findViewById(R.id.textView34);
        notfication = (TextView) findViewById(R.id.textView57);
        textView = (TextView) findViewById(R.id.textView35);
        textView2 = (TextView) findViewById(R.id.textView47);
        textView3 = (TextView) findViewById(R.id.textView48);

        editText = (EditText) findViewById(R.id.editText20);
        editText2 = (EditText) findViewById(R.id.editText39);
        editText3 = (EditText) findViewById(R.id.editText44);

        display.setTextSize(size + 4);
        notfication.setTextSize(size + 1);
        textView.setTextSize(size + 1);
        textView2.setTextSize(size + 1);
        textView3.setTextSize(size + 1);

        editText.setTextSize(size);
        editText2.setTextSize(size);
        editText3.setTextSize(size);

        reset = (Button) findViewById(R.id.button6);
        signin = (Button) findViewById(R.id.button5);

       // display.setText("Write your \n \tunforgettable \nPassword & Username");

        account = new AccountHolder(this).getWritableDatabase();

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
                editText2.setText("");
                editText3.setText("");
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editText.getText().toString().length() < 1) {
                    notfication.setText("username can't be null");
                    editText.setText("");
                    editText2.setText("");
                    editText3.setText("");
                } else if (editText2.getText().toString().length() < 4 || editText3.getText().toString().length() < 4) {
                    notfication.setText("pssword length must be greater than 4");
                    editText.setText("");
                    editText2.setText("");
                    editText3.setText("");
                } else if (!editText2.getText().toString().equals(editText3.getText().toString())) {
                    notfication.setText("password and confirm password not equal");
                    editText.setText("");
                    editText2.setText("");
                    editText3.setText("");
                } else {
                    account.execSQL("insert into AccountLogin values ('" + editText.getText().toString()
                            + "','" + editText2.getText().toString() + "')");

                    cursor = account.rawQuery("select * from AccountLogin", null);

                    if (cursor.moveToFirst()) {
                        Toast.makeText(CreateAccount.this, "Account create sucessfuly!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(CreateAccount.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        notfication.setText("Account  not  create \n\t Tray again!");
                    }
                }
            }
        });
    }
}
