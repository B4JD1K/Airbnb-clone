package pl.b4jd1k.airbnb_clone_back.user.application;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.b4jd1k.airbnb_clone_back.infrastructure.config.SecurityUtils;
import pl.b4jd1k.airbnb_clone_back.user.application.dto.ReadUserDTO;
import pl.b4jd1k.airbnb_clone_back.user.domain.User;
import pl.b4jd1k.airbnb_clone_back.user.mapper.UserMapper;
import pl.b4jd1k.airbnb_clone_back.user.repository.UserRepository;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

  private static final String UPDATED_AT_KEY = "updated_at";
  private final UserRepository userRepository;
  private final UserMapper userMapper;

  public UserService(UserRepository userRepository, UserMapper userMapper) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
  }

  // pobiera aktualnie uwierzytelnionego użytkownika z SecurityContext
  @Transactional(readOnly = true)
  public ReadUserDTO getAuthenticatedUserFromSecurityContext() {
    // pobiera główny obiekt uwierzytelniony w SecurityContext
    OAuth2User principal = (OAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    // mapuje atrybuty OAuth2 na obiekt User
    User user = SecurityUtils.mapOauth2AttributesToUser(principal.getAttributes());
    return getByEmail(user.getEmail()).orElseThrow(); // pobiera użytkownika z bazy (na podst. emaila) lub rzuca wyjątek
  }

  @Transactional(readOnly = true)
  public Optional<ReadUserDTO> getByEmail(String email) {
    Optional<User> oneByEmail = userRepository.findOneByEmail(email);
    return oneByEmail.map(userMapper::readUserDTOToUser); // mapowanie obiektu User na DTO
  }

  // synchronizuje użytkownika z zewnętrzymi Identity Provider (google, fb itp)
  public void syncWithIdp(OAuth2User oAuth2User, boolean forceResync) {

    // pobiera mapę atrybutów z obiektu OAuth2User i mapuje je na obiekt User
    Map<String, Object> attributes = oAuth2User.getAttributes();
    User user = SecurityUtils.mapOauth2AttributesToUser(attributes);

    // szuka użytkownika w bazie danych na podstawie emaila
    Optional<User> existingUser = userRepository.findOneByEmail(user.getEmail());
    if (existingUser.isPresent()) {
      if (attributes.get(UPDATED_AT_KEY) != null) {
        Instant lastModifiedDate = existingUser.orElseThrow().getLastModifiedDate();
        Instant idpModifiedDate;
        if (attributes.get(UPDATED_AT_KEY) instanceof Instant instant) {
          idpModifiedDate = instant;
        } else { // jeśli nie jest typem Instant, traktuje "updated_at" jako sekundowy timestamp i konwertuje
          idpModifiedDate = Instant.ofEpochSecond((Integer) attributes.get(UPDATED_AT_KEY));
        }
        if (idpModifiedDate.isAfter(lastModifiedDate) || forceResync) { // jeśli data jest nowsza lub wymuszono synchronizację - aktualizuje użytkownika
          updateUser(user);
        }
      }
    } else { // jeśli użytkownik nie istnieje - zapisuje nowego użytkownika do bazy
      userRepository.saveAndFlush(user);
    }
  }

  private void updateUser(User user) {
    Optional<User> userToUpdateOpt = userRepository.findOneByEmail(user.getEmail());
    if (userToUpdateOpt.isPresent()) {
      User userToUpdate = userToUpdateOpt.get();

      // aktualizacja danych użytkownika
      userToUpdate.setEmail(user.getEmail());
      userToUpdate.setFirstName(user.getFirstName());
      userToUpdate.setLastName(user.getLastName());
      userToUpdate.setAuthorities(user.getAuthorities());
      userToUpdate.setImageUrl(user.getImageUrl());

      userRepository.saveAndFlush(userToUpdate); // zapis zmian w db
    }
  }

  // pobiera użytkownika z bazy na podstawie 'publicId' - szuka i mapuje na DTO
  public Optional<ReadUserDTO> getByPublicId(UUID publicId) {
    Optional<User> oneByPublicId = userRepository.findOneByPublicId(publicId);
    return oneByPublicId.map(userMapper::readUserDTOToUser);
  }
}
