package org.example.service;

import org.example.controller.BatchController;
import org.example.core.domain.entity.Engagement;
import org.example.core.domain.entity.Share;
import org.example.dto.EngagementEmailStuff;

public interface EmailService {
    void sendEngagement(EngagementEmailStuff stuff);

    void sendAlarmMail(BatchController.SendMailBatchRequest req);
    void sendShareRequestMail(String email, String email1, Share.Direction direction);

}
