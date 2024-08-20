// 기본 프로필 이미지 경로
const defaultProfileImg = "../images/profileImg.png";

function loadUserInfo(userId, callback) {
  apiModule.GET(`/users/${userId}`, function (result) {
    if (result.statusCode === 200) {
      const userInfo = result.data;
      callback(null, userInfo);
    } else {
      callback("Failed to load user info");
      alert("Failed to load user info");
      window.history.back();
    }
  }, function () {
    callback("Server error occurred while loading user info");
  });
}

function renderUserProfile(userId,userInfo) {
  const pictureUrl = userInfo.pictureUrl ? userInfo.pictureUrl : defaultProfileImg;

  const userProfileHtml = `
    <div class="user-info d-flex align-items-center mb-4" id="user-info">
      <img id="user-picture" src="${pictureUrl}" alt="User Picture" class="rounded-circle" width="50" height="50" onerror="this.src='${defaultProfileImg}'">
      <span id="user-nickname" class="ms-3" style="font-size: 1.0rem; font-weight: bold;">${userInfo.nickname}</span>
    </div>
  `;
  $('#user-profile').html(userProfileHtml);

  // 프로필 클릭 이벤트 추가
  $('#user-info').on('click', function() {
    const newUrl = `../html/mypage.html?user=${userId}`;
    window.location.href = newUrl;
  });
}