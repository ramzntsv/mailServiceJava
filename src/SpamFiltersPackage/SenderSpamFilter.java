package SpamFiltersPackage;

import UsersMessagePackage.Message;
import UsersMessagePackage.User;

public class SenderSpamFilter implements SpamFilter {
    private final User spamSender;

    public SenderSpamFilter(User spamSender) {
        this.spamSender = spamSender;
    }

    @Override
    public boolean isSpam(Message message) {
        String spamSenderName = spamSender.getUserName();
        return message.getSender().getUserName().equals(spamSenderName);
    }
}
