app.controller("payController", function ($scope, $location, cartService, payService) {

    $scope.getUsername = function () {
        cartService.getUsername().success(function (response) {
            $scope.username = response.username;

        });

    };

    //生成二维码
    $scope.createNative = function () {
        //获取支付日志id
        $scope.outTradeNo = $location.search()["outTradeNo"];

        payService.createNative($scope.outTradeNo).success(function (response) {
            if("SUCCESS"== response.result_code){
                //如果生成订单成功；则生成二维码
                $scope.totalFee = (response.total_fee/100).toFixed(2);

                //创建二维码图片
                var qr = new QRious({
                    element:document.getElementById("qrious"),
                    level:"Q",
                    size:250,
                    value:response.code_url
                });

            } else {
                alert("生成支付二维码失败");
            }

        });

    };

});