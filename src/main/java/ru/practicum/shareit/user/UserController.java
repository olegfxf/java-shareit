package ru.practicum.shareit.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.ConflictException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.messages.HandlerMessages;
import ru.practicum.shareit.messages.LogMessages;
import ru.practicum.shareit.user.dto.UserDtoReq;
import ru.practicum.shareit.user.dto.UserDtoRes;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping(path = "/users")
public class UserController {
    UserService userService;
    UserStorage userStorage;
    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public UserController(UserService userService, UserDLAStorage userDLAStorage) {
        this.userService = userService;
        this.userStorage = userDLAStorage;
    }

    @PostMapping
    @ResponseBody
    public UserDtoRes save(@Valid @RequestBody UserDtoReq userDtoReq) {
        User user = userDtoReq.toUser();//.modelMapper.map(userDtoReq, User.class);
        log.debug(String.valueOf(LogMessages.TRY_ADD), user);
        return new UserDtoRes(userService.save(user));
    }

    @GetMapping
    public List<UserDtoRes> getAll() {
        log.debug(String.valueOf(LogMessages.TRY_GET_ALL), "пользователей");
        return userService.getAll().stream().map(e -> new UserDtoRes(e)).collect(Collectors.toList());
    }

    @GetMapping("/{userId}")
    public UserDtoRes getById(@PathVariable Long userId) {
        log.debug(String.valueOf(LogMessages.TRY_GET_OBJECT), userId);
        return new UserDtoRes(userService.getById(userId));
    }


    @PutMapping
    public UserDtoRes update(@Valid @RequestBody User user) {
        log.debug(String.valueOf(LogMessages.TRY_UPDATE), user);
        return new UserDtoRes(userService.update(user));
    }


    @DeleteMapping("/{userId}")
    public User removeById(@PathVariable Long userId) {
        log.debug(String.valueOf(LogMessages.TRY_REMOVE), userId);
        return userService.removeById(userId);
    }

    @PatchMapping(path = "/{id}", consumes = "application/json")
    public ResponseEntity<User> updateCustomer(@PathVariable Long id, @RequestBody JsonMergePatch patch) {
        try {
            User user = userStorage.getById(id);
            User userPatched = applyPatchToUser(patch, user);
            log.debug(String.valueOf(LogMessages.TRY_PATCH), userPatched );

//            userDao.getALL().stream().forEach(e-> System.out.println(e));
//            System.out.println("++++++++++++++++++++++++++++++++++++++");
//            System.out.println(user + "   user");
//            System.out.println((patch + "   patch"));
//            System.out.println(userPatched + "   userPatched");

            if (user.getName().equals(userPatched.getName()) && !user.getEmail().equals(userPatched.getEmail()))
                userStorage.getALL().stream().forEach(e -> {
                    if (userPatched.getEmail().equals(e.getEmail()))
                        throw new ConflictException(String.valueOf(HandlerMessages.CONFLICT));
                });

            userService.update(userPatched);
            return ResponseEntity.ok(userPatched);
        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    private User applyPatchToUser(
            JsonMergePatch patch, User targetCustomer) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(new ObjectMapper().convertValue(targetCustomer, JsonNode.class));
        return new ObjectMapper().treeToValue(patched, User.class);
    }

}
