package com.InterviewTatpara.analysis_service_it.util;

import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class AudioExtractor {

	public String extractAudio(String videoPath) throws IOException, InterruptedException {
	    String audioPath = videoPath.replace(".mp4", ".wav");
	    String ffmpegPath = "/opt/homebrew/bin/ffmpeg";

	    ProcessBuilder builder = new ProcessBuilder(
	        ffmpegPath,
	        "-i", videoPath,
	        "-vn",
	        "-acodec", "pcm_s16le",
	        "-ar", "44100",
	        "-ac", "2",
	        "-y",
	        audioPath
	    );

	    // Capture the error output to read why it failed
	    builder.redirectErrorStream(true);
	    Process process = builder.start();
	    
	    // Read the output logs
	    String output = new String(process.getInputStream().readAllBytes());
	    int exitCode = process.waitFor();

	    if (exitCode != 0) {
	        // Check if the failure was because there was no audio
	        if (output.contains("does not contain any stream")) {
	            System.out.println("Warning: Video has no audio. Skipping extraction.");
	            return null; // Return null to signal "No Audio"
	        }
	        // Real crash
	        System.err.println("FFmpeg Error Output: " + output);
	        throw new RuntimeException("FFmpeg failed with exit code " + exitCode);
	    }

	    System.out.println("Audio extracted: " + audioPath);
	    return audioPath;
	}
}
