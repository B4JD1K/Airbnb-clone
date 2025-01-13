package pl.b4jd1k.airbnb_clone_back.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.b4jd1k.airbnb_clone_back.booking.domain.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}
