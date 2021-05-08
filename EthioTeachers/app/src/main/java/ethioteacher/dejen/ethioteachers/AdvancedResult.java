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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AdvancedResult extends Activity {

    private EditText result, grade;
    private Spinner colomn, command, semester, subject, sex;
    private Button button;
    private SQLiteDatabase db;
    private Cursor cursor;
    private String opration = "";
    private TextView resultText, resultTypeText, semesterText, subjectText, gradeText, sexText;

    String colomonTypte = "";
    String sign = "";
    String resultValue = "";
    String semesterType = "";
    String subjectTypew = "";
    String gradeType = "";
    String sexType = "";

    String dataRetrived[];
    int totalRetrive = 0, totalMale = 0, totalFemale = 0;
    int size = 13;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_result);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        try {
            size = Integer.parseInt(preferences.getString("size", "13"));
        } catch (Exception e) {

        }

        button = (Button) findViewById(R.id.button13);

        db = new MainStudentDataDb(this, "STUDENT_DATA.db").getWritableDatabase();
        opration = getIntent().getStringExtra("operation");
        resultText = (TextView) findViewById(R.id.textView23);
        resultTypeText = (TextView) findViewById(R.id.textView32);
        semesterText = (TextView) findViewById(R.id.textView22);
        subjectText = (TextView) findViewById(R.id.textView21);
        gradeText = (TextView) findViewById(R.id.textView20);
        sexText = (TextView) findViewById(R.id.textView);

        sexText.setTextSize(size);
        gradeText.setTextSize(size);
        subjectText.setTextSize(size);
        semesterText.setTextSize(size);
        resultTypeText.setTextSize(size);
        resultText.setTextSize(size);

        colomn = (Spinner) findViewById(R.id.spinner1);
        command = (Spinner) findViewById(R.id.spinner2);
        semester = (Spinner) findViewById(R.id.spinner3);
        subject = (Spinner) findViewById(R.id.spinner4);
        sex = (Spinner) findViewById(R.id.sex);

        result = (EditText) findViewById(R.id.editText);
        grade = (EditText) findViewById(R.id.editText2);

        result.setAllCaps(true);
        grade.setAllCaps(true);

        String subjectHolder[] = {"All", "English", "Maths natural", "Chemistry", "Biology", "Physics", "Civics"
                , "HP", "ICT", "አማርኛ", "Economics", "Business", "History", "Geography", "Drawing", "Maths scocial"};

        String colomon[] = {"Class work", "Home work", "Proj,Assign", "Group work", "Mid exam", "Final", "Total 40", "Total 100"};

        String commands[] = {"", "Equals", "Greater", "Less", "GreaterOrEqual", "LessOrEqual"};

        String semesterHollder[] = {"All", "First", "Second"};

        if (opration.equals("result")) {
            String commandsresult[] = {"Equals", "Greater", "Less", "GreaterOrEqual", "LessOrEqual"};
            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, commandsresult) {
                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    TextView view = (TextView) super.getView(position, convertView, parent);
                    view.setTextSize(size);
                    return view;
                }
            };
            command.setAdapter(adapter2);

            String semesterHollderresult[] = {"First", "Second"};
            ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, semesterHollderresult) {
                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    TextView view = (TextView) super.getView(position, convertView, parent);
                    view.setTextSize(size);
                    return view;
                }
            };
            semester.setAdapter(adapter3);
        } else {
            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, commands) {
                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    TextView view = (TextView) super.getView(position, convertView, parent);
                    view.setTextSize(size);
                    return view;
                }
            };
            command.setAdapter(adapter2);
            ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, semesterHollder) {
                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    TextView view = (TextView) super.getView(position, convertView, parent);
                    view.setTextSize(size);
                    return view;
                }
            };
            semester.setAdapter(adapter3);
        }
        if (opration.equals("top")) {
            String semesterHollderresult[] = {"First", "Second"};
            ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, semesterHollderresult) {
                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    TextView view = (TextView) super.getView(position, convertView, parent);
                    view.setTextSize(size);
                    return view;
                }
            };
            semester.setAdapter(adapter3);
        }
        if (opration.equals("least")) {
            String semesterHollderresult[] = {"First", "Second"};
            ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, semesterHollderresult) {
                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    TextView view = (TextView) super.getView(position, convertView, parent);
                    view.setTextSize(size);
                    return view;
                }
            };
            semester.setAdapter(adapter3);
        }
        if (opration.equals("average")) {
            command.setVisibility(View.INVISIBLE);
            result.setEnabled(false);
            button.setText("GET AVERAGE");
        } else if (opration.equals("top")) {
            resultText.setText("Top student");
            resultText.setTextSize(size + 4);
            result.setEnabled(true);
            command.setVisibility(View.INVISIBLE);
            button.setText("GET TOP STUDENT");
        } else if (opration.equals("least")) {
            resultText.setText("Least student");
            resultText.setTextSize(size + 4);
            result.setEnabled(true);
            command.setVisibility(View.INVISIBLE);
            button.setText("GET LEAST STUDENT");
        }

        String sexHollder[] = {"All", "F", "M"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, colomon) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setTextSize(size);
                return view;
            }
        };
        colomn.setAdapter(adapter);

        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, subjectHolder) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setTextSize(size);
                return view;
            }
        };
        subject.setAdapter(adapter4);

        ArrayAdapter<String> adapter5 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sexHollder) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setTextSize(size);
                return view;
            }
        };
        sex.setAdapter(adapter5);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (colomn.getSelectedItem().toString().equals("Class work")) {
                    colomonTypte = "Class_Work_10";
                } else if (colomn.getSelectedItem().toString().equals("Home work")) {
                    colomonTypte = "Home_Work_5";
                } else if (colomn.getSelectedItem().toString().equals("Proj,Assign")) {
                    colomonTypte = "Proj_and_Assign_10";
                } else if (colomn.getSelectedItem().toString().equals("Group work")) {
                    colomonTypte = "Group_Work_5";
                } else if (colomn.getSelectedItem().equals("Mid exam")) {
                    colomonTypte = "Mid_Exam_10";
                } else if (colomn.getSelectedItem().toString().equals("Final")) {
                    colomonTypte = "Final_60";
                } else if (colomn.getSelectedItem().toString().equals("Total 40")) {
                    colomonTypte = "Total40";
                } else if (colomn.getSelectedItem().toString().equals("Total 100")) {
                    colomonTypte = "Total100";
                }

                if (command.getSelectedItem().toString().equals("Less")) {
                    sign = " < ";
                } else if (command.getSelectedItem().toString().equals("GreaterOrEqual")) {
                    sign = " >= ";
                } else if (command.getSelectedItem().toString().equals("LessOrEqual")) {
                    sign = " <= ";
                } else if (command.getSelectedItem().toString().equals("Greater")) {
                    sign = " > ";
                } else if (command.getSelectedItem().toString().equals("Equals") && result.getText().toString().isEmpty()) {
                    sign = " = ''";
                } else if (command.getSelectedItem().toString().equals("Equals")) {
                    sign = " = ";
                }

                if (!grade.getText().toString().isEmpty())
                    gradeType = " and StudentData.Grade = '" + grade.getText().toString() + "'";
                if (!subject.getSelectedItem().toString().equals("All"))
                    subjectTypew = " and StudentData.Subject = '" + subject.getSelectedItem().toString() + "'";
                if (!sex.getSelectedItem().toString().equals("All"))
                    sexType = " and StudentData.Sex = '" + sex.getSelectedItem().toString() + "'";

                if (opration.equals("result")) {
                    if (!result.getText().toString().isEmpty()) {
                        retriveResult();
                    } else {
                        retriveResult();
                    }
                } else if (opration.equals("average")) {
                    retriveAverage();
                } else if (opration.equals("top")) {
                    retriveTop("desc");
                } else if (opration.equals("least")) {
                    retriveTop("asc");
                }
                colomonTypte = "";
                sign = "";
                resultValue = "";
                semesterType = "";
                subjectTypew = "";
                gradeType = "";
                sexType = "";
            }
        });
    }

    private void retriveTop(String orderLevel) {
        if (!result.getText().toString().isEmpty()) {
            String holdGrade = "All";
            if (!grade.getText().toString().isEmpty())
                holdGrade = grade.getText().toString();
            try {
                semesterType = "StudentValue" + semester.getSelectedItem().toString();
                cursor = db.rawQuery("select Number,Sname,Fname,GFname," + colomonTypte
                        + ",Sex from StudentData," + semesterType
                        + " where " + semesterType + ".NumberV = StudentData.Number"
                        + " and " + semesterType + ".GradeV = StudentData.Grade"
                        + gradeType + subjectTypew + sexType + " order by cast("
                        + semesterType + "." + colomonTypte + " as real) " + orderLevel + " limit "
                        + Integer.parseInt(result.getText().toString()), null);
                if (cursor.moveToFirst()) {
                    dataRetrived = new String[cursor.getCount()];
                    do {
                        dataRetrived[totalRetrive] = "Name :" + cursor.getString(1) + " " + cursor.getString(2) + " "
                                + cursor.getString(3) + " :" + cursor.getString(5)  + "\nNumber :" + cursor.getString(0)
                                + "\n" + colomonTypte + " :" + cursor.getString(4)+ "\n";
                        totalRetrive++;
                        if (cursor.getString(5).equals("F"))
                            totalFemale++;
                        else
                            totalMale++;
                    } while (cursor.moveToNext());
                    Intent intent = new Intent(AdvancedResult.this, ListViewForAdvanced.class);
                    Bundle bundle = new Bundle();
                    bundle.putStringArray("dataRetrived", dataRetrived);
                    bundle.putString("title", "Sub:" + subject.getSelectedItem().toString()
                            + ",Grade:" + holdGrade
                            + ",Sem:" + semester.getSelectedItem().toString());
                    bundle.putInt("Female", totalFemale);
                    bundle.putInt("Male", totalMale);

                    bundle.putString("alert", "Subject\t\t:" + subject.getSelectedItem().toString()
                            + "\nGrade\t\t:" + holdGrade
                            + "\nSemester\t\t:" + semester.getSelectedItem().toString()
                            + "\n" + colomonTypte + " " + sign + " " + result.getText().toString());
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    Toast.makeText(AdvancedResult.this, "No smilar data found!", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(AdvancedResult.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        } else

        {
            if (opration.equals("top"))
                Toast.makeText(this, "Please enter top level!", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Please enter least level!", Toast.LENGTH_SHORT).show();
        }

        totalRetrive = 0;
        totalFemale = 0;
        totalMale = 0;
    }

    private void retriveAverage() {
        String holdGrade = "All";
        if (!grade.getText().toString().isEmpty())
            holdGrade = grade.getText().toString();
        if (semester.getSelectedItem().toString().equals("All")) {
            try {
                cursor = db.rawQuery("select AVG(StudentValueFirst." + colomonTypte + ")" +
                        ",AVG(StudentValueSecond." + colomonTypte + ") from StudentData,StudentValueFirst,StudentValueSecond"
                        + " where StudentValueFirst.NumberV = StudentData.Number"
                        + " and StudentValueFirst.GradeV = StudentData.Grade"
                        + " and StudentValueSecond.NumberV = StudentData.Number"
                        + " and StudentValueSecond.GradeV = StudentData.Grade"
                        + gradeType + subjectTypew + sexType, null);

                if (cursor.moveToFirst()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AdvancedResult.this);
                    builder.setMessage("Subject\t\t\t\t\t\t\t:" + subject.getSelectedItem().toString()
                            + "\nGrade\t\t\t\t\t\t\t\t:" + holdGrade
                            + "\nSemester\t\t\t\t\t\t:" + semester.getSelectedItem().toString()
                            + "\n" + colomonTypte + "%\nSex \t\t\t\t\t\t\t\t\t:" + sex.getSelectedItem()
                            + "\n\n\t\t\tAverage = " + (Double.parseDouble(cursor.getString(0))
                            + Double.parseDouble(cursor.getString(1))) / 2);
                    builder.setCancelable(false);

                    builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                } else {
                    Toast.makeText(AdvancedResult.this, "No smilar data found!", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception r) {
                Toast.makeText(AdvancedResult.this, "No smilar data found!", Toast.LENGTH_SHORT).show();
            }
        } else {
            try {
                semesterType = "StudentValue" + semester.getSelectedItem().toString();
                cursor = db.rawQuery("select AVG(" + colomonTypte + ") from StudentData," + semesterType
                        + " where " + semesterType + ".NumberV = StudentData.Number"
                        + " and " + semesterType + ".GradeV = StudentData.Grade" + gradeType + subjectTypew + sexType, null);

                if (cursor.moveToFirst()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AdvancedResult.this);
                    builder.setMessage("Subject\t\t\t\t\t\t\t:" + subject.getSelectedItem().toString()
                            + "\nGrade\t\t\t\t\t\t\t\t:" + holdGrade
                            + "\nSemester\t\t\t\t\t\t:" + semester.getSelectedItem().toString()
                            + "\n" + colomonTypte + "%\nSex \t\t\t\t\t\t\t\t\t:" + sex.getSelectedItem()
                            + "\n\n\t\t\tAverage = " + Double.parseDouble(cursor.getString(0)));
                    builder.setCancelable(false);
                    builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                } else {
                    Toast.makeText(AdvancedResult.this, "No smilar data found!", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(AdvancedResult.this, "No smilar data found!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void retriveResult() {

        if (!semester.getSelectedItem().toString().equals("All"))
            semesterType = "StudentValue" + semester.getSelectedItem().toString();
        try {
            if (!result.getText().toString().isEmpty())
                cursor = db.rawQuery("select Number,Sname,Fname,GFname,Sex," + colomonTypte
                        + " from StudentData inner join " + semesterType
                        + " on " + semesterType + ".NumberV = StudentData.Number"
                        + " and " + semesterType + ".GradeV = StudentData.Grade"
                        + " and " + semesterType + "." + colomonTypte + sign + Double.parseDouble(result.getText().toString())
                        + gradeType + subjectTypew + sexType + " order by Number asc", null);
            else
                cursor = db.rawQuery("select Number,Sname,Fname,GFname,Sex," + colomonTypte
                        + " from StudentData inner join " + semesterType
                        + " on " + semesterType + ".NumberV = StudentData.Number"
                        + " and " + semesterType + ".GradeV = StudentData.Grade"
                        + " and " + semesterType + "." + colomonTypte + sign
                        + gradeType + subjectTypew + sexType + " order by Number asc", null);
            if (cursor.moveToFirst()) {
                dataRetrived = new String[cursor.getCount()];
                do {
                    dataRetrived[totalRetrive] = "Name :" + cursor.getString(1) + " " + cursor.getString(2) + " "
                            + cursor.getString(3) + "\nNumber :" + cursor.getString(0) + " Sex :" + cursor.getString(4) + "\n";
                    totalRetrive++;
                    if (cursor.getString(4).equals("F"))
                        totalFemale++;
                    else
                        totalMale++;
                } while (cursor.moveToNext());
                Intent intent = new Intent(AdvancedResult.this, ListViewForAdvanced.class);
                Bundle bundle = new Bundle();
                bundle.putStringArray("dataRetrived", dataRetrived);
                bundle.putInt("Female", totalFemale);
                bundle.putInt("Male", totalMale);
                bundle.putString("title", "Sub:" + subject.getSelectedItem().toString()
                        + ",Grade:" + grade.getText().toString()
                        + ",Sem:" + semester.getSelectedItem().toString());

                bundle.putString("alert", "Subject\t\t:" + subject.getSelectedItem().toString()
                        + "\nGrade\t\t:" + grade.getText().toString()
                        + "\nSemester\t\t:" + semester.getSelectedItem().toString()
                        + "\n" + colomonTypte + " " + sign + " " + result.getText().toString());
                intent.putExtras(bundle);
                startActivity(intent);
            } else {
                Toast.makeText(AdvancedResult.this, "No smilar data found!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
        }

        totalRetrive = 0;
        totalFemale = 0;
        totalMale = 0;
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AdvancedResult.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
