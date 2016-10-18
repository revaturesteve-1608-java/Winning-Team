/**
 * 
 */
var app = angular.module('routingApp');
app.config(function($mdThemingProvider) {
	  $mdThemingProvider.theme('custom').backgroundPalette('blue');
	})
app.controller("indexCtrl", function($scope, $http, $window, $location, $cookies, createUserService, $mdDialog) {
	
	$scope.user;
	
	console.log($scope.user);
	
	
	
	$scope.logout = function(){
//		$scope.user.id = 0;
		
		createUserService.logout();
	//	$window.location.href = 'login.html';
		
	}
	
	$scope.getUser = createUserService.getUser(
			function(response){
				
				console.log($window.location.href.includes("associate-view"));
				$scope.user = response.data; 	
				if($scope.user.id == 0){
					$window.location.href = 'login.html';
//					$("#myModal").modal() 
				} else {
					console.log($scope.user.role.roleName === "Assoicate");
					console.log($window.location.href.includes("moderate-view"));
					if($scope.user.vaildated !== false) {
						if($scope.user.role.roleName === "Moderator" && $window.location.href.includes("associate-view")) {
							$window.location.href = 'moderate-view.html';
						} else if($scope.user.role.roleName === "Assoicate" && $window.location.href.includes("moderate-view")){
							$window.location.href = 'associate-view.html';
						}
					} else {
						$window.location.href = 'updateTempInfo.html';
					}
					
				}
				if($scope.user.id == 0){
					$window.location.href = 'index.jsp';	
				}
				
				console.log($scope.user);
				
			})
	
	
	$scope.allPosts;
			
	$scope.getPost = createUserService.getPosts(function(response){
		$scope.allPosts = response.data;
		console.log($scope.allPosts);
	})
	
	createUserService.getAllCategories(function(response){
			$scope.allCategories = response.data;
			var newArray = $scope.allCategories.slice(0); //clone the array, or you'll end up with a new "None" option added to your "values" array on every digest cycle.
	        newArray.unshift("None");
	        $scope.allCategories = newArray
		})
	
	$scope.getPostsByCategory = function(catName){
		console.log("GOT INTO HERERERER")
		if(catName === "None"){
			createUserService.getPosts(function(response){
				console.log("GOT BACKKKKK")
				$scope.allPosts = response.data;
				$scope.$apply()
			})
		} else{
			createUserService.getPostsByCategory(catName, function(response){
				$scope.allPosts = response.data;
				$scope.$apply()
			})
		}
	}
			
			
			function DialogController($scope, $mdDialog) {
	    $scope.hide = function() {
	      $mdDialog.hide();
	    };

	    $scope.cancel = function() {
	      $mdDialog.cancel();
	    };

	    $scope.answer = function(answer) {
	      $mdDialog.hide(answer);
	    };
	  }
 
 function customAlert(ev) {
	    $mdDialog.show({
	      controller: DialogController,
	      templateUrl: 'dialog1.tmpl.html',
	      parent: angular.element(document.body),
	      targetEvent: ev,
	      clickOutsideToClose:true,
	      fullscreen: $scope.customFullscreen
	    });
};

})