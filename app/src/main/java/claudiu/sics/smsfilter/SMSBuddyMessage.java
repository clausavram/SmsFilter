package claudiu.sics.smsfilter;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by cosming on 10/31/15.
 */
public class SMSBuddyMessage implements Serializable {

    private long id = -1;
    private String phone;
    private String message;
    private boolean handled;
    private Date timestamp;

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isRead() {
        return handled;
    }

    public void setHandled(boolean handled) {
        this.handled = handled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SMSBuddyMessage that = (SMSBuddyMessage) o;

        if (getId() != that.getId()) return false;
        if (isRead() != that.isRead()) return false;
        if (getPhone() != null ? !getPhone().equals(that.getPhone()) : that.getPhone() != null)
            return false;
        if (getMessage() != null ? !getMessage().equals(that.getMessage()) : that.getMessage() != null)
            return false;
        return !(getTimestamp() != null ? !getTimestamp().equals(that.getTimestamp()) : that.getTimestamp() != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + (getPhone() != null ? getPhone().hashCode() : 0);
        result = 31 * result + (getMessage() != null ? getMessage().hashCode() : 0);
        result = 31 * result + (isRead() ? 1 : 0);
        result = 31 * result + (getTimestamp() != null ? getTimestamp().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SMSBuddyMessage{" +
                "id=" + id +
                ", phone='" + phone + '\'' +
                ", message='" + message + '\'' +
                ", handled=" + handled +
                ", timestamp=" + timestamp +
                '}';
    }
}
