package com.challenge.reviews.service;

import com.challenge.reviews.domain.review.ReviewDocument;
import com.challenge.reviews.repository.ReviewMongoRepository;
import org.springframework.stereotype.Service;

@Service
public class ReviewMongoServiceImpl implements ReviewMongoService {

	private final ReviewMongoRepository reviewMongoRepository;

	public ReviewMongoServiceImpl(ReviewMongoRepository reviewMongoRepository) {
		this.reviewMongoRepository = reviewMongoRepository;
	}

	@Override
	public void saveReview(ReviewDocument jpaReview) {
		ReviewDocument mongoReview =
				new ReviewDocument();
		mongoReview.setReviewId(jpaReview.getReviewId());
		mongoReview.setReviewTitle(jpaReview.getReviewTitle());
		mongoReview.setReviewText(jpaReview.getReviewText());
		mongoReview.setRecommended(jpaReview.isRecommended());
		reviewMongoRepository.save(mongoReview);
	}

	@Override
	public ReviewDocument retrieveReviewById(String reviewId) {
		ReviewDocument mongoReview = reviewMongoRepository.findById(reviewId)
				.orElse(null);
		return mongoReview;
	}
}
