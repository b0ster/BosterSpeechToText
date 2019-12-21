package bo.sterenborg.voicerecognition.util;

import bo.sterenborg.voicerecognition.util.exception.VoiceRecognitionException;
import com.google.cloud.dialogflow.v2.AudioEncoding;
import com.google.cloud.dialogflow.v2.InputAudioConfig;
import ie.corballis.sox.Sox;
import ie.corballis.sox.WrongParametersException;
import io.opencensus.internal.StringUtils;
import sun.management.FileSystem;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author Bo Sterenborg
 * Provides some audio utilities for this application.
 */
public class AudioUtils {
    private static final AudioEncoding DEFAULT_AUDIO_ENCODING = AudioEncoding.AUDIO_ENCODING_FLAC;
    private static final int DEFAULT_SAMPLE_RATE = 22050;
    private static final int DEFAULT_BITS = 16;
    private static final String SOX_PATH = "sox/sox.exe";
    private static final String RESOURCES_PATH = "src/main/resources";
    private static final String ORIGINAL_AUDIO_FILES_PATH = RESOURCES_PATH + "/audiofiles/original";
    private static final String FLAC_EXTENSION = ".flac";
    private static final String CONVERTED_AUDIO_FILES_PATH = RESOURCES_PATH + "/audiofiles/converted";
    private static final String TRANSCRIPTIONS_CSV_PATH = RESOURCES_PATH + "/audiofiles/transcript/transcriptions.csv";


    /**
     * Converts a given audio file to a .flac audio file.
     *
     * @param originalFileName = the original file name (no path specified, f.e. "hello.mp3")
     * @return = the relative path to the new converted flac audio file.
     */
    public synchronized static String convertAudioFileToFlac(String originalFileName) {
        final String destination = CONVERTED_AUDIO_FILES_PATH + '/' +
                ((originalFileName.contains(".")
                        ? originalFileName.substring(0, originalFileName.lastIndexOf('.'))
                        : originalFileName) + FLAC_EXTENSION);
        try {
            //convert file using SOX library
            new Sox(SOX_PATH)
                    .inputFile(getRelativeAudioFilePath(originalFileName, AudioFileType.ORIGINAL))
                    .sampleRate(DEFAULT_SAMPLE_RATE)
                    .bits(DEFAULT_BITS)
                    .outputFile(destination)
                    .execute();
            return destination;
        } catch (IOException | WrongParametersException e) {
            throw new VoiceRecognitionException(e);
        }
    }

    public static float getLengthOfAudioFileSeconds(String convertedFileName) {
        try {
            File file = new File(convertedFileName);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            AudioFormat format = audioInputStream.getFormat();
            long frames = audioInputStream.getFrameLength();
            return (float) frames / format.getFrameRate();
        } catch (UnsupportedAudioFileException | IOException e) {
            throw new VoiceRecognitionException(e);
        }
    }

    /**
     * Gets the absolute path of an audio file.
     *
     * @param name          = name of audio file (f.e. "hello.mp3").
     * @param audioFileType = the type of file (converted or original).
     * @return = String representation of relative path.
     */
    public static String getRelativeAudioFilePath(String name, AudioFileType audioFileType) {
        return getAllAudioFilePaths(audioFileType)
                .stream()
                .filter(e -> e.endsWith(name))
                .findAny()
                .orElseThrow(() -> new VoiceRecognitionException(new FileNotFoundException(name)));
    }


    /**
     * Creates a {@link InputAudioConfig} only with given locale and the default properties.
     *
     * @param locale = {@link Locale} to use for speech recognition.
     *               Note that not all the locales are supported by Google yet.
     * @return = {@link InputAudioConfig} instance.
     */
    public static InputAudioConfig createInputAudioConfig(Locale locale) {
        return InputAudioConfig.newBuilder()
                .setAudioEncoding(AudioUtils.DEFAULT_AUDIO_ENCODING)
                .setLanguageCode(toGoogleLocaleString(locale))
                .setSampleRateHertz(AudioUtils.DEFAULT_SAMPLE_RATE)
                .build();
    }

    /**
     * Reads the audio transcriptions as CSV and returns a map with audio file name as key, and transcript as value.
     * Note: the key differs from the real audio file name, as multiple persons has spoken the same record.
     * This means the key must be appended with the person's name.
     *
     * @return = Map with audio file as key and transcript as value.
     */
    public static Map<String, String> getTranscriptionAsMap() {
        try {
            final Map<String, String> transcriptionMap = new HashMap<>();
            final List<String> lines = Files.readAllLines(Paths.get(TRANSCRIPTIONS_CSV_PATH), StandardCharsets.ISO_8859_1);
            for (String line : lines) {
                final int separatorIndex = line.indexOf(';');
                final String key = line.substring(0, separatorIndex);
                final String val = line.substring(separatorIndex + 1);
                transcriptionMap.put(key, val.trim());
            }
            return transcriptionMap;
        } catch (IOException e) {
            throw new VoiceRecognitionException(e);
        }
    }

    /**
     * Finds a transcription for a given file (only file name no path).
     *
     * @param transcriptions = all the transcriptions.
     * @param fileName       = name of the file to search for.
     * @return = String value of transcription.
     */
    public static String findTranscriptionForFile(Map<String, String> transcriptions, String fileName) {
        int counter = 0;
        String transcription = null;
        for (Map.Entry<String, String> entry : transcriptions.entrySet()) {
            if (fileName.startsWith(entry.getKey())) {
                counter++;
                transcription = entry.getValue();
            }
        }
        if (counter > 1) {
            System.err.println("There are multiple transcriptions for file: " + fileName);
        }
        if (transcription == null) {
            throw new VoiceRecognitionException("No transcription found for file: " + fileName);
        }
        return transcription;
    }


    /**
     * Gets all the audio file names in the original audio file directory.
     *
     * @return = Set of audio file names.
     */
    public static Set<String> getAllOriginalAudioFileNames() {
        final Set<String> allAudioFilePaths = getAllAudioFilePaths(AudioFileType.ORIGINAL);
        final Set<String> fileNames = new HashSet<>();
        for (String allAudioFilePath : allAudioFilePaths) {
            fileNames.add(allAudioFilePath.substring(allAudioFilePath.lastIndexOf(File.separator) + 1));
        }
        return fileNames;
    }


    /**
     * Gets all the audio files within either the 'original' audio folder or 'converted' audio folder.
     *
     * @param audioFileType = the {@link AudioFileType} to hint which directory to use.
     * @return = Set of all relative file paths within the chosen audio folder.
     */
    private static Set<String> getAllAudioFilePaths(AudioFileType audioFileType) {
        final String dir = audioFileType == AudioFileType.ORIGINAL
                ? ORIGINAL_AUDIO_FILES_PATH
                : CONVERTED_AUDIO_FILES_PATH;
        try {

            final Set<String> allPaths = Files.list(Paths.get(dir)).filter(Files::isRegularFile)
                    .map(Path::toString).collect(Collectors.toSet());
            final List<Path> firstLayerDirectories = Files.list(Paths.get(dir)).filter(Files::isDirectory)
                    .collect(Collectors.toList());
            for (Path firstLayerDirectory : firstLayerDirectories) {
                Set<String> nestedFiles = Files.list(firstLayerDirectory).filter(Files::isRegularFile)
                        .map(Path::toString).collect(Collectors.toSet());
                allPaths.addAll(nestedFiles);
            }
            return allPaths;
        } catch (IOException e) {
            throw new VoiceRecognitionException(e);
        }
    }

    /**
     * Converts a Java locale {@link Locale} to a Google's String convention.
     *
     * @param locale = a {@link Locale}
     * @return = String value of Google's locale.
     */
    private static String toGoogleLocaleString(Locale locale) {
        return locale.toString().replaceAll("_", "-");
    }


    public enum AudioFileType {
        ORIGINAL,
        CONVERTED
    }
}
