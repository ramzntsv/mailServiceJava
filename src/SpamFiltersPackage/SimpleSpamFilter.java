package SpamFiltersPackage;
import UsersMessagePackage.Message;

import java.util.List;
public class SimpleSpamFilter implements SpamFilter {
    @Override
    public boolean isSpam(Message message) {
        List<String> caption = List.of(message.getCaption().toLowerCase().split("[^a-zA-Z0-9]+"));
        List<String> text = List.of(message.getText().toLowerCase().split("[^a-zA-Z0-9]+"));
        boolean isMessageSpam = false;
        for (String word : caption) {
            if (word.equals("spam")) {
                isMessageSpam = true;
                break;
            }
        }

        for (String word : text) {
            if (word.equals("spam")) {
                isMessageSpam = true;
                break;
            }
        }

        return  isMessageSpam;
    }
}
