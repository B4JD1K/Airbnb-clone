package pl.b4jd1k.airbnb_clone_back.listing.application;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.b4jd1k.airbnb_clone_back.booking.application.BookingService;
import pl.b4jd1k.airbnb_clone_back.listing.application.dto.DisplayCardListingDTO;
import pl.b4jd1k.airbnb_clone_back.listing.application.dto.DisplayListingDTO;
import pl.b4jd1k.airbnb_clone_back.listing.application.dto.SearchDTO;
import pl.b4jd1k.airbnb_clone_back.listing.application.dto.sub.LandlordListingDTO;
import pl.b4jd1k.airbnb_clone_back.listing.domain.BookingCategory;
import pl.b4jd1k.airbnb_clone_back.listing.domain.Listing;
import pl.b4jd1k.airbnb_clone_back.listing.mapper.ListingMapper;
import pl.b4jd1k.airbnb_clone_back.listing.repository.ListingRepository;
import pl.b4jd1k.airbnb_clone_back.sharedkernel.service.State;
import pl.b4jd1k.airbnb_clone_back.user.application.UserService;
import pl.b4jd1k.airbnb_clone_back.user.application.dto.ReadUserDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TenantService {

  private final ListingRepository listingRepository;

  private final ListingMapper listingMapper;

  private final UserService userService;
  private final BookingService bookingService;

  public TenantService(ListingRepository listingRepository, ListingMapper listingMapper, UserService userService, BookingService bookingService) {
    this.listingRepository = listingRepository;
    this.listingMapper = listingMapper;
    this.userService = userService;
    this.bookingService = bookingService;
  }

  public Page<DisplayCardListingDTO> getAllByCategory(Pageable pageable, BookingCategory category) {
    Page<Listing> allOrBookingCategory;
    if (category == BookingCategory.ALL) {
      allOrBookingCategory = listingRepository.findAllWithCoverOnly(pageable);
    } else {
      allOrBookingCategory = listingRepository.findAllByBookingCategoryWithCoverOnly(pageable, category);
    }

    return allOrBookingCategory.map(listingMapper::listingToDisplayCardListingDTO);
  }

  @Transactional(readOnly = true)
  public State<DisplayListingDTO, String> getOne(UUID publicId) {
    Optional<Listing> listingByPublicIdOpt = listingRepository.findByPublicId(publicId);

    if (listingByPublicIdOpt.isEmpty()) {
      return State.<DisplayListingDTO, String>builder()
        .forError(String.format("Listing doesn't exist for publicId: %s", publicId));
    }

    DisplayListingDTO displayListingDTO = listingMapper.listingToDisplayListingDTO(listingByPublicIdOpt.get());

    ReadUserDTO readUserDTO = userService.getByPublicId(listingByPublicIdOpt.get().getLandlordPublicId()).orElseThrow();
    LandlordListingDTO landlordListingDTO = new LandlordListingDTO(readUserDTO.firstName(), readUserDTO.imageUrl());
    displayListingDTO.setLandlord(landlordListingDTO);

    return State.<DisplayListingDTO, String>builder().forSuccess(displayListingDTO);
  }

  @Transactional(readOnly = true)
  public Page<DisplayCardListingDTO> search(Pageable pageable, SearchDTO newSearch) {

    Page<Listing> allMatchedListings = listingRepository.findAllByLocationAndBathroomsAndBedroomsAndGuestsAndBeds(
      pageable, newSearch.location(),
      newSearch.infos().baths().value(),
      newSearch.infos().bedrooms().value(),
      newSearch.infos().guests().value(),
      newSearch.infos().beds().value());

    List<UUID> listingsUUIDs = listingRepository.saveAll(allMatchedListings).stream().map(Listing::getPublicId).toList();

    List<UUID> bookingUUIDs = bookingService.getBookingMatchByListingIdsAndBookedDate(listingsUUIDs, newSearch.dates());

    List<DisplayCardListingDTO> listingsNotBooked = allMatchedListings.stream().filter(listing -> !bookingUUIDs.contains(listing.getPublicId()))
      .map(listingMapper::listingToDisplayCardListingDTO)
      .toList();

    return new PageImpl<>(listingsNotBooked, pageable, listingsNotBooked.size());
  }
}
