package ethioteacher.dejen.ethioteachers;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdateStudentEditable extends Activity {

    private String UGrade = "";
    private int UNumber;

    private Spinner spinner;

    private TextView textView, textView2, textView3, textView5, textView6, textView9, textView10;
    private EditText editText, editText2, editText3, editText6, editText9, editText10;
    private String Sname = "", Fname = "", GFname = "", Semester = "", Subject = "", Phone_number = "", Sex = "", Birth_Date = "";
    private String updateType = "";
    private Button button;
    private SQLiteDatabase db;
    private String updateRequest[];
    private Cursor cursor;
    private String SemesterFirstOrSecond = "First";
    private String gradeHolder[] = null;
    private String numberHolder[] = null;
    private int totalUpdate;
    private int totalSucess = 0;
    private File file = null;
    private int size = 13;
    private SharedPreferences preferences;

    private Utility utility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_update_student_editable);

        file = new File(this.getFilesDir(), "/Teme/");
        if (!file.exists())
            file.mkdirs();
        spinner = (Spinner) findViewById(R.id.spinner);
        textView = (TextView) findViewById(R.id.textView37);
        textView2 = (TextView) findViewById(R.id.textView38);
        textView3 = (TextView) findViewById(R.id.textView39);
        textView5 = (TextView) findViewById(R.id.textView41);
        textView6 = (TextView) findViewById(R.id.textView42);
        textView9 = (TextView) findViewById(R.id.textView45);
        textView10 = (TextView) findViewById(R.id.textView46);

        utility = new Utility();

        try {
            updateRequest = getIntent().getStringArrayExtra("updateRequest");
        } catch (Exception t) {

        }
        try {
            gradeHolder = getIntent().getStringArrayExtra("gradeHolder");
            numberHolder = getIntent().getStringArrayExtra("numberHolder");
        } catch (Exception t) {

        }
        try {
            totalUpdate = getIntent().getIntExtra("totalUpdate", 0);
        } catch (Exception r) {

        }

        button = (Button) findViewById(R.id.button8);

        editText = (EditText) findViewById(R.id.editText40);
        editText2 = (EditText) findViewById(R.id.editText41);
        editText3 = (EditText) findViewById(R.id.editText42);
        editText6 = (EditText) findViewById(R.id.editText45);
        editText9 = (EditText) findViewById(R.id.editText48);
        editText10 = (EditText) findViewById(R.id.editText49);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        try {
            size = Integer.parseInt(preferences.getString("size", "13"));
        } catch (Exception e) {

        }
        editText.setTextSize(size);
        editText2.setTextSize(size);
        editText3.setTextSize(size);
        editText6.setTextSize(size);
        editText9.setTextSize(size);
        editText10.setTextSize(size);

        editText.setAllCaps(true);
        editText2.setAllCaps(true);
        editText3.setAllCaps(true);
        editText6.setAllCaps(true);
        editText9.setAllCaps(true);
        editText10.setAllCaps(true);

        textView.setTextSize(size + 1);
        textView2.setTextSize(size + 1);
        textView3.setTextSize(size + 1);
        textView5.setTextSize(size + 1);
        textView6.setTextSize(size + 1);
        textView9.setTextSize(size + 1);
        textView10.setTextSize(size + 1);

        try {
            UGrade = getIntent().getStringExtra("GradeV");
            UNumber = getIntent().getIntExtra("NumberV", 0);
        } catch (Exception t) {

        }

        String subjectHolder[] = {"English", "Maths natural", "Chemistry", "Biology", "Physics", "Civics"
                , "HP", "ICT", "አማርኛ", "Economics", "Business", "History", "Geography", "Drawing", "Maths scocial"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, subjectHolder) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView arrr = (TextView) super.getView(position, convertView, parent);
                arrr.setTextSize(size);
                return arrr;
            }
        };
        spinner.setAdapter(adapter);
        spinner.setScrollBarSize(size);

        db = new MainStudentDataDb(this, "STUDENT_DATA.db").getWritableDatabase();

        if (utility.semester(this).equals("Second"))
            SemesterFirstOrSecond = "Second";

        try {
            updateType = getIntent().getStringExtra("updateType");
        } catch (Exception e) {
        }

        if (updateRequest[0].equals(""))
            editText.setEnabled(false);
        if (updateRequest[1].equals(""))
            editText2.setEnabled(false);
        if (updateRequest[2].equals(""))
            editText3.setEnabled(false);
        if (updateRequest[4].equals(""))
            spinner.setEnabled(false);
        if (updateRequest[5].equals(""))
            editText6.setEnabled(false);
        if (updateRequest[8].equals(""))
            editText9.setEnabled(false);
        if (updateRequest[9].equals(""))
            editText10.setEnabled(false);

        if (!updateType.equals("all")) {
            try {
                cursor = db.rawQuery("select Sname , Fname , GFname , Semester , Subject, Phone_number, Grade " +
                        ", Number , Birth_Date , Sex from StudentData where Grade = '" + UGrade + "' and Number = " + UNumber, null);

                if (cursor.moveToFirst()) {
                    textView.append(" :" + cursor.getString(0));
                    textView2.append(" :" + cursor.getString(1));
                    textView3.append(" :" + cursor.getString(2));
                    textView5.append(" :" + cursor.getString(4));
                    textView6.append(" :" + cursor.getString(5));
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
                    Date date = new Date();
                    String a = String.valueOf(cursor.getString(8).charAt(0));
                    String b = String.valueOf(cursor.getString(8).charAt(1));
                    String c = String.valueOf(cursor.getString(8).charAt(2));
                    String d = String.valueOf(cursor.getString(8).charAt(3));
                    int age = Integer.parseInt(a + b + c + d);

                    if (SemesterFirstOrSecond.equals("First"))
                        age = Integer.parseInt(dateFormat.format(date)) - age - 7;
                    else
                        age = Integer.parseInt(dateFormat.format(date)) - age - 8;
                    textView9.append(" :" + age);
                    textView10.append(" :" + cursor.getString(9));
                }
            } catch (Exception t) {
                finish();
                super.onBackPressed();
            }
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!updateType.equals("all")) {
                    updateOneStudent();
                } else
                    updateMonyStudent();
            }
        });

    }

    private void updateMonyStudent() {
        if (!updateRequest[0].equals("")) {
            if (!editText.getText().toString().isEmpty())
                Sname = ",Sname = '" + editText.getText().toString() + "'";
            else {
                Sname = "mm";
                Toast.makeText(this, "Student name can't be null!", Toast.LENGTH_SHORT).show();
            }
        }
        if (!updateRequest[1].equals("")) {
            if (!editText2.getText().toString().isEmpty())
                Fname = ",Fname = '" + editText2.getText().toString() + "'";
            else {
                Fname = "mm";
                Toast.makeText(this, "Father name can't be null!", Toast.LENGTH_SHORT).show();
            }
        }
        if (!updateRequest[2].equals("")) {
            GFname = ",GFname = '" + editText3.getText().toString() + "'";
        }
        if (!updateRequest[4].equals("")) {
            Subject = ",Subject = '" + spinner.getSelectedItem() + "'";
        }
        if (!updateRequest[8].equals("")) {
            if (editText9.getText().toString().length() == 8 || editText9.getText().toString().length() == 0) {
                if (!editText9.getText().toString().isEmpty()) {
                    Birth_Date = ",Birth_Date ='" + editText9.getText().toString() + "'";
                } else
                    Birth_Date = ",Birth_Date ='1975_1_1'";

            } else {
                Toast.makeText(this, "Age format year_month_day", Toast.LENGTH_SHORT).show();
                Birth_Date = "mm";
            }

        }
        if (!updateRequest[9].equals("")) {
            if (!editText10.getText().toString().isEmpty())
                Sex = ",Sex ='" + editText10.getText().toString() + "'";
            else {
                Toast.makeText(this, "Sex can't be null!", Toast.LENGTH_SHORT).show();
                Sex = "mm";
            }
        }
        if (!updateRequest[5].equals("")) {
            try {
                if (editText6.getText().toString().isEmpty()) {
                    Phone_number = ",Phone_number = '" + editText6.getText().toString() + "'";
                    updateStudentMore();
                } else if (editText6.getText().toString().length() == 10) {
                    if (editText6.getText().toString().charAt(0) == '0' && editText6.getText().toString().charAt(1) == '9') {
                        Phone_number = ",Phone_number = '" + editText6.getText().toString() + "'";
                        updateStudentMore();
                    } else {
                        Toast.makeText(UpdateStudentEditable.this, "Invalid phone Number ", Toast.LENGTH_SHORT).show();
                        Phone_number = "mm";
                    }
                } else {
                    Toast.makeText(UpdateStudentEditable.this, "Invalid phone Number ", Toast.LENGTH_SHORT).show();
                    Phone_number = "mm";
                }
            } catch (Exception u) {
                {
                    Toast.makeText(UpdateStudentEditable.this, "Invalid phone Number ", Toast.LENGTH_SHORT).show();
                    Phone_number = "mm";
                }
            }
        }else
		updateStudentMore();	
    }

    private void updateStudentMore() {
        try {
            for (int i = 0; i < totalUpdate; i++) {
                db.execSQL("update StudentData set Date_last_update ='" + utility.CurrentTime(file, UpdateStudentEditable.this)
                        + "'" + Sname + Fname + GFname + Semester + Subject + Phone_number
                        + Birth_Date + Sex + " where Grade = '"
                        + gradeHolder[i] + "' and Number = '" + numberHolder[i] + "'");

                totalSucess++;
            }
            if (preferences.getBoolean("all", false) == true)
                utility.backUp(UpdateStudentEditable.this);
            Toast.makeText(this, totalSucess + " Update sucessful!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UpdateStudentEditable.this, UpdateStudent.class);
            startActivity(intent);
            finish();
        } catch (Exception e) {
        }
    }

    private void updateOneStudent() {
		
        if (!updateRequest[0].equals("")) {
            if (!editText.getText().toString().isEmpty())
                Sname = ",Sname = '" + editText.getText().toString() + "'";
            else {
                Sname = "mm";
                Toast.makeText(this, "Student name can't be null!", Toast.LENGTH_SHORT).show();
            }
        }
        if (!updateRequest[1].equals("")) {
            if (!editText2.getText().toString().isEmpty())
                Fname = ",Fname = '" + editText2.getText().toString() + "'";
            else {
                Fname = "mm";
                Toast.makeText(this, "Father name can't be null!", Toast.LENGTH_SHORT).show();
            }
        }
        if (!updateRequest[2].equals("")) {
            GFname = ",GFname = '" + editText3.getText().toString() + "'";
        }
        if (!updateRequest[4].equals("")) {
            Subject = ",Subject = '" + spinner.getSelectedItem() + "'";
        }
		if (!updateRequest[8].equals("")) {
            if (editText9.getText().toString().length() == 8 || editText9.getText().toString().length() == 0) {
                if (!editText9.getText().toString().isEmpty()) {
                    Birth_Date = ",Birth_Date ='" + editText9.getText().toString() + "'";
                } else
                    Birth_Date = ",Birth_Date ='1975_1_1'";

            } else {
                Toast.makeText(this, "Age format year_month_day", Toast.LENGTH_SHORT).show();
                Birth_Date = "mm";
            }

        }
        if (!updateRequest[9].equals("")) {
            if (!editText10.getText().toString().isEmpty())
                Sex = ",Sex ='" + editText10.getText().toString() + "'";
            else {
                Toast.makeText(this, "Sex can't be null!", Toast.LENGTH_SHORT).show();
                Sex = "mm";
            }
        }
        if (!updateRequest[5].equals("")) {
            if (utility.checkPhoneNumber(editText6.getText().toString()) == true)
               updateStudentOne();              
            else
                Toast.makeText(UpdateStudentEditable.this, "Invalid phone Number ", Toast.LENGTH_SHORT).show();
        }else
			updateStudentOne();

    }

    private void updateStudentOne() {
        try {
            db.execSQL("update StudentData set Date_last_update ='" + utility.CurrentTime(file, UpdateStudentEditable.this)
                    + "'" + Sname + Fname + GFname + Semester + Subject + Phone_number
                    + Birth_Date + Sex + " where Grade = '" + UGrade + "' and Number = " + UNumber);

            if (preferences.getBoolean("update", false) == true)
                utility.backUp(UpdateStudentEditable.this);

            cursor = db.rawQuery("select Sname , Fname , GFname , Semester , Subject, Phone_number, Grade " +
                    ", Number , Birth_Date , Sex from StudentData where Grade = '" + UGrade + "' and Number = " + UNumber, null);

            cursor.moveToFirst();
            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateStudentEditable.this);
            builder.setTitle("Sucess!");
            builder.setCancelable(false);
            builder.setMessage("Name :" + cursor.getString(0) + " " + cursor.getString(1) + " " + cursor.getString(2)
                    + "\nSemester :" + cursor.getString(3) + " , Subject :" + cursor.getString(4)
                    + "\nP.Num :" + cursor.getString(5) + " , Grade :" + cursor.getString(6)
                    + " , Number :" + cursor.getString(7) + "\nBirth_Date :" + cursor.getString(8)
                    + " , Sex :" + cursor.getString(9));
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(UpdateStudentEditable.this, UpdateStudent.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } catch (Exception y) {
            Toast.makeText(this, y.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}