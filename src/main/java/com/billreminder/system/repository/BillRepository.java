package com.billreminder.system.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.billreminder.system.model.Bill;

public interface BillRepository extends JpaRepository<Bill, Long> {

    // 1. Find all bills belonging to a specific user
    List<Bill> findByUserId(Long userId);

    /**
     * Finds bills that are DUE SOON (unpaid and due within the next 'days' from today).
     * @param today The current date.
     * @param reminderWindow The date N days from today (the cutoff).
     * @return List of Bills matching the criteria.
     */
    @Query("SELECT b FROM Bill b WHERE b.paid = false AND b.reminderSent = false AND b.dueDate BETWEEN :today AND :reminderWindow")
    List<Bill> findBillsDueSoon(@Param("today") LocalDate today, @Param("reminderWindow") LocalDate reminderWindow);

    /**
     * Finds bills that are OVERDUE (unpaid and due before today).
     * @param today The current date.
     * @return List of Bills matching the criteria.
     */
    List<Bill> findByPaidFalseAndDueDateBefore(LocalDate today);
    
    // Find bills by user and payment status, useful for reporting
    List<Bill> findByUserIdAndPaid(Long userId, boolean paidStatus);
}