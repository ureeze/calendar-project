package org.example.job;

import lombok.*;

import java.time.LocalDateTime;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SendMailBatchRequest {
    private Long id;
    private LocalDateTime startAt;
    private String title;
    private String userEmail;
}
