package claudiu.sics.smsfilter;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import sisc.claudiu.smsfilter.R;

public class SendMessageActivity extends AppCompatActivity {

    private EditText phoneNumberText;
    private EditText messageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sms);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        phoneNumberText = (EditText) findViewById(R.id.phoneNumberText);
        messageText = (EditText) findViewById(R.id.messageText);

        final Intent intent = getIntent();
        if (intent != null) {
            final String phone = intent.getStringExtra(Resources.PHONE_NUMBER);
            if (phone != null) {
                phoneNumberText.setText(phone);
                messageText.requestFocus();
            }
        }

    }

    public void sendSMS(View view) {
        String address = phoneNumberText.getText().toString();
        if (null == address || address.trim().length() < 1) {
            phoneNumberText.setError(getResources().getString(R.string.typePhoneNumber));
            phoneNumberText.requestFocus();
            return;
        }

        String text = messageText.getText().toString();
        if (null == text || text.trim().length() < 1) {
            messageText.setError(getResources().getString(R.string.typeMessage));
            messageText.requestFocus();
            return;
        }

        final SmsManager smsManager = SmsManager.getDefault();
        if (smsManager != null) {
            // TODO enhance to have message sent confirmation...
            String scAddress = null;
            PendingIntent sentIntent = null;
            PendingIntent deliveryIntent = null;
            try {
                smsManager.sendTextMessage(address, scAddress, text, sentIntent, deliveryIntent);
                Toast.makeText(getApplicationContext(), "Message sent!", Toast.LENGTH_SHORT).show();
                finish();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Message not send! " + e.getClass().getName() + " (" + e.getMessage() + ")", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "SMS not available on device!", Toast.LENGTH_LONG).show();
        }
    }
}
