package org.example.service;

import org.example.core.domain.entity.Engagement;
import org.example.dto.EngagementEmailStuff;

public interface EmailService {
    void sendEngagement(EngagementEmailStuff stuff);
}
