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
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.messages.LogMessages;
import ru.practicum.shareit.user.dto.UserDtoReq;
import ru.practicum.shareit.user.dto.UserDtoRes;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


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
    public UserDtoRes removeById(@PathVariable Long userId) {
        log.debug(String.valueOf(LogMessages.TRY_REMOVE_OBJECT), userId);
        return new UserDtoRes(userService.removeById(userId));
    }

    @PatchMapping(path = "/{id}", consumes = "application/json")
    @ResponseBody
    public UserDtoRes updateCustomer(@PathVariable Long id, @RequestBody JsonMergePatch patch) {
//        try {
//            User user = userService.getById(id);
//            User userPatched = applyPatchToUser(patch, user);
//            log.debug(String.valueOf(LogMessages.TRY_PATCH), userPatched);
//
//            userService.update(userPatched);
//            return ResponseEntity.ok(userPatched);
//        } catch (JsonPatchException | JsonProcessingException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        } catch (NotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
        return userService.updateCustomer(id, patch);
    }



//    private User applyPatchToUser(
//            JsonMergePatch patch, User targetCustomer) throws JsonPatchException, JsonProcessingException {
//        JsonNode patched = patch.apply(new ObjectMapper().convertValue(targetCustomer, JsonNode.class));
//        return new ObjectMapper().treeToValue(patched, User.class);
//    }

}
