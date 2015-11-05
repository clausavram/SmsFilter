package claudiu.sics.smsfilter.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SMSDBHelper extends SQLiteOpenHelper {
    private static final String SQL_CREATE_MESSAGES_ENTRIES =
            String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s, %s, %s, %s)",
                    SMSDBSchema.MessagesTable.TABLE_NAME,
                    SMSDBSchema.MessagesTable._ID,
                    SMSDBSchema.MessagesTable.COLUMN_NAME_PHONE_NUMBER,
                    SMSDBSchema.MessagesTable.COLUMN_NAME_MESSAGE,
                    SMSDBSchema.MessagesTable.COLUMN_NAME_DATETIME,
                    SMSDBSchema.MessagesTable.COLUMN_NAME_READ);
    private static final String SQL_CREATE_FILTERS_ENTRIES =
            String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s, %s, %s, %s, %s, %s)",
                    SMSDBSchema.FilstersTable.TABLE_NAME,
                    SMSDBSchema.FilstersTable._ID,
                    SMSDBSchema.FilstersTable.COLUMN_NAME_PHONE_NUMBER_PATTERN,
                    SMSDBSchema.FilstersTable.COLUMN_NAME_START_TIME,
                    SMSDBSchema.FilstersTable.COLUMN_NAME_END_TIME,
                    SMSDBSchema.FilstersTable.COLUMN_NAME_MESSAGE_PATTERN,
                    SMSDBSchema.FilstersTable.COLUMN_NAME_IS_DATE_FILTER,
                    SMSDBSchema.FilstersTable.COLUMN_NAME_LABEL);

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "SMSFilter.db";

    public SMSDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_MESSAGES_ENTRIES);
        Log.d(getClass().getSimpleName(), "Executed create query: " + SQL_CREATE_MESSAGES_ENTRIES);
        db.execSQL(SQL_CREATE_FILTERS_ENTRIES);
        Log.d(getClass().getSimpleName(), "Executed create query: " + SQL_CREATE_FILTERS_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}