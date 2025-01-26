package pl.b4jd1k.airbnb_clone_back.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories({"pl.b4jd1k.airbnb_clone_back.user.repository",
  "pl.b4jd1k.airbnb_clone_back.listing.repository",
  "pl.b4jd1k.airbnb_clone_back.booking.repository"})
@EnableTransactionManagement
@EnableJpaAuditing
public class DatabaseConfiguration {
}
