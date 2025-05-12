package UsersMessagePackage;

import SpamFiltersPackage.SpamFilter;

import java.util.*;

public class User {
    private final String userName;
    private final List<Message> inbox;
    private final  List<Message> outbox;
    private final  List<Message> spam;
    private SpamFilter spamFilter;

    public User(String userName) {
        this.userName = userName;
        this.inbox = new ArrayList<>();
        this.outbox = new ArrayList<>();
        this.spam = new ArrayList<>();
        this.spamFilter = null;
    }

    public String getUserName() {
        return userName;
    }
    public List<Message> getInbox() {
        return new ArrayList<>(inbox);
    }
    public List<Message> getOutbox() {
        return new ArrayList<>(outbox);
    }
    public List<Message> getSpam() {
        return new ArrayList<>(spam);
    }

    public void setSpamFilter(SpamFilter spamFilter) {
        this.spamFilter = spamFilter;
    }

    public void sendMessage(User receiver, String caption, String text) {
        Message message = new Message(caption, text, this, receiver);
        outbox.add(message);
        if (receiver.spamFilter != null && receiver.spamFilter.isSpam(message)) {
            receiver.spam.add(message);
        } else {
            receiver.inbox.add(message);
        }
    }
}