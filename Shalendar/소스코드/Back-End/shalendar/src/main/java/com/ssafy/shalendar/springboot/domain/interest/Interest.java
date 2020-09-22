package com.ssafy.shalendar.springboot.domain.interest;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Interest {
    @Id  //해당 테이블의 PK 필드
    @GeneratedValue(strategy = GenerationType.IDENTITY)  //PK의 생성 규칙
    private Long interestNo;
    @Column(nullable = false, columnDefinition = "varchar(20)")
    private String interestName;

    @Builder
    public Interest(Long interestNo, String interestName) {
        this.interestNo = interestNo;
        this.interestName = interestName;
    }

    @Override
    public String toString() {
        return "Interest{" +
                "interestNo=" + interestNo +
                ", interestName='" + interestName + '\'' +
                '}';
    }
}
