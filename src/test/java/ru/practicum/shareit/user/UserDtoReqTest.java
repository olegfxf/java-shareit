package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.UserDtoReq;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest

public class UserDtoReqTest {
    @Autowired
    JacksonTester<UserDtoReq> json;

    UserDtoReq userDtoReq = UserDtoReq.builder()

            .name("name")
            .email("name@email.ru")
            .build();

    @Test
    void jsonItemDtoReq() throws Exception {
        JsonContent<UserDtoReq> result = json.write(userDtoReq);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(userDtoReq.getName());
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo(userDtoReq.getEmail());
    }
}
