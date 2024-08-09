const jsUrlParams = new URLSearchParams(window.location.search);

// 좋아요 or 싫어요의 id 값 초기화
$(document).ready(function() {
  if (jsUrlParams.get('movie')) {
    const movieId = jsUrlParams.get('movie');
    $('.btnReaction[data-type="MOVIE"]').attr('data-content-id', movieId);
    $('#btnFavorite').attr('data-content-id', movieId);
  }

  if (jsUrlParams.get('review')) {
    const reviewId = jsUrlParams.get('review');
    $('.btnReaction[data-type="REVIEW"]').attr('data-content-id', reviewId);
  }

  if (jsUrlParams.get('board')) {
    const boardId = jsUrlParams.get('board');
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
  if (jsUrlParams.get('review')) {
    let reviewId = jsUrlParams.get('review');

  }
});

// 닉네임 옆 톱니바퀴의 차단, 신고를 클릭했을 때
$(document).ready(function() {
  let selectedUserId = null;
  let selectedContentId = null;
  let selectedContentType = null;

  // 차단 버튼 클릭 이벤트 처리
  $(document).on('click', '.block-user', function(e) {
    e.preventDefault();
    $('#blockReportModal').removeAttr('data-block');
    $('#blockReportModal').removeAttr('data-report');
    selectedUserId = $(this).closest('.dropdown-menu').attr('data-user-id');
    selectedContentId = $(this).closest('.dropdown-menu').attr('data-content-id');
    selectedContentType = $(this).closest('.dropdown-menu').attr('data-content-type');

    $('#inputBlockReport').val(''); // 텍스트 지우기
    $('#blockReportModalLabel').text('차단하기');
    $('#reasonLabel').text('차단 사유');
    $('#btnBlockReport').text('차단하기');
    $('#blockReportModal').attr('data-block', true);
    $('#blockReportModal').modal('show'); // 모달 표시
  });

  // 신고 버튼 클릭 이벤트 처리
  $(document).on('click', '.report-user', function(e) {
    e.preventDefault();
    $('#blockReportModal').removeAttr('data-block');
    $('#blockReportModal').removeAttr('data-report');
    selectedUserId = $(this).closest('.dropdown-menu').attr('data-user-id');
    selectedContentId = $(this).closest('.dropdown-menu').attr('data-content-id');
    selectedContentType = $(this).closest('.dropdown-menu').attr('data-content-type');

    $('#inputBlockReport').val(''); // 텍스트 지우기
    $('#blockReportModalLabel').text('신고하기');
    $('#reasonLabel').text('신고 사유');
    $('#btnBlockReport').text('신고하기');
    $('#blockReportModal').attr('data-report', true);
    $('#blockReportModal').modal('show'); // 모달 표시
  });

  // btnBlockReport 버튼 클릭 이벤트 처리
  $('#btnBlockReport').on('click', function() {
    if ($('#blockReportModal').attr('data-block')) {
      let memo = $('#inputBlockReport').val();

      let data = {
        "blockedId": selectedUserId,
        "memo": memo
      };

      //alert('차단하기\nuser id : ' + selectedUserId + '\ncontent id : ' + selectedContentId + '\ncontent type : ' + selectedContentType);
      blockUser(data);
    } else if ($('#blockReportModal').attr('data-report')) {
      let reason = $('#inputBlockReport').val();

      let data = {
        "reportedId": selectedUserId,
        "typeId": selectedContentId,
        "type": selectedContentType.toUpperCase(),
        "reason": reason
      };

      //alert('신고하기\nuser id : ' + selectedUserId + '\ncontent id : ' + selectedContentId + '\ncontent type : ' + selectedContentType);
      reportUser(data);
    } else {
      alert('신고, 차단 안됨');
    }

  });
});

function blockUser(data) {
  apiModule.POST('/blocks', data,
      function (result) {
        if (result.statusCode === 200) {
          console.log(result);
          alert("선택한 유저를 차단했습니다.");
          location.reload();
        }
      },
      function (result) {
        alert(result.responseJSON.message);
      });
}

function reportUser(data) {
  apiModule.POST('/reports', data,
      function (result) {
        if (result.statusCode === 200) {
          console.log(result);
          alert("신고가 접수되었습니다.");
          location.reload();
        }
      },
      function (result) {
        console.log("fail : " + result);
      });
}
