package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.UserDtoRes;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class UserDtoResTest {
    @Autowired
    JacksonTester<UserDtoRes> json;

    UserDtoRes userDtoRes = UserDtoRes.builder()
            .id(1L)
            .name("name")
            .email("name@email.ru")
            .build();

    @Test
    void jsonItemDtoReq() throws Exception {
        JsonContent<UserDtoRes> result = json.write(userDtoRes);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(userDtoRes.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(userDtoRes.getName());
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo(userDtoRes.getEmail());
    }
}
