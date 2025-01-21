package pl.b4jd1k.airbnb_clone_back.listing.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.mapstruct.Mapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import pl.b4jd1k.airbnb_clone_back.listing.application.LandlordService;
import pl.b4jd1k.airbnb_clone_back.listing.application.dto.CreateListingDTO;
import pl.b4jd1k.airbnb_clone_back.listing.application.dto.SaveListingDTO;
import pl.b4jd1k.airbnb_clone_back.listing.application.dto.sub.PictureDTO;
import pl.b4jd1k.airbnb_clone_back.user.application.UserException;
import pl.b4jd1k.airbnb_clone_back.user.application.UserService;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

// zbieranie danych i zdjęć ogłoszenia od użytkownika
@RestController
@RequestMapping("/api/landlord-listing")
public class LandlordResource {

  private final LandlordService landlordService;
  private final Validator validator;
  private final UserService userService;
  private ObjectMapper objectMapper = new ObjectMapper();

  public LandlordResource(LandlordService landlordService, Validator validator, UserService userService) {
    this.landlordService = landlordService;
    this.validator = validator;
    this.userService = userService;
  }

  @PostMapping(value = "/create",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<CreateListingDTO> create(
    MultipartHttpServletRequest request,
    @RequestPart(name = "dto") String saveListingDTOString
  ) throws IOException {
    List<PictureDTO> pictures = request.getFileMap()
      .values()
      .stream()
      .map(mapMultiPartFileToPictureDTO())
      .toList();

    SaveListingDTO saveListingDTO = objectMapper.readValue(saveListingDTOString, SaveListingDTO.class);
    saveListingDTO.setPictures(pictures);

    Set<ConstraintViolation<SaveListingDTO>> violations = validator.validate(saveListingDTO);
    if (!violations.isEmpty() ) {
      String violationsJoined = violations.stream()
        .map(violation -> violation.getPropertyPath() + " " + violation.getMessage())
        .collect(Collectors.joining());

      ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, violationsJoined);
      return ResponseEntity.of(problemDetail).build();
    }else {
      return ResponseEntity.ok(landlordService.create(saveListingDTO));
    }
  }

  // konwersja plików multipart na obiekty PictureDTO
  /**
   * Definicja z : https://loadfocus.com/pl-pl/glossary/what-is-multipart-form-data-content-type
   * Multipart/Form-Data jest typem zawartości używanym do wysyłania plików i danych w żądaniu HTTP,
   * zwykle za pomocą metody POST. Pozwala na połączenie danych binarnych i tekstowych,
   * co jest niezbędne do przesyłania plików.
   */
  private static Function<MultipartFile, PictureDTO> mapMultiPartFileToPictureDTO() {
    return multipartFile -> {
      try {
        return new PictureDTO(multipartFile.getBytes(), multipartFile.getContentType(), false);
      } catch (IOException ioe) {
        throw new UserException(String.format("Cannot parse multipart file: %s", multipartFile.getOriginalFilename()));
      }
    };
  }
}

