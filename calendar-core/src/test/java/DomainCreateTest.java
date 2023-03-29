import org.example.core.domain.ScheduleType;
import org.example.core.domain.entity.Schedule;
import org.example.core.domain.entity.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DomainCreateTest {

    @Test
    void eventCreate() {
        final User me = User.builder()
                .name("name")
                .email("email")
                .password("pw")
                .birthday(LocalDate.now())
                .build();
        final Schedule taskSchedule = Schedule.task("할일", "청소하기", LocalDateTime.now(), me);

        assertThat(taskSchedule.getScheduleType()).isEqualTo(ScheduleType.TASK);
        assertThat(taskSchedule.toTask().getTitle()).isEqualTo("할일");
    }
}
