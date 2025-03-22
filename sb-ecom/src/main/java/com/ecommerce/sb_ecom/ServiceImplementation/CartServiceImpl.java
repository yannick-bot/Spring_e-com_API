package com.ecommerce.sb_ecom.ServiceImplementation;

import com.ecommerce.sb_ecom.DTOModels.Cart.CartDTO;
import com.ecommerce.sb_ecom.DTOModels.Cart.CartResponse;
import com.ecommerce.sb_ecom.DTOModels.CartItem.CartItemDTO;
import com.ecommerce.sb_ecom.DTOModels.Product.ProductDTO;
import com.ecommerce.sb_ecom.Exceptions.ApiException;
import com.ecommerce.sb_ecom.Model.Cart;
import com.ecommerce.sb_ecom.Model.CartItem;
import com.ecommerce.sb_ecom.Model.Product;
import com.ecommerce.sb_ecom.Model.User;
import com.ecommerce.sb_ecom.Repositories.CartItemRepository;
import com.ecommerce.sb_ecom.Repositories.CartRepository;
import com.ecommerce.sb_ecom.Repositories.ProductRepository;
import com.ecommerce.sb_ecom.Repositories.UserRepository;
import com.ecommerce.sb_ecom.Service.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;


    // METHODES UTILITAIRES

    public Cart findUserCart(String username) {
        Optional<User> optUser = userRepository.findByUsername(username);
        if (optUser.isEmpty()) {
            throw new ApiException("User not found");
        }
        User foundUser = optUser.get();
        Optional<Cart> userCart = cartRepository.findByUser(foundUser);
        if (userCart.isEmpty()) {
            throw new ApiException("User's Cart doesn't exist");
        }
        return userCart.get();
    }

    Product findProduct(Long productId) {
        Optional<Product> optProduct = productRepository.findById(productId);
        if (optProduct.isEmpty()) {
            throw new ApiException("Product not found");
        }
        return optProduct.get();
    }

    public CartItem createCartItem(Product product, Cart cart) {
        CartItem cartItem = new CartItem();
        cartItem.setDiscount(product.getDiscount());
        if (product.getSpecialPrice() > 0) {
            cartItem.setProduct_price(product.getSpecialPrice());
        }
        else {
            cartItem.setProduct_price(product.getPrice());
        }
        cartItem.setQuantity(1);
        cartItem.setCart(cart);
        cartItem.setProduct(product);

        return cartItem;
    }

    public Cart setTotalPrice(Product product, Cart cart) {
        if (product.getSpecialPrice() > 0) {
            cart.setTotalPrice(cart.getTotalPrice() + product.getSpecialPrice());
        }
        else {
            cart.setTotalPrice(cart.getTotalPrice() + product.getPrice());

        }
        return cart;
    }



    @Override
    public CartResponse getAllCarts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        //sorting
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        // pagination
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize);
        Page<Cart> cartPage = cartRepository.findAll(pageDetails);

        // vérifier que la liste n'est pas vide
        List<Cart> carts= cartPage.getContent();
        if(carts.isEmpty()) {
            throw new ApiException("No Carts to display");
        }

        List<CartDTO> cartDTOS = carts.stream()
                .map(cart -> modelMapper.map(cart, CartDTO.class))
                .toList();

        CartResponse cartResponse = new CartResponse();
        cartResponse.setContent(cartDTOS);
        cartResponse.setPageNumber(cartPage.getNumber());
        cartResponse.setTotalPages(cartPage.getTotalPages());
        cartResponse.setTotalElements((int) cartPage.getTotalElements());
        cartResponse.setPageSize(cartPage.getSize());
        cartResponse.setLastPage(cartPage.isLast());

        return cartResponse;
    }



    @Override
    public CartDTO getUserCart(String username) {
        Cart cart = findUserCart(username);
        List<CartItem> cartItems = cart.getCartItems();

        List<Product> products = cartItems.stream()
                .map(CartItem::getProduct)
                .toList();

        List<ProductDTO> productDTOS = products.stream()
                .map(productDTO -> modelMapper.map(productDTO, ProductDTO.class))
                .toList();

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        cartDTO.setItems(productDTOS);
        return cartDTO;
    }



    @Override
    public CartDTO updateProductQuantity(Long productId, Integer quantity) {
        Product product = findProduct(productId);
        Double addPrice = 0.0;
        if(product.getSpecialPrice() > 0) {
            addPrice = quantity * product.getSpecialPrice();
        }
        else {
            addPrice = quantity * product.getPrice();
        }
        Optional<CartItem> cartItem = cartItemRepository.findByProduct(product);
        if (cartItem.isEmpty()) {
            throw new ApiException("CartItem doesn't exist for this product");
        }
        CartItem foundCartItem = cartItem.get();
        foundCartItem.setQuantity(foundCartItem.getQuantity() + quantity);
        CartItem savedCartItem = cartItemRepository.save(foundCartItem);
        Cart cart = savedCartItem.getCart();
        cart.setTotalPrice(cart.getTotalPrice() + addPrice);
        return modelMapper.map(cart, CartDTO.class);
    }

    @Override
    public String deleteProductFromCart(Long cartId, Long productId) {
        // trouver la Cart
        Optional<Cart> optCart = cartRepository.findById(cartId);
        if (optCart.isEmpty()) {
            throw new ApiException("Cart not found");
        }
        Cart cart = optCart.get();
        Product product = findProduct(productId);
        Optional<CartItem> optCartItem = cartItemRepository.findByProduct(product);
        if (optCartItem.isEmpty()) {
            throw new ApiException("Product doesn't exist");
        }
        CartItem cartItem = optCartItem.get();
        cartItemRepository.delete(cartItem);
        return "Product deleted from Cart";
    }



    @Override
    public CartDTO addProductToCart(Long productId, String name) {

        Product product = findProduct(productId);
        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);

        Cart newUserCart = new Cart();
        Cart userCart = null;
        // vérifier si parmi les Carts en base de données, il y en a une associé à ce user
        try{
            userCart = findUserCart(name);
        } catch (ApiException e) {
            // Créer une CartItem pour le produit à ajouter

            newUserCart.setUser(userRepository.findByUsername(name).get());
            Cart newCart = setTotalPrice(product, newUserCart);

            Cart savedCart = cartRepository.save(newCart);
            CartDTO savedCartDTO = modelMapper.map(savedCart, CartDTO.class);

            CartItem savedCartItem = cartItemRepository.save(createCartItem(product, savedCart));
            savedCartDTO.getItems().add(productDTO);

            return savedCartDTO;
        }

        // si le user a déja une Cart rechercher si il y a une CartItem pour le produit
        Optional<CartItem> cartItem = userCart.getCartItems().stream()
                .filter(cartIt -> cartIt.getProduct().getProductId().equals(productId))
                .findFirst();
        if (cartItem.isEmpty()) {
            // Créer une CartItem pour le produit à ajouter
            CartItem savedCartItem = cartItemRepository.save(createCartItem(product, userCart));
            Cart cart = setTotalPrice(product, userCart);

            CartDTO userCartDTO = modelMapper.map(cart, CartDTO.class);
            userCartDTO.getItems().add(productDTO);
        }
        else {
            return updateProductQuantity(productId, 1);
        }

        return modelMapper.map(userCart, CartDTO.class);
    }
}
