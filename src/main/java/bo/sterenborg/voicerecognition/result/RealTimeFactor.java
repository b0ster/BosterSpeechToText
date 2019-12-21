package bo.sterenborg.voicerecognition.result;

import java.time.Duration;
import java.time.LocalDateTime;

public class RealTimeFactor {
    private LocalDateTime sendTime;
    private float audioFileLength;
    private LocalDateTime responseTime;

    public RealTimeFactor(float audioFileLength) {
        this.audioFileLength = audioFileLength;
    }

    public void setSendTime(LocalDateTime sendTime) {
        this.sendTime = sendTime;
    }

    public void setResponseTime(LocalDateTime responseTime) {
        this.responseTime = responseTime;
    }

    public float getSecondsNeeded() {
        return (float) (Duration.between(sendTime, responseTime).toMillis() / 1000.00);
    }

    public float getRealtimeFactor() {
        return getSecondsNeeded() / audioFileLength;
    }


}
