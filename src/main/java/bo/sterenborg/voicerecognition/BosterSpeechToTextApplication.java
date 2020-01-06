package bo.sterenborg.voicerecognition;

import bo.sterenborg.voicerecognition.result.CSVAble;
import bo.sterenborg.voicerecognition.result.SpeechToTextResult;
import bo.sterenborg.voicerecognition.result.SpeechToTextResults;
import bo.sterenborg.voicerecognition.util.AudioUtils;
import bo.sterenborg.voicerecognition.util.exception.VoiceRecognitionException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BosterSpeechToTextApplication {
    private static final String RESULTS_DIRECTORY = "results/";

    public static void main(String[] args)  {
        final Scanner scanner = new Scanner(System.in);
        final FileSpeechToTextConverter converter = FileSpeechToTextConverter.newInstance();
        final SpeechToTextResults speechToTextResults = new SpeechToTextResults();
        final List<SpeechToTextResult> speechToTextResultList = new ArrayList<>();
        System.out.print("Would you like to use all file or one specific file? (A = all, S = specific): ");
        final char choice = scanner.next().charAt(0);
        if (Character.toLowerCase(choice) == 'a') {
            for (String audioFileName : AudioUtils.getAllOriginalAudioFileNames()) {
                final SpeechToTextResult speechToTextResult = converter.convertSpeechToText(audioFileName);
                speechToTextResultList.add(speechToTextResult);
                logResult(speechToTextResult, speechToTextResult.getFileName());
                saveAsCSV(speechToTextResult, speechToTextResult.getFileName());
            }
            speechToTextResultList.forEach(speechToTextResults::addSpeechToTextResult);
            logResult(speechToTextResults, "combined");
            saveAsCSV(speechToTextResults, "combined");
        }else{
            System.out.print("Which file would you like to use? (from /audiofiles/original): ");
            final String fileName = scanner.next();
            final SpeechToTextResult speechToTextResult = converter.convertSpeechToText(fileName);
            speechToTextResultList.add(speechToTextResult);
            logResult(speechToTextResult, speechToTextResult.getFileName());
            saveAsCSV(speechToTextResult, speechToTextResult.getFileName());
        }
    }

    private static void logResult(Object result, String fileName) {
        final String header = "---------- Result for " + fileName + " ----------\n";
        final String footer = header.replaceAll(".", "-");

        System.out.println(header);
        System.out.println(result);
        System.out.println(footer);
    }

    private static void saveAsCSV(CSVAble result, String fileName) {
        final String newFileName = RESULTS_DIRECTORY + fileName.replaceAll("\\.", "_") + "_result.csv";
        try (FileOutputStream os = new FileOutputStream(newFileName)) {
            os.write(result.toCSV().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new VoiceRecognitionException(e);
        }
    }
}
