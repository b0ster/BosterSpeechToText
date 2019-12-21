package bo.sterenborg.voicerecognition.result;

import java.util.Arrays;
import java.util.List;

public class WordCorrectRate {
    private String originalText;
    private String recognizedText;
    private Float rate;

    public WordCorrectRate(String originalText, String recognizedText) {
        this.originalText = originalText;
        this.recognizedText = recognizedText;
    }

    public float getRate() {
        if (rate != null) {
            return rate;
        }
        final List<String> originalWords = Arrays.asList(originalText.split("\\W+"));
        final List<String> recognizedWords = Arrays.asList(recognizedText.split("\\W+"));

        int correctWords = 0;
        for (int i = 0; i < recognizedWords.size(); i++) {
            if (originalWords.size() > i) {
                if (originalWords.get(i).equalsIgnoreCase(recognizedWords.get(i))) {
                    correctWords++;
                }
            }
        }
        rate = (float) correctWords / originalWords.size();
        return rate;
    }

}
