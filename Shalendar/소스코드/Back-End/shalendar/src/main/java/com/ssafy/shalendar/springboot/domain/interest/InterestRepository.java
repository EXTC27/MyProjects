package com.ssafy.shalendar.springboot.domain.interest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface InterestRepository extends JpaRepository<Interest, Long>  {

    Interest findByInterestName(@Param("interest_name") String interest_name);

}
