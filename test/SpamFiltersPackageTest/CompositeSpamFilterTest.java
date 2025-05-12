package SpamFiltersPackageTest;
import SpamFiltersPackage.*;
import UsersMessagePackage.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CompositeSpamFilterTest {
    private static final User sender = new User("Lenya");
    private static final User receiver = new User("Леня");

    private static final Message message = new Message("caption",
            "text", sender, receiver);

    private static final SpamFilter spamFilterFalse = message1 -> false;
    private static final SpamFilter spamFilterTrue = message1 -> true;

    private static final CompositeSpamFilter compositeSpamFilter = new CompositeSpamFilter();

    @Test
    void compositeSpamFilterTest(){

        assertTrue(compositeSpamFilter.isEmpty());

        compositeSpamFilter.addFilter(spamFilterFalse);
        assertFalse(compositeSpamFilter.isSpam(message));

        compositeSpamFilter.addFilter(spamFilterTrue);
        assertTrue(compositeSpamFilter.isSpam(message));
    }

}