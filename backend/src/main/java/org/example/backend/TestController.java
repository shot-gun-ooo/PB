package org.example.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
// 💡 주소를 /api/watchlist 하나로 통일합니다.
@RequestMapping("/api/watchlist")
// 💡 프론트엔드(Vue) 접속을 허용하는 설정입니다.
@CrossOrigin(origins = "http://localhost:5173", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class TestController {

    @Autowired
    private CompanyRepository companyRepository;

    // 1. 전체 목록 조회 (GET)
    @GetMapping
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    // 2. 새 기업 등록 (POST)
    @PostMapping
    public Company addCompany(@RequestBody Company company) {
        return companyRepository.save(company);
    }

    // 3. 기업 삭제 (DELETE) - 💡 여기서 중복이 발생했을 수 있으니 이것만 남기세요!
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        if (companyRepository.existsById(id)) {
            companyRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}