package pl.b4jd1k.airbnb_clone_back.infrastructure.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import pl.b4jd1k.airbnb_clone_back.user.domain.Authority;
import pl.b4jd1k.airbnb_clone_back.user.domain.User;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SecurityUtils {

  public static final String ROLE_TENANT = "ROLE_TENANT";
  public static final String ROLE_LANDLORD = "ROLE_LANDLORD";

  public static final String CLAIMS_NAMESPACE = "https://www.b4jd1k.pl/roles";

  public static User mapOauth2AttributesToUser(Map<String, Object> attributes) {
    User user = new User();
    String sub = String.valueOf(attributes.get("sub"));

    String username = null;

    // wszystkie poniższe 'attributes' dotyczą danych konta użytkownika
    // jeśli wartość jest inna niż pusta to przypisać użytkownikowi
    if (attributes.get("preferred_username") != null) {
      username = ((String) attributes.get("preffered_username")).toLowerCase();
    }

    if (attributes.get("given_name") != null) {
      user.setFirstName((String) attributes.get("given_name"));
    } else if (attributes.get("nickname") != null) {
      user.setFirstName((String) attributes.get("nickname"));
    }

    if (attributes.get("family_name") != null) {
      user.setLastName((String) attributes.get("family_name"));
    }

    if (attributes.get("email") != null) {
      user.setEmail((String) attributes.get("email"));
    } else if (sub.contains("|") && (username != null && username.contains("@"))) {
      user.setEmail(username);
    } else {
      user.setEmail(sub);
    }

    if (attributes.get("picture") != null) {
      user.setImageUrl((String) attributes.get("picture"));
    }

    // mapowanie niestandardowych claims na role użytkownika
    if (attributes.get(CLAIMS_NAMESPACE) != null) {
      List<String> authoritiesRaw = (List<String>) attributes.get(CLAIMS_NAMESPACE);
      Set<Authority> authorities = authoritiesRaw.stream()
        .map(authority -> {
          Authority auth = new Authority();
          auth.setName(authority);
          return auth;
        }).collect(Collectors.toSet());
      user.setAuthorities(authorities);
    }
    return user;
  }

  // ekstraktuje listę ról z tokena i mapuje je na obiekty
  public static List<SimpleGrantedAuthority> extractAuthorityFromClaims(Map<String, Object> claims) {
    return mapRolesToGrantedAuthorities(getRolesFromClaims(claims));
  }

  // pobiera role użytkownika z claims
  public static Collection<String> getRolesFromClaims(Map<String, Object> claims) {
    return (List<String>) claims.get(CLAIMS_NAMESPACE);
  }

  // mapuje listę ról (jako string) zaczynających się od "ROLE_", na obiekty SimpleGrantedAuthority
  public static List<SimpleGrantedAuthority> mapRolesToGrantedAuthorities(Collection<String> roles) {
    return roles.stream()
      .filter(role -> role.startsWith("ROLE_")) // filtruje role zaczynające się od "ROLE_"
      .map(SimpleGrantedAuthority::new) // tworzy obiekty SimpleGrantedAuthority dla każdej roli
      .toList(); // i zbiera wynik do listy
  }

  // sprawdza czy zalogowany użytkownik ma co najmniej jedną z podanych ról (ROLE_XYZ)
  public static boolean hasCurrentUserAnyOfAuthorities(String... authorities) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return (authentication != null && getAuthorities(authentication) // pobiera role użytkownika
      .anyMatch(authority -> Arrays.asList(authorities).contains(authority))); // prawdza, czy którakolwiek nazwa roli użytkownika znajduje się w podanej tablicy 'authorities'
  }

  // pobiera strumień nazw ról z obiektu Authentication
  private static Stream<String> getAuthorities(Authentication authentication) {
    Collection<? extends GrantedAuthority> authorities = authentication
      instanceof JwtAuthenticationToken jwtAuthenticationToken ? // jeśli authentication jest JwtAuthenticationToken
      extractAuthorityFromClaims(jwtAuthenticationToken.getToken().getClaims()) : // to wyciąga role z claims
      authentication.getAuthorities(); // w przeciwnym razie pobiera role bezpośrednio
    return authorities.stream().map(GrantedAuthority::getAuthority); // następnie mapuje obiekty GrantedAuthority na nazwy ról
  }
}
