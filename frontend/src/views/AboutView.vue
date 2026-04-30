<template>
  <div class="dashboard">
    <!-- 1. 상단: 글로벌 매크로 및 시장 지표 (3칸) -->
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
        <h3>안전마진 확보 종목</h3>
        <p class="number highlight">24 개</p>
      </div>
    </div>

    <!-- 2. 중간: 종목 필터링 컨트롤 -->
    <div class="filter-card card">
      <h3>🔍 우량주 퀀트 필터링</h3>
      <div class="filter-controls">
        <select class="filter-select">
          <option>PER 10 미만 (저퍼주)</option>
        </select>
        <select class="filter-select">
          <option>PBR 1 미만 (저평가)</option>
        </select>
        <select class="filter-select">
          <option>ROE 15% 이상 (고수익)</option>
        </select>
        <button class="search-btn">조건 검색</button>
      </div>
    </div>

    <!-- 3. 하단: 필터링 결과 포트폴리오 리스트 -->
    <div class="bottom-grid">
      <div class="card list-section">
        <h3>추천 종목 포트폴리오</h3>
        <div class="placeholder-box">📋 향후 자바(Java) 백엔드에서 분석한 종목 데이터가 표 형식으로 출력됩니다</div>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* 전체 대시보드 레이아웃 */
.dashboard {
  padding: 30px 40px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

/* 개별 카드 디자인 */
.card {
  background-color: #2a2a2a;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 4px 6px rgba(0,0,0,0.3);
}

h3 {
  margin-top: 0;
  color: #aaaaaa;
  font-size: 16px;
  margin-bottom: 15px;
}

.placeholder-box {
  background-color: #333333;
  height: 250px;
  border-radius: 8px;
  display: flex;
  justify-content: center;
  align-items: center;
  color: #888;
}

/* 1. 상단 매크로 지표 그리드 */
.macro-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}

.metric-card .number {
  font-size: 28px;
  font-weight: bold;
  margin: 0;
  color: #ffffff;
  display: flex;
  justify-content: space-between;
  align-items: baseline;
}

/* 주식 시장 색상 (한국 기준: 상승 빨강, 하락 파랑) */
.metric-card .up { color: #ef4444; font-size: 16px; font-weight: normal; }
.metric-card .down { color: #3b82f6; font-size: 16px; font-weight: normal; }
.metric-card .highlight { color: #10b981; }

/* 2. 중간 필터링 영역 */
.filter-controls {
  display: flex;
  gap: 15px;
}

.filter-select {
  flex: 1;
  padding: 10px;
  background-color: #333;
  color: white;
  border: 1px solid #555;
  border-radius: 6px;
  font-size: 15px;
  outline: none;
}

.search-btn {
  padding: 10px 25px;
  background-color: #3b82f6;
  color: white;
  border: none;
  border-radius: 6px;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.2s;
}

.search-btn:hover {
  background-color: #2563eb;
}
</style>