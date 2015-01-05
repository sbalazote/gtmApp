SaveProductGroup = function() {
	
	var validateForm = function() {
		var form = $("#productGroupAdministrationForm");
		form.validate({
			rules: {
				productGroupCode: {
					required: true,
					digits: true,
					maxlength: 9,
				},
				productGroupDescription: {
					required: true,
					maxlength: 45,
				},
				productGroupActive: {
					required: true
				}
			},
			showErrors: myShowErrors,
			onsubmit: false
		});
		return form.valid();
	};
	
	var existsProductGroup = function() {
		var exists = false;
		$.ajax({
			url: "existsProductGroup.do",
			type: "GET",
			async: false,
			data: {
				code: $("#productGroupCodeInput").val()
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
	
	$('#productGroupModal').on('shown.bs.modal', function () {
		  $('.chosen-select', this).chosen('destroy').chosen();
	});
	
	$("#addProductGroupButton, #updateProductGroupButton").click(function(e) {
		if (validateForm()) {
			var jsonProductGroup = {
					"id": $("#productGroupIdInput").val(),
					"code": $("#productGroupCodeInput").val(),
					"description": $("#productGroupDescriptionInput").val(),
					"active": $("#productGroupActiveSelect option:selected").val(),
			};

			//	si existe el codigo y ademas no se trata de una actualizacion, lanzo modal.
			if (existsProductGroup() && (e.currentTarget.id === 'addButton')) {
				myExistentCodeError();
			} else {
				$.ajax({
					url: "saveProductGroup.do",
					type: "POST",
					contentType:"application/json",
					data: JSON.stringify(jsonProductGroup),
					async: true,
					success: function(response) {
						if (response.id === parseInt($("#productGroupIdInput").val())) {
							myUpdateSuccessful();
						} else {
							myCreateSuccessful();
						}
						$('#productGroupModal').modal('hide');
						$("#productGroupsTable").bootgrid("reload");
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
	
	$("#productGroupAdministrationForm input, #productGroupAdministrationForm select").keypress(function(event) {
		return event.keyCode != 13;
	});

};