package org.example.core.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.core.domain.entity.Schedule;

@NoArgsConstructor
@Getter
public class Event {
    private Schedule schedule;

    public Event(Schedule schedule) {
        this.schedule = schedule;
    }
}
