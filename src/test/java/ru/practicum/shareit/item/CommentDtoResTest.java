package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.CommentDtoRes;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class CommentDtoResTest {
    @Autowired
    JacksonTester<CommentDtoRes> json;

    CommentDtoRes commentDtoRes = CommentDtoRes.builder()
            .id(0L)
            .text("text")
            .authorName("name")
            .created(LocalDateTime.of(2023, 5, 18, 22, 50))
            .build();

    @Test
    void jsonCommentDtoRes() throws IOException {
        JsonContent<CommentDtoRes> result = json.write(commentDtoRes);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(commentDtoRes.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo(commentDtoRes.getText());
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo(commentDtoRes.getAuthorName());
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo("2023-05-18T22:50:00");
    }

}

