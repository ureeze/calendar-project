package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.core.domain.entity.repository.EngagementRepository;
import org.example.core.domain.entity.repository.ScheduleRepository;
import org.example.dto.AuthUser;
import org.example.dto.ScheduleDto;
import org.example.util.DtoConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScheduleQueryService {
    private final ScheduleRepository scheduleRepository;
    private final EngagementRepository engagementRepository;

    public List<ScheduleDto> getScheduleByDay(AuthUser authUser, LocalDate date) {
        final Stream<ScheduleDto> schedules =
                scheduleRepository.findAllByWriter_Id(authUser.getId())
                        .stream()
                        .filter(schedule -> schedule.isOverlapped(date))
                        .map(schedule -> DtoConverter.fromSchedule(schedule));
        final Stream<ScheduleDto> engagements = engagementRepository.findAllByAttendee_Id(authUser.getId())
                .stream()
                .filter(engagement -> engagement.isOverlapped(date))
                .map(engagement -> DtoConverter.fromSchedule(engagement.getSchedule()));
        List<ScheduleDto> result = new ArrayList<>();
        result = Stream.concat(schedules, engagements).collect(Collectors.toList());
        return result;
    }
}
