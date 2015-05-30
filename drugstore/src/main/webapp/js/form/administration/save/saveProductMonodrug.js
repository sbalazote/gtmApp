SaveProductMonodrug = function() {
	
	var validateForm = function() {
		var form = $("#productMonodrugAdministrationForm");
		form.validate({
			rules: {
				productMonodrugCode: {
					required: true,
					digits: true,
					maxlength: 9,
				},
				productMonodrugDescription: {
					required: true,
					maxlength: 45,
				},
				productMonodrugActive: {
					required: true
				}
			},
			showErrors: myShowErrors,
			onsubmit: false
		});
		return form.valid();
	};
	
	var existsProductMonodrug = function() {
		var exists = false;
		$.ajax({
			url: "existsProductMonodrug.do",
			type: "GET",
			async: false,
			data: {
				code: $("#productMonodrugCodeInput").val()
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
	
	$('#productMonodrugModal').on('shown.bs.modal', function () {
		  $('.chosen-select', this).chosen('destroy').chosen();
	});
	
	$("#addProductMonodrugButton, #updateProductMonodrugButton").click(function(e) {
		if (validateForm()) {
			var jsonProductMonodrug = {
					"id": $("#productMonodrugIdInput").val(),
					"code": $("#productMonodrugCodeInput").val(),
					"description": $("#productMonodrugDescriptionInput").val(),
					"active": $("#productMonodrugActiveSelect option:selected").val(),
			};

			//	si existe el codigo y ademas no se trata de una actualizacion, lanzo modal.
			if (existsProductMonodrug() && (e.currentTarget.id === 'addButton')) {
				myExistentCodeError();
			} else {
				$.ajax({
					url: "saveProductMonodrug.do",
					type: "POST",
					contentType:"application/json",
					data: JSON.stringify(jsonProductMonodrug),
					async: true,
					success: function(response) {
						if (response.id === parseInt($("#productMonodrugIdInput").val())) {
							myUpdateSuccessful();
						} else {
							myCreateSuccessful();
						}
						$('#productMonodrugModal').modal('hide');
						$("#productMonodrugsTable").bootgrid("reload");
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
	
	$("#productMonodrugAdministrationForm input, #productMonodrugAdministrationForm select").keypress(function(event) {
		return event.keyCode != 13;
	});

};