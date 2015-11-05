package claudiu.sics.smsfilter;


import android.graphics.Typeface;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import sisc.claudiu.smsfilter.R;

public class MessagesAdapter extends BaseAdapter {

    private SMSManagerDefault manager;

    public void setManager(SMSManagerDefault manager) {
        this.manager = manager;
    }

    public SMSManagerDefault getManager() {
        return manager;
    }

    @Override
    public int getCount() {
        return getManager().getMessageCount();
    }

    @Override
    public Object getItem(int position) {
        Log.d(getClass().getSimpleName(), "Getting item [" + position + "]: " + getManager().getMessages());
        return getManager().getMessages().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            convertView = layoutInflater.inflate(R.layout.sms_layout, parent, false);
        }
        final Message m = (Message) getItem(position);

        TextView phone = (TextView) convertView.findViewById(R.id.phoneNumber);
        phone.setText(m.getPhone());
        phone.setTypeface(null, m.isRead() ? Typeface.NORMAL : Typeface.BOLD);

        final CharSequence formatedTimestamp = DateFormat.format(Resources.FORMAT_YYYY_MM_DD_HH_MM_SS, m.getTimestamp());
        TextView timestamp = (TextView) convertView.findViewById(R.id.timestamp);
        timestamp.setText(formatedTimestamp);
        timestamp.setTypeface(null, m.isRead() ? Typeface.NORMAL : Typeface.BOLD);

        TextView message = (TextView) convertView.findViewById(R.id.message);
        String messageText = m.getMessage();
        int characterLimit = 70;
        if (messageText.length() > characterLimit) {
            messageText = messageText.substring(0, characterLimit - 2) + "...";
        }
        message.setText(messageText);
        message.setTypeface(null, m.isRead() ? Typeface.NORMAL : Typeface.BOLD);

        Log.d(getClass().getSimpleName(), "GetView: " + m);
        return convertView;
    }
}
