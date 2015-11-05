package claudiu.sics.smsfilter.db;

import android.provider.BaseColumns;

public final class SMSDBSchema {
    public static abstract class MessagesTable implements BaseColumns {
        public static final String TABLE_NAME = "smsMessages";
        public static final String COLUMN_NAME_PHONE_NUMBER = "phoneNumber";
        public static final String COLUMN_NAME_MESSAGE = "message";
        public static final String COLUMN_NAME_DATETIME = "dateTime";
        public static final String COLUMN_NAME_READ = "read";
    }

    public static abstract class FilstersTable implements BaseColumns {
        public static final String TABLE_NAME = "smsFilters";
        public static final String COLUMN_NAME_START_TIME = "startTime";
        public static final String COLUMN_NAME_END_TIME = "endTime";
        public static final String COLUMN_NAME_PHONE_NUMBER_PATTERN = "phoneNumberPattern";
        public static final String COLUMN_NAME_MESSAGE_PATTERN = "contentPattern";
        public static final String COLUMN_NAME_IS_DATE_FILTER = "isDateFilter";
        public static final String COLUMN_NAME_LABEL = "label";
    }
}