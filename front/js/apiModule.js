const serverUrl = 'http://localhost:8080';
const imageUrl = 'https://image.tmdb.org/t/p/w600_and_h900_bestv2';
let apiModule = (function() {
  // 기본 설정
  let settings = {
     //baseUrl: 'http://localhost:8080', // 기본 URL, 필요에 따라 변경
    // baseUrl: 'http://3.34.139.188:8080', // 기본 URL, 필요에 따라 변경
    baseUrl: 'https://api.filmfly.life', // 기본 URL, 필요에 따라 변경
  };

  // 내부 함수: Ajax 요청을 보내는 함수
  function ajaxRequest(method, url, data, successCallback, errorCallback, options = {}) {
    let fullUrl = settings.baseUrl + url;
    $.ajax({
      url: fullUrl,
      type: method,
      data: options.processData === false ? data : (data ? JSON.stringify(data) : null),
      headers: settings.headers,
      contentType: options.contentType !== undefined ? options.contentType : 'application/json',
      processData: options.processData !== undefined ? options.processData : true,
      enctype: options.enctype,
      xhrFields: {
        withCredentials: true
      },
      success: function(result, status, xhr) {
        console.log(`요청 성공: ${method} ${fullUrl}\n`, result);
        if (successCallback) successCallback(result);
      },
      error: function(xhr, status, error) {
        console.error(`요청 실패:  ${method} ${fullUrl}\n`
            + `상태 코드 : ${xhr.status}\n`
            + `응답 데이터 : ${JSON.stringify(xhr, null , 2)}\n`
        );
        if (errorCallback) errorCallback(xhr, status, error);
      }
    });
  }

  // GET 요청
  function GET(url, successCallback, errorCallback) {
    ajaxRequest('GET', url, null, successCallback, errorCallback);
  }

  // POST 요청
  function POST(url, data, successCallback, errorCallback, options) {
    ajaxRequest('POST', url, data, successCallback, errorCallback, options);
  }

  // PATCH 요청
  function PATCH(url, data, successCallback, errorCallback, options) {
    ajaxRequest('PATCH', url, data, successCallback, errorCallback, options);
  }

  // PUT 요청
  function PUT(url, data, successCallback, errorCallback, options) {
    ajaxRequest('PUT', url, data, successCallback, errorCallback, options);
  }

  // DELETE 요청
  function DELETE(url, successCallback, errorCallback, options) {
    ajaxRequest('DELETE', url, null, successCallback, errorCallback, options);
  }

  // DELETE + DATA 요청
  function DELETE_DATA(url, data, successCallback, errorCallback, options) {
    ajaxRequest('DELETE', url, data, successCallback, errorCallback, options);
  }

  // 외부에 공개할 API
  return {
    GET: GET,
    POST: POST,
    PATCH: PATCH,
    PUT: PUT,
    DELETE: DELETE,
    DELETE_DATA: DELETE_DATA,
    settings: settings // 설정을 외부에서 수정 가능
  };
})();