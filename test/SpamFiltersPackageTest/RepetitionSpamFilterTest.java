package SpamFiltersPackageTest;
import SpamFiltersPackage.RepetitionSpamFilter;
import UsersMessagePackage.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigInteger;

class RepetitionSpamFilterTest {
    private static final User sender = new User("Lenya");
    private static final User receiver = new User("Леня");

    private static final Message spamMessage = new Message("caption",
            "text text text", sender, receiver);
    private static final Message normalMessage = new Message("caption caption caption",
            "text text", sender, receiver);

    private static final BigInteger MAX_REPETITION_COUNT = BigInteger.valueOf(3);

    private static final RepetitionSpamFilter repetitionSpamFilter
            = new RepetitionSpamFilter(MAX_REPETITION_COUNT);

    @Test
    void repetitionSpamFilterTest(){
        assertTrue(repetitionSpamFilter.isSpam(spamMessage));
        assertFalse(repetitionSpamFilter.isSpam(normalMessage));
    }
}