package SpamFiltersPackage;

import UsersMessagePackage.Message;

import java.util.*;
import java.math.BigInteger;

public class RepetitionSpamFilter implements SpamFilter {
    private final BigInteger maxRepetitions;

    public RepetitionSpamFilter(BigInteger maxRepetitions) {
        this.maxRepetitions = maxRepetitions;
    }

    @Override
    public boolean isSpam(Message message) {
        String text = message.getText();
        List<String> wordsInText = List.of(text.split("\\W+"));
        for (String word: wordsInText){
            BigInteger wordInTextCount = BigInteger.valueOf(Collections.frequency(wordsInText, word));
            if (wordInTextCount.compareTo(maxRepetitions) >= 0){
                return true;
            }
        }

        return false;
    }
}
