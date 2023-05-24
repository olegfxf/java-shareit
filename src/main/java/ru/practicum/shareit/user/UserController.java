package ru.practicum.shareit.user;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.messages.LogMessages;
import ru.practicum.shareit.user.dto.UserDtoReq;
import ru.practicum.shareit.user.dto.UserDtoRes;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import java.util.List;


@RestController
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping(path = "/users")
public class UserController {
    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseBody
    public UserDtoRes save(@RequestBody @Valid UserDtoReq userDtoReq) {
        User user = userDtoReq.toUser();
        log.debug(String.valueOf(LogMessages.TRY_ADD), user);
        return userService.save1(user);
    }

    @GetMapping
    public List<UserDtoRes> getAll() {
        log.debug(String.valueOf(LogMessages.TRY_GET_ALL), "пользователей");
        return userService.getAll1();
    }

    @GetMapping("/{userId}")
    public UserDtoRes getById(@PathVariable Long userId) {
        log.debug(String.valueOf(LogMessages.TRY_GET_OBJECT), userId);
        return userService.getById1(userId);
    }

    @PutMapping
    public UserDtoRes update(@Valid @RequestBody User user) {
        log.debug(String.valueOf(LogMessages.TRY_UPDATE), user);
        return userService.update1(user);
    }

    @DeleteMapping("/{userId}")
    public UserDtoRes removeById(@PathVariable Long userId) {
        log.debug(String.valueOf(LogMessages.TRY_REMOVE_OBJECT), userId);
        return userService.removeById1(userId);
    }

    @PatchMapping(path = "/{id}", consumes = "application/json")
    @ResponseBody
    public UserDtoRes updateCustomer(@PathVariable Long id,
                                     @RequestBody UserDtoReq userDtoReq) {
        return userService.updateCustomer(id, userDtoReq);
    }


}
