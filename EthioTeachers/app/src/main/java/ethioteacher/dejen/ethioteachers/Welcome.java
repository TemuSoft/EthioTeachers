package ethioteacher.dejen.ethioteachers;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.io.File;

public class Welcome extends AppCompatActivity implements LocationListener {

    private LocationManager locationManager;
    private int counter = 0;
    private SharedPreferences preferences;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (preferences.getBoolean("admin", false) == true) {
            intent = new Intent(this, HelpAndAbout.class);
            finish();
            startActivity(intent);
        } else {
            intent = new Intent(Welcome.this, LoginPage.class);
            startActivity(intent);
            finish();
        }
    /*/
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) == false)
            mainGPSEnabler();
        else {
            Toast.makeText(this, "Please disable GPS", Toast.LENGTH_SHORT).show();
           // android.os.Process.killProcess(android.os.Process.myPid());
        }
        */
    }

    private void mainGPSEnabler() {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mainGPSEnabler();
                        }
                    }, 10000);
                }
                return;
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 0, this);
                if (locationManager != null) {
                    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null)
                        onLocationChanged(location);
                    else {
                        Toast.makeText(Welcome.this, "Please waint ....", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } else {
            Toast.makeText(Welcome.this, "Enable GPS please!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, 21);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    counter++;
                    mainGPSEnabler();
                }
            }, 20000);
        }
        if (counter == 3)
            android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public void onBackPressed() {
        android.os.Process.killProcess(android.os.Process.myPid());
        super.onBackPressed();
    }

    @Override
    public void onLocationChanged(Location location) {

        if (location.getLatitude() >= 10.150 && location.getLatitude() <= 10.180 && location.getLongitude() >= 38.120
                && location.getLongitude() <= 38.160) {
            File file = new File(this.getFilesDir(), "/Teme/");
            if (!file.exists())
                file.mkdirs();
            new Utility().setTilme(file, String.valueOf(location.getTime()));
            Intent intent = new Intent(Welcome.this, LoginPage.class);
            startActivity(intent);
            finish();
            locationManager.removeUpdates(this);
        } else {
            locationManager.removeUpdates(this);
            Toast.makeText(this, "you are out of Dejen", Toast.LENGTH_SHORT).show();
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 21) {
            mainGPSEnabler();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}