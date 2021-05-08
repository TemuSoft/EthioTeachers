package ethioteacher.dejen.ethioteachers;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;

public class ListViewForAdvanced extends AppCompatActivity {

    private ListView listView;
    private int Female, Male;
    private String dataRetrived[] = null;
    private SharedPreferences preferences;
    private int size = 13;
    private Utility utility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_for_advanced);

        utility = new Utility();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        try {
            size = Integer.parseInt(preferences.getString("size", "13"));
        } catch (Exception e) {

        }

        listView = (ListView) findViewById(R.id.listView);
        this.setTitle(getIntent().getStringExtra("title"));

        Female = getIntent().getIntExtra("Female", 0);
        Male = getIntent().getIntExtra("Male", 0);

        dataRetrived = getIntent().getStringArrayExtra("dataRetrived");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataRetrived) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setTextSize(size);
                return view;
            }
        };
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_listview_advanced, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        if (item.getItemId() == R.id.countAll) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ListViewForAdvanced.this);
            builder.setMessage("All in\n" + getIntent().getStringExtra("alert") + "\n\n\t\tFemale = " + Female
                    + "\n\t\t Male =" + Male + "\n\t\tTotal =" + (Female + Male));
            builder.setCancelable(false);
            builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else if (item.getItemId() == R.id.saveResult) {
            if (Build.VERSION.SDK_INT >= 19)
                intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
            else
                intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("text/txt");
            startActivityForResult(intent, 30);
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String path = null;
        if (requestCode == 30 && resultCode == RESULT_OK && data != null) {
            try {
                path = utility.getFilePath(ListViewForAdvanced.this, data.getData());
                saveTextFile(path);
            } catch (Exception e) {

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void saveTextFile(String path) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(path+"txt");
            for (int i = 0; i < listView.getCount(); i++) {
                fileOutputStream.write(i);
                fileOutputStream.write(":-".getBytes());
                fileOutputStream.write(dataRetrived[i].getBytes());
                fileOutputStream.write("\n".getBytes());
            }
            fileOutputStream.close();
            Toast.makeText(this, "Save", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
        }

    }
}
