package com.ecommerce.sb_ecom.service.impl;

import com.ecommerce.sb_ecom.exceptions.ResourceNotFoundException;
import com.ecommerce.sb_ecom.model.Category;
import com.ecommerce.sb_ecom.model.Product;
import com.ecommerce.sb_ecom.payload.ProductDTO;
import com.ecommerce.sb_ecom.payload.ProductResponse;
import com.ecommerce.sb_ecom.repositories.CategoryRepository;
import com.ecommerce.sb_ecom.repositories.ProductReposity;
import com.ecommerce.sb_ecom.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service // poner esta clase como servicio de Spring
public class ProductServiceImpl implements ProductService {

    // Inyecta el repositorio de productos, categorías
    @Autowired
    ProductReposity productReposity;

    @Autowired
    CategoryRepository categoryRepository;

    // Inyecta el ModelMapper para convertir entre entidades y DTOs
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProductDTO addProduct(Long categoryId, ProductDTO productDTO) {

        // Busca la categoría por su Id, lanza error si no se encuentra
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        Product product = modelMapper.map(productDTO, Product.class);

        // Asocia la categoría al producto
        product.setCategory(category);
        product.setImage("default.png");

        // Calcula el precio especial basado en el descuento
        double specialPrice = product.getPrice() - (product.getDiscount() * 0.01) * product.getPrice();
        // Asigna el precio especial al producto
        product.setSpecialPrice(specialPrice);

        // Guarda el producto en la base de datos
        Product savedProduct = productReposity.save(product);

        // Mapea el producto guardado a un DTO y lo devuelve
        return modelMapper.map(savedProduct, ProductDTO.class);
    }


    @Override
    public ProductResponse getAllProducts() {

        // Obtener todos los productos de la base de datos.
        List<Product> products = productReposity.findAll();

        // Convierte la lista de productos (entidades) a una lista de ProductDTO.
        // Se utiliza stream() para procesar la lista, map() para convertir cada producto en un ProductDTO,
        // y toList() para recoger el resultado en una nueva lista.
        // Utiliza ModelMapper para la conversion

        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        // Crea un objeto ProductResponse, que es probablemente sera una respuesta que se enviara al cliente.
        ProductResponse productResponse = new ProductResponse();

        // Asigna la lista de ProductDTOs a la propiedad 'content' de ProductResponse.
        productResponse.setContent(productDTOS);

        return productResponse; // Retorna la respuesta con los productos mapeados.

    }

    @Override
    public ProductResponse searchByCategory(Long categoryId) {
        // Buscar categoría por Id,
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        // Obtener productos de la categoría ordenados por precio ascendente.
        List<Product> products = productReposity.findByCategoryOrderByPriceAsc(category);

        // Convertimos la lista de productos "List,Product" a "List,ProductDTO" usando modelMapper para mapear
        // las entidades Product a sus correspondientes objetos ProductDTO.
        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        // Creamos una instancia ProductResponse para contener lalista de productos convertidas.
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);

        return productResponse;
    }

    @Override
    public ProductResponse searchProductByKeyword(String keyword) {
        List<Product> products = productReposity.findByProductNameLikeIgnoreCase("%"+ keyword+ "%");
        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class)).toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        return productResponse;
    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {

        //Obtener producto de la base de datos
        Product productFromDb = productReposity.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        Product product = modelMapper.map(productDTO, Product.class);

       //actualizar producto informacion de la peticion
        productFromDb.setProductName(product.getProductName());
        productFromDb.setDescription(product.getDescription());
        productFromDb.setPrice(product.getPrice());
        productFromDb.setQuantity(product.getQuantity());
        productFromDb.setDiscount(product.getDiscount());
        productFromDb.setSpecialPrice(product.getSpecialPrice());

        //actualizar en la base de datos
        Product savedProduct = productReposity.save(productFromDb);

        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {

        Product product = productReposity.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        productReposity.delete(product);
        return modelMapper.map(product, ProductDTO.class);
    }


    //actualizar producto por imagen
    @Override
    public ProductDTO updateProductByImage(Long productId, ProductDTO image) {
        return null;
    }


}

