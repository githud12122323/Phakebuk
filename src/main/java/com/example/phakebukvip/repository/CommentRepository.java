package com.example.phakebukvip.repository;

import com.example.phakebukvip.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
