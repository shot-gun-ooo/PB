<script setup>
import { ref } from 'vue'

// 💡 실무에서 진짜 볼법한 디테일한 가짜(Mock) 데이터
const watchList = ref([
  { id: 1, name: 'A건설', industry: '건설/PF', loanAmount: '500 억', icr: 0.8, ltv: '85%', debtRatio: '250%', maturity: 'D-12', status: '위험' },
  { id: 2, name: 'B해운', industry: '운수/창고', loanAmount: '350 억', icr: 1.1, ltv: '60%', debtRatio: '180%', maturity: 'D-45', status: '주의' },
  { id: 3, name: 'C무역', industry: '도소매', loanAmount: '1,200 억', icr: 0.5, ltv: '95%', debtRatio: '320%', maturity: 'D-3', status: '위험' },
  { id: 4, name: 'D전자', industry: '제조', loanAmount: '200 억', icr: 2.5, ltv: '40%', debtRatio: '80%', maturity: 'D-120', status: '정상' }
])

// 💡 버튼을 눌렀을 때 실행될 가짜 동작(함수)
const handleAction = (companyName, actionType) => {
  alert(`[시스템] ${companyName} 기업의 '${actionType}' 창을 엽니다.`);
}
</script>

<template>
  <div class="dashboard">
    <!-- 1. 상단: 핵심 리스크 지표 (가로 3칸 정렬 복구 완료!) -->
    <div class="summary-grid">
      <div class="card metric-card">
        <h3>총 여신 잔액</h3>
        <p class="number">₩ 1,250 억</p>
      </div>
      <div class="card metric-card">
        <h3>요주의/고정 이하 여신</h3>
        <p class="number warning">₩ 85 억</p>
      </div>
      <div class="card metric-card">
        <h3>이자보상배율 1미만 기업</h3>
        <p class="number danger">12 곳</p>
      </div>
    </div>

    <!-- 2. 중간: 차트 및 실시간 알림 (차트 영역을 2개로 분할) -->
    <div class="middle-grid">
      <!-- 건전성 추이 차트 -->
      <div class="card chart-section">
        <h3>📉 여신 건전성 추이 (전체)</h3>
        <div class="placeholder-box">꺾은선 차트 영역</div>
      </div>
      <!-- 업종별/만기별 분석 차트 -->
      <div class="card chart-section">
        <h3>🥧 포트폴리오 분석 (업종 / 만기)</h3>
        <div class="placeholder-box double-chart">
          <div class="mini-box">파이 차트 (업종 편중도)</div>
          <div class="mini-box">바 차트 (월별 만기액)</div>
        </div>
      </div>
      <!-- 실시간 알림 -->
      <div class="card alert-section">
        <h3>🚨 실시간 리스크 알림</h3>
        <ul>
          <li>[경고] C무역 만기 D-3 도래</li>
          <li>[경고] A건설 부채비율 250% 돌파</li>
          <li>[주의] B해운 이자보상배율 하락 징후</li>
        </ul>
      </div>
    </div>

    <!-- 3. 하단: 실무용 관리 대상 기업 목록 (데이터/버튼 대폭 추가) -->
    <div class="bottom-grid">
      <div class="card list-section">
        <h3>실무 심사 대상 기업 목록 (Watchlist)</h3>
        
        <div class="table-container">
          <table class="data-table">
            <thead>
              <tr>
                <th>기업명</th>
                <th>업종</th>
                <th>여신 잔액</th>
                <th>ICR (이자보상)</th>
                <th>LTV (담보)</th>
                <th>부채비율</th>
                <th>만기일</th>
                <th>상태</th>
                <th>즉각 조치 (Action)</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="company in watchList" :key="company.id">
                <td class="fw-bold">{{ company.name }}</td>
                <td>{{ company.industry }}</td>
                <td>{{ company.loanAmount }}</td>
                <td :class="{'text-danger': company.icr < 1}">{{ company.icr }}</td>
                <td :class="{'text-warning': parseInt(company.ltv) > 80}">{{ company.ltv }}</td>
                <td :class="{'text-danger': parseInt(company.debtRatio) > 200}">{{ company.debtRatio }}</td>
                <td :class="{'text-danger': company.maturity.includes('D-') && parseInt(company.maturity.replace('D-', '')) < 30}">
                  {{ company.maturity }}
                </td>
                <td>
                  <span class="badge" 
                        :class="{
                          'bg-danger': company.status === '위험',
                          'bg-warning': company.status === '주의',
                          'bg-safe': company.status === '정상'
                        }">
                    {{ company.status }}
                  </span>
                </td>
                <td class="action-buttons">
                  <button @click="handleAction(company.name, '심사보고서 작성')" class="btn-action primary">심사보고</button>
                  <button @click="handleAction(company.name, '금리 재산정')" class="btn-action secondary">금리조정</button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* 기존 레이아웃 유지 */
.dashboard { padding: 30px 40px; display: flex; flex-direction: column; gap: 20px; }
.card { background-color: #2a2a2a; border-radius: 12px; padding: 20px; box-shadow: 0 4px 6px rgba(0,0,0,0.3); }
h3 { margin-top: 0; color: #aaaaaa; font-size: 16px; margin-bottom: 15px; }
.placeholder-box { background-color: #333333; height: 200px; border-radius: 8px; display: flex; justify-content: center; align-items: center; color: #888; }

/* 1. 상단 3칸짜리 그리드 복구 영역!! */
.summary-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 20px; }
.metric-card .number { font-size: 28px; font-weight: bold; margin: 0; color: #ffffff; }
.metric-card .warning { color: #f59e0b; }
.metric-card .danger { color: #ef4444; }

/* 2. 중간 그리드: 3칸으로 쪼개기 (차트1, 차트2, 알림) */
.middle-grid { display: grid; grid-template-columns: 1.5fr 1.5fr 1fr; gap: 20px; }
.double-chart { display: flex; gap: 10px; padding: 10px; background-color: transparent; }
.mini-box { flex: 1; background-color: #333; border-radius: 8px; display: flex; justify-content: center; align-items: center; text-align: center; font-size: 14px;}
.alert-section ul { list-style: none; padding: 0; margin: 0; }
.alert-section li { padding: 12px 0; border-bottom: 1px solid #444; color: #ddd; font-size: 14px; }
.alert-section li:last-child { border-bottom: none; }

/* 3. 테이블 컨테이너 (스크롤 방어용) */
.bottom-grid { display: grid; grid-template-columns: 1fr; }
.table-container { overflow-x: auto; } /* 창이 좁아지면 가로 스크롤 생성 */
.data-table { width: 100%; border-collapse: collapse; color: #eeeeee; text-align: center; font-size: 14px; }
.data-table th { padding: 12px; background-color: #333333; color: #aaaaaa; border-bottom: 2px solid #444; white-space: nowrap; }
.data-table td { padding: 15px 12px; border-bottom: 1px solid #333; vertical-align: middle; }
.fw-bold { font-weight: bold; }
.text-danger { color: #ef4444; font-weight: bold; }
.text-warning { color: #f59e0b; font-weight: bold; }

/* 뱃지 */
.badge { padding: 5px 10px; border-radius: 4px; font-size: 12px; font-weight: bold; }
.bg-danger { background-color: rgba(239, 68, 68, 0.2); color: #ef4444; }
.bg-warning { background-color: rgba(245, 158, 11, 0.2); color: #f59e0b; }
.bg-safe { background-color: rgba(16, 185, 129, 0.2); color: #10b981; }

/* 액션 버튼 */
.action-buttons { display: flex; gap: 8px; justify-content: center; }
.btn-action { padding: 6px 10px; border: none; border-radius: 4px; font-size: 12px; font-weight: bold; cursor: pointer; transition: 0.2s; }
.btn-action.primary { background-color: #3b82f6; color: white; }
.btn-action.primary:hover { background-color: #2563eb; }
.btn-action.secondary { background-color: #4b5563; color: white; }
.btn-action.secondary:hover { background-color: #374151; }
</style>