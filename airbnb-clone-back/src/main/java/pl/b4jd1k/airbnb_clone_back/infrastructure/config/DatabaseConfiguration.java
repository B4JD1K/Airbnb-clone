package pl.b4jd1k.airbnb_clone_back.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration // definiuje ustawienia i komponenty, które będą zarządzane przez Spring Boota
@EnableJpaRepositories({"pl.b4jd1k.airbnb_clone_back.user.repository"}) // aktywuje Spring Data JPA i umożliwia skanowanie pakietów w poszukiwaniu repo
@EnableTransactionManagement // włącza zarządzanie transakcjami
@EnableJpaAuditing // włącza audytowanie encji i umożliwia korzystanie z pól 'auditing' '
public class DatabaseConfiguration {
}
