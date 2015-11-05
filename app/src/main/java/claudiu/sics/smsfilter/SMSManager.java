package claudiu.sics.smsfilter;

import java.util.List;

/**
 * Created by cosming on 11/2/15.
 */
public interface SMSManager {

    List<SMSBuddyMessage> getMessages();

    void markSMSRead(SMSBuddyMessage message);

    void deleteSMS(SMSBuddyMessage message);

    void saveSMS(SMSBuddyMessage message);

    int getMessageCount();

    List<SMSBuddyFilter> getFilters();

    void saveFilter(SMSBuddyFilter filter);

    void deleteFilter(SMSBuddyFilter filter);

    int countFilters();

}
