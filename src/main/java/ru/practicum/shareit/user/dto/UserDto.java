package ru.practicum.shareit.user.dto;

import lombok.Getter;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
public enum UserDto {
    ;
    private interface Id {
        @Positive Long getId();
    }

    private interface Name {
        @NotBlank String getName();
    }

    private interface Email {
        @NotNull @javax.validation.constraints.Email String getEmail();
    }


    public enum Request {
        ;
        @Value
        public static class UserDtoReq implements Name, UserDto.Email {
            String name;
            String email;
        }
    }

    public enum Response {
        ;
        @Value
        public static class UserDtoRes implements Id, Name, UserDto.Email {
            Long id;
            String name;
            String email;
        }
    }
}
