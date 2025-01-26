package pl.b4jd1k.airbnb_clone_back.user.mapper;

import org.mapstruct.Mapper;
import pl.b4jd1k.airbnb_clone_back.user.application.dto.ReadUserDTO;
import pl.b4jd1k.airbnb_clone_back.user.domain.Authority;
import pl.b4jd1k.airbnb_clone_back.user.domain.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

  ReadUserDTO readUserDTOToUser(User user);

  default String mapAuthoritiesToString(Authority authority) {
    return authority.getName();
  }
}
