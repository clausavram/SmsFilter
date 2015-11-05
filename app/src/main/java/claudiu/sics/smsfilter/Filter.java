package claudiu.sics.smsfilter;

import android.util.Log;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Filter implements Serializable {

    private long id = -1;
    private String label;
    private String startTime;
    private String endTime;
    private String phone;
    private String messagePattern;
    private boolean dateFilter;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String time) {
        this.startTime = time;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String end) {
        this.endTime = end;
    }

    public String getPhonePattern() {
        return phone;
    }

    public void setPhonePattern(String phone) {
        this.phone = phone;
    }

    public String getMessagePattern() {
        return messagePattern;
    }

    public void setMessagePattern(String messagePattern) {
        this.messagePattern = messagePattern;
    }

    public boolean isDateFilter() {
        return dateFilter;
    }

    public void setDateFilter(boolean dateFilter) {
        this.dateFilter = dateFilter;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String name) {
        this.label = name;
    }

    private boolean phoneMatch(Message message) {
        return (phone == null && message.getPhone() == null) ||
                (phone != null &&
                        (phone.isEmpty()
                                || message.getPhone() != null && (phone.equals(message.getPhone()) || message.getPhone().matches(phone)))
                );
    }

    private boolean contentMatch(Message message) {
        return (messagePattern == null && message.getMessage() == null) ||
                (messagePattern != null && message.getMessage() != null &&
                        (messagePattern.equals(message.getMessage()) || message.getMessage().matches(messagePattern)));
    }

    public boolean matches(Message message) {
        Log.d(getClass().getSimpleName(), toString());
        boolean phoneMatch = phoneMatch(message);
        if (!phoneMatch) {
            Log.d(getClass().getSimpleName(), "phone not matching");
            return false;
        }
        boolean contentMatch = contentMatch(message);
        if (!contentMatch) {
            Log.d(getClass().getSimpleName(), "content not matching");
            return false;
        }
        try {
            if (isDateFilter()) {
                Date from = parseToDate(getStartTime(), true);
                if (from != null && !from.before(message.getTimestamp())) {
                    Log.d(getClass().getSimpleName(), "FROM DATE not matching (from=" + from + ") current " + message.getTimestamp());
                    return false;
                }
                Date to = parseToDate(getEndTime(), false);
                if (to != null && !message.getTimestamp().before(to)) {
                    Log.d(getClass().getSimpleName(), "TO DATE not matching (to=" + to + ") current " + message.getTimestamp());
                    return false;
                }
            } else {
                Date from = parseToTime(message.getTimestamp(), getStartTime(), true);
                if (from != null && !from.before(message.getTimestamp())) {
                    Log.d(getClass().getSimpleName(), "FROM TIME not matching (from=" + from + ") current " + message.getTimestamp());
                    return false;
                }
                Date to = parseToTime(message.getTimestamp(), getEndTime(), false);
                if (to != null && !message.getTimestamp().before(to)) {
                    Log.d(getClass().getSimpleName(), "TO TIME not matching (to=" + to + ") current " + message.getTimestamp());
                    return false;
                }
            }
            return true;
        } catch (final Exception e) {
            Log.d(getClass().getSimpleName(), "Failed to check timestamp filtering!", e);
        }
        return false;
    }

    private static Date parseToTime(Date currentTimestamp, String date, boolean from) throws ParseException {
        if (date == null) {
            return null;
        }
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Resources.FORMAT_HH_MM);
        final Date parse = simpleDateFormat.parse(date);
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(parse);

        final Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.setTime(currentTimestamp);

        calendar.set(Calendar.YEAR, currentCalendar.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, currentCalendar.get(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, currentCalendar.get(Calendar.DAY_OF_MONTH));
        if (from) {
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
        } else {
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 999);
        }
        return calendar.getTime();
    }

    private static Date parseToDate(String date, boolean from) throws ParseException {
        if (date == null) {
            return null;
        }
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Resources.FORMAT_YYYY_MM_DD);
        final Date parse = simpleDateFormat.parse(date);
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(parse);
        if (from) {
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
        } else {
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 999);
        }
        return parse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Filter that = (Filter) o;

        if (getId() != that.getId()) return false;
        if (isDateFilter() != that.isDateFilter()) return false;
        if (getStartTime() != null ? !getStartTime().equals(that.getStartTime()) : that.getStartTime() != null)
            return false;
        if (getEndTime() != null ? !getEndTime().equals(that.getEndTime()) : that.getEndTime() != null)
            return false;
        if (getPhonePattern() != null ? !getPhonePattern().equals(that.getPhonePattern()) : that.getPhonePattern() != null)
            return false;
        return !(getMessagePattern() != null ? !getMessagePattern().equals(that.getMessagePattern()) : that.getMessagePattern() != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + (getStartTime() != null ? getStartTime().hashCode() : 0);
        result = 31 * result + (getEndTime() != null ? getEndTime().hashCode() : 0);
        result = 31 * result + (getPhonePattern() != null ? getPhonePattern().hashCode() : 0);
        result = 31 * result + (getMessagePattern() != null ? getMessagePattern().hashCode() : 0);
        result = 31 * result + (isDateFilter() ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Filter{" +
                "label=" + label +
                ", id=" + id +
                ", startTime='" + startTime + '\'' +
                ", to='" + endTime + '\'' +
                ", phone='" + phone + '\'' +
                ", contentPattern='" + messagePattern + '\'' +
                ", dateFilter=" + dateFilter +
                '}';
    }
}
