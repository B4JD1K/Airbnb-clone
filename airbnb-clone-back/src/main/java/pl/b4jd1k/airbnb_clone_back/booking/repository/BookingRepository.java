package pl.b4jd1k.airbnb_clone_back.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.b4jd1k.airbnb_clone_back.booking.domain.Booking;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, Long> {

  @Query("SELECT case WHEN count(booking) > 0 THEN true ELSE false end" +
    " FROM Booking booking WHERE NOT (booking.endDate <= :startDate or booking.startDate >= :endDate)" +
    " AND booking.fkListing = :fkListing")
  boolean bookingExistsAtInterval(OffsetDateTime endDate, OffsetDateTime startDate, UUID fkListing);

  List<Booking> findAllByFkListing(UUID fkListing);

  List<Booking> findAllByFkTenant(UUID fkTenant);

  int deleteBookingByFkTenantAndPublicId(UUID tenantPublicId, UUID bookingPublicId);

  int deleteBookingByPublicIdAndFkListing(UUID bookingPublicId, UUID listingPublicId);

  List<Booking> findAllByFkListingIn(List<UUID> allPropertiesPublicId);

  @Query("SELECT booking FROM Booking booking WHERE" +
          " NOT (booking.endDate <= :startDate or booking.startDate >= :endDate)" +
          " AND booking.fkListing IN :fkListings")
  List<Booking> findAllMatchWithDate(List<UUID> fkListings, OffsetDateTime startDate, OffsetDateTime endDate);
}
