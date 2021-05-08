package ethioteacher.dejen.ethioteachers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.*;
import android.os.Bundle;
import android.app.*;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class InfoAddEdit extends Activity {

    private String name = "";
    private String Number = "";
    private String grade = "";

    private EditText info;
    private Button add, edit;

    private FileWriter outputStream;
    private FileInputStream inputStream;

    private File root;
    private File folder;
    private File file;

    private String data = "";
    private String alldata = "";

	private Utility utility;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_add_edit);

		utility = new Utility();
		
        edit = (Button) findViewById(R.id.edit);
        add = (Button) findViewById(R.id.add);
        info = (EditText) findViewById(R.id.info);

        this.setTitle(name + "_" + grade + "_" + Number);
        try {
            name = getIntent().getStringExtra("Name");
            grade = getIntent().getStringExtra("Grade");
            Number = getIntent().getStringExtra("Number");
        } catch (Exception e) {
        }

        try {
            root = utility.getExternalFileDiroctory();
            if (!root.exists())
                root.mkdirs();
            folder = new File(root + "/Student_comment/");
            if (!folder.exists())
                folder.mkdirs();
            file = new File(folder + "/" + name + "_" + grade + "_" + Number + ".txt");

        } catch (Exception e) {
            add.setEnabled(false);
            edit.setEnabled(false);
        }

        try {
            data = "";
            alldata = "";
            inputStream = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            while ((data = reader.readLine()) != null) {
                alldata = alldata + data;
                info.setText("");
                info.setText(alldata);
            }
        } catch (Exception e) {

        }
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    outputStream = new FileWriter(file, false);
                    outputStream.write("\n\n___"+info.getText().toString());
                    outputStream.close();
                    Toast.makeText(InfoAddEdit.this, "Sucessfull!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(InfoAddEdit.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data = "";
                alldata = "";
                try {
                    inputStream = new FileInputStream(file);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    while ((data = reader.readLine()) != null) {
                        alldata = alldata + data;
                    }
                    info.setText("");
                    info.setText(alldata);
                } catch (Exception e) {

                }
            }
        });
    }

    @Override
    public void onBackPressed() {

        data = "";
        alldata = "";
        try {
            inputStream = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            while ((data = reader.readLine()) != null) {
                alldata = alldata + data;
            }
            if (alldata.equals(info.getText().toString())) {
                Intent intent = new Intent(InfoAddEdit.this, AdvancedSearch.class);
                startActivity(intent);
                finish();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(InfoAddEdit.this);
                builder.setMessage("Do you want to save?");
                builder.setNeutralButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            outputStream = new FileWriter(file, false);
                            outputStream.write(info.getText().toString());
                            outputStream.close();
                        } catch (Exception e) {
                        }
                        Intent intent = new Intent(InfoAddEdit.this, AdvancedSearch.class);
                        startActivity(intent);
                        finish();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Don't save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(InfoAddEdit.this, AdvancedSearch.class);
                        startActivity(intent);
                        finish();
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        } catch (Exception e) {
            Intent intent = new Intent(InfoAddEdit.this, AdvancedSearch.class);
            startActivity(intent);
            finish();
        }
    }
}
