package SpamFiltersPackageTest;
import SpamFiltersPackage.SenderSpamFilter;
import UsersMessagePackage.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SenderSpamFilterTest {
    private static final User spamSender = new User("Lenya");
    private static final User normalSender = new User("Леня");
    private static final User receiver = new User("shmulya_praim");

    private static final Message spamMessage = new Message("normal caption",
            "normal text", spamSender, receiver);
    private static final Message normalMessage = new Message("normal caption",
            "normal text", normalSender, receiver);

    private static final SenderSpamFilter senderSpamFilter = new SenderSpamFilter(spamSender);

    @Test
    void senderSpamFilterTest(){
        assertTrue(senderSpamFilter.isSpam(spamMessage));
        assertFalse(senderSpamFilter.isSpam(normalMessage));
    }
}