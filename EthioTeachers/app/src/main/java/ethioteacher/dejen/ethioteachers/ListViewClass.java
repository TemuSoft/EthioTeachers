package ethioteacher.dejen.ethioteachers;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ListViewClass extends AppCompatActivity {

    private ListView listView;
    private String buttonActivatde;
    private SQLiteDatabase db;

    private Cursor cursor = null;
    private ArrayAdapter<String> adapter;
    private String resultHolder[] = null, gradeHolder[] = null, numberHolder[] = null, updateRequest[] = null;
    private String sqlSelectCommand;
    private int id;
    private Intent intent;
    private String SemesterFirstOrSecond = "First";
    private String sendFailedStudent = "";
    private File fileTime;
    private int deleteTotalRemain = 0;
    private int size = 13;
    private SharedPreferences preferences;
    private Utility utility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_class);

        utility = new Utility();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        try {
            size = Integer.parseInt(preferences.getString("size", "13"));
        } catch (Exception e) {

        }
        fileTime = new File(this.getFilesDir(), "/Teme/");
        if (!fileTime.exists())
            fileTime.mkdirs();

        if (new Utility().semester(this).equals("Second"))
            SemesterFirstOrSecond = "Second";

        db = new MainStudentDataDb(this,"STUDENT_DATA.db").getWritableDatabase();
        listView = (ListView) findViewById(R.id.listview);

        try {
            buttonActivatde = getIntent().getStringExtra("operation");
        } catch (Exception u) {
        }

        try {
            resultHolder = getIntent().getStringArrayExtra("resultHolder");
        } catch (Exception e) {
        }
        try {
            gradeHolder = getIntent().getStringArrayExtra("gradeHolder");
        } catch (Exception e) {
        }
        try {
            numberHolder = getIntent().getStringArrayExtra("numberHolder");
        } catch (Exception e) {
        }
        try {
            updateRequest = getIntent().getStringArrayExtra("updateRequest");
        } catch (Exception e) {
        }
        refresh();

        if (buttonActivatde.equals("search")) {
            ListViewClass.this.setTitle(listView.getCount() + " student search view");
        } else if (buttonActivatde.equals("delete")) {
            ListViewClass.this.setTitle(listView.getCount() + " student delete view");
        } else if (buttonActivatde.equals("allstudent")) {
            ListViewClass.this.setTitle("All " + listView.getCount() + " student registered view");
        } else if (buttonActivatde.equals("updateResult")) {
            ListViewClass.this.setTitle(listView.getCount() + " student result update view");
        } else if (buttonActivatde.equals("update")) {
            ListViewClass.this.setTitle(listView.getCount() + " student info update view");
        } else if (buttonActivatde.equals("searchMore")) {
            ListViewClass.this.setTitle(listView.getCount() + " student search by result view");
        }
    }

    @Override
    public void onBackPressed() {
        if (buttonActivatde.equals("delete")) {
            intent = new Intent(this, DeleteStudents.class);
        } else if (buttonActivatde.equals("update")) {
            intent = new Intent(this, UpdateStudent.class);
        } else if (buttonActivatde.equals("allstudent")) {
            intent = new Intent(this, MainActivity.class);
        } else if (buttonActivatde.equals("search")) {
            intent = new Intent(this, SearchStudent.class);
        } else if (buttonActivatde.equals("searchMore")) {
            intent = new Intent(this, SearchStudentMore.class);
        } else if (buttonActivatde.equals("updateResult")) {
            intent = new Intent(this, UpdateStudentMore.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.deleteAll) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ListViewClass.this);
            builder.setTitle("Delete all!!!");
            builder.setMessage("Are you sure?");
            builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    for (int i = 0; i < listView.getCount(); i++) {
                        try {
                            db.execSQL("delete from StudentData where  Grade = " + "'" + gradeHolder[i]
                                    + "' and Number = " + "'" + numberHolder[i] + "'");
                            db.execSQL("delete from StudentValueFirst where  GradeV = " + "'" + gradeHolder[i]
                                    + "' and NumberV = " + "'" + numberHolder[i] + "'");
                            db.execSQL("delete from StudentValueSecond where  GradeV = " + "'" + gradeHolder[i]
                                    + "' and NumberV = " + "'" + numberHolder[i] + "'");
                        } catch (Exception ee) {
                        }
                        listView.setAdapter(null);
                        ListViewClass.this.setTitle("0 student delete remain");
                    }

                    listView.setAdapter(null);
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

        } else if (id == R.id.editall) {
            int totalUpdate = listView.getCount();
            try {
                if (updateRequest[8].equals("")) {
                    if (buttonActivatde.equals("update")) {
                        Intent intent = new Intent(ListViewClass.this, UpdateStudentEditable.class);
                        Bundle bundle = new Bundle();
                        bundle.putStringArray("updateRequest", updateRequest);
                        bundle.putStringArray("numberHolder", numberHolder);
                        bundle.putStringArray("gradeHolder", gradeHolder);
                        bundle.putInt("totalUpdate", totalUpdate);
                        bundle.putString("updateType", "all");
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                } else
                    Toast.makeText(this, "you can't update all student age!", Toast.LENGTH_SHORT).show();
            } catch (Exception f) {

            }
        } else if (id == R.id.save) {
            try {
                if (Build.VERSION.SDK_INT >= 19)
                    intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                else
                    intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("text/txt");
                startActivityForResult(intent, 20);
            } catch (Exception e) {

            }
        } else if (id == R.id.send) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ListViewClass.this);
            builder.setTitle("Send all current list!!");
            builder.setMessage("Are you sure? \n" + "uses " + listView.getCount() * 0.35 + " Birr Airtime");
            builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int totalsend = 0;
                    if (ActivityCompat.checkSelfPermission(ListViewClass.this, Manifest.permission.SEND_SMS)
                            == PackageManager.PERMISSION_GRANTED) {
                        for (int i = 0; i < listView.getCount(); i++) {
                            sqlSelectCommand = "select Sname , Fname , GFname , Semester , Subject " +
                                    ", Phone_number, Grade , Number , Birth_Date , Sex ,School,Class_Work_10, Home_Work_5, " +
                                    "Proj_and_Assign_10 , Group_Work_5 , Mid_Exam_10 , Final_60 ,Total40,Total100" +
                                    " from StudentData inner join StudentValue" + SemesterFirstOrSecond + " on "
                                    + " StudentValue" + SemesterFirstOrSecond + ".NumberV = StudentData.Number"
                                    + " and StudentValue" + SemesterFirstOrSecond + ".GradeV = StudentData.Grade "
                                    + " and Grade = '" + gradeHolder[i] + "' and Number = " + numberHolder[i];
                            try {
                                cursor = db.rawQuery(sqlSelectCommand, null);
                                cursor.moveToFirst();
                                String text = cursor.getString(0) + " " + cursor.getString(1) + " " + cursor.getString(2)+" :" + cursor.getString(9)
                                        + "\nSem:" + cursor.getString(3)+" :" + cursor.getString(4)+" :" + cursor.getString(6) + " :" + cursor.getString(7) 
                                        + "\n\tResult\n"
                                        + "CW10 :" + cursor.getString(11) + ",HW5 :" + cursor.getString(12) + ",PA10 :"
                                        + cursor.getString(13) + "\nGW5 :" + cursor.getString(14) + ",ME10 :"
                                        + cursor.getString(15) + ",Final :" + cursor.getString(16)
                                        + "\n\tTotal\n40% :" + cursor.getString(17) + ",100% :" + cursor.getString(18);

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
                        while (cursor.moveToNext()) ;
                        AlertDialog.Builder builder = new AlertDialog.Builder(ListViewClass.this);
                        builder.setTitle("Failed & Sucess");
                        builder.setMessage("Failed " + (listView.getCount() - totalsend) + "\n" + sendFailedStudent + "\nSucess " + totalsend);
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
                            requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 10);
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
        } else if (id == R.id.start) {
            Intent intent = new Intent(ListViewClass.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return true;
    }

    public void refresh() {
        adapter = new ArrayAdapter<String>(this, R.layout.layout, resultHolder) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setTextSize(size + 2);
                return view;
            }
        };
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setID(id);
                if (buttonActivatde.equals("search")) {
                    sendAndCallToStudent(getID());
                    ListViewClass.this.setTitle(listView.getCount() + " student search view");
                } else if (buttonActivatde.equals("delete")) {
                    delteCurrentSelectd(getID());
                    ListViewClass.this.setTitle(listView.getCount() + " student delete view");
                } else if (buttonActivatde.equals("allstudent")) {
                    sendAndCallToStudent(getID());
                    ListViewClass.this.setTitle("All " + listView.getCount() + " student registered view");
                } else if (buttonActivatde.equals("updateResult")) {
                    updateCurrentStudentDetail(getID());
                    ListViewClass.this.setTitle(listView.getCount() + " student result update view");
                } else if (buttonActivatde.equals("update")) {
                    updateCurrentStudent(getID());
                    ListViewClass.this.setTitle(listView.getCount() + " student info update view");
                } else if (buttonActivatde.equals("searchMore")) {
                    sendAndCallToStudent(getID());
                    ListViewClass.this.setTitle(listView.getCount() + " student search by result view");
                }
            }
        });
    }

    private void updateCurrentStudentDetail(int id) {
        String grade = gradeHolder[id];
        int number = Integer.parseInt(numberHolder[id]);
        Intent intent = new Intent(ListViewClass.this, UpdateStudentMoreEditable.class);
        Bundle bundle = new Bundle();
        bundle.putStringArray("updateRequest", updateRequest);
        bundle.putString("GradeV", grade);
        bundle.putInt("NumberV", number);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void updateCurrentStudent(int id) {
        String grade = gradeHolder[id];
        int number = Integer.parseInt(numberHolder[id]);
        Intent intent = new Intent(ListViewClass.this, UpdateStudentEditable.class);
        Bundle bundle = new Bundle();
        bundle.putStringArray("updateRequest", updateRequest);
        bundle.putString("GradeV", grade);
        bundle.putInt("NumberV", number);
        bundle.putString("updateType", "one");
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void delteCurrentSelectd(int id) {

        sqlSelectCommand = "select Sname , Fname , GFname , Semester , Subject " +
                ", Phone_number, Grade , Number , Birth_Date , Sex from StudentData" +
                " where Grade = '" + gradeHolder[id] + "' and Number = " + numberHolder[id];
        try {
            cursor = db.rawQuery(sqlSelectCommand, null);
            if (cursor.moveToFirst()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ListViewClass.this);
                builder.setTitle("Delete");
                builder.setCancelable(false);
                builder.setMessage("Are you sure?\n\nName :" + cursor.getString(0) + " " + cursor.getString(1) + " " + cursor.getString(2)
                        + "\nGrade :" + cursor.getString(6) + " :" + cursor.getString(7));
                builder.setNeutralButton("Cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            db.execSQL("delete from StudentData where  Grade = " + "'" + gradeHolder[getID()]
                                    + "' and Number = " + "'" + numberHolder[getID()] + "'");
                            Toast.makeText(ListViewClass.this, "Sucessfull!", Toast.LENGTH_SHORT).show();
                        } catch (Exception ee) {
                            Toast.makeText(ListViewClass.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                        try {
                            db.execSQL("delete from StudentValueFirst where  GradeV = " + "'" + gradeHolder[getID()]
                                    + "' and NumberV = " + "'" + numberHolder[getID()] + "'");
                        } catch (Exception w) {
                        }
                        try {
                            db.execSQL("delete from StudentValueSecond where  GradeV = " + "'" + gradeHolder[getID()]
                                    + "' and NumberV = " + "'" + numberHolder[getID()] + "'");
                            if (preferences.getBoolean("update", false) == true)
                                new Utility().backUp(ListViewClass.this);
                            resultHolder[getID()] = "Deleted befor ...";
                            deleteTotalRemain++;
                        } catch (Exception ee) {
                        }
                        ListViewClass.this.setTitle(listView.getCount() - deleteTotalRemain + " student delete remain");
                        refresh();
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        } catch (Exception e) {
        }
    }

    private void sendAndCallToStudent(int id) {
        sqlSelectCommand = "select Sname , Fname , GFname , Semester , Subject " +
                ", Phone_number, Grade , Number , Birth_Date , Sex ,School,Class_Work_10, Home_Work_5, " +
                "Proj_and_Assign_10 , Group_Work_5 , Mid_Exam_10 , Final_60 ,Total40,Total100" +
                " from StudentData inner join StudentValue" + SemesterFirstOrSecond + " on "
                + " StudentValue" + SemesterFirstOrSecond + ".NumberV = StudentData.Number"
                + " and StudentValue" + SemesterFirstOrSecond + ".GradeV = StudentData.Grade "
                + " and Grade = '" + gradeHolder[id] + "' and Number = " + numberHolder[id];
        try {
            cursor = db.rawQuery(sqlSelectCommand, null);
            if (cursor.moveToFirst()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ListViewClass.this);
                builder.setCancelable(false);
                builder.setMessage("Name :" + cursor.getString(0) + " " + cursor.getString(1) + " " + cursor.getString(2)
                        + "\nGrade :" + cursor.getString(6) + " :" + cursor.getString(7) + "\nSchool :" + cursor.getString(10));
                builder.setNeutralButton("Cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Call", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + cursor.getString(5)));
                        if (ActivityCompat.checkSelfPermission(ListViewClass.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)
                            startActivity(intent);
                        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 10);
                        }
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (ActivityCompat.checkSelfPermission(ListViewClass.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                            try {
                                String text = cursor.getString(0) + " " + cursor.getString(1) + " " + cursor.getString(2)+" :" + cursor.getString(9)
                                        + "\nSem:" + cursor.getString(3)+" :" + cursor.getString(4)+" :" + cursor.getString(6) + " :" + cursor.getString(7) 
                                        + "\n\tResult\n"
                                        + "CW10 :" + cursor.getString(11) + ",HW5 :" + cursor.getString(12) + ",PA10 :"
                                        + cursor.getString(13) + "\nGW5 :" + cursor.getString(14) + ",ME10 :"
                                        + cursor.getString(15) + ",Final :" + cursor.getString(16)
                                        + "\n\tTotal\n40% :" + cursor.getString(17) + ",100% :" + cursor.getString(18);

                                String phoneNumber = cursor.getString(5);
                                if (!phoneNumber.isEmpty()) {
                                    SmsManager smsManager = SmsManager.getDefault();
                                    ArrayList<String> list = smsManager.divideMessage(text);
                                    smsManager.sendMultipartTextMessage(phoneNumber, null, list, null, null);
                                    Toast.makeText(ListViewClass.this, "Send", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception w) {
                                Toast.makeText(ListViewClass.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 10);
                            }
                        }

                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if (buttonActivatde.equals("search")) {
            menu.getItem(0).setEnabled(false);
            menu.getItem(1).setEnabled(false);
        } else if (buttonActivatde.equals("delete")) {
            menu.getItem(1).setEnabled(false);
			menu.getItem(2).getEnabled(false);
        } else if (buttonActivatde.equals("allstudent")) {
            menu.getItem(0).setEnabled(false);
            menu.getItem(1).setEnabled(false);
			menu.getItem(2).getEnabled(false);
        } else if (buttonActivatde.equals("updateResult")) {
            menu.getItem(0).setEnabled(false);
            menu.getItem(1).setEnabled(false);
			menu.getItem(2).getEnabled(false);
        } else if (buttonActivatde.equals("update")) {
            menu.getItem(0).setEnabled(false);
			menu.getItem(2).getEnabled(false);
        } else if (buttonActivatde.equals("searchMore")) {
            menu.getItem(0).setEnabled(false);
            menu.getItem(1).setEnabled(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private int getID() {
        return id;
    }

    private void setID(long id) {
        this.id = (int) id;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri uri = null;
        String path = null;
        if (requestCode == 20 && resultCode == RESULT_OK && data != null) {
            uri = data.getData();
            try {
                path = utility.getFilePath(ListViewClass.this, uri);
                saveTextFile(path);
            } catch (Exception e) {

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void saveTextFile(String path) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(path+".txt");
            for (int i = 0; i < listView.getCount(); i++) {
                fileOutputStream.write(i);
                fileOutputStream.write(":-".getBytes());
                fileOutputStream.write(resultHolder[i].getBytes());
                fileOutputStream.write("\n".getBytes());
            }
            fileOutputStream.close();
            Toast.makeText(this, "Save", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
        }

    }
}