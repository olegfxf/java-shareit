package ru.practicum.shareit.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.sun.jdi.InternalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.shareit.abstracts.AbstractServiceImpl;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.messages.HandlerMessages;
import ru.practicum.shareit.messages.LogMessages;
import ru.practicum.shareit.user.dto.UserDtoRes;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService extends AbstractServiceImpl<User, UserRepository> {
    public UserService(UserRepository repository) {
        super(repository);
    }

    UserRepository userRepository;

    @Autowired
    public UserService(UserRepository repository, UserRepository userRepository) {
        super(repository);
        this.userRepository = userRepository;
    }

    public UserDtoRes updateCustomer(Long id, JsonMergePatch patch) {
        try {
            User user = getById(id);
            User userPatched = applyPatchToUser(patch, user);
            log.debug(String.valueOf(LogMessages.TRY_PATCH), userPatched);

            update(userPatched);
            return new UserDtoRes(userPatched);
        } catch (JsonPatchException | JsonProcessingException e) {
            throw new InternalException(String.valueOf(HandlerMessages.SERVER_ERROR));
        } catch (NotFoundException e) {
            throw new NotFoundException(String.valueOf(HandlerMessages.NOT_FOUND));
        }
    }

    private User applyPatchToUser(
            JsonMergePatch patch, User targetCustomer) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(new ObjectMapper().convertValue(targetCustomer, JsonNode.class));
        return new ObjectMapper().treeToValue(patched, User.class);
    }

    public UserDtoRes save1(User user) {
        return new UserDtoRes(userRepository.save(user));
    }

    public UserDtoRes removeById1(Long userId) {
        User user = userRepository.findById(userId).get();
        userRepository.deleteById(userId);
        return new UserDtoRes(user);
    }

    public List<UserDtoRes> getAll1() {
        return userRepository.findAll().stream()
                .map(e -> UserMapper.toUserDtoRes(e)).collect(Collectors.toList());
    }

    public UserDtoRes getById1(Long userId) {
        if (!userRepository.findById(userId).isPresent())
            throw new NotFoundException(String.valueOf(HandlerMessages.NOT_FOUND));

        return UserMapper.toUserDtoRes(userRepository.findById(userId).get());
    }

    public UserDtoRes update1(User user) {
        if (!userRepository.findById(user.getId()).isPresent())
            throw new NotFoundException(String.valueOf(HandlerMessages.NOT_FOUND));

        return UserMapper.toUserDtoRes(userRepository.save(user));
    }

}