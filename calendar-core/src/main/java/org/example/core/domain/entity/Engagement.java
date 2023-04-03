package org.example.core.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.core.domain.Event;
import org.example.core.domain.RequestReplyType;
import org.example.core.domain.RequestStatus;
import org.example.core.util.Period;

import javax.persistence.*;
import java.time.LocalDate;

@NoArgsConstructor
@Builder
@AllArgsConstructor
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

    public Schedule getSchedule() {
        return schedule;
    }

    public User getAttendee() {
        return attendee;
    }

    public RequestStatus getStatus() {
        return this.requestStatus;
    }

    public boolean isRequested() {
        return this.requestStatus == RequestStatus.REQUESTED;
    }

    public boolean isOverlapped(LocalDate date) {
        return this.schedule.isOverlapped(date);
    }

    public boolean isOverlapped(Period period) {
        return this.schedule.isOverlapped(period);
    }

    public Engagement updateStatus(RequestReplyType type) {
        switch (type) {
            case ACCEPT -> this.requestStatus = RequestStatus.ACCEPTED;
            case REJECT -> this.requestStatus = RequestStatus.REJECTED;
        }
        return this;
    }

    public RequestStatus getRequestStatus() {
        return this.requestStatus;
    }
}
