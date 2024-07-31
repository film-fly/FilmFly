$('#summernote').summernote({
  height: 500,
  placeholder: '내용',
  lang: 'ko-KR',
  toolbar: [
    ['fontsize', ['fontsize']],
    ['style', ['bold', 'italic', 'underline', 'strikethrough', 'clear']],
    ['color', ['color']],
    ['para', ['ul', 'ol', 'paragraph']],
    ['insert', ['picture', 'link']]
  ],
  fontNames: ['Arial', 'Arial Black', 'Comic Sans MS', 'Courier New', '맑은 고딕', '궁서', '굴림체', '굴림', '돋음체', '바탕체'],
  fontSizes: ['8', '9', '10', '11', '12', '14', '16', '18', '20', '22', '24', '28', '30', '36', '50', '72', '96'],
  focus: true,
  callbacks: {
    onImageUpload: function (files, editor, welEditable) {
      // 다중 이미지 처리를 위해 for문을 사용했습니다.
      for (var i = 0; i < files.length; i++) {
        imageUploader(files[i], this);
        //if(files[i].size > 1024*1024*5){alert("이미지는 5MB 미만입니다.")}
      }
    },
    onMediaDelete: function ($target, editor, $editable) {
      // 삭제된 이미지의 파일 이름을 알아내기 위해 split 활용
      //if (confirm('이미지를 삭제하시겠습니까?')) { 확인 누르면 삭제됨 취소 누르면 섬머보드에서만 지워지고 서버에는 남음
        var deletedImageUrl = $target.attr('src').split('/').pop()
        imageDelete(deletedImageUrl)
    },
  }
});

function imageUploader(file, el) {
  let formData = new FormData();
  formData.append('file', file);

  $.ajax({
    data : formData,
    type : "POST",
    url : 'http://localhost:8080/image/upload',
    contentType : false,
    processData : false,
    enctype : 'multipart/form-data',
    success : function(data) {
      console.log(data);
      // let imageUrl = window.location.origin + data;
      let imageUrl = 'http://localhost:8080' + data;
      console.log('url: ' + imageUrl);
      $(el).summernote('insertImage', imageUrl, function($image) {
        $image.css('width', "25%");
      });
    }
  });
}

// 이미지 삭제 ajax
function imageDelete(imageName) {
  data = new FormData()
  data.append('imageName', imageName)
  $.ajax({
    data: data,
    type: 'DELETE',
    url: 'http://localhost:8080/image/delete',
    contentType: false,
    enctype: 'multipart/form-data',
    processData: false,
  })
}