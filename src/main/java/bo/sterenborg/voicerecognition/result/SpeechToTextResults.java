package bo.sterenborg.voicerecognition.result;

import java.util.ArrayList;
import java.util.List;

public class SpeechToTextResults implements CSVAble {
    private List<SpeechToTextResult> speechToTextResults = new ArrayList<>();

    public void addSpeechToTextResult(SpeechToTextResult speechToTextResult) {
        this.speechToTextResults.add(speechToTextResult);
    }

    private float getPrecisionMicroAverage() {
        int totalSize = 0;
        float weightedPrecision = 0;
        for (SpeechToTextResult speechToTextResult : speechToTextResults) {
            final RecallAndPrecision recallAndPrecision = speechToTextResult.getRecallAndPrecision();
            final int size = recallAndPrecision.getRecognizedWords().size();
            weightedPrecision += size * recallAndPrecision.getPrecision();
            totalSize += size;
        }
        return weightedPrecision / totalSize;
    }

    private float getRecallMicroAverage() {
        int totalSize = 0;
        float weightedRecall = 0;
        for (SpeechToTextResult speechToTextResult : speechToTextResults) {
            final RecallAndPrecision recallAndPrecision = speechToTextResult.getRecallAndPrecision();
            final int size = recallAndPrecision.getOriginalWords().size();
            weightedRecall += size * recallAndPrecision.getRecall();
            totalSize += size;
        }
        return weightedRecall / totalSize;
    }

    private float getPrecisionMacroAverage() {
        final float allPrecisions = (float) speechToTextResults.stream().map(SpeechToTextResult::getRecallAndPrecision)
                .map(RecallAndPrecision::getPrecision).mapToDouble(Float::doubleValue).sum();
        return allPrecisions / speechToTextResults.size();
    }

    private float getRecallMacroAverage() {
        final float allRecalls = (float) speechToTextResults.stream().map(SpeechToTextResult::getRecallAndPrecision)
                .map(RecallAndPrecision::getRecall).mapToDouble(Float::doubleValue).sum();
        return allRecalls / speechToTextResults.size();
    }

    private float getAverageWordErrorRate() {
        final float allErrorRates = (float) speechToTextResults.stream().map(SpeechToTextResult::getWordErrorRate)
                .map(WordErrorRate::getRate).mapToDouble(Float::doubleValue).sum();
        return allErrorRates / speechToTextResults.size();
    }

    private float getAverageWordCorrectRate() {
        final float allCorrectRates = (float) speechToTextResults.stream().map(SpeechToTextResult::getWordCorrectRate)
                .map(WordCorrectRate::getRate).mapToDouble(Float::doubleValue).sum();
        return allCorrectRates / speechToTextResults.size();
    }

    private float getAverageRealTimeFactor() {
        final float allRealTimeFactors = (float) speechToTextResults.stream().map(SpeechToTextResult::getRealTimeFactor)
                .map(RealTimeFactor::getRealtimeFactor).mapToDouble(Float::doubleValue).sum();
        return allRealTimeFactors / speechToTextResults.size();
    }

    private float getFScoreMicroAverage() {
        final float recallAvg = getRecallMicroAverage();
        final float precisionAvg = getPrecisionMicroAverage();
        return (2 * precisionAvg * recallAvg) / (recallAvg + precisionAvg);
    }

    private float getFScoreMacroAverage() {
        final float recallAvg = getRecallMacroAverage();
        final float precisionAvg = getPrecisionMacroAverage();
        return (2 * precisionAvg * recallAvg) / (recallAvg + precisionAvg);
    }


    @Override
    public String toString() {
        return "Precision micro average: " + String.format("%.2f", getPrecisionMicroAverage()) + '\n' +
                "Precision macro average: " + String.format("%.2f", getPrecisionMacroAverage()) + '\n' +
                "Recall micro average: " + String.format("%.2f", getRecallMicroAverage()) + '\n' +
                "Recall macro average: " + String.format("%.2f", getRecallMacroAverage()) + '\n' +
                "F-Score micro average: " + String.format("%.2f", getFScoreMicroAverage()) + '\n' +
                "F-Score macro average: " + String.format("%.2f", getFScoreMacroAverage()) + '\n' +
                "Real time factor average: " + String.format("%.2f", getAverageRealTimeFactor()) + '\n' +
                "Word correct rate average: " + String.format("%.2f", getAverageWordCorrectRate()) + '\n' +
                "Word error rate average: " + String.format("%.2f", getAverageWordErrorRate()) + '\n';


    }

    public String toCSV() {
        return "Precision micro average;" + String.format("%.2f", getPrecisionMicroAverage()) + '\n' +
                "Precision macro average;" + String.format("%.2f", getPrecisionMacroAverage()) + '\n' +
                "Recall micro average;" + String.format("%.2f", getRecallMicroAverage()) + '\n' +
                "Recall macro average;" + String.format("%.2f", getRecallMacroAverage()) + '\n' +
                "F-Score micro average;" + String.format("%.2f", getFScoreMicroAverage()) + '\n' +
                "F-Score macro average;" + String.format("%.2f", getFScoreMacroAverage()) + '\n' +
                "Real time factor average;" + String.format("%.2f", getAverageRealTimeFactor()) + '\n' +
                "Word correct rate average;" + String.format("%.2f", getAverageWordCorrectRate()) + '\n' +
                "Word error rate average;" + String.format("%.2f", getAverageWordErrorRate()) + '\n';
    }
}
