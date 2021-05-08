package ethioteacher.dejen.ethioteachers;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by hp on 8/25/2017.
 */

public class Utility  {

    private Handler handler = new Handler();
    private Handler handler2 = new Handler();
    private String currentTime = "0";

    private File folder;
    private FileWriter fileWriter;
    private FileInputStream inputStream;

    private SharedPreferences sharedPreferences;

    public String semester(Context context) {
        String semester = "First";
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean check = sharedPreferences.getBoolean("date", false);

        File fileTime = new File(context.getFilesDir(), "/Teme/");
        if (!fileTime.exists())
            fileTime.mkdirs();
        currentTime = "0";
        folder = new File(fileTime + "/teme.txt");
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(folder)));
            currentTime = reader.readLine();
            inputStream.close();
            reader.close();
        } catch (Exception y) {
        }
        Calendar c = Calendar.getInstance();

        if (check == false) {
            c.setTimeInMillis(Long.parseLong(currentTime));
        } else
            c.setTimeInMillis(System.currentTimeMillis());

        if (c.get(Calendar.MONTH) > 2 && c.get(Calendar.MONTH) <= 8)
            semester = "Second";
        else if (c.get(Calendar.MONTH) >= 9 && c.get(Calendar.MONTH) <= 2)
            semester = "First";

        return semester;
    }
	public int Year(Context context) {
        int year = 0;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean check = sharedPreferences.getBoolean("date", false);

        File fileTime = new File(context.getFilesDir(), "/Teme/");
        if (!fileTime.exists())
            fileTime.mkdirs();
        currentTime = "0";
        folder = new File(fileTime + "/teme.txt");
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(folder)));
            currentTime = reader.readLine();
            inputStream.close();
            reader.close();
        } catch (Exception y) {
        }
        Calendar c = Calendar.getInstance();

        if (check == false) {
            c.setTimeInMillis(Long.parseLong(currentTime));
        } else
            c.setTimeInMillis(System.currentTimeMillis());

        if (c.get(Calendar.MONTH) >=1 && c.get(Calendar.MONTH) <= 8)
            year =  c.get(Calendar.YEAR) - 8;
        else if (c.get(Calendar.MONTH) >= 9 && c.get(Calendar.MONTH) < 1)
            year = c.get(Calendar.YEAR) - 7;

        return year;
    }

    public void setTilme(File file, String s) {
        folder = new File(file + "/teme.txt");
        currentTime = String.valueOf(Long.parseLong(s) + Long.parseLong("2570320000"));
        try {
            fileWriter = new FileWriter(folder, false);
            fileWriter.write(currentTime);
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
        }
        timeCounter(file);
        exiter();
    }

    public String CurrentTime(File file, Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean check = sharedPreferences.getBoolean("date", false);
        currentTime = "0";
        folder = new File(file + "/teme.txt");
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(folder)));
            currentTime = reader.readLine();
            inputStream.close();
            reader.close();
        } catch (Exception y) {
        }
        long year = 0, month = 0, day = 0, hour = 0, minute = 0, second = 0;
        Calendar c = Calendar.getInstance();

        if (check == false) {
            c.setTimeInMillis(Long.parseLong(currentTime));
        } else
            c.setTimeInMillis(System.currentTimeMillis());

        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        second = c.get(Calendar.SECOND);
        return (year + "_" + month + "_" + day + " " + hour + ":" + minute + ":" + second);
    }

    public void timeCounter(final File file) {
        folder = new File(file + "/teme.txt");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                currentTime = String.valueOf(Long.parseLong(currentTime) + 35000);
                try {
                    fileWriter = new FileWriter(folder, false);
                    fileWriter.write(currentTime);
                    fileWriter.flush();
                    fileWriter.close();
                } catch (Exception e) {
                }
                timeCounter(file);
            }
        }, 35000);
    }

    private void exiter() {
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }, 6000000);
    }

    public void backUp(Context context) {
        try {
            FileOutputStream outputStream = null;
            FileInputStream inputStream = null;
            File backupFolder = getExternalFileDiroctory();
            if (!backupFolder.exists())
                backupFolder.mkdirs();
            File file = new File(backupFolder + "/STUDENT_DATA_SYSTEM_BACKUP.db");
            outputStream = new FileOutputStream(file);
            if (Build.VERSION.SDK_INT >= 17)
                inputStream = new FileInputStream(context.getApplicationContext().getDatabasePath("STUDENT_DATA.db"));
            else
                inputStream = new FileInputStream("STUDENT_DATA.db");
            FileChannel to = outputStream.getChannel();
            FileChannel from = inputStream.getChannel();
            from.transferTo(0, from.size(), to);
            if (from != null)
                from.close();
            if (to != null)
                to.close();
        } catch (Exception e) {
        }
    }

    public void securBackup(Context context) {
        try {
            FileOutputStream outputStream = null;
            FileInputStream inputStream = null;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_hh:mm:ss");
            Date date = new Date(System.currentTimeMillis());

            File backupFolder = getExternalFileDiroctoryBackup();
            if (!backupFolder.exists())
                backupFolder.mkdirs();
            File file = new File(backupFolder + "/" + dateFormat.format(date).toString() + ".db");
            outputStream = new FileOutputStream(file);
            if (Build.VERSION.SDK_INT >= 17)
                inputStream = new FileInputStream(context.getApplicationContext().getDatabasePath("STUDENT_DATA.db"));
            else
                inputStream = new FileInputStream("STUDENT_DATA.db");
            FileChannel to = outputStream.getChannel();
            FileChannel from = inputStream.getChannel();
            from.transferTo(0, from.size(), to);
            if (from != null)
                from.close();
            if (to != null)
                to.close();
        } catch (Exception e) {
        }
    }


    public  String getFilePath(Context context, Uri uri) throws URISyntaxException {
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

	public boolean checkPhoneNumber(String Phone){
	   boolean phon = false;
	   try{
		if(Phone.isEmpty())
			phon = true;
		else if(Phone.length()==10){
			if(Phone.charAt(0)=='0'&&Phone.charAt(1)=='9')
				phon = true;
		}
	   }catch(Exception e){
	   }
		return phon;
	}
	public boolean checkStudentName(String Sname){
		boolean sname = false;
		if(!Sname.isEmpty())
			sname = true;
		return sname;
	}
	public boolean checkFatherName(String Fname){
		boolean fname = false;
		if(!Fname.isEmpty())
			fname = true;
		return fname;
	}
	public boolean checkBirthDate(String BirthDate){
		boolean birthDate = false;
        if(BirthDate.length()>=8&&BirthDate.length()<=10)
		birthDate = true;
        return birthDate;		
	}
	public boolean checkClassNumber(String clasNumber){
		boolean number = false;
		try{
		if(Integer.parseInt(clasNumber)>0&&Integer.parseInt(clasNumber)<70)
			number = true;
		}catch(Exception e){
			number = false;
		}
		return number;
	}
	public String getStorageDirctoryName(){
		return "/TEACHERS MINI EXCEL/";
	}
	public String getDatabaseName(){
		return "/STUDENT_DATA.db";
	}
	
	public File getExternalFileDiroctory(){
		File file = new File(Environment.getExternalStorageDirectory(), getStorageDirctoryName());
		return file;
	}
	public File getExternalFileDiroctoryBackup(){
        File file = new File(Environment.getExternalStorageDirectory(), getStorageDirctoryName()+"/secure backup/");
        return file;
    }

}
