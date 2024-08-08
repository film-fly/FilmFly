$(function () {
  // 헤더 삽입
  $('#common-header').load('../common/common-header.html', function() {
    console.log('Header loaded.');

    // 로그인 상태 확인 함수
    function checkLoginStatus() {
      const isLoggedIn = localStorage.getItem('isLoggedIn') === 'true';
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
      if (confirm('로그아웃하시겠습니까?')) {
        apiModule.POST('/users/logout', {},
            function (result) {
              alert('로그아웃되었습니다.');
              localStorage.setItem('isLoggedIn', 'false'); // 로그인 상태 업데이트
              localStorage.removeItem('userRole'); // 사용자 역할 정보 제거
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
