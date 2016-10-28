<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>RevPage</title>
<script src="http://code.jquery.com/jquery.min.js"></script>

<!-- bootstrap -->
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

<!-- Optional theme -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">

<!-- Latest compiled and minified JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
<script src="https://use.fontawesome.com/8d8fa1da46.js"></script>

<link rel="stylesheet" href="resources/css/index.css">

<script src='https://ajax.googleapis.com/ajax/libs/angularjs/1.5.6/angular.js'></script>
<script src="scripts/angular-route.min.js"></script>
<!-- angular cookies -->
<script src="//ajax.googleapis.com/ajax/libs/angularjs/1.5.6/angular-cookies.js"></script>

<script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/angular-ui-bootstrap/0.13.4/ui-bootstrap.min.js"></script>
<script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/angular-ui-bootstrap/0.13.4/ui-bootstrap-tpls.min.js"></script>
<script
	src="http://ajax.googleapis.com/ajax/libs/angularjs/1.5.5/angular-animate.min.js"></script>
<script
	src="http://ajax.googleapis.com/ajax/libs/angularjs/1.5.5/angular-aria.min.js"></script>
<script
	src="http://ajax.googleapis.com/ajax/libs/angularjs/1.5.5/angular-messages.min.js"></script>
<script type="text/javascript"
	src="https://s3-us-west-2.amazonaws.com/s.cdpn.io/t-114/svg-assets-cache.js"></script>
	<script
		src='https://ajax.googleapis.com/ajax/libs/angularjs/1.2.4/angular-sanitize.min.js'></script>
	<script
		src='http://cdnjs.cloudflare.com/ajax/libs/textAngular/1.1.2/textAngular.min.js'></script>
<script type="text/javascript"
	src="https://cdn.gitcdn.link/cdn/angular/bower-material/v1.1.1/angular-material.js"></script>
<script src="resources/js/app.js"></script>
<script type="text/javascript" src="resources/js/loginController.js"></script>
</head>
<body data-ng-app="routingApp">

<nav class="navbar navbar-inverse navbar-fixed-top">
	<div class="container-fluid">
		<!-- Brand and toggle get grouped for better mobile display -->
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed"
				data-toggle="collapse" data-target="#index-navbar"
				aria-expanded="false">
				<span class="sr-only">Toggle navigation</span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="index.jsp">Revature</a>
		</div>

		<!-- Collect the nav links, forms, and other content for toggling -->
		<div class="collapse navbar-collapse" id="index-navbar" 
			data-ng-controller="frontCtrl">
			<ul class="nav navbar-nav navbar-right">
				<li>

				<button type="button" class="btn btn-danger"

				
					data-target="#myModal" 

					data-ng-click="homePage()">Login</button>
				</li>
			</ul>
		</div>
		<!-- /.navbar-collapse -->
	</div>
	<!-- /.container-fluid -->
</nav>

<!-- The Modal -->
<div id="myModal" class="modal" data-ng-controller="loginController">

  <!-- Modal Content -->

    <div class="container">
        <div class="card card-container">
            <img id="profile-img" class="profile-img-card" src="resources/imgs/favicon_192.png" />
            <form class="form-signin">
                <input type="text" id="inputUsername" class="form-control" placeholder="Username" 
                	data-ng-model="person.username" required autofocus>
                <input type="password" id="inputPassword" class="form-control" placeholder="Password" 
                	data-ng-model="person.password" required>
                <button class="btn btn-lg btn-primary btn-block btn-signin" type="submit"
                	data-ng-click="signIn(person)">Sign in</button>
            </form><!-- /form -->
            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        </div><!-- /card-container -->
    </div><!-- /container -->
    <script type="text/ng-template" id="dialog1.tmpl.html">
		<md-dialog aria-label="">
    		<md-toolbar>
      			<div class="md-toolbar-tools">
        			<h2>User not found</h2>
        			<span flex></span>
        			<md-button class="md-icon-button" ng-click="cancel()">
          				Ok
        			</md-button>
      			</div>
    		</md-toolbar>
	 		<md-dialog-content>
	 			<h5>Please try again</h5>
			</md-dialog-content>
		</md-dialog>
	</script>
</div>

<div id="myCarousel" class="carousel slide" data-ride="carousel">
	<!-- Indicators -->
	<ol class="carousel-indicators">
		<li data-target="#myCarousel" data-slide-to="0" class="active"></li>
		<li data-target="#myCarousel" data-slide-to="1"></li>
		<li data-target="#myCarousel" data-slide-to="2"></li>
	</ol>

	<!-- Wrapper for slides -->
	<div class="carousel-inner" role="listbox">
		<div class="item active">
			<img src="resources/imgs/emily-higgins.jpg" alt="Revature">
			<div class="carousel-caption" data-ng-controller="frontCtrl">
			</div>
		</div>

		<div class="item">
			<img src="resources/imgs/chris-olney.jpg" alt="Chris Olney">
			<div class="carousel-caption" data-ng-controller="frontCtrl">
			</div>
		</div>

		<div class="item">
			<img src="resources/imgs/robert-rolle-business-development-manager.jpg" alt="robert-rolle">
			<div class="carousel-caption" data-ng-controller="frontCtrl">
			</div>
		</div>
	</div>

	<!-- Left and right controls -->
	<a class="left carousel-control" href="#myCarousel" role="button"
		data-slide="prev"> <span class="glyphicon glyphicon-chevron-left"
		aria-hidden="true"></span> <span class="sr-only">Previous</span>
	</a> <a class="right carousel-control" href="#myCarousel" role="button"
		data-slide="next"> <span
		class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
		<span class="sr-only">Next</span>
	</a>
</div>
</body>
</html>