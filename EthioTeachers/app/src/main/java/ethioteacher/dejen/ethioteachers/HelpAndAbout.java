package ethioteacher.dejen.ethioteachers;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import jxl.*;

public class HelpAndAbout extends AppCompatActivity {

    private String path = "";
    private Uri uri = null;

    private WebView webView;

    private final String inputType = "text/html";
    private final String encodind = "UTF-8";
    private String textx = "";
    private Spinner spinner;
    private SQLiteDatabase db;
    private Cursor cursor = null, excelCursor = null;
    private FileInputStream inputStream;
    private FileOutputStream outputStream;
    private String SemesterFirstOrSecond = "First";
    private String order = "";
    private String orderValue = "asc";
    private String Subject = "", Grade = "";
    private EditText subject, grade;
    private SharedPreferences preferences;
    private int size = 13;
    private Utility utility;
    private boolean saveType = false;
    private String oldGrade = "", oldSubject = "";

    private String adminOne = "", adminTwo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //  this.requestWindowFeature(Window.FEATURE_LEFT_ICON);
        setContentView(R.layout.activity_help_and_about);
        setTitle("");

        utility = new Utility();
        spinner = (Spinner) findViewById(R.id.spinner);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        try {
            size = Integer.parseInt(preferences.getString("size", "13"));
        } catch (Exception e) {

        }

        saveType = preferences.getBoolean("saveType", false);
        String values[] = {"Number", "Sname", "Fname", "GFname", "Grade", "Sex", "Age", "First final", "First 40", "First 100", "Second final", "Second 40", "Second 100"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, values) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setTextSize(size);
                return view;
            }
        };
        spinner.setAdapter(arrayAdapter);

        subject = (EditText) findViewById(R.id.editText28);
        grade = (EditText) findViewById(R.id.editText36);

        subject.setAllCaps(true);
        grade.setAllCaps(true);

        webView = (WebView) findViewById(R.id.webeview);
        if (preferences.getBoolean("admin", false) == true) {
            db = new MainStudentDataDb(this, "ADMINISTRATOR.db").getWritableDatabase();
            adminOne = " and StudentData.Subject = StudentValueFirst.SubjectV ";
            adminTwo = " and StudentData.Subject = StudentValueSecond.SubjectV ";
			subject.setVisibility(View.INVISIBLE);
			grade.setVisibility(View.INVISIBLE);
			spinner.setVisibility(View.INVISIBLE);

        } else
            db = new MainStudentDataDb(this, "STUDENT_DATA.db").getWritableDatabase();

        if (new Utility().semester(this).equals("Second"))
            SemesterFirstOrSecond = "Second";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Asending or Descending?");
        builder.setPositiveButton("Ascending", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                order = " order by Number  asc";
                orderValue = "asc";
                dialog.dismiss();
                methodDislapy();
            }
        });
        builder.setNegativeButton("Descending", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                order = " order by Number desc";
                orderValue = "desc";
                dialog.dismiss();
                methodDislapy();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void refresh() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Asending or Descending?");
        builder.setPositiveButton("Ascending", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                order = " order by " + spinner.getSelectedItem().toString() + "  asc";
                orderValue = "asc";
                dialog.dismiss();
                methodDislapy();
            }
        });
        builder.setNegativeButton("Descending", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                order = " order by " + spinner.getSelectedItem().toString() + " desc";
                orderValue = "desc";
                dialog.dismiss();
                methodDislapy();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void viewResultSelected() {
        try {
            changeDatabaseToHtmlFormat();
            webView.getSettings().setJavaScriptEnabled(true);
            WebSettings webSettings = webView.getSettings();
            webSettings.setBuiltInZoomControls(true);
            webView.loadDataWithBaseURL("", textx, inputType, encodind, "");
        } catch (Exception e) {
        }
        textx = "";
        oldGrade = "";
        oldSubject = "";
    }

    private void methodDislapy() {
        if (spinner.getSelectedItem().toString().equals("First 100"))
            order = " order by StudentValueFirst.Total100 " + orderValue;
        if (spinner.getSelectedItem().toString().equals("Second 100"))
            order = " order by StudentValueSecond.Total100 " + orderValue;
        if (spinner.getSelectedItem().toString().equals("First 40"))
            order = " order by StudentValueFirst.Total40 " + orderValue;
        if (spinner.getSelectedItem().toString().equals("Second 40"))
            order = " order by StudentValueSecond.Total40 " + orderValue;
        if (spinner.getSelectedItem().toString().equals("First final"))
            order = " order by StudentValueFirst.Final_60 " + orderValue;
        if (spinner.getSelectedItem().toString().equals("Second final"))
            order = " order by StudentValueSecond.Final_60 " + orderValue;
        if (spinner.getSelectedItem().toString().equals("Age"))
            order = " order by Birth_Date " + orderValue;

        String gradeValue = "";
        String subjectValue = "";

        if (!grade.getText().toString().isEmpty())
            gradeValue = "and StudentData.Grade = '" + grade.getText().toString() + "' ";
        if (!subject.getText().toString().isEmpty())
            subjectValue = "and StudentData.Subject = '" + subject.getText().toString() + "'";

        try {
            if (preferences.getBoolean("admin", false) == true)
                order = " order by Subject " + orderValue +", Grade "+orderValue;
            cursor = db.rawQuery("select Number,Sname,Fname,GFname,Sex,Birth_Date"
                    + ",StudentValueFirst.Class_Work_10,StudentValueFirst.Home_Work_5"
                    + ",StudentValueFirst.Proj_and_Assign_10,StudentValueFirst.Group_Work_5"
                    + ",StudentValueFirst.Mid_Exam_10,StudentValueFirst.Total40,StudentValueFirst.Final_60"
                    + ",StudentValueFirst.Total100"
                    + ",StudentValueSecond.Class_Work_10,StudentValueSecond.Home_Work_5"
                    + ",StudentValueSecond.Proj_and_Assign_10,StudentValueSecond.Group_Work_5"
                    + ",StudentValueSecond.Mid_Exam_10,StudentValueSecond.Total40,StudentValueSecond.Final_60 "
                    + ",StudentValueSecond.Total100,Grade,Subject from StudentData "
                    + "inner join StudentValueFirst on StudentValueFirst.NumberV = StudentData.Number "
                    + "and StudentValueFirst.GradeV = StudentData.Grade " + adminOne + gradeValue + subjectValue
                    + " inner join StudentValueSecond "
                    + "on StudentValueSecond.NumberV = StudentValueFirst.NumberV and "
                    + "StudentValueSecond.GradeV = StudentValueFirst.GradeV" + adminTwo + order, null);

            excelCursor = db.rawQuery("select  Sname,Fname,GFname,Semester,Subject,Phone_number"
                    + ",Grade,Number,Birth_Date,Sex,Date_Register,Date_last_update,School from StudentData "
                    + "inner join StudentValueFirst on StudentValueFirst.NumberV = StudentData.Number "
                    + "and StudentValueFirst.GradeV = StudentData.Grade " + adminOne + gradeValue + subjectValue
                    + " inner join StudentValueSecond "
                    + "on StudentValueSecond.NumberV = StudentValueFirst.NumberV and "
                    + "StudentValueSecond.GradeV = StudentValueFirst.GradeV" + adminTwo + order, null);
            if (cursor.moveToFirst())
                viewResultSelected();
            else
                viewResultSelected();
        } catch (Exception e) {
        }
    }

    @Override
    public void onBackPressed() {
        if (preferences.getBoolean("admin", false) == true) {
            AlertDialog.Builder builder = new AlertDialog.Builder(HelpAndAbout.this);
            builder.setCancelable(false);

            builder.setPositiveButton("Refresh", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {

                    refresh();
                    dialog.dismiss();
                }
            });

            builder.setNegativeButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = null;
                    if (saveType == true) {
                        try {
                            if (Build.VERSION.SDK_INT >= 19)
                                intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                            else
                                intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType("application/vnd.ms-excel");
                            startActivityForResult(intent, 12);
                        } catch (Exception e) {

                        }
                    } else {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_mm");
                        Date date = new Date();
                        File file = utility.getExternalFileDiroctory();
                        if (!file.exists())
                            file.mkdirs();
                        File file2 = new File(file + "/" + Subject.toUpperCase() + "_" + Grade + "_" + dateFormat.format(date).toString() + ".html");
                        try {
                            FileOutputStream outputStream = new FileOutputStream(file2);
                            outputStream.write(textx.getBytes());
                            outputStream.flush();
                            outputStream.close();
                            Toast.makeText(HelpAndAbout.this, "Sucess", Toast.LENGTH_SHORT).show();
                        } catch (Exception rr) {
                            Toast.makeText(HelpAndAbout.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
            builder.setNeutralButton("Setting", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(HelpAndAbout.this, Setting.class);
                    startActivity(intent);
                    finish();
                    dialog.dismiss();
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(HelpAndAbout.this);
            builder.setCancelable(false);
            builder.setPositiveButton("Refresh", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    textx = "";
                    refresh();
                }
            });
            builder.setNegativeButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = null;
                    if (saveType == true) {
                        try {
                            if (Build.VERSION.SDK_INT >= 19)
                                intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                            else
                                intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType("application/vnd.ms-excel");
                            startActivityForResult(intent, 12);
                        } catch (Exception e) {

                        }
                    } else {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_mm");
                        Date date = new Date();
                        File file = utility.getExternalFileDiroctory();
                        if (!file.exists())
                            file.mkdirs();
                        File file2 = new File(file + "/" + Subject.toUpperCase() + "_" + Grade + "_" + dateFormat.format(date).toString() + ".txt");
                        try {
                            FileOutputStream outputStream = new FileOutputStream(file2);
                            outputStream.write(textx.getBytes());
                            outputStream.flush();
                            outputStream.close();
                            Toast.makeText(HelpAndAbout.this, "Sucess", Toast.LENGTH_SHORT).show();
                        } catch (Exception rr) {
                            Toast.makeText(HelpAndAbout.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
            builder.setNeutralButton("Back", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(HelpAndAbout.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    dialog.dismiss();
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.help_and_about_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (preferences.getBoolean("admin", false) == false) {
            menu.getItem(0).setEnabled(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.AdmiImport) {
            //import only xls files
            // becous add only one course result
            try {
                Intent intent;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                else
                    intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/*");
                startActivityForResult(intent, 111);
            } catch (Exception e) {

            }
        } else if (item.getItemId() == R.id.AdminExit) {
            android.os.Process.killProcess(android.os.Process.myPid());
        }
        return true;
    }

    private void changeDatabaseToHtmlFormat() {
        int acadamicYear;
        if (!subject.getText().toString().isEmpty())
            Subject = subject.getText().toString();

        if (!grade.getText().toString().isEmpty())
            Grade = grade.getText().toString();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        Date date = new Date();
        String fileName = "HTML_" + dateFormat.format(date).toString() + "hr";
        File file = new File(this.getFilesDir(), utility.getStorageDirctoryName());
        if (!file.exists())
            file.mkdirs();

        acadamicYear = utility.Year(HelpAndAbout.this);

        try {
            outputStream = new FileOutputStream(file + "/" + fileName + ".html");
            outputStream.write((
                    "<html>\n" +
                            "<head>\n" +
                            "</head>\n" +
                            "<body bgcolor=\"white\">\n" +
                            "<table border=\"2\" bgcolor=\"green\">\n" +
                            "<tr>\n" +
                            "<h3 align =\"center\">" + preferences.getString("schoolname", "___") + "</h3>\n" +
                            "<h4 align=\"center-left\">SUBJECT  " + Subject + "</h3>\n" +
                            "<h4 align=\"center-left\">GRADE  " + Grade + "</h3>\n" +
                            "<h4 align =\"center\">Academic year :" + acadamicYear + "E.C</h4> \n" +
                            "<h4 align =\"center\">Prepaired by " + preferences.getString("teacherName", "___") + "</h4>\n" +
                            "</tr>\n" +
                            "<tr>\n" +
                            "<th rowspan=\"3\"> Class room NO</th>\n" +
                            "<th rowspan=\"3\"  colspan=\"3\"> Name of student's father's g/father's name</th>\n" +
                            "<th rowspan=\"3\"> Sex</th>\n" +
                            "<th rowspan=\"3\"> Age</th>\n" +
                            "<th colspan=\"8\"> First semester</th>\n" +
                            "<th colspan=\"8\"> Second semester</th>\n" +
                            "<th rowspan=\"3\"> Average</th>\n" +
                            "<th rowspan=\"3\"> Remark</th>\n" +
                            "</tr>\n" +
                            "<tr>\n" +
                            "<th>Class Work</th>\n" +
                            "<th>Home Work</th>\n" +
                            "<th>Proj & Assign</th>\n" +
                            "<th>Group Work</th>\n" +
                            "<th>Mid Exam</th>\n" +
                            "<th>Total</th>\n" +
                            "<th>Final</th>\n" +
                            "<th>Total</th>\n" +
                            "<th>Class Work</th>\n" +
                            "<th>Home Work</th>\n" +
                            "<th>Proj & Assign</th>\n" +
                            "<th>Group Work</th>\n" +
                            "<th>Mid Exam</th>\n" +
                            "<th>Total</th>\n" +
                            "<th>Final</th>\n" +
                            "<th>Total</th>\n" +
                            "</tr>\n" +
                            "<tr>\n" +
                            "<th>10%</th>\n" +
                            "<th>5%</th>\n" +
                            "<th>10%</th>\n" +
                            "<th>5%</th>\n" +
                            "<th>10%</th>\n" +
                            "<th>40%</th>\n" +
                            "<th>60%</th>\n" +
                            "<th>100%</th>\n" +
                            "<th>10%</th>\n" +
                            "<th>5%</th>\n" +
                            "<th>10%</th>\n" +
                            "<th>5%</th>\n" +
                            "<th>10%</th>\n" +
                            "<th>40%</th>\n" +
                            "<th>60%</th>\n" +
                            "<th>100%</th>\n" +
                            "</tr>").getBytes());
            if (cursor.moveToFirst())
                do {

                    if (preferences.getBoolean("admin", false) == true)
						if(!oldSubject.equals(cursor.getString(23))||!oldGrade.equals(cursor.getString(22)))
                        takeSpaceInHTMLRows();

                    String colorAverage = "white";
                    String average = "";
                    String a = String.valueOf(cursor.getString(5).charAt(0));
                    String b = String.valueOf(cursor.getString(5).charAt(1));
                    String c = String.valueOf(cursor.getString(5).charAt(2));
                    String d = String.valueOf(cursor.getString(5).charAt(3));
                    int age = Integer.parseInt(a + b + c + d);

                    if (SemesterFirstOrSecond.equals("First"))
                        age = Integer.parseInt(dateFormat.format(date)) - age - 7;
                    else
                        age = Integer.parseInt(dateFormat.format(date)) - age - 8;

                    double firstTotal100 = 0;
                    double secondTotal100 = 0;

                    if (!cursor.getString(13).isEmpty())
                        firstTotal100 = Double.parseDouble(cursor.getString(13));
                    if (!cursor.getString(21).isEmpty())
                        secondTotal100 = Double.parseDouble(cursor.getString(21));

                    if (!cursor.getString(13).isEmpty() && !cursor.getString(21).isEmpty()) {
                        average = String.valueOf((firstTotal100 + secondTotal100) / 2);
                        colorAverage = averageColorSeter((firstTotal100 + secondTotal100) / 2);
                    } else if (!cursor.getString(13).isEmpty() && cursor.getString(21).isEmpty()) {
                        average = "First";
                        colorAverage = averageColorSeter(firstTotal100);
                    } else if (cursor.getString(13).isEmpty() && !cursor.getString(21).isEmpty()) {
                        average = "Second";
                        colorAverage = averageColorSeter(secondTotal100);
                    } else
                        average = "";

                    outputStream.write(("<tr>" +
                            "<td>" + cursor.getString(0) + "</td>" +
                            "<td>" + cursor.getString(1) + "</td>" +
                            "<td>" + cursor.getString(2) + "</td>" +
                            "<td>" + cursor.getString(3) + "</td>" +
                            "<td>" + cursor.getString(4) + "</td>" +
                            "<td>" + age + "</td>" +
                            "<td bgcolor=" + colorEverySeter(cursor.getString(6)) + ">" + cursor.getString(6) + "</td>" +
                            "<td bgcolor=" + colorEverySeter(cursor.getString(7)) + ">" + cursor.getString(7) + "</td>" +
                            "<td bgcolor=" + colorEverySeter(cursor.getString(8)) + ">" + cursor.getString(8) + "</td>" +
                            "<td bgcolor=" + colorEverySeter(cursor.getString(9)) + ">" + cursor.getString(9) + "</td>" +
                            "<td bgcolor=" + colorEverySeter(cursor.getString(10)) + ">" + cursor.getString(10) + "</td>" +
                            "<td bgcolor=" + colorEverySeter(cursor.getString(11)) + ">" + cursor.getString(11) + "</td>" +
                            "<td bgcolor=" + colorEverySeter(cursor.getString(12)) + ">" + cursor.getString(12) + "</td>" +
                            "<td bgcolor=" + colorEverySeter(cursor.getString(13)) + ">" + cursor.getString(13) + "</td>" +
                            "<td bgcolor=" + colorEverySeter(cursor.getString(14)) + ">" + cursor.getString(14) + "</td>" +
                            "<td bgcolor=" + colorEverySeter(cursor.getString(15)) + ">" + cursor.getString(15) + "</td>" +
                            "<td bgcolor=" + colorEverySeter(cursor.getString(16)) + ">" + cursor.getString(16) + "</td>" +
                            "<td bgcolor=" + colorEverySeter(cursor.getString(17)) + ">" + cursor.getString(17) + "</td>" +
                            "<td bgcolor=" + colorEverySeter(cursor.getString(18)) + ">" + cursor.getString(18) + "</td>" +
                            "<td bgcolor=" + colorEverySeter(cursor.getString(19)) + ">" + cursor.getString(19) + "</td>" +
                            "<td bgcolor=" + colorEverySeter(cursor.getString(20)) + ">" + cursor.getString(20) + "</td>" +
                            "<td bgcolor=" + colorEverySeter(cursor.getString(21)) + ">" + cursor.getString(21) + "</td>" +
                            "<td bgcolor=" + colorAverage + ">" + average + "</td>" +
                            "<td ></td>" +
                            "</tr>").getBytes());

					oldGrade = cursor.getString(22);
                    oldSubject = cursor.getString(23);
                } while (cursor.moveToNext());
            outputStream.write((
                    "</table>" +
                            "</body>" +
                            "</html>").getBytes());

            outputStream.close();

            inputStream = new FileInputStream(file + "/" + fileName + ".html");
            InputStreamReader reader = new InputStreamReader(inputStream);
            char[] inputBuffer = new char[1024];
            int charRead;
            while ((charRead = reader.read(inputBuffer)) > 0) {
                String readString = String.copyValueOf(inputBuffer, 0, charRead);
                textx = textx + readString;
            }
            file.delete();

        } catch (Exception e) {
        }
    }

    private void saveTypeByExcel(String path) {
        try {
            String SheetName = "sheet1";
            if (excelCursor.moveToFirst()) {
                HSSFWorkbook workbook = new HSSFWorkbook();
                HSSFSheet sheet = workbook.createSheet(SheetName);
                int rowSize = excelCursor.getCount();
                int colSize = 13;

                for (int i = 0; i < rowSize; i++) {
                    HSSFRow row = sheet.createRow(i);
                    for (int c = 0; c < colSize; c++) {
                        HSSFCell cell = row.createCell(c);
                        cell.setCellValue(excelCursor.getString(c));
                    }
                    excelCursor.moveToNext();
                }
                FileOutputStream outputStream = new FileOutputStream(path);
                workbook.write(outputStream);
                outputStream.flush();
                outputStream.close();
                excelCursor.close();
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(this, "No data found!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void takeSpaceInHTMLRows() {
        try {
            outputStream.write(("<tr>" +
                    "<td bgcolor = \"blue\"></td> <td bgcolor = \"blue\"></td ><td bgcolor = \"blue\"></td>" +
                    "<td bgcolor = \"blue\"></td> <td bgcolor = \"blue\"></td ><td bgcolor = \"blue\"></td>" +
                    "<td bgcolor = \"blue\"></td> <td bgcolor = \"blue\"></td ><td bgcolor = \"blue\"></td>" +
                    "<td bgcolor = \"blue\"></td><td bgcolor = \"blue\">Grade</td><td bgcolor = \"blue\">" + cursor.getString(22) + "</td>" +
                    "<td bgcolor = \"blue\"></td> <td bgcolor = \"blue\"></td ><td bgcolor = \"blue\"></td>" +
                    "<td bgcolor = \"blue\"></td> <td bgcolor = \"blue\"></td ><td bgcolor = \"blue\"></td>" +
                    "<td bgcolor = \"blue\"></td> <td bgcolor = \"blue\"></td ><td bgcolor = \"blue\"></td>" +
                    "<td bgcolor = \"blue\"></td> <td bgcolor = \"blue\"></td ><td bgcolor = \"blue\"></td>" +
                    "</tr>").getBytes());

            outputStream.write(("<tr>" +
                    "<td bgcolor = \"blue\"></td> <td bgcolor = \"blue\"></td ><td bgcolor = \"blue\"></td>" +
                    "<td bgcolor = \"blue\"></td> <td bgcolor = \"blue\"></td ><td bgcolor = \"blue\"></td>" +
                    "<td bgcolor = \"blue\"></td> <td bgcolor = \"blue\"></td ><td bgcolor = \"blue\"></td>" +
                    "<td bgcolor = \"blue\"></td><td bgcolor = \"blue\">Subject</td><td bgcolor = \"blue\">" + cursor.getString(23) + "</td>" +
                    "<td bgcolor = \"blue\"></td> <td bgcolor = \"blue\"></td ><td bgcolor = \"blue\"></td>" +
                    "<td bgcolor = \"blue\"></td> <td bgcolor = \"blue\"></td ><td bgcolor = \"blue\"></td>" +
                    "<td bgcolor = \"blue\"></td> <td bgcolor = \"blue\"></td ><td bgcolor = \"blue\"></td>" +
                    "<td bgcolor = \"blue\"></td> <td bgcolor = \"blue\"></td ><td bgcolor = \"blue\"></td>" +
                    "</tr>").getBytes());
        } catch (Exception e) {

        }
    }

    private String averageColorSeter(double ave) {
        String colorType = "\"white\"";
        if (ave >= 50)
            colorType = "\"green\"";
        else if (ave >= 0)
            colorType = "\"red\"";

        return colorType;
    }

    private String colorEverySeter(String value) {
        String colorEvery = "\"white\"";
        if (!value.equals("")) {
            colorEvery = "\"#3a7f0h\"";
        }
        return colorEvery;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri uri = null;
        String path = "";
        int totalSucess = 0;
        if (requestCode == 111 && resultCode == RESULT_OK && data != null) {
            uri = data.getData();
            try {
                path = utility.getFilePath(HelpAndAbout.this, uri);
            } catch (Exception e) {

            }
            try {
                String[] value = new String[13];
                FileInputStream inputStream = new FileInputStream(new File(path));

                Workbook workbook = Workbook.getWorkbook(inputStream);
                Sheet sheet = workbook.getSheet(0);
                int row = sheet.getRows();
                int col = sheet.getColumns();
                for (int i = 0; i < row; i++) {
                    for (int c = 0; c < col; c++) {
                        Cell cell = sheet.getCell(c, i);
                        value[c] = cell.getContents();
                    }
                    if (utility.checkPhoneNumber(value[5]) == true && utility.checkStudentName(value[0]) == true &&
                            utility.checkFatherName(value[1]) == true && utility.checkClassNumber(value[7]) == true && utility.checkBirthDate(value[8]) == true) {
                        try {
                            db.execSQL("insert into StudentData values('" + value[0] + "','" + value[1] + "','"
                                    + value[2] + "','" + utility.semester(HelpAndAbout.this) + "','" + value[4] + "','" + value[5] + "','"
                                    + value[6] + "','" + value[7] + "','" + value[8] + "','" + value[9] + "','"
                                    + value[10] + "','No update','" + value[12] + "')");
                            db.execSQL("insert into StudentValueFirst values('','','','','','','','','','" + value[7] + "','" + value[6] + "','" + value[4] + "')");
                            db.execSQL("insert into StudentValueSecond values('','','','','','','','','','" + value[7] + "','" + value[6] + "','" + value[4] + "')");
                            totalSucess++;
                        } catch (Exception e) {

                        }
                    }
                }
                Toast.makeText(this, totalSucess + " of " + row + " add sucessfully!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
            }
        } else if (requestCode == 12 && resultCode == RESULT_OK && data != null) {
            uri = data.getData();
            try {
                path = utility.getFilePath(HelpAndAbout.this, uri);
                saveTypeByExcel(path);
            } catch (Exception e) {

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
