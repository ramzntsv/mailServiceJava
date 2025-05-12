package UsersMessagePackageTest;
import SpamFiltersPackage.SpamFilter;
import UsersMessagePackage.Message;
import UsersMessagePackage.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


class UserTest {

    private static User sender;
    private static User receiver;

    @BeforeEach
    void setUp() {
        sender = new User("Lenya");
        receiver = new User("Леня");
    }

    @Test
    void sendMessageToInboxNoSpamFilter() {
        sender.sendMessage(receiver, "test caption", "test text");

        List<Message> receiverInbox = receiver.getInbox();
        List<Message> senderOutbox = sender.getOutbox();
        List<Message> receiverSpam = receiver.getSpam();

        assertTrue(receiverSpam.isEmpty());
        assertEquals(receiverInbox.getFirst(), senderOutbox.getFirst());

        assertEquals("test caption", receiverInbox.getFirst().getCaption());
        assertEquals("test text", receiverInbox.getFirst().getText());
        assertEquals(sender, receiverInbox.getFirst().getSender());
        assertEquals(receiver, senderOutbox.getFirst().getReceiver());

        assertEquals("Lenya" , sender.getUserName());
        assertEquals("Леня", receiver.getUserName());
    }

    @Test
    void sendMessageToInboxFalseSpamFilter(){
        SpamFilter spam = message -> false;
        receiver.setSpamFilter(spam);
        sender.sendMessage(receiver, "test caption", "test text");

        List<Message> receiverInbox = receiver.getInbox();
        List<Message> senderOutbox = sender.getOutbox();
        List<Message> receiverSpam = receiver.getSpam();

        assertTrue(receiverSpam.isEmpty());
        assertEquals(senderOutbox.getFirst(), receiverInbox.getFirst());
    }

    @Test
    void sendMessageToSpam(){
        SpamFilter spam = message -> true;
        receiver.setSpamFilter(spam);
        sender.sendMessage(receiver, "test caption", "test text");

        List<Message> receiverInbox = receiver.getInbox();
        List<Message> senderOutbox = sender.getOutbox();
        List<Message> receiverSpam = receiver.getSpam();

        assertTrue(receiverInbox.isEmpty());
        assertEquals(senderOutbox.getFirst(), receiverSpam.getFirst());
    }

}