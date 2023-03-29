package org.example.service;

import org.example.core.domain.entity.Engagement;

public interface EmailService {
    void sendEngagement(Engagement engagement);
}
