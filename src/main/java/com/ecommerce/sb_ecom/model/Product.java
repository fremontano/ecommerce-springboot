package com.ecommerce.sb_ecom.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @NotBlank
    @Size(min = 3, max = 100, message = "Product Name must contain most from 3 characters")
    private String productName;
    private String image;

    @NotBlank
    @Size(min = 8, message = "description  must contain most from 8 characters")
    private String description;

    private Integer quantity;
    private double price; //100
    private double discount;//25
    private double specialPrice;//75

    // 100-(25/100)*100

    //relacion con category
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;




}
