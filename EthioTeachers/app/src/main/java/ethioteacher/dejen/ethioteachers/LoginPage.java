package ethioteacher.dejen.ethioteachers;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class LoginPage extends Activity {

    private EditText username, password;

    private TextView display, notfication;

    private Cursor cursor = null;

    private String Lusername = "", Lpassword = "";

    private SQLiteDatabase account = null;
    private Button reset, login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login_page);

        username = (EditText) findViewById(R.id.editText58);
        password = (EditText) findViewById(R.id.editText59);

        display = (TextView) findViewById(R.id.textView60);
        notfication = (TextView) findViewById(R.id.textView59);

        reset = (Button) findViewById(R.id.button12);
        login = (Button) findViewById(R.id.button11);

        account = new AccountHolder(this).getWritableDatabase();
        try {
            cursor = account.rawQuery("select userName ,password from AccountLogin", null);
            if (!cursor.moveToFirst()) {
                Intent intent = new Intent(LoginPage.this, MainActivity.class);
                intent.putExtra("CreateAccount", "fromLogin");
                startActivity(intent);
                finish();
            } else {
                cursor.moveToLast();
                Lusername = cursor.getString(0);
                Lpassword = cursor.getString(1);
            }
        } catch (Exception e) {

        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    cursor.moveToLast();
                    if (cursor.getString(0).equals(username.getText().toString()) && cursor.getString(1).equals(password.getText().toString())) {
                        Intent intent = new Intent(LoginPage.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        notfication.setText("Invallid username password combination!");
                        username.setText("");
                        password.setText("");
                    }
                } catch (Exception e) {
                }
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.setText("");
                password.setText("");
            }
        });
    }

    @Override
    public void onBackPressed() {
        android.os.Process.killProcess(android.os.Process.myPid());
        super.onBackPressed();
    }
}
