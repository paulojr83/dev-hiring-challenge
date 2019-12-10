package com.challenge.reviews.repository;

import com.challenge.reviews.domain.product.Product;
import com.challenge.reviews.domain.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

	List<Review> findAllByProduct(Product product);
}
