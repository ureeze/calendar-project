package org.example.job;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.core.domain.entity.Schedule;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
@Slf4j
@RequiredArgsConstructor
@Configuration
public class AllReadJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    private static final int CHUNK_SIZE = 4;
    private static final int FETCH_SIZE = 4;

    @Bean
    public Job allReadJob(
            Step allReadStep
    ) {
        return jobBuilderFactory.get("allReadJob")
                .incrementer(new RunIdIncrementer())
                .start(allReadStep)
                .build();
    }

    @Bean
    public Step allReadStep(
            ItemReader<Schedule> allReadPagingReader,
            ItemWriter<Schedule> allReadWriter
    ) {
        return stepBuilderFactory.get("allReadStep")
                .<Schedule, Schedule>chunk(CHUNK_SIZE)
                .reader(allReadPagingReader)
//                .reader(allReadCursorReader())
                .writer(allReadWriter)
                .allowStartIfComplete(true) // 재시작 여부 설정
                .build();
    }

    @Bean
    public JdbcCursorItemReader<Schedule> allReadCursorReader() {
        return new JdbcCursorItemReaderBuilder<Schedule>()
                .verifyCursorPosition(false)
                .fetchSize(FETCH_SIZE)
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(Schedule.class))
                .sql("select * from schedules order by id")
                .name("jdbcCursorItemReader")
                .build();
    }

    @Bean
    public JdbcPagingItemReader<Schedule> allReadPagingReader(
            PagingQueryProvider queryProvider) {
        return new JdbcPagingItemReaderBuilder<Schedule>()
                .pageSize(CHUNK_SIZE)
                .fetchSize(FETCH_SIZE)
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(Schedule.class)) //  DB 에서 불러온 값을 객체에 전달해주기 위한 mapper 설정
                .queryProvider(queryProvider)   // 쿼리문을 만들어 전달
                .name("jdbcCursorItemReader")
                .build();
    }

    @Bean
    public PagingQueryProvider queryProvider() throws Exception {
        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setDataSource(dataSource);
        queryProvider.setSelectClause("*");
        queryProvider.setFromClause("from schedules");

        Map<String, Order> sortKeys = new HashMap<>(1);
        sortKeys.put("id", Order.ASCENDING);

        queryProvider.setSortKeys(sortKeys);
        return queryProvider.getObject();
    }

    @Bean
    public ItemWriter<Schedule> allReadWriter() {
        return list -> log.info("write items.\n" +
                list.stream()
                        .map(s -> s.toString())
                        .collect(Collectors.joining("\n")));
    }
}