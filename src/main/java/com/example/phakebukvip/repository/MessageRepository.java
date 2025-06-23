package com.example.phakebukvip.repository;

import com.example.phakebukvip.model.Message;
import com.example.phakebukvip.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySenderAndRecipientOrRecipientAndSenderOrderBySentAtAsc(
            User sender1, User recipient1, User sender2, User recipient2
    );
}
