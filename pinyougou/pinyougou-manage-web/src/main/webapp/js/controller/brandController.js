//定义处理器
app.controller("brandController", function ($scope, $http, brandService) {

    $scope.findAll = function () {
        //发送请求到后台获取数据
        brandService.findAll().success(function (response) {
            //将返回结果设置到一个上下文变量中
            $scope.list = response;
        }).error(function () {
            alert("加载数据失败");
        });

    };

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

    //根据分页信息查询数据
    $scope.findPage = function (pageNo, rows) {
        brandService.findPage(pageNo, rows).success(function (response) {
            //response是分页对象（列表，总记录数）
            $scope.list = response.rows;
            //给分页组件重新设置最新的总记录数
            $scope.paginationConf.totalItems = response.total;
        });

    };

    //保存
    $scope.save = function () {
        var obj;
        if($scope.entity.id != null){
            //修改
            obj = brandService.update($scope.entity);
        } else {
            obj = brandService.add($scope.entity);
        }

        obj.success(function (response) {
            if(response.success){
                //如果操作成功；则刷新列表
                $scope.reloadList();
            } else {
                alert(response.message);
            }

        });

    };

    //根据id查询
    $scope.findOne = function (id) {
        //根据id到后台查询数据
        brandService.findOne(id).success(function (response) {
            $scope.entity = response;
        });
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

    //删除
    $scope.delete = function () {
        if($scope.selectedIds.length == 0){
            alert("请先选择要删除的记录");
            return;
        }
        //confirm会弹出确认窗口；如果点击 确定则返回true,否则false
        if(confirm("你确定要删除选中了的那些记录吗？")){
            brandService.delete($scope.selectedIds).success(function (response) {
                if(response.success){
                    //刷新列表
                    $scope.reloadList();
                    //重置数组
                    $scope.selectedIds = [];
                } else {
                    alert(response.message);
                }

            });
        }

    };

    //初始化查询对象；为了避免后台接收时候出现解析错误
    $scope.searchEntity = {};

    //条件分页查询
    $scope.search = function (pageNo, rows) {
        brandService.search(pageNo, rows, $scope.searchEntity).success(function (response) {
            $scope.list = response.rows;

            //更新分页组件的总记录数
            $scope.paginationConf.totalItems = response.total;
        });

    };
});