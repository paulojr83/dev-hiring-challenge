package com.challenge.reviews.controller;

import com.challenge.reviews.domain.comment.Comment;
import com.challenge.reviews.domain.product.Product;
import com.challenge.reviews.domain.review.Review;
import com.challenge.reviews.domain.review.ReviewDocument;
import com.challenge.reviews.exceptions.NotFoundException;
import com.challenge.reviews.repository.ProductRepository;
import com.challenge.reviews.repository.ReviewRepository;
import com.challenge.reviews.service.ReviewMongoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Spring REST controller for working with review entity.
 */
@RestController
@RequestMapping("/reviews")
public class ReviewsController {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final ReviewMongoService reviewMongoService;

    public ReviewsController(ReviewRepository reviewRepository,
                             ProductRepository productRepository,
                             ReviewMongoService reviewMongoService) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.reviewMongoService = reviewMongoService;
    }

    /**
     * Creates a review for a product.
     * <p>
     * 1. Add argument for review entity. Use {@link RequestBody} annotation.
     * 2. Check for existence of product.
     * 3. If product not found, return NOT_FOUND.
     * 4. If found, save review.
     *
     * @param productId The id of the product.
     * @return The created review or 404 if product id is not found.
     */
    @PostMapping("/products/{productId}")
    public ResponseEntity<?> createReviewForProduct(@Valid @PathVariable("productId") Long productId,
                                                    @Valid @RequestBody Review review) {

        Product product = productRepository.findById(productId).orElseThrow(NotFoundException::new);
        review.setProduct(product);
        Review saved = reviewRepository.save(review);

        if( saved.getReviewId() > 0 ){
            Set<Comment> comments = reviewRepository.findById(saved.getReviewId()).get().getComments();

            ReviewDocument reviewDocument = new ReviewDocument(saved, productId, comments);
            reviewMongoService.saveReview(reviewDocument);
        }

        return new ResponseEntity<>( review, HttpStatus.CREATED);
    }

    /**
     * Lists reviews by product.
     *
     * @param productId The id of the product.
     * @return The list of reviews.
     */
    @GetMapping("/products/{productId}")
    public ResponseEntity<List<?>> listReviewsForProduct(@Valid  @PathVariable("productId") Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(NotFoundException::new);

        if (product != null) {
            List<Review> allJpaReviews = reviewRepository.findAllByProduct(product);
            List<ReviewDocument> allMongoReviews = new ArrayList<>();

            for (Review review : allJpaReviews) {
                ReviewDocument mongoReview =
                        reviewMongoService.retrieveReviewById(review.getReviewId().toString());
                allMongoReviews.add(mongoReview);
            }
            return ResponseEntity.ok(allMongoReviews);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
    }
}
