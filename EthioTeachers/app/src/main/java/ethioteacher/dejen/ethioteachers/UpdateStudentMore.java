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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class UpdateStudentMore extends Activity {

    private String resultHolder[], gradeHolder[], numberHolder[];

    private String updateRequest[] = new String[6];

    private TextView textView;

    private CheckBox checkBox, checkBox2, checkBox3, checkBox4, checkBox5, checkBox6, checkBox10, checkBox11;

    private String Class_Work_10 = "", Home_Work_5 = "", Proj_Assign_10 = "", Group_Work_5 = "", Mid_Exam_10 = "", Final_60 = "";

    private EditText editText, editText2, editText3, editText4, editText5, editText6;

    private Button button;

    private SQLiteDatabase db;

    private Cursor cursor = null;

    private String sqlSelectCommand = null;

    private String SemesterFirstOrSecond = "First";

    private SharedPreferences preferences;

    private int size = 13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_update_student_more);

        checkBox = (CheckBox) findViewById(R.id.a);
        checkBox2 = (CheckBox) findViewById(R.id.b);
        checkBox3 = (CheckBox) findViewById(R.id.c);
        checkBox4 = (CheckBox) findViewById(R.id.d);
        checkBox5 = (CheckBox) findViewById(R.id.e);
        checkBox6 = (CheckBox) findViewById(R.id.f);
        checkBox10 = (CheckBox) findViewById(R.id.k); // select all
        checkBox11 = (CheckBox) findViewById(R.id.l); // dis select all

        editText = (EditText) findViewById(R.id.aa);
        editText2 = (EditText) findViewById(R.id.bb);
        editText3 = (EditText) findViewById(R.id.cc);
        editText4 = (EditText) findViewById(R.id.dd);
        editText5 = (EditText) findViewById(R.id.ee);
        editText6 = (EditText) findViewById(R.id.ff);
        button = (Button) findViewById(R.id.search);
        button.setText("UPDATE");

        textView = (TextView)findViewById(R.id.total);

        button.setEnabled(false);

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


        checkBox.setTextSize(size + 1);
        checkBox2.setTextSize(size + 1);
        checkBox3.setTextSize(size + 1);
        checkBox4.setTextSize(size + 1);
        checkBox5.setTextSize(size + 1);
        checkBox6.setTextSize(size + 1);
        checkBox10.setTextSize(size + 1);
        checkBox11.setTextSize(size + 1);

        textView.setTextSize(size);

        editText.setEnabled(false);
        editText2.setEnabled(false);
        editText3.setEnabled(false);
        editText4.setEnabled(false);
        editText5.setEnabled(false);
        editText6.setEnabled(false);

        editText.setAllCaps(true);
        editText2.setAllCaps(true);
        editText3.setAllCaps(true);
        editText4.setAllCaps(true);
        editText5.setAllCaps(true);
        editText6.setAllCaps(true);

        db = new MainStudentDataDb(this,"STUDENT_DATA.db").getWritableDatabase();

        if (new Utility().semester(this).equals("Second"))
            SemesterFirstOrSecond = "Second";

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Class_Work_10 = "";
                Home_Work_5 = "";
                Proj_Assign_10 = "";
                Group_Work_5 = "";
                Mid_Exam_10 = "";
                Final_60 = "";
                seeCheckedBox();
                updateRequest[0] = Class_Work_10;
                updateRequest[1] = Home_Work_5;
                updateRequest[2] = Proj_Assign_10;
                updateRequest[3] = Group_Work_5;
                updateRequest[4] = Mid_Exam_10;
                updateRequest[5] = Final_60;
                sqlSelectCommand = "select Grade,Number, Sname , Fname , GFname ,Subject " +
                        ", Grade , Number , Sex ,Class_Work_10, Home_Work_5, " +
                        "Proj_and_Assign_10 , Group_Work_5 , Mid_Exam_10 , Final_60" +
                        " from StudentData inner join StudentValue" + SemesterFirstOrSecond + " on "
                        + " StudentValue" + SemesterFirstOrSecond + ".NumberV = StudentData.Number"
                        + " and StudentValue" + SemesterFirstOrSecond + ".GradeV = StudentData.Grade "
                        + Final_60 + Class_Work_10
                        + Home_Work_5 + Proj_Assign_10 + Group_Work_5 + Mid_Exam_10;
                cursor = db.rawQuery(sqlSelectCommand, null);
                if (cursor.moveToFirst()) {
                    try {
                        int totalCount = 0;
                        cursor = db.rawQuery(sqlSelectCommand, null);
                        resultHolder = new String[cursor.getCount()];
                        gradeHolder = new String[cursor.getCount()];
                        numberHolder = new String[cursor.getCount()];
                        if (cursor.moveToFirst()) {
                            do {
                                resultHolder[totalCount] = "Name  : " + cursor.getString(2) + " " + cursor.getString(3) + " " + cursor.getString(4)
                                        + "\nSubject :" + cursor.getString(5) + " , Grade :" + cursor.getString(6)
                                        + "\nNumber :" + cursor.getString(7) + "  Sex  :" + cursor.getString(8)
                                        + "\n\n" + cursor.getColumnName(9) + "           :" + cursor.getString(9)
                                        + "\n" + cursor.getColumnName(10) + "            :" + cursor.getString(10)
                                        + "\n" + cursor.getColumnName(11) + "  :" + cursor.getString(11)
                                        + "\n" + cursor.getColumnName(12) + "            :" + cursor.getString(12)
                                        + "\n" + cursor.getColumnName(13) + "             :" + cursor.getString(13)
                                        + "\n" + cursor.getColumnName(14) + "                      :" + cursor.getString(14) + "\n";
                                gradeHolder[totalCount] = cursor.getString(0);
                                numberHolder[totalCount] = cursor.getString(1);
                                totalCount++;
                            } while (cursor.moveToNext());
                        }

                        Intent intent = new Intent(UpdateStudentMore.this, ListViewClass.class);
                        Bundle bundle = new Bundle();
                        bundle.putStringArray("resultHolder", resultHolder);
                        bundle.putStringArray("gradeHolder", gradeHolder);
                        bundle.putStringArray("numberHolder", numberHolder);
                        bundle.putStringArray("updateRequest", updateRequest);
                        bundle.putString("operation", "updateResult");
                        intent.putExtras(bundle);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(UpdateStudentMore.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                } else
                    Toast.makeText(UpdateStudentMore.this, "No smilar data found!", Toast.LENGTH_SHORT).show();

            }
        });

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!checkBox.isChecked()) {
                    checkBox.setChecked(false);
                    editText.setEnabled(false);
                    editText.setText("");
                    if (!checkBox.isChecked() && !checkBox2.isChecked() && !checkBox3.isChecked() && !checkBox4.isChecked()
                            && !checkBox5.isChecked() && !checkBox6.isChecked())
                        button.setEnabled(false);
                    else
                        button.setEnabled(true);
                } else {
                    checkBox.setChecked(true);
                    editText.setEnabled(true);
                    button.setEnabled(true);
                }
                checkBox10.setChecked(false);
                checkBox11.setChecked(false);
            }
        });
        checkBox2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!checkBox2.isChecked()) {
                    checkBox2.setChecked(false);
                    editText2.setEnabled(false);
                    editText2.setText("");
                    if (!checkBox.isChecked() && !checkBox2.isChecked() && !checkBox3.isChecked() && !checkBox4.isChecked()
                            && !checkBox5.isChecked() && !checkBox6.isChecked())
                        button.setEnabled(false);
                    else
                        button.setEnabled(true);
                } else {
                    checkBox2.setChecked(true);
                    editText2.setEnabled(true);
                    button.setEnabled(true);
                }
                checkBox10.setChecked(false);
                checkBox11.setChecked(false);


            }
        });
        checkBox3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!checkBox3.isChecked()) {
                    checkBox3.setChecked(false);
                    editText3.setEnabled(false);
                    editText3.setText("");
                    if (!checkBox.isChecked() && !checkBox2.isChecked() && !checkBox3.isChecked() && !checkBox4.isChecked()
                            && !checkBox5.isChecked() && !checkBox6.isChecked())
                        button.setEnabled(false);
                    else
                        button.setEnabled(true);
                } else {
                    checkBox3.setChecked(true);
                    editText3.setEnabled(true);
                    button.setEnabled(true);
                }
                checkBox10.setChecked(false);
                checkBox11.setChecked(false);
            }
        });
        checkBox4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!checkBox4.isChecked()) {
                    checkBox4.setChecked(false);
                    editText4.setEnabled(false);
                    editText4.setText("");
                    if (!checkBox.isChecked() && !checkBox2.isChecked() && !checkBox3.isChecked() && !checkBox4.isChecked()
                            && !checkBox5.isChecked() && !checkBox6.isChecked())
                        button.setEnabled(false);
                    else
                        button.setEnabled(true);
                } else {
                    checkBox4.setChecked(true);
                    editText4.setEnabled(true);
                    button.setEnabled(true);
                }
                checkBox10.setChecked(false);
                checkBox11.setChecked(false);
            }
        });
        checkBox5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!checkBox5.isChecked()) {
                    checkBox5.setChecked(false);
                    editText5.setEnabled(false);
                    editText5.setText("");
                    if (!checkBox.isChecked() && !checkBox2.isChecked() && !checkBox3.isChecked() && !checkBox4.isChecked()
                            && !checkBox5.isChecked() && !checkBox6.isChecked())
                        button.setEnabled(false);
                    else
                        button.setEnabled(true);
                } else {
                    checkBox5.setChecked(true);
                    editText5.setEnabled(true);
                    button.setEnabled(true);
                }
                checkBox10.setChecked(false);
                checkBox11.setChecked(false);
            }
        });
        checkBox6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!checkBox6.isChecked()) {
                    checkBox6.setChecked(false);
                    editText6.setEnabled(false);
                    editText6.setText("");
                    if (!checkBox.isChecked() && !checkBox2.isChecked() && !checkBox3.isChecked() && !checkBox4.isChecked()
                            && !checkBox5.isChecked() && !checkBox6.isChecked())
                        button.setEnabled(false);
                    else
                        button.setEnabled(true);
                } else {
                    checkBox6.setChecked(true);
                    editText6.setEnabled(true);
                    button.setEnabled(true);
                }
                checkBox10.setChecked(false);
                checkBox11.setChecked(false);
            }
        });
        checkBox10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //select all
                checkBox.setChecked(true);
                checkBox2.setChecked(true);
                checkBox3.setChecked(true);
                checkBox4.setChecked(true);
                checkBox5.setChecked(true);
                checkBox6.setChecked(true);
                checkBox10.setChecked(true);
                checkBox11.setChecked(false);

                editText.setEnabled(true);
                editText2.setEnabled(true);
                editText3.setEnabled(true);
                editText4.setEnabled(true);
                editText5.setEnabled(true);
                editText6.setEnabled(true);
                button.setEnabled(true);

            }
        });
        checkBox11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //disselect all
                checkBox.setChecked(false);
                checkBox2.setChecked(false);
                checkBox3.setChecked(false);
                checkBox4.setChecked(false);
                checkBox5.setChecked(false);
                checkBox6.setChecked(false);
                checkBox10.setChecked(false);
                checkBox11.setChecked(true);

                editText.setEnabled(false);
                editText2.setEnabled(false);
                editText3.setEnabled(false);
                editText4.setEnabled(false);
                editText5.setEnabled(false);
                editText6.setEnabled(false);

                editText.setText("");
                editText2.setText("");
                editText3.setText("");
                editText4.setText("");
                editText5.setText("");
                editText6.setText("");

                button.setEnabled(false);

            }
        });
    }

    public void onBackPressed() {
        Intent intent = new Intent(this, UpdateStudent.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        super.onBackPressed();
    }

    private void seeCheckedBox() {
        if (checkBox.isChecked()) {
            if (!editText.getText().toString().isEmpty()) {
                Final_60 = " and Final_60 = " + Double.parseDouble(editText.getText().toString());
            } else
                Final_60 = " and Final_60 = ''";
        }
        if (checkBox2.isChecked()) {
            if (!editText2.getText().toString().isEmpty()) {
                Class_Work_10 = " and Class_Work_10 = " + Double.parseDouble(editText2.getText().toString());
            } else
                Class_Work_10 = " and Class_Work_10 = ''";
        }
        if (checkBox3.isChecked()) {
            //delete student by grand father
            if (!editText3.getText().toString().isEmpty()) {
                Home_Work_5 = " and Home_Work_5 = " + Double.parseDouble(editText3.getText().toString());
            } else
                Home_Work_5 = " and Home_Work_5 = ''";
        }
        if (checkBox4.isChecked()) {
            //delete student by school
            // this not want any logic delete all student in short
            if (!editText4.getText().toString().isEmpty()) {
                Proj_Assign_10 = " and Proj_and_Assign_10 = " + Double.parseDouble(editText4.getText().toString());
            } else
                Proj_Assign_10 = " and Proj_and_Assign_10 = ''";
        }
        if (checkBox5.isChecked()) {
            if (!editText5.getText().toString().isEmpty()) {
                Group_Work_5 = " and Group_Work_5 = " + Double.parseDouble(editText5.getText().toString());
            } else
                Group_Work_5 = " and Group_Work_5 = ''";
        }
        if (checkBox6.isChecked()) {
            if (!editText6.getText().toString().isEmpty()) {
                Mid_Exam_10 = " and Mid_Exam_10 = " + Double.parseDouble(editText6.getText().toString());
            } else
                Mid_Exam_10 = " and Mid_Exam_10 = ''";
        }
    }
}
