(function () {
	'use strict';

	angular
		.module('acmeApp')
		.config(stateConfig);

	stateConfig.$inject = ['$stateProvider'];

	function stateConfig($stateProvider) {
		$stateProvider.state('home', {
			parent: 'app',
			url: '/',
			data: {
				roles: []
			},
			views: {
				'content@': {
					templateUrl: 'app/home/home.html',
					controller: 'HomeController',
					controllerAs: 'vm'
				}
			}
		});
	}
})();
