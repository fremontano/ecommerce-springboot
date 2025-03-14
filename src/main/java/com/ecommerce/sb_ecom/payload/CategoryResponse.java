package com.ecommerce.sb_ecom.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * CategoryResponse es una clase que se usa para estructurar la respuesta de la API cuando se obtiene una lista de categorias.
 * En lugar de devolver solo una lista de categorías, se encapsulan en esta clase, lo que permite agregar más informacion si es necesario.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {

    /**
     * Lista de categorías que se enviara como respuesta en la API.
     * En lugar de devolver directamente una lista de entidades Category, se usa una lista de DTOs (CategoryDTO).
     * Esto ayuda a mantener una separación entre la capa de datos y la capa de presentación.
     */
    private List<CategoryDTO> content;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Long totalPages;
    private boolean lastPage;

}
