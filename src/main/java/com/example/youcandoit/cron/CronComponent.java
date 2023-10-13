package com.example.youcandoit.cron;

import com.example.youcandoit.dto.DiyCountDto;
import com.example.youcandoit.entity.*;
import com.example.youcandoit.entity.Id.DiyCertifyId;
import com.example.youcandoit.firebase.FirebaseComponent;
import com.example.youcandoit.repository.*;
import com.example.youcandoit.schedule.service.ScheduleService;
import com.example.youcandoit.schedule.service.impl.ScheduleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.*;

@Component
public class CronComponent {
    MemberRepository memberRepository;
    GroupRepository groupRepository;
    GroupPersonRepository groupPersonRepository;
    PedometerRankingRepository pedometerRankingRepository;
    PedometerAccumulateRepository pedometerAccumulateRepository;
    DiyAccumulateRepository diyAccumulateRepository;
    DiyCertifyRepository diyCertifyRepository;
    ScheduleRepository scheduleRepository;
    ReminderRepository reminderRepository;
    StickerRepository stickerRepository;

    @Autowired
    public CronComponent(
            MemberRepository memberRepository,
            GroupRepository groupRepository,
            GroupPersonRepository groupPersonRepository,
            PedometerRankingRepository pedometerRankingRepository,
            PedometerAccumulateRepository pedometerAccumulateRepository,
            DiyAccumulateRepository diyAccumulateRepository,
            DiyCertifyRepository diyCertifyRepository,
            ScheduleRepository scheduleRepository,
            ReminderRepository reminderRepository,
            StickerRepository stickerRepository) {
        this.memberRepository = memberRepository;
        this.groupPersonRepository = groupPersonRepository;
        this.groupRepository = groupRepository;
        this.pedometerRankingRepository = pedometerRankingRepository;
        this.pedometerAccumulateRepository = pedometerAccumulateRepository;
        this.diyAccumulateRepository = diyAccumulateRepository;
        this.diyCertifyRepository = diyCertifyRepository;
        this.scheduleRepository = scheduleRepository;
        this.reminderRepository = reminderRepository;
        this.stickerRepository = stickerRepository;
    }

    /** 일정알림 만들기 */
    @Async
    @Scheduled(cron = "0 0/1 * * * *")
    public void ScheduleReminder() {
        // 현재 시간을 구함
        String now = LocalDate.now() + " " + LocalTime.now().toString().substring(0, 6) + "00";
        System.out.println("스케줄 리마인더 : " + now);
        System.out.println("실제 시간 : " + LocalTime.now());
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

                String content = "";

                if(entity.getScheduleStartDate().substring(11, 16).equals("00:00") &&
                    entity.getScheduleEndDate().substring(11, 16).equals("23:59")) {
                    // 알림 내용 생성
                    content = String.format("%d/%d (%s) %s 일정이 있습니다.",
                            Integer.parseInt(entity.getScheduleStartDate().substring(5, 7)),
                            Integer.parseInt(entity.getScheduleStartDate().substring(8, 10)),
                            week, entity.getScheduleTitle());
                } else {
                    String time = entity.getScheduleStartDate().substring(11, 13) + "시";
                    if(!entity.getScheduleStartDate().substring(14, 16).equals("00")) {
                        time += " " + entity.getScheduleStartDate().substring(14, 16) + "분";
                    }

                    // 알림 내용 생성
                    content = String.format("%d/%d (%s) [%s] %s 일정이 있습니다.",
                            Integer.parseInt(entity.getScheduleStartDate().substring(5, 7)),
                            Integer.parseInt(entity.getScheduleStartDate().substring(8, 10)),
                            week, time, entity.getScheduleTitle());
                }

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

                // 반복 일정이라면 일정 다시 생성
                if(!entity.getScheduleRepeat().equals("0")) {
                    LocalDateTime startDate = null;
                    LocalDateTime endDate = null;
                    switch (entity.getScheduleRepeat()) {
                        case "1":
                            startDate = LocalDateTime.parse(entity.getScheduleStartDate()).plusWeeks(1);
                            endDate = LocalDateTime.parse(entity.getScheduleStartDate()).plusWeeks(1);
                            break;
                        case "2":
                            startDate = LocalDateTime.parse(entity.getScheduleStartDate()).plusMonths(1);
                            endDate = LocalDateTime.parse(entity.getScheduleStartDate()).plusMonths(1);
                            break;
                        case "3":
                            startDate = LocalDateTime.parse(entity.getScheduleStartDate()).plusYears(1);
                            endDate = LocalDateTime.parse(entity.getScheduleStartDate()).plusYears(1);
                    }
                    entity.setScheduleStartDate(startDate.toString());
                    entity.setScheduleEndDate(endDate.toString());
                    scheduleRepository.save(entity);
                }

            }
        }
    }

    /** 1시간마다 랭킹 업데이트 */
    @Async
    @Scheduled(cron = "0 1 1-23/1 * * *")
//    @Scheduled(cron = "0/10 * * * * *")
    public void rankingUpdate() {
        Date today = Date.valueOf(LocalDate.now());

        // 일일랭킹 순위 업데이트
        System.out.println("일일랭킹 순위 업데이트");
        int previousGroupNum = 0; // 전 row의 그룹번호를 저장
        int previousPedoRes = 0; // 전 row의 만보기결과를 저장
        int ranking = 0; // 랭킹 저장
        List<PedometerRankingEntity> getRow = pedometerRankingRepository.findByPedometerDateOrderByGroupNumberAscPedometerResultDesc(today);
        for (PedometerRankingEntity row : getRow) {
            if (previousGroupNum != row.getGroupNumber()) { // 전 row와 그룹번호가 다르다면
                ranking = 0; // 랭킹 넘버링 초기화
                previousPedoRes = 0; // 만보기결과 초기화
            }

            if (previousPedoRes == row.getPedometerResult()) { // 전 row와 만보기 결과가 같다면
                row.setPedometerRank(ranking); // 같은 순위
                pedometerRankingRepository.save(row);
            } else { // 전 row와 만보기 결과가 다르다면
                row.setPedometerRank(++ranking); // 다음 순위
                pedometerRankingRepository.save(row);
            }

            previousGroupNum = row.getGroupNumber();
            previousPedoRes = row.getPedometerResult();
        }
    }

    /** 자정마다 데이터베이스 업데이트 */
    @Async
    @Scheduled(cron = "0 1 0 * * *")
//    @Scheduled(cron = "0/10 * * * * *")
    public void databaseUpdate() {
        Date today = Date.valueOf(LocalDate.now());
        Date yesterday = Date.valueOf(LocalDate.now().minusDays(1));

        // 갓생 일일랭킹 순위 업데이트
        System.out.println("일일랭킹 순위 업데이트");
        int previousGroupNum = 0; // 전 row의 그룹번호를 저장
        int previousPedoRes = 0; // 전 row의 만보기결과를 저장
        int ranking = 0; // 랭킹 저장
        List<PedometerRankingEntity> getRow = pedometerRankingRepository.findByPedometerDateOrderByGroupNumberAscPedometerResultDesc(yesterday);
        for(PedometerRankingEntity row : getRow) {
            if(previousGroupNum != row.getGroupNumber()) { // 전 row와 그룹번호가 다르다면
                ranking = 0; // 랭킹 넘버링 초기화
                previousPedoRes = 0; // 만보기결과 초기화
            }

            if(previousPedoRes == row.getPedometerResult()) // 전 row와 만보기 결과가 같다면
                row.setPedometerRank(ranking); // 같은 순위
            else // 전 row와 만보기 결과가 다르다면
                row.setPedometerRank(++ranking); // 다음 순위
            pedometerRankingRepository.save(row);

            previousGroupNum = row.getGroupNumber();
            previousPedoRes = row.getPedometerResult();

            // 일일랭킹 값을 누적랭킹에 반영
            System.out.println("일일랭킹 값을 누적랭킹에 반영");
            pedometerAccumulateRepository.updateAccumulate(row.getPedometerResult(), row.getGroupNumber(), row.getMemId());
        }

        // 갓생 누적랭킹 순위 업데이트
        System.out.println("누적랭킹 순위 업데이트");
        previousGroupNum = 0;
        previousPedoRes = 0;
        ranking = 0;
        List<PedometerAccumulateEntity> getRow2 = pedometerAccumulateRepository.findPedometerAccumulate();
        for(PedometerAccumulateEntity row : getRow2) {
            if(previousGroupNum != row.getGroupNumber()) { // 전 row와 그룹번호가 다르다면
                ranking = 0; // 랭킹 넘버링 초기화
                previousPedoRes = 0; // 만보기결과 초기화
            }

            if(previousPedoRes == row.getPedometerCount()) // 전 row와 만보기 결과가 같다면
                row.setPedoaccuRank(ranking); // 같은 순위
            else // 전 row와 만보기 결과가 다르다면
                row.setPedoaccuRank(++ranking); // 다음 순위
            pedometerAccumulateRepository.save(row);

            previousGroupNum = row.getGroupNumber();
            previousPedoRes = row.getPedometerCount();
        }

        // DIY 반대가 과반수를 넘은 인증 삭제
        System.out.println("DIY 반대가 과반수를 넘은 인증 삭제");
        List<DiyCertifyEntity> diyOppositeList = diyCertifyRepository.findMajorityOpposite();
        if(!diyOppositeList.isEmpty()) {
            for(DiyCertifyEntity row : diyOppositeList) {
                diyCertifyRepository.deleteById(new DiyCertifyId(row.getCertifyDate(), row.getGroupNumber(), row.getMemId()));
                diyAccumulateRepository.decreaseCertifyCount(row.getGroupNumber(), row.getMemId());
            }
        }

        // Diy 인증 랭킹에 반영
        System.out.println("diy 인증 랭킹 반영");
        List<DiyCertifyEntity> diyCertifyEntityList = diyCertifyRepository.findByCertifyDate(yesterday);
        for(DiyCertifyEntity row : diyCertifyEntityList) {
            diyAccumulateRepository.increaseCertifyCount(row.getGroupNumber(), row.getMemId());
        }

        // diy 랭킹 업데이트
        System.out.println("diy 랭킹 순위 업데이트");
        previousGroupNum = 0;
        previousPedoRes = 0;
        ranking = 0;
        List<DiyAccumulateEntity> getRow3 = diyAccumulateRepository.findDiyAccumulate();
        for(DiyAccumulateEntity row : getRow3) {
            if(previousGroupNum != row.getGroupNumber()) { // 전 row와 그룹번호가 다르다면
                ranking = 0; // 랭킹 넘버링 초기화
                previousPedoRes = 0; // 만보기결과 초기화
            }

            if(previousPedoRes == row.getCertifyCount()) // 전 row와 만보기 결과가 같다면
                row.setDiyaccuRank(ranking); // 같은 순위
            else // 전 row와 만보기 결과가 다르다면
                row.setDiyaccuRank(++ranking); // 다음 순위
            diyAccumulateRepository.save(row);

            previousGroupNum = row.getGroupNumber();
            previousPedoRes = row.getCertifyCount();
        }

        // sticker 생성
        System.out.println("sticker 생성");
        // 사용자 목록 불러오기
        List<MemberEntity> getRow4 = memberRepository.findAll();

        // 필요한 데이터들
        List<DiyCountDto> getRow5 = stickerRepository.findDiyCount();
        List<String> getRow6 = stickerRepository.findPedometerResult(yesterday);

        // 각 사용자들에 대해 총개수, 성공개수 확인
        int totalCount;
        int successCount;
        for(MemberEntity member : getRow4) {
            totalCount = 1; // 만보기 기본 목표
            successCount = 0;

            // 만약 목표달성한 사용자 목록에 있다면 성공개수 추가
            if(getRow6.contains(member.getMemId())) {
                successCount++;
            }

            // 만약 진행중인 diy챌린지가 있다면 챌린지 총 개수 추가
            for(DiyCountDto dto : getRow5) {
                if(dto.getMemId().equals(member.getMemId())) {
                    totalCount += dto.getCount();

                    Optional<Integer> count = stickerRepository.findDiySuccess(yesterday, member.getMemId());
                    successCount += count.get();
                    break;
                }
            }

            // 스티커 생성
            StickerEntity entity = StickerEntity.builder()
                    .stickerDate(yesterday)
                    .memId(member.getMemId())
                    .build();
            double success = (successCount / (double)totalCount);
            System.out.println(success);
            if(success < 0.5) {
                entity.setStickerColor("0");
            } else if(success < 1) {
                entity.setStickerColor("1");
            } else {
                entity.setStickerColor("2");
            }
            stickerRepository.save(entity);
        }

        // 기간이 지난 그룹 종료하기
        System.out.println("기간 지난 그룹 종료하기");
        groupRepository.updateGroupEnd(yesterday);


        System.out.println("기간이 된 그룹 시작(대기자 삭제), 시작한 그룹의 누적랭킹 row 생성");
        // 기간이 된 그룹 시작하기(대기자 삭제)
        groupPersonRepository.deleteGroupPerson(today);
        groupRepository.updateGroupStart(today);

        // 시작한 갓생 그룹의 누적랭킹 row 생성
        pedometerAccumulateRepository.insertPedometerAccumulate(today);

        // 시작한 diy 그룹의 랭킹 row 생성
        diyAccumulateRepository.insertDiyAccumulate(today);

        // 진행중인 그룹의 일일랭킹 row 생성
        System.out.println("진행중인 그룹의 일일랭킹 row 생성");
        pedometerRankingRepository.insertRanking(today);
    }
}


