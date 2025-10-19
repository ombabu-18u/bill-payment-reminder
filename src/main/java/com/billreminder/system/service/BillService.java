package com.billreminder.system.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.billreminder.system.exception.ResourceNotFoundException;
import com.billreminder.system.model.Bill;
import com.billreminder.system.model.User;
import com.billreminder.system.repository.BillRepository;

@Service
public class BillService {

    private final BillRepository billRepository;
    private final UserService userService;
    private final BillCategoryService categoryService;

    @Autowired
    public BillService(BillRepository billRepository, UserService userService, BillCategoryService categoryService) {
        this.billRepository = billRepository;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    // --- CRUD Operations ---
    
    public Bill createBill(Long userId, Long categoryId, Bill bill) {
        // 1. Validate User and Category existence
        User user = userService.getUserById(userId);
        bill.setUser(user);
        
        bill.setCategory(categoryService.getCategoryById(categoryId));
        
        // 2. Default statuses
        bill.setPaid(false);
        bill.setReminderSent(false);

        return billRepository.save(bill);
    }
    
    public List<Bill> getUserBills(Long userId) {
        // Validation for user is handled by repository query (implicitly) and will be handled by security later.
        // For now, assume user exists if called from a valid context.
        return billRepository.findByUserId(userId);
    }

    public Bill getBillById(Long billId) {
        return billRepository.findById(billId)
                .orElseThrow(() -> new ResourceNotFoundException("Bill", "Id", billId));
    }

    // --- Core Business Logic ---

    // 1. Payment Tracking
    public Bill markBillAsPaid(Long billId) {
        Bill bill = getBillById(billId);
        if (bill.isPaid()) {
            // Already paid, maybe log a warning or return early
            return bill; 
        }
        bill.setPaid(true);
        return billRepository.save(bill);
    }
    
    // 2. Reminder Logic (for Controller/Reporting)
    
    /**
     * Finds bills due within the next N days (for user dashboards).
     * @param userId The ID of the user.
     * @param days The number of days in the reminder window (e.g., 7).
     * @return List of upcoming unpaid bills.
     */
    public List<Bill> findUpcomingBills(Long userId, int days) {
        // This is a simple in-memory filter of all user bills.
        // For a large database, we would ideally pass the date filter directly to the repository query.
        LocalDate today = LocalDate.now();
        LocalDate reminderWindow = today.plusDays(days);
        
        return billRepository.findByUserId(userId).stream()
                .filter(b -> !b.isPaid() && !b.getDueDate().isBefore(today) && !b.getDueDate().isAfter(reminderWindow))
                .toList();
    }
    
    /**
     * Finds bills that are overdue (for user dashboards).
     * @param userId The ID of the user.
     * @return List of overdue bills.
     */
    public List<Bill> findOverdueBills(Long userId) {
        LocalDate today = LocalDate.now();
        
        return billRepository.findByUserId(userId).stream()
                .filter(b -> !b.isPaid() && b.getDueDate().isBefore(today))
                .toList();
    }
}