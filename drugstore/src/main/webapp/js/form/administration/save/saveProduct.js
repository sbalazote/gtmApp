SaveProduct = function() {
	
	// Atributos del objeto
	var gtins = null;
	var prices = null;

	var getGtins = function() {
		return gtins;
	};
	
	var setGtins = function(value) {
		gtins = value;
	};
	
	var getPrices = function() {
		return prices;
	};
	
	var setPrices = function(value) {
		prices = value;
	};
	
	var validateForm = function() {
		var form = $("#productAdministrationForm");
		form.validate({
			rules: {
				productCode: {
					required: true,
					digits: true,
					maxlength: 9,
				},
				gtin: {
					digits: true,
					exactLength: 13
				},
				productDescription: {
					required: true,
					maxlength: 100,
				},
				brand: {
					required: true,
				},
				monodrug: {
					required: true
				},
				productGroup: {
					required: true
				},
				drugCategory: {
					required: true
				},
				type: {
					required: true
				},
				productActive: {
					required: true
				},
				price: {
					required: true,
					number: true
				},
				informAnmat: {
					required: true
				},
				cold: {
					required: true
				}
			},
			showErrors: myShowErrors,
			onsubmit: false
		});
		return form.valid();
	};
	
	var existsProduct = function() {
		var exists = false;
		$.ajax({
			url: "existsProduct.do",
			type: "GET",
			async: false,
			data: {
				code: $("#productCodeInput").val()
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
	
	$('#productModal').on('shown.bs.modal', function () {
		  $('.chosen-select', this).chosen('destroy').chosen();
	});
	
	var confirm = function(e) {
		if (validateForm()) {
			for (var i = 0; i < prices.length; i++) {
				prices[i].price = prices[i].price.replace(",","");
			}
			var jsonProduct = {
					"id": $("#productIdInput").val(),
					"code": $("#productCodeInput").val(),
					"description": $("#productDescriptionInput").val(),
					"gtin": $("#gtinInput").val(),
					"brandId": $("#brandSelect").val(),
					"monodrugId": $("#monodrugSelect").val(),
					"groupId": $("#productGroupSelect").val(),
					"drugCategoryId": $("#drugCategorySelect").val(),
					"price": $("#priceInput").val().replace(",",""),
					"cold": $("#coldSelect option:selected").val(),
					"informAnmat": $("#informAnmatSelect option:selected").val(),
					"type": $("#typeSelect option:selected").val(),
					"active": $("#productActiveSelect option:selected").val(),
					"gtins": gtins,
					"prices": prices
			};

			//	si existe el codigo y ademas no se trata de una actualizacion, lanzo modal.
			if (existsProduct() && (e.currentTarget.id === 'addButton')) {
				myExistentCodeError();
			} else {
				$.ajax({
					url: "saveProduct.do",
					type: "POST",
					contentType:"application/json",
					data: JSON.stringify(jsonProduct),
					async: true,
					success: function(response) {
						if (response.id === parseInt($("#productIdInput").val())) {
							myUpdateSuccessful();
						} else {
							myCreateSuccessful();
						}
						$('#productModal').modal('hide');
						$("#productsTable").bootgrid("reload");
					},
					error: function(jqXHR, textStatus, errorThrown) {
						myGenericError();
					}
				});
			}
		}
	};
	
	$(".alert .close").on('click', function(e) {
	    $(this).parent().hide();
	});
	
	$("#productAdministrationForm input, #productAdministrationForm select").keypress(function(event) {
		return event.keyCode != 13;
	});
	
	return {
		getGtins: getGtins,
		setGtins: setGtins,
		getPrices: getPrices,
		setPrices: setPrices,
		confirm: confirm
	};
};