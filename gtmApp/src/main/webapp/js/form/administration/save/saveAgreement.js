SaveAgreement = function() {
	
	var validateForm = function() {
		var form = $("#agreementAdministrationForm");
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
				},
				pickingList: {
					required: true
				},
				numberOfDeliveryNoteDetailsPerPage: {
					required: true,
					digits: true
				},
				orderLabelPrinter: {
					required: true
				},
				deliveryNotePrinter: {
					required: true
				},
				deliveryNoteConcept: {
					required: true
				},
				destructionConcept: {
					required: true
				}
			},
			showErrors: myShowErrors,
			onsubmit: false
		});
		return form.valid();
	};
	
	var existsAgreement = function() {
		var exists = false;
		$.ajax({
			url: "existsAgreement.do",
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
	
	$('#agreementModal').on('shown.bs.modal', function () {
		  $('.chosen-select', this).chosen('destroy').chosen();
	});
	
	$("#addButton, #updateButton").click(function(e) {
		if (validateForm()) {
			var jsonAgreement = {
				"id": $("#idInput").val(),
				"code": $("#codeInput").val(),
				"description": $("#descriptionInput").val(),
				"active": $("#activeSelect option:selected").val(),
				"pickingList": $("#pickingListSelect option:selected").val(),
				"numberOfDeliveryNoteDetailsPerPage": $("#numberOfDeliveryNoteDetailsPerPageInput").val(),
				"orderLabelPrinter": $("#orderLabelPrinterSelect option:selected").val(),
				"deliveryNotePrinter": $("#deliveryNotePrinterSelect option:selected").val(),
				"deliveryNoteConceptId": $("#deliveryNoteConceptSelect option:selected").val(),
				"destructionConceptId": $("#destructionConceptSelect option:selected").val()
			};

			//	si existe el codigo y ademas no se trata de una actualizacion, lanzo modal.
			if (existsAgreement() && (e.currentTarget.id === 'addButton')) {
				myExistentCodeError();
			} else {
				$.ajax({
					url: "saveAgreement.do",
					type: "POST",
					contentType:"application/json",
					data: JSON.stringify(jsonAgreement),
					async: true,
					success: function(response) {
						if (response.id === parseInt($("#idInput").val())) {
							myUpdateSuccessful();
						} else {
							myCreateSuccessful();
						}
						$('#agreementModal').modal('hide');
						$("#agreementsTable").bootgrid("reload");
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
	
	$("#agreementAdministrationForm input, #agreementAdministrationForm select").keypress(function(event) {
		return event.keyCode != 13;
	});

};