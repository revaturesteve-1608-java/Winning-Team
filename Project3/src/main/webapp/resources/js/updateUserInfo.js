/**
 * 
 */

angular.module('routingApp')

.controller('updateUserCtrl', function($scope, updateUserData, $window, createUserService){
	$scope.updateInformation = function(oldPassword, newPassword, username, newEmail, newPhone, 
			complex, newUniversity, newLinkedIn){
		var complexN = "";
		if(complex.complexName != null) {
			complexN = complex.complexName.complexName;
		}
		var information = [oldPassword, newPassword, username, newEmail, newPhone, newUniversity, newLinkedIn, 
		                   complexN]
		console.log(information);
		updateUserData.update(information, function(response){
			$window.alert(response.data);
			createUserService.getUser(
					function(response){
						$scope.user = response.data; 	
						$window.location.reload();
					})
			
		});
	}
	
	$scope.getComplex = updateUserData.getComplex(
			// pass in the callback function
			function(response) {
				$scope.complex = response.data;
			})
})

.directive('ngFiles', ['$parse', function ($parse) {

    function fn_link(scope, element, attrs) {
        var onChange = $parse(attrs.ngFiles);
        element.on('change', function (event) {
            onChange(scope, { $files: event.target.files });
        });
    };

    return {
        link: fn_link
    }
} ])

.controller('fupController', function ($scope, $http, $window, updateUserData) {
	
    var formdata = new FormData();
    $scope.getTheFiles = function ($files) {
    	console.log($files);
    	console.log($files[0].type.includes("image"));
    	if($files[0].type.includes("image")) {
	        angular.forEach($files, function (value, key) {
	        	formdata.append(key, value);
	            console.log(key + ' ' + value.name);
	            console.log(formdata.get(0));
	        });
    	} else {
    		$window.alert("Only image");
    	}
    	console.log(formdata.get(0));
    };

    // NOW UPLOAD THE FILES.
    $scope.uploadFiles = function () {
    	console.log(formdata.get(0));
        var request = {
            method: 'POST',
            url: 'rest/fileupload',
            data: formdata,
            fileElementId : 'file',
            headers: {
                'Content-Type': undefined,
                contentType: false,
				processData: false,
            }
        };

        // SEND THE FILES.
        $http(request)
            .success(function (d) {
                alert(d);
            })
            .error(function () {
            });
    }
})

.service('updateUserData', function($http, $window, $q, $route){
	this.update = function(information, callback){
		$http.post("rest/updateInfo", information).then(callback);
	}
	
	this.updatePics = function(formdata){
		$http.post("rest/fileupload", formdata).then(function(response) {
		}, function(error) {
			console.log($q.reject(error));
		});
	}
	
	this.getComplex = function(callback) {
		// callback is a function that takes a response
		$http.post('rest/getComplex').then(callback);
	}
})

