package ethioteacher.dejen.ethioteachers;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SearchStudent extends Activity {

    private String resultHolder[], gradeHolder[], numberHolder[];

    private CheckBox checkBox, checkBox2, checkBox3, checkBox4, checkBox5;
    private CheckBox checkBox6, checkBox7, checkBox8, checkBox9, checkBox10, checkBox11, checkBox12;

    private EditText editText, editText2, editText3, editText4, editText5;
    private EditText editText6, editText7, editText8, editText9, editText10;

    private TextView textView;

    private Button button, button2;

    private Cursor cursor;

    private SQLiteDatabase db;

    private String SearchBySname = "", SearchByFname = "", SearchByGFname = "", SearchBySemester = "";
    private String SearchBySubject = "", SearchByPhone_number = "", SearchByGrade = "", SearchByNumber = "", SearchByBirth_Date = "", SearchBySex = "";

    private String sqlSelectCommand = null;

    private String SemesterFirstOrSecond = "First";

    private int size = 13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_search_student);

        checkBox = (CheckBox) findViewById(R.id.a);
        checkBox2 = (CheckBox) findViewById(R.id.b);
        checkBox3 = (CheckBox) findViewById(R.id.c);
        checkBox4 = (CheckBox) findViewById(R.id.d);
        checkBox5 = (CheckBox) findViewById(R.id.e);
        checkBox6 = (CheckBox) findViewById(R.id.f);
        checkBox7 = (CheckBox) findViewById(R.id.g);
        checkBox8 = (CheckBox) findViewById(R.id.h);
        checkBox9 = (CheckBox) findViewById(R.id.i);
        checkBox10 = (CheckBox) findViewById(R.id.j);
        checkBox11 = (CheckBox) findViewById(R.id.k);
        checkBox12 = (CheckBox) findViewById(R.id.l);

        textView = (TextView)findViewById(R.id.total);

        editText = (EditText) findViewById(R.id.aa);
        editText2 = (EditText) findViewById(R.id.bb);
        editText3 = (EditText) findViewById(R.id.cc);
        editText4 = (EditText) findViewById(R.id.dd);
        editText5 = (EditText) findViewById(R.id.ee);
        editText6 = (EditText) findViewById(R.id.ff);
        editText7 = (EditText) findViewById(R.id.gg);
        editText8 = (EditText) findViewById(R.id.hh);
        editText9 = (EditText) findViewById(R.id.editText18);
        editText10 = (EditText) findViewById(R.id.jj);

        editText.setAllCaps(true);
        editText2.setAllCaps(true);
        editText3.setAllCaps(true);
        editText4.setAllCaps(true);
        editText5.setAllCaps(true);
        editText6.setAllCaps(true);
        editText7.setAllCaps(true);
        editText8.setAllCaps(true);
        editText9.setAllCaps(true);
        editText10.setAllCaps(true);

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
        editText9.setTextSize(size);
        editText10.setTextSize(size);

        textView.setTextSize(size);

        checkBox.setTextSize(size + 1);
        checkBox2.setTextSize(size + 1);
        checkBox3.setTextSize(size + 1);
        checkBox4.setTextSize(size + 1);
        checkBox5.setTextSize(size + 1);
        checkBox6.setTextSize(size + 1);
        checkBox7.setTextSize(size + 1);
        checkBox8.setTextSize(size + 1);
        checkBox9.setTextSize(size + 1);
        checkBox10.setTextSize(size + 1);
        checkBox11.setTextSize(size + 1);
        checkBox12.setTextSize(size + 1);


        button = (Button) findViewById(R.id.search);
        button2 = (Button) findViewById(R.id.more);

        // by default all edit text must be disabled
        editText.setEnabled(false);
        editText2.setEnabled(false);
        editText3.setEnabled(false);
        editText4.setEnabled(false);
        editText5.setEnabled(false);
        editText6.setEnabled(false);
        editText7.setEnabled(false);
        editText8.setEnabled(false);
        editText9.setEnabled(false);
        editText10.setEnabled(false);

        button.setEnabled(false);

        db = new MainStudentDataDb(this,"STUDENT_DATA.db").getWritableDatabase();

        if (new Utility().semester(this).equals("Second"))
            SemesterFirstOrSecond = "Second";

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchBySname = "";
                SearchByFname = "";
                SearchByGFname = "";
                SearchBySemester = "";
                SearchBySubject = "";
                SearchByPhone_number = "";
                SearchByGrade = "";
                SearchByNumber = "";
                SearchByBirth_Date = "";
                SearchBySex = "";

                seeCheckedBox();

                try {
                    sqlSelectCommand = "select * from StudentData inner join StudentValue" + SemesterFirstOrSecond + " on "
                            + " StudentValue" + SemesterFirstOrSecond + ".NumberV = StudentData.Number"
                            + " and StudentValue" + SemesterFirstOrSecond + ".GradeV = StudentData.Grade "
                            + SearchBySname + SearchByFname + SearchByGFname + SearchBySemester
                            + SearchBySubject + SearchByPhone_number + SearchByGrade + SearchByNumber + SearchByBirth_Date
                            + SearchBySex;

                    cursor = db.rawQuery(sqlSelectCommand, null);
                    if (cursor.moveToFirst()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SearchStudent.this);
                        builder.setMessage("Show reult?");
                        builder.setNegativeButton("Detail", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                sqlSelectCommand = "select Grade,Number, Sname , Fname , GFname , Semester , Subject " +
                                        ", Phone_number, Grade , Number , Birth_Date , Sex ,Class_Work_10, Home_Work_5, " +
                                        "Proj_and_Assign_10 , Group_Work_5 , Mid_Exam_10 , Final_60 ,Date_Register," +
                                        "Date_last_update from StudentData inner join StudentValue" + SemesterFirstOrSecond + " on "
                                        + " StudentValue" + SemesterFirstOrSecond + ".NumberV = StudentData.Number"
                                        + " and StudentValue" + SemesterFirstOrSecond + ".GradeV = StudentData.Grade "
                                        + SearchBySname + SearchByFname + SearchByGFname + SearchBySemester
                                        + SearchBySubject + SearchByPhone_number + SearchByGrade + SearchByNumber + SearchByBirth_Date
                                        + SearchBySex;
                                try {
                                    int totalCount = 0;
                                    cursor = db.rawQuery(sqlSelectCommand, null);
                                    resultHolder = new String[cursor.getCount()];
                                    gradeHolder = new String[cursor.getCount()];
                                    numberHolder = new String[cursor.getCount()];
                                    if (cursor.moveToFirst()) {
                                        do {
                                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
                                            Date date = new Date();
                                            String a = String.valueOf(cursor.getString(10).charAt(0));
                                            String b = String.valueOf(cursor.getString(10).charAt(1));
                                            String c = String.valueOf(cursor.getString(10).charAt(2));
                                            String d = String.valueOf(cursor.getString(10).charAt(3));
                                            int age = Integer.parseInt(a + b + c + d);

                                            if (SemesterFirstOrSecond.equals("First"))
                                                age = Integer.parseInt(dateFormat.format(date)) - age - 7;
                                            else
                                                age = Integer.parseInt(dateFormat.format(date)) - age - 8;

                                            resultHolder[totalCount] = "Name  : " + cursor.getString(2) + " " + cursor.getString(3) + " " + cursor.getString(4)
                                                    + "\nSemester :" + cursor.getString(5) + " , Subject :" + cursor.getString(6)
                                                    + "\nP.Number :" + cursor.getString(7) + "  Grade  :" + cursor.getString(8)
                                                    + "\nClass room No :" + cursor.getString(9)
                                                    + "\nAge :" + age + "     Sex :" + cursor.getString(11)
                                                    + "\n\n" + cursor.getColumnName(12) + "     :" + cursor.getString(12)
                                                    + "\n" + cursor.getColumnName(13) + "     :" + cursor.getString(13)
                                                    + "\n" + cursor.getColumnName(14) + "    :" + cursor.getString(14)
                                                    + "\n" + cursor.getColumnName(15) + "     :" + cursor.getString(15)
                                                    + "\n" + cursor.getColumnName(16) + "    :" + cursor.getString(16)
                                                    + "\n" + cursor.getColumnName(17) + "    :" + cursor.getString(17)
                                                    + "\n" + cursor.getColumnName(18) + "     :" + cursor.getString(18)
                                                    + "\n" + cursor.getColumnName(19) + "     :" + cursor.getString(19) + "\n";
                                            gradeHolder[totalCount] = cursor.getString(0);
                                            numberHolder[totalCount] = cursor.getString(1);
                                            totalCount++;
                                        } while (cursor.moveToNext());
                                    }

                                    Intent intent = new Intent(SearchStudent.this, ListViewClass.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putStringArray("resultHolder", resultHolder);
                                    bundle.putStringArray("gradeHolder", gradeHolder);
                                    bundle.putStringArray("numberHolder", numberHolder);
                                    bundle.putString("operation", "search");
                                    intent.putExtras(bundle);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    dialog.dismiss();
                                } catch (Exception e) {
                                }
                            }
                        });
                        builder.setPositiveButton("Checked only", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sqlSelectCommand = "select Grade,Number, Sname , Fname , GFname , Grade , Number , Semester , Subject " +
                                        ", Phone_number, Birth_Date , Sex  from StudentData inner join StudentValue"
                                        + SemesterFirstOrSecond + " on "
                                        + " StudentValue" + SemesterFirstOrSecond + ".NumberV = StudentData.Number"
                                        + " and StudentValue" + SemesterFirstOrSecond + ".GradeV = StudentData.Grade "
                                        + SearchBySname + SearchByFname + SearchByGFname + SearchBySemester
                                        + SearchBySubject + SearchByPhone_number + SearchByGrade + SearchByNumber + SearchByBirth_Date
                                        + SearchBySex;
                                try {
                                    int totalCount = 0;
                                    cursor = db.rawQuery(sqlSelectCommand, null);
                                    resultHolder = new String[cursor.getCount()];
                                    gradeHolder = new String[cursor.getCount()];
                                    numberHolder = new String[cursor.getCount()];
                                    if (cursor.moveToFirst()) {
                                        do {
                                            resultHolder[totalCount] = "Name :" + cursor.getString(2) + " " + cursor.getString(3) + " " + cursor.getString(4)
                                                    + "\nGrade :" + cursor.getString(5) + "\t\tNumber :" + cursor.getString(6) + "\t\tSex :" + cursor.getString(11)
                                                    + "\n" + showSemester() + " " + checkAgeWantOrNot() + showSubject()
                                                    + " " + showPhone_Num() + "\n";
                                            gradeHolder[totalCount] = cursor.getString(0);
                                            numberHolder[totalCount] = cursor.getString(1);
                                            totalCount++;
                                        } while (cursor.moveToNext());
                                    }
                                    Intent intent = new Intent(SearchStudent.this, ListViewClass.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putStringArray("resultHolder", resultHolder);
                                    bundle.putStringArray("gradeHolder", gradeHolder);
                                    bundle.putStringArray("numberHolder", numberHolder);
                                    bundle.putString("operation", "search");
                                    intent.putExtras(bundle);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    dialog.cancel();
                                } catch (Exception e) {

                                }
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    } else
                        Toast.makeText(SearchStudent.this, "no similar data found", Toast.LENGTH_SHORT).show();
                } catch (Exception t) {

                }
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SearchStudent.this, SearchStudentMore.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
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
                            && !checkBox5.isChecked() && !checkBox6.isChecked() && !checkBox7.isChecked() && !checkBox8.isChecked()
                            && !checkBox9.isChecked() && !checkBox10.isChecked())
                        button.setEnabled(false);
                    else
                        button.setEnabled(true);
                } else {
                    checkBox.setChecked(true);
                    editText.setEnabled(true);
                    button.setEnabled(true);
                }
                checkBox11.setChecked(false);
                checkBox12.setChecked(false);
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
                            && !checkBox5.isChecked() && !checkBox6.isChecked() && !checkBox7.isChecked() && !checkBox8.isChecked()
                            && !checkBox9.isChecked() && !checkBox10.isChecked())
                        button.setEnabled(false);
                    else
                        button.setEnabled(true);
                } else {
                    checkBox2.setChecked(true);
                    editText2.setEnabled(true);
                    button.setEnabled(true);
                }
                checkBox11.setChecked(false);
                checkBox12.setChecked(false);
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
                            && !checkBox5.isChecked() && !checkBox6.isChecked() && !checkBox7.isChecked() && !checkBox8.isChecked()
                            && !checkBox9.isChecked() && !checkBox10.isChecked())
                        button.setEnabled(false);
                    else
                        button.setEnabled(true);
                } else {
                    checkBox3.setChecked(true);
                    editText3.setEnabled(true);
                    button.setEnabled(true);
                }
                checkBox11.setChecked(false);
                checkBox12.setChecked(false);
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
                            && !checkBox5.isChecked() && !checkBox6.isChecked() && !checkBox7.isChecked() && !checkBox8.isChecked()
                            && !checkBox9.isChecked() && !checkBox10.isChecked())
                        button.setEnabled(false);
                    else
                        button.setEnabled(true);
                } else {
                    checkBox4.setChecked(true);
                    editText4.setEnabled(true);
                    button.setEnabled(true);
                }
                checkBox11.setChecked(false);
                checkBox12.setChecked(false);
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
                            && !checkBox5.isChecked() && !checkBox6.isChecked() && !checkBox7.isChecked() && !checkBox8.isChecked()
                            && !checkBox9.isChecked() && !checkBox10.isChecked())
                        button.setEnabled(false);
                    else
                        button.setEnabled(true);
                } else {
                    checkBox5.setChecked(true);
                    editText5.setEnabled(true);
                    button.setEnabled(true);
                }
                checkBox11.setChecked(false);
                checkBox12.setChecked(false);
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
                            && !checkBox5.isChecked() && !checkBox6.isChecked() && !checkBox7.isChecked() && !checkBox8.isChecked()
                            && !checkBox9.isChecked() && !checkBox10.isChecked())
                        button.setEnabled(false);
                    else
                        button.setEnabled(true);
                } else {
                    checkBox6.setChecked(true);
                    editText6.setEnabled(true);
                    button.setEnabled(true);
                }
                checkBox11.setChecked(false);
                checkBox12.setChecked(false);
            }
        });
        checkBox7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!checkBox7.isChecked()) {
                    checkBox7.setChecked(false);
                    editText7.setEnabled(false);
                    editText7.setText("");
                    if (!checkBox.isChecked() && !checkBox2.isChecked() && !checkBox3.isChecked() && !checkBox4.isChecked()
                            && !checkBox5.isChecked() && !checkBox6.isChecked() && !checkBox7.isChecked() && !checkBox8.isChecked()
                            && !checkBox9.isChecked() && !checkBox10.isChecked())
                        button.setEnabled(false);
                    else
                        button.setEnabled(true);
                } else {
                    checkBox7.setChecked(true);
                    editText7.setEnabled(true);
                    button.setEnabled(true);
                }
                checkBox11.setChecked(false);
                checkBox12.setChecked(false);
            }
        });
        checkBox8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!checkBox8.isChecked()) {
                    checkBox8.setChecked(false);
                    editText8.setEnabled(false);
                    editText8.setText("");
                    if (!checkBox.isChecked() && !checkBox2.isChecked() && !checkBox3.isChecked() && !checkBox4.isChecked()
                            && !checkBox5.isChecked() && !checkBox6.isChecked() && !checkBox7.isChecked() && !checkBox8.isChecked()
                            && !checkBox9.isChecked() && !checkBox10.isChecked())
                        button.setEnabled(false);
                    else
                        button.setEnabled(true);
                } else {
                    checkBox8.setChecked(true);
                    editText8.setEnabled(true);
                    button.setEnabled(true);
                }
                checkBox11.setChecked(false);
                checkBox12.setChecked(false);
            }
        });
        checkBox9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!checkBox9.isChecked()) {
                    checkBox9.setChecked(false);
                    editText9.setEnabled(false);
                    editText9.setText("");
                    if (!checkBox.isChecked() && !checkBox2.isChecked() && !checkBox3.isChecked() && !checkBox4.isChecked()
                            && !checkBox5.isChecked() && !checkBox6.isChecked() && !checkBox7.isChecked() && !checkBox8.isChecked()
                            && !checkBox9.isChecked() && !checkBox10.isChecked())
                        button.setEnabled(false);
                    else
                        button.setEnabled(true);
                } else {
                    checkBox9.setChecked(true);
                    editText9.setEnabled(true);
                    button.setEnabled(true);
                }
                checkBox11.setChecked(false);
                checkBox12.setChecked(false);
            }
        });
        checkBox10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!checkBox10.isChecked()) {
                    checkBox10.setChecked(false);
                    editText10.setEnabled(false);
                    editText10.setText("");
                    if (!checkBox.isChecked() && !checkBox2.isChecked() && !checkBox3.isChecked() && !checkBox4.isChecked()
                            && !checkBox5.isChecked() && !checkBox6.isChecked() && !checkBox7.isChecked() && !checkBox8.isChecked()
                            && !checkBox9.isChecked() && !checkBox10.isChecked())
                        button.setEnabled(false);
                    else
                        button.setEnabled(true);
                } else {
                    checkBox10.setChecked(true);
                    editText10.setEnabled(true);
                    button.setEnabled(true);
                }
                checkBox11.setChecked(false);
                checkBox12.setChecked(false);
            }
        });

        checkBox11.setOnClickListener(new View.OnClickListener() {
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
                checkBox9.setChecked(true);
                checkBox10.setChecked(true);
                checkBox11.setChecked(true);
                checkBox12.setChecked(false);

                editText.setEnabled(true);
                editText2.setEnabled(true);
                editText3.setEnabled(true);
                editText4.setEnabled(true);
                editText5.setEnabled(true);
                editText6.setEnabled(true);
                editText7.setEnabled(true);
                editText8.setEnabled(true);
                editText9.setEnabled(true);
                editText10.setEnabled(true);

                button.setEnabled(true);

            }
        });
        checkBox12.setOnClickListener(new View.OnClickListener() {
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
                checkBox9.setChecked(false);
                checkBox10.setChecked(false);
                checkBox11.setChecked(false);
                checkBox12.setChecked(true);

                editText.setEnabled(false);
                editText2.setEnabled(false);
                editText3.setEnabled(false);
                editText4.setEnabled(false);
                editText5.setEnabled(false);
                editText6.setEnabled(false);
                editText7.setEnabled(false);
                editText8.setEnabled(false);
                editText9.setEnabled(false);
                editText10.setEnabled(false);

                editText.setText("");
                editText2.setText("");
                editText3.setText("");
                editText4.setText("");
                editText5.setText("");
                editText6.setText("");
                editText7.setText("");
                editText8.setText("");
                editText9.setText("");
                editText10.setText("");

                button.setEnabled(false);

            }
        });
    }

    private String showPhone_Num() {
        String showPhone = "";
        if (checkBox6.isChecked()) {
            showPhone = cursor.getColumnName(9) + " :" + cursor.getString(9);
        }
        return showPhone;
    }

    private String showSubject() {
        String showSubject = "";
        if (checkBox5.isChecked()) {
            showSubject = cursor.getColumnName(8) + " :" + cursor.getString(8);
        }
        return showSubject;
    }

    private String showSemester() {
        String showSemester = "";
        if (checkBox4.isChecked()) {
            showSemester = cursor.getColumnName(7) + " :" + cursor.getString(7);
        }
        return showSemester;
    }

    private String checkAgeWantOrNot() {
        String ageOfStudent = "";
        if (checkBox9.isChecked()) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
            Date date = new Date();
            String a = String.valueOf(cursor.getString(10).charAt(0));
            String b = String.valueOf(cursor.getString(10).charAt(1));
            String c = String.valueOf(cursor.getString(10).charAt(2));
            String d = String.valueOf(cursor.getString(10).charAt(3));
            int age = Integer.parseInt(a + b + c + d);
            if (SemesterFirstOrSecond.equals("First"))
                age = Integer.parseInt(dateFormat.format(date)) - age - 7;
            else
                age = Integer.parseInt(dateFormat.format(date)) - age - 8;
            ageOfStudent = "Age :" + String.valueOf(age);
        }
        return ageOfStudent;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        super.onBackPressed();
    }

    private void seeCheckedBox() {
        if (checkBox.isChecked()) {
            SearchBySname = " and StudentData.Sname = '" + editText.getText().toString() + "' ";
        }
        if (checkBox2.isChecked()) {
            SearchByFname = " and StudentData.Fname = '" + editText2.getText().toString() + "' ";
        }
        if (checkBox3.isChecked()) {
            SearchByGFname = " and StudentData.GFname = '" + editText3.getText().toString() + "' ";
        }
        if (checkBox4.isChecked()) {
            SearchBySemester = " and StudentData.Semester = '" + editText4.getText().toString() + "' ";
        }
        if (checkBox5.isChecked()) {
            SearchBySubject = " and StudentData.Subject = '" + editText5.getText().toString() + "' ";
        }
        if (checkBox6.isChecked()) {
            SearchByPhone_number = " and StudentData.Phone_number = '" + editText6.getText().toString() + "' ";
        }
        if (checkBox7.isChecked()) {
            SearchByGrade = " and StudentData.Grade = '" + editText7.getText().toString() + "' ";
        }
        if (checkBox8.isChecked()) {
            if (!editText8.getText().toString().isEmpty())
                SearchByNumber = " and StudentData.Number = " + Integer.parseInt(editText8.getText().toString()) + " ";
            else
                SearchByNumber = " and StudentData.Number =''";
        }
        if (checkBox9.isChecked()) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
            Date date = new Date();

            try {
                if (Integer.parseInt(editText9.getText().toString()) > 12) {
                    int age;
                    if (SemesterFirstOrSecond.equals("First"))
                        age = Integer.parseInt(dateFormat.format(date)) - (Integer.parseInt(editText9.getText().toString()) + 7);
                    else
                        age = Integer.parseInt(dateFormat.format(date)) - (Integer.parseInt(editText9.getText().toString()) + 8);
                    SearchByBirth_Date = " and StudentData.Birth_Date like '" + age + "%' ";
                } else {
                    SearchByBirth_Date = " and StudentData.Birth_Daten like 'b%' ";
                    Toast.makeText(this, "Age must greater than 12!", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception r) {
                SearchByBirth_Date = " and StudentData.Birth_Daten like 'b%' ";
            }
        }

        if (checkBox10.isChecked()) {
            SearchBySex = "and StudentData.Sex = '" + editText10.getText().toString() + "' ";
        }
    }
}
