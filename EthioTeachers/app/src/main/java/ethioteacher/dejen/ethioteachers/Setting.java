package ethioteacher.dejen.ethioteachers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

public class Setting extends PreferenceActivity {

    private SharedPreferences preferences;
    private PreferenceScreen preferenceScreen;
    private PreferenceCategory preferenceCategory, preferenceCategory2, preferenceCategory3, preferenceCategory4, admin;
    private CheckBoxPreference checkBoxPreference, saveType, ByDelete, schoolOrManual;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.activity_setting);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        handler = new Handler();

        preferenceScreen = getPreferenceScreen();
        preferenceCategory = (PreferenceCategory) findPreference("prefer");
        preferenceCategory2 = (PreferenceCategory) findPreference("prefer2");
        preferenceCategory3 = (PreferenceCategory) findPreference("prefer3");
        preferenceCategory4 = (PreferenceCategory) findPreference("prefer4");
        admin = (PreferenceCategory) findPreference("preferAdmin");
        checkBoxPreference = (CheckBoxPreference) findPreference("admin");
        saveType = (CheckBoxPreference) findPreference("saveType");
        ByDelete = (CheckBoxPreference) findPreference("ByDelete");
        schoolOrManual = (CheckBoxPreference) findPreference("schoolOrManual");

        recreateDesign();
    }

    @Override
    public void onBackPressed() {
        if (preferences.getBoolean("admin", false) == false) {
            Intent intent = new Intent(Setting.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            Intent intent = new Intent(Setting.this, HelpAndAbout.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        super.onBackPressed();
    }

    private void checkPreferenceAgain() {
        if (checkBoxPreference.isChecked()) {
            preferenceScreen.removePreference(preferenceCategory);
            preferenceScreen.removePreference(preferenceCategory2);
            preferenceScreen.removePreference(preferenceCategory3);
            preferenceScreen.removePreference(preferenceCategory4);
            checkBoxPreference.setTitle("Unckecked to personal user");
        } else {
            preferenceScreen.addPreference(preferenceCategory);
            preferenceScreen.addPreference(preferenceCategory2);
            preferenceScreen.addPreference(preferenceCategory3);
            preferenceScreen.addPreference(preferenceCategory4);
            checkBoxPreference.setTitle("Checked to as administrator");
        }
        if (saveType.isChecked())
            saveType.setSummary("Unchecked to save as HTML file");
        else
            saveType.setSummary("Checked to save as excel file");
        if (ByDelete.isChecked()) {
            ByDelete.setTitle("Without delete old data");
            ByDelete.setSummary("Unchecked to remove current database");
        } else {
            ByDelete.setSummary("Checked to remove current database");
            ByDelete.setTitle("By delete old data");
        }
        if (schoolOrManual.isChecked()) {
            schoolOrManual.setSummary("Uncheck to add from school data");
        } else {
            schoolOrManual.setSummary("Check to add manualy");
        }
        recreateDesign();
    }

    private void recreateDesign() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkPreferenceAgain();
            }
        }, 1000);
    }
}
