package com.example.youcandoit.challenge.repository;

import com.example.youcandoit.entity.GroupEntity;
import com.example.youcandoit.entity.Id.PedometerRankingId;
import com.example.youcandoit.entity.MemberEntity;
import com.example.youcandoit.entity.PedometerRankingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DailyRankingRepository extends JpaRepository<PedometerRankingEntity, PedometerRankingId> {

    //갓생 챌린지 일일랭킹
    @Query(value = "select m,g from PedometerRankingEntity p join MemberEntity m on p.memId = m.memId join GroupEntity g on p.groupNumber = g.groupNumber " +
            "where g.groupSubject=:groupSubject and g.groupHeadcount=:groupHeadcount and m.profilePicture=profilePicture ")
    List<MemberEntity> MemberJoin(@Param("loginId")String loginId);

    @Query(value = "select g from PedometerRankingEntity p join GroupEntity g on p.groupNumber = g.groupNumber" +
            " where p.groupNumber=:groupNumber and g.groupSubject=:groupSubject and g.groupHeadcount    =:groupHeadcount")
    List<GroupEntity> groupJoin(@Param("groupNumber")Integer groupNumber, @Param("groupSubject")String groupSubject,
                                @Param("groupHeadcount")String groupHeadcount);
}
