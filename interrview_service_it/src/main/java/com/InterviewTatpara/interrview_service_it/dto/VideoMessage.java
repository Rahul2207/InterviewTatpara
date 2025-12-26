package com.InterviewTatpara.interrview_service_it.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoMessage {
	private String sessionId;
    private String videoPath;
    private String email;
    private String question;
    private String category;
}
