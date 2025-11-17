/*
OpenAPI документация — автоматическая генерация интерактивной документации для API.
Генерируется автоматически на основе контроллеров и DTO.
 */

package com.store.StorePlatform.config;

// Импорты классов необходимых для настройки OpenAPI
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
Аннотация @Configuration указывает, что этот класс содержит конфигурацию Spring.
Spring будет сканировать этот класс при запуске приложения и создавать указанные бины.
 */
@Configuration
public class OpenApiConfig {

    /*
    Аннотация @Bean указывает, что метод возвращает объект, который должен быть
    управляемым бином Spring. Этот бин будет автоматически создан при запуске приложения
    и доступен для внедрения в другие компоненты.
     */
    @Bean
    // Создается бин OpenAPI с помощью метода customOpenAPI()
    public OpenAPI customOpenAPI() {
        // Создание и настройка объекта OpenAPI для документации
        return new OpenAPI()
                .info(new Info() // метод info() настраивает основную информацию о API
                        .title("Store Platform API") // заголовок API, который будет отображаться в Swagger UI
                        .version("1.0") // версия API
                        .description("API для платформы создания интернет-магазинов") // описание API
                )
                .components(new Components().addSecuritySchemes("bearerAuth",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}
