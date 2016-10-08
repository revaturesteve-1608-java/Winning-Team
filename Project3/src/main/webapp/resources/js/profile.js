/**
 * 
 */
angular.module('routingApp')

.controller("profileCtrl", function($scope, createUserService) {
	$scope.createUser = function(person) {
		console.log('About to create ' + person.first_name);
		createUserService.createUser(person);
	}
	
	$scope.getRoles = createUserService.getRoles(
			// pass in the callback function
			function(response) {
				$scope.roles = response.data;
			})

});

/*
 * Service
 * 
 * -Services allow you to create a reusable set of functions and values that can
 * be passed across the application. 
 * -Services are useful for getting data from
 * a database, as well as firing save, edit, and delete operations 
 * -Services can share data between controllers
 */
app.service('profileService', function($http, $q, $window) {
	
	this.createUser = function(person) {
		$http.post('rest/createUser', person).then(function(response) {
			console.log(response + ' YAY!');
			$window.alert(response.data);
		}, function(error) {
			console.log($q.reject(error));
		});
	}
	
	this.getRoles = function(callback) {
		// callback is a function that takes a response
		$http.post('rest/getRoles').then(callback);
	}
})