let apiModule = (function() {
  // 기본 설정
  let settings = {
    baseUrl: 'https://localhost', // 기본 URL, 필요에 따라 변경
    headers: {
      'Content-Type': 'application/json',
      // 추가 헤더 설정 가능
    }
  };

  // 내부 함수: Ajax 요청을 보내는 함수
  function ajaxRequest(method, url, data, successCallback, errorCallback) {
    $.ajax({
      url: settings.baseUrl + url,
      type: method,
      data: data ? JSON.stringify(data) : null,
      headers: settings.headers,
      xhrFields: {
        withCredentials: true
      },
      success: function(result, status, xhr) {
        console.log("성공 : " + JSON.stringify(result));
        if (successCallback) successCallback(result);
      },
      error: function(xhr, status, error) {
        console.error("에러: " + xhr.responseText);
        if (errorCallback) errorCallback(xhr, status, error);
      }
    });
  }

  // GET 요청
  function GET(url, successCallback, errorCallback) {
    ajaxRequest('GET', url, null, successCallback, errorCallback);
  }

  // POST 요청
  function POST(url, data, successCallback, errorCallback) {
    ajaxRequest('POST', url, data, successCallback, errorCallback);
  }

  // PATCH 요청
  function PATCH(url, data, successCallback, errorCallback) {
    ajaxRequest('PATCH', url, data, successCallback, errorCallback);
  }

  // PUT 요청
  function PUT(url, data, successCallback, errorCallback) {
    ajaxRequest('PUT', url, data, successCallback, errorCallback);
  }

  // DELETE 요청
  function DELETE(url, successCallback, errorCallback) {
    ajaxRequest('DELETE', url, null, successCallback, errorCallback);
  }

  // 외부에 공개할 API
  return {
    GET: GET,
    POST: POST,
    PATCH: PATCH,
    PUT: PUT,
    DELETE: DELETE,
    settings: settings // 설정을 외부에서 수정 가능
  };
})();