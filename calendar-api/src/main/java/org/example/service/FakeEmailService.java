package org.example.service;

import org.example.controller.BatchController;
import org.example.core.domain.entity.Engagement;
import org.example.core.domain.entity.Share;
import org.example.dto.EngagementEmailStuff;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("test")
@Service
public class FakeEmailService implements EmailService {
    @Override
    public void sendEngagement(EngagementEmailStuff stuff) {
        System.out.println("Send Email. email:"
                + stuff.getSubject());
    }

    @Override
    public void sendAlarmMail(BatchController.SendMailBatchRequest req) {
        System.out.println("send alarm." + req.toString());
    }

    @Override
    public void sendShareRequestMail(String email, String email1, Share.Direction direction) {
        System.out.println("send share mail");
    }
}
