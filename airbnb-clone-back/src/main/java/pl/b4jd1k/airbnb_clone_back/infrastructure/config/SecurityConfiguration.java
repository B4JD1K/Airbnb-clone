package pl.b4jd1k.airbnb_clone_back.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {

  @Bean
  public SecurityFilterChain configure(HttpSecurity http) throws Exception {
    // tworze instancję handlera dla CSRF (Cross-Site Request Forgery)
    CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
    requestHandler.setCsrfRequestAttributeName(null);
    http.authorizeHttpRequests(authorize -> authorize // konfiguracja reguł autoryzacji
        .requestMatchers(HttpMethod.GET, "api/tenant-listing/get-all-by-category").permitAll() // endpoint jest dostępny nawet dla osób które nie są zalogowane
        .requestMatchers(HttpMethod.GET, "assets/*").permitAll() // dostęp do assetów (np. countries.json)
        .anyRequest().authenticated()) // wymaga uwierzytelnienia dla każdego żądania, każdy endpoint wymaga zalogowania
      .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) // konf. zabezpieczeń CSRF, używa ciasteczek do przechowywania tokena CSRF
        .csrfTokenRequestHandler(requestHandler))
      .oauth2Login(Customizer.withDefaults())
      .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults())) // umożliwia obsługę tokenów JTW
      .oauth2Client(Customizer.withDefaults()); // pozwala aplikacji na komunikację z innymi usługami

    return http.build(); // buduje i zwraca obiekt, ze zdefiniowaną całą konfiguracją zabezpieczeń
  }

  @Bean
  public GrantedAuthoritiesMapper userAuthoritiesMapper() {
    return authorities ->
    {
      Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

      authorities.forEach(grantedAuthority -> { // zwraca nowy zbiór przekształconych autoryzacji
        if (grantedAuthority instanceof OidcUserAuthority oidcUserAuthority) {
          grantedAuthorities
            .addAll(SecurityUtils.extractAuthorityFromClaims(oidcUserAuthority.getUserInfo().getClaims()));
        }
      });
      return grantedAuthorities;
    };
  }
}
