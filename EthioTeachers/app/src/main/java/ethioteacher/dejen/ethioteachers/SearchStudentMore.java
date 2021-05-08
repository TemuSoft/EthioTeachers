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

public class SearchStudentMore extends Activity {

    private String resultHolder[], gradeHolder[], numberHolder[];

    private CheckBox checkBox, checkBox2, checkBox3, checkBox4, checkBox5, checkBox6;
    private CheckBox checkBox7, checkBox8, checkBox10, checkBox11;

    private EditText editText, editText2, editText3, editText4, editText5, editText6, editText7, editText8;

    private TextView textView;

    private Button button;

    private Cursor cursor = null;

    private String sqlSelectCommand = null;

    private String Total100 = "", Total40 = "", Class_Work_10 = "", Home_Work_5 = "", Proj_Assign_10 = "", Group_Work_5 = "", Mid_Exam_10 = "", Final_60 = "";

    private SQLiteDatabase db;

    private String SemesterFirstOrSecond = "First";

    private int size = 13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_search_student_more);

        checkBox = (CheckBox) findViewById(R.id.a);
        checkBox2 = (CheckBox) findViewById(R.id.b);
        checkBox3 = (CheckBox) findViewById(R.id.c);
        checkBox4 = (CheckBox) findViewById(R.id.d);
        checkBox5 = (CheckBox) findViewById(R.id.e);
        checkBox6 = (CheckBox) findViewById(R.id.f);
        checkBox7 = (CheckBox) findViewById(R.id.g);
        checkBox8 = (CheckBox) findViewById(R.id.h);
        checkBox10 = (CheckBox) findViewById(R.id.k); // select all
        checkBox11 = (CheckBox) findViewById(R.id.l); // dis select all

        textView = (TextView)findViewById(R.id.total);

        editText = (EditText) findViewById(R.id.aa);
        editText2 = (EditText) findViewById(R.id.bb);
        editText3 = (EditText) findViewById(R.id.cc);
        editText4 = (EditText) findViewById(R.id.dd);
        editText5 = (EditText) findViewById(R.id.ee);
        editText6 = (EditText) findViewById(R.id.ff);
        editText7 = (EditText) findViewById(R.id.gg);
        editText8 = (EditText) findViewById(R.id.hh);

        button = (Button) findViewById(R.id.search);
        button.setEnabled(false);

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
        editText7.setTextSize(size);
        editText8.setTextSize(size);

        textView.setTextSize(size);

        checkBox.setTextSize(size + 1);
        checkBox2.setTextSize(size + 1);
        checkBox3.setTextSize(size + 1);
        checkBox4.setTextSize(size + 1);
        checkBox5.setTextSize(size + 1);
        checkBox6.setTextSize(size + 1);
        checkBox7.setTextSize(size + 1);
        checkBox8.setTextSize(size + 1);
        checkBox10.setTextSize(size + 1);
        checkBox11.setTextSize(size + 1);


        editText.setEnabled(false);
        editText2.setEnabled(false);
        editText3.setEnabled(false);
        editText4.setEnabled(false);
        editText5.setEnabled(false);
        editText6.setEnabled(false);
        editText7.setEnabled(false);
        editText8.setEnabled(false);

        db = new MainStudentDataDb(this,"STUDENT_DATA.db").getWritableDatabase();

        if (new Utility().semester(this).equals("Second"))
            SemesterFirstOrSecond = "Second";

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Total100 = "";
                Total40 = "";
                Class_Work_10 = "";
                Home_Work_5 = "";
                Proj_Assign_10 = "";
                Group_Work_5 = "";
                Mid_Exam_10 = "";
                Final_60 = "";
                seeCheckedBox();
                sqlSelectCommand = "select Grade,Number, Sname , Fname , GFname ,Subject " +
                        ", Grade , Number , Sex ,Class_Work_10, Home_Work_5, " +
                        "Proj_and_Assign_10 , Group_Work_5 , Mid_Exam_10 , Final_60 ,Total40,Total100" +
                        " from StudentData inner join StudentValue" + SemesterFirstOrSecond + " on "
                        + " StudentValue" + SemesterFirstOrSecond + ".NumberV = StudentData.Number"
                        + " and StudentValue" + SemesterFirstOrSecond + ".GradeV = StudentData.Grade "
                        + Total100 + Total40 + Final_60 + Class_Work_10
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
                                        + "\n" + cursor.getColumnName(14) + "                      :" + cursor.getString(14)
                                        + "\n" + cursor.getColumnName(15) + "                       :" + cursor.getString(15)
                                        + "\n" + cursor.getColumnName(16) + "                     :" + cursor.getString(16) + "\n";
                                gradeHolder[totalCount] = cursor.getString(0);
                                numberHolder[totalCount] = cursor.getString(1);
                                totalCount++;
                            } while (cursor.moveToNext());
                        }

                        Intent intent = new Intent(SearchStudentMore.this, ListViewClass.class);
                        Bundle bundle = new Bundle();
                        bundle.putStringArray("resultHolder", resultHolder);
                        bundle.putStringArray("gradeHolder", gradeHolder);
                        bundle.putStringArray("numberHolder", numberHolder);
                        bundle.putString("operation", "searchMore");
                        intent.putExtras(bundle);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(SearchStudentMore.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                } else
                    Toast.makeText(SearchStudentMore.this, "No smilar data found!", Toast.LENGTH_SHORT).show();
            }
        });

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!checkBox.isChecked()) {
                    checkBox.setChecked(false);
                    editText.setEnabled(false);
                    editText.setText("");
                    if (!checkBox.isChecked() && !checkBox2.isChecked() && !checkBox3.isChecked() && !checkBox4.isChecked() &&
                            !checkBox5.isChecked() && !checkBox6.isChecked() && !checkBox7.isChecked() && !checkBox8.isChecked())
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
                    if (!checkBox.isChecked() && !checkBox2.isChecked() && !checkBox3.isChecked() && !checkBox4.isChecked() &&
                            !checkBox5.isChecked() && !checkBox6.isChecked() && !checkBox7.isChecked() && !checkBox8.isChecked())
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
                    if (!checkBox.isChecked() && !checkBox2.isChecked() && !checkBox3.isChecked() && !checkBox4.isChecked() &&
                            !checkBox5.isChecked() && !checkBox6.isChecked() && !checkBox7.isChecked() && !checkBox8.isChecked())
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
                    if (!checkBox.isChecked() && !checkBox2.isChecked() && !checkBox3.isChecked() && !checkBox4.isChecked() &&
                            !checkBox5.isChecked() && !checkBox6.isChecked() && !checkBox7.isChecked() && !checkBox8.isChecked())
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
                    if (!checkBox.isChecked() && !checkBox2.isChecked() && !checkBox3.isChecked() && !checkBox4.isChecked() &&
                            !checkBox5.isChecked() && !checkBox6.isChecked() && !checkBox7.isChecked() && !checkBox8.isChecked())
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
                    if (!checkBox.isChecked() && !checkBox2.isChecked() && !checkBox3.isChecked() && !checkBox4.isChecked() &&
                            !checkBox5.isChecked() && !checkBox6.isChecked() && !checkBox7.isChecked() && !checkBox8.isChecked())
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
        checkBox7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!checkBox7.isChecked()) {
                    checkBox7.setChecked(false);
                    editText7.setEnabled(false);
                    editText7.setText("");
                    if (!checkBox.isChecked() && !checkBox2.isChecked() && !checkBox3.isChecked() && !checkBox4.isChecked() &&
                            !checkBox5.isChecked() && !checkBox6.isChecked() && !checkBox7.isChecked() && !checkBox8.isChecked())
                        button.setEnabled(false);
                    else
                        button.setEnabled(true);
                } else {
                    checkBox7.setChecked(true);
                    editText7.setEnabled(true);
                    button.setEnabled(true);
                }
                checkBox10.setChecked(false);
                checkBox11.setChecked(false);
            }
        });
        checkBox8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!checkBox8.isChecked()) {
                    checkBox8.setChecked(false);
                    editText8.setEnabled(false);
                    editText8.setText("");
                    if (!checkBox.isChecked() && !checkBox2.isChecked() && !checkBox3.isChecked() && !checkBox4.isChecked() &&
                            !checkBox5.isChecked() && !checkBox6.isChecked() && !checkBox7.isChecked() && !checkBox8.isChecked())
                        button.setEnabled(false);
                    else
                        button.setEnabled(true);
                } else {
                    checkBox8.setChecked(true);
                    editText8.setEnabled(true);
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
                checkBox7.setChecked(true);
                checkBox8.setChecked(true);
                checkBox10.setChecked(true);
                checkBox11.setChecked(false);

                editText.setEnabled(true);
                editText2.setEnabled(true);
                editText3.setEnabled(true);
                editText4.setEnabled(true);
                editText5.setEnabled(true);
                editText6.setEnabled(true);
                editText7.setEnabled(true);
                editText8.setEnabled(true);

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
                checkBox7.setChecked(false);
                checkBox8.setChecked(false);
                checkBox10.setChecked(false);
                checkBox11.setChecked(true);

                editText.setEnabled(false);
                editText2.setEnabled(false);
                editText3.setEnabled(false);
                editText4.setEnabled(false);
                editText5.setEnabled(false);
                editText6.setEnabled(false);
                editText7.setEnabled(false);
                editText8.setEnabled(false);

                editText.setText("");
                editText2.setText("");
                editText3.setText("");
                editText4.setText("");
                editText5.setText("");
                editText6.setText("");
                editText7.setText("");
                editText8.setText("");

                button.setEnabled(false);

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, SearchStudent.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        super.onBackPressed();
    }

    private void seeCheckedBox() {
        if (checkBox.isChecked()) {
            if (!editText.getText().toString().isEmpty()) {
                Total100 = " and Total100 = " + Double.parseDouble(editText.getText().toString());
            } else
                Total100 = " and Total100 = ''";
        }
        if (checkBox2.isChecked()) {
            if (!editText2.getText().toString().isEmpty()) {
                Total40 = " and Total40 =  " + Double.parseDouble(editText2.getText().toString());
            } else
                Total40 = " and Total40 = ''";
        }
        if (checkBox3.isChecked()) {
            if (!editText3.getText().toString().isEmpty()) {
                Final_60 = " and Final_60 = " + Double.parseDouble(editText3.getText().toString());
            } else
                Final_60 = " and Final_60 = ''";
        }
        if (checkBox4.isChecked()) {
            if (!editText4.getText().toString().isEmpty()) {
                Class_Work_10 = " and Class_Work_10 = " + Double.parseDouble(editText4.getText().toString());
            } else
                Class_Work_10 = " and Class_Work_10 = ''";
        }
        if (checkBox5.isChecked()) {
            if (!editText5.getText().toString().isEmpty()) {
                Home_Work_5 = " and Home_Work_5 = " + Double.parseDouble(editText5.getText().toString());
            } else
                Home_Work_5 = " and Home_Work_5 = ''";
        }
        if (checkBox6.isChecked()) {
            if (!editText6.getText().toString().isEmpty()) {
                Proj_Assign_10 = " and Proj_and_Assign_10 = " + Double.parseDouble(editText6.getText().toString());
            } else
                Proj_Assign_10 = " and Proj_and_Assign_10 = ''";
        }
        if (checkBox7.isChecked()) {
            if (!editText7.getText().toString().isEmpty()) {
                Group_Work_5 = " and Group_Work_5 = " + Double.parseDouble(editText7.getText().toString());
            } else
                Group_Work_5 = " and Group_Work_5 = ''";
        }
        if (checkBox8.isChecked()) {
            if (!editText8.getText().toString().isEmpty()) {
                Mid_Exam_10 = " and Mid_Exam_10 = " + Double.parseDouble(editText8.getText().toString());
            } else
                Mid_Exam_10 = " and Mid_Exam_10 = ''";
        }
    }
}