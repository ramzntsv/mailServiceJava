package SpamFiltersPackageTest;
import SpamFiltersPackage.SimpleSpamFilter;
import UsersMessagePackage.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SimpleSpamFilterTest {
    private static final User sender = new User("Lenya");
    private static final User receiver = new User("Леня");

    private static final Message normalMessage = new Message("normal caption",
            "normal text", sender, receiver);
    private static final Message notSpamMessage = new Message("notSpam caption",
            "notSpam text", sender, receiver);
    private static final Message spamCaptionMessage = new Message("spAm caption",
            "normal text", sender, receiver);
    private static final Message spamTextMessage = new Message("normal caption",
            "spam_+_spam text", sender, receiver);

    private static final SimpleSpamFilter simpleSpamFilter = new SimpleSpamFilter();

    @Test
    void simpleSpamFilterTest(){

        assertTrue(simpleSpamFilter.isSpam(spamCaptionMessage));
        assertTrue(simpleSpamFilter.isSpam(spamTextMessage));
        assertFalse(simpleSpamFilter.isSpam(normalMessage));
        assertFalse(simpleSpamFilter.isSpam(notSpamMessage));

    }
}
