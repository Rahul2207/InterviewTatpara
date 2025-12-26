package com.InterviewTatpara.analysis_service_it.util;

import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@Service
public class TranscriptionService {

	public String transcribeAudio(String audioPath) {
	    System.out.println("--- Starting FAST Transcription (C++) for: " + audioPath + " ---");
	    
	    // 1. Point to where you saved the .bin model in Step 2
	    String modelPath = System.getProperty("user.home") + "/whisper-models/ggml-base.bin";
	    
	    // 2. Find where brew installed whisper-cpp
	    // On Apple Silicon, it is usually here:
	    String whisperCommand = "/opt/homebrew/bin/whisper-cli";

	    try {
	        // COMMAND: whisper-cpp -m /path/to/model -f /path/to/audio --output-txt
	        ProcessBuilder builder = new ProcessBuilder(
	            whisperCommand,
	            "-m", modelPath,       // Path to the .bin model
	            "-f", audioPath,       // The audio file
	            "--output-txt",        // Output a text file
	            "--no-timestamps"      // Clean text only
	        );

	        // redirectErrorStream(true) merges standard output and error output
	        // whisper-cpp prints progress to stderr, so we might want to capture that.
	        builder.redirectErrorStream(true);
	        Process process = builder.start();
	        
	        // Wait for it to finish
	        int exitCode = process.waitFor();

	        if (exitCode != 0) {
	            String errorLog = new String(process.getInputStream().readAllBytes());
	            System.err.println("Whisper-CPP Error: " + errorLog);
	            throw new RuntimeException("Transcription failed");
	        }

	        // whisper-cpp appends ".txt" to the audio filename automatically.
	        // If input is "video.wav", output is "video.wav.txt"
	        String textFilePath = audioPath + ".txt";
	        
	        return readTextFile(textFilePath);

	    } catch (Exception e) {
	        e.printStackTrace();
	        return "Error: Could not transcribe audio.";
	    }
	}
    

    private String readTextFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append(" ");
            }
        }
        System.out.println("Transcription Result: " + content.toString().trim());
        return content.toString().trim();
    }
}