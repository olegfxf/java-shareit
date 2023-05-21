package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.messages.HandlerMessages;
import ru.practicum.shareit.messages.LogMessages;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoForUser;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ItemRequestService {
    int page = 0;
    int size = 20;
    ItemRequestRepository itemRequestRepository;
    UserRepository userRepository;
    ItemRepository itemRepository;

    @Autowired
    public ItemRequestService(ItemRequestRepository itemRequestRepository,
                              ItemRepository itemRepository,
                              UserRepository userRepository) {
        this.itemRequestRepository = itemRequestRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    public User getRequester(Long userId) {
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    public ItemRequestDto addItemRequest(Long userId, ItemRequest itemRequest) {
        User requester = getRequester(userId);
        itemRequest.setRequester(requester);
        itemRequest.setCreated(LocalDateTime.now());

        log.debug(String.valueOf(LogMessages.ADD), "запрос на вещь выполнен");
        return ItemRequestMapper.toItemRequestDto(itemRequestRepository.save(itemRequest));
    }

    public List<ItemRequestDtoForUser> getAllForOwner(Long userId) {
        User requester = getRequester(userId);
        List<ItemRequestDtoForUser> itemRequestDtoPageForUsers = itemRequestRepository.findAllByRequester(requester,
                        PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "created")))
                .stream().map(e -> ItemRequestMapper.toItemRequestDtoForUser(e,
                        itemRepository.findAllByRequestId(e.getId()))).collect(Collectors.toList());

        log.debug(String.valueOf(LogMessages.GET_ALL_USERS), "предоставлен список запросов пользователя с id " + userId);
        return itemRequestDtoPageForUsers;
    }


    public List<ItemRequestDtoForUser> getAllForUser(Integer from, Integer size, Long userId) {
        User requester = getRequester(userId);
        List<ItemRequestDtoForUser> itemRequestDtoPageForUsers = itemRequestRepository.findAllByRequesterIsNot(requester,
                        PageRequest.of(from, size, Sort.by(Sort.Direction.DESC, "created")))
                .stream().map(e -> ItemRequestMapper.toItemRequestDtoForUser(e, itemRepository.findAllByRequestId(e.getId()))).collect(Collectors.toList());
        log.debug(String.valueOf(LogMessages.GET_ALL_USERS), "предоставлен список запросов всех пользоватей");
        return itemRequestDtoPageForUsers;
    }

//    public void getItemRequest(Long itemRequestId) {
//        if (itemRequestRepository.findById(itemRequestId).isEmpty())
//            throw new NotFoundException(String.valueOf(HandlerMessages.NOT_FOUND));
//    }

    public ItemRequestDtoForUser getById(Long itemRequestId, Long userId) {
        if (!itemRequestRepository.findById(itemRequestId).isPresent())
            throw new NotFoundException(String.valueOf(HandlerMessages.NOT_FOUND));

//        getItemRequest(itemRequestId);

        getRequester(userId);

        return ItemRequestMapper.toItemRequestDtoForUser(itemRequestRepository.findById(itemRequestId).get(),
                itemRepository.findAllByRequestId(itemRequestId));
    }
}
