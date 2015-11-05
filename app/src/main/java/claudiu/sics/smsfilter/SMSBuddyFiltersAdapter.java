package claudiu.sics.smsfilter;


import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import sisc.claudiu.smsfilter.R;

public class SMSBuddyFiltersAdapter extends BaseAdapter {

    private SMSManagerDefault manager;

    public void setManager(SMSManagerDefault manager) {
        this.manager = manager;
    }

    public SMSManagerDefault getManager() {
        return manager;
    }

    @Override
    public int getCount() {
        return manager.countFilters();
    }

    @Override
    public Object getItem(int position) {
        return manager.getFilters().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            convertView = layoutInflater.inflate(R.layout.filter_layout, parent, false);
        }
        final SMSBuddyFilter m = (SMSBuddyFilter) getItem(position);

        TextView labelText = (TextView) convertView.findViewById(R.id.labelFilter);
        SpannableString labelSpan = new SpannableString(m.getLabel());
        labelSpan.setSpan(new UnderlineSpan(), 0, labelSpan.length(), 0);
        labelText.setText(labelSpan);
        ((TextView) convertView.findViewById(R.id.phoneFilter)).setText(m.getPhonePattern());
        ((TextView) convertView.findViewById(R.id.patternFilter)).setText(m.getMessagePattern());

        final View viewById = convertView.findViewById(R.id.fromToLayout);

        ((TextView) viewById.findViewById(R.id.fromFilter)).setText(m.getStartTime());
        ((TextView) viewById.findViewById(R.id.toFilter)).setText(m.getEndTime());

        return convertView;

    }
}
