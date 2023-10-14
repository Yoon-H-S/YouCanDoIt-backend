package com.example.youcandoit.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

import static com.google.firebase.messaging.AndroidConfig.Priority.HIGH;

public class FirebaseAdmin {

    public FirebaseAdmin() {
        try {
            if(FirebaseApp.getApps().isEmpty()){
                InputStream serviceAccount = new ClassPathResource("firebase_admin.json").getInputStream();
                FirebaseOptions options = new FirebaseOptions.Builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();
                FirebaseApp.initializeApp(options);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 개인 알림 전송 */
    public void sendMessage(String token, String title, String content) {
        try {
            Message message = Message.builder()
                    .putData("title", title)
                    .putData("content", content)
                    .setToken(token)
                    .setAndroidConfig(AndroidConfig.builder().setPriority(HIGH).build())
                    .setApnsConfig(ApnsConfig.builder().putHeader("apns-priority", "10").build())
                    .setWebpushConfig(WebpushConfig.builder().putHeader("Urgency", "high").build())
                    .build();

            FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            throw new RuntimeException(e);
        }
    }

    /** 단체 알림 전송 */
    public void sendGroupMessage(List<String> tokenList, String title, String content) {
        try {
            MulticastMessage message = MulticastMessage.builder()
                    .putData("title", title)
                    .putData("content", content)
                    .addAllTokens(tokenList)
                    .setAndroidConfig(AndroidConfig.builder().setPriority(HIGH).build())
                    .setApnsConfig(ApnsConfig.builder().putHeader("apns-priority", "10").build())
                    .setWebpushConfig(WebpushConfig.builder().putHeader("Urgency", "high").build())
                    .build();

            FirebaseMessaging.getInstance().sendMulticast(message);
        } catch (FirebaseMessagingException e) {
            throw new RuntimeException(e);
        }
    }

    /** 만보기값 업데이트 요청 */
    public void sendPedometerUpdate(String isLast) {
        try {
            String date = LocalDate.now().toString();

            Message message = Message.builder()
                    .putData("title", "pedometerUpdate")
                    .putData("date",date)
                    .putData("isLast", isLast)
                    .setTopic("pedometerUpdate")
                    .setAndroidConfig(AndroidConfig.builder().setPriority(HIGH).build())
                    .setApnsConfig(ApnsConfig.builder().putHeader("apns-priority", "10").build())
                    .setWebpushConfig(WebpushConfig.builder().putHeader("Urgency", "high").build())
                    .build();

            FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
