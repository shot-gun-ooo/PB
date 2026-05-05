package org.example.backend;

import jakarta.annotation.PostConstruct;
import org.springframework.web.bind.annotation.*; // @PostMapping 등을 위해 수정
import java.util.List;

@RestController
//@CrossOrigin(origins = "http://localhost:5173")
@CrossOrigin(origins = "*")
public class TestController {

    private final CompanyRepository companyRepository;

    public TestController(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @PostConstruct
    public void initData() {
        // 기존에 id 자리에 0을 썼다면, null로 바꿔주세요.
        // 그러면 DB가 "아, 이건 새 데이터구나! 내가 알아서 번호 매길게" 하고 좋아합니다.
        if (companyRepository.count() == 0) {
            companyRepository.save(new Company(null, "A건설", "건설/PF", "500 억", 0.8, "85%", "250%", "D-3", "위험"));
            companyRepository.save(new Company(null, "B해운", "운수/창고", "350 억", 1.2, "60%", "180%", "2026-12", "주의"));
            companyRepository.save(new Company(null, "C무역", "도소매", "1,200 억", 0.5, "90%", "400%", "D-15", "위험"));
            companyRepository.save(new Company(null, "D전자", "제조업", "200 억", 3.5, "40%", "80%", "2027-05", "정상"));
        }
    }

    @GetMapping("/api/watchlist")
    public List<Company> getWatchlist() {
        return companyRepository.findAll();
    }

//    /**
//     * [코드 리뷰] 🟢 데이터 추가 기능 (POST)
//     * 프론트엔드에서 보내준 기업 정보를 받아서 DB 창고에 저장합니다.
//     * @RequestBody: 손님이 보낸 JSON 데이터를 자바 객체(Company)로 자동 변환해줍니다.
//     */
    @PostMapping("/api/watchlist")
    public Company addCompany(@RequestBody Company company) {
        // 창고지기에게 "새로운 기업 정보 왔으니까 DB 선반에 올려놔!"라고 시킵니다.
        return companyRepository.save(company);
    }
}