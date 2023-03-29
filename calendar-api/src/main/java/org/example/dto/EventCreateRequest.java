package org.example.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class EventCreateRequest {
    @NotBlank
    private final String title;
    private final String description;
    @NotNull
    private final LocalDateTime startAt;
    @NotNull
    private final LocalDateTime endAt;
    private final List<Long> attendeeIds;
}
