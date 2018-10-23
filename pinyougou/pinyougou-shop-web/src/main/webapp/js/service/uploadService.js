app.service("uploadService",function ($http) {

    this.uploadFile = function () {
        //表单数据对象
        var formData = new FormData();
        //添加一个表单项名称为file；
        formData.append("file", file.files[0]);
        return $http({
            url:"../upload.do",
            method:"post",
            data:formData,
            headers:{"Content-Type": undefined},
            transformRequest: angular.identity
        });
    };
});