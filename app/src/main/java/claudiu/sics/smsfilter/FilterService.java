package claudiu.sics.smsfilter;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class FilterService extends IntentService {

    private static final String ACTION_SMS_RECEIVED = "sisc.claudiu.action.received";
    private static final String ACTION_SMS_HANDLED = "sisc.claudiu.action.handled";
    private static final String ACTION_SMS_DELETE = "sisc.claudiu.action.delete";

    private static final String ACTION_PARAM_MESSAGE = "sisc.claudiu.action.param.message";

    public FilterService() {
        super("FilterService");
    }

    public static void startActionHandleReceivedSMS(Context context, Message message) {
        Intent intent = new Intent(context, FilterService.class);
        intent.setAction(ACTION_SMS_RECEIVED);
        intent.putExtra(ACTION_PARAM_MESSAGE, message);
        context.startService(intent);
    }

    public static void startActionHandleSMSHandled(Context context, Message message) {
        Intent intent = new Intent(context, FilterService.class);
        intent.setAction(ACTION_SMS_HANDLED);
        intent.putExtra(ACTION_PARAM_MESSAGE, message);
        context.startService(intent);
    }

    public static void startActionHandleSMSDelete(Context context, Message message) {
        Intent intent = new Intent(context, FilterService.class);
        intent.setAction(ACTION_SMS_DELETE);
        intent.putExtra(ACTION_PARAM_MESSAGE, message);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SMS_RECEIVED.equals(action)) {
                final Message message = (Message) intent.getSerializableExtra(ACTION_PARAM_MESSAGE);
                Log.i(getClass().getSimpleName(), "Handling received: " + message);
                final SMSManagerDefault manager = ((Application) getApplication()).getManager();
                for (Filter filter : manager.getFilters()) {
                    if (filter.matches(message)) {
                        Log.d(getClass().getSimpleName(), "Found filter matching " + message + " : " + filter);
                        manager.saveSMS(message);
                        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(Resources.ACTION_SMS_CHANGED));
                        break;
                    }
                }
            } else if (ACTION_SMS_DELETE.equals(action)) {
                final Message message = (Message) intent.getSerializableExtra(ACTION_PARAM_MESSAGE);
                Log.i(getClass().getSimpleName(), "handle delete SMS: " + message);
                final SMSManagerDefault manager = ((Application) getApplication()).getManager();
                manager.deleteSMS(message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(Resources.ACTION_SMS_CHANGED));
            } else if (ACTION_SMS_HANDLED.equals(action)) {
                final Message message = (Message) intent.getSerializableExtra(ACTION_PARAM_MESSAGE);
                Log.i(getClass().getSimpleName(), "handle SMS: " + message);
                final SMSManagerDefault manager = ((Application) getApplication()).getManager();
                manager.markSMSRead(message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(Resources.ACTION_SMS_CHANGED));
            }
        }
    }

}
