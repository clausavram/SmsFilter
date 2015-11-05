package claudiu.sics.smsfilter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import claudiu.sics.smsfilter.db.SMSDB;
import claudiu.sics.smsfilter.db.SMSDBSchema;

public class SMSManagerDefault implements SMSManager {

    private List<Filter> filters = new ArrayList<>();
    private final SMSDB db;
    private DateFormat dateFormat = new SimpleDateFormat(Resources.FORMAT_YYYY_MM_DD_HH_MM_SS);

    public SMSManagerDefault(Context context) {
        filters.add(createFilter("1234", "(mouse|cat|dog|rabbit|bird|fish)", "00:00", "23:59", false));
        filters.add(createFilter("1234", "Test", "2015-10-20", "2015-11-25", true));
        filters.add(createFilter("+40728076592", "Test", "2015-10-20", "2015-11-25", true));
        filters.add(createFilter("15555215556", ".*", "2015-10-20", "2015-11-25", true));
        db = new SMSDB(context);
    }

    @Override
    public List<Message> getMessages() {
        List<Message> tmp = new ArrayList<>();
        Cursor c = db.getMessages();
        int ciId = c.getColumnIndex(SMSDBSchema.MessagesTable._ID);
        int ciPhone = c.getColumnIndex(SMSDBSchema.MessagesTable.COLUMN_NAME_PHONE_NUMBER);
        int ciMessage = c.getColumnIndex(SMSDBSchema.MessagesTable.COLUMN_NAME_MESSAGE);
        int ciDateTime = c.getColumnIndex(SMSDBSchema.MessagesTable.COLUMN_NAME_DATETIME);
        int ciRead = c.getColumnIndex(SMSDBSchema.MessagesTable.COLUMN_NAME_READ);
        while (c.moveToNext()) {
            try {
                Message m = new Message();
                m.setId(c.getInt(ciId));
                m.setPhone(c.getString(ciPhone));
                m.setMessage(c.getString(ciMessage));
                m.setTimestamp(dateFormat.parse(c.getString(ciDateTime)));
                m.setHandled(Boolean.parseBoolean(c.getString(ciRead)));
                tmp.add(m);
            } catch (ParseException e) {
                Log.e(getClass().getSimpleName(), e.getMessage());
            }
        }

        c.close();
        return tmp;
    }

    @Override
    public void saveSMS(Message message) {
        db.insertMessage(message);
        Log.d(getClass().getSimpleName(), "Saved message into DB: " + message);
    }

    @Override
    public void deleteSMS(Message message) {
        db.removeMessage(message);
        Log.d(getClass().getSimpleName(), "Deleted message from DB: " + message);
    }

    @Override
    public void markSMSRead(Message message) {
        long rowCount = db.markMessageRead(message);
        Log.d(getClass().getSimpleName(), "Marked message as 'read' into DB: " + message + " (affected rows: " + rowCount + ")");
    }

    @Override
    public int getMessageCount() {
        return getMessages().size();
    }

    @Override
    public List<Filter> getFilters() {
        List<Filter> tmp = new ArrayList<>();
        Cursor c = db.getFilters();
        int ciId = c.getColumnIndex(SMSDBSchema.FilstersTable._ID);
        int ciLabel = c.getColumnIndex(SMSDBSchema.FilstersTable.COLUMN_NAME_LABEL);
        int ciStart = c.getColumnIndex(SMSDBSchema.FilstersTable.COLUMN_NAME_START_TIME);
        int ciEnd = c.getColumnIndex(SMSDBSchema.FilstersTable.COLUMN_NAME_END_TIME);
        int ciPhone = c.getColumnIndex(SMSDBSchema.FilstersTable.COLUMN_NAME_PHONE_NUMBER_PATTERN);
        int ciMessage = c.getColumnIndex(SMSDBSchema.FilstersTable.COLUMN_NAME_MESSAGE_PATTERN);
        int ciIsDate = c.getColumnIndex(SMSDBSchema.FilstersTable.COLUMN_NAME_IS_DATE_FILTER);
        while(c.moveToNext()) {
            try {
                Filter f = new Filter();
                f.setId(c.getInt(ciId));
                f.setLabel(c.getString(ciLabel));
                f.setStartTime(c.getString(ciStart));
                f.setEndTime(c.getString(ciEnd));
                f.setPhonePattern(c.getString(ciPhone));
                f.setMessagePattern(c.getString(ciMessage));
                f.setDateFilter(Boolean.parseBoolean(c.getString(ciIsDate)));
                tmp.add(f);
            } catch (Exception e) {
                Log.e(getClass().getSimpleName(), e.getMessage());
            }
        }
        c.close();
        return tmp;
    }

    @Override
    public void saveFilter(Filter filter) {
        if (filter.getId() == -1) {
            insertFilter(filter);
        } else {
            updateFilter(filter);
        }
    }

    @Override
    public void deleteFilter(Filter filter) {
        db.removeFilter(filter);
    }

    @Override
    public int countFilters() {
        return getFilters().size();
    }

    private void insertFilter(Filter filter) {
        db.insertFilter(filter);
    }

    private void updateFilter(Filter filter) {
        db.updateFilter(filter);
    }

    private Filter createFilter(String phone, String pattern, String from, String to, boolean dateFilter) {
        Filter f = new Filter();
        f.setId(System.nanoTime());
        f.setPhonePattern(phone);
        f.setStartTime(from);
        f.setEndTime(to);
        f.setMessagePattern(pattern);
        f.setDateFilter(dateFilter);
        return f;
    }
}


