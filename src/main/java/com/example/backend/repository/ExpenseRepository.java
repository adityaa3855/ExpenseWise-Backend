package com.example.backend.repository;

import com.example.backend.entity.Expense;
import com.example.backend.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByUser(User user);

    List<Expense> findByUserOrderByDateDesc(User user, Pageable pageable);

}