<template>
  <div class="dashboard">
    <!-- 1. 상단: 핵심 리스크 지표 (3칸) -->
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

    <!-- 2. 중간: 차트 및 실시간 알림 (2칸) -->
    <div class="middle-grid">
      <div class="card chart-section">
        <h3>여신 건전성 추이</h3>
        <div class="placeholder-box">📈 꺾은선 차트가 들어갈 자리입니다</div>
      </div>
      <div class="card alert-section">
        <h3>🚨 실시간 리스크 알림</h3>
        <ul>
          <li>[경고] A기업 부채비율 200% 돌파</li>
          <li>[주의] B기업 3분기 연속 영업이익 적자</li>
          <li>[알림] C기업 여신 만기일 도래 (D-7)</li>
        </ul>
      </div>
    </div>

    <!-- 3. 하단: 관리 대상 기업 목록 (1칸 전체) -->
    <div class="bottom-grid">
      <div class="card list-section">
        <h3>관리 대상 기업 목록 (Watchlist)</h3>
        <div class="placeholder-box">📋 자바 서버(DB)에서 받아온 표(Table) 데이터가 들어갈 자리입니다</div>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* 대시보드 전체 여백 설정 */
.dashboard {
  padding: 30px 40px;
  display: flex;
  flex-direction: column;
  gap: 20px; /* 위아래 섹션 간격 */
}

/* 개별 박스(카드) 디자인 */
.card {
  background-color: #2a2a2a; /* 헤더보다 살짝 밝은 어두운 회색 */
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

/* 임시 박스 디자인 (나중에 차트나 표로 교체할 부분) */
.placeholder-box {
  background-color: #333333;
  height: 200px;
  border-radius: 8px;
  display: flex;
  justify-content: center;
  align-items: center;
  color: #888;
}

/* 1. 상단 3칸짜리 그리드 */
.summary-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr); /* 1:1:1 비율로 3칸 쪼개기 */
  gap: 20px;
}

.metric-card .number {
  font-size: 28px;
  font-weight: bold;
  margin: 0;
  color: #ffffff;
}
.metric-card .warning { color: #f59e0b; } /* 노란색 경고 */
.metric-card .danger { color: #ef4444; } /* 빨간색 위험 */

/* 2. 중간 2칸짜리 그리드 (차트는 넓게, 알림은 좁게) */
.middle-grid {
  display: grid;
  grid-template-columns: 2fr 1fr; /* 2:1 비율로 쪼개기 */
  gap: 20px;
}

.alert-section ul {
  list-style: none;
  padding: 0;
  margin: 0;
}
.alert-section li {
  padding: 12px 0;
  border-bottom: 1px solid #444;
  color: #ddd;
  font-size: 15px;
}
.alert-section li:last-child { border-bottom: none; }

/* 3. 하단 1칸짜리 그리드 */
.bottom-grid {
  display: grid;
  grid-template-columns: 1fr; /* 통짜 1칸 */
}
</style>