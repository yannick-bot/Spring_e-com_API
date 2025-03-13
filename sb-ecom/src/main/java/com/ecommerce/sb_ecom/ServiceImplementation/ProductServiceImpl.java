package com.ecommerce.sb_ecom.ServiceImplementation;

import com.ecommerce.sb_ecom.DTOModels.Product.ProductDTO;
import com.ecommerce.sb_ecom.DTOModels.Product.ProductResponse;
import com.ecommerce.sb_ecom.Exceptions.ApiException;
import com.ecommerce.sb_ecom.Exceptions.RessourceNotFoundException;
import com.ecommerce.sb_ecom.Model.Category;
import com.ecommerce.sb_ecom.Model.Product;
import com.ecommerce.sb_ecom.Repositories.CategoryRepository;
import com.ecommerce.sb_ecom.Repositories.ProductRepository;
import com.ecommerce.sb_ecom.Service.FileService;
import com.ecommerce.sb_ecom.Service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private FileService fileService;
    @Value("${project.images}")
    private String path;



    public ProductDTO saveProduct(ProductDTO productDTO, Long categoryId) {
        Product product = modelMapper.map(productDTO, Product.class);
        // vérifier que la catégorie existe
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow( ()-> new ApiException(String.format("Category with id %d doesn't exists", categoryId)));

        // vérifier si il existe un produit avec le même nom et la même description
        boolean optProduct = productRepository.existsByProductNameAndDescription(product.getProductName(), product.getDescription());
        if (optProduct) throw new ApiException(String.format("Product with name %s and description %s already exists", product.getProductName(), product.getDescription()));


        // on enregistre le produit
        product.setCategory(category);
        // on calcule le specialPrice basé sur le discount
        double specialPrice = product.getPrice() - (product.getPrice() * product.getDiscount() / 100.0);
        product.setSpecialPrice(specialPrice);
        // on met l'image du produit
        product.setImage("default.png");
        Product savedProduct = productRepository.save(product);

        return modelMapper.map(savedProduct, ProductDTO.class);

    }

    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        // sorting
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        //pagination
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> productPage = productRepository.findAll(pageDetails);

        List<Product> products = productPage.getContent();
        if(products.isEmpty()) {
            throw new ApiException("The product List is empty");
        }

        List<ProductDTO> productDTOS = products.stream()
                .map(product-> modelMapper.map(product, ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setLastPage(productPage.isLast());
        productResponse.setTotalElements((int) productPage.getTotalElements());

        return productResponse;
    }

    @Override
    public ProductResponse getProductByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        // d'abord vérifier si la category exist
        Category optCategory = categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ApiException(String.format("Category with id %d doesn't exist", categoryId)));

        // ensuite voir si cette catégorie a une liste de produits
        if(optCategory.getProducts().isEmpty()) throw  new ApiException(String.format("The category %s has no products", optCategory.getCategoryName()));

        // sorting
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        //pagination
        Page<Product> productPage = productRepository.findByCategory(optCategory, PageRequest.of(pageNumber, pageSize, sortByAndOrder));

        List<Product> products = productPage.getContent();
        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        //envoyer la réponse
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalElements((int) productPage.getTotalElements());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setLastPage(productPage.isLast());

        return productResponse;
    }

    public ProductResponse getProductsByKeyWord(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        //sorting
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        //pagination
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> productPage = productRepository.findByProductNameLikeIgnoreCase('%' + keyword + '%', pageDetails);

        List<Product> products = productPage.getContent();

        if (products.isEmpty()) throw new ApiException("No products found with keyword" + keyword);

        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        //envoyer la réponse
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalElements((int) productPage.getTotalElements());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setLastPage(productPage.isLast());

        return productResponse;

    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
        //mappage vers le DTO
        Product product = modelMapper.map(productDTO, Product.class);

        //verifier que la catégorie existe
        Product oldProduct = productRepository.findById(productId)
                .orElseThrow(()-> new RessourceNotFoundException("product", "id", productId));

        //mettre à jour
        oldProduct.setProductName(product.getProductName());
        oldProduct.setPrice(product.getPrice());
        oldProduct.setDescription(product.getDescription());
        oldProduct.setDiscount(product.getDiscount());
        oldProduct.setQuantity(product.getQuantity());
        // on calcule le specialPrice basé sur le discount
        double specialPrice = product.getPrice() - (product.getPrice() * product.getDiscount() / 100.0);
        oldProduct.setSpecialPrice(specialPrice);
        oldProduct.setCategory(product.getCategory());

        Product newProduct = productRepository.save(oldProduct);

        return modelMapper.map(newProduct, ProductDTO.class);
    }

    /**
     * @param productId
     * @return
     */
    @Override
    public String deleteProduct(Long productId) {
        Product toDelete = productRepository.findById(productId)
                .orElseThrow(()-> new RessourceNotFoundException("product", "id", productId));

        productRepository.delete(toDelete);
        return "Product deleted";
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        // Get the product from DB
        Product dbProduct = productRepository.findById(productId)
                .orElseThrow(() -> new RessourceNotFoundException("product", "productId", productId));
        // upload image to server
        // Get the file name of uploaded image
        String fileName = fileService.uploadImage(path, image);
        // Updating the new file name to the product
        dbProduct.setImage(fileName);
        // return DTO after mapping model to DTO
        return modelMapper.map(productRepository.save(dbProduct), ProductDTO.class);
    }

}
