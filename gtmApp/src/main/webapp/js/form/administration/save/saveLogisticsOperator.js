SaveLogisticsOperator = function() {
	
	var validateForm = function() {
		var form = $("#logisticsOperatorAdministrationForm");
		form.validate({
			rules: {
				code: {
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
				gln: {
					required: true,
					exactLength: 13
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
				zipCode: {
					required: true,
					maxlength: 10,
				},
				phone: {
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
	
	var existsLogisticsOperator = function() {
		var exists = false;
		$.ajax({
			url: "existsLogisticsOperator.do",
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
	
	$('#logisticsOperatorModal').on('shown.bs.modal', function () {
		  $('.chosen-select', this).chosen('destroy').chosen();
	});
	
	$("#addButton, #updateButton").click(function(e) {
		if (validateForm()) {
			var jsonLogisticsOperator = {
					"id": $("#idInput").val(),
					"code": $("#codeInput").val(),
					"name": $("#nameInput").val(),
					"taxId": $("#taxIdInput").val(),
					"corporateName": $("#corporateNameInput").val(),
				    "gln": $("#glnInput").val(),
					"provinceId": $("#provinceSelect option:selected").val(),
					"locality": $("#localityInput").val(),
					"address": $("#addressInput").val(),
					"zipCode": $("#zipCodeInput").val(),
					"phone": $("#phoneInput").val(),
					"active": $("#activeSelect option:selected").val(),
			};

			//	si existe el codigo y ademas no se trata de una actualizacion, lanzo modal.
			if (existsLogisticsOperator() && (e.currentTarget.id === 'addButton')) {
				myExistentCodeError();
			} else {
				$.ajax({
					url: "saveLogisticsOperator.do",
					type: "POST",
					contentType:"application/json",
					data: JSON.stringify(jsonLogisticsOperator),
					async: true,
					success: function(response) {
						if (response.id === parseInt($("#idInput").val())) {
							myUpdateSuccessful();
						} else {
							myCreateSuccessful();
						}
						$('#logisticsOperatorModal').modal('hide');
						$("#logisticsOperatorsTable").bootgrid("reload");
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

	$("#logisticsOperatorAdministrationForm input, #logisticsOperatorAdministrationForm select").keypress(function(event) {
		return event.keyCode != 13;
	});

};