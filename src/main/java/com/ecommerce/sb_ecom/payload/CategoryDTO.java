package com.ecommerce.sb_ecom.payload;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    private  Long categoryId;
    private String categoryName;
}

/**
 * CategoryDTO (Data Transfer Object) es una clase que actua como un contenedor de datos
 * para transferir información entre la capa de servicio y la capa de presentacion.
 * Su objetivo es evitar exponer directamente la entidad `Category`, protegiendo la estructura interna del sistema.
 */