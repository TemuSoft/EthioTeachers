package ethioteacher.dejen.ethioteachers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hp on 8/21/2017.
 */

public class AccountHolder extends SQLiteOpenHelper {

    public AccountHolder(Context context) {
        super(context, "AccountHolder", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table AccountLogin(userName varchar(25),password varchar(12))");
        db.execSQL("create table AccountLoginBackup(userName varchar(25),password varchar(12))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
