package pl.b4jd1k.airbnb_clone_back.user.mapper;

import org.mapstruct.Mapper;
import pl.b4jd1k.airbnb_clone_back.user.application.dto.ReadUserDTO;
import pl.b4jd1k.airbnb_clone_back.user.domain.Authority;
import pl.b4jd1k.airbnb_clone_back.user.domain.User;

// MapStruct automatycznie wygeneruje implementacje, która będzie konwertować obiekty między klasą 'User' a jej DTO
@Mapper(componentModel = "spring")
public interface UserMapper {

  ReadUserDTO readUserDTOToUser(User user); // mapuje obiekt User na ReadUserDTO

  // dla każdego obiektu 'Authority' zwróci nazwę roli
  // MapStruct dzięki tej metodzie skonwertuje kolecjkę Set<Authority> (z klasy User) na Set<String> (ReadUserDTO)
  default String mapAuthoritiesToString(Authority authority){
    return authority.getName();
  }
}
