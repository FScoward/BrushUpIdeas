//var ideaCreateApp = angular.module('IdeaCreateApp', []);
function IdeaCreateCtrl($scope, $http) {

    $scope.ideas = [];

    // 初期化
    $scope.init = function() {
        // データを取得
        $http.post('/readIdeas').success(function(data, status, headers, config){
            console.log(data);
            for (var i = data.length - 1; i >= 0; i--) {
                $scope.ideas.push(data[i]);
//                $scope.ideas.unshift(data[i]);
            };
        }).error(function (data, status, headers, config) {
            // body...
        })
    };

    $scope.doCreate = function(tw) {
        $http.post('/createIdea', $scope.idea).success(function(data, status, headers, config){
            $scope.ideas.unshift({id: '-1', content:$scope.idea.content, twitterAccount: tw});
            $scope.idea.content = '';
            $scope.successMessage = 'Yes!';
            // $scope.xbutton = '<button type="button" class="close" aria-hidden="true">&times;</button>';
        }).error(function(data, status, headers, config) {
            $scope.errorMessage = 'Oops... Error Occured!';
        })
    };
}