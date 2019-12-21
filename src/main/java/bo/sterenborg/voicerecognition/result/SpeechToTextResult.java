package bo.sterenborg.voicerecognition.result;


public class SpeechToTextResult implements CSVAble {
    private String fileName;
    private String originalText;
    private String recognizedText;
    private Float confidence;
    private RealTimeFactor realTimeFactor;
    private WordCorrectRate wordCorrectRate;
    private WordErrorRate wordErrorRate;
    private RecallAndPrecision recallAndPrecision;


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getRecognizedText() {
        return recognizedText;
    }

    public void setRecognizedText(String recognizedText) {
        this.recognizedText = recognizedText;
    }

    public Float getConfidence() {
        return confidence;
    }

    public void setConfidence(Float confidence) {
        this.confidence = confidence;
    }

    public RealTimeFactor getRealTimeFactor() {
        return realTimeFactor;
    }

    public void setRealTimeFactor(RealTimeFactor realTimeFactor) {
        this.realTimeFactor = realTimeFactor;
    }

    public WordCorrectRate getWordCorrectRate() {
        return wordCorrectRate;
    }

    public void setWordCorrectRate(WordCorrectRate wordCorrectRate) {
        this.wordCorrectRate = wordCorrectRate;
    }

    public WordErrorRate getWordErrorRate() {
        return wordErrorRate;
    }

    public void setWordErrorRate(WordErrorRate wordErrorRate) {
        this.wordErrorRate = wordErrorRate;
    }

    public String getOriginalText() {
        return originalText;
    }

    public void setOriginalText(String originalText) {
        this.originalText = originalText;
    }

    public RecallAndPrecision getRecallAndPrecision() {
        return recallAndPrecision;
    }

    public void setRecallAndPrecision(RecallAndPrecision recallAndPrecision) {
        this.recallAndPrecision = recallAndPrecision;
    }

    @Override
    public String toString() {
        return "Filename: " + fileName + '\n' +
                "Original text: [" + originalText + "]\n" +
                "Recognized text: [" + recognizedText + "]\n" +
                "Confidence: " + String.format("%.2f", confidence * 100) + "%\n" +
                "Time needed (seconds): " + String.format("%.2f", realTimeFactor.getSecondsNeeded()) + '\n' +
                "Real time factor: " + String.format("%.2f", realTimeFactor.getRealtimeFactor()) + '\n' +
                "Word correct rate: " + String.format("%.2f", wordCorrectRate.getRate()) + '\n' +
                "Word error rate: " + String.format("%.2f", wordErrorRate.getRate()) + '\n' +
                "Recall: " + String.format("%.2f", recallAndPrecision.getRecall()) + '\n' +
                "Precision: " + String.format("%.2f", recallAndPrecision.getPrecision()) + '\n' +
                "F-Score: " + String.format("%.2f", recallAndPrecision.getFScore()) + '\n';
    }


    public String toCSV() {
        return "Filename;" + fileName + '\n' +
                "Original text;" + originalText + "\n" +
                "Recognized text;" + recognizedText + "\n" +
                "Confidence;" + String.format("%.2f", confidence * 100) + "%\n" +
                "Time needed (seconds);" + String.format("%.2f", realTimeFactor.getSecondsNeeded()) + '\n' +
                "Real time factor;" + String.format("%.2f", realTimeFactor.getRealtimeFactor()) + '\n' +
                "Word correct rate;" + String.format("%.2f", wordCorrectRate.getRate()) + '\n' +
                "Word error rate;" + String.format("%.2f", wordErrorRate.getRate()) + '\n' +
                "Recall;" + String.format("%.2f", recallAndPrecision.getRecall()) + '\n' +
                "Precision;" + String.format("%.2f", recallAndPrecision.getPrecision()) + '\n' +
                "F-Score;" + String.format("%.2f", recallAndPrecision.getFScore()) + '\n';
    }


}
