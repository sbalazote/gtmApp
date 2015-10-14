SaveAffiliate = function() {
	
	var validateForm = function() {
		var form = $("#affiliateAdministrationForm");
		form.validate({
			rules: {
				code: {
					required: true,
					digits: true,
					maxlength: 45
				},
				name: {
					required: true,
					maxlength: 45
				},
				surname: {
					required: true,
					maxlength: 45
				},
				document: {
					digits: true,
					maxlength: 15
				},
				client: {
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
	
	var existsAffiliate = function() {
		var exists = false;
		$.ajax({
			url: "existsAffiliate.do",
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

	$('#my-select').multiSelect();

	$('#affiliateModal').on('shown.bs.modal', function () {
		  $('.chosen-select', this).chosen('destroy').chosen();
	});
	
	$("#addButton, #updateButton").click(function(e) {
		if (validateForm()) {
			var jsonAffiliate = {
					"id": $("#idInput").val(),
					"code": $("#codeInput").val(),
					"name": $("#nameInput").val(),
					"surname": $("#surnameInput").val(),
					"documentType": $("#documentTypeSelect option:selected").val() || null ,
					"document": $("#documentInput").val() || null,
					"clientId": $("#clientSelect option:selected").val(),
					"active": $("#activeSelect option:selected").val(),
					"clients": $("#my-select").val() || new Array()
			};

			//	si existe el codigo y ademas no se trata de una actualizacion, lanzo modal.
			if (existsAffiliate() && (e.currentTarget.id === 'addButton')) {
				myExistentCodeError();
			} else {
				$.ajax({
					url: "saveAffiliate.do",
					type: "POST",
					contentType:"application/json",
					data: JSON.stringify(jsonAffiliate),
					async: true,
					success: function(response) {
						if (response.id === parseInt($("#idInput").val())) {
							myUpdateSuccessful();
						} else {
							myCreateSuccessful();
						}
						$('#affiliateModal').modal('hide');
						$("#affiliatesTable").bootgrid("reload");
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
	
	$("#affiliateAdministrationForm input, #affiliateAdministrationForm select").keypress(function(event) {
		return event.keyCode != 13;
	});
};