package com.challenge.reviews.domain.review;

import com.challenge.reviews.domain.comment.Comment;
import com.challenge.reviews.domain.comment.CommentDocument;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;
import java.util.List;
import java.util.Set;

@Document("reviews")
public class ReviewDocument {

	@Id
	private String id;

	@Column(name = "review_id")
	private String reviewId;

	private String reviewTitle;

	private String reviewText;

	private boolean recommended;

	@Column(name = "product_id")
	private Long productId;

	private List<CommentDocument> comments;

	public ReviewDocument() {
	}

	public ReviewDocument(Review review, Long productId, Set<Comment> comments) {
		this.reviewId = review.getReviewId().toString();
		this.reviewTitle = review.getTitle();
		this.reviewText = review.getReviewText();
		this.recommended = review.isRecommended();
		this.productId  = productId;
		comments.forEach(comment ->  this.comments.add(new CommentDocument(comment) ) );
	}
 
	public String getReviewId() {
		return reviewId;
	}

	public void setReviewId(String reviewId) {
		this.reviewId = reviewId;
	}

	public String getReviewTitle() {
		return reviewTitle;
	}

	public void setReviewTitle(String reviewTitle) {
		this.reviewTitle = reviewTitle;
	}

	public String getReviewText() {
		return reviewText;
	}

	public void setReviewText(String reviewText) {
		this.reviewText = reviewText;
	}

	public boolean isRecommended() {
		return recommended;
	}

	public void setRecommended(boolean recommended) {
		this.recommended = recommended;
	}

}
