package com.ecommerce.sb_ecom.service.impl;

import com.ecommerce.sb_ecom.exceptions.APIException;
import com.ecommerce.sb_ecom.exceptions.ResourceNotFoundException;
import com.ecommerce.sb_ecom.model.Category;
import com.ecommerce.sb_ecom.model.Product;
import com.ecommerce.sb_ecom.payload.ProductDTO;
import com.ecommerce.sb_ecom.payload.ProductResponse;
import com.ecommerce.sb_ecom.repositories.CategoryRepository;
import com.ecommerce.sb_ecom.repositories.ProductReposity;
import com.ecommerce.sb_ecom.service.FileService;
import com.ecommerce.sb_ecom.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Service // poner esta clase como servicio de Spring
public class ProductServiceImpl implements ProductService {

    // Inyecta el repositorio de productos, categorías, fileService
    @Autowired
    ProductReposity productReposity;

    @Autowired
    CategoryRepository categoryRepository;

    // Inyecta el ModelMapper para convertir entre entidades y DTOs
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String paht;


    @Override
    public ProductDTO addProduct(Long categoryId, ProductDTO productDTO) {

        // Busca la categoría por su Id, lanza error si no se encuentra
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));


        //si se encuentran productos
        boolean isProductNotPresent = true;

        List<Product> products = category.getProducts();
        for (Product value : products) {
            if (value.getProductName().equals(productDTO.getProductName())) {
                isProductNotPresent = false;
                break;
            }
        }


        if(isProductNotPresent){
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
        }else {
            throw new APIException("Product already exists");
        }
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

        if (products.isEmpty()) {
            throw  new APIException("No products found");
        }

        // Crea un objeto ProductResponse, que  probablemente sera una respuesta que se enviara al cliente.
        ProductResponse productResponse = new ProductResponse();

        // Asigna la lista de ProductDTOs a la propiedad 'content' de ProductResponse.
        productResponse.setContent(productDTOS);

        return productResponse; // Retorna la respuesta con los productos mapeados.
    }


    @Override
    public ProductResponse searchByCategory(Long categoryId) {
        // Buscar categoria por Id,
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

    @Override
    public ProductDTO updateProductByImage(Long productId, MultipartFile image) throws IOException {

        // Obtener el producto por ID
        Product productFromDb =  productReposity.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        // Subir la imagen al servidor
        // Obtener el nombre del archivo de la imagen subida
        //String paht = "images/";
        String fileName = fileService.uploadImage(paht, image);

        // Actualizar el nombre del archivo en el producto
        productFromDb.setImage(fileName);

        // Guardar los cambios y devolver el producto mapeado a DTO
        Product updatedProduct = productReposity.save(productFromDb);
        return  modelMapper.map(updatedProduct, ProductDTO.class);
    }





}

