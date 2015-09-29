SaveEvent = function() {
	
	var validateForm = function() {
		var form = $("#eventAdministrationForm");
		form.validate({
			rules: {
				code: {
					required: true,
					digits: true,
					maxlength: 9,
				},
				description: {
					required: true,
					maxlength: 100,
				},
				originAgentId: {
					required: true
				},
				destinationAgentId: {
					required: true
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
	
	var existsEvent = function() {
		var exists = false;
		$.ajax({
			url: "existsEvent.do",
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
	
	$('#eventModal').on('shown.bs.modal', function () {
		  $('.chosen-select', this).chosen('destroy').chosen();
	});
	
	$("#addButton, #updateButton").click(function(e) {
		if (validateForm()) {
			var jsonEvent = {
					"id": $("#idInput").val(),
					"code": $("#codeInput").val(),
					"description": $("#descriptionInput").val(),
					"originAgentId": $("#originAgentIdSelect").val(),
					"destinationAgentId": $("#destinationAgentIdSelect").val(),
					"active": $("#activeSelect option:selected").val(),
			};

			//	si existe el codigo y ademas no se trata de una actualizacion, lanzo modal.
			if (existsEvent() && (e.currentTarget.id === 'addButton')) {
				myExistentCodeError();
			} else {
				$.ajax({
					url: "saveEvent.do",
					type: "POST",
					contentType:"application/json",
					data: JSON.stringify(jsonEvent),
					async: true,
					success: function(response) {
						if (response.id === parseInt($("#idInput").val())) {
							myUpdateSuccessful();
						} else {
							myCreateSuccessful();
						}
						$('#eventModal').modal('hide');
						$("#eventsTable").bootgrid("reload");
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
	
	$("#eventAdministrationForm input, #eventAdministrationForm select").keypress(function(event) {
		return event.keyCode != 13;
	});

};