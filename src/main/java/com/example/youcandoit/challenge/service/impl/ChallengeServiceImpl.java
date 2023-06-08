package com.example.youcandoit.challenge.service.impl;

import com.example.youcandoit.entity.*;
import com.example.youcandoit.repository.*;
import com.example.youcandoit.challenge.service.ChallengeService;
import com.example.youcandoit.dto.GodlifeChallengeDto;
import com.example.youcandoit.dto.GroupDto;
import com.example.youcandoit.dto.MemberDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChallengeServiceImpl implements ChallengeService {
    GroupRepository groupRepository;
    GroupPersonRepository groupPersonRepository;
    GodLifeChallengeRepository godLifeChallengeRepository;
    FriendRepository friendRepository;
    PedometerRankingRepository pedometerRankingRepository;
    PedometerAccumulateRepository pedometerAccumulateRepository;

    @Autowired
    public ChallengeServiceImpl(
            GodLifeChallengeRepository godLifeChallengeRepository,
            GroupRepository groupRepository,
            FriendRepository friendRepository,
            GroupPersonRepository groupPersonRepository,
            PedometerRankingRepository pedometerRankingRepository,
            PedometerAccumulateRepository pedometerAccumulateRepository) {
        this.groupPersonRepository = groupPersonRepository;
        this.godLifeChallengeRepository = godLifeChallengeRepository;
        this.groupRepository = groupRepository;
        this.friendRepository = friendRepository;
        this.pedometerRankingRepository = pedometerRankingRepository;
        this.pedometerAccumulateRepository = pedometerAccumulateRepository;
    }


    // 나의 랭킹
    @Override
    public List<Object[]> myRanking(String loginId) {
        Date date = Date.valueOf(LocalDate.now());
        List<Object[]> getRow = groupPersonRepository.findGroup(loginId, date);
        List<Object[]> myRankData = new ArrayList<Object[]>();
        for(Object[] row : getRow) {
            GroupDto groupDto = ((GroupEntity)row[0]).toDto();
            long dDay = (groupDto.getGroupEnddate().getTime() - date.getTime()) / (1000 * 60 * 60 * 24); // getTime()의 반환값이 밀리세컨드이기 때문에 계산을 해야 한다.
            // 랭킹, 남은날짜, 그룹이미지, 그룹주제, 그룹이름
            myRankData.add(new Object[]{groupDto.getGroupName(), groupDto.getGroupSubject(), groupDto.getGroupImage(), dDay, row[1]});
        }
        return myRankData;
    }

    // 갓생챌린지 일일랭킹
    @Override
    public List<Object[]> dailyRanking(String loginId) {
        Date date = Date.valueOf(LocalDate.now());
        List<GroupEntity> getRow = groupRepository.findRankingList(loginId);

        if(getRow.isEmpty()) {
            return null;
        }

        List<Object[]> rankingList = new ArrayList<Object[]>();
        for(GroupEntity row : getRow) {
            List<Object[]> getRow2 = groupRepository.findDailyRanking(row.getGroupNumber(), date);
            rankingList.add(new Object[]{row.getGroupNumber(), row.getGroupSubject(), getRow2});
        }
        return rankingList;
    }

    // 갓생챌린지 일일랭킹 상세
    @Override
    public List<Object> dailyRankingDetail(int groupNumber, Date date) {
        Optional<GroupEntity> getRow = groupRepository.findById(groupNumber);
        List<Object[]> getRow2 = groupRepository.findDailyRankingDetail(groupNumber, date);

        GroupDto groupDto = getRow.get().toDto();
        List<Object> ranking = new ArrayList<Object>();
        ranking.add(groupDto);
        ranking.add(getRow2);

        return ranking;
    }

    // 갓생챌린지 누적랭킹
    @Override
    public List<Object[]> accumulateRanking(String loginId) {
        List<GroupEntity> getRow = groupRepository.findRankingList(loginId);

        if(getRow.isEmpty()) {
            return null;
        }

        List<Object[]> rankingList = new ArrayList<Object[]>();
        for(GroupEntity row : getRow) {
            List<Object[]> getRow2 = groupRepository.findRanking(row.getGroupNumber());
            GroupDto groupDto = row.toDto();
            rankingList.add(new Object[]{groupDto.getGroupNumber(), groupDto.getGroupSubject(), getRow2});
        }
        return rankingList;
    }

    // 갓생챌린지 누적랭킹 상세
    @Override
    public List<Object> accumulateRankingDetail(int groupNumber) {
        Optional<GroupEntity> getRow = groupRepository.findById(groupNumber);
        List<Object[]> getRow2 = groupRepository.findRankingDetail(groupNumber);

        GroupDto groupDto = GroupDto.builder().groupSubject(getRow.get().getGroupSubject()).groupName(getRow.get().getGroupName()).build();
        List<Object> ranking = new ArrayList<Object>();
        ranking.add(groupDto);
        ranking.add(getRow2);

        return ranking;
    }

    // 예약된 챌린지
    @Override
    public List<GroupDto> challengeReservation(String loginId) {
        List<GroupEntity> getRow = groupRepository.findChallengeReservation(loginId);

        List<GroupDto> groups = new ArrayList<GroupDto>();
        for(GroupEntity row : getRow) {
            groups.add(GroupDto.builder()
                    .groupSubject(row.getGroupSubject())
                    .groupName(row.getGroupName())
                    .groupStartdate(row.getGroupStartdate())
                    .groupEnddate(row.getGroupEnddate())
                    .groupImage(row.getGroupImage())
                    .build());
        }
        return groups;
    }

    // 갓생 챌린지
    @Override
    public List<GodlifeChallengeDto> godLifeChallenge() {
        List<GodlifeChallengeEntity> getRow = godLifeChallengeRepository.findAll();

        List<GodlifeChallengeDto> godlifeList = new ArrayList<GodlifeChallengeDto>();
        for(GodlifeChallengeEntity row : getRow) {
            godlifeList.add(row.toDto());
        }

        return godlifeList;
    }

    // 갓생 챌린지 상세보기
    @Override
    public GodlifeChallengeDto godLifeChallengeDetail(String challengeSubject) {
        Optional<GodlifeChallengeEntity> getRow = godLifeChallengeRepository.findById(challengeSubject);

        return getRow.get().toDto();
    }

    // 갓생 챌린지 생성하기 > 함께할 친구 선택
    @Override
    public List<MemberDto> withFriend(String loginId) {
        List<MemberEntity> getRow = friendRepository.findFriendList(loginId);

        if(getRow.isEmpty()) { // 조회 결과가 없다
            return null;
        } else { // 조회 결과가 있다.
            List<MemberDto> friends = new ArrayList<MemberDto>();
            for (MemberEntity row : getRow) {
                friends.add(MemberDto.builder().memId(row.getMemId()).nickname(row.getNickname()).build());
            }
            return friends;
        }
    }

    // 갓생 챌린지 생성하기
    @Override
    public int saveGodlifeChallenge(GroupDto groupDto, String loginId, String[] members) {
        GroupEntity groupEntity = groupDto.toEntity();
        int groupNumber = groupRepository.save(groupEntity).getGroupNumber(); // 그룹 insert

        GroupPersonEntity groupPersonEntity = GroupPersonEntity.builder().groupNumber(groupNumber).build();
        for(String member : members) {
            groupPersonEntity.setMemId(member);
            groupPersonRepository.save(groupPersonEntity); // 그룹멤버 insert
        }

        groupPersonEntity.setMemId(loginId);
        groupPersonEntity.setPersonStatus("1");
        groupPersonRepository.save(groupPersonEntity);

        return groupNumber;
    }

    // 챌린지 이미지 저장
    @Override
    public void saveGroupImage(GroupDto groupDto) {
        Optional<GroupEntity> getRow = groupRepository.findById(groupDto.getGroupNumber());
        GroupEntity groupEntity = getRow.get();
        groupEntity.setGroupImage(groupDto.getGroupImage());

        groupRepository.save(groupEntity);
    }

    /*
    =======================================1시간마다 랭킹 업데이트===========================================
     */

    @Scheduled(cron = "0 2 1-23/1 * * *")
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

    @Scheduled(cron = "0 2 0 * * *")
//    @Scheduled(cron = "0/10 * * * * *")
    public void databaseUpdate() {
        Date today = Date.valueOf(LocalDate.now());
        Date yesterday = Date.valueOf(LocalDate.now().minusDays(1));

        // 일일랭킹 순위 업데이트
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

            if(previousPedoRes == row.getPedometerResult()) { // 전 row와 만보기 결과가 같다면
                row.setPedometerRank(ranking); // 같은 순위
                pedometerRankingRepository.save(row);
            } else { // 전 row와 만보기 결과가 다르다면
                row.setPedometerRank(++ranking); // 다음 순위
                pedometerRankingRepository.save(row);
            }

            previousGroupNum = row.getGroupNumber();
            previousPedoRes = row.getPedometerResult();

            // 일일랭킹 값을 누적랭킹에 반영
            System.out.println("일일랭킹 값을 누적랭킹에 반영");
            pedometerAccumulateRepository.updateAccumulate(row.getPedometerResult(), row.getGroupNumber(), row.getMemId());
        }

        // 누적랭킹 순위 업데이트
        System.out.println("누적랭킹 순위 업데이트");
        previousGroupNum = 0;
        previousPedoRes = 0;
        ranking = 0;
        List<PedometerAccumulateEntity> getRow2 = pedometerAccumulateRepository.findAccumulate();
        for(PedometerAccumulateEntity row : getRow2) {
            if(previousGroupNum != row.getGroupNumber()) { // 전 row와 그룹번호가 다르다면
                ranking = 0; // 랭킹 넘버링 초기화
                previousPedoRes = 0; // 만보기결과 초기화
            }

            if(previousPedoRes == row.getPedometerCount()) { // 전 row와 만보기 결과가 같다면
                row.setPedoaccuRank(ranking); // 같은 순위
                pedometerAccumulateRepository.save(row);
            } else { // 전 row와 만보기 결과가 다르다면
                row.setPedoaccuRank(++ranking); // 다음 순위
                pedometerAccumulateRepository.save(row);
            }

            previousGroupNum = row.getGroupNumber();
            previousPedoRes = row.getPedometerCount();
        }

        // 기간이 지난 그룹 종료하기
        System.out.println("기간 지난 그룹 종료하기");
        groupRepository.updateGroupEnd(yesterday);

        // 기간이 된 그룹 시작하기(대기자 삭제), 시작한 그룹의 누적랭킹 row 생성
        System.out.println("기간이 된 그룹 시작(대기자 삭제), 시작한 그룹의 누적랭킹 row 생성");
        groupPersonRepository.deleteGroupPerson(today);
        groupRepository.updateGroupStart(today);
        pedometerAccumulateRepository.insertAccumulate(today);

        // 진행중인 그룹의 일일랭킹 row 생성
        System.out.println("진행중인 그룹의 일일랭킹 row 생성");
        List<GroupPersonEntity> getRow3 = groupPersonRepository.findInsertRanking();
        for(GroupPersonEntity row : getRow3) {
            PedometerRankingEntity pedometerRankingEntity = PedometerRankingEntity.builder()
                    .pedometerDate(today)
                    .groupNumber(row.getGroupNumber())
                    .memId(row.getMemId())
                    .pedometerResult(0)
                    .pedometerRank(0)
                    .build();
            pedometerRankingRepository.save(pedometerRankingEntity);
        }
    }
}
