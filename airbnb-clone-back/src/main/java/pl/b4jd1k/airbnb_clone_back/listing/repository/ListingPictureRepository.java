package pl.b4jd1k.airbnb_clone_back.listing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.b4jd1k.airbnb_clone_back.listing.domain.Listing;
import pl.b4jd1k.airbnb_clone_back.listing.domain.ListingPicture;

public interface ListingPictureRepository extends JpaRepository<ListingPicture,Long> {
}
