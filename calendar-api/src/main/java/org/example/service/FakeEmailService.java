package org.example.service;

import org.example.core.domain.entity.Engagement;
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
}
