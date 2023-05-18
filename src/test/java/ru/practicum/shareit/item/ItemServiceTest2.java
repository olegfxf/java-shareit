package ru.practicum.shareit.item;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.H2TestProfileJPAConfig;
import ru.practicum.shareit.ShareItApp;
import ru.practicum.shareit.item.dto.ItemDtoReq;
import ru.practicum.shareit.item.dto.ItemDtoRes;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

//@SpringBootTest
@SpringBootTest(classes = {
        ShareItApp.class,
        H2TestProfileJPAConfig.class})
@ActiveProfiles("test")
@Transactional
//@TestPropertySource(properties = { "db.name=test"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)

public class ItemServiceTest2 {

    private final ItemService itemService;

    private final ItemRepository itemRepository;

    // private final ItemRequestRepository;

    private final UserService userService;

    private final ObjectMapper mapper;


    long userId;
    User user = new User();
    ItemRequest itemRequest;

    ItemDtoReq itemDtoReq;

    @BeforeEach
    public void setUp() {
        userId = 1l;
        user.setId(userId);
        user.setName("name");
        user.setEmail("name@mail.ru");
        //  User user1 = new User(1,"name1", "name1@mail.ru");

        long itemRequestId = 1L;
        itemRequest = ItemRequest.builder()
                .id(itemRequestId)
                .description("description")
                .requester(user)
                .created(LocalDateTime.of(2023, 2, 1, 0, 0))
                .build();

        itemDtoReq = ItemDtoReq.builder()
                .name("name")
                .description("description")
                .available(true)
                .ownerId(1L)
                .requestId(1L)
                .build();


    }

    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    @Test
    void addItem() {
        userService.save(user);
        Item item = new Item();
        item.setName("name1");
        item.setDescription("description2");
        itemRepository.save(item);
        ItemDtoReq expectedItem = itemDtoReq;
        ItemDtoRes actualItem = itemService.addItem(itemDtoReq, userId);

        assertEquals(expectedItem.getName(), actualItem.getName());
    }


    @Test
    void updateItem() throws JsonProcessingException, JsonPatchException {
//        userService.save(user);
//        Item item = new Item();
//        item.setName("name1");
//        item.setOwner(user);
//        item.setDescription("description2");
//        itemRepository.save(item);
//
//
//        String jsonString1 = mapper.writeValueAsString(user);
//        JsonNode source = mapper.readTree(jsonString1);

//        user.setEmail("name1@mail.ru");
//
//        String jsonString2 = mapper.writeValueAsString(user);
//        JsonNode target = mapper.readTree(jsonString2);
//
//        JsonNode patch = JsonDiff.asJson(source, target);
//
//        itemService.updateItem(userId, JsonMergePatch.fromJson(patch), userId);


//
//        JsonValue jsonSource = Json.createValue(jsonString1);
//        System.out.println("jsonString = " + jsonString1);
//
//        user.setEmail("name1@mail.ru");
//        String jsonTarget = mapper.writeValueAsString(user);
//        JsonValue jsonTargetV = Json.createValue(jsonTarget);
//        javax.json.JsonMergePatch jsonMergePatchST = Json.createMergeDiff(jsonSource, jsonTargetV);
//        JsonValue jsonResult = jsonMergePatchST.apply(jsonSource);
//        System.out.println("jsonResult =  " + jsonResult);
//
//
//        itemService.updateItem(userId, jsonMergePatchST, userId);


//        JsonValue s1 = Json.createValue(jsonString);
//        System.out.println("s1 = " + s1);
//        JsonValue t1 = Json.createValue("{\"email\":\"name1@mail.ru\"}");
//        JsonMergePatch jsonMergePatch1 = Json.createMergeDiff(s1, t1);
//        System.out.println("jsonMergePatch1 = " + jsonMergePatch1);
//        JsonValue jsonValue1 = jsonMergePatch1.apply(s1);
//        System.out.println("jsonValue1 = " + jsonValue1);


//        JsonValue source = Json.createValue("{\"colour\":\"blue\"}");
//        JsonValue target = Json.createValue("{\"colour\":\"red\"}");
//        JsonMergePatch jsonMergePatch = Json.createMergeDiff(source, target);
//        JsonValue jsonValue = jsonMergePatch.apply(source);
//        System.out.println(jsonValue);

//        List<Item> expectedItems = new ArrayList<>();
//        when(itemRepository.save(any()))
//                .thenReturn(expectedItems);
    }

    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    @Test
    void getAllByUserId() {
        userService.save(user);
        Item item = new Item();
        item.setName("name1");
        item.setOwner(user);
        item.setDescription("description2");
        itemRepository.save(item);

        List<ItemDtoRes> items = itemService.getAllByUserId(userId);
        assertEquals(1, items.size());
    }

//    @AfterEach
//    void qqq()
//    {
//        itemService.removeALL();
//        itemRepository.deleteAll();
//        userService.removeALL();
//
//    }


}
