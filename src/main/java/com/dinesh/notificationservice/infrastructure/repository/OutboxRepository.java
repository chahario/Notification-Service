package com.dinesh.notificationservice.infrastructure.repository;

import com.dinesh.notificationservice.domain.model.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface OutboxRepository extends JpaRepository<OutboxEvent, Integer>{
    List<OutboxEvent> findTop100ByStatusOrderByCreatedAt(String status);
}

