//继承处理器，其它的处理器中公共使用到的配置、变量、方法都可以放置在这里，然后
//让那些处理器继承该处理器即可
app.controller("baseController", function ($scope) {
    // 初始化分页参数
    $scope.paginationConf = {
        currentPage:1,// 当前页号
        totalItems:10,// 总记录数
        itemsPerPage:10,// 页大小
        perPageOptions:[10, 20, 30, 40, 50],// 可选择的每页大小
        onChange: function () {// 当上述的参数发生变化了后触发
            $scope.reloadList();
        }
    };

    $scope.reloadList = function () {
        //$scope.findPage($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
        $scope.search($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);

    };

    //在列表中复选框选中了的那些id数组
    $scope.selectedIds = [];


    //选中或反选；event是当前点击的元素事件
    $scope.updateSelection = function ($event, id) {
        if($event.target.checked){
            $scope.selectedIds.push(id);
        } else {
            //查询id在数组中的索引号
            var index = $scope.selectedIds.indexOf(id);
            //在数组中删除某个位置的元素
            //参数1：要删除的元素索引号，参数2：删除的个数
            $scope.selectedIds.splice(index, 1);
        }

    };

    //将一个json列表字符串中的某个属性的值串起来并返回
    $scope.jsonToString = function (jsonListStr, key) {
        var str = "";
        //将json字符串转换为js对象
        var jsonArray = JSON.parse(jsonListStr);
        for (var i = 0; i < jsonArray.length; i++) {
           var jsonObj = jsonArray[i];

           if(str.length > 0){
               str += "," + jsonObj[key];
           } else {
               str = jsonObj[key];
           }
        }

        return str;
    };
});