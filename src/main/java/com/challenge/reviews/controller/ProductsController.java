package com.challenge.reviews.controller;

import com.challenge.reviews.domain.product.Product;
import com.challenge.reviews.exceptions.NotFoundException;
import com.challenge.reviews.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Spring REST controller for working with product entity.
 */
@RestController
@RequestMapping("/api/products")
public class ProductsController {

    private final ProductRepository productRepository;
    @Autowired
    public ProductsController( ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Creates a product.
     *
     * 1. Accept product as argument. Use {@link RequestBody} annotation.
     * 2. Save product.
     */
    @PostMapping
    ResponseEntity<?> createProduct(@Valid @RequestBody Product product) throws URISyntaxException {
        Product resource =  productRepository.save(product);
        return new ResponseEntity(resource, HttpStatus.CREATED);
    }

    /**
     * Finds a product by id.
     *
     * @param id The id of the product.
     * @return The product if found, or a 404 not found.
     */
    @GetMapping("/{id}")
    ResponseEntity<Product> findById(@Valid @PathVariable("id") Long id) {
        Product product = productRepository.findById(id).orElseThrow(NotFoundException::new);
        return new ResponseEntity(product, HttpStatus.OK);
    }

    /**
     * Lists all products.
     *
     * @return The list of products.
     */
    @GetMapping
    ResponseEntity<Product> listProducts() {
        List<Product> resources = productRepository.findAll();
        return new ResponseEntity(resources, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@Valid @PathVariable("id") Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            productRepository.delete(product);
        } else {
            throw new HttpServerErrorException(HttpStatus.NOT_FOUND);
        }
    }
}
