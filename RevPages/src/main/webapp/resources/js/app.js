var app = angular.module('routingApp', ['ngRoute','ngMaterial','ngMessages','material.svgAssetsCache', 'ngCookies', 'textAngular']);

app.config(function($routeProvider) {
	
	
	
    $routeProvider
    .when('/', {
        templateUrl : 'views/dashboard.html',
        controller : 'indexCtrl'
    })
    
    .when('/userProfile', {
        templateUrl : 'views/userProfile.html',
        controller : 'profileCtrl'
    })
    
    .when('/settings', {
        templateUrl : 'views/settings.html'
        // ,controller : 'contactContoller'
    })
    
    .when('/createNewUser', {
        templateUrl : 'views/createNewUser.html',
        controller : "newUserCtrl"
    })
    
    .when('/post', {
        templateUrl : 'views/viewPost.html',
        controller : "postCtrl"
    })
    
    .otherwise({
        redirectTo : '/'
    });
})

app.controller("newUserCtrl", function($scope, createUserService) {
	$scope.createUser = function(person) {
		console.log('About to create ' + person.first_name);
		createUserService.createUser(person);
	}
	
	$scope.getRoles = createUserService.getRoles(
			
			function(response) {
				$scope.roles = response.data;
			})
	
	

})
app.controller("frontCtrl", function($scope, $http, $window, $cookies, createUserService) {
	
	$scope.user;
	
	$scope.homePage = function() {
//		console.log($cookies.user)
		console.log($scope.user.vaildated === false);
		console.log($scope.user.id);
		if($scope.user.id == 0){
//			$window.location.href = 'login.html';
			$("#myModal").modal() 
		} else {
			console.log($scope.user.vaildated);
			if($scope.user.vaildated !== false) {
				if($scope.user.role.roleName === "Moderator") {
					$window.location.href = 'moderate-view.html';
				} else {
					$window.location.href = 'associate-view.html';
				}
			} else {
				$window.location.href = 'updateTempInfo.html';
			}
			
		}
	}
	
	$scope.getUser = createUserService.getUser(
			function(response){
				
				$scope.user = response.data;
				
			})

})
/*
 * Service
 * 
 * -Services allow you to create a reusable set of functions and values that can
 * be passed across the application. 
 * -Services are useful for getting data from
 * a database, as well as firing save, edit, and delete operations 
 * -Services can share data between controllers
 */
app.service('createUserService', function($http, $q, $window) {
	
	this.createUser = function(person) {
		$http.post('rest/createUser', person).then(function(response) {
			$window.alert(response.data);
		}, function(error) {
			console.log($q.reject(error));
		});
	}
	
	this.getRoles = function(callback) {
		// callback is a function that takes a response
		$http.post('rest/getRoles').then(callback);
	}
	
	this.getUser = function(callback) {
		$http.get('rest/user').then(callback);
	}
	
	this.logout = function() {
		$http.post('rest/logout').then(function(response) {
			$window.location.href = 'index.jsp';
		}, function(error) {
			console.log($q.reject(error));
		});
	}
	
	this.getPosts = function(callback){
		$http.get("rest/getPosts").then(callback)
	}
	
	this.getForm = function(callback){
		$http.get("rest/getForm").then(callback)
	}
	
	this.getDislike = function(info, callback) {
	
	$http({method: 'POST', url: 'rest/getDislike', data: $.param({id: info}), headers: {'Content-Type': 'application/x-www-form-urlencoded'} }).then(callback);
	}
	
	this.getLike = function(info, callback) {
		
		$http({method: 'POST', url: 'rest/getLike', data: $.param({id: info}), headers: {'Content-Type': 'application/x-www-form-urlencoded'} }).then(callback);
	}
	
	this.getAllReplyDislikes = function(info, callback) {
		
		$http({method: 'POST', url: 'rest/getDislikes', data: $.param({id: info}), headers: {'Content-Type': 'application/x-www-form-urlencoded'} }).then(callback);
	}
	
	this.getAllReplyLikes = function(info, callback) {
		
		$http({method: 'POST', url: 'rest/getLikes', data: $.param({id: info}), headers: {'Content-Type': 'application/x-www-form-urlencoded'} }).then(callback);
	}
	
	this.deletePost = function(postId){
		$http.post("rest/deletePost", postId).then();
		if($window.location.href.includes("associate-view")) {
			$window.location.href = 'associate-view.html';
		} else {
			$window.location.href = 'moderate-view.html';
		}
		
	}
	
	this.getPostsByCategory = function(categoryName, callback){
		$http.post("rest/getPostsCat", categoryName).then(callback);
	}
	
	this.getAllCategories = function(callback){
		$http.post("rest/getAllCat").then(callback);
	}
	
	this.getPostsByUsername = function(username, callback){
		$http.post("rest/getPostsByUsername", username).then(callback)
	}
	
})