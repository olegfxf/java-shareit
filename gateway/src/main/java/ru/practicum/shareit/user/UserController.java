package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserClient userClient;

    @PostMapping()
    public ResponseEntity<Object> save(@Validated() @RequestBody UserDto userDto) {
        return userClient.createUser(userDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateCustomer(@PathVariable Long userId,
                                                 @RequestBody UserDto userDto) {
        return userClient.updateUser(userDto, userId);
    }

    @GetMapping()
    public ResponseEntity<Object> getAll() {
        return userClient.getUsers();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getById(@NotNull @Min(value = 1) @PathVariable Long userId) {
        return userClient.getUserById(userId);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> removeById(@NotNull @Min(value = 1) @PathVariable Long userId) {
        return userClient.deleteUserById(userId);
    }
}
