package pl.b4jd1k.airbnb_clone_back.listing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.b4jd1k.airbnb_clone_back.listing.domain.Listing;

import java.util.List;
import java.util.UUID;

public interface ListingRepository extends JpaRepository<Listing,Long> {

@Query("SELECT listing FROM Listing listing LEFT JOIN FETCH listing.pictures picture" +
        " WHERE listing.landlordPublicId = :landlordPublicId AND picture.isCover = true")
  List<Listing> findAllByLandlordPublicIdFetchCoverPicture(UUID landlordPublicId);

  long deleteByPublicIdAndLandlordPublicId(UUID publicId, UUID landlordPublicId);
}
