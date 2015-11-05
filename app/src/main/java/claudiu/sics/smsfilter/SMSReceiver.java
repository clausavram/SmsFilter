package claudiu.sics.smsfilter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.Date;
import java.util.List;

public class SMSReceiver extends BroadcastReceiver {

    private final SmsManager smsManager;

    public SMSReceiver() {
        smsManager = SmsManager.getDefault();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        if (bundle != null) {
            try {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                // handle just first msg - multipart sms messages maybe in next version... :)
                if (pdusObj != null && pdusObj.length > 0 && pdusObj[0] != null) {
                    final SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[0]);

                    final String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    final String message = currentMessage.getDisplayMessageBody();
                    final Date timestamp = new Date(currentMessage.getTimestampMillis());

                    Log.i(getClass().getSimpleName(), "senderNum: " + phoneNumber + "; message: " + message);
                    final boolean abortBroadcats = handleActionReceivedSMS(context, phoneNumber, message, timestamp);
                    if (abortBroadcats) {
                        final Message smsBuddyMessage = new Message();
                        smsBuddyMessage.setPhone(phoneNumber);
                        smsBuddyMessage.setMessage(message);
                        smsBuddyMessage.setTimestamp(timestamp);
                        FilterService.startActionHandleReceivedSMS(context, smsBuddyMessage);
                        abortBroadcast();
                    }
                }
            } catch (final Exception e) {
                Log.e(getClass().getSimpleName(), "Error processing incoming SMS!", e);
            }
        }
    }

    private boolean handleActionReceivedSMS(final Context context, final String phone, final String message, final Date timestamp) {
        boolean shouldAbortBroadcast = false;

        final Message smsBuddyMessage = new Message();
        smsBuddyMessage.setPhone(phone);
        smsBuddyMessage.setMessage(message);
        smsBuddyMessage.setTimestamp(timestamp);
        smsBuddyMessage.setHandled(false);

        final SMSManagerDefault manager = ((Application) context.getApplicationContext()).getManager();
        final List<Filter> filters = manager.getFilters();
        for (final Filter filter : filters) {

            if (filter.matches(smsBuddyMessage)) {
                Log.i(getClass().getSimpleName(), "Found filter matching SMS " + smsBuddyMessage + ": " + filter);
                shouldAbortBroadcast = true;
                break;
            }
        }
        return shouldAbortBroadcast;
    }
}
