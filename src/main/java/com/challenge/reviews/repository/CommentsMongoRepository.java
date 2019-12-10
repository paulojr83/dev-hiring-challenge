package com.challenge.reviews.repository;

import com.challenge.reviews.domain.comment.CommentDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentsMongoRepository extends MongoRepository<CommentDocument, String> {
}
