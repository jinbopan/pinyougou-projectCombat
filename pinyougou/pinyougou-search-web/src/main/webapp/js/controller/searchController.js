app.controller("searchController", function ($scope, searchService) {

    $scope.search = function () {
        searchService.search($scope.searchMap).success(function (response) {
            $scope.resultMap = response;
        });

    };

    //定义查询和过滤条件
    $scope.searchMap = {"keywords":"", "brand":"", "category":"","spec":{}, "price":""};

    //添加过滤条件
    $scope.addSearchItem = function (key, value) {
        if("brand" == key || "category" == key || "price" == key){
            $scope.searchMap[key] = value;
        } else {
            //规格的选项
            $scope.searchMap.spec[key] = value;
        }

        //重新查询
        $scope.search();
    };

    //删除过滤条件值
    $scope.removeSearchItem = function (key) {
        if("brand" == key || "category" == key || "price" == key){
            $scope.searchMap[key] = "";
        } else {
            //规格的选项
            delete $scope.searchMap.spec[key];
        }
        //重新查询
        $scope.search();
    };
});