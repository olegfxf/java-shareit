package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.CommentDtoReq;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class CommentDtoReqTest {
    @Autowired
    JacksonTester<CommentDtoReq> json;

    CommentDtoReq commentDtoReq = CommentDtoReq.builder()
            .text("name")
            .build();

    @Test
    void jsonItemDtoReq() throws Exception {
        JsonContent<CommentDtoReq> result = json.write(commentDtoReq);
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo(commentDtoReq.getText());
    }
}
