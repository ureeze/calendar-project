package org.example.core.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.core.domain.entity.Schedule;
import org.example.core.domain.entity.User;

@NoArgsConstructor
public class Notification {

    private Schedule schedule;

    @Builder
    public Notification(Schedule schedule) {
        this.schedule = schedule;
    }

    public User getWriter() {
        return this.schedule.getWriter();
    }
}
