package pl.b4jd1k.airbnb_clone_back.listing.mapper;

import org.mapstruct.Mapper;

// mapper MapStructa, który generuje kod konwertujący między DTO a encjami
// wskazuje, że ListingMapper może korzystać z innego mappera do mapowania obiektów powiązanych
// czyli ListingPictureMapper może korzystać np. ze zdjęć ListingPicture
@Mapper(componentModel = "spring", uses = {ListingPictureMapper.class})
public interface ListingMapper {
}
