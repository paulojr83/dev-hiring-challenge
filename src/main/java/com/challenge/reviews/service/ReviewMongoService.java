package com.challenge.reviews.service;

import com.challenge.reviews.domain.review.ReviewDocument;

public interface ReviewMongoService {

	public void saveReview( ReviewDocument jpaReview);

	public ReviewDocument retrieveReviewById(String reviewId);
}
