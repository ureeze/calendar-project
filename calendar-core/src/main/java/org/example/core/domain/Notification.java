package org.example.core.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.core.domain.entity.Schedule;

@NoArgsConstructor
@Getter
public class Notification {

    private Schedule schedule;

    @Builder
    public Notification(Schedule schedule) {
        this.schedule = schedule;
    }
}
