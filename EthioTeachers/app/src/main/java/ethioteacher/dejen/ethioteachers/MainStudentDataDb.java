package ethioteacher.dejen.ethioteachers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hp on 8/15/2017.
 */

public class MainStudentDataDb extends SQLiteOpenHelper {

    String adminOrNot = "";

    public MainStudentDataDb(Context context, String path) {
        super(context, path, null, 1);
        adminOrNot = path;
    }

    //"STUDENT_DATA.db"
    @Override
    public void onCreate(SQLiteDatabase db) {

        if (adminOrNot.equals("ADMINISTRATOR.db")) {
            db.execSQL("create table StudentData(    \n" +
                    "   Sname varchar(20)            \n" +
                    "  ,Fname varchar(20)            \n" +
                    "  ,GFname varchar(20)           \n" +
                    "  ,Semester varchar(6)          \n" +
                    "  ,Subject varchar(15)          \n" +
                    "  ,Phone_number varchar(10)     \n" +
                    "  ,Grade varchar(3)             \n" +
                    "  ,Number int(2)                \n" +
                    "  ,Birth_Date varchar(10)       \n" +
                    "  ,Sex varchar(1)               \n" +
                    "  ,Date_Register varchar(20)    \n" +
                    "  ,Date_last_update varchar(20) \n" +
                    "  ,School varchar(20)           \n" +
                    "  ,primary key(Number,Grade,Subject))");
     	db.execSQL("create table StudentValueFirst(" +
                "    Class_Work_10 double(6)                              \n" +
                "   ,Home_Work_5 double(6)                                \n" +
                "   ,Proj_and_Assign_10 double(6)                         \n" +
                "   ,Group_Work_5 double(6)                               \n" +
                "   ,Mid_Exam_10 double(6)                                \n" +
                "   ,Final_60 double(6)                                   \n" +
                "   ,Total40 double(6)                                    \n" +
                "   ,Total100 double(6)                                   \n" +
                "   ,Last_update_date varchar(20)                          \n" +
                "   ,NumberV int(2) references StudentData(Number)         \n" +
                "   ,GradeV varchar(3) references StudentData(Grade)       \n" +
				"   ,SubjectV varchar(13) references StudentData(Subject)  \n" +  
                "   ,primary key(NumberV,GradeV,SubjectV))");

        db.execSQL("create table StudentValueSecond(" +
                "    Class_Work_10 double(6)                              \n" +
                "   ,Home_Work_5 double(6)                                \n" +
                "   ,Proj_and_Assign_10 double(6)                         \n" +
                "   ,Group_Work_5 double(6)                               \n" +
                "   ,Mid_Exam_10 double(6)                                \n" +
                "   ,Final_60 double(6)                                   \n" +
                "   ,Total40 double(6)                                    \n" +
                "   ,Total100 double(6)                                   \n" +
                "   ,Last_update_date varchar(20)                          \n" +
                "   ,NumberV int(2) references StudentData(Number)         \n" +
                "   ,GradeV varchar(3) references StudentData(Grade)       \n" +
				"   ,SubjectV varchar(13) references StudentData(Subject)  \n" +
                "   ,primary key(NumberV,GradeV,SubjectV))");

        } else {
            db.execSQL("create table StudentData(    \n" +
                    "   Sname varchar(20)            \n" +
                    "  ,Fname varchar(20)            \n" +
                    "  ,GFname varchar(20)           \n" +
                    "  ,Semester varchar(6)          \n" +
                    "  ,Subject varchar(15)          \n" +
                    "  ,Phone_number varchar(10)     \n" +
                    "  ,Grade varchar(3)             \n" +
                    "  ,Number int(2)                \n" +
                    "  ,Birth_Date varchar(10)       \n" +
                    "  ,Sex varchar(1)               \n" +
                    "  ,Date_Register varchar(20)    \n" +
                    "  ,Date_last_update varchar(20) \n" +
                    "  ,School varchar(20)           \n" +
                    "  ,primary key(Number,Grade))");
        
        db.execSQL("create table StudentValueFirst(" +
                "    Class_Work_10 double(6)                              \n" +
                "   ,Home_Work_5 double(6)                                \n" +
                "   ,Proj_and_Assign_10 double(6)                         \n" +
                "   ,Group_Work_5 double(6)                               \n" +
                "   ,Mid_Exam_10 double(6)                                \n" +
                "   ,Final_60 double(6)                                   \n" +
                "   ,Total40 double(6)                                    \n" +
                "   ,Total100 double(6)                                   \n" +
                "   ,Last_update_date varchar(20)                          \n" +
                "   ,NumberV int(2) references StudentData(Number)         \n" +
                "   ,GradeV varchar(3) references StudentData(Grade)       \n" +
                "   ,primary key(NumberV,GradeV))");

        db.execSQL("create table StudentValueSecond(" +
                "    Class_Work_10 double(6)                              \n" +
                "   ,Home_Work_5 double(6)                                \n" +
                "   ,Proj_and_Assign_10 double(6)                         \n" +
                "   ,Group_Work_5 double(6)                               \n" +
                "   ,Mid_Exam_10 double(6)                                \n" +
                "   ,Final_60 double(6)                                   \n" +
                "   ,Total40 double(6)                                    \n" +
                "   ,Total100 double(6)                                   \n" +
                "   ,Last_update_date varchar(20)                          \n" +
                "   ,NumberV int(2) references StudentData(Number)         \n" +
                "   ,GradeV varchar(3) references StudentData(Grade)       \n" +
                "   ,primary key(NumberV,GradeV))");
		}
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
