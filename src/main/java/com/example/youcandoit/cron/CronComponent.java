package com.example.youcandoit.cron;

import com.example.youcandoit.entity.*;
import com.example.youcandoit.entity.Id.DiyCertifyId;
import com.example.youcandoit.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Component
public class CronComponent {
    GroupRepository groupRepository;
    GroupPersonRepository groupPersonRepository;
    GodLifeChallengeRepository godLifeChallengeRepository;
    FriendRepository friendRepository;
    PedometerRankingRepository pedometerRankingRepository;
    PedometerAccumulateRepository pedometerAccumulateRepository;
    DiyAccumulateRepository diyAccumulateRepository;
    DiyCertifyRepository diyCertifyRepository;
    OppositeRepository oppositeRepository;
    StickerRepository stickerRepository;

    @Autowired
    public CronComponent(
            GodLifeChallengeRepository godLifeChallengeRepository,
            GroupRepository groupRepository,
            FriendRepository friendRepository,
            GroupPersonRepository groupPersonRepository,
            PedometerRankingRepository pedometerRankingRepository,
            PedometerAccumulateRepository pedometerAccumulateRepository,
            DiyAccumulateRepository diyAccumulateRepository,
            DiyCertifyRepository diyCertifyRepository,
            OppositeRepository oppositeRepository,
            StickerRepository stickerRepository) {
        this.groupPersonRepository = groupPersonRepository;
        this.godLifeChallengeRepository = godLifeChallengeRepository;
        this.groupRepository = groupRepository;
        this.friendRepository = friendRepository;
        this.pedometerRankingRepository = pedometerRankingRepository;
        this.pedometerAccumulateRepository = pedometerAccumulateRepository;
        this.diyAccumulateRepository = diyAccumulateRepository;
        this.diyCertifyRepository = diyCertifyRepository;
        this.oppositeRepository = oppositeRepository;
        this.stickerRepository = stickerRepository;
    }

    /*
    =======================================1시간마다 랭킹 업데이트===========================================
     */

    /** 1시간마다 랭킹 업데이트 */
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

    /*
    =======================================자정마다 데이터베이스 업데이트===========================================
     */

    /** 자정마다 데이터베이스 업데이트 */
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

        // sticker 카운트 계산
//        System.out.println("sticker 카운트 계산");
//        int totalCount = 1;
//        int successCount = 0;
//        List<MemberEntity> getRow4 = MemberRepository.findByAll();
//        List<Objects[]> getRow5 = stickerRepository.findDiyCount();
//        List<Objects[]> getRow6 = stickerRepository.findPedometerResult(yesterday);
//        if()


//        stickerRepository.insertSticker(yesterday);

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
