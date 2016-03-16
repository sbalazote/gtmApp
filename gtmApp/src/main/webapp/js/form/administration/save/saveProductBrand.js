SaveProductBrand = function() {
	
	var validateForm = function() {
		var form = $("#productBrandAdministrationForm");
		form.validate({
			rules: {
				productBrandCode: {
					required: true,
					digits: true,
					maxlength: 9
				},
				productBrandDescription: {
					required: true,
					maxlength: 45
				},
				productBrandActive: {
					required: true
				}
			},
			showErrors: myShowErrors,
			onsubmit: false
		});
		return form.valid();
	};
	
	var existsProductBrand = function() {
		var exists = false;
		$.ajax({
			url: "existsProductBrand.do",
			type: "GET",
			async: false,
			data: {
				code: $("#productBrandCodeInput").val()
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
	
	$('#productBrandModal').on('shown.bs.modal', function () {
		  $('.chosen-select', this).chosen('destroy').chosen();
	});
	
	$("#addProductBrandButton, #updateProductBrandButton").click(function(e) {
		if (validateForm()) {
			var jsonProductBrand = {
					"id": $("#productBrandIdInput").val(),
					"code": $("#productBrandCodeInput").val(),
					"description": $("#productBrandDescriptionInput").val(),
					"active": $("#productBrandActiveSelect option:selected").val()
			};

			//	si existe el codigo y ademas no se trata de una actualizacion, lanzo modal.
			if (existsProductBrand() && (e.currentTarget.id === 'addProductBrandButton')) {
				myShowAlert('danger', 'C\u00f3digo existente. Por favor, ingrese uno diferente.', 'productBrandModalAlertDiv');
			} else {
				$.ajax({
					url: "saveProductBrand.do",
					type: "POST",
					contentType:"application/json",
					data: JSON.stringify(jsonProductBrand),
					async: true,
					success: function(response) {
						if (response.id === parseInt($("#productBrandIdInput").val())) {
							myUpdateSuccessful();
						} else {
							myCreateSuccessful();
						}
						$('#productBrandModal').modal('hide');
						$("#productBrandsTable").bootgrid("reload");
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
	
	$("#productBrandAdministrationForm input, #productBrandAdministrationForm select").keypress(function(event) {
		return event.keyCode != 13;
	});

};