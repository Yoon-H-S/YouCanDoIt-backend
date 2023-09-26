package com.example.youcandoit.firebase;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FirebaseComponent {
    FirebaseAdmin admin;
    public FirebaseComponent() {
        admin = new FirebaseAdmin();
    }

    public void reminderToOne(String token, String title, String content) {
        System.out.println("개인 알림 전송");
        admin.sendMessage(token, title, content);
    }

    public void reminderToGroup(List<String> tokenList, String title, String content) {
        System.out.println("단체 알림 전송");
        admin.sendGroupMessage(tokenList, title, content);
    }

    @Scheduled(cron = "0 0 1-23/1 * * *")
    public void pedometerUpdate() {
        System.out.println("만보기값 업데이트");
        admin.sendPedometerUpdate("0");
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void pedometerUpdateAtMidnight() {
        System.out.println("만보기값 업데이트(자정)");
        FirebaseAdmin admin = new FirebaseAdmin();
        admin.sendPedometerUpdate("1");
    }
}
