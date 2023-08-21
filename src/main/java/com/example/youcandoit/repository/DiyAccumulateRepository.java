package com.example.youcandoit.repository;

import com.example.youcandoit.entity.DiyAccumulateEntity;
import com.example.youcandoit.entity.Id.DiyAccumulateId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DiyAccumulateRepository extends JpaRepository<DiyAccumulateEntity, DiyAccumulateId> {

    /** diy 챌린지 리스트의 순위 */
    @Query(value = "select m.profilePicture, a.certifyCount from GroupPersonEntity p " +
            "join DiyAccumulateEntity a on a.groupNumber = p.groupNumber and a.memId = p.memId " +
            "join MemberEntity m on m.memId = p.memId " +
            "where p.groupNumber=:groupNumber " +
            "order by a.diyaccuRank limit 3")
    List<Object[]> findDiyRanking(@Param("groupNumber")int groupNumber);

    /** DIY 챌린지 랭킹 상세의 순위 */
    @Query(value = "select m.memId, m.nickname, m.profilePicture, a.certifyCount, a.diyaccuRank from DiyAccumulateEntity a " +
            "join GroupPersonEntity p on a.groupNumber = p.groupNumber and a.memId = p.memId " +
            "join MemberEntity m on m.memId = p.memId " +
            "where p.groupNumber=:groupNumber " +
            "order by a.diyaccuRank")
    List<Object[]> findDiyRankingDetail(@Param("groupNumber")int groupNumber);

}
