<template>
  <div class="app-container">
    <!-- 상단 네비게이션 바 -->
    <header class="top-nav">
      <div class="logo">K-NANCE</div>
      <nav class="menu">
        <RouterLink to="/" class="nav-item">Credit Management</RouterLink>
        <RouterLink to="/investment" class="nav-item">Investment Agent</RouterLink>
      </nav>
      <div class="user-profile">
        <span>🔔</span>
        <span>⚙️</span>
        <div class="avatar">👩‍💼</div>
      </div>
    </header>

    <!-- 메뉴를 누를 때마다 실제 화면이 바뀌는 영역 -->
    <main class="main-content">
      <RouterView />
    </main>
  </div>
</template>

<style>
/* 기본 여백 초기화 및 피그마의 다크 테마 배경 설정 */
body {
  margin: 0;
  padding: 0;
  background-color: #1e1e1e; 
  color: white;
  font-family: 'Arial', sans-serif;
}

.app-container {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.top-nav {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 40px;
  background-color: #222222;
  border-bottom: 2px solid #3b82f6; /* 파란색 라인 포인트 */
}

.logo {
  font-size: 24px;
  font-weight: 900;
  letter-spacing: 1px;
}

.menu {
  display: flex;
  gap: 50px;
}

.nav-item {
  color: #888888;
  text-decoration: none;
  font-weight: bold;
  font-size: 16px;
  padding-bottom: 5px;
}

/* 현재 선택된 메뉴일 때의 스타일 */
.nav-item.router-link-exact-active {
  color: #ffffff;
  border-bottom: 3px solid #3b82f6;
}

.user-profile {
  display: flex;
  gap: 20px;
  align-items: center;
  font-size: 20px;
}

.avatar {
  background-color: #e0e0e0;
  border-radius: 50%;
  width: 32px;
  height: 32px;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>