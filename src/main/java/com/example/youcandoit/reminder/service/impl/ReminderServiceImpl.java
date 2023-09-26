package com.example.youcandoit.reminder.service.impl;

import com.example.youcandoit.entity.MemberEntity;
import com.example.youcandoit.entity.ReminderEntity;
import com.example.youcandoit.entity.ScheduleEntity;
import com.example.youcandoit.firebase.FirebaseComponent;
import com.example.youcandoit.reminder.service.ReminderService;
import com.example.youcandoit.repository.MemberRepository;
import com.example.youcandoit.repository.ReminderRepository;
import com.example.youcandoit.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

    /** 일정알림 만들기 */
    @Scheduled(cron = "0 0/1 * * * *")
    public void ScheduleReminder() {
        // 현재 시간을 구함
        String now = LocalDate.now() + " " + LocalTime.now().toString().substring(0, 6) + "00";
        System.out.println("스케줄 리마인더 : " + now);
        // 알림을 전송해야할 일정이 있는지 확인
        List<ScheduleEntity> scheduleEntityList = scheduleRepository.findScheduleToReminder(now);
        // 있다면
        if(!scheduleEntityList.isEmpty()) {
            System.out.println("전송해야할 알림이 있음.");
            // 알림 전송을 위해 파이어베이스와 연결
            FirebaseComponent fc = new FirebaseComponent();
            for(ScheduleEntity entity : scheduleEntityList) {
                // 요일 구하기
                LocalDate date = LocalDate.parse(entity.getScheduleStartDate().substring(0, 10));
                String week = date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN);

                String time = entity.getScheduleStartDate().substring(11, 13) + "시";
                if(!entity.getScheduleStartDate().substring(14, 16).equals("00")) {
                    time += " " + entity.getScheduleStartDate().substring(14, 16) + "분";
                }

                // 알림 내용 생성
                String content = String.format("%d/%d (%s) [%s] %s 일정이 있습니다.",
                        Integer.parseInt(entity.getScheduleStartDate().substring(5, 7)),
                        Integer.parseInt(entity.getScheduleStartDate().substring(8, 10)),
                        week, time, entity.getScheduleTitle());

                // 알림 저장
                ReminderEntity reminder = ReminderEntity.builder()
                        .reminderNumber(0)
                        .memId(entity.getMemId())
                        .reminderContents(content)
                        .reminderDate(now)
                        .build();
                reminderRepository.save(reminder);

                // 토큰 가져오기
                String token = memberRepository.findById(entity.getMemId()).get().getMobileToken();

                // 알림 전송
                if(token != null)
                    fc.reminderToOne(token, "일정", content);
            }
        }
    }
}
