package claudiu.sics.smsfilter;

public interface Resources {

    String PHONE_NUMBER = "phone";
    String EDIT_FILTER = "updateFilter";

    int RESULT_NEW_DATE_FILTER = 100;
    int RESULT_NEW_TIME_FILTER = 200;
    int RESULT_EDIT_DATE_FILTER = 300;
    int RESULT_EDIT_TIME_FILTER = 400;

    String ACTION_SMS_CHANGED = "sisc.claudiu.smsChanged";

    String FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    String FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    String FORMAT_HH_MM = "HH:mm";

    String DATE_PICKER_FROM = "datePickerFrom";
    String DATE_PICKER_TO = "datePickerTo";

    String TIME_PICKER_FROM = "timePickerFrom";
    String TIME_PICKER_TO = "timePickerTo";

}
