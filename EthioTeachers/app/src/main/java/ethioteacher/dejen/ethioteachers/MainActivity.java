package ethioteacher.dejen.ethioteachers;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String resultHolder[], gradeHolder[], numberHolder[];
    private SQLiteDatabase account = null, db = null, dbImport = null;
    private Cursor cursor = null, cursor2 = null, cursorImport = null;
    private ListView listView;

    private String sendFailedStudent = "";
    private NavigationView navigationView;
    private String SemesterFirstOrSecond = "First";
    private String sqlSelectCommand = null;

    private FileOutputStream outputStream = null;
    private FileInputStream inputStream = null;

    private AlertDialog.Builder builder = null;

    private Utility utility;
    private int size = 13;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        try {
            size = Integer.parseInt(preferences.getString("size", "13"));
        } catch (Exception e) {
        }

        utility = new Utility();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        drawer.openDrawer(GravityCompat.START);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (utility.semester(this).equals("Second"))
            SemesterFirstOrSecond = "Second";

        account = new AccountHolder(this).getWritableDatabase();
        db = new MainStudentDataDb(this, "STUDENT_DATA.db").getWritableDatabase();

        try {
            cursor = account.rawQuery("select * from AccountLogin ", null);
            cursor2 = db.rawQuery("select * from StudentData", null);
        } catch (Exception op) {
        }

        listView = (ListView) findViewById(R.id.listview);
        String optionwwwww[] = {" ", "Detail result view", "Calculate Average", "Search"
                , "Top student", "Least student", "Calculate first semester result",
                "Calculate second semester result", "Change to default"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, optionwwwww) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setTextSize(size + 5);
                return view;
            }
        };
        if (cursor2.moveToFirst())
            listView.setAdapter(adapter);
        else
            listView.setAdapter(null);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listViewOnclickListener((int) id);
            }
        });


        try {
            if (getIntent().getStringExtra("CreateAccount").equals("fromLogin"))
                createAccountCommand();
        } catch (Exception p) {
        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10);
            }
        }
    }

    private void listViewOnclickListener(int id) {
        if ((int) id == 1) {
            Intent intent = new Intent(MainActivity.this, AdvancedResult.class);
            intent.putExtra("operation", "result");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        if ((int) id == 2) {
            Intent intent = new Intent(MainActivity.this, AdvancedResult.class);
            intent.putExtra("operation", "average");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        if ((int) id == 3) {
            Intent intent = new Intent(MainActivity.this, AdvancedSearch.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        if ((int) id == 4) {
            Intent intent = new Intent(MainActivity.this, AdvancedResult.class);
            intent.putExtra("operation", "top");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        if ((int) id == 5) {
            Intent intent = new Intent(MainActivity.this, AdvancedResult.class);
            intent.putExtra("operation", "least");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        if ((int) id == 6) {
            try {
                cursor = db.rawQuery("select GradeV,NumberV,Class_Work_10, Home_Work_5" +
                        ", Proj_and_Assign_10, Group_Work_5 , Mid_Exam_10 , Final_60 from StudentValueFirst", null);
                double FCW = 0, FHW = 0, FPA = 0, FGW = 0, FME = 0, FF = 0;
                if (cursor.moveToFirst()) {
                    do {
                        if (!cursor.getString(2).isEmpty() && !cursor.getString(3).isEmpty() && !cursor.getString(4).isEmpty()
                                && !cursor.getString(5).isEmpty() && !cursor.getString(6).isEmpty()) {
                            FCW = Double.parseDouble(cursor.getString(2));
                            FHW = Double.parseDouble(cursor.getString(3));
                            FPA = Double.parseDouble(cursor.getString(4));
                            FGW = Double.parseDouble(cursor.getString(5));
                            FME = Double.parseDouble(cursor.getString(6));

                            double Ftotal40 = FCW + FHW + FPA + FGW + FME;

                            if (!cursor.getString(7).isEmpty()) {
                                FF = Double.parseDouble(cursor.getString(7));
                                double Ftotal100 = Ftotal40 + FF;
                                db.execSQL("update StudentValueFirst set Total100 = " + Ftotal100 + " where NumberV = "
                                        + cursor.getString(1) + " and GradeV = '" + cursor.getString(0) + "'");
                            }
                            db.execSQL("update StudentValueFirst set Total40 = " + Ftotal40 + " where NumberV = "
                                    + cursor.getString(1) + " and GradeV = '" + cursor.getString(0) + "'");

                        }

                    } while (cursor.moveToNext());
                    Toast.makeText(MainActivity.this, "SucessFull!", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
            cursor = null;
        }
        if ((int) id == 7) {
            try {
                cursor = db.rawQuery("select GradeV,NumberV,Class_Work_10, Home_Work_5" +
                        ", Proj_and_Assign_10, Group_Work_5 , Mid_Exam_10 , Final_60 from StudentValueSecond", null);
                double FCW = 0, FHW = 0, FPA = 0, FGW = 0, FME = 0, FF = 0;
                if (cursor.moveToFirst()) {
                    do {
                        if (!cursor.getString(2).isEmpty() && !cursor.getString(3).isEmpty() && !cursor.getString(4).isEmpty()
                                && !cursor.getString(5).isEmpty() && !cursor.getString(6).isEmpty()) {
                            FCW = Double.parseDouble(cursor.getString(2));
                            FHW = Double.parseDouble(cursor.getString(3));
                            FPA = Double.parseDouble(cursor.getString(4));
                            FGW = Double.parseDouble(cursor.getString(5));
                            FME = Double.parseDouble(cursor.getString(6));

                            double Ftotal40 = FCW + FHW + FPA + FGW + FME;

                            if (!cursor.getString(7).isEmpty()) {
                                FF = Double.parseDouble(cursor.getString(7));
                                double Ftotal100 = Ftotal40 + FF;
                                db.execSQL("update StudentValueSecond set Total100 = " + Ftotal100 + " where NumberV = "
                                        + cursor.getString(1) + " and GradeV = '" + cursor.getString(0) + "'");
                            }
                            db.execSQL("update StudentValueSecond set Total40 = " + Ftotal40 + " where NumberV = "
                                    + cursor.getString(1) + " and GradeV = '" + cursor.getString(0) + "'");
                        }

                    } while (cursor.moveToNext());
                    Toast.makeText(MainActivity.this, "SucessFull!", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
            cursor = null;
        }
        if ((int) id == 8) {
            try {
                db.execSQL("update StudentValueSecond set Total100 = '',Total40 =''");
                db.execSQL("update StudentValueFirst set Total100 = '',Total40 =''");
                Toast.makeText(this, "Sucesssfull!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void createAccountCommand() {
        account = new AccountHolder(this).getWritableDatabase();

        cursor = account.rawQuery("select userName ,password from AccountLogin", null);

        if (cursor.moveToFirst()) {

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setTitle("Account");
            builder.setMessage("Do u want to create account?");

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(MainActivity.this, CreateAccount.class);
                    startActivity(intent);
                    finish();
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("No Thanks!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.DBexport) {
            FileOutputStream outputStream = null;
            FileInputStream inputStream = null;
            File backupFolder = utility.getExternalFileDiroctory();
            if (!backupFolder.exists())
                backupFolder.mkdirs();
            File file = new File(backupFolder + "/STUDENT_DATA.db");

            try {
                outputStream = new FileOutputStream(file);
                if (Build.VERSION.SDK_INT >= 17) {
                    inputStream = new FileInputStream(getApplicationContext().getDatabasePath("STUDENT_DATA.db"));

                } else
                    inputStream = new FileInputStream("/STUDENT_DATA.db");

                FileChannel to = outputStream.getChannel();
                FileChannel from = inputStream.getChannel();
                from.transferTo(0, from.size(), to);
                if (from != null)
                    from.close();
                if (to != null)
                    to.close();
                Toast.makeText(this, "Backup sucessfull!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.DBimport) {
            try {
                Intent intent;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                else
                    intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/*");
                startActivityForResult(intent, 10);
            } catch (Exception e) {
            }

        } else if (id == R.id.nav_slideshow) {
            builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you want table?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(MainActivity.this, HelpAndAbout.class);
                    intent.putExtra("operation", "showAll");
                    startActivity(intent);
                    finish();
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("No!,Thanks!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    sqlSelectCommand = "select Grade,Number, Sname , Fname , GFname , Semester , Subject " +
                            ", Phone_number, Grade , Number , Birth_Date , Sex ,Class_Work_10, Home_Work_5, " +
                            "Proj_and_Assign_10 , Group_Work_5 , Mid_Exam_10 , Final_60 ,Total40,Total100,Date_Register," +
                            "Date_last_update from StudentData inner join StudentValue" + SemesterFirstOrSecond + " on "
                            + " StudentValue" + SemesterFirstOrSecond + ".NumberV = StudentData.Number"
                            + " and StudentValue" + SemesterFirstOrSecond + ".GradeV = StudentData.Grade";
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
                                        + "\n" + cursor.getColumnName(19) + "     :" + cursor.getString(19)
                                        + "\n" + cursor.getColumnName(20) + "     :" + cursor.getString(20)
                                        + "\n" + cursor.getColumnName(21) + "     :" + cursor.getString(21) + "\n";
                                gradeHolder[totalCount] = cursor.getString(0);
                                numberHolder[totalCount] = cursor.getString(1);
                                totalCount++;
                            } while (cursor.moveToNext());
                        }

                        Intent intent = new Intent(MainActivity.this, ListViewClass.class);
                        Bundle bundle = new Bundle();
                        bundle.putStringArray("resultHolder", resultHolder);
                        bundle.putStringArray("gradeHolder", gradeHolder);
                        bundle.putStringArray("numberHolder", numberHolder);
                        bundle.putString("operation", "allstudent");
                        intent.putExtras(bundle);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        dialog.dismiss();
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, e.toString() + "search detail", Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(MainActivity.this, Setting.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            Intent intent;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            else
                intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("application/vnd.ms-excel");
            startActivityForResult(intent, 70);

        } else if (id == R.id.nav_send) {
            sqlSelectCommand = "select Sname , Fname , GFname , Semester , Subject " +
                    ", Phone_number, Grade , Number , Birth_Date , Sex ,School,Class_Work_10, Home_Work_5, " +
                    "Proj_and_Assign_10 , Group_Work_5 , Mid_Exam_10 , Final_60 ,Total40,Total100" +
                    " from StudentData inner join StudentValue" + SemesterFirstOrSecond + " on "
                    + " StudentValue" + SemesterFirstOrSecond + ".NumberV = StudentData.Number"
                    + " and StudentValue" + SemesterFirstOrSecond + ".GradeV = StudentData.Grade";

            cursor = db.rawQuery(sqlSelectCommand, null);

            builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Send all!!!");
            builder.setMessage("Are you sure? \n" + "uses " + cursor.getCount() * 0.35 + " Birr Airtime");
            builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int totalsend = 0;
                    if (cursor.moveToFirst()) {
                        if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                            do {
                                try {
                                    String text = cursor.getString(0) + " " + cursor.getString(1) + " " + cursor.getString(2)
                                            + "\nSem:" + cursor.getString(3)
                                            + "\nSub:" + cursor.getString(4)
                                            + "\nGra:" + cursor.getString(6) + ":" + cursor.getString(7) + "\nSex :" + cursor.getString(9)
                                            + "\nResult\n"
                                            + "CW10=" + cursor.getString(11) + "\nHW5=" + cursor.getString(12) + "\nPA10="
                                            + cursor.getString(13) + "\nGW5=" + cursor.getString(14) + "\nME 10="
                                            + cursor.getString(15) + "\nFinal=" + cursor.getString(16)
                                            + "\n40=" + cursor.getString(17) + "\n100=" + cursor.getString(18);

                                    String phoneNumber = cursor.getString(5);
                                    if (!phoneNumber.isEmpty()) {
                                        SmsManager smsManager = SmsManager.getDefault();
                                        ArrayList<String> list = smsManager.divideMessage(text);
                                        smsManager.sendMultipartTextMessage(phoneNumber, null, list, null, null);
                                        totalsend++;
                                    } else
                                        sendFailedStudent += cursor.getString(0) + " " + cursor.getString(1) + " " + cursor.getString(2) + "\n";
                                } catch (Exception w) {
                                    sendFailedStudent += cursor.getString(0) + " " + cursor.getString(1) + " " + cursor.getString(2) + "\n";
                                }
                            }
                            while (cursor.moveToNext());
                            builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle("Failed & Sucess");
                            builder.setMessage("Failed " + (cursor.getCount() - totalsend) + "\n" + sendFailedStudent + "\nSucess " + totalsend);
                            builder.setCancelable(false);
                            builder.setPositiveButton("Ok!", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int num) {
                                    dialog.dismiss();
                                    sendFailedStudent = "";
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        } else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(new String[]{android.Manifest.permission.SEND_SMS}, 10);
                            }
                        }

                    }
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else if (id == R.id.CreateAccount) {
            Intent intent = new Intent(this, CreateAccount.class);
            startActivity(intent);
        } else if (id == R.id.UpdateAccount) {
            Intent intent = new Intent(this, UpdateAccount.class);
            startActivity(intent);
        } else if (id == R.id.DeleteAccount) {
            Intent intent = new Intent(this, DeleteAccount.class);
            startActivity(intent);
        } else if (id == R.id.AddStudent) {
            if (preferences.getBoolean("schoolOrManual", false) == true) {
                Intent intent = new Intent(MainActivity.this, AddStudent.class);
                startActivity(intent);
                finish();
            } else {
                try {
                    Intent intent;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                        intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    else
                        intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("application/vnd.ms-excel");
                    startActivityForResult(intent, 45);
                } catch (Exception e) {
                }
            }
        } else if (id == R.id.UpdateStudent) {
            Intent intent = new Intent(this, UpdateStudent.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.DeletStudent) {
            Intent intent = new Intent(this, DeleteStudents.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.SearchStudent) {
            Intent intent = new Intent(this, SearchStudent.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.exit) {
            builder = new AlertDialog.Builder(this);
            builder.setTitle("Exit");
            builder.setMessage("Are you sure?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    android.os.Process.killProcess(android.os.Process.myPid());
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    dialog.cancel();
                }
            });

            AlertDialog alertDialog1 = builder.create();
            alertDialog1.show();

        } else if (id == R.id.Help) {
            Intent intent = new Intent(this, AboutAndHelp.class);
            intent.putExtra("operation", "help");
            startActivity(intent);
        } else if (id == R.id.about) {
            Intent intent = new Intent(this, AboutAndHelp.class);
            intent.putExtra("operation", "about");
            startActivity(intent);
        }

        // DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        // drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String path = null, grade = preferences.getString("grade", "").toUpperCase();
        int totalSucess = 0;
        Uri uri = null;

        if (requestCode == 45 && resultCode == RESULT_OK && data != null) {
            uri = data.getData();
            try {
                path = utility.getFilePath(MainActivity.this, uri);
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
                    if (grade.equals(value[6].toUpperCase())) {
                        if (utility.checkPhoneNumber(value[5]) == true && utility.checkStudentName(value[0]) == true &&
                                utility.checkFatherName(value[1]) == true && utility.checkClassNumber(value[7]) == true
                                && utility.checkBirthDate(value[8]) == true) {
                            try {
                                db.execSQL("insert into StudentData values('" + value[0] + "','" + value[1] + "','"
                                        + value[2] + "','" + utility.semester(MainActivity.this) + "','"
                                        + preferences.getString("subject", "Null") + "','" + value[5] + "','"
                                        + value[6] + "','" + value[7] + "','" + value[8] + "','" + value[9] + "','"
                                        + value[10] + "','No update','" + value[12] + "')");
                                db.execSQL("insert into StudentValueFirst values('','','','','','','','','','" + value[7] + "','" + value[6] + "')");
                                db.execSQL("insert into StudentValueSecond values('','','','','','','','','','" + value[7] + "','" + value[6] + "')");
                                totalSucess++;
                            } catch (Exception e) {

                            }
                        }
                    }
                }
                inputStream.close();
                Toast.makeText(this, totalSucess + " of " + row + " register sucessfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(this, "" + e.toString(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(MainActivity.this, "\t\tFailed\nMove excel file to Internal storage.", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == 10 && resultCode == RESULT_OK && data != null) {
            uri = data.getData();
            try {
                path = utility.getFilePath(MainActivity.this, uri);
            } catch (Exception e) {

            }

            if (preferences.getBoolean("ByDelete", false) == true) {
                importStudentDataByDeleteOld(new File(path));
            } else
                importWithoutDeleteOldData(path);
        } else if (requestCode == 70 && resultCode == RESULT_OK && data != null) {
            try {
                path = new Utility().getFilePath(MainActivity.this, data.getData());
                Uri uril = Uri.parse(path);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("application/*");
                intent.putExtra(Intent.EXTRA_STREAM, data.getData());
                startActivity(Intent.createChooser(intent, "Send file"));
            } catch (Exception e) {

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu = navigationView.getMenu();
        MenuItem menuItem;

        menuItem = menu.findItem(R.id.Help);
        menuItem.setEnabled(false);

        if (cursor.moveToFirst()) {
            menuItem = menu.findItem(R.id.CreateAccount);
            menuItem.setEnabled(false);
        } else {
            menuItem = menu.findItem(R.id.UpdateAccount);
            menuItem.setEnabled(false);
            menuItem = menu.findItem(R.id.DeleteAccount);
            menuItem.setEnabled(false);
        }

        if (!cursor2.moveToFirst()) {
            menuItem = menu.findItem(R.id.UpdateStudent);
            menuItem.setEnabled(false);
            menuItem = menu.findItem(R.id.SearchStudent);
            menuItem.setEnabled(false);
            menuItem = menu.findItem(R.id.DeletStudent);
            menuItem.setEnabled(false);
            menuItem = menu.findItem(R.id.DBexport);
            menuItem.setEnabled(false);
            menuItem = menu.findItem(R.id.nav_slideshow);
            menuItem.setEnabled(false);
            menuItem = menu.findItem(R.id.nav_send);
            menuItem.setEnabled(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        cursor = db.rawQuery("select * from StudentData", null);
        if (cursor.moveToFirst()) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Exit");
            alertDialog.setMessage("Are you sure?");
            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    android.os.Process.killProcess(android.os.Process.myPid());
                    dialog.dismiss();
                }
            });
            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            AlertDialog alertDialog1 = alertDialog.create();
            alertDialog1.show();
        } else
            android.os.Process.killProcess(android.os.Process.myPid());
        //  super.onBackPressed();
    }

    private void importStudentDataByDeleteOld(File backupFolder) {
        if (backupFolder.exists()) {
            try {
                if (Build.VERSION.SDK_INT >= 17)
                    outputStream = new FileOutputStream(MainActivity.this.getApplicationContext().getDatabasePath("STUDENT_DATA.db"));
                else
                    outputStream = new FileOutputStream("/STUDENT_DATA.db");

                inputStream = new FileInputStream(backupFolder);
                FileChannel from = inputStream.getChannel();
                FileChannel to = outputStream.getChannel();
                from.transferTo(0, from.size(), to);
                if (from != null)
                    from.close();
                if (to != null)
                    to.close();
                Toast.makeText(MainActivity.this, "Import sucessfull!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else
            Toast.makeText(MainActivity.this, "No data found!", Toast.LENGTH_SHORT).show();
    }

    private void importWithoutDeleteOldData(String path) {
        try {
            dbImport = new MainStudentDataDb(MainActivity.this, path).getWritableDatabase();
            cursorImport = dbImport.rawQuery("select " +
                    "Sname ,Fname , " +
                    "GFname , Semester , " +
                    "Subject ,Phone_number, " +
                    "Grade , Number , " +
                    "Birth_Date ,Sex ," +
                    "Date_Register," +
                    "Date_last_update,School," +
                    "StudentValueFirst.Class_Work_10, StudentValueFirst.Home_Work_5, " +
                    "StudentValueFirst.Proj_and_Assign_10 , StudentValueFirst.Group_Work_5 ," +
                    " StudentValueFirst.Mid_Exam_10 , StudentValueFirst.Final_60 ," +
                    "StudentValueFirst.Total40,StudentValueFirst.Total100," +
                    "StudentValueFirst.Last_update_date," +
                    "StudentValueSecond.Home_Work_5,StudentValueSecond.Home_Work_5, " +
                    "StudentValueSecond.Proj_and_Assign_10 , StudentValueSecond.Group_Work_5 ," +
                    " StudentValueSecond.Mid_Exam_10 , StudentValueSecond.Final_60 ," +
                    "StudentValueSecond.Total40,StudentValueSecond.Total100," +
                    "StudentValueSecond.Last_update_date" +
                    " from StudentData inner join StudentValueFirst on "
                    + " StudentValueFirst.NumberV = StudentData.Number"
                    + " and StudentValueFirst.GradeV = StudentData.Grade inner join StudentValueSecond on" +
                    " StudentValueSecond.NumberV = StudentData.Number" +
                    " and StudentValueSecond.GradeV = StudentData.Grade", null);
        } catch (Exception e) {

        }

        if (cursorImport.moveToFirst()) {
            db = new MainStudentDataDb(MainActivity.this, "STUDENT_DATA.db").getWritableDatabase();
            do {
                try {
                    db.execSQL("insert into StudentData values ('"
                            + cursorImport.getString(0) + "','" + cursorImport.getString(1)
                            + "','" + cursorImport.getString(2) + "','" + cursorImport.getString(3)
                            + "','" + cursorImport.getString(4) + "','" + cursorImport.getString(5)
                            + "','" + cursorImport.getString(6) + "','" + cursorImport.getString(7)
                            + "','" + cursorImport.getString(8) + "','" + cursorImport.getString(9)
                            + "','" + cursorImport.getString(10) + "','" + cursorImport.getString(11)
                            + "','" + cursorImport.getString(12) + "')");
                    db.execSQL("insert into StudentValueFirst values('"
                            + cursorImport.getString(13) + "','" + cursorImport.getString(14)
                            + "','" + cursorImport.getString(15) + "','" + cursorImport.getString(16)
                            + "','" + cursorImport.getString(17) + "','" + cursorImport.getString(18)
                            + "','" + cursorImport.getString(19) + "','" + cursorImport.getString(20)
                            + "','" + cursorImport.getString(21) + "','" + Integer.parseInt(cursorImport.getString(7)) + "'" +
                            ",'" + cursorImport.getString(6) + "')");
                    db.execSQL("insert into StudentValueSecond values('"
                            + cursorImport.getString(22) + "','" + cursorImport.getString(23)
                            + "','" + cursorImport.getString(24) + "','" + cursorImport.getString(25)
                            + "','" + cursorImport.getString(26) + "','" + cursorImport.getString(27)
                            + "','" + cursorImport.getString(28) + "','" + cursorImport.getString(29)
                            + "','" + cursorImport.getString(30) + "','" + Integer.parseInt(cursorImport.getString(7)) + "'" +
                            ",'" + cursorImport.getString(6) + "')");
                } catch (Exception e) {

                }
            }
            while (cursorImport.moveToNext());
            Toast.makeText(this, "Import finish", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(this, "No data found!", Toast.LENGTH_SHORT).show();
    }
}