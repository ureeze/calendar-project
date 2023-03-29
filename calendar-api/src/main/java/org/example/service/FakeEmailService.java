package org.example.service;

import org.example.core.domain.entity.Engagement;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("dev")
@Service
public class FakeEmailService implements EmailService {
    @Override
    public void sendEngagement(Engagement engagement) {
        System.out.println("Send Email. email:"
                + engagement.getAttendee().getEmail()
                + ", scheduleId: "
                + engagement.getSchedule().getId());
    }
}
