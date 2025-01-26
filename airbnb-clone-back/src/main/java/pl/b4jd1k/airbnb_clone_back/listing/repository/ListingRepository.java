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

  @Query("SELECT listing FROM Listing listing LEFT JOIN FETCH listing.pictures picture" +
    " WHERE picture.isCover = true AND listing.bookingCategory = :bookingCategory")
  Page<Listing> findAllByBookingCategoryWithCoverOnly(Pageable pageable, BookingCategory bookingCategory);

  @Query("SELECT listing FROM Listing listing LEFT JOIN FETCH listing.pictures picture" +
    " WHERE picture.isCover = true")
  Page<Listing> findAllWithCoverOnly(Pageable pageable);

  Optional<Listing> findByPublicId(UUID publicId);

  List<Listing> findAllByPublicIdIn(List<UUID> allListingPublicIDs);

  Optional<Listing> findOneByPublicIdAndLandlordPublicId(UUID listingPublicId, UUID landlordPublicId);

  Page<Listing> findAllByLocationAndBathroomsAndBedroomsAndGuestsAndBeds(
    Pageable pageable, String location, int bathrooms, int bedrooms, int guests, int beds);
}
