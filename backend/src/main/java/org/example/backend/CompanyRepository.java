package org.example.backend;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * [코드 리뷰] DB 창고지기 (Repository)
 * JpaRepository를 상속(extends)받는 인터페이스입니다.
 *
 * 자바 기초에서 배운 인터페이스 개념을 활용한 것인데,
 * 여기에 <Company, Integer>라고 적어주기만 하면 스프링부트가 알아서
 * "아, Company 데이터를 다루고, 고유번호(Id)는 숫자(Integer)구나!" 하고 파악한 뒤
 * 데이터를 저장하는 save(), 꺼내오는 findAll() 같은 메서드를 공짜로 만들어줍니다.
 */
public interface CompanyRepository extends JpaRepository<Company, Integer> {
}