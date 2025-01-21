package pl.b4jd1k.airbnb_clone_back.listing.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.b4jd1k.airbnb_clone_back.listing.application.dto.CreateListingDTO;
import pl.b4jd1k.airbnb_clone_back.listing.application.dto.DisplayCardListingDTO;
import pl.b4jd1k.airbnb_clone_back.listing.application.dto.SaveListingDTO;
import pl.b4jd1k.airbnb_clone_back.listing.application.dto.vo.PriceVO;
import pl.b4jd1k.airbnb_clone_back.listing.domain.Listing;

import java.util.List;

// mapper MapStructa, który generuje kod konwertujący między DTO a encjami
// wskazuje, że ListingMapper może korzystać z innego mappera do mapowania obiektów powiązanych
// czyli ListingPictureMapper może korzystać np. ze zdjęć ListingPicture
@Mapper(componentModel = "spring", uses = {ListingPictureMapper.class})
public interface ListingMapper {

  // określa w jaki sposób mają być mapowane poszczególne wartości
  // ignore = true - ignoruje (nie mapuje go)
  // source - źródło właściwości w obiekcie; z którego obiektu mapujemy dane
  // target - do jakiej właściwości w tym obiekcie mają trafić dane
  @Mapping(target = "landlordPublicId", ignore = true)
  @Mapping(target = "publicId", ignore = true)
  @Mapping(target = "lastModifiedDate", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdDate", ignore = true)
  @Mapping(target = "pictures", ignore = true)
  @Mapping(target = "title", source = "description.title.value")
  @Mapping(target = "description", source = "description.description.value")
  @Mapping(target = "bedrooms", source = "infos.bedrooms.value")
  @Mapping(target = "guests", source = "infos.guests.value")
  @Mapping(target = "bookingCategory", source = "category")
  @Mapping(target = "beds", source = "infos.beds.value")
  @Mapping(target = "bathrooms", source = "infos.baths.value")
  @Mapping(target = "price", source = "price.value")
  // mapuje obiekt SaveListingDTO na obiekt Listing
  Listing saveListingDTOToListing(SaveListingDTO saveListingDTO);

  // mapuje obiekt Listing na obiekt CreateListingDTO
  CreateListingDTO listingToCreatedListingDTO(Listing listing);

  @Mapping(target = "cover", source = "pictures")
  List<DisplayCardListingDTO> listingToDisplayCardListingDTOs(List<Listing> listings);

  @Mapping(target = "cover", source = "pictures", qualifiedByName = "extract-cover")
  DisplayCardListingDTO listingToDisplayCardListingDTO(Listing listing);

  default PriceVO mapPriceToPriceVO(int price){
    return new PriceVO(price);
  }
}
