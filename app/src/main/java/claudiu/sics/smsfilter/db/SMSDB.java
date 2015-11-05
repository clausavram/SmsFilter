package claudiu.sics.smsfilter.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Locale;

import claudiu.sics.smsfilter.Filter;
import claudiu.sics.smsfilter.Message;
import claudiu.sics.smsfilter.Resources;

public class SMSDB {
    private SMSDBHelper hDBHelper;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat(Resources.FORMAT_YYYY_MM_DD_HH_MM_SS, Locale.getDefault());

    public SMSDB(Context context) {
        hDBHelper = new SMSDBHelper(context);
    }

    public long insertMessage(Message m) {
        SQLiteDatabase db = hDBHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SMSDBSchema.MessagesTable.COLUMN_NAME_PHONE_NUMBER, m.getPhone());
        values.put(SMSDBSchema.MessagesTable.COLUMN_NAME_MESSAGE, m.getMessage());
        values.put(SMSDBSchema.MessagesTable.COLUMN_NAME_DATETIME, dateFormat.format(m.getTimestamp()));
        values.put(SMSDBSchema.MessagesTable.COLUMN_NAME_READ, Boolean.toString(m.isRead()));

        return db.insert(SMSDBSchema.MessagesTable.TABLE_NAME, null, values);
    }

    public long markMessageRead(Message m) {
        Log.d(getClass().getSimpleName(), "Update on: " + m + "(most important: id = " + m.getId() + ")");
        SQLiteDatabase db = hDBHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SMSDBSchema.MessagesTable.COLUMN_NAME_READ, Boolean.toString(true));

        Log.d(getClass().getSimpleName(), "Repo before update: " + getMessages());
        int count = db.update(SMSDBSchema.MessagesTable.TABLE_NAME, values,
                String.format("%s = ?", SMSDBSchema.MessagesTable._ID),
                new String[]{Long.toString(m.getId())}
        );
        Log.d(getClass().getSimpleName(), "Repo after update: " + getMessages());
        return count;
    }

    public int removeMessage(Message m) {
        SQLiteDatabase db = hDBHelper.getWritableDatabase();

        return db.delete(SMSDBSchema.MessagesTable.TABLE_NAME,
                String.format("%s = ?", SMSDBSchema.MessagesTable._ID),
                new String[]{Long.toString(m.getId())}
        );
    }

    public Cursor getMessages() {
        SQLiteDatabase db = hDBHelper.getWritableDatabase();

        String[] projection = {
                SMSDBSchema.MessagesTable._ID,
                SMSDBSchema.MessagesTable.COLUMN_NAME_PHONE_NUMBER,
                SMSDBSchema.MessagesTable.COLUMN_NAME_MESSAGE,
                SMSDBSchema.MessagesTable.COLUMN_NAME_DATETIME,
                SMSDBSchema.MessagesTable.COLUMN_NAME_READ
        };

        String sortOrder = SMSDBSchema.MessagesTable.COLUMN_NAME_DATETIME + " DESC";
        return db.query(SMSDBSchema.MessagesTable.TABLE_NAME, projection, null, null, null, null, sortOrder);
    }

    public long insertFilter(Filter f) {
        SQLiteDatabase db = hDBHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SMSDBSchema.FilstersTable.COLUMN_NAME_LABEL, f.getLabel());
        values.put(SMSDBSchema.FilstersTable.COLUMN_NAME_PHONE_NUMBER_PATTERN, f.getPhonePattern());
        values.put(SMSDBSchema.FilstersTable.COLUMN_NAME_MESSAGE_PATTERN, f.getMessagePattern());
        values.put(SMSDBSchema.FilstersTable.COLUMN_NAME_START_TIME, f.getStartTime());
        values.put(SMSDBSchema.FilstersTable.COLUMN_NAME_END_TIME, f.getEndTime());
        values.put(SMSDBSchema.FilstersTable.COLUMN_NAME_IS_DATE_FILTER, Boolean.toString(f.isDateFilter()));

        return db.insert(SMSDBSchema.FilstersTable.TABLE_NAME, null, values);
    }

    public long removeFilter(Filter f) {
        SQLiteDatabase db = hDBHelper.getWritableDatabase();

        return db.delete(SMSDBSchema.FilstersTable.TABLE_NAME,
                String.format("%s = ?", SMSDBSchema.FilstersTable._ID),
                new String[]{Long.toString(f.getId())}
        );
    }

    public int updateFilter(Filter f) {
        SQLiteDatabase db = hDBHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SMSDBSchema.FilstersTable.COLUMN_NAME_LABEL, f.getLabel());
        values.put(SMSDBSchema.FilstersTable.COLUMN_NAME_START_TIME, f.getStartTime());
        values.put(SMSDBSchema.FilstersTable.COLUMN_NAME_END_TIME, f.getEndTime());
        values.put(SMSDBSchema.FilstersTable.COLUMN_NAME_PHONE_NUMBER_PATTERN, f.getPhonePattern());
        values.put(SMSDBSchema.FilstersTable.COLUMN_NAME_MESSAGE_PATTERN, f.getMessagePattern());
        values.put(SMSDBSchema.FilstersTable.COLUMN_NAME_IS_DATE_FILTER, f.isDateFilter());

        return db.update(SMSDBSchema.FilstersTable.TABLE_NAME, values,
                String.format("%s = ?", SMSDBSchema.FilstersTable._ID),
                new String[]{Long.toString(f.getId())}
        );
    }

    public Cursor getFilters() {
        SQLiteDatabase db = hDBHelper.getWritableDatabase();

        String[] projection = {
                SMSDBSchema.FilstersTable._ID,
                SMSDBSchema.FilstersTable.COLUMN_NAME_LABEL,
                SMSDBSchema.FilstersTable.COLUMN_NAME_START_TIME,
                SMSDBSchema.FilstersTable.COLUMN_NAME_END_TIME,
                SMSDBSchema.FilstersTable.COLUMN_NAME_PHONE_NUMBER_PATTERN,
                SMSDBSchema.FilstersTable.COLUMN_NAME_MESSAGE_PATTERN,
                SMSDBSchema.FilstersTable.COLUMN_NAME_IS_DATE_FILTER
        };

        String sortOrder = SMSDBSchema.FilstersTable._ID + " DESC";
        return db.query(SMSDBSchema.FilstersTable.TABLE_NAME, projection, null, null, null, null, sortOrder);
    }
}
