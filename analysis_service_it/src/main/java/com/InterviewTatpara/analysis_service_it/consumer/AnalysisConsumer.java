package com.InterviewTatpara.analysis_service_it.consumer;


import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.InterviewTatpara.analysis_service_it.dto.VideoMessage;
import com.InterviewTatpara.analysis_service_it.service.AiService;

@Component
public class AnalysisConsumer {

    @Autowired
    private AiService aiService;

    @RabbitListener(queues = "video_analysis_queue")
    public void consumeMessage(VideoMessage message) {
        System.out.println("Received Message: " + message.getSessionId());
        aiService.analyzeVideo(message);
    }
}
