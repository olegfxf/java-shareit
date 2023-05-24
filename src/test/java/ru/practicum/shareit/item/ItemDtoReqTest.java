package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDtoReq;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemDtoReqTest {
    @Autowired
    JacksonTester<ItemDtoReq> json;

    ItemDtoReq itemDtoReq = ItemDtoReq.builder()
            .name("name")
            .description("description")
            .available(true)
            .ownerId(1L)
            .build();

    @Test
    void jsonItemDtoReq() throws Exception {
        JsonContent<ItemDtoReq> result = json.write(itemDtoReq);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(itemDtoReq.getName());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(itemDtoReq.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(itemDtoReq.getAvailable());
        assertThat(result).extractingJsonPathNumberValue("$.ownerId").isEqualTo(itemDtoReq.getOwnerId().intValue());
    }
}