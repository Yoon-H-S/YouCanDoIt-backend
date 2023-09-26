package com.example.youcandoit.repository;

import com.example.youcandoit.entity.ReminderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReminderRepository extends JpaRepository<ReminderEntity, Integer> {
    List<ReminderEntity> findByMemIdOrderByReminderDateDesc(String id);
}
