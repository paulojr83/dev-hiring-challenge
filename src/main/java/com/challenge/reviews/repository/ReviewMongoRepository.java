package com.challenge.reviews.repository;

import com.challenge.reviews.domain.review.ReviewDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReviewMongoRepository extends MongoRepository<ReviewDocument, String> {
}
