package claudiu.sics.smsfilter;

public class Application extends android.app.Application {

    private SMSManagerDefault manager;

    public SMSManagerDefault getManager() {
        if (manager == null) {
            manager = new SMSManagerDefault(getApplicationContext());
        }
        return manager;
    }
}
