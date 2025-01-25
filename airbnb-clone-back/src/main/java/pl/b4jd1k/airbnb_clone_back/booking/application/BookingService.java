package pl.b4jd1k.airbnb_clone_back.booking.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import pl.b4jd1k.airbnb_clone_back.booking.application.dto.BookedDateDTO;
import pl.b4jd1k.airbnb_clone_back.booking.application.dto.BookedListingDTO;
import pl.b4jd1k.airbnb_clone_back.booking.application.dto.NewBookingDTO;
import pl.b4jd1k.airbnb_clone_back.booking.domain.Booking;
import pl.b4jd1k.airbnb_clone_back.booking.mapper.BookingMapper;
import pl.b4jd1k.airbnb_clone_back.booking.repository.BookingRepository;
import pl.b4jd1k.airbnb_clone_back.infrastructure.config.SecurityUtils;
import pl.b4jd1k.airbnb_clone_back.listing.application.LandlordService;
import pl.b4jd1k.airbnb_clone_back.listing.application.dto.DisplayCardListingDTO;
import pl.b4jd1k.airbnb_clone_back.listing.application.dto.ListingCreateBookingDTO;
import pl.b4jd1k.airbnb_clone_back.listing.application.dto.vo.PriceVO;
import pl.b4jd1k.airbnb_clone_back.sharedkernel.service.State;
import pl.b4jd1k.airbnb_clone_back.user.application.UserService;
import pl.b4jd1k.airbnb_clone_back.user.application.dto.ReadUserDTO;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookingService {

  private final BookingRepository bookingRepository;
  private final BookingMapper bookingMapper;
  private final UserService userService;
  private final LandlordService landlordService;
  private final RestClient.Builder builder;

  public BookingService(BookingRepository bookingRepository, BookingMapper bookingMapper,
                        UserService userService, LandlordService landlordService, RestClient.Builder builder) {
    this.bookingRepository = bookingRepository;
    this.bookingMapper = bookingMapper;
    this.userService = userService;
    this.landlordService = landlordService;
    this.builder = builder;
  }

  @Transactional
  public State<Void, String> create(NewBookingDTO newBookingDTO) {
    Booking booking = bookingMapper.newBookingToBooking(newBookingDTO);

    Optional<ListingCreateBookingDTO> listingOpt = landlordService.getByListingPublicId(newBookingDTO.listingPublicId());

    if (listingOpt.isEmpty()) {
      return State.<Void, String>builder().forError("Landlord public id not found");
    }

    boolean alreadyBooked = bookingRepository.bookingExistsAtInterval(newBookingDTO.startDate(), newBookingDTO.endDate(), newBookingDTO.listingPublicId());

    if (alreadyBooked) {
      return State.<Void, String>builder().forError("One booking already exists");
    }

    ListingCreateBookingDTO listingCreateBookingDTO = listingOpt.get();

    booking.setFkListing(listingCreateBookingDTO.listingPublicId());

    // kto zrobił rezerwację (ofc user, który jest połączony)
    ReadUserDTO connectedUser = userService.getAuthenticatedUserFromSecurityContext();
    booking.setFkTenant(connectedUser.publicId());
    booking.setNumberOfTravelers(1);

    long numberOfNights = ChronoUnit.DAYS.between(booking.getStartDate(), booking.getEndDate());
    booking.setTotalPrice((int) (numberOfNights * listingCreateBookingDTO.price().value()));

    bookingRepository.save(booking);

    return State.<Void, String>builder().forSuccess();
  }

  @Transactional(readOnly = true)
  public List<BookedDateDTO> checkAvailability(UUID publicId) {
    return bookingRepository.findAllByFkListing(publicId)
      .stream().map(bookingMapper::bookingToCheckAvailability).toList();
  }

  @Transactional(readOnly = true)
  public List<BookedListingDTO> getBookedListing() {
    ReadUserDTO connectedUser = userService.getAuthenticatedUserFromSecurityContext();
    List<Booking> allBookings = bookingRepository.findAllByFkTenant(connectedUser.publicId());
    List<UUID> allListingPublicIDs = allBookings.stream().map(Booking::getFkListing).toList();
    List<DisplayCardListingDTO> allListings = landlordService.getCardDisplayByListingPublicId(allListingPublicIDs);
    return mapBookingToBookedListing(allBookings, allListings);
  }

  private List<BookedListingDTO> mapBookingToBookedListing(List<Booking> allBookings, List<DisplayCardListingDTO> allListings) {
    return allBookings.stream().map(booking -> {
        DisplayCardListingDTO displayCardListingDTO = allListings
          .stream()
          .filter(listing -> listing.publicId().equals(booking.getFkListing()))
          .findFirst()
          .orElseThrow();
        BookedDateDTO dates = bookingMapper.bookingToCheckAvailability(booking);

        return new BookedListingDTO(displayCardListingDTO.cover(),
          displayCardListingDTO.location(),
          dates, new PriceVO(booking.getTotalPrice()),
          booking.getPublicId(), displayCardListingDTO.publicId());
      })
      .toList();
  }

  @Transactional
  public State<UUID, String> cancel(UUID bookingPublicId, UUID listingPublicId, boolean byLandlord) {
    ReadUserDTO connectedUser = userService.getAuthenticatedUserFromSecurityContext();
    int deleteSuccess = 0;

    if (SecurityUtils.hasCurrentUserAnyOfAuthorities(SecurityUtils.ROLE_LANDLORD)
      && byLandlord) {
      deleteSuccess = handleDeletionFromLandlord(bookingPublicId, listingPublicId, connectedUser, deleteSuccess);
    } else {
      deleteSuccess = bookingRepository.deleteBookingByPublicIdAndFkListing(connectedUser.publicId(), bookingPublicId);
    }

    bookingRepository.deleteBookingByFkTenantAndPublicId(connectedUser.publicId(), bookingPublicId);

    if (deleteSuccess >= 1) {
      return State.<UUID, String>builder().forSuccess(bookingPublicId);
    } else {
      return State.<UUID, String>builder().forError("Booking not found");
    }
  }

  private int handleDeletionFromLandlord(UUID bookingPublicId, UUID listingPublicId, ReadUserDTO connectedUser, int deleteSuccess) {
    Optional<DisplayCardListingDTO> listingVerificationOpt = landlordService.getByPublicIdAndLandlordPublicId(listingPublicId, connectedUser.publicId());
    if (listingVerificationOpt.isPresent()) {
      deleteSuccess = bookingRepository.deleteBookingByPublicIdAndFkListing(bookingPublicId, listingVerificationOpt.get().publicId());
    }
    return deleteSuccess;
  }

  @Transactional(readOnly = true)
  public List<BookedListingDTO> getBookedListingForLandlord() {
    ReadUserDTO connectedUser = userService.getAuthenticatedUserFromSecurityContext();
    List<DisplayCardListingDTO> allProperties = landlordService.getAllProperties(connectedUser);
    List<UUID> allPropertiesPublicId = allProperties.stream().map(DisplayCardListingDTO::publicId).toList();
    List<Booking> allBookings = bookingRepository.findAllByFkListingIn(allPropertiesPublicId);
    return mapBookingToBookedListing(allBookings, allProperties);
  }
}
