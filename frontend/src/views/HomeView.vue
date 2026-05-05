<script setup>
import { ref, onMounted, watch, computed } from 'vue'; // 💡 필터링 계산을 위해 computed 추가
import axios from 'axios'; // 💡 백엔드와 통신하기 위해 꼭 필요한 도구입니다.
import Chart from 'chart.js/auto'; // 📊 차트 시각화를 위한 라이브러리 추가

// 💡 [코드 리뷰] 실시간 데이터 상태 관리
// 초기에는 빈 리스트로 두고, 서버에서 데이터를 받아와서 채울 겁니다.
const watchList = ref([]);
let icrChart = null; // 차트 객체를 담을 변수

// 🔍 [필터 상태 관리] 검색어와 선택된 필터 등급을 저장합니다.
const searchQuery = ref('');
const filterStatus = ref('전체');

// 💡 [코드 리뷰] 새 기업 입력을 위한 바구니
const newCompany = ref({
  name: '',
  industry: '',
  loanAmount: '',
  icr: 0,
  ltv: '70%',
  debtRatio: '150%',
  maturity: '2026-12',
  status: '정상'
});

// 💡 [핵심 로직] 검색어와 필터 조건에 맞는 기업만 계산해서 화면에 보여줍니다.
const filteredList = computed(() => {
  return watchList.value.filter(company => {
    const matchesSearch = company.name.toLowerCase().includes(searchQuery.value.toLowerCase());
    const matchesStatus = filterStatus.value === '전체' || company.status === filterStatus.value;
    return matchesSearch && matchesStatus;
  });
});

// 📊 [차트 로직] 데이터를 기반으로 차트를 그리는 함수
const renderChart = () => {
  const ctx = document.getElementById('icrChart');
  if (!ctx) return;

  // 이미 차트가 있다면 지우고 다시 그립니다 (업데이트용)
  if (icrChart) {
    icrChart.destroy();
  }

  icrChart = new Chart(ctx, {
    type: 'bar',
    data: {
      labels: watchList.value.map(c => c.name), // X축: 기업명
      datasets: [{
        label: '이자보상배율(ICR)',
        data: watchList.value.map(c => c.icr), // Y축: ICR 값
        // 💡 실무 로직: ICR 1 미만은 리스크(빨강), 이상은 정상(파랑)
        backgroundColor: watchList.value.map(c => c.icr < 1 ? 'rgba(239, 68, 68, 0.7)' : 'rgba(59, 130, 246, 0.7)'),
        borderColor: watchList.value.map(c => c.icr < 1 ? '#ef4444' : '#3b82f6'),
        borderWidth: 1
      }]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: { labels: { color: '#aaa' } }
      },
      scales: {
        y: { 
          beginAtZero: true, 
          grid: { color: '#444' },
          ticks: { color: '#aaa' }
        },
        x: { 
          grid: { color: '#444' },
          ticks: { color: '#aaa' }
        }
      }
    }
  });
};

// 💡 [코드 리뷰] 서버에서 데이터 가져오는 함수 (GET)
const fetchWatchlist = async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/watchlist');
    watchList.value = response.data; // 서버에서 받은 진짜 데이터를 리스트에 넣습니다.
  } catch (error) {
    console.error("데이터 로딩 실패:", error);
  }
};

// 💡 [코드 리뷰] 서버에 데이터 저장하는 함수 (POST)
const addCompany = async () => {
  // 1. 필수 입력값 체크 (비어있으면 서버에 보내지도 않음)
  if (!newCompany.value.name || !newCompany.value.industry) {
    alert("기업명과 업종을 입력해주세요!");
    return;
  }
  
  // 💡 [로직 수정] 입력된 ICR 숫자를 보고 시스템이 스스로 등급을 판단하게 합니다.
  let autoStatus = "정상";
  const currentIcr = Number(newCompany.value.icr) || 0;
  
  if (currentIcr < 1.0) {
    autoStatus = "위험"; // 1미만: 이자 낼 돈도 못 번다 (이자보상배율의 기본 원리)
  } else if (currentIcr < 1.5) {
    autoStatus = "주의"; // 1.5미만: 주의 깊은 관찰 필요
  }

  // 2. 서버가 좋아하는 깔끔한 양식으로 정리
  const sendData = {
    name: String(newCompany.value.name),
    industry: String(newCompany.value.industry),
    loanAmount: String(newCompany.value.loanAmount),
    icr: currentIcr, 
    ltv: String(newCompany.value.ltv || '70%'),
    debtRatio: String(newCompany.value.debtRatio || '150%'),
    maturity: String(newCompany.value.maturity || '2026-12'),
    status: autoStatus // 💡 사용자가 정한 '정상'이 아니라, 계산된 autoStatus를 보냅니다!
  };

  try {
    // 3. 통행증(*)이 발급된 서버로 전송!
    await axios.post('http://localhost:8080/api/watchlist', sendData);
    alert(`✅ ${autoStatus} 등급으로 진짜 DB에 등록되었습니다!`);
    
    await fetchWatchlist(); // 목록 새로고침
    
    // 입력창 초기화
    newCompany.value = { name: '', industry: '', loanAmount: '', icr: 0, ltv: '70%', debtRatio: '150%', maturity: '2026-12', status: '정상' };
  } catch (error) {
    console.error("상세 에러 내역:", error.response?.data);
    alert("등록에 실패했습니다. 입력한 숫자 형식을 확인해주세요!");
  }
};

// 💡 [코드 리뷰] 데이터 삭제 함수 (DELETE)
const deleteCompany = async (id) => {
  if (!confirm("정말 이 여신 데이터를 삭제하시겠습니까?")) return;
  try {
    await axios.delete(`http://localhost:8080/api/watchlist/${id}`);
    alert("🗑️ 성공적으로 삭제되었습니다.");
    fetchWatchlist();
  } catch (error) {
    console.error("삭제 실패:", error);
  }
};

// 📊 데이터가 업데이트될 때마다 차트를 다시 그립니다.
watch(watchList, () => {
  renderChart();
}, { deep: true });

// 💡 화면이 처음 켜질 때 자동으로 실행되는 곳
onMounted(() => {
  fetchWatchlist();
});

// 💡 버튼을 눌렀을 때 실행될 가짜 동작(함수)
const handleAction = (companyName, actionType) => {
  alert(`[시스템] ${companyName} 기업의 '${actionType}' 창을 엽니다.`);
};
</script>

<template>
  <div class="dashboard">
    <!-- 1. 상단: 핵심 리스크 지표 -->
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
        <p class="number danger">{{ watchList.filter(c => c.icr < 1).length }} 곳</p>
      </div>
    </div>

    <!-- 2. 중간: 차트 및 실시간 알림 -->
    <div class="middle-grid">
      <div class="card chart-section">
        <h3>📉 기업별 ICR 현황 (실시간 데이터)</h3>
        <div class="chart-container">
          <canvas id="icrChart"></canvas>
        </div>
      </div>
      <div class="card chart-section">
        <h3>🥧 포트폴리오 분석 (업종 / 만기)</h3>
        <div class="placeholder-box double-chart">
          <div class="mini-box">파이 차트 (업종 편중도)</div>
          <div class="mini-box">바 차트 (월별 만기액)</div>
        </div>
      </div>
      <div class="card alert-section">
        <h3>🚨 실시간 리스크 알림</h3>
        <ul>
          <li v-for="company in watchList.filter(c => c.icr < 1)" :key="company.id">
            [위험] {{ company.name }} ICR {{ company.icr }} (기준치 미달)
          </li>
          <li>[경고] C무역 만기 D-3 도래</li>
          <li>[경고] A건설 부채비율 250% 돌파</li>
        </ul>
      </div>
    </div>

    <!-- 3. 하단: 실무용 관리 대상 기업 목록 -->
    <div class="bottom-grid">
      <div class="card list-section">
        <div class="list-header">
          <h3>실무 심사 대상 기업 목록 (Watchlist)</h3>
          
          <!-- 🔍 [신규] 검색 및 필터 컨트롤 -->
          <div class="controls">
            <input v-model="searchQuery" placeholder="🔍 기업명 검색..." class="search-input" />
            <div class="filter-tabs">
              <button @click="filterStatus = '전체'" :class="{ active: filterStatus === '전체' }">전체</button>
              <button @click="filterStatus = '위험'" :class="{ active: filterStatus === '위험' }" class="btn-risk">위험</button>
              <button @click="filterStatus = '주의'" :class="{ active: filterStatus === '주의' }" class="btn-warn">주의</button>
              <button @click="filterStatus = '정상'" :class="{ active: filterStatus === '정상' }" class="btn-safe">정상</button>
            </div>
          </div>
        </div>
        
        <!-- ➕ 새 심사 대상 기업 등록 폼 -->
        <div class="add-form card mb-4">
          <h5 style="color: #fff; margin-bottom: 15px;">➕ 새 심사 대상 기업 등록</h5>
          <div class="input-group">
            <input v-model="newCompany.name" placeholder="기업명" />
            <input v-model="newCompany.industry" placeholder="업종" />
            <input v-model="newCompany.loanAmount" placeholder="여신잔액" />
            <input v-model="newCompany.icr" type="number" step="0.1" placeholder="ICR" />
            <button @click="addCompany" class="btn-register">등록하기</button>
          </div>
        </div>

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
                <th>Action / 관리</th>
              </tr>
            </thead>
            <tbody>
              <!-- 💡 v-for 대상을 filteredList로 변경하여 검색/필터 적용 -->
              <tr v-for="company in filteredList" :key="company.id">
                <td class="fw-bold">{{ company.name }}</td>
                <td>{{ company.industry }}</td>
                <td>{{ company.loanAmount }}</td>
                <td :class="{ 'text-danger': company.icr < 1 }">{{ company.icr }}</td>
                <td>{{ company.ltv }}</td>
                <td>{{ company.debtRatio }}</td>
                <td>{{ company.maturity }}</td>
                <td>
                  <span class="badge" :class="{'bg-danger': company.status === '위험', 'bg-warning': company.status === '주의', 'bg-safe': company.status === '정상'}">
                    {{ company.status }}
                  </span>
                </td>
                <td class="action-buttons">
                  <button @click="handleAction(company.name, '심사보고서 작성')" class="btn-action primary">심사보고</button>
                  <button @click="deleteCompany(company.id)" class="btn-action delete">삭제</button>
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
/* 🎨 대시보드 전체 레이아웃 */
.dashboard { 
  padding: 30px 40px; 
  display: flex; 
  flex-direction: column; 
  gap: 20px; 
  background-color: #1a1a1a; 
  min-height: 100vh; 
}

.card { 
  background-color: #2a2a2a; 
  border-radius: 12px; 
  padding: 20px; 
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.3); 
}

/* 📊 [수정] 중간 영역: 차트 2개와 알림창을 가로로 정렬 */
.middle-grid { 
  display: grid; 
  grid-template-columns: 1.2fr 1.2fr 1fr; /* 가로 3등분 비율 조절 */
  gap: 20px; 
  align-items: stretch; /* 카드 높이를 동일하게 맞춤 */
}

/* 📊 [수정] 차트 컨테이너: 높이를 고정하여 세로로 늘어짐 방지 */
.chart-container { 
  height: 250px; /* 차트의 실제 높이 고정 */
  width: 100%; 
  position: relative; 
  margin-top: 10px;
}

/* 🚨 리스트 알림 섹션: 높이 고정 및 스크롤 */
.alert-section ul { 
  list-style: none; 
  padding: 0; 
  max-height: 250px; 
  overflow-y: auto; 
}

.alert-section li { 
  padding: 10px 0; 
  border-bottom: 1px solid #444; 
  color: #ddd; 
  font-size: 14px; 
}

/* 🔍 리스트 헤더 및 컨트롤러 */
.list-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.controls { display: flex; gap: 15px; align-items: center; }
.search-input { padding: 8px 15px; border-radius: 20px; border: 1px solid #444; background: #222; color: white; width: 200px; }
.filter-tabs { display: flex; background: #222; border-radius: 20px; padding: 4px; gap: 5px; }
.filter-tabs button { border: none; background: transparent; color: #888; padding: 5px 15px; border-radius: 15px; cursor: pointer; font-size: 13px; transition: 0.3s; }
.filter-tabs button.active { background: #444; color: white; box-shadow: 0 2px 4px rgba(0,0,0,0.2); }
.filter-tabs .btn-risk.active { color: #ef4444; }
.filter-tabs .btn-warn.active { color: #f59e0b; }
.filter-tabs .btn-safe.active { color: #10b981; }

/* ➕ 등록 폼 */
.add-form { background-color: #333; margin-bottom: 20px; border: 1px solid #444; }
.input-group { display: flex; gap: 10px; }
.input-group input { flex: 1; padding: 10px; border-radius: 6px; border: 1px solid #444; background: #222; color: white; }
.btn-register { padding: 10px 20px; background-color: #3b82f6; color: white; border: none; border-radius: 6px; cursor: pointer; font-weight: bold; }

/* 📈 상단 요약 카드 */
.summary-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 20px; }
.metric-card .number { font-size: 28px; font-weight: bold; color: #ffffff; }
.metric-card .warning { color: #f59e0b; }
.metric-card .danger { color: #ef4444; }

/* 📋 테이블 디자인 */
.data-table { width: 100%; border-collapse: collapse; color: #eee; text-align: center; }
.data-table th { padding: 12px; background-color: #333; color: #aaa; border-bottom: 2px solid #444; }
.data-table td { padding: 15px 12px; border-bottom: 1px solid #333; }
.badge { padding: 5px 10px; border-radius: 4px; font-size: 12px; font-weight: bold; }
.bg-danger { background-color: rgba(239, 68, 68, 0.2); color: #ef4444; }
.bg-warning { background-color: rgba(245, 158, 11, 0.2); color: #f59e0b; }
.bg-safe { background-color: rgba(16, 185, 129, 0.2); color: #10b981; }

.btn-action { padding: 6px 12px; border: none; border-radius: 4px; font-size: 12px; cursor: pointer; margin: 0 4px; }
.btn-action.primary { background-color: #3b82f6; color: white; }
.btn-action.delete { background-color: #ef4444; color: white; }

.placeholder-box { background-color: #333; height: 180px; border-radius: 8px; display: flex; justify-content: center; align-items: center; color: #888; }
</style>