package org.example.backend;

import jakarta.persistence.*;
import lombok.*;

/**
 * [코드 리뷰] 실무형 Entity 클래스
 * @Data 대신 @Getter/@Setter를 쓰는 이유는 JPA 엔티티에서 발생할 수 있는 무한 루프 버그를 방지하기 위함입니다.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Company {

    /**
     * [코드 리뷰] 객체형 타입(Integer) 사용 이유
     * 기본형(int)은 값이 없으면 자동으로 0이 들어갑니다.
     * 하지만 실무에서는 '값이 아직 없는 상태(null)'와 '진짜 0인 상태'를 구분해야 하므로
     * null을 담을 수 있는 Integer를 사용합니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;         // 순번 (DB 자동 생성)

    private String name;        // 기업명
    private String industry;    // 업종
    private String loanAmount;  // 여신 잔액

    /**
     * [코드 리뷰] Double 사용 이유
     * double(기본형) 대신 Double(객체형)을 사용하면 프론트엔드에서 실수로 값을 안 보내더라도
     * 에러로 서버가 터지는 대신 'null'로 안전하게 받아낼 수 있습니다.
     */
    private Double icr;         // 이자보상배율

    private String ltv;         // 담보인정비율
    private String debtRatio;   // 부채비율
    private String maturity;    // 만기일
    private String status;      // 상태 (위험/주의/정상)
    // Company 엔티티 클래스 안에 추가하세요.
    public void updateStatus() {
        // ICR이 1 미만이면 무조건 위험
        if (this.icr < 1.0) {
            this.status = "위험";
        }
        // ICR이 낮거나 부채비율이 높으면 주의 (부채비율은 String이라 숫자 변환 필요)
        else if (this.icr < 1.5 || Integer.parseInt(this.debtRatio.replace("%", "")) > 200) {
            this.status = "주의";
        }
         else {
            this.status = "정상";
        }
    }
}