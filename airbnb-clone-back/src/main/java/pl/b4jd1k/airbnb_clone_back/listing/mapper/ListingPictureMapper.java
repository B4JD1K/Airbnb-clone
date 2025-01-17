package pl.b4jd1k.airbnb_clone_back.listing.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pl.b4jd1k.airbnb_clone_back.listing.application.dto.sub.PictureDTO;
import pl.b4jd1k.airbnb_clone_back.listing.domain.ListingPicture;

import java.util.List;
import java.util.Set;

// generowany przez MapStruct, służy do konwertowania obiektów ListingPicture na DTO i odwrotnie
@Mapper(componentModel = "spring")
public interface ListingPictureMapper {

  // mapuje listę obiektów zdjęć na zestaw (Set)
  // Set nie pozwala na przechowywanie dwóch (lub więcej) powtarzających się elementów
  Set<ListingPicture> pictureDTOToListingPicture(List<PictureDTO> pictureDTOs);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "listing", ignore = true)
  @Mapping(target = "createdDate", ignore = true)
  @Mapping(target = "lastModifiedDate", ignore = true)
  @Mapping(target = "cover",source = "isCover")
  // przesyłając X zdjęć w formularzu chcę je zamienić na encje (które trafią do bazy)
  ListingPicture pictureDTOToListingPicture(PictureDTO pictureDTO);

  List<PictureDTO> listingPictureToPictureDTO(List<ListingPicture> listingPictures);

  @Mapping(target = "isCover",source = "cover")
  // zdjęcie powiązane z ogłoszeniem w bazie danych, które chcę przekształcić na DTO by wysłać go do klienta lub API
  PictureDTO convertToPictureDTO(ListingPicture listingPicture);

  // pomocnicza metoda - która może zostać użyta w kontekście mapowania
  // by wyodrębnić pierwsze zdjęcie z zestawu ListingPicture i zamienić je na PictureDTO
  // czyli tak jak dodaje 5 zdjęć na ogłoszenie to pierwsze będzie ustawione jako zdjęcie oferty
  @Named("extract-cover")
  default PictureDTO extractCover(Set<ListingPicture> pictures){
    return pictures.stream().findFirst().map(this::convertToPictureDTO).orElseThrow();
  }
}
