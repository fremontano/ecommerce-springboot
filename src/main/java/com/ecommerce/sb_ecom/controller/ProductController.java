package com.ecommerce.sb_ecom.controller;


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

@RestController
@RequestMapping("/api")
public class ProductController {


    // Inyecta el servicio ProductService automaticamente
    @Autowired
    private ProductService productService;

    @PostMapping("/admin/categories/{categoryId}/product") // Mapea las solicitudes POST a esta ruta
    public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductDTO productDTO, // Recibe el producto en el cuerpo de la solicitud
                                                 @PathVariable Long categoryId) { // Extrae la categoryId de la URL
        // Llama al servicio para agregar el producto
        ProductDTO saveProductDTO = productService.addProduct(categoryId, productDTO);
        // Devuelve el producto creado con un estado 201
        return new ResponseEntity<>(saveProductDTO, HttpStatus.CREATED);
    }


    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse>getAllProducts() {
        ProductResponse productResponse = productService.getAllProducts();
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }


    @GetMapping("/public/categories/{categoryId}/product")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long categoryId) {
        ProductResponse productResponse = productService.searchByCategory(categoryId);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }


    @GetMapping("/public/product/{keyword}")
    public  ResponseEntity<ProductResponse>getProductByKeyword(@PathVariable String keyword) {

        ProductResponse productResponse =  productService.searchProductByKeyword(keyword);

        return new ResponseEntity<>(productResponse, HttpStatus.FOUND);
    }

    @PutMapping("/admin/product/{productId}")
    public ResponseEntity<ProductDTO>updateProduct(@PathVariable Long productId,
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
    public ResponseEntity<ProductDTO>updateProductImage(@PathVariable Long productId,
                                                        @RequestParam("Image") MultipartFile image) {

      ProductDTO product =   productService.updateProductByImage(productId, (ProductDTO) image);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }



}
