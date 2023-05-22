package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoForUser;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceTest {
    @InjectMocks
    ItemRequestService itemRequestService;
    @Mock
    ItemRequestRepository itemRequestRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    ItemRepository itemRepository;
    ItemRequest itemRequest;


    @BeforeEach
    void setUp() {
        itemRequest = ItemRequest.builder()
                .id(1L)
                .description("description")
                .requester(new User())
                .created(LocalDateTime.of(2023, 2, 1, 0, 0))
                .build();
    }

    @Test
    void getRequester() {
        Long userId = 1L;
        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class,
                () -> itemRequestService.getRequester(userId));
    }

    @Test
    void addItemRequest() {
        Long userId = 1L;
        User user = new User();

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        when(itemRequestRepository.save(itemRequest)).thenReturn(itemRequest);

        ItemRequestDto actualItemRequestDto = itemRequestService.addItemRequest(userId, itemRequest);
        ItemRequestDto expectedItemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequest);

        assertEquals(expectedItemRequestDto.toString(), actualItemRequestDto.toString());
    }

    @Test
    void getAllForOwner() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        User requester = user;
        List<Item> items = new ArrayList<>();
        List<ItemRequest> itemRequest = new ArrayList<>();
        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        when(itemRequestRepository.findAllByRequester(any(), any()))
                .thenReturn(itemRequest);

        List<ItemRequestDtoForUser> actualItemRequestDtoForUser =
                itemRequestService.getAllForOwner(userId);

        List<ItemRequestDtoForUser> expectedItemRequestDtoForUser =
                itemRequest.stream().map(e -> ItemRequestMapper.toItemRequestDtoForUser(e, items))
                        .collect(Collectors.toList());

        assertEquals(expectedItemRequestDtoForUser, actualItemRequestDtoForUser);
    }

    @Test
    void getAllForUser() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        User requester = user;
        List<Item> items = new ArrayList<>();
        List<ItemRequest> itemRequest = new ArrayList<>();

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        when(itemRequestRepository.findAllByRequesterIsNot(any(), any()))
                .thenReturn(itemRequest);

        List<ItemRequestDtoForUser> actualItemRequestDtoForUser =
                itemRequestService.getAllForUser(0, 20, userId);

        List<ItemRequestDtoForUser> expectedItemRequestDtoForUser =
                itemRequest.stream().map(e -> ItemRequestMapper.toItemRequestDtoForUser(e, items))
                        .collect(Collectors.toList());

        assertEquals(expectedItemRequestDtoForUser, actualItemRequestDtoForUser);
    }


    @Test
    void getById() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        User requester = user;
        Long itemRequestId = 1L;
        List<Item> items = new ArrayList<>();

        when(itemRequestRepository.findById(userId))
                .thenReturn(Optional.of(itemRequest));
        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        when(itemRequestRepository.findById(1L))
                .thenReturn(Optional.of(itemRequest));
        when(itemRepository.findAllByRequestId(itemRequestId))
                .thenReturn(items);

        ItemRequestDtoForUser actualItemRequestDtoForUser =
                itemRequestService.getById(1L, 1L);
        ItemRequestDtoForUser expectedItemRequestDtoForUser =
                ItemRequestMapper.toItemRequestDtoForUser(itemRequest, items);


        assertEquals(expectedItemRequestDtoForUser.getId(),
                actualItemRequestDtoForUser.getId());
    }
}