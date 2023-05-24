package ru.practicum.shareit.booking.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.abstracts.AbstractModel;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */

@Setter
@Getter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "bookings")
public class Booking extends AbstractModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat
    @Column(name = "start_date")
    LocalDateTime start;

    @JsonFormat
    @Column(name = "end_date")
    LocalDateTime end;

    @ManyToOne
    @JoinColumn(name = " item_id")
    Item item;

    @ManyToOne
    @JoinColumn(name = " booker_id")
    User booker;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    Status status;

}
