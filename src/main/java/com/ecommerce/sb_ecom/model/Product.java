package com.ecommerce.sb_ecom.model;


import jakarta.persistence.*;
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

    private String productName;
    private String image;
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
