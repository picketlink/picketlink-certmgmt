var picketlinkCertMgmtApp = angular.module('picketlinkCertMgmtApp', [
		'ngRoute', 'picketlinkCertMgmtControllers',
		'picketlinkCertMgmtDirectives', ]);

picketlinkCertMgmtApp.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/', {
		templateUrl : 'jsp/picketlink-cert-mgmt.html'
	}).when('/create', {
		templateUrl : 'jsp/picketlink-cert-create.html',
		controller : 'CreateCertificateController'
	}).when('/view', {
		templateUrl : 'jsp/picketlink-cert-view.html',
		controller : 'ViewCertificateController'
	}).when('/view/:keyPassword', {
		templateUrl : 'jsp/picketlink-cert.html',
		controller : 'GetCertificateController'
	}).when('/edit', {
		templateUrl : 'jsp/picketlink-cert-edit.html',
		controller : 'UpdateCertificateController'
	}).otherwise({
		redirectTo : '/'
	});
} ]);
