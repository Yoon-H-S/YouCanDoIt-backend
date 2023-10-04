package com.example.youcandoit.reminder.service.impl;

import com.example.youcandoit.entity.ReminderEntity;
import com.example.youcandoit.reminder.service.ReminderService;
import com.example.youcandoit.repository.MemberRepository;
import com.example.youcandoit.repository.ReminderRepository;
import com.example.youcandoit.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReminderServiceImpl implements ReminderService {
    ReminderRepository reminderRepository;
    ScheduleRepository scheduleRepository;
    MemberRepository memberRepository;

    @Autowired
    public ReminderServiceImpl(ReminderRepository reminderRepository, ScheduleRepository scheduleRepository, MemberRepository memberRepository) {
        this.reminderRepository = reminderRepository;
        this.scheduleRepository = scheduleRepository;
        this.memberRepository = memberRepository;
    }

    /** 지난 알림 조회 */
    @Override
    public List<String> reminderList(String loginId) {
        List<ReminderEntity> getRow = reminderRepository.findByMemIdOrderByReminderDateDesc(loginId);

        List<String> reminders = new ArrayList<>();
        for(ReminderEntity entity : getRow) {
            reminders.add(entity.getReminderContents());
        }

        return reminders;
    }
}
