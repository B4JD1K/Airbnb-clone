package pl.b4jd1k.airbnb_clone_back.listing.application;

import org.springframework.stereotype.Service;
import pl.b4jd1k.airbnb_clone_back.listing.application.dto.sub.PictureDTO;
import pl.b4jd1k.airbnb_clone_back.listing.domain.Listing;
import pl.b4jd1k.airbnb_clone_back.listing.domain.ListingPicture;
import pl.b4jd1k.airbnb_clone_back.listing.mapper.ListingPictureMapper;
import pl.b4jd1k.airbnb_clone_back.listing.repository.ListingPictureRepository;

import java.util.List;
import java.util.Set;

@Service
public class PictureService {
  private final ListingPictureRepository listingPictureRepository;
  private final ListingPictureMapper listingPictureMapper;

  public PictureService(ListingPictureRepository listingPictureRepository, ListingPictureMapper listingPictureMapper) {
    this.listingPictureRepository = listingPictureRepository;
    this.listingPictureMapper = listingPictureMapper;
  }

  public List<PictureDTO> saveAll(List<PictureDTO> pictures, Listing listing) {
    Set<ListingPicture> listingPictures = listingPictureMapper.pictureDTOToListingPicture(pictures);

    boolean isFirst = true;
    for (ListingPicture listingPicture : listingPictures) {
      listingPicture.setCover(isFirst);
      listingPicture.setListing(listing);
      isFirst = false;
    }

    listingPictureRepository.saveAll(listingPictures);
    return listingPictureMapper.listingPictureToPictureDTO(listingPictures.stream().toList());
  }
}
