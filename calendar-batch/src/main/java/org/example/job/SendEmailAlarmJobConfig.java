package org.example.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.core.domain.entity.Schedule;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class SendEmailAlarmJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    private static final int CHUNK_SIZE = 3;

    @Bean
    public Job sendEmailAlarmJob(
            Step sendScheduleAlarmStep,
            Step sendEngagementAlarmStep
    ) {
        return jobBuilderFactory.get("sendEmailAlarmJob")
                .start(sendScheduleAlarmStep)
                .next(sendEngagementAlarmStep)
                .build();
    }

    @Bean
    public Step sendEngagementAlarmStep(
            ItemReader<SendMailBatchRequest> sendEngagementAlarmReader,
            ItemWriter<SendMailBatchRequest> sendEmailAlarmWriter
    ) {
        return stepBuilderFactory.get("sendEngagementAlarmStep")
                .<SendMailBatchRequest, SendMailBatchRequest>chunk(CHUNK_SIZE)
                .reader(sendEngagementAlarmReader)
                .writer(sendEmailAlarmWriter)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Step sendScheduleAlarmStep(
            ItemReader<SendMailBatchRequest> sendScheduleAlarmReader,
            ItemWriter<SendMailBatchRequest> sendEmailAlarmWriter
    ) {
        return stepBuilderFactory.get("sendScheduleAlarmStep")
                .<SendMailBatchRequest, SendMailBatchRequest>chunk(CHUNK_SIZE)
                .reader(sendScheduleAlarmReader)
                .writer(sendEmailAlarmWriter)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public JdbcCursorItemReader<SendMailBatchRequest> sendEngagementAlarmReader() {
        return new JdbcCursorItemReaderBuilder<SendMailBatchRequest>()
                .name("jdbcCursorItemReader")
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(SendMailBatchRequest.class))
                .sql("select s.id, s.start_at, s.title, u.email as user_email\n" +
                        "from schedules s\n" +
                        "         left join engagements e on s.id = e.schedule_id\n" +
                        "         left join users u on u.id = e.attendee_id\n" +
                        "where s.start_at >= now() + interval 10 minute\n" +
                        "  and s.start_at < now() + interval 11 minute\n" +
                        "  and e.request_status = 'ACCEPTED'")
                .build();
    }

    @Bean
    public JdbcCursorItemReader<SendMailBatchRequest> sendScheduleAlarmReader() {
        return new JdbcCursorItemReaderBuilder<SendMailBatchRequest>()
                .name("jdbcCursorItemReader")
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(SendMailBatchRequest.class))
                .sql("select s.id, s.start_at, s.title, u.email as user_email\n" +
                        "from schedules s\n" +
                        "         left join users u on u.id = s.writer_id\n" +
                        "where s.start_at >= now() + interval 10 minute\n" +
                        "  and s.start_at < now() + interval 11 minute")
                .build();
    }

    @Bean
    public ItemWriter<SendMailBatchRequest> sendEmailAlarmWriter() {
//        return list -> log.info("write items.\n" +
//                list.stream()
//                        .map(s -> s.toString())
//                        .collect(Collectors.joining("\n")));
        return list -> new RestTemplate()
                .postForObject("http://localhost:8080/api/batch/mail", list, Object.class
                );
    }
}
