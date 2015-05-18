SaveConcept = function() {
	
	var validateForm = function() {
		var form = $("#conceptAdministrationForm");
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
				deliveryNotePOS: {
					required: true,
					digits: true,
					maxlength: 4,
					minlength: 4,
				},
				input: {
					required: true
				},
				printDeliveryNote: {
					required: true
				},
				refund: {
					required: true
				},
				informAnmat: {
					required: true
				},
				deliveryNotesCopies: {
					required: true,
					digits: true,
					maxlength: 9,
				},
				active: {
					required: true
				},
				events: {
					required: true
				},
				client:{
					required: true
				}
			},
			showErrors: myShowErrors,
			onsubmit: false
		});
		return form.valid();
	};
	
	var existsConcept = function() {
		var exists = false;
		$.ajax({
			url: "existsConcept.do",
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

	$('#conceptModal').on('shown.bs.modal', function () {
		  $('.chosen-select', this).chosen('destroy').chosen();
	});
	
	$('#my-select').multiSelect();

	$("#addButton, #updateButton").click(function(e) {
		if (validateForm()) {
			var jsonConcept = {
					"id": $("#idInput").val(),
					"code": $("#codeInput").val(),
					"description": $("#descriptionInput").val(),
					"deliveryNotePOS": $("#deliveryNotePOSInput").val(),
					"input": $("#inputSelect").val(),
					"printDeliveryNote": $("#printDeliveryNoteSelect").val(),
					"refund": $("#refundSelect").val(),
					"informAnmat": $("#informAnmatSelect").val(),
					"deliveryNotesCopies": $("#deliveryNotesCopiesInput").val(),
					"active": $("#activeSelect option:selected").val(),
					"client": $("#clientSelect option:selected").val(),
					"events":  $("#my-select").val() || new Array(),
			};

			//	si existe el codigo y ademas no se trata de una actualizacion, lanzo modal.
			if (existsConcept() && (e.currentTarget.id === 'addButton')) {
				myExistentCodeError();
			} else {
				$.ajax({
					url: "saveConcept.do",
					type: "POST",
					contentType:"application/json",
					data: JSON.stringify(jsonConcept),
					async: true,
					success: function(response) {
						if (response.id === parseInt($("#idInput").val())) {
							myUpdateSuccessful();
						} else {
							myCreateSuccessful();
						}
						$('#conceptModal').modal('hide');
						$("#conceptsTable").bootgrid("reload");
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