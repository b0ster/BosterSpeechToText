package bo.sterenborg.voicerecognition.util.exception;

public class VoiceRecognitionException extends RuntimeException {

    public VoiceRecognitionException() {
    }

    public VoiceRecognitionException(String message) {
        super(message);
    }

    public VoiceRecognitionException(String message, Throwable cause) {
        super(message, cause);
    }

    public VoiceRecognitionException(Throwable cause) {
        super(cause);
    }

    public VoiceRecognitionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
