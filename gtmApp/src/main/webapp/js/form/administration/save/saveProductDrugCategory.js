SaveProductDrugCategory = function() {
	
	var validateForm = function() {
		var form = $("#productDrugCategoryAdministrationForm");
		form.validate({
			rules: {
				productDrugCategoryCode: {
					required: true,
					digits: true,
					maxlength: 9
				},
				productDrugCategoryDescription: {
					required: true,
					maxlength: 45
				},
				productDrugCategoryActive: {
					required: true
				}
			},
			showErrors: myShowErrors,
			onsubmit: false
		});
		return form.valid();
	};
	
	var existsProductDrugCategory = function() {
		var exists = false;
		$.ajax({
			url: "existsProductDrugCategory.do",
			type: "GET",
			async: false,
			data: {
				code: $("#productDrugCategoryCodeInput").val()
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
	
	$('#productDrugCategoryModal').on('shown.bs.modal', function () {
		  $('.chosen-select', this).chosen('destroy').chosen();
	});
	
	$("#addProductDrugCategoryButton, #updateProductDrugCategoryButton").click(function(e) {
		if (validateForm()) {
			var jsonProductDrugCategory = {
					"id": $("#productDrugCategoryIdInput").val(),
					"code": $("#productDrugCategoryCodeInput").val(),
					"description": $("#productDrugCategoryDescriptionInput").val(),
					"active": $("#productDrugCategoryActiveSelect option:selected").val()
			};

			//	si existe el codigo y ademas no se trata de una actualizacion, lanzo modal.
			if (existsProductDrugCategory() && (e.currentTarget.id === 'addProductDrugCategoryButton')) {
				myShowAlert('danger', 'C\u00f3digo existente. Por favor, ingrese uno diferente.', 'productDrugCategoryModalAlertDiv');
			} else {
				$.ajax({
					url: "saveProductDrugCategory.do",
					type: "POST",
					contentType:"application/json",
					data: JSON.stringify(jsonProductDrugCategory),
					async: true,
					success: function(response) {
						if (response.id === parseInt($("#productDrugCategoryIdInput").val())) {
							myUpdateSuccessful();
						} else {
							myCreateSuccessful();
						}
						$('#productDrugCategoryModal').modal('hide');
						$("#productDrugCategoriesTable").bootgrid("reload");
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
	
	$("#productDrugCategoryAdministrationForm input, #productDrugCategoryAdministrationForm select").keypress(function(event) {
		return event.keyCode != 13;
	});

};