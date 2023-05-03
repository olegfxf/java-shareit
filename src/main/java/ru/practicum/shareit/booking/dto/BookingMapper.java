package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class BookingMapper {
    public static Booking toItem(BookingDtoReq bookingDtoReq) {
        Booking booking = new Booking();
        booking.setStart(bookingDtoReq.getStart());
        booking.setEnd(bookingDtoReq.getEnd());
        Item item = new Item();
        item.setId(bookingDtoReq.getItemId());
        booking.setItem(item);
        User booker = new User();
        booker.setId(bookingDtoReq.getBookerId());
        booking.setBooker(booker);
        booking.setStatus(bookingDtoReq.getStatus());
        return booking;
    }

    public static BookingDtoRes toBookingDtoRes(Booking booking) {
        BookingDtoRes bookingDtoRes = new BookingDtoRes();
        bookingDtoRes.setId(booking.getId());
        bookingDtoRes.setStart(booking.getStart());
        bookingDtoRes.setEnd(booking.getEnd());
        bookingDtoRes.setItem(booking.getItem());
        bookingDtoRes.setBooker(booking.getBooker());
        bookingDtoRes.setStatus(booking.getStatus());
        return bookingDtoRes;
    }
}
