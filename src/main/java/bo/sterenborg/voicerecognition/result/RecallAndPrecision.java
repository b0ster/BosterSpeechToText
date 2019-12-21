package bo.sterenborg.voicerecognition.result;

import java.util.Arrays;
import java.util.List;

public class RecallAndPrecision {
    private List<String> originalWords;
    private List<String> recognizedWords;
    private float recall;
    private float precision;

    public RecallAndPrecision(String originalText, String recognizedText) {
        this.originalWords = Arrays.asList(originalText.split("\\W+"));
        this.recognizedWords = Arrays.asList(recognizedText.split("\\W+"));
        this.recall = (float) getRelevantDocumentCount() / originalWords.size();
        this.precision = (float) getRelevantDocumentCount() / recognizedWords.size();
    }

    public float getRecall() {
        return recall;
    }

    public float getPrecision() {
        return precision;
    }

    public float getFScore() {
        return (2 * precision * recall) / (recall + precision);
    }


    public List<String> getOriginalWords() {
        return originalWords;
    }

    public List<String> getRecognizedWords() {
        return recognizedWords;
    }

    private int getRelevantDocumentCount() {
        int count = 0;
        for (String recognizedWord : recognizedWords) {
            if (containsIgnoreCase(recognizedWord, originalWords)) {
                count++;
            }
        }
        return count;
    }

    private boolean containsIgnoreCase(String word, List<String> listToSearch) {
        for (String toSearch : listToSearch) {
            if (toSearch.equalsIgnoreCase(word)) {
                return true;
            }
        }
        return false;
    }
}
