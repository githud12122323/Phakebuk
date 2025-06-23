package com.example.phakebukvip.repository;

import com.example.phakebukvip.model.Follow;
import com.example.phakebukvip.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    boolean existsByFollowerAndFollowing(User follower, User following);
    Optional<Follow> findByFollowerAndFollowing(User follower, User following);

    List<Follow> findByFollower(User follower);  // bạn đang theo dõi
    List<Follow> findByFollowing(User following); // ai đang theo dõi bạn
}
