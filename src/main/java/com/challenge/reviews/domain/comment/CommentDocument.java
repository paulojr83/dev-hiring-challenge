package com.challenge.reviews.domain.comment;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;

@Document("comments")
public class CommentDocument {

	@Id
	private String id;

	@Column(name = "comment_id")
	private Long commentId;

	private String title;

	private String comment;

	public CommentDocument() {
	}

	public CommentDocument(Comment comment ) {
		this.commentId = comment.getCommentId();
		this.title = comment.getTitle();
		this.comment = comment.getComment();
	}

	public CommentDocument(String id, Long commentId, String title, String comment) {
		this.id = id;
		this.commentId = commentId;
		this.title = title;
		this.comment = comment;
	}

	public String getId() {
		return id;
	}

	public Long getCommentId() {
		return commentId;
	}

	public void setCommentId(Long commentId) {
		this.commentId = commentId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
