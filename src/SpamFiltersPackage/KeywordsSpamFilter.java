package SpamFiltersPackage;

import UsersMessagePackage.Message;

import java.util.List;

public class KeywordsSpamFilter implements SpamFilter {
    private final List<String> spamKeywords;

    public KeywordsSpamFilter(List<String> spamKeywords) {
        this.spamKeywords = spamKeywords;
    }
    @Override
    public boolean isSpam(Message message) {
        String caption = message.getCaption().toLowerCase();
        String text = message.getText().toLowerCase();

        for (String spamKeyword : spamKeywords) {
            String lowerSpamKeyword = spamKeyword.toLowerCase();
            boolean captionContainsSpam = caption.contains(lowerSpamKeyword);
            boolean textContainsSpam = text.contains(lowerSpamKeyword);

            if (captionContainsSpam || textContainsSpam){
                return true;
            }

        }
        return false;
    }
}
