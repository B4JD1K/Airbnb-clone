package pl.b4jd1k.airbnb_clone_back.listing.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.b4jd1k.airbnb_clone_back.listing.domain.BookingCategory;
import pl.b4jd1k.airbnb_clone_back.listing.domain.Listing;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ListingRepository extends JpaRepository<Listing, Long> {

  @Query("SELECT listing FROM Listing listing LEFT JOIN FETCH listing.pictures picture" +
    " WHERE listing.landlordPublicId = :landlordPublicId AND picture.isCover = true")
  List<Listing> findAllByLandlordPublicIdFetchCoverPicture(UUID landlordPublicId);

  long deleteByPublicIdAndLandlordPublicId(UUID publicId, UUID landlordPublicId);

  // pageable - poniewaz nie chcemy aby wszystkie rekordy z bazy sie ladowaly bo byloby to za duzo danych
  @Query("SELECT listing FROM Listing listing LEFT JOIN FETCH listing.pictures picture" +
    " WHERE picture.isCover = true AND listing.bookingCategory = :bookingCategory")
  Page<Listing> findAllByBookingCategoryWithCoverOnly(Pageable pageable, BookingCategory bookingCategory);

  @Query("SELECT listing FROM Listing listing LEFT JOIN FETCH listing.pictures picture" +
    " WHERE picture.isCover = true")
  Page<Listing> findAllWithCoverOnly(Pageable pageable);

  // ładowanie tylko jednego ogłoszenia
  Optional<Listing> findByPublicId(UUID publicId);

  List<Listing> findAllByPublicIdIn(List<UUID> allListingPublicIDs);
}
