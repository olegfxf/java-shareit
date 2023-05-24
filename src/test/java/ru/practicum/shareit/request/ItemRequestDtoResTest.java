package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.request.dto.ItemForRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoForUser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemRequestDtoResTest {
    @Autowired
    JacksonTester<ItemRequestDtoForUser> json;
    ItemForRequestDto itemForRequestDto = ItemForRequestDto.builder()
            .id(1L)
            .name("nameItemForRequest")
            .description("descriptionItemForRequest")
            .available(Boolean.TRUE)
            .requestId(1L)
            .build();


    @Test
    void jsonItemDtoReq() throws Exception {
        List<ItemForRequestDto> itemForRequestDtos = new ArrayList<>();
        itemForRequestDtos.add(itemForRequestDto);

        ItemRequestDtoForUser itemRequestDtoForUser = ItemRequestDtoForUser.builder()
                .id(1L)
                .description("description")
                .created(LocalDateTime.of(2023, 5, 18, 22, 50))
                .items(itemForRequestDtos)
                .build();

        JsonContent<ItemRequestDtoForUser> result = json.write(itemRequestDtoForUser);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(itemRequestDtoForUser.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(itemRequestDtoForUser.getDescription());
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo("2023-05-18T22:50:00");
        assertThat(result).extractingJsonPathStringValue("$.items[0].name").isEqualTo(itemRequestDtoForUser.getItems().get(0).getName());
    }
}
