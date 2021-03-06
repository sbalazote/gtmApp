SaveUser = function() {
	
	var validateForm = function() {
		var form = $("#userAdministrationForm");
		form.validate({
			rules: {
				name: {
					required: true,
					maxlength: 45
				},
				password: {
					minlength: 5,
					maxlength: 15
				},
				passwordCheck: { 
	                equalTo: "#passwordInput",
	                minlength: 5,
					maxlength: 15
		        },
				active: {
					required: true
				},
				profile: {
					required: true
				}
			},
			showErrors: myShowErrors,
			onsubmit: false
		});
		return form.valid();
	};
	
	var existsUser = function() {
		var exists = false;
		$.ajax({
			url: "existsUser.do",
			type: "GET",
			async: false,
			data: {
				name: $("#nameInput").val()
			},
			success: function(response) {
				exists = response;
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myGenericError();
			}
		});
		return exists;
	};
	
	$('#userModal').on('shown.bs.modal', function () {
		  $('.chosen-select', this).chosen('destroy').chosen();
	});
	
	$("#addButton, #updateButton").click(function(e) {
		if (validateForm()) {
			var jsonUser = {
					"id": $("#idInput").val(),
					"name": $("#nameInput").val(),
					"password": $("#passwordInput").val(),
					"active": $("#activeSelect option:selected").val(),
					"profileId": $("#profileSelect").val()
			};

			//	si existe el codigo y ademas no se trata de una actualizacion, lanzo modal.
			if (existsUser() && (e.currentTarget.id === 'addButton')) {
				myShowAlert('danger', 'Usuario existente. Por favor, ingrese uno diferente.', 'userModalAlertDiv');
			} else {
				$.ajax({
					url: "saveUser.do",
					type: "POST",
					contentType:"application/json",
					data: JSON.stringify(jsonUser),
					async: true,
					success: function(response) {
						if (response.id === parseInt($("#idInput").val())) {
							myUpdateSuccessful();
						} else {
							myCreateSuccessful();
						}
						$('#userModal').modal('hide');
						$("#usersTable").bootgrid("reload");
					},
					error: function(jqXHR, textStatus, errorThrown) {
						myGenericError();
					}
				});
			}
		}
	});
	
	$(".alert .close").on('click', function(e) {
	    $(this).parent().hide();
	});
	
	$("#userAdministrationForm input, #userAdministrationForm select").keypress(function(event) {
		return event.keyCode != 13;
	});
	
	$("#changePasswordSelect").change(function(){
		var value = $(this).find("option:selected").val();
		if (value == "true") {
			$("#passwordInput").prop("disabled", false);
			$("#passwordInputCheck").prop("disabled", false);
		} else {
			$("#passwordInput").prop("disabled", true);
			$("#passwordInputCheck").prop("disabled", true);
			$("#passwordInput").val("");
			$("#passwordInputCheck").val("");
			$('#passwordInput').data("title", "").removeClass("has-error").tooltip("destroy");
			$('#passwordInputCheck').data("title", "").removeClass("has-error").tooltip("destroy");
		}
	});

};