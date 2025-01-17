package pl.b4jd1k.airbnb_clone_back.listing.application;

import org.springframework.stereotype.Service;
import pl.b4jd1k.airbnb_clone_back.listing.application.dto.CreateListingDTO;
import pl.b4jd1k.airbnb_clone_back.listing.application.dto.SaveListingDTO;
import pl.b4jd1k.airbnb_clone_back.listing.domain.Listing;
import pl.b4jd1k.airbnb_clone_back.listing.mapper.ListingMapper;
import pl.b4jd1k.airbnb_clone_back.listing.repository.ListingRepository;
import pl.b4jd1k.airbnb_clone_back.user.application.Auth0Service;
import pl.b4jd1k.airbnb_clone_back.user.application.UserService;
import pl.b4jd1k.airbnb_clone_back.user.application.dto.ReadUserDTO;

// zarządzanie ogłoszeniami - tworzenie, przypisywanie zdjęć, ustawia odpowiednią rolę użytkownikowi itp
@Service
public class LandlordService {
  private final ListingRepository listingRepository;
  private final ListingMapper listingMapper;
  private final UserService userService;
  private final Auth0Service auth0Service;
  private final PictureService pictureService;

  public LandlordService(ListingRepository listingRepository, ListingMapper listingMapper,
                         UserService userService, Auth0Service auth0Service, PictureService pictureService) {
    this.listingRepository = listingRepository;
    this.listingMapper = listingMapper;
    this.userService = userService;
    this.auth0Service = auth0Service;
    this.pictureService = pictureService;
  }

  public CreateListingDTO create(SaveListingDTO saveListingDTO) {
    Listing newListing = listingMapper.saveListingDTOToListing(saveListingDTO);
    ReadUserDTO userConnected = userService.getAuthenticatedUserFromSecurityContext();

    newListing.setLandlordPublicId(userConnected.publicId());

    Listing savedListing = listingRepository.saveAndFlush(newListing);

    pictureService.saveAll(saveListingDTO.getPictures(), savedListing);

    auth0Service.addLandlordRoleToUser(userConnected);

    return listingMapper.listingToCreatedListingDTO(savedListing);
  }
}
