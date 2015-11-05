package claudiu.sics.smsfilter;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import sisc.claudiu.smsfilter.R;

public class SMSBuddyFilterTimeActivity extends AppCompatActivity {

    private SMSBuddyFilter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_time_filter);
        final Intent intent = getIntent();
        if (intent != null) {
            filter = (SMSBuddyFilter) intent.getSerializableExtra(SMSBuddyResources.EDIT_FILTER);
        }
        if (filter == null) {
            filter = new SMSBuddyFilter();
            filter.setDateFilter(false);
        } else {
            ((EditText) findViewById(R.id.editLabelTimeText)).setText(filter.getLabel());
            ((EditText) findViewById(R.id.editPhoneNumberTimeText)).setText(filter.getPhonePattern());
            ((EditText) findViewById(R.id.editMessagePatternTimeText)).setText(filter.getMessagePattern());
            ((EditText) findViewById(R.id.fromTimeText)).setText(filter.getStartTime());
            ((EditText) findViewById(R.id.toTimeText)).setText(filter.getEndTime());
        }
        Log.d(getClass().getSimpleName(), "Editing TIME filter: " + filter);
    }

    public SMSBuddyFilter getFilter() {
        return filter;
    }

    public void pickFrom(View view) {
        DialogFragment newFragment = new SMSBuddyFilterTimeActivity.TimePickerFragment() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                super.onDismiss(dialog);
                ((EditText) findViewById(R.id.fromTimeText)).setText(filter.getStartTime());
            }
        };
        newFragment.show(getFragmentManager(), SMSBuddyResources.TIME_PICKER_FROM);
    }

    public void pickTo(View view) {
        DialogFragment newFragment = new SMSBuddyFilterTimeActivity.TimePickerFragment() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                super.onDismiss(dialog);
                ((EditText) findViewById(R.id.toTimeText)).setText(filter.getEndTime());
            }
        };
        newFragment.setCancelable(true);
        newFragment.show(getFragmentManager(), SMSBuddyResources.TIME_PICKER_TO);
    }

    public void saveTimeFilter(View view) {
        EditText labelText = (EditText) findViewById(R.id.editLabelTimeText);
        EditText phoneText = (EditText) findViewById(R.id.editPhoneNumberTimeText);
        EditText patternText = (EditText) findViewById(R.id.editMessagePatternTimeText);
        String label = labelText.getText().toString();
        String phone = phoneText.getText().toString();
        String pattern = patternText.getText().toString();
        filter.setLabel(label);
        filter.setMessagePattern(pattern);
        filter.setPhonePattern(phone);
        final Intent result = new Intent();
        result.putExtra(SMSBuddyResources.EDIT_FILTER, filter);
        setResult(Activity.RESULT_OK, result);
        finish();
    }

    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final SMSBuddyFilter filter = ((SMSBuddyFilterTimeActivity) getActivity()).getFilter();
            final String tag = getTag();
            String editValue = null;
            if (SMSBuddyResources.TIME_PICKER_FROM.equals(tag)) {
                editValue = filter.getStartTime();
            } else if (SMSBuddyResources.TIME_PICKER_TO.equals(tag)) {
                editValue = filter.getEndTime();
            }
            final Calendar calendar = Calendar.getInstance();
            if (editValue != null) {
                try {
                    final Date parse = new SimpleDateFormat(SMSBuddyResources.FORMAT_HH_MM).parse(editValue);
                    calendar.setTime(parse);
                } catch (ParseException e) {
                    Log.e(SMSBuddyFilterDateActivity.class.getSimpleName(), "Error parsing time", e);
                }
            }
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int min = calendar.get(Calendar.MINUTE);
            return new TimePickerDialog(getActivity(), this, hour, min, true);
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            final SMSBuddyFilter filter = ((SMSBuddyFilterTimeActivity) getActivity()).getFilter();
            final String tag = getTag();
            if (SMSBuddyResources.TIME_PICKER_FROM.equals(tag)) {
                final Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                filter.setStartTime(DateFormat.format(SMSBuddyResources.FORMAT_HH_MM, calendar.getTime()).toString());
            } else if (SMSBuddyResources.TIME_PICKER_TO.equals(tag)) {
                final Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                filter.setEndTime(DateFormat.format(SMSBuddyResources.FORMAT_HH_MM, calendar.getTime()).toString());
            }
        }
    }
}
