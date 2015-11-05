package claudiu.sics.smsfilter;

import java.util.List;

/**
 * Created by cosming on 11/2/15.
 */
public interface SMSManager {

    List<Message> getMessages();

    void markSMSRead(Message message);

    void deleteSMS(Message message);

    void saveSMS(Message message);

    int getMessageCount();

    List<Filter> getFilters();

    void saveFilter(Filter filter);

    void deleteFilter(Filter filter);

    int countFilters();

}
