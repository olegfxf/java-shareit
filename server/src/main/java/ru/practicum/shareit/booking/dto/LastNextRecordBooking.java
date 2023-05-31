package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;
import java.util.Optional;

@Setter
@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Component
public class LastNextRecordBooking {
    Long id;

    @JsonFormat
    @FutureOrPresent
    LocalDateTime start;

    @JsonFormat
    @Future
    LocalDateTime end;

    Long itemId;

    Long bookerId;

    BookingRepository bookingRepository;

    @Autowired
    public LastNextRecordBooking(BookingRepository bookingRepository,
                                 ItemRepository itemRepository) {
        this.bookingRepository = bookingRepository;
    }

    public Optional<LastNextRecordBooking> get(Long itemId, boolean iNext, Long userId) {
        LastNextRecordBooking lastNextRecordBooking = new LastNextRecordBooking();
        Booking booking = new Booking();
        Optional<LastNextRecordBooking> optionalSmallBooking = null;

        if (iNext)
            booking = bookingRepository.findBookingsNext(itemId, LocalDateTime.now(), userId);
        else
            booking = bookingRepository.findBookingsLast(itemId, LocalDateTime.now(), userId);

        if (booking != null) {
            lastNextRecordBooking.id = booking.getId();
            lastNextRecordBooking.start = booking.getStart();
            lastNextRecordBooking.end = booking.getEnd();
            lastNextRecordBooking.itemId = booking.getItem().getId();
            lastNextRecordBooking.bookerId = booking.getBooker().getId();
            optionalSmallBooking = Optional.of(lastNextRecordBooking);
        }

        return optionalSmallBooking;
    }
}
