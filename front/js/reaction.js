const urlParams = new URL(location.href).searchParams;

// 좋아요 or 싫어요의 id 값 초기화
$(document).ready(function() {
  if (urlParams.get('movie')) {
    const movieId = urlParams.get('movie');
    $('.btnReaction[data-type="MOVIE"]').attr('data-content-id', movieId);
    $('#btnFavorite').attr('data-content-id', movieId);
  }

  if (urlParams.get('review')) {
    const reviewId = urlParams.get('review');
    $('.btnReaction[data-type="REVIEW"]').attr('data-content-id', reviewId);
  }

  if (urlParams.get('board')) {
    const boardId = urlParams.get('board');
    $('.btnReaction[data-type="board"]').attr('data-content-id', boardId);
  }
  // 댓글은 다른방식으로
});

// 좋아요 or 싫어요를 클릭했을 때
$(document).on('click', '.btnReaction', function () {
  let button = $(this);
  let isAdding;
  let isGood;

  if (button.hasClass('btn-outline-primary')) {
    button.removeClass('btn-outline-primary').addClass('btn-primary');
    isAdding = true;
  } else if (button.hasClass('btn-primary')) {
    button.removeClass('btn-primary').addClass('btn-outline-primary');
    isAdding = false;
  }

  if (button.attr('data-good')) {
    isGood = true;
  } else if (button.attr('data-bad')) {
    isGood = false;
  }

  let contentType = button.attr('data-type');
  let contentId = button.attr('data-content-id');
  let data = {
    contentId: contentId,
    contentType: contentType
  };

  let test = '';
  isAdding ? test += 'POST : ' : test += 'DELETE : ';
  isGood ? test += '/goods' : test += '/bads';
  alert(test + '\n' + JSON.stringify(data));

  // $.ajax({
  //   type: isAdding ? 'POST' : 'DELETE',
  //   url: isGood ? '/goods' : '/bads',
  //   data: JSON.stringify(data),
  //   contentType: "application/json;charset=utf-8"
  // })
  // .done(function (result, status, xhr) {
  //   let reactionType = '';
  //   isGood ? reactionType = 'data-good-id' : reactionType = 'data-bad-id';
  //   if (isAdding) {
  //     button.attr(reactionType, result.goodId);
  //   } else {
  //     button.removeAttr(reactionType);
  //   }
  // })
  // .fail(function (xhr, status, er) {
  //   alert("리액션 저장 실패");
  // });
});

// 신고 할 content id 초기화
$(document).ready(function () {
  if (urlParams.get('review')) {
    let reviewId = urlParams.get('review');

  }
});

// 닉네임 옆 톱니바퀴의 차단, 신고를 클릭했을 때
$(document).ready(function() {
  // 차단 버튼 클릭 이벤트 처리
  $(document).on('click', '.block-user', function(e) {
    e.preventDefault();
    let userId = $(this).closest('.profile-box').attr('data-user-id');
    alert('user id : ' + userId);
    // 실제 차단 기능 구현 코드
  });

  // 신고 버튼 클릭 이벤트 처리
  $(document).on('click', '.report-user', function(e) {
    e.preventDefault();
    let userId = $(this).closest('.profile-box').attr('data-user-id');
    let contentId = $(this).closest('.profile-box').attr('data-content-id');
    alert('user id : ' + userId + "\n신고할 content id : " + contentId);
    // 실제 신고 기능 구현 코드
  });
});