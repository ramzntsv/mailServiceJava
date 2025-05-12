package SpamFiltersPackage;

import UsersMessagePackage.Message;

import java.util.*;
public class CompositeSpamFilter implements SpamFilter {
    private final List<SpamFilter> spamFiltersList = new ArrayList<>();

    public void addFilter(SpamFilter filter) {
        spamFiltersList.add(filter);
    }

    public boolean isEmpty(){
        return spamFiltersList.isEmpty();
    }

    @Override
    public boolean isSpam(Message message) {
        for (SpamFilter spamFilter : spamFiltersList) {
            if (spamFilter.isSpam(message)) {
                return true;
            }
        }
        return false;
    }
}
