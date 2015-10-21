SaveProvider = function() {
	
	var validateForm = function() {
		var form = $("#providerAdministrationForm");
		form.validate({
			rules: {
				providerTypeCode: {
					required: true,
					digits: true,
					maxlength: 9,
				},
				name: {
					required: true,
					maxlength: 45,
				},
				taxId: {
					required: true,
					digits: true,
					exactLength: 11
				},
				corporateName: {
					required: true,
					maxlength: 45,
				},
				province: {
					required: true
				},
				locality: {
					required: true,
					maxlength: 45,
				},
				address: {
					required: true,
					maxlength: 45,
				},
				email: {
					email: true,
					maxlength: 45,
				},
				zipCode: {
					required: true,
					maxlength: 10,
				},
				gln: {
					required: true,
					exactLength: 13
				},
				agent: {
					required: true
				},
				type: {
					required: true
				},
				VATLiability: {
					required: true
				},
				providerTypeActive: {
					required: true
				},
				phone: {
					maxlength: 45,
				},
			},
			showErrors: myShowErrors,
			onsubmit: false
		});
		return form.valid();
	};
	
	var existsProvider = function() {
		var exists = false;
		$.ajax({
			url: "existsProvider.do",
			type: "GET",
			async: false,
			data: {
				code: $("#providerCodeInput").val()
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
	
	$('#providerModal').on('shown.bs.modal', function () {
		  $('.chosen-select', this).chosen('destroy').chosen();
	});

	$('#my-select').multiSelect();
	
	$("#addProviderButton, #updateProviderButton").click(function(e) {
		if (validateForm()) {
			var jsonProvider = {
					"id": $("#providerIdInput").val(),
					"code": $("#providerCodeInput").val(),
					"name": $("#nameInput").val(),
					"taxId": $("#taxIdInput").val(),
					"corporateName": $("#corporateNameInput").val(),
					"provinceId": $("#provinceSelect option:selected").val(),
					"locality": $("#localityInput").val(),
					"address": $("#addressInput").val(),
					"zipCode": $("#zipCodeInput").val(),
					"phone": $("#phoneInput").val(),
					"mail": $("#emailInput").val(),
					"gln": $("#glnInput").val(),
					"agentId": $("#agentSelect option:selected").val(),
					"typeId": $("#typeSelect option:selected").val(),
					"VATLiabilityId": $("#VATLiabilitySelect option:selected").val(),
					"active": $("#providerActiveSelect option:selected").val(),
					"logisticsOperators": $("#my-select").val() || new Array(),
			};

			//	si existe el codigo y ademas no se trata de una actualizacion, lanzo modal.
			if (existsProvider() && (e.currentTarget.id === 'addButton')) {
				myExistentCodeError();
			} else {
				$.ajax({
					url: "saveProvider.do",
					type: "POST",
					contentType:"application/json",
					data: JSON.stringify(jsonProvider),
					async: true,
					success: function(response) {
						if (response.id === parseInt($("#providerIdInput").val())) {
							myUpdateSuccessful();
						} else {
							myCreateSuccessful();
						}
						$('#providerModal').modal('hide');
						$("#providersTable").bootgrid("reload");
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
	
	$("#providerAdministrationForm input, #providerAdministrationForm select").keypress(function(event) {
		return event.keyCode != 13;
	});

};