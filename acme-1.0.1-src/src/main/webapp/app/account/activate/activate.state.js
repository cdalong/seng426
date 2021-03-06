(function () {
	'use strict';

	angular
		.module('acmeApp')
		.config(stateConfig);

	stateConfig.$inject = ['$stateProvider'];

	function stateConfig($stateProvider) {
		$stateProvider.state('activate', {
			parent: 'account',
			url: '/activate?key',
			data: {
				roles: [],
				pageTitle: 'Activation'
			},
			views: {
				'content@': {
					templateUrl: 'app/account/activate/activate.html',
					controller: 'ActivationController',
					controllerAs: 'vm'
				}
			}
		});
	}
})();
