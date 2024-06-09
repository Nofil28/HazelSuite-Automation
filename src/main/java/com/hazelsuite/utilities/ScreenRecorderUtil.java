package com.hazelsuite.utilities;

import org.monte.media.Format;
import org.monte.media.FormatKeys.MediaType;
import org.monte.media.Registry;
import org.monte.media.math.Rational;
import org.monte.screenrecorder.ScreenRecorder;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import static org.monte.media.AudioFormatKeys.EncodingKey;
import static org.monte.media.AudioFormatKeys.FrameRateKey;
import static org.monte.media.AudioFormatKeys.KeyFrameIntervalKey;
import static org.monte.media.AudioFormatKeys.MIME_AVI;
import static org.monte.media.AudioFormatKeys.MediaTypeKey;
import static org.monte.media.AudioFormatKeys.MimeTypeKey;
import static org.monte.media.VideoFormatKeys.*;

public class ScreenRecorderUtil extends ScreenRecorder {
    private static ScreenRecorder screenRecorder;
    private final String name;

    public ScreenRecorderUtil(GraphicsConfiguration cfg, Rectangle captureArea, Format fileFormat,
                              Format screenFormat, Format mouseFormat, Format audioFormat, File movieFolder, String name)
            throws IOException, AWTException {
        super(cfg, captureArea, fileFormat, screenFormat, mouseFormat, audioFormat, movieFolder);
        this.name = name;
    }

    @Override
    protected File createMovieFile(Format fileFormat) throws IOException {
        if (!movieFolder.exists()) {
            movieFolder.mkdirs();
        } else if (!movieFolder.isDirectory()) {
            throw new IOException("\"" + movieFolder + "\" is not a directory.");
        }
        return new File(movieFolder, name + "." + Registry.getInstance().getExtension(fileFormat));
    }

    public static void startRecording(String methodName) throws Exception {
        File file = new File("./Test Report");

        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration();

        Format fileFormat = new Format(MediaTypeKey, MediaType.FILE, MimeTypeKey, MIME_AVI);
        Format screenFormat = new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                CompressorNameKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE, DepthKey, 24, FrameRateKey,
                Rational.valueOf(15), QualityKey, 1.0f, KeyFrameIntervalKey, 15 * 60);
        Format mouseFormat = new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, "black", FrameRateKey, Rational.valueOf(30));

        screenRecorder = new ScreenRecorderUtil(gc, new Rectangle(0, 0, gc.getBounds().width, gc.getBounds().height),
                fileFormat, screenFormat, mouseFormat, null, file, methodName);

        screenRecorder.start();
    }

    public static void stopRecording() throws Exception {
        screenRecorder.stop();
    }
}
