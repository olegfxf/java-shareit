package ru.practicum.shareit.booking.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Setter
@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Component
public class SmallBooking {
    Long id;
    LocalDateTime start;
    LocalDateTime end;
    Long itemId;
    Long bookerId;

    BookingRepository bookingRepository;

    ItemRepository itemRepository;

    @Autowired
    public SmallBooking(BookingRepository bookingRepository,
                        ItemRepository itemRepository) {
        this.bookingRepository = bookingRepository;
        this.itemRepository = itemRepository;
    }

    public Optional<SmallBooking> get(Long itemId, boolean iNext, Long userId) {
        SmallBooking smallBooking = new SmallBooking();
        Booking booking = new Booking();
        Optional<SmallBooking> optionalSmallBooking = null;

        if (iNext)
            booking = bookingRepository.findBookingsNext(itemId, LocalDateTime.now(), userId);
        else
            booking = bookingRepository.findBookingsLast(itemId, LocalDateTime.now(), userId);

        if (booking != null) {
            smallBooking.id = booking.getId();
            smallBooking.start = booking.getStart();
            smallBooking.end = booking.getEnd();
            smallBooking.itemId = booking.getItem().getId();
            smallBooking.bookerId = booking.getBooker().getId();
            optionalSmallBooking = Optional.of(smallBooking);
        }


        return optionalSmallBooking;
    }
}
