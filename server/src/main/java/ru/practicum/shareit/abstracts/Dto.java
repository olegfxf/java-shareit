package ru.practicum.shareit.abstracts;

import lombok.Getter;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.sql.Timestamp;

@Getter
public enum Dto {
    ;

    private interface Id {
        @Positive Long getId();
    }

    private interface Name {
        @NotBlank String getName();
    }

    private interface Email {
        @NotNull
        @javax.validation.constraints.Email
        String getEmail();
    }

    /**
     * Column(name = "start_date")
     */
    private interface Start {
        @jdk.jfr.Timestamp
        Timestamp getStart();

    }

    ;

    /**
     * @Column(name = "end_date")
     */
    private interface End {
        @jdk.jfr.Timestamp
        Timestamp getEnd();
    }

    /**
     * @Column(name = " item_id")
     */
    private interface ItemId {
        @Positive Long getItemId();
    }

    /**
     * @Column(name = " booker_id")
     */
    private interface BookerId {
        @Positive Long getBookerId();
    }

    /**
     * @Column(name = "status")
     */
    private interface Status {
        Status getStatus();
    }


    public enum Request {
        ;

        @Value
        public static class UserDtoReq implements Name, Dto.Email {
            String name;
            String email;
        }

        @Value
        public static class Booker implements Start, End, ItemId, BookerId {
            Timestamp start;
            Timestamp end;
            Long itemId;
            Long bookerId;
        }
    }

    public enum Response {
        ;

        @Value
        public static class UserDtoRes implements Id, Name, Dto.Email {
            Long id;
            String name;
            String email;
        }

        @Value
        public static class Booker implements Id, Start, End, ItemId, BookerId, Status {
            Long id;
            Timestamp start;
            Timestamp end;
            Long itemId;
            Long bookerId;
            Status status;
        }

    }
}
