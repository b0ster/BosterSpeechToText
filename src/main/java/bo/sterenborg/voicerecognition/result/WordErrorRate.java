package bo.sterenborg.voicerecognition.result;

public class WordErrorRate {
    private final WordCorrectRate wordCorrectRate;

    public WordErrorRate(WordCorrectRate wordCorrectRate) {
        this.wordCorrectRate = wordCorrectRate;
    }

    public float getRate() {
        return 1f - wordCorrectRate.getRate();
    }
}
