package claudiu.sics.smsfilter;

import android.app.Application;

public class SMSBuddyApp extends Application {

    private SMSManagerDefault manager;

    public SMSManagerDefault getManager() {
        if (manager == null) {
            manager = new SMSManagerDefault(getApplicationContext());
        }
        return manager;
    }
}
