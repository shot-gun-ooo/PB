<script setup>
import { ref } from 'vue'

// 💡 퀀트 스크리닝을 통과한 가짜(Mock) 종목 데이터
const portfolioList = ref([
  { id: 1, name: 'S전자', sector: 'IT/반도체', price: '72,500', per: 13.5, pbr: 1.2, roe: '10.5%', yield: '2.1%', status: '매수' },
  { id: 2, name: 'H자동차', sector: '운수장비', price: '245,000', per: 4.8, pbr: 0.6, roe: '15.2%', yield: '5.4%', status: '강력매수' },
  { id: 3, name: 'K금융', sector: '금융업', price: '68,000', per: 3.5, pbr: 0.4, roe: '11.8%', yield: '6.8%', status: '강력매수' },
  { id: 4, name: 'N플랫폼', sector: '서비스업', price: '185,000', per: 35.4, pbr: 2.8, roe: '8.5%', yield: '0.5%', status: '관망' }
])

const handleInvestAction = (companyName, actionType) => {
  alert(`[시스템] ${companyName} 종목의 '${actionType}' 프로세스를 시작합니다.`);
}
</script>

<template>
  <div class="dashboard">
    <!-- 1. 상단: 글로벌 매크로 및 시장 지표 -->
    <div class="macro-grid">
      <div class="card metric-card">
        <h3>KOSPI</h3>
        <p class="number">2,750.24 <span class="up">▲ 1.2%</span></p>
      </div>
      <div class="card metric-card">
        <h3>USD/KRW (환율)</h3>
        <p class="number">1,345.50 <span class="down">▼ 0.5%</span></p>
      </div>
      <div class="card metric-card">
        <h3>안전마진 확보 종목 (Watch)</h3>
        <p class="number highlight">24 개</p>
      </div>
    </div>

    <!-- 2. 중간: 종목 필터링 컨트롤 -->
    <div class="filter-card card">
      <h3>🔍 우량주 퀀트 스크리닝 (Value & Quality)</h3>
      <div class="filter-controls">
        <select class="filter-select">
          <option>PER 10 미만 (저퍼주)</option>
          <option>PER 15 미만</option>
        </select>
        <select class="filter-select">
          <option>PBR 1 미만 (자산가치 우량)</option>
          <option>PBR 1.5 미만</option>
        </select>
        <select class="filter-select">
          <option>ROE 10% 이상 (수익성)</option>
          <option>ROE 15% 이상</option>
        </select>
        <select class="filter-select">
          <option>배당수익률 4% 이상 (고배당)</option>
        </select>
        <button class="search-btn">조건 검색</button>
      </div>
    </div>

    <!-- 3. 하단: 필터링 결과 포트폴리오 리스트 -->
    <div class="bottom-grid">
      <div class="card list-section">
        <h3>추천 종목 포트폴리오 (목표수익률 15%)</h3>
        
        <div class="table-container">
          <table class="data-table">
            <thead>
              <tr>
                <th>종목명</th>
                <th>섹터</th>
                <th>현재가(원)</th>
                <th>PER</th>
                <th>PBR</th>
                <th>ROE</th>
                <th>배당수익률</th>
                <th>투자의견</th>
                <th>액션 (Action)</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="stock in portfolioList" :key="stock.id">
                <td class="fw-bold">{{ stock.name }}</td>
                <td>{{ stock.sector }}</td>
                <td>{{ stock.price }}</td>
                <!-- PER 10 미만 초록색, 20 이상 빨간색 -->
                <td :class="{'text-safe': stock.per < 10, 'text-danger': stock.per >= 20}">{{ stock.per }}</td>
                <!-- PBR 1 미만 초록색 -->
                <td :class="{'text-safe': stock.pbr < 1}">{{ stock.pbr }}</td>
                <td class="fw-bold">{{ stock.roe }}</td>
                <!-- 배당수익률 5% 이상 파란색 강조 -->
                <td :class="{'text-dividend': parseFloat(stock.yield) >= 5}">{{ stock.yield }}</td>
                <td>
                  <span class="badge" 
                        :class="{
                          'bg-strong-buy': stock.status === '강력매수',
                          'bg-buy': stock.status === '매수',
                          'bg-hold': stock.status === '관망'
                        }">
                    {{ stock.status }}
                  </span>
                </td>
                <td class="action-buttons">
                  <button @click="handleInvestAction(stock.name, '기업분석 리포트')" class="btn-action secondary">리포트</button>
                  <button @click="handleInvestAction(stock.name, '모의투자 편입')" class="btn-action primary">편입</button>
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
/* 전체 레이아웃 (HomeView와 통일감 유지) */
.dashboard { padding: 30px 40px; display: flex; flex-direction: column; gap: 20px; }
.card { background-color: #2a2a2a; border-radius: 12px; padding: 20px; box-shadow: 0 4px 6px rgba(0,0,0,0.3); }
h3 { margin-top: 0; color: #aaaaaa; font-size: 16px; margin-bottom: 15px; }

/* 1. 상단 매크로 지표 그리드 */
.macro-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 20px; }
.metric-card .number { font-size: 28px; font-weight: bold; margin: 0; color: #ffffff; display: flex; justify-content: space-between; align-items: baseline; }
.metric-card .up { color: #ef4444; font-size: 16px; font-weight: normal; }
.metric-card .down { color: #3b82f6; font-size: 16px; font-weight: normal; }
.metric-card .highlight { color: #10b981; }

/* 2. 중간 필터링 영역 */
.filter-controls { display: flex; gap: 15px; flex-wrap: wrap; }
.filter-select { flex: 1; min-width: 150px; padding: 10px; background-color: #333; color: white; border: 1px solid #555; border-radius: 6px; font-size: 14px; outline: none; }
.search-btn { padding: 10px 25px; background-color: #3b82f6; color: white; border: none; border-radius: 6px; font-weight: bold; cursor: pointer; transition: background-color 0.2s; white-space: nowrap; }
.search-btn:hover { background-color: #2563eb; }

/* 3. 테이블 컨테이너 및 디자인 */
.bottom-grid { display: grid; grid-template-columns: 1fr; }
.table-container { overflow-x: auto; }
.data-table { width: 100%; border-collapse: collapse; color: #eeeeee; text-align: center; font-size: 14px; }
.data-table th { padding: 12px; background-color: #333333; color: #aaaaaa; border-bottom: 2px solid #444; white-space: nowrap; }
.data-table td { padding: 15px 12px; border-bottom: 1px solid #333; vertical-align: middle; }
.fw-bold { font-weight: bold; }

/* 조건부 텍스트 색상 */
.text-danger { color: #ef4444; font-weight: bold; } /* 고평가 주의 */
.text-safe { color: #10b981; font-weight: bold; } /* 저평가 안전 */
.text-dividend { color: #3b82f6; font-weight: bold; } /* 고배당 파란색 */

/* 투자의견 뱃지 */
.badge { padding: 5px 10px; border-radius: 4px; font-size: 12px; font-weight: bold; }
.bg-strong-buy { background-color: rgba(239, 68, 68, 0.2); color: #ef4444; } /* 강력매수 빨강 */
.bg-buy { background-color: rgba(245, 158, 11, 0.2); color: #f59e0b; } /* 매수 노랑 */
.bg-hold { background-color: rgba(156, 163, 175, 0.2); color: #9ca3af; } /* 관망 회색 */

/* 액션 버튼 */
.action-buttons { display: flex; gap: 8px; justify-content: center; }
.btn-action { padding: 6px 10px; border: none; border-radius: 4px; font-size: 12px; font-weight: bold; cursor: pointer; transition: 0.2s; }
.btn-action.primary { background-color: #10b981; color: white; } /* 편입 초록색 */
.btn-action.primary:hover { background-color: #059669; }
.btn-action.secondary { background-color: #4b5563; color: white; }
.btn-action.secondary:hover { background-color: #374151; }
</style>