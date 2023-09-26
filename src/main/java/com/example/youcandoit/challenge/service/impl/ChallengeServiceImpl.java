package com.example.youcandoit.challenge.service.impl;

import com.example.youcandoit.dto.*;
import com.example.youcandoit.entity.*;
import com.example.youcandoit.entity.Id.DiyCertifyId;
import com.example.youcandoit.entity.Id.OppositeId;
import com.example.youcandoit.firebase.FirebaseComponent;
import com.example.youcandoit.repository.*;
import com.example.youcandoit.challenge.service.ChallengeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class ChallengeServiceImpl implements ChallengeService {
    GroupRepository groupRepository;
    GroupPersonRepository groupPersonRepository;
    GodLifeChallengeRepository godLifeChallengeRepository;
    FriendRepository friendRepository;
    PedometerRankingRepository pedometerRankingRepository;
    PedometerAccumulateRepository pedometerAccumulateRepository;
    DiyAccumulateRepository diyAccumulateRepository;
    DiyCertifyRepository diyCertifyRepository;
    OppositeRepository oppositeRepository;
    MemberRepository memberRepository;
    ReminderRepository reminderRepository;

    @Autowired
    public ChallengeServiceImpl(
            GodLifeChallengeRepository godLifeChallengeRepository,
            GroupRepository groupRepository,
            FriendRepository friendRepository,
            GroupPersonRepository groupPersonRepository,
            PedometerRankingRepository pedometerRankingRepository,
            PedometerAccumulateRepository pedometerAccumulateRepository,
            DiyAccumulateRepository diyAccumulateRepository,
            DiyCertifyRepository diyCertifyRepository,
            OppositeRepository oppositeRepository,
            MemberRepository memberRepository,
            ReminderRepository reminderRepository) {
        this.groupPersonRepository = groupPersonRepository;
        this.godLifeChallengeRepository = godLifeChallengeRepository;
        this.groupRepository = groupRepository;
        this.friendRepository = friendRepository;
        this.pedometerRankingRepository = pedometerRankingRepository;
        this.pedometerAccumulateRepository = pedometerAccumulateRepository;
        this.diyAccumulateRepository = diyAccumulateRepository;
        this.diyCertifyRepository = diyCertifyRepository;
        this.oppositeRepository = oppositeRepository;
        this.memberRepository = memberRepository;
        this.reminderRepository = reminderRepository;
    }


    /** 나의 랭킹 */
    @Override
    public List<Object[]> myRanking(String loginId) {
        Date date = Date.valueOf(LocalDate.now());
        List<Object[]> getRow = pedometerRankingRepository.findMyRankingList(loginId, date);
        List<Object[]> myRankData = new ArrayList<Object[]>();
        for(Object[] row : getRow) {
            GroupDto groupDto = ((GroupEntity)row[0]).toDto();
            long dDay = (groupDto.getGroupEnddate().getTime() - date.getTime()) / (1000 * 60 * 60 * 24); // getTime()의 반환값이 밀리세컨드이기 때문에 계산을 해야 한다.
            // 랭킹, 남은날짜, 그룹이미지, 그룹주제, 그룹이름
            myRankData.add(new Object[]{groupDto.getGroupName(), groupDto.getGroupSubject(), groupDto.getGroupImage(), dDay, row[1]});
        }
        return myRankData;
    }

    /** 챌린지 랭킹 리스트 조회 */
    @Override
    public List<Object[]> challengeRanking(String loginId, String rankingType) {
        System.out.println(rankingType);
        List<GroupEntity> getRow;
        switch (rankingType) {
            case "1": case "2":
                getRow = groupRepository.findInProgressGroupList(loginId, "1");
                break;
            case "3":
                getRow = groupRepository.findInProgressGroupList(loginId, "2");
                break;
            case "4": default:
                getRow = groupRepository.findEndGroupList(loginId, new String[]{"1", "2"});
                break;
        }

        if(getRow.isEmpty()) {
            return null;
        }

        Date date = Date.valueOf(LocalDate.now());
        List<Object[]> rankingList = new ArrayList<Object[]>();
        List<Object[]> getRow2 = null;

        for(GroupEntity row : getRow) {
            switch (rankingType) {
                case "1":
                    getRow2 = pedometerRankingRepository.findDailyRanking(row.getGroupNumber(), date);
                    break;
                case "2":
                    getRow2 = pedometerAccumulateRepository.findGodLifeRanking(row.getGroupNumber());
                    break;
                case "3":
                    getRow2 = diyAccumulateRepository.findDiyRanking(row.getGroupNumber());
                    break;
                case "4": default:
                    if(row.getGroupClass().equals("1"))
                        getRow2 = pedometerAccumulateRepository.findGodLifeRanking(row.getGroupNumber());
                    else
                        getRow2 = diyAccumulateRepository.findDiyRanking(row.getGroupNumber());
                    break;
            }
            GroupDto groupDto = row.toDto();
            rankingList.add(new Object[]{groupDto.getGroupNumber(), groupDto.getGroupSubject(), getRow2});
        }
        return rankingList;
    }

    /** 랭킹 상세 조회 */
    @Override
    public List<Object> rankingDetail(String rankingType, int groupNumber, Date date) {
        switch (rankingType) {
            case "1":
                return dailyRankingDetail(groupNumber, date);
            case "2":
                return GodLifeRankingDetail(groupNumber);
            case "3":
                return diyRankingDetail(groupNumber);
            case "4": default:
                Optional<GroupEntity> groupEntity = groupRepository.findById(groupNumber);
                if(groupEntity.isPresent()) {
                    if(groupEntity.get().getGroupClass().equals("1"))
                        return GodLifeRankingDetail(groupNumber);
                    else
                        return diyRankingDetail(groupNumber);
                }
                return null;
        }
    }

    /** 갓생챌린지 일일랭킹 상세 */
    public List<Object> dailyRankingDetail(int groupNumber, Date date) {
        Optional<GroupEntity> getRow = groupRepository.findById(groupNumber);
        List<Object[]> getRow2 = pedometerRankingRepository.findDailyRankingDetail(groupNumber, date);

        GroupDto groupDto = getRow.get().toDto();
        List<Object> ranking = new ArrayList<Object>();
        ranking.add(groupDto);
        ranking.add(getRow2);

        return ranking;
    }

    /** 갓생챌린지 누적랭킹 상세 */
    public List<Object> GodLifeRankingDetail(int groupNumber) {
        Optional<GroupEntity> getRow = groupRepository.findById(groupNumber);
        List<Object[]> getRow2 = pedometerAccumulateRepository.findGodLifeRankingDetail(groupNumber);

        GroupDto groupDto = getRow.get().toDto();
        List<Object> ranking = new ArrayList<Object>();
        ranking.add(groupDto);
        ranking.add(getRow2);

        return ranking;
    }

    /** DIY 챌린지 랭킹 상세 */
    public List<Object> diyRankingDetail(int groupNumber) {
        Optional<GroupEntity> getRow = groupRepository.findById(groupNumber);
        List<Object[]> getRow2 = diyAccumulateRepository.findDiyRankingDetail(groupNumber);

        GroupDto groupDto = getRow.get().toDto();
        List<Object> ranking = new ArrayList<Object>();
        ranking.add(groupDto);
        ranking.add(getRow2);

        return ranking;
    }

    /** diy 갤러리 리스트 조회 */
    @Override
    public List<Object[]> diyGallery(int groupNumber, String memId, String loginId) {
        Date today = Date.valueOf(LocalDate.now());
        List<DiyCertifyEntity> getRow = null;
        if(memId == null) {
            getRow = diyCertifyRepository.findByGroupNumberAndCertifyDateBeforeOrderByCertifyDateDesc(groupNumber, today);
        } else {
            getRow = diyCertifyRepository.findByGroupNumberAndMemIdAndCertifyDateBeforeOrderByCertifyDateDesc(groupNumber, memId, today);
        }
        List<Object[]> objectList = new ArrayList<Object[]>();
        for(DiyCertifyEntity row : getRow) {
            OppositeId oppositeId = new OppositeId(row.getCertifyDate(), row.getGroupNumber(), row.getMemId(), loginId);
            objectList.add(new Object[]{row.toDto(), oppositeRepository.existsById(oppositeId)});
        }
        return objectList;
    }

    /** diy 인증 반대 */
    @Override
    public int diyOpposite(OppositeDto oppositeDto) {
        Date today = Date.valueOf(LocalDate.now());
        Date certifyDay = oppositeDto.getCertifyDate();
        int diffDate = (int)((today.getTime() - certifyDay.getTime()) / (1000*60*60*24))+1;
        // 반대 기간이 지난경우
        if(diffDate > 7) {
            return diffDate;
        }

        OppositeEntity oppositeEntity = oppositeDto.toEntity();
        try {
            // 인증 반대
            oppositeRepository.save(oppositeEntity);
            diyCertifyRepository.updateOppositeCount(oppositeEntity.getCertifyDate(), oppositeEntity.getGroupNumber(), oppositeEntity.getMemId());

            // 인증에 대한 반대가 처음이라면
            DiyCertifyId diyCertifyId = new DiyCertifyId(oppositeEntity.getCertifyDate(), oppositeEntity.getGroupNumber(), oppositeEntity.getMemId());
            int count = diyCertifyRepository.findById(diyCertifyId).get().getOppositeCount();
            if(count == 1) {
                // 알림 관련
                String now = LocalDate.now() + " " + LocalTime.now().toString().substring(0, 6) + "00";
                String subject = groupRepository.findById(oppositeEntity.getGroupNumber()).get().getGroupSubject();
                String nickname = memberRepository.findById(oppositeEntity.getMemId()).get().getNickname();

                // 요일 구하기
                LocalDate date = LocalDate.now();
                String week = date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN);

                String content = String.format("%d/%d (%s) %s 챌린지 %s의 인증이 반대되었습니다. 반대 기간이 %d일 남았습니다. 투표에 참여해주세요",
                        Integer.parseInt(date.toString().substring(5, 7)),
                        Integer.parseInt(date.toString().substring(8, 10)),
                        week, subject, nickname, 7-diffDate);

                List<MemberEntity> getRow = groupPersonRepository.findGroupMemberInfo(oppositeEntity.getGroupNumber(), oppositeEntity.getOppositeId());

                List<String> tokenList = new ArrayList<>();
                for(MemberEntity entity : getRow) {
                    String token = memberRepository.findById(entity.getMemId()).get().getMobileToken();
                    if(token != null)
                        tokenList.add(token); // 멤버 토큰 획득

                    // 알림 저장
                    ReminderEntity reminder = ReminderEntity.builder()
                            .reminderNumber(0)
                            .memId(entity.getMemId())
                            .reminderContents(content)
                            .reminderDate(now)
                            .build();
                    reminderRepository.save(reminder);
                }
                // 알림 전송
                if(!tokenList.isEmpty())
                    new FirebaseComponent().reminderToGroup(tokenList, "반대 투표", content);
            }
            return diffDate;
        } catch (Exception e) {
            System.out.println(e);
            return 0;
        }
    }

    /** 예약된 챌린지 */
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

    /** 갓생 챌린지 */
    @Override
    public List<GodlifeChallengeDto> godLifeChallenge() {
        List<GodlifeChallengeEntity> getRow = godLifeChallengeRepository.findAll();

        List<GodlifeChallengeDto> godlifeList = new ArrayList<GodlifeChallengeDto>();
        for(GodlifeChallengeEntity row : getRow) {
            godlifeList.add(row.toDto());
        }

        return godlifeList;
    }

    /** 갓생 챌린지 상세보기 */
    @Override
    public GodlifeChallengeDto godLifeChallengeDetail(String challengeSubject) {
        Optional<GodlifeChallengeEntity> getRow = godLifeChallengeRepository.findById(challengeSubject);

        return getRow.get().toDto();
    }

    /** 챌린지 생성하기 > 함께할 친구 선택 */
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

    /** 챌린지 생성하기 */
    @Override
    public int saveGodLifeChallenge(GroupDto groupDto, String loginId, String[] members) {
        GroupEntity groupEntity = groupDto.toEntity();
        int groupNumber = groupRepository.save(groupEntity).getGroupNumber(); // 그룹 insert

        // 알림 관련
        List<String> tokenList = new ArrayList<>();
        String content = groupEntity.getGroupSubject() + " 그룹 초대가 왔습니다.";
        String now = LocalDate.now() + " " + LocalTime.now().toString().substring(0, 6) + "00";

        // 멤버 초대
        GroupPersonEntity groupPersonEntity = GroupPersonEntity.builder().groupNumber(groupNumber).build();
        for(String member : members) {
            groupPersonEntity.setMemId(member);
            groupPersonRepository.save(groupPersonEntity); // 그룹멤버 insert

            String token = memberRepository.findById(member).get().getMobileToken();
            if(token != null)
                tokenList.add(token); // 멤버 토큰 획득

            // 알림 저장
            ReminderEntity reminder = ReminderEntity.builder()
                    .reminderNumber(0)
                    .memId(member)
                    .reminderContents(content)
                    .reminderDate(now)
                    .build();
            reminderRepository.save(reminder);
        }

        // 알림 전송
        if(!tokenList.isEmpty())
            new FirebaseComponent().reminderToGroup(tokenList, "그룹 초대", content);

        groupPersonEntity.setMemId(loginId);
        groupPersonEntity.setPersonStatus("1");
        groupPersonRepository.save(groupPersonEntity);

        return groupNumber;
    }

    /** 챌린지 이미지 저장 */
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
