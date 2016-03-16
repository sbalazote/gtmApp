SaveDeliveryLocation = function() {
	
	var validateForm = function() {
		var form = $("#deliveryLocationAdministrationForm");
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
					maxlength: 60
				},
				province: {
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
				VATLiability: {
					required: true
				},
				email: {
					email: true,
					maxlength: 45
				},
				gln: {
					required: true,
					exactLength: 13
				},
				active: {
					required: true
				},
				agent: {
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
	
	var existsDeliveryLocation = function() {
		var exists = false;
		$.ajax({
			url: "existsDeliveryLocation.do",
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
	
	$('#deliveryLocationModal').on('shown.bs.modal', function () {
		  $('.chosen-select', this).chosen('destroy').chosen();
	});
	
	$("#addButton, #updateButton").click(function(e) {
		if (validateForm()) {
			var jsonDeliveryLocation = {
					"id": $("#idInput").val(),
					"code": $("#codeInput").val(),
					"name": $("#nameInput").val(),
					"taxId": $("#taxIdInput").val(),
					"corporateName": $("#corporateNameInput").val(),
					"provinceId": $("#provinceSelect option:selected").val(),
					"locality": $("#localityInput").val(),
					"address": $("#addressInput").val(),
					"zipCode": $("#zipCodeInput").val(),
					"VATLiabilityId": $("#VATLiabilitySelect option:selected").val(),
					"phone": $("#phoneInput").val(),
					"mail": $("#emailInput").val(),
					"gln": $("#glnInput").val(),
					"active": $("#activeSelect option:selected").val(),
					"agentId": $("#agentSelect option:selected").val()
			};

			//	si existe el codigo y ademas no se trata de una actualizacion, lanzo modal.
			if (existsDeliveryLocation() && (e.currentTarget.id === 'addButton')) {
				myShowAlert('danger', 'C\u00f3digo existente. Por favor, ingrese uno diferente.', 'deliveryLocationModalAlertDiv');
			} else {
				$.ajax({
					url: "saveDeliveryLocation.do",
					type: "POST",
					contentType:"application/json",
					data: JSON.stringify(jsonDeliveryLocation),
					async: true,
					success: function(response) {
						if (response.id === parseInt($("#idInput").val())) {
							myUpdateSuccessful();
						} else {
							myCreateSuccessful();
						}
						$('#deliveryLocationModal').modal('hide');
						$("#deliveryLocationsTable").bootgrid("reload");
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
	
	$("#deliveryLocationAdministrationForm input, #deliveryLocationAdministrationForm select").keypress(function(event) {
		return event.keyCode != 13;
	});

};