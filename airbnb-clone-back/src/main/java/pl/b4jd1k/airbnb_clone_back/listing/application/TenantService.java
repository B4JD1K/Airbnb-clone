package pl.b4jd1k.airbnb_clone_back.listing.application;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.b4jd1k.airbnb_clone_back.listing.application.dto.DisplayCardListingDTO;
import pl.b4jd1k.airbnb_clone_back.listing.domain.BookingCategory;
import pl.b4jd1k.airbnb_clone_back.listing.domain.Listing;
import pl.b4jd1k.airbnb_clone_back.listing.mapper.ListingMapper;
import pl.b4jd1k.airbnb_clone_back.listing.repository.ListingRepository;
import pl.b4jd1k.airbnb_clone_back.user.application.UserService;

@Service
public class TenantService {

  private final ListingRepository listingRepository;

  private final ListingMapper listingMapper;

  private final UserService userService;

  public TenantService(ListingRepository listingRepository, ListingMapper listingMapper, UserService userService) {
    this.listingRepository = listingRepository;
    this.listingMapper = listingMapper;
    this.userService = userService;
  }

  public Page<DisplayCardListingDTO> getAllByCategory(Pageable pageable, BookingCategory category) {
    Page<Listing> allOrBookingCategory;
    if (category == = BookingCategory.ALL) {
      allOrBookingCategory = listingRepository.findAllWithCoverOnly(pageable);
    } else {
      allOrBookingCategory = listingRepository.findAllByBookingCategoryWithCoverOnly(pageable,category);
    }

    return allOrBookingCategory.map(listingMapper::listingToDisplayCardListingDTO);
  }
}
