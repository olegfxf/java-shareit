package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.messages.HandlerMessages;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ItemRequestService {
    ItemRequestRepository itemRequestRepository;
    UserRepository userRepository;

    @Autowired
    public ItemRequestService(ItemRequestRepository itemRequestRepository,
                              UserRepository userRepository) {
        this.itemRequestRepository = itemRequestRepository;
        this.userRepository = userRepository;
    }

    public ItemRequestDto addItemRequest(ItemRequest itemRequest, Long id) {
        return ItemRequestMapper.toItemRequestDto(itemRequestRepository.save(itemRequest));
    }

    public List<ItemRequestDto> getAllCustom(String from, String size, Long userId) {
        if (!userRepository.findById(userId).isPresent())
            throw new NotFoundException(String.valueOf(HandlerMessages.NOT_FOUND));
        User requester = userRepository.findById(userId).get();
        return itemRequestRepository.findByRequester(requester).stream()
                .map(e -> ItemRequestMapper.toItemRequestDto(e)).collect(Collectors.toList());
    }

    public ItemRequestDto getById(Long itemId, Long userId) {
        if (!itemRequestRepository.findById(itemId).isPresent())
            throw new NotFoundException(String.valueOf(HandlerMessages.NOT_FOUND));

//        User user = userRepository.findById(userId).get();
        return ItemRequestMapper.toItemRequestDto(itemRequestRepository.findById(itemId).get());
    }
}
