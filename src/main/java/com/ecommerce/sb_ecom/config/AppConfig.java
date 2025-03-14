package com.ecommerce.sb_ecom.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Importaciones necesarias
 * - ModelMapper: Biblioteca que ayuda a convertir objetos de un tipo a otro, por ejemplo, de `Category` a `CategoryDTO`.
 * - @Configuration: Indica que esta clase contiene configuraciones para la aplicacion.
 * - @Bean: Indica que un metodo devuelve un objeto que sera administrado por el contenedor de Spring.
 */

@Configuration
public class AppConfig {

    /**
     * Se define un Bean de `ModelMapper`, que es una herramienta para mapear objetos.
     * Esto permite convertir automaticamente entre entidades y DTOs sin necesidad de escribir codigo manualmente.
     *
     * @return una instancia de `ModelMapper` lista para ser utilizada en la inyeccion de dependencias.
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    };

}
