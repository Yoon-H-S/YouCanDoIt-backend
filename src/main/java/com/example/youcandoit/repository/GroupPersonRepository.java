package com.example.youcandoit.repository;


import com.example.youcandoit.entity.GroupEntity;
import com.example.youcandoit.entity.GroupPersonEntity;
import com.example.youcandoit.entity.Id.GroupPersonId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface GroupPersonRepository extends JpaRepository<GroupPersonEntity, GroupPersonId> { // <>안에는 Entity, Primarykey를 넣어준다.
    @Query(value = "select g, r.pedometerRank from GroupPersonEntity p " +
            "join GroupEntity g on p.groupNumber = g.groupNumber " +
            "join PedometerRankingEntity r on p.groupNumber = r.groupNumber and p.memId = r.memId " +
            "where r.pedometerDate=:date and r.memId=:loginId and g.groupState=2 " +
            "order by groupEnddate")
    List<Object[]> findGroup(@Param("loginId")String loginId, @Param("date")Date date);
}
