package pl.b4jd1k.airbnb_clone_back.booking.mapper;

import org.mapstruct.Mapper;
import org.springframework.context.annotation.Scope;
import pl.b4jd1k.airbnb_clone_back.booking.application.dto.BookedDateDTO;
import pl.b4jd1k.airbnb_clone_back.booking.application.dto.NewBookingDTO;
import pl.b4jd1k.airbnb_clone_back.booking.domain.Booking;

@Mapper(componentModel = "spring")
public interface BookingMapper {

  Booking newBookingToBooking(NewBookingDTO newBookingDTO);

  BookedDateDTO bookingToCheckAvailability(Booking booking);
}
