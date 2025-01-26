package pl.b4jd1k.airbnb_clone_back.listing.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pl.b4jd1k.airbnb_clone_back.listing.application.dto.sub.PictureDTO;
import pl.b4jd1k.airbnb_clone_back.listing.domain.ListingPicture;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface ListingPictureMapper {

  Set<ListingPicture> pictureDTOToListingPicture(List<PictureDTO> pictureDTOs);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "listing", ignore = true)
  @Mapping(target = "createdDate", ignore = true)
  @Mapping(target = "lastModifiedDate", ignore = true)
  @Mapping(target = "cover", source = "isCover")
  ListingPicture pictureDTOToListingPicture(PictureDTO pictureDTO);

  List<PictureDTO> listingPictureToPictureDTO(List<ListingPicture> listingPictures);

  @Mapping(target = "isCover", source = "cover")
  PictureDTO convertToPictureDTO(ListingPicture listingPicture);

  @Named("extract-cover")
  default PictureDTO extractCover(Set<ListingPicture> pictures) {
    return pictures.stream().findFirst().map(this::convertToPictureDTO).orElseThrow();
  }
}
