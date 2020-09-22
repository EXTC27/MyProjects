package com.ssafy.shalendar.springboot.domain.schedules;

import com.ssafy.shalendar.springboot.domain.channel.Channel;
import com.ssafy.shalendar.springboot.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SchedulesRepository extends JpaRepository<Schedules,Long> {
    @Query(value ="select s from Schedules s where s.memNo=:memNo and (s.sdate like concat(:Yymm,'%') or s.edate like concat(:Yymm,'%'))", nativeQuery = false)
    List<Schedules> findByMemNoAndSdateOrEdate(@Param("memNo") Member memNo, @Param("Yymm") String Yymm);

    @Query(value = "select s from Schedules s where s.chNo=:chNo and (s.sdate like concat(:Yymm,'%') or s.edate like concat(:Yymm,'%'))", nativeQuery = false)
    List<Schedules> findByChNoAndSdateOrEdate(@Param("chNo") Channel chNo, @Param("Yymm") String Yymm);

    @Query(value = "select s.schNo from Schedules s where s.createdDate = (select max(a.createdDate) from Schedules a where a.chNo=:chNo)", nativeQuery = false)
    Long findByChNoOrderByCreatedDateDesc(@Param("chNo") Channel chNo);

    @Query(value = "select s.schNo from Schedules s where s.createdDate = (select max(a.createdDate) from Schedules a where a.memNo=:memNo)", nativeQuery = false)
    Long findByMemNoOrderByCreatedDateDesc(@Param("memNo") Member memNo);

}
