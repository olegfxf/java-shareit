package ru.practicum.shareit.user.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.abstracts.AbstractModel;

import javax.persistence.*;

@Setter
@Getter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "users")
public class User extends AbstractModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    String name;

    String email;
}
