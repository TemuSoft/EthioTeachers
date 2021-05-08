package ethioteacher.dejen.ethioteachers;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddStudentDetailMark extends Activity {

    private EditText editText, editText2, editText3, editText4, editText5, editText6;
    private TextView display, textView2, textView3, textView4, textView5, textView6, textView7;
    private Button button;
    private String Class_Work_10 = "0", Home_Work_5 = "0", Proj_Assign_10 = "0", Group_Work_5 = "0", Mid_Exam_10 = "0", Final_60 = "0";

    private int size =13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_student_detail_mark);

        editText = (EditText) findViewById(R.id.editText21);
        editText2 = (EditText) findViewById(R.id.editText22);
        editText3 = (EditText) findViewById(R.id.editText23);
        editText4 = (EditText) findViewById(R.id.editText24);
        editText5 = (EditText) findViewById(R.id.editText25);
        editText6 = (EditText) findViewById(R.id.editText26);

        display = (TextView) findViewById(R.id.textView13);
        textView2 = (TextView) findViewById(R.id.textView14);
        textView3 = (TextView) findViewById(R.id.textView15);
        textView4 = (TextView) findViewById(R.id.textView16);
        textView5 = (TextView) findViewById(R.id.textView17);
        textView6 = (TextView) findViewById(R.id.textView18);
        textView7 = (TextView) findViewById(R.id.textView19);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        try {
            size = Integer.parseInt(preferences.getString("size", "13"));
        } catch (Exception e) {

        }
        editText.setTextSize(size);
        editText2.setTextSize(size);
        editText3.setTextSize(size);
        editText4.setTextSize(size);
        editText5.setTextSize(size);
        editText6.setTextSize(size);

        display.setTextSize(size+4);
        textView2.setTextSize(size+1);
        textView3.setTextSize(size+1);
        textView4.setTextSize(size+1);
        textView5.setTextSize(size+1);
        textView6.setTextSize(size+1);
        textView7.setTextSize(size+1);

        button = (Button) findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // save command and pass data to the AddStudent class

                if (!editText.getText().toString().isEmpty())
                    Class_Work_10 = editText.getText().toString();
                if (!editText2.getText().toString().isEmpty())
                    Home_Work_5 = editText2.getText().toString();
                if (!editText3.getText().toString().isEmpty())
                    Proj_Assign_10 = editText3.getText().toString();
                if (!editText4.getText().toString().isEmpty())
                    Group_Work_5 = editText4.getText().toString();
                if (!editText5.getText().toString().isEmpty())
                    Mid_Exam_10 = editText5.getText().toString();
                if (!editText6.getText().toString().isEmpty())
                    Final_60 = editText6.getText().toString();

                if (Double.parseDouble(Class_Work_10) > 10 || Double.parseDouble(Home_Work_5) > 5
                        || Double.parseDouble(Proj_Assign_10) > 10 || Double.parseDouble(Group_Work_5) > 5
                        || Double.parseDouble(Mid_Exam_10) > 10 || Double.parseDouble(Final_60) > 60)
                    Toast.makeText(AddStudentDetailMark.this, "detail result is above expected", Toast.LENGTH_SHORT).show();
                else {
                    Intent intent = new Intent(AddStudentDetailMark.this, AddStudent.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("detailCheck", 1);
                    if (editText.getText().toString().isEmpty())
                        bundle.putString("Class_Work_10", "");
                    else
                        bundle.putString("Class_Work_10", Class_Work_10);
                    if (editText2.getText().toString().isEmpty())
                        bundle.putString("Home_Work_5", "");
                    else
                        bundle.putString("Home_Work_5", Home_Work_5);
                    if (editText3.getText().toString().isEmpty())
                        bundle.putString("Proj_Assign_10", "");
                    else
                        bundle.putString("Proj_Assign_10", Proj_Assign_10);
                    if (editText4.getText().toString().isEmpty())
                        bundle.putString("Group_Work_5", "");
                    else
                        bundle.putString("Group_Work_5", Group_Work_5);
                    if (editText5.getText().toString().isEmpty())
                        bundle.putString("Mid_Exam_10", "");
                    else
                        bundle.putString("Mid_Exam_10", Mid_Exam_10);
                    if (editText6.getText().toString().isEmpty())
                        bundle.putString("Final_60", "");
                    else
                        bundle.putString("Final_60", Final_60);
                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });
    }

    public void onBackPressed() {
        Intent intent = new Intent(AddStudentDetailMark.this, AddStudent.class);
        Bundle bundle = new Bundle();
        bundle.putInt("detailCheck", 0);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        super.onBackPressed();
    }
}
