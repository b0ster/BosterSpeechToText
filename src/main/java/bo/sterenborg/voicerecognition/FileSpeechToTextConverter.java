package bo.sterenborg.voicerecognition;

import bo.sterenborg.voicerecognition.result.*;
import com.google.cloud.dialogflow.v2.*;
import com.google.protobuf.ByteString;
import bo.sterenborg.voicerecognition.util.AudioUtils;
import bo.sterenborg.voicerecognition.util.exception.VoiceRecognitionException;
import bo.sterenborg.voicerecognition.util.SessionUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Map;

public class FileSpeechToTextConverter {
    private static final SessionsSettings SESSIONS_SETTINGS = SessionUtils.getDefaultSessionSettings();
    private static final Map<String, String> TRANSCRIPTIONS = AudioUtils.getTranscriptionAsMap();
    private Locale locale = Locale.US; //default locale is Locale.US

    private FileSpeechToTextConverter() {
    }

    public static FileSpeechToTextConverter newInstance() {
        return new FileSpeechToTextConverter();
    }

    public FileSpeechToTextConverter withLocale(Locale locale) {
        this.locale = locale;
        return this;
    }

    /**
     * Takes an audio file as input (f.e. "hello.mp3") and converts the speech within the file to text.
     *
     * @param originalFileName = the file name of the audio file. This file must be present in resources/audiofiles/original.
     * @return = {@link SpeechToTextResult}
     */
    public SpeechToTextResult convertSpeechToText(String originalFileName) {
        final String convertedFileName = AudioUtils.convertAudioFileToFlac(originalFileName);
        final SpeechToTextResult speechToTextResult = new SpeechToTextResult();

        final RealTimeFactor realTimeFactor = new RealTimeFactor(AudioUtils.getLengthOfAudioFileSeconds(
                AudioUtils.getRelativeAudioFilePath(originalFileName, AudioUtils.AudioFileType.ORIGINAL)));
        realTimeFactor.setSendTime(LocalDateTime.now());

        final String originalText = AudioUtils.findTranscriptionForFile(TRANSCRIPTIONS, originalFileName);
        try (SessionsClient sessionsClient = SessionsClient.create(SESSIONS_SETTINGS)) {
            final SessionName session = SessionName.of(SessionUtils.DIALOG_FLOW_PROPERTIES.getProjectId(), SessionUtils.newSessionUUID());
            final QueryInput queryInput = QueryInput.newBuilder().setAudioConfig(AudioUtils.createInputAudioConfig(locale)).build();
            final byte[] inputAudio = Files.readAllBytes(Paths.get(convertedFileName));
            final QueryResult queryResult = sessionsClient.detectIntent(createIntentRequest(session, queryInput, inputAudio)).getQueryResult();
            realTimeFactor.setResponseTime(LocalDateTime.now());
            speechToTextResult.setFileName(originalFileName);
            speechToTextResult.setConfidence(queryResult.getSpeechRecognitionConfidence());
            speechToTextResult.setRecognizedText(queryResult.getQueryText());
            speechToTextResult.setOriginalText(originalText);

            final WordCorrectRate wordCorrectRate = new WordCorrectRate(originalText, queryResult.getQueryText());
            speechToTextResult.setWordCorrectRate(wordCorrectRate);
            speechToTextResult.setWordErrorRate(new WordErrorRate(wordCorrectRate));
            speechToTextResult.setRealTimeFactor(realTimeFactor);

            final RecallAndPrecision recallAndPrecision = new RecallAndPrecision(originalText, queryResult.getQueryText());
            speechToTextResult.setRecallAndPrecision(recallAndPrecision);

            return speechToTextResult;
        } catch (IOException e) {
            throw new VoiceRecognitionException(e);
        }
    }

    /**
     * Creates an {@link DetectIntentRequest} used for speech to text conversion. Can be send towards DialogFlow
     * using {@link SessionsClient#detectIntent(DetectIntentRequest)}
     *
     * @param session    = Created {@link SessionName}
     * @param queryInput = Created {@link QueryInput}
     * @param inputAudio = a byte[] formatted input audio.
     * @return = {@link DetectIntentRequest}.
     */
    private DetectIntentRequest createIntentRequest(SessionName session, QueryInput queryInput, byte[] inputAudio) {
        return DetectIntentRequest.newBuilder()
                .setSession(session.toString())
                .setQueryInput(queryInput)
                .setInputAudio(ByteString.copyFrom(inputAudio))
                .build();
    }


}
