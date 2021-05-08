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

import java.io.File;

public class UpdateStudentMoreEditable extends Activity {

    private EditText editText, editText2, editText3, editText4, editText5, editText6;
    private TextView textView, textView2, textView3, textView4, textView5, textView6, textView10;
    private Button button;
    private String Final_60 = "", Class_Work_10 = "", Home_Work_5 = "", Proj_Assign_10 = "", Group_Work_5 = "", Mid_Exam_10 = "";
    private String UGrade = "";
    private int UNumber;
    private SQLiteDatabase db;
    private String SemesterFirstOrSecond = "First";
    private Cursor cursor;
    private String gradeHolder[] = null, numberHolder[] = null, updateRequest[] = null;
    private File file = null;
    private SharedPreferences preferences;
    private int size = 13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_update_student_more_editable);

        file = new File(this.getFilesDir(), "/Teme/");
        if (!file.exists())
            file.mkdirs();

        if (new Utility().semester(this).equals("Second"))
            SemesterFirstOrSecond = "Second";
        try {
            updateRequest = getIntent().getStringArrayExtra("updateRequest");
        } catch (Exception t) {

        }

        try {
            gradeHolder = getIntent().getStringArrayExtra("gradeHolder");
            numberHolder = getIntent().getStringArrayExtra("numberHolder");
        } catch (Exception t) {

        }
        inputAndAccepetValue();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double finalS = 0, classw = 0, homew = 0, projA = 0, groupw = 0, mide = 0;

                if (!editText.getText().toString().isEmpty())
                    finalS = Double.parseDouble(editText.getText().toString());
                if (!editText2.getText().toString().isEmpty())
                    classw = Double.parseDouble(editText2.getText().toString());
                if (!editText3.getText().toString().isEmpty())
                    homew = Double.parseDouble(editText3.getText().toString());
                if (!editText4.getText().toString().isEmpty())
                    projA = Double.parseDouble(editText4.getText().toString());
                if (!editText5.getText().toString().isEmpty())
                    groupw = Double.parseDouble(editText5.getText().toString());
                if (!editText6.getText().toString().isEmpty())
                    mide = Double.parseDouble(editText6.getText().toString());


                if (!updateRequest[5].equals("")) {
                    Final_60 = ",Final_60 ='" + editText.getText().toString() + "'";
                }
                if (!updateRequest[0].equals("")) {
                    Class_Work_10 = ",Class_Work_10 ='" + editText2.getText().toString() + "'";
                }
                if (!updateRequest[1].equals("")) {
                    Home_Work_5 = ",Home_Work_5 ='" + editText3.getText().toString() + "'";
                }
                if (!updateRequest[2].equals("")) {
                    Proj_Assign_10 = ",Proj_and_Assign_10 ='" + editText4.getText().toString() + "'";
                }
                if (!updateRequest[3].equals("")) {
                    Group_Work_5 = ",Group_Work_5 ='" + editText5.getText().toString() + "'";
                }
                if (!updateRequest[4].equals("")) {
                    Mid_Exam_10 = ",Mid_Exam_10 ='" + editText6.getText().toString() + "'";
                }

                if (finalS > 60 || classw > 10 || homew > 5 || projA > 10 || groupw > 5 || mide > 10)
                    Toast.makeText(UpdateStudentMoreEditable.this, "detail result is above expected", Toast.LENGTH_SHORT).show();
                else {
                    try {
                        db.execSQL("update StudentValue" + SemesterFirstOrSecond + " set Last_update_date ='" + new Utility().CurrentTime(file, UpdateStudentMoreEditable.this) + "'" + Final_60 + Class_Work_10
                                + Home_Work_5 + Proj_Assign_10 + Group_Work_5 + Mid_Exam_10 + " where GradeV = '"
                                + UGrade + "' and NumberV = " + UNumber + "");
                        if (preferences.getBoolean("update", false) == true)
                            new Utility().backUp(UpdateStudentMoreEditable.this);
                        Toast.makeText(UpdateStudentMoreEditable.this, "Sucessful!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UpdateStudentMoreEditable.this, UpdateStudentMore.class);
                        startActivity(intent);
                        finish();
                    } catch (Exception ee) {
                        Toast.makeText(UpdateStudentMoreEditable.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void inputAndAccepetValue() {

        editText = (EditText) findViewById(R.id.editText30);
        editText2 = (EditText) findViewById(R.id.editText31);
        editText3 = (EditText) findViewById(R.id.editText32);
        editText4 = (EditText) findViewById(R.id.editText33);
        editText5 = (EditText) findViewById(R.id.editText34);
        editText6 = (EditText) findViewById(R.id.editText35);

        editText.setAllCaps(true);
        editText2.setAllCaps(true);
        editText3.setAllCaps(true);
        editText4.setAllCaps(true);
        editText5.setAllCaps(true);
        editText6.setAllCaps(true);

        textView = (TextView) findViewById(R.id.textView25);
        textView2 = (TextView) findViewById(R.id.textView26);
        textView3 = (TextView) findViewById(R.id.textView27);
        textView4 = (TextView) findViewById(R.id.textView28);
        textView5 = (TextView) findViewById(R.id.textView29);
        textView6 = (TextView) findViewById(R.id.textView30);

        textView10 = (TextView) findViewById(R.id.textView24);

        button = (Button) findViewById(R.id.button4);

        db = new MainStudentDataDb(this,"STUDENT_DATA.db").getWritableDatabase();

        try {
            UGrade = getIntent().getStringExtra("GradeV");
            UNumber = getIntent().getIntExtra("NumberV", 0);
        } catch (Exception p) {

        }
        cursor = db.rawQuery("select Sname , Fname, Grade , Number,Class_Work_10, Home_Work_5, Proj_and_Assign_10" +
                " , Group_Work_5 , Mid_Exam_10 , Final_60  from StudentData,StudentValue" + SemesterFirstOrSecond + " where" +
                " StudentValue" + SemesterFirstOrSecond + ".NumberV = StudentData.Number and GradeV = '" + UGrade + "' and NumberV = " + UNumber, null);

        cursor.moveToFirst();

        textView10.setText("Name :" + cursor.getString(0) + " " + cursor.getString(1)
                + "\nGrade :" + cursor.getString(2) + " , :" + cursor.getString(3));

        textView.append(" :" + cursor.getString(9));
        textView2.append(" :" + cursor.getString(4));
        textView3.append(" :" + cursor.getString(5));
        textView4.append(" :" + cursor.getString(6));
        textView5.append(" :" + cursor.getString(7));
        textView6.append(" :" + cursor.getString(8));

        if (updateRequest[5].equals(""))
            editText.setEnabled(false);
        if (updateRequest[0].equals(""))
            editText2.setEnabled(false);
        if (updateRequest[1].equals(""))
            editText3.setEnabled(false);
        if (updateRequest[2].equals(""))
            editText4.setEnabled(false);
        if (updateRequest[3].equals(""))
            editText5.setEnabled(false);
        if (updateRequest[4].equals(""))
            editText6.setEnabled(false);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
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


        textView.setTextSize(size + 1);
        textView2.setTextSize(size + 1);
        textView3.setTextSize(size + 1);
        textView4.setTextSize(size + 1);
        textView5.setTextSize(size + 1);
        textView6.setTextSize(size + 1);
        textView10.setTextSize(size + 1);
    }
}
