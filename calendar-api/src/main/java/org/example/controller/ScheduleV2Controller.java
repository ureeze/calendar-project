package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.AuthUser;
import org.example.dto.SharedScheduleDto;
import org.example.service.ScheduleQueryService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/V2/schedules")
@RestController
public class ScheduleV2Controller {

    private final ScheduleQueryService scheduleQueryService;

    @GetMapping("/day")
    public List<SharedScheduleDto> getScheduleByDay(
            AuthUser authUser,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return scheduleQueryService.getSharedScheduleByDay(authUser, date == null ? LocalDate.now() : date);
    }

    @GetMapping("/week")
    public List<SharedScheduleDto> getScheduleByWeek(
            AuthUser authUser,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startOfWeek) {
        return scheduleQueryService.getSharedScheduleByWeek(authUser, startOfWeek == null ? LocalDate.now() : startOfWeek);
    }

    @GetMapping("/month")
    public List<SharedScheduleDto> getScheduleByMonth(
            AuthUser authUser,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM") String yearMonth) {
        return scheduleQueryService.getSharedScheduleByMonth(authUser, yearMonth == null ? YearMonth.now() : YearMonth.parse(yearMonth));
    }
}
