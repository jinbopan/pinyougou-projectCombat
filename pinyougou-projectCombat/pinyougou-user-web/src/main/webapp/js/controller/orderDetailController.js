app.controller("orderDetailController", function ($scope, userService, $location,$interval) {

    $scope.orderStatus = ["", "立即付款", "买家已付款", "待发货", "已发货", "交易成功", "交易关闭", "去评价"];

    $scope.payType = ["", "在线支付", "货到付款"];

    //获取服务器端时间
    function getServerDate() {
        return new Date($.ajax({async: false}).getResponseHeader("Date"));
    }

    $scope.orderDetail = function () {
        var orderItemId = $location.search()["orderItemId"];
        $scope.totalNum = $location.search()["totalNum"];
        userService.orderDetail(orderItemId).success(function (response) {
            $scope.entity = response;

            //倒计时总秒数
            var allSeconds = Math.floor((new Date($scope.entity.tbOrder.expire).getTime() - getServerDate().getTime()) / 1000);
            var task = $interval(function () {
                if (allSeconds > 0) {
                    allSeconds = allSeconds - 1;
                    //转换倒计时总秒数为 **天**:**:** 的格式并在页面展示
                    $scope.timestring = convertTimeString(allSeconds);
                } else {
                    $interval.cancel(task);
                    alert("订单到时自动确认收货");
                }
            }, 1000);
        });
    };

    convertTimeString = function (allSeconds) {
        //天数
        var days = Math.floor(allSeconds / (60 * 60 * 24));
        //时
        var hours = Math.floor((allSeconds - days * 60 * 60 * 24) / (60 * 60));
        //分
        var minutes = Math.floor((allSeconds - days * 60 * 60 * 24 - hours * 60 * 60) / 60);
        //秒
        var seconds = allSeconds - days * 60 * 60 * 24 - hours * 60 * 60 - minutes * 60;

        var str = "";
        if (days > 0) {
            str = days + "天";
        }
        return str + hours + "时" + minutes + "分" + seconds;
    };

    $scope.nowNum = function (orderStatus) {
        var arr = $scope.orderStatus;
        for (var i = 0; i < arr.length; i++) {
            if (orderStatus == i) ;
            return i;
        }
    };

    //该订单当前状态
    $scope.nowStatus = function (orderStatus, status) {
        if (orderStatus == status) {
            return true;
        }
        return false;
    };


});