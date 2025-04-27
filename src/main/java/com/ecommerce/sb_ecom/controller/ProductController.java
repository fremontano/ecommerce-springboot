package com.ecommerce.sb_ecom.controller;


import com.ecommerce.sb_ecom.config.AppConstants;
import com.ecommerce.sb_ecom.model.Product;
import com.ecommerce.sb_ecom.payload.CategoryDTO;
import com.ecommerce.sb_ecom.payload.ProductDTO;
import com.ecommerce.sb_ecom.payload.ProductResponse;
import com.ecommerce.sb_ecom.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ProductController {


    // Inyecta el servicio ProductService automaticamente
    @Autowired
    private ProductService productService;

    @PostMapping("/admin/categories/{categoryId}/product") // Mapea las solicitudes POST a esta ruta
    public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody ProductDTO productDTO, // Recibe el producto en el cuerpo de la solicitud
                                                 @PathVariable Long categoryId) { // Extrae la categoryId de la URL

        // Llama al servicio para agregar el producto
        ProductDTO saveProductDTO = productService.addProduct(categoryId, productDTO);
        // Devuelve el producto creado con un estado 201
        return new ResponseEntity<>(saveProductDTO, HttpStatus.CREATED);
    }


    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse>getAllProducts(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize ,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder",defaultValue = AppConstants.SORT_DIR,required = false) String sortOrder
    ) {
        ProductResponse productResponse = productService.getAllProducts(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }


    @GetMapping("/public/categories/{categoryId}/product")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long categoryId,
                                                          @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                          @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize ,
                                                          @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
                                                          @RequestParam(name = "sortOrder",defaultValue = AppConstants.SORT_DIR,required = false) String sortOrder) {
        ProductResponse productResponse = productService.searchByCategory(categoryId, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }


    @GetMapping("/public/product/{keyword}")
    public  ResponseEntity<ProductResponse>getProductByKeyword(@PathVariable String keyword,
                                                               @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                               @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize ,
                                                               @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
                                                               @RequestParam(name = "sortOrder",defaultValue = AppConstants.SORT_DIR,required = false) String sortOrder) {

        ProductResponse productResponse =  productService.searchProductByKeyword(keyword,pageNumber, pageSize, sortBy, sortOrder);

        return new ResponseEntity<>(productResponse, HttpStatus.FOUND);
    }

    @PutMapping("/admin/product/{productId}")
    public ResponseEntity<ProductDTO>updateProduct(@Valid @PathVariable Long productId,
                                                   @RequestBody ProductDTO productDTO) {

        ProductDTO updateProductDTO =  productService.updateProduct(productId, productDTO);
        return new ResponseEntity<>(updateProductDTO, HttpStatus.OK);

    }

    @DeleteMapping("/admin/product/{productId}")
    public ResponseEntity<ProductDTO>deleteProduct(@PathVariable Long productId) {
        ProductDTO deleteProductDTO = productService.deleteProduct(productId);

        return new ResponseEntity<>(deleteProductDTO, HttpStatus.OK);
    }

    //SUBIR IMAGEN

    @PutMapping("/products/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductByImage(@PathVariable Long productId,
                                                           @RequestParam("image" ) MultipartFile image) throws IOException {
        ProductDTO updateProduct = productService.updateProductByImage(productId, image);
        return new ResponseEntity<>(updateProduct, HttpStatus.OK);
    }



}
