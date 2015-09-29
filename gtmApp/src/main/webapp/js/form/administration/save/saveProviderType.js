SaveProviderType = function() {
	
	var validateForm = function() {
		var form = $("#providerTypeAdministrationForm");
		form.validate({
			rules: {
				providerTypeCode: {
					required: true,
					digits: true,
					maxlength: 9,
				},
				description: {
					required: true,
					maxlength: 45,
				},
				providerTypeActive: {
					required: true
				}
			},
			showErrors: myShowErrors,
			onsubmit: false
		});
		return form.valid();
	};
	
	var existsProviderType = function() {
		var exists = false;
		$.ajax({
			url: "existsProviderType.do",
			type: "GET",
			async: false,
			data: {
				code: $("#providerTypeCodeInput").val()
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
	
	$('#providerTypeModal').on('shown.bs.modal', function () {
		  $('.chosen-select', this).chosen('destroy').chosen();
	});
	
	$("#addProviderTypeButton, #updateProviderTypeButton").click(function(e) {
		if (validateForm()) {
			var jsonProviderType = {
					"id": $("#providerTypeIdInput").val(),
					"code": $("#providerTypeCodeInput").val(),
					"description": $("#descriptionInput").val(),
					"active": $("#providerTypeActiveSelect option:selected").val(),
			};

			//	si existe el codigo y ademas no se trata de una actualizacion, lanzo modal.
			if (existsProviderType() && (e.currentTarget.id === 'addButton')) {
				myExistentCodeError();
			} else {
				$.ajax({
					url: "saveProviderType.do",
					type: "POST",
					contentType:"application/json",
					data: JSON.stringify(jsonProviderType),
					async: true,
					success: function(response) {
						if (response.id === parseInt($("#providerTypeIdInput").val())) {
							myUpdateSuccessful();
						} else {
							myCreateSuccessful();
						}
						$('#providerTypeModal').modal('hide');
						$("#providerTypesTable").bootgrid("reload");
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
	
	$("#providerTypeAdministrationForm input, #providerTypeAdministrationForm select").keypress(function(event) {
		return event.keyCode != 13;
	});

};