/*
$http({
    method: 'POST',
    url: url,
    data: xsrf,
    headers: {'Content-Type': 'application/x-www-form-urlencoded'}
})
*/


/*
function IdeaCreateCtrl($scope, $http) {
    $scope.doCreate = function() {
        $http({
            method: 'POST',
            url: '/createIdea'
        })
    }
}
*/

var app = angular.module('IdeaCreateApp', []);
app.controller('IdeaCreateCtrl', function($scope, $http){
            alert('fo:x-www-form-urlencoded')
    $scope.doCreate = function(){
        $http.post('/createIdea').success(function(data) {
            // body...
        })
    }
            }    
})