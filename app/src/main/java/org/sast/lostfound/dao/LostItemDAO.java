package org.sast.lostfound.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.sast.lostfound.model.LostItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LostItemDAO {
    private final LostFoundDatabaseHelper mHelper;

    public LostItemDAO(Context context) {
        mHelper = new LostFoundDatabaseHelper(context);
    }

    public void addLostItem(LostItem item) {
        try(SQLiteDatabase mDatabase = mHelper.getWritableDatabase()) {
            ContentValues values = getContentValues(item);
            mDatabase.insert(LostItemTable.NAME, null, values);
        }
    }

    public List<LostItem> getLostItems() {
        List<LostItem> lostItems = new ArrayList<>();
        try(SQLiteDatabase mDatabase = mHelper.getWritableDatabase()) {
            try (Cursor cursor = mDatabase.query(LostItemTable.NAME,
                    null, null, null,
                    null, null, null)) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    LostItem lostItem = new LostItem(
                            cursor.getInt(0),
                            cursor.getString(1),
                            new Date(cursor.getLong(2)),
                            cursor.getString(3),
                            cursor.getString(4),
                            cursor.getString(5),
                            cursor.getString(6),
                            cursor.getString(7)
                    );
                    lostItems.add(lostItem);
                    cursor.moveToNext();
                }
            }
        }
        return lostItems;
    }

    public List<LostItem> getLostItemByFilter(String selection,
                                              List<String> selectionArgs) {
        List<LostItem> lostItems = new ArrayList<>();
        try(SQLiteDatabase mDatabase = mHelper.getWritableDatabase()) {
            try (Cursor cursor = mDatabase.query(LostItemTable.NAME,
                    null, selection, selectionArgs.toArray(new String[0]),
                    null, null, "time DESC")) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    LostItem lostItem = new LostItem(
                            cursor.getInt(0),
                            cursor.getString(1),
                            new Date(cursor.getLong(2)),
                            cursor.getString(3),
                            cursor.getString(4),
                            cursor.getString(5),
                            cursor.getString(6),
                            cursor.getString(7)
                    );
                    lostItems.add(lostItem);
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return lostItems;
    }

    public void updateLostItem(LostItem item) {
        ContentValues values = getContentValues(item);
        try(SQLiteDatabase mDatabase = mHelper.getWritableDatabase()) {
            mDatabase.update(
                    LostItemTable.NAME,
                    values,
                    "_id = ?",
                    new String[]{String.valueOf(item.getId())}
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static ContentValues getContentValues(LostItem item) {
        ContentValues values = new ContentValues();
        values.put(LostItemTable.Cols.TITLE, item.getTitle());
        values.put(LostItemTable.Cols.TIME, item.getTime().getTime());
        values.put(LostItemTable.Cols.PHOTO_PATH, item.getPhotoPath());
        values.put(LostItemTable.Cols.LOCATION, item.getLocation());
        values.put(LostItemTable.Cols.DESCRIPTION, item.getDescription());
        values.put(LostItemTable.Cols.CATEGORY, item.getCategory());
        values.put(LostItemTable.Cols.STATUS, item.getStatus());
        return values;
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
    }
}