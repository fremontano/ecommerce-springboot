package com.ecommerce.sb_ecom.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long productId;

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    private String productName;

    private String image;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(min = 10, max = 500, message = "La descripción debe tener entre 10 y 500 caracteres")
    private String description;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    private Integer quantity;

    @Positive(message = "El precio debe ser mayor que cero")
    private double price; //100

    @Min(value = 0, message = "El descuento no puede ser negativo")
    private double discount;//25

    @Positive(message = "El precio especial debe ser mayor que cero")
    private double specialPrice;//75

    // 100-(25/100)*100

    //relacion con category
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;




}
