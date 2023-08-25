package com.example.youcandoit.repository;

import com.example.youcandoit.entity.DiyCertifyEntity;
import com.example.youcandoit.entity.Id.DiyCertifyId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface DiyCertifyRepository extends JpaRepository<DiyCertifyEntity, DiyCertifyId> {
    /** 그룹번호로 갤러리 조회 */
    List<DiyCertifyEntity> findByGroupNumberAndCertifyDateBeforeOrderByCertifyDateDesc(int groupNumber, Date today);

    /** 회원아이디로 갤러리 조회 */
    List<DiyCertifyEntity> findByGroupNumberAndMemIdAndCertifyDateBeforeOrderByCertifyDateDesc(int groupNumber, String memId, Date today);

    /** 반대 수 증가 */
    @Transactional
    @Modifying
    @Query(value = "update DiyCertifyEntity set oppositeCount=oppositeCount+1 " +
            "where certifyDate=:certifyDate and groupNumber=:groupNumber and memId=:memId")
    void updateOppositeCount(@Param("certifyDate")Date certifyDate, @Param("groupNumber")int groupNumber, @Param("memId")String memId);

    /*
    =========================================자정에 동작하는 데이터베이스 업데이트=========================================================
     */

    /** 반대 과반수가 넘은 인증 조회 */
    @Query(value = "select c from DiyCertifyEntity c " +
            "join GroupPersonEntity p on c.groupNumber=p.groupNumber and c.memId=p.memId " +
            "join GroupEntity g on p.groupNumber=g.groupNumber " +
            "where g.groupState=2 and g.groupHeadcount/2<c.oppositeCount")
    List<DiyCertifyEntity> findMajorityOpposite();

    /** 어제의 인증 조회(랭킹반영을 위함) */
    List<DiyCertifyEntity> findByCertifyDate(Date certifyDate);


}
