$(function () {
  // 헤더 삽입
  $('#common-header').load('../common/common-header.html', function() {
    console.log('Header loaded.');

    // 로그인 상태 확인 함수
    function checkLoginStatus() {
      const isLoggedIn = localStorage.getItem('isLoggedIn') === 'true';
      const loginTime = localStorage.getItem('loginTime');
      const currentTime = new Date().getTime();

      // 로그인 시간이 설정되어 있고, 현재 시간이 로그인 시간보다 크면 세션 만료
      if (loginTime && currentTime > parseInt(loginTime)) {
        if (localStorage.getItem('isLoggedIn') === 'true') {
          localStorage.setItem('isLoggedIn', 'false');
          localStorage.removeItem('userRole');
          alert('세션이 만료되었습니다. 다시 로그인해 주세요.');
          location.href = '../html/login.html';
        }
        return false;
      }

      console.log('Check login status:', isLoggedIn);
      return isLoggedIn;
    }

    // 로그인 상태에 따라 버튼 표시
    function updateAuthLinks() {
      const userRole = localStorage.getItem('userRole');
      console.log('User role:', userRole);
      if (checkLoginStatus()) {
        $('#loginLink').hide();
        $('#logoutLink').show();
        $('#myPageLink').show();
        if (userRole === 'ROLE_ADMIN') {
          $('#adminPageLink').show();
        } else {
          $('#adminPageLink').hide();
        }
      } else {
        $('#loginLink').show();
        $('#logoutLink').hide();
        $('#myPageLink').hide();
        $('#adminPageLink').hide();
      }
    }

    // 초기 상태 설정
    updateAuthLinks();

    // 로그아웃 버튼 클릭 이벤트
    $('#logoutBtn').on('click', function(event) {
      event.preventDefault();
      if (confirm('로그아웃 하시겠습니까?')) {
        apiModule.POST('/users/logout', {},
            function (result) {
              alert('로그아웃 되었습니다.');
              localStorage.setItem('isLoggedIn', 'false'); // 로그인 상태 업데이트
              localStorage.removeItem('userRole'); // 사용자 역할 정보 제거
              localStorage.removeItem('loginTime'); // 로그인 시간 제거
              updateAuthLinks();
              location.href = '../html/index.html';
            },
            function (xhr) {
              alert('로그아웃에 실패했습니다.');
            });
      }
    });
  });
});
