package com.example.phakebukvip.repository;

import com.example.phakebukvip.model.Post;
import com.example.phakebukvip.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedAtDesc();
    List<Post> findByUserOrderByCreatedAtDesc(User user);
    List<Post> findByContentContainingIgnoreCase(String keyword);


}
