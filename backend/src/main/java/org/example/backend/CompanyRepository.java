package org.example.backend;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * [코드 리뷰] DB 창고지기 (Repository)
 * 💡 수정 포인트: Integer를 Long으로 변경했습니다.
 * 보통 실무에서는 데이터가 많아질 것을 대비해 숫자의 범위가 더 큰 Long을 Id로 씁니다.
 */
public interface CompanyRepository extends JpaRepository<Company, Long> {
}