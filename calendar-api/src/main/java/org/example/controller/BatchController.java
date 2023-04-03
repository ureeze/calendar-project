package org.example.controller;

import lombok.*;
import org.example.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class BatchController {
    private final EmailService emailService;

    @PostMapping("/api/batch/mail")
    public ResponseEntity<Void> sendMail(@RequestBody List<SendMailBatchRequest> request) {
        request.forEach(emailService::sendAlarmMail);
        return ResponseEntity.ok().build();

    }

    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    static public class SendMailBatchRequest {
        private Long id;
        private LocalDateTime startAt;
        private String title;
        private String userEmail;
    }

}
