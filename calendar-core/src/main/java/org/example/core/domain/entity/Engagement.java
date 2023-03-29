package org.example.core.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.core.domain.Event;
import org.example.core.domain.RequestStatus;
import org.example.core.util.Period;

import javax.persistence.*;
import java.time.LocalDate;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
@Table(name = "engagements")
@Entity
public class Engagement extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @ManyToOne
    @JoinColumn(name = "attendee_id")
    private User attendee;

    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus;

    public Event getEvent() {
        return schedule.toEvent();
    }

    public boolean isOverlapped(LocalDate date) {
        return this.schedule.isOverlapped(date);
    }

    public boolean isOverlapped(Period period) {
        return this.schedule.isOverlapped(period);
    }
}
