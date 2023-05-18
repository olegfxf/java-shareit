package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class BookingMapper {
    public static Booking toBooking(BookingDtoReq bookingDtoReq) {
        Item item = new Item();
        item.setId(bookingDtoReq.getItemId());
        User booker = new User();
        booker.setId(bookingDtoReq.getBookerId());
        return Booking.builder()
                .start(bookingDtoReq.getStart())
                .end(bookingDtoReq.getEnd())
                .item(item)
                .booker(booker)
                .build();

    }

    public static BookingDtoRes toBookingDtoRes(Booking booking) {
        return BookingDtoRes.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .booker(booking.getBooker())
                .item(booking.getItem())
                .build();
    }

    public static Booking toBookingFromDtoRes(BookingDtoRes booking) {
        return Booking.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .booker(booking.getBooker())
                .item(booking.getItem())
                .build();
    }


}
