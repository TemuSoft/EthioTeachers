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

public class DeleteAccount extends Activity {

    private EditText editText, editText2;

    private TextView display, notfication, textView, textView2;

    private Button delete;

    private Cursor cursor;
    private SharedPreferences preferences;
    private SQLiteDatabase account;

    int size = 13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_delete_account);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            size = Integer.parseInt(preferences.getString("size", "13"));
        } catch (Exception e) {

        }
        editText = (EditText) findViewById(R.id.editText50);
        editText2 = (EditText) findViewById(R.id.editText51);

        display = (TextView) findViewById(R.id.textView49);
        notfication = (TextView) findViewById(R.id.textView58);
        textView = (TextView) findViewById(R.id.textView50);
        textView2 = (TextView) findViewById(R.id.textView51);

        editText.setTextSize(size);
        editText2.setTextSize(size);

        display.setTextSize(size + 4);
        notfication.setTextSize(size + 1);
        textView.setTextSize(size + 1);
        textView2.setTextSize(size + 1);

        delete = (Button) findViewById(R.id.button7);
        account = new AccountHolder(this).getWritableDatabase();

        display.setText("Please write you \n \tCurrent \nPassword & Username");

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                account.execSQL("delete from AccountLogin where userName  = '" + editText.getText().toString()
                        + "' and password = '" + editText2.getText().toString() + "'");

                cursor = account.rawQuery("select * from AccountLogin", null);

                if (cursor.moveToFirst()) {
                    notfication.setText("Incorrect User name and password \n \t\tTray again!");
                    editText.setText("");
                    editText2.setText("");
                } else {
                    account.execSQL("insert into AccountLoginBackup values ('" + editText.getText().toString()
                            + "','" + editText.getText().toString() + "')");
                    Toast.makeText(DeleteAccount.this, "Account delete sucessfuly!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DeleteAccount.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DeleteAccount.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
