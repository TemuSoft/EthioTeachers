package ethioteacher.dejen.ethioteachers;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class AddStudent extends Activity {

    private EditText editText, editText2, editText3, editText6, editText8;
    private Spinner semester, spinner2, year, month, day, grade, section;
    private RadioButton radioButton, radioButton2;
    private Button button, button2;

    private TextView textView, textView2, textView3, textView4, textView5, textView6, textView7, textView8, textView9, textView10, notification;
    private SQLiteDatabase db;
    private String Sname = "", Fname = "", GFname = "", Semester = "", Subject = "", Phone_number = "", Grade = "", Sex = "", Birth_Date = "";
    private int Number;
    private String Class_Work_10 = "", Home_Work_5 = "", Proj_Assign_10 = "", Group_Work_5 = "", Mid_Exam_10 = "", Final_60 = "";
    private int detailCheck = 0;
    private String semesterHolderF[] = {"First"};
    private String semesterHolderS[] = {"Second"};
    private File file = null;

    private Utility utility;
    private int size = 13;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_student);

        file = new File(this.getFilesDir(), "/Teme/");
        if (!file.exists())
            file.mkdirs();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        try {
            size = Integer.parseInt(preferences.getString("size", "13"));
        } catch (Exception e) {

        }

        utility = new Utility();
        try {
            detailCheck = getIntent().getIntExtra("detailCheck", 0);
            Class_Work_10 = getIntent().getStringExtra("Class_Work_10");
            Home_Work_5 = getIntent().getStringExtra("Home_Work_5");
            Proj_Assign_10 = getIntent().getStringExtra("Proj_Assign_10");
            Group_Work_5 = getIntent().getStringExtra("Group_Work_5");
            Mid_Exam_10 = getIntent().getStringExtra("Mid_Exam_10");
            Final_60 = getIntent().getStringExtra("Final_60");
        } catch (Exception e) {

        }

        textView = (TextView) findViewById(R.id.textView3);
        textView2 = (TextView) findViewById(R.id.textView4);
        textView3 = (TextView) findViewById(R.id.textView5);
        textView4 = (TextView) findViewById(R.id.textView6);
        textView5 = (TextView) findViewById(R.id.textView7);
        textView6 = (TextView) findViewById(R.id.textView8);
        textView7 = (TextView) findViewById(R.id.textView9);
        textView8 = (TextView) findViewById(R.id.textView10);
        textView9 = (TextView) findViewById(R.id.textView11);
        textView10 = (TextView) findViewById(R.id.textView12);
        notification = (TextView) findViewById(R.id.total);

        editText = (EditText) findViewById(R.id.editText);
        editText2 = (EditText) findViewById(R.id.editText2);
        editText3 = (EditText) findViewById(R.id.editText3);
        editText6 = (EditText) findViewById(R.id.editText6);
        editText8 = (EditText) findViewById(R.id.editText8);

        editText.setAllCaps(true);
        editText2.setAllCaps(true);
        editText3.setAllCaps(true);
        editText6.setAllCaps(true);
        editText8.setAllCaps(true);

        notification.setTextSize(size + 4);

        textView.setTextSize(size + 1);
        textView2.setTextSize(size + 1);
        textView3.setTextSize(size + 1);
        textView4.setTextSize(size + 1);
        textView5.setTextSize(size + 1);
        textView6.setTextSize(size + 1);
        textView7.setTextSize(size + 1);
        textView8.setTextSize(size + 1);
        textView9.setTextSize(size + 1);
        textView10.setTextSize(size + 1);

        editText.setTextSize(size);
        editText2.setTextSize(size);
        editText3.setTextSize(size);
        editText6.setTextSize(size);
        editText8.setTextSize(size);

        semester = (Spinner) findViewById(R.id.editText4);
        spinner2 = (Spinner) findViewById(R.id.editText5);
        grade = (Spinner) findViewById(R.id.spinner3);
        section = (Spinner) findViewById(R.id.spinner2);
        year = (Spinner) findViewById(R.id.spinnerYear);
        month = (Spinner) findViewById(R.id.spinnerMonth);
        day = (Spinner) findViewById(R.id.spinnerDay);

        radioButton = (RadioButton) findViewById(R.id.radioButtonFemale);
        radioButton2 = (RadioButton) findViewById(R.id.radioButtonMale);

        button = (Button) findViewById(R.id.button);//add datail
        button2 = (Button) findViewById(R.id.button2);//save

        String yearHolder[] = {"1975", "1976", "1977", "1978", "1979", "1980", "1981", "1982", "1983", "1984", "1985", "1986"
                , "1987", "1988", "1989", "1990", "1991", "1992", "1993", "1994", "1995", "1996", "1997", "1998", "1999", "2000"};

        String monthHolder[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};

        String gradeHolder[] = {"12","11","10","9","8","7","6","5"};
        String sectionHolder[] = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

        String datHolder[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18"
                , "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};

        String subjectHolder[] = {"English", "Maths natural", "Chemistry", "Biology", "Physics", "Civics"
                , "HP", "ICT", "አማርኛ", "Economics", "Business", "History", "Geography", "Drawing", "Maths scocial"};


        if (utility.semester(this).equals("Second")) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, semesterHolderS) {
                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    TextView view = (TextView) super.getView(position, convertView, parent);
                    view.setTextSize(size);
                    return view;
                }
            };
            semester.setAdapter(adapter);
        } else {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, semesterHolderF) {
                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    TextView view = (TextView) super.getView(position, convertView, parent);
                    view.setTextSize(size);
                    return view;
                }
            };
            semester.setAdapter(adapter);
        }

        ArrayAdapter<String> adapter6 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sectionHolder) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setTextSize(size);
                return view;
            }
        };
        section.setAdapter(adapter6);

        ArrayAdapter<String> adapter7 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, gradeHolder) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setTextSize(size);
                return view;
            }
        };
        grade.setAdapter(adapter7);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, subjectHolder) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setTextSize(size);
                return view;
            }
        };
        spinner2.setAdapter(adapter2);

        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, yearHolder) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setTextSize(size);
                return view;
            }
        };
        year.setAdapter(adapter3);

        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, monthHolder) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setTextSize(size);
                return view;
            }
        };
        month.setAdapter(adapter4);

        ArrayAdapter<String> adapter5 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, datHolder) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setTextSize(size);
                return view;
            }
        };
        day.setAdapter(adapter5);

        db = new MainStudentDataDb(this,"STUDENT_DATA.db").getWritableDatabase();

        radioButton.setTextSize(size);
        radioButton2.setTextSize(size);

        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButton2.setChecked(false);
                radioButton.setChecked(true);
            }
        });
        radioButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButton2.setChecked(true);
                radioButton.setChecked(false);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add datail mark
                Intent intent = new Intent(AddStudent.this, AddStudentDetailMark.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!editText3.getText().toString().isEmpty())
                    GFname = editText3.getText().toString();
                Subject = spinner2.getSelectedItem().toString();
                if (!editText6.getText().toString().isEmpty())
                    Phone_number = editText6.getText().toString();

                if (radioButton.isChecked())
                    Sex = "F";
                else
                    Sex = "M";
                try {
                    if (utility.checkPhoneNumber(editText6.getText().toString()) == true)
                        checkExistOrNot();
                    else
                        Toast.makeText(AddStudent.this, "Invalid phone Number ", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {

                }
            }
        });
    }

    private void checkExistOrNot() {
        Cursor cursor = db.rawQuery("select * from StudentData where Grade = '"
                + grade.getSelectedItem().toString() + section.getSelectedItem().toString()
                + "' and Number = " + Integer.parseInt(editText8.getText().toString()) + "", null);

        if (cursor.moveToFirst()) {
            Toast.makeText(AddStudent.this, "Number and Grade aleredy exists", Toast.LENGTH_SHORT).show();
            editText8.setText("");
        } else
            registerStudent();
    }

    private void registerStudent() {
        if (editText.getText().toString().isEmpty() ||
                editText2.getText().toString().isEmpty() ||
                editText8.getText().toString().isEmpty())
            Toast.makeText(AddStudent.this, "please write student info!", Toast.LENGTH_SHORT).show();
        else if (Integer.parseInt(editText8.getText().toString()) > 70 || Integer.parseInt(editText8.getText().toString()) < 1)
            Toast.makeText(AddStudent.this, "Invalid Number", Toast.LENGTH_SHORT).show();
        else {
            Sname = editText.getText().toString();
            Fname = editText2.getText().toString();
            Semester = semester.getSelectedItem().toString();
            Phone_number = editText6.getText().toString();
            Grade = grade.getSelectedItem().toString() + section.getSelectedItem().toString();
            Number = Integer.parseInt(editText8.getText().toString());

            String updateValue = "Not update in the...";

            Birth_Date = year.getSelectedItem().toString() + "-" + month.getSelectedItem().toString()
                    + "-" + day.getSelectedItem().toString();

            if (detailCheck == 0) {
                try {
                    db.execSQL("insert into StudentData values('" + Sname + "','" + Fname + "','"
                            + GFname + "','" + Semester + "','" + Subject + "','" + Phone_number + "','"
                            + Grade + "','" + Number + "','" + Birth_Date + "','" + Sex + "','"
                            + utility.CurrentTime(file, AddStudent.this) + "','" + updateValue + "','" +
                            preferences.getString("schoolname", "___") + "')");
                    db.execSQL("insert into StudentValueFirst values('','','','','','','','','','" + Number + "','" + Grade + "')");
                    db.execSQL("insert into StudentValueSecond values('','','','','','','','','','" + Number + "','" + Grade + "')");
                    Toast.makeText(AddStudent.this, "Student register successfull!", Toast.LENGTH_SHORT).show();
                    editText.setText("");
                    editText2.setText("");
                    editText3.setText("");
                    editText6.setText("");
                    editText8.setText("");
                    detailCheck = 0;
                    Birth_Date = "";
                    Sname = "";
                    Fname = "";
                    GFname = "";
                    Semester = "";
                    Subject = "";
                    Phone_number = "";
                    Grade = "";
                    Sex = "";
                    if (preferences.getBoolean("add", false) == true)
                        utility.backUp(AddStudent.this);
                } catch (Exception w) {

                }
            } else if (detailCheck == 1) {
                try {
                    db.execSQL("insert into StudentData values('" + Sname + "','" + Fname + "','"
                            + GFname + "','" + Semester + "','" + Subject + "','" + Phone_number + "','"
                            + Grade + "','" + Number + "','" + Birth_Date + "','" + Sex + "','"
                            + utility.CurrentTime(file, AddStudent.this) + "','" + updateValue + "','"
                            + preferences.getString("schoolname", "___") + "')");
                    Toast.makeText(AddStudent.this, "Student register successfull!", Toast.LENGTH_SHORT).show();

                    if (semester.getSelectedItem().toString().equals("First")) {
                        db.execSQL("insert into StudentValueFirst values('" + Class_Work_10 + "','" + Home_Work_5
                                + "','" + Proj_Assign_10 + "','" + Group_Work_5 + "','" + Mid_Exam_10 + "','"
                                + Final_60 + "','','','','" + Number + "','" + Grade + "')");
                        db.execSQL("insert into StudentValueSecond values('','','','','','','','','','" + Number + "','" + Grade + "')");
                        Toast.makeText(AddStudent.this, "Student result add sucessfull!", Toast.LENGTH_SHORT).show();
                        detailCheck = 0;
                    } else if (semester.getSelectedItem().toString().equals("Second")) {
                        db.execSQL("insert into StudentValueSecond values('" + Class_Work_10 + "','" + Home_Work_5
                                + "','" + Proj_Assign_10 + "','" + Group_Work_5 + "','" + Mid_Exam_10 + "','"
                                + Final_60 + "','','','','" + Number + "','" + Grade + "')");
                        db.execSQL("insert into StudentValueFirst values('','','','','','','','','','" + Number + "','" + Grade + "')");
                        Toast.makeText(AddStudent.this, "Student result add sucessfull!", Toast.LENGTH_SHORT).show();
                        detailCheck = 0;
                        editText.setText("");
                        editText2.setText("");
                        editText3.setText("");
                        editText6.setText("");
                        editText8.setText("");
                        detailCheck = 0;
                        Birth_Date = "";
                        Sname = "";
                        Fname = "";
                        GFname = "";
                        Semester = "";
                        Subject = "";
                        Phone_number = "";
                        Grade = "";
                        Sex = "";
                        if (preferences.getBoolean("add", false) == true)
                            utility.backUp(AddStudent.this);
                    }
                } catch (Exception e) {

                }
            }
        }
    }

    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        super.onBackPressed();
    }
}
