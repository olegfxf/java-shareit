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
        return userClient.save(userDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateCustomer(@PathVariable Long userId,
                                                 @RequestBody UserDto userDto) {
        return userClient.updateCustomer(userDto, userId);
    }

    @GetMapping()
    public ResponseEntity<Object> getAll() {
        return userClient.getAll();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getById(@NotNull @Min(value = 1) @PathVariable Long userId) {
        return userClient.getById(userId);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> removeById(@NotNull @Min(value = 1) @PathVariable Long userId) {
        return userClient.removeById(userId);
    }
}
