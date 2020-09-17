package com.ssafy.shalendar.springboot.domain.feed;

import com.ssafy.shalendar.springboot.domain.schedules.Schedules;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FeedRepository extends JpaRepository<Feed, Long> {
    //SpringDataJpa 에서 제공하지 않는 메소드는 아래처럼 쿼리로 작성 해도 됨
    @Query("SELECT f FROM Feed f ORDER BY  f.createdDate DESC")
    List<Feed> findAllDesc();

    @Query(value = "select f.feedNo from Feed f where f.createdDate = (select max(f2.createdDate) from Feed f2 where f2.schNo=:schNo)", nativeQuery = false)
    Long findBySchNoOrderByCreatedDateDesc(@Param("schNo") Schedules schNo);

    @Modifying
    @Query("DELETE FROM Feed f WHERE f.schNo=:schNo")
    void deleteBySchNo(@Param("schNo") Schedules schNo);
}
