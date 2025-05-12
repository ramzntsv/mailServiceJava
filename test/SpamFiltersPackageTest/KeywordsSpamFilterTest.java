package SpamFiltersPackageTest;
import SpamFiltersPackage.KeywordsSpamFilter;
import UsersMessagePackage.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class KeywordsSpamFilterTest {
    private static final User sender = new User("Lenya");
    private static final User receiver = new User("Леня");

    private static final Message spamCaptionMessage = new Message("keyword2 caption keyword1",
            "text text", sender, receiver);
    private static final Message spamTextMessage = new Message("caption",
            "text keyword2 keyword 3 text ", sender, receiver);
    private static final Message normalMessage = new Message("caption caption",
            "text text text ", sender, receiver);

    private static final List<String> spamKeywords = List.of("keyword1", "keyword2",  "keyword3");

    private static final KeywordsSpamFilter keywordsSpamFilter = new KeywordsSpamFilter(spamKeywords);

    @Test
    void keywordsSpamFilterTest(){
        assertTrue(keywordsSpamFilter.isSpam(spamCaptionMessage));
        assertTrue(keywordsSpamFilter.isSpam(spamTextMessage));
        assertFalse(keywordsSpamFilter.isSpam(normalMessage));
    }

}