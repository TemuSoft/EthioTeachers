package ethioteacher.dejen.ethioteachers;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AdvancedSearch extends Activity {

    private EditText editText;
    private ListView listView;
    private Cursor cursor;
    private SQLiteDatabase db;
    private String SemesterFirstOrSecond = "First";
    private String dataSearch[], numberHoldValueer[], gradeHoldValueer[], namePassedTo[];
    private String oldValue = "start",oldSpinner = "";
    private ArrayAdapter<String> adapter;
    private Spinner spinner;

    private Handler handler;

    private int size = 13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_search);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        try {
            size = Integer.parseInt(preferences.getString("size", "13"));
        } catch (Exception e) {

        }

        handler = new Handler();
        String values[] = {"Sname", "Fname", "GFname", "Phone_number", "Grade", "Sex"};
        if (new Utility().semester(this).equals("Second"))
            SemesterFirstOrSecond = "Second";

        this.setTitle("Search engine!");
        spinner = (Spinner) findViewById(R.id.spinner);
        db = new MainStudentDataDb(this, "STUDENT_DATA.db").getWritableDatabase();

        editText = (EditText) findViewById(R.id.editText);
        listView = (ListView) findViewById(R.id.listView);

        editText.setTextSize(size);
        editText.setAllCaps(true);

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
        first();
    }

    private void display() {
        oldValue = editText.getText().toString();
		oldSpinner = spinner.getSelectedItem().toString();
		
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataSearch);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AdvancedSearch.this);
                builder.setTitle("About");
                builder.setCancelable(false);
                builder.setMessage("Name:" + namePassedTo[(int) id] + "\nGrade:" + gradeHoldValueer[(int) id] + "\nNO:" + numberHoldValueer[(int) id] + "\nInfo Add,Edit&Delete");
                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(AdvancedSearch.this, InfoAddEdit.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("Name", namePassedTo[(int) id]);
                        bundle.putString("Grade", gradeHoldValueer[(int) id]);
                        bundle.putString("Number", numberHoldValueer[(int) id]);
                        intent.putExtras(bundle);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        first();
    }

    private void first() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!oldValue.equals(editText.getText().toString())||!oldSpinner.equals(spinner.getSelectedItem().toString())) {
                    try {
                        cursor = db.rawQuery("select Sname , Fname , GFname , Subject " +
                                ", Phone_number, Grade , Number , Sex ,Class_Work_10, Home_Work_5, Proj_and_Assign_10" +
                                " , Group_Work_5 , Mid_Exam_10 , Final_60 ,Total40,Total100 from StudentData inner join StudentValue"
                                + SemesterFirstOrSecond + " on"
                                + " " + spinner.getSelectedItem().toString() + " like '" + editText.getText().toString() + "%' and StudentValue" + SemesterFirstOrSecond
                                + ".NumberV = StudentData.Number "
                                + "and StudentValue" + SemesterFirstOrSecond + ".GradeV = StudentData.Grade ", null);
                        if (cursor.moveToFirst()) {
                            dataSearch = new String[cursor.getCount()];
                            numberHoldValueer = new String[cursor.getCount()];
                            gradeHoldValueer = new String[cursor.getCount()];
                            namePassedTo = new String[cursor.getCount()];

                            AdvancedSearch.this.setTitle(cursor.getCount() + " student available");
                            int totalGet = 0;
                            do {
                                dataSearch[totalGet] = "Name :" + cursor.getString(0) + " " + cursor.getString(1) + " " + cursor.getString(2)+ " :" + cursor.getString(7)
                                        + "\nSubject :" + cursor.getString(3) + " :" + cursor.getString(4)" :" + cursor.getString(5) + " :" + cursor.getString(6) 
                                        + "\n\tResult\nClass work :" + cursor.getString(8)
                                        + "\nHome work :" + cursor.getString(9)
                                        + "\nProj & Assign :" + cursor.getString(10)
                                        + "\nGruop work :" + cursor.getString(11)
                                        + "\nMid exam :" + cursor.getString(12)
                                        + "\nFinal :" + cursor.getString(13)
                                        + "\n\tTotal\nTotal40 :" + cursor.getString(14)
                                        + ",Total100 :" + cursor.getString(15) + "\n";
                                numberHoldValueer[totalGet] = cursor.getString(6);
                                gradeHoldValueer[totalGet] = cursor.getString(5);
                                namePassedTo[totalGet] = cursor.getString(0).toUpperCase() + "_" + cursor.getString(1).toUpperCase();
                                totalGet++;
                            } while (cursor.moveToNext());
                            display();
                        } else {
                            String s[] = {"No smilar data found!"};
                            AdvancedSearch.this.setTitle("0 student available");
                            adapter = new ArrayAdapter<String>(AdvancedSearch.this, android.R.layout.simple_list_item_1, s);
                            listView.setAdapter(adapter);
							oldValue = "start";
                            first();
                        }
                    } catch (Exception e) {
                        first();
                    }
                } else
                    first();
            }
        }, 1500);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AdvancedSearch.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
