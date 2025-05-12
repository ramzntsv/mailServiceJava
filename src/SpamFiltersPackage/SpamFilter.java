package SpamFiltersPackage;
import UsersMessagePackage.Message;

public interface SpamFilter {
    boolean isSpam(Message message);
}
