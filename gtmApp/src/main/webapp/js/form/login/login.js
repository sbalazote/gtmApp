Login = function() {
	$("#login").validate({
		rules: {
			username: {
				required: true
			},
			password: {
				required: true
			}
		},
		showErrors: myShowErrors
	});
};
