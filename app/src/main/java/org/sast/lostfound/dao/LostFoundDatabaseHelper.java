package org.sast.lostfound.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class LostFoundDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "lost_found.db";
    private static final int DATABASE_VERSION = 1;

    public LostFoundDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建失物表
        db.execSQL(LostItemTable.CREATE);
    }

    // 开启日志输出
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.enableWriteAheadLogging();
        Log.d("LostFoundDatabaseHelper",
                "SQLite database is configured with " +
                        "write-ahead logging and other settings");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 升级数据库
        db.execSQL("DROP TABLE IF EXISTS " + LostItemTable.NAME);
        onCreate(db);
    }

    private static class LostItemTable {
        static final String NAME = "lostitems";

        static class Cols {
            static final String ID = "_id";
            static final String TITLE = "title";
            static final String TIME = "time";
            static final String LOCATION = "location";
            static final String DESCRIPTION = "description";
            static final String CATEGORY = "category";
            static final String STATUS = "status";
            static final String PHOTO_PATH = "photo_path";
            static final String[] ALL = {ID, TITLE, TIME, PHOTO_PATH, LOCATION,
                    DESCRIPTION, CATEGORY, STATUS};
        }

        static final String CREATE =
                "CREATE TABLE " + NAME + "(" +
                        Cols.ID + " integer NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        Cols.TITLE + " varchar NOT NULL, " +
                        Cols.TIME + " timestamp NOT NULL, " +
                        Cols.LOCATION + " varchar NOT NULL, " +
                        Cols.DESCRIPTION + " TEXT NOT NULL, " +
                        Cols.CATEGORY + " varchar NOT NULL, " +
                        Cols.STATUS + " varchar NOT NULL, " +
                        Cols.PHOTO_PATH + " TEXT)";
    }
}