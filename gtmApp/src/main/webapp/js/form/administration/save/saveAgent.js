SaveAgent = function() {
	
	var validateForm = function() {
		var form = $("#agentAdministrationForm");
		form.validate({
			rules: {
				code: {
					required: true,
					digits: true,
					maxlength: 9,
				},
				description: {
					required: true,
					maxlength: 45,
				},
				active: {
					required: true
				}
			},
			showErrors: myShowErrors,
			onsubmit: false
		});
		return form.valid();
	};
	
	var existsAgent = function() {
		var exists = false;
		$.ajax({
			url: "existsAgent.do",
			type: "GET",
			async: false,
			data: {
				code: $("#codeInput").val()
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
	
	$('#agentModal').on('shown.bs.modal', function () {
		  $('.chosen-select', this).chosen('destroy').chosen();
	});
	
	$("#addButton, #updateButton").click(function(e) {
		if (validateForm()) {
			var jsonAgent = {
					"id": $("#idInput").val(),
					"code": $("#codeInput").val(),
					"description": $("#descriptionInput").val(),
					"active": $("#activeSelect option:selected").val(),
			};

			//	si existe el codigo y ademas no se trata de una actualizacion, lanzo modal.
			if (existsAgent() && (e.currentTarget.id === 'addButton')) {
				myExistentCodeError();
			} else {
				$.ajax({
					url: "saveAgent.do",
					type: "POST",
					contentType:"application/json",
					data: JSON.stringify(jsonAgent),
					async: true,
					success: function(response) {
						if (response.id === parseInt($("#idInput").val())) {
							myUpdateSuccessful();
						} else {
							myCreateSuccessful();
						}
						$('#agentModal').modal('hide');
						$("#agentsTable").bootgrid("reload");
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
	
	$("#agentAdministrationForm input, #agentAdministrationForm select").keypress(function(event) {
		return event.keyCode != 13;
	});

};