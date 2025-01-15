package pl.b4jd1k.airbnb_clone_back.user.application.dto;

import pl.b4jd1k.airbnb_clone_back.infrastructure.config.SecurityUtils;

import java.util.Set;
import java.util.UUID;

public record ReadUserDTO(UUID publicId,
                          String firstName,
                          String lastName,
                          String email,
                          String imageUrl,
                          Set<String> authorities){
}
