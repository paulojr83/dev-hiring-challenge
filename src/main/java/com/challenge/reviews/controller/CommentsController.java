package com.challenge.reviews.controller;

import com.challenge.reviews.domain.comment.Comment;
import com.challenge.reviews.domain.review.Review;
import com.challenge.reviews.exceptions.NotFoundException;
import com.challenge.reviews.repository.CommentRepository;
import com.challenge.reviews.repository.ReviewRepository;
import com.challenge.reviews.service.CommentMongoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Spring REST controller for working with comment entity.
 */
@RestController
@RequestMapping("/api/comments")
public class CommentsController {

    private final CommentRepository commenRepository;
    private final ReviewRepository reviewRepository;
    private final CommentMongoService commentMongoService;

    public CommentsController(CommentRepository commenRepository, ReviewRepository reviewRepository,
                              CommentMongoService commentMongoService) {
        this.commenRepository = commenRepository;
        this.reviewRepository = reviewRepository;
        this.commentMongoService = commentMongoService;
    }

    /**
     * Creates a comment for a review.
     *
     * 1. Add argument for comment entity. Use {@link RequestBody} annotation.
     * 2. Check for existence of review.
     * 3. If review not found, return NOT_FOUND.
     * 4. If found, save comment.
     *
     * @param reviewId The id of the review.
     */
    @PostMapping("/reviews/{reviewId}")
    public ResponseEntity<Comment> createCommentForReview(@Valid @PathVariable("reviewId") Long reviewId,
														  @Valid @RequestBody Comment comment) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(NotFoundException::new);
        comment.setReview(review);
        Comment commentSaved = commenRepository.save(comment);

        if( commentSaved.getCommentId() > 0 ){
            commentMongoService.saveComment(commentSaved);
        }
        return new ResponseEntity<>( commentSaved, HttpStatus.CREATED);
    }

    /**
     * List comments for a review.
     *
     * 2. Check for existence of review.
     * 3. If review not found, return NOT_FOUND.
     * 4. If found, return list of comments.
     *
     * @param reviewId The id of the review.
     */
    @GetMapping("/reviews/{reviewId}")
    public ResponseEntity<List<Comment>> listCommentsForReview(@Valid @PathVariable("reviewId") Long reviewId) {
        return ResponseEntity.ok(commenRepository.findAllByReview(new Review(reviewId)));
    }
}
