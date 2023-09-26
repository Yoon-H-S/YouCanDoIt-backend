package com.example.youcandoit.reminder.controller;

import com.example.youcandoit.reminder.service.ReminderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/reminder-api")
public class ReminderController {

    ReminderService reminderService;

    @Autowired
    public ReminderController(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    /** 지난 알림 조회 */
    @PostMapping("/reminder-list")
    public List<String> reminderList(HttpSession session) {
        String loginId = (String)session.getAttribute("loginId");
        return reminderService.reminderList(loginId);
    }
}
