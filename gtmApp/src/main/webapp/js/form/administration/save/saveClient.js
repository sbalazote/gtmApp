SaveClient = function() {
	
	var validateForm = function() {
		var form = $("#clientAdministrationForm");
		form.validate({
			rules: {
				code: {
					required: true,
					digits: true,
					maxlength: 9
				},
				name: {
					required: true,
					maxlength: 45
				},
				taxId: {
					required: true,
					digits: true,
					exactLength: 11
				},
				corporateName: {
					required: true,
					maxlength: 45
				},
				province: {
					required: true
				},
				VATLiability: {
					required: true
				},
				locality: {
					required: true,
					maxlength: 45
				},
				address: {
					required: true,
					maxlength: 45
				},
				zipCode: {
					required: true,
					maxlength: 10
				},
				deliveryLocations: {
					required: true
				},
				active: {
					required: true
				},
				phone: {
					maxlength: 45
				}
			},
			showErrors: myShowErrors,
			onsubmit: false
		});
		return form.valid();
	};
	
	var existsClient = function() {
		var exists = false;
		$.ajax({
			url: "existsClient.do",
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
	
	$('#clientModal').on('shown.bs.modal', function () {
		  $('.chosen-select', this).chosen('destroy').chosen();
	});
	
	$('#my-select').multiSelect();

	$("#addButton, #updateButton").click(function(e) {
		if (validateForm()) {
			var jsonClient = {
					"id": $("#idInput").val(),
					"code": $("#codeInput").val(),
					"name": $("#nameInput").val(),
					"taxId": $("#taxIdInput").val(),
					"corporateName": $("#corporateNameInput").val(),
					"provinceId": $("#provinceSelect option:selected").val(),
					"VATLiabilityId": $("#VATLiabilitySelect option:selected").val(),
					"locality": $("#localityInput").val(),
					"address": $("#addressInput").val(),
					"zipCode": $("#zipCodeInput").val(),
					"phone": $("#phoneInput").val(),
					"deliveryLocations": $("#my-select").val() || [],
					"active": $("#activeSelect option:selected").val(),
					"medicalInsuranceCode": $("#medicalInsuranceCodeInput").val()
			};

			//	si existe el codigo y ademas no se trata de una actualizacion, lanzo modal.
			if (existsClient() && (e.currentTarget.id === 'addButton')) {
				myShowAlert('danger', 'C\u00f3digo existente. Por favor, ingrese uno diferente.', 'clientModalAlertDiv');
			} else {
				$.ajax({
					url: "saveClient.do",
					type: "POST",
					contentType:"application/json",
					data: JSON.stringify(jsonClient),
					async: true,
					success: function(response) {
						if (response.id === parseInt($("#idInput").val())) {
							myUpdateSuccessful();
						} else {
							myCreateSuccessful();
						}
						$('#clientModal').modal('hide');
						$("#clientsTable").bootgrid("reload");
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
};