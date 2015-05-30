Login = function() {
	$("#login").validate({
		rules: {
			j_username: {
				required: true
			},
			j_password: {
				required: true
			}
		},
		showErrors: myShowErrors
	});
};
