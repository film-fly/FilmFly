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
      }
    }
  }
});

function imageUploader(file, el) {
  let formData = new FormData();
  formData.append('file', file);

  $.ajax({
    data : formData,
    type : "POST",
    url : '/image/upload',
    contentType : false,
    processData : false,
    enctype : 'multipart/form-data',
    success : function(data) {
      console.log(data);
      let imageUrl = window.location.origin + data;
      console.log('url: ' + imageUrl);
      $(el).summernote('insertImage', imageUrl, function($image) {
        $image.css('width', "25%");
      });
    }
  });
}