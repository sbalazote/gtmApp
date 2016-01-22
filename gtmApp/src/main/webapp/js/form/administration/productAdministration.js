ProductAdministration = function() {

	var saveProduct = new SaveProduct();
	
	var entity = "";
	var productGtins;
	var productPrices;

	var searchHTML;
	var searchPhrase;
	var exportHTML;
	
	$("body").tooltip({ selector: '[data-toggle="tooltip"]' });
	
	var deleteGtin = function(gtin) {
		var idx = 0;
		for (var i = 0; i < productGtins.length; i++) {
			if (productGtins[i].number === gtin) {
				idx = i;
			}
		}
		return productGtins.splice(idx, 1);
	};
	
	var deletePrice = function(id) {
		var idx = 0;
		for (var i = 0; i < productPrices.length; i++) {
			if (productPrices[i].id == id) {
				idx = i;
			}
		}
		return productPrices.splice(idx, 1);
	};
	
	var updateGtin = function(gtin, date) {
		for (var i = 0; i < productGtins.length; i++) {
			if (productGtins[i].number === gtin) {
				productGtins[i].date = date;
			}
		}

	};
	
	var updatePrice = function(id, price, date) {
		for (var i = 0; i < productPrices.length; i++) {
			if (productPrices[i].id == id) {
				productPrices[i].price = price;
				productPrices[i].date = date;
			}
		}

	};
	
	$('#productGtinsModal').on('shown.bs.modal', function () {
		$("#currentGtinInput").val($("#gtinInput").val());
		$("#addGtinInput").focus();
		var aaData = [];
		for (var i = 0, l = productGtins.length; i < l; ++i) {
			var gtin = {
				number: "",
				date: "",
				commands: ""
			};
			gtin.number= productGtins[i].number;
			gtin.date = myParseDateTime(productGtins[i].date);
			gtin.commands = "<button type=\"button\" class=\"btn btn-sm btn-default command-delete\" data-row-id=\"" + gtin.id + "\"><span class=\"glyphicon glyphicon-trash\"></span></button> " +
					"<button type=\"button\" class=\"btn btn-sm btn-default command-saved\" data-toggle=\"tooltip\" data-placement=\"right\" title=\"Marcar como GTIN Actual\" data-row-id=\"" + gtin.id + "\"><span class=\"glyphicon glyphicon-saved\"></span></button>";
			aaData.push(gtin);
		}
		$("#productGtinsTable").bootgrid("clear");
		$("#productGtinsTable").bootgrid("append", aaData);
	});
	
	$('#productPricesModal').on('shown.bs.modal', function () {
		$("#currentPriceInput").val($("#priceInput").val());
		$("#addPriceInput").focus();
		var aaData = [];
		for (var i = 0, l = productPrices.length; i < l; ++i) {
			var price = {
				id: "",
				price: "",
				date: "",
				commands: ""
			};
			price.id= productPrices[i].id;
			price.price= productPrices[i].price.slice(0,-2) + "," + productPrices[i].price.slice(-2);
			price.date = myParseDateTime(productPrices[i].date);
			price.commands = "<button type=\"button\" class=\"btn btn-sm btn-default command-delete\" data-row-id=\"" + price.id + "\"><span class=\"glyphicon glyphicon-trash\"></span></button> " +
					"<button type=\"button\" class=\"btn btn-sm btn-default command-saved\" data-toggle=\"tooltip\" data-placement=\"right\" title=\"Marcar como Precio Actual\" data-row-id=\"" + price.id + "\"><span class=\"glyphicon glyphicon-saved\"></span></button>";
			aaData.push(price);
		}
		$("#productPricesTable").bootgrid("clear");
		$("#productPricesTable").bootgrid("append", aaData);
	});
	
	$("#updateProductGtinsButton").click(function() {
		$('#productGtinsModal').modal('hide')
	});
	
	$("#updateProductPricesButton").click(function() {
		$('#productPricesModal').modal('hide')
	});
	
	$("#addProductButton, #updateProductButton").click(function(e) {
		saveProduct.setGtins(productGtins);
		saveProduct.setPrices(productPrices);
		saveProduct.confirm(e);
	});
	

	$('#productGtinsTableBody').on("click", ".command-delete", function() {
		var parent = $(this).parent().parent();
		var gtinNumber = parent.find("td:first").html();
		// Si borro el gtin actual lanzo una alerta y verifico si esta en uso.
		var message;
		if ($("#currentGtinInput").val() === gtinNumber) {
			message = 'Se va a eliminar el GTIN actual <strong>' + gtinNumber + '</strong>. Desea continuar?.';
		} else {
			message = 'Se va a eliminar el GTIN <strong>' + gtinNumber + '</strong>. Desea continuar?.';
		}
		BootstrapDialog.show({
			type: BootstrapDialog.TYPE_WARNING,
			message: message,
			title: 'Advertencia!',
			closable: false,
			buttons: [{
				id: "isGtinUsedCheckButton",
				icon: 'glyphicon glyphicon-send',
				label: ' Si',
				cssClass: 'btn-primary',
				autospin: true,
				action: function(dialogRef){
					var $button = this; // 'this' here is a jQuery object that wrapping the <button> DOM element.
					dialogRef.enableButtons(false);
					dialogRef.setClosable(false);
					dialogRef.getModalBody().html('Verificando si se encuentra en uso...');
					$.ajax({
						url: "isGtinUsed.do",
						type: "POST",
						data: {
							gtinNumber: gtinNumber
						},
						async: true,
						success: function(response) {
							if (response === true) {
								dialogRef.getModalBody().html('<strong>No se puede eliminar el GTIN ya que se encuentra en uso.</strong>');
							} else {
								var rows = Array();
								rows[0] = gtinNumber;
								$("#productGtinsTable").bootgrid("remove", rows);
								if ($("#currentGtinInput").val() === gtinNumber) {
									$("#currentGtinInput").val('');
									$("#gtinInput").val('');
								}
								deleteGtin(gtinNumber);
								dialogRef.getModalBody().html('<strong>Se elimino el GTIN.</strong>');
							}
						},
						error: function(jqXHR, textStatus, errorThrown) {
							dialogRef.getModalBody().html('<strong>Error en la busqueda de GTINs. Reintente mas tarde.</strong>');
						},
						complete: function(jqXHR, textStatus) {
							$button.stopSpin();
							setTimeout(function() {
								dialogRef.close();
							}, 2000);
						}
					});
				}
			}, {
				label: 'No',
				action: function(dialogRef){
					dialogRef.close();
				}
			}]
		});
	});
	
	$('#productPricesTableBody').on("click", ".command-delete", function() {
		var parent = $(this).parent().parent();
		var id = parent.find("td:first").html();
		var price = parent.find("td:nth-child(2)").html();
		// Si borro el precio actual lanzo una alerta
		if ($("#currentPriceInput").val() === price) {
			BootstrapDialog.show({
				type: BootstrapDialog.TYPE_WARNING,
		        message: 'Se va a eliminar el precio actual. Desea continuar?.',
		        title: 'Advertencia!',
		        closable: false,
		        buttons: [{
	                label: 'No',
	                action: function(dialogItself) {
	                    dialogItself.close();
	                }
	            }, {
	                label: 'Si',
	                cssClass: 'btn-primary',
	                action: function(dialogItself) {
	                	var rows = Array();
	                    rows[0] = parseInt(id);
	                    $("#productPricesTable").bootgrid("remove", rows);
	                    $("#currentPriceInput").val('');
	                    $("#priceInput").val('');
	                    deletePrice(id);
	                    dialogItself.close();
	                }
	            }]
			});
		} else {
			var rows = Array();
			rows[0] = parseInt(id);
            $("#productPricesTable").bootgrid("remove", rows);
			deletePrice(id);
		}
	});
	
	$('#productGtinsTableBody').on("click", ".command-saved", function() {
		var parent = $(this).parent().parent();
		var gtinNumber = parent.find("td:first").html();
		var modifiedDate = parent.find("td:nth-child(2)");
		modifiedDate.html(myParseDateTime(new Date()));
		$('#currentGtinInput').val(gtinNumber);
		$("#gtinInput").val(gtinNumber);
		updateGtin(gtinNumber, new Date());
	});
	
	$('#productPricesTableBody').on("click", ".command-saved", function() {
		var parent = $(this).parent().parent();
		var id = parent.find("td:first").html();
		var price = parent.find("td:nth-child(2)").html();
		var modifiedDate = parent.find("td:nth-child(3)");
		modifiedDate.html(myParseDateTime(new Date()));
		$('#currentPriceInput').val(price);
		$("#priceInput").val(price);
		updatePrice(id, price.replace(",",""), new Date());
	});
	
	$("#addGtinButton").click(function() {
		var validateForm = function() {
			var form = $("#productGtinsForm");
			form.validate({
				rules: {
					addGtin: {
						digits: true,
						exactLength: 13
					}
				},
				showErrors: myShowErrors,
				onsubmit: false
			});
			return form.valid();
		};
		
		if (validateForm()) {
			var aaData = [];
			var gtin = {
					number: $("#addGtinInput").val(),
					date: myParseDateTime(new Date()),
					commands: "<button type=\"button\" class=\"btn btn-sm btn-default command-delete\" data-row-id=\"0\"><span class=\"glyphicon glyphicon-trash\"></span></button> " +
					"<button type=\"button\" class=\"btn btn-sm btn-default command-saved\" data-toggle=\"tooltip\" data-placement=\"right\" title=\"Marcar como GTIN Actual\" data-row-id=\"0\"><span class=\"glyphicon glyphicon-saved\"></span></button>"
			};
			aaData.push(gtin);
			$("#productGtinsTable").bootgrid("append", aaData);
			var gt = {
					id: "",
					number: $("#addGtinInput").val(),
					date: new Date()
			};
			productGtins.push(gt);
			$("#addGtinInput").val("");
			$("#addGtinInput").focus();
		}
	});
	
	$("#addPriceButton").click(function() {
		var validateForm = function() {
			var form = $("#productPricesForm");
			form.validate({
				rules: {
					addPrice: {
						priceWithCents: true,
						required: true
					}
				},
				showErrors: myShowErrors,
				onsubmit: false
			});
			return form.valid();
		};
		
		if (validateForm()) {
			var aaData = [];
			var price = {
					id: "-",
					price: $("#addPriceInput").val(),
					date: myParseDateTime(new Date()),
					commands: "<button type=\"button\" class=\"btn btn-sm btn-default command-delete\" data-row-id=\"0\"><span class=\"glyphicon glyphicon-trash\"></span></button> " +
					"<button type=\"button\" class=\"btn btn-sm btn-default command-saved\" data-toggle=\"tooltip\" data-placement=\"right\" title=\"Marcar como Precio Actual\" data-row-id=\"0\"><span class=\"glyphicon glyphicon-saved\"></span></button>"
			};
			aaData.push(price);
			$("#productPricesTable").bootgrid("append", aaData);
			var gt = {
					id: "",
					price: $("#addPriceInput").val(),
					date: new Date()
			};
			productPrices.push(gt);
			$("#addPriceInput").val("");
			$("#addPriceInput").focus();
		}
	});
	
	//Modulo Productos	
	
	var productId;
	
	var resetProductForm = function() {
		$("#productIdInput").val('');
		$("#productCodeInput").val('');
		$("#gtinInput").val('');
		$("#productDescriptionInput").val('');
		$('#brandSelect').val('').trigger('chosen:updated');
		$('#monodrugSelect').val('').trigger('chosen:updated');
		$('#productGroupSelect').val('').trigger('chosen:updated');
		$('#drugCategorySelect').val('').trigger('chosen:updated');
		$("#typeSelect").val($("#typeSelect option:first").val());
		$("#productActiveSelect").val($("#productActiveSelect option:first").val());
		$('#priceInput').val('');
		$("#informAnmatSelect").val($("#informAnmatSelect option:first").val());
		$("#coldSelect").val($("#coldSelect option:first").val());
		productPrices = [];
		productGtins = [];
	};
	
	var deleteProduct = function(productId) {
		$.ajax({
			url: "deleteProduct.do",
			type: "POST",
			data: {
				productId: productId
			},
			async: true,
			success: function(response) {
				if (response === true) {
					myReload("success", "Se ha registrado la baja con identificador:  " + productId);
				} else {
					myReload("danger", "El registro no existe o ya ha sido dado de baja.");
				}
				$("#productsTable").bootgrid("reload");
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myDeleteError();
			}
		});
	};
	
	var readProduct = function(productId) {
		$.ajax({
			url: "readProduct.do",
			type: "GET",
			data: {
				productId: productId
			},
			async: false,
			success: function(response) {
				$("#productIdInput").val(response.id);
				$("#productCodeInput").val(response.code);
				if (response.gtin != null) {
					$("#gtinInput").val(response.gtin);
				} else {
					$("#gtinInput").val('');
				}
				productGtins = response.gtins;
				$("#productDescriptionInput").val(response.description);
				$('#brandSelect').val(response.brandId).trigger('chosen:updated');
				$('#monodrugSelect').val(response.monodrugId).trigger('chosen:updated');
				$('#productGroupSelect').val(response.groupId).trigger('chosen:updated');
				$('#drugCategorySelect').val(response.drugCategoryId).trigger('chosen:updated');
				$("#typeSelect").val(response.type);
				var isActive = (response.active) ? "true" : "false";
				$("#productActiveSelect").val(isActive).trigger('chosen:update');
				if (response.price != null) {
					var price = String(response.price);
					$('#priceInput').val(price.slice(0,-2) + "," + price.slice(-2));
				} else {
					$("#priceInput").val('');
				}
				productPrices = response.prices;
				for (var i = 0; i < productPrices.length; i++) {
					productPrices[i].price = productPrices[i].price.toString();
				}
				var isInformAnmat = (response.informAnmat) ? "true" : "false";
				$("#informAnmatSelect").val(isInformAnmat).trigger('chosen:update');
				var isCold = (response.cold) ? "true" : "false";
				$("#coldSelect").val(isCold).trigger('chosen:update');
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myDeleteError();
			}
		});
	};
	
	var toggleProductElements = function(hidden) {
		$("#productCodeInput").attr('disabled', hidden);
		//$("#gtinInput").attr('disabled', hidden);
		$("#editGtinsButton").attr('disabled', hidden);
		$("#productDescriptionInput").attr('disabled', hidden);
		$('#brandSelect').prop('disabled', hidden).trigger('chosen:update');
		$('#monodrugSelect').prop('disabled', hidden).trigger('chosen:update');
		$('#productGroupSelect').prop('disabled', hidden).trigger('chosen:update');
		$('#drugCategorySelect').prop('disabled', hidden).trigger('chosen:update');
		$("#typeSelect").prop('disabled', hidden).trigger('chosen:update');
		$("#productActiveSelect").prop('disabled', hidden).trigger('chosen:update');
		//$('#priceInput').attr('disabled', hidden);
		$("#editPricesButton").attr('disabled', hidden);
		$("#informAnmatSelect").prop('disabled', hidden).trigger('chosen:update');
		$("#coldSelect").prop('disabled', hidden).trigger('chosen:update');
	};
	
	$("#addProduct").click(function() {
		resetProductForm();
		toggleProductElements(false);
		$('#addProductButton').show();
		$('#updateProductButton').hide();
		$('#addProductLabel').show();
		$('#readProductLabel').hide();
		$('#updateProductLabel').hide();
		$('#productModal').modal('show');
	});
	
	var productsTable = $("#productsTable").bootgrid({
		columnSelection: false,
	    ajax: true,
	    requestHandler: function (request) {
	    	return request; 
	    },
	    url: "./getMatchedProducts.do",
	    formatters: {
	        "commands": function(column, row)
	        {
	            return "<button type=\"button\" class=\"btn btn-sm btn-default command-edit\" data-row-id=\"" + row.id + "\"><span class=\"glyphicon glyphicon-pencil\"></span></button> " + 
	                "<button type=\"button\" class=\"btn btn-sm btn-default command-delete\" data-row-id=\"" + row.id + "\"><span class=\"glyphicon glyphicon-trash\"></span></button>" +
	                "<button type=\"button\" class=\"btn btn-sm btn-default command-view\" data-row-id=\"" + row.id + "\"><span class=\"glyphicon glyphicon-eye-open\"></span></button>";
	        },
	        "gtin": function(column, row)
	        {
	        	if (typeof row.gtin === "undefined") {
	        		return "-";
	        	} else {
	        		return row.gtin;
	        	}
	    	},
	        "price": function(column, row)
	        {
	        	if (typeof row.price === "undefined") {
	        		return "-";
	        	} else {
	        		var price = String(row.price);
	        		return price.slice(0,-2) + "," + price.slice(-2);
	        	}
	    	}
	    }
	}).on("loaded.rs.jquery.bootgrid", function() {
		/* Executes after data is loaded and rendered */
		productsTable.find(".command-edit").on("click", function(e) {
			resetProductForm();
			toggleProductElements(false);
			readProduct($(this).data("row-id"));
			$('#addProductButton').hide();
			$('#updateProductButton').show();
			$('#addProductLabel').hide();
			$('#readProductLabel').hide();
			$('#updateProductLabel').show();
			$('#productModal').modal('show');
		}).end().find(".command-delete").on("click", function(e) {
			entity = "product";
			productId = $(this).data("row-id");
			$('#deleteConfirmationModal').modal('show');
		}).end().find(".command-view").on("click", function(e) {
			resetProductForm();
			toggleProductElements(true);
			readProduct($(this).data("row-id"));
			$('#addProductButton').hide();
			$('#updateProductButton').hide();
			$('#addProductLabel').hide();
			$('#readProductLabel').show();
			$('#updateProductLabel').hide();
			$('#productModal').modal('show');
		});
	});

	$('div#product .search').before(exportQueryTableHTML("./rest/products", '&searchPhrase=' + $('div#product .search-field').val()));
	
	$('div#product .search-field').keyup(function(e) {
		searchHTML = $('div#product .search');
		searchPhrase = '&searchPhrase=' + $('div#product .search-field').val();
		exportHTML = exportQueryTableHTML("./rest/products", searchPhrase);
		if (searchHTML.prev().length == 0) {
			searchHTML.before(exportHTML);
		} else {
			searchHTML.prev().html(exportHTML);
		}
	});
	//Fin Modulo Productos
	
	//Modulo Marcas
	
	var productBrandId;
	
	var resetProductBrandForm = function() {
		$("#productBrandIdInput").val('');
		$("#productBrandCodeInput").val('');
		$("#productBrandDescriptionInput").val('');
		$("#productBrandActiveSelect").val($("#productBrandActiveSelect option:first").val());
	};
	
	var deleteProductBrand = function(productBrandId) {
		$.ajax({
			url: "deleteProductBrand.do",
			type: "POST",
			data: {
				productBrandId: productBrandId
			},
			async: true,
			success: function(response) {
				if (response === true) {
					myReload("success", "Se ha registrado la baja con identificador:  " + productBrandId);
				} else {
					myReload("danger", "El registro no existe o ya ha sido dado de baja.");
				}
				$("#productBrandsTable").bootgrid("reload");
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myDeleteError();
			}
		});
	};
	
	var readProductBrand = function(productBrandId) {
		$.ajax({
			url: "readProductBrand.do",
			type: "GET",
			data: {
				productBrandId: productBrandId
			},
			async: false,
			success: function(response) {
				$("#productBrandIdInput").val(response.id);
				$("#productBrandCodeInput").val(response.code);
				$("#productBrandDescriptionInput").val(response.description);
				var isActive = (response.active) ? "true" : "false";
				$("#productBrandActiveSelect").val(isActive).trigger('chosen:update');
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myDeleteError();
			}
		});
	};
	
	var toggleProductBrandElements = function(hidden) {
		$("#productBrandCodeInput").attr('disabled', hidden);
		$("#productBrandDescriptionInput").attr('disabled', hidden);
		$("#productBrandActiveSelect").prop('disabled', hidden).trigger('chosen:update');
	};
	
	$("#addProductBrand").click(function() {
		resetProductBrandForm();
		toggleProductBrandElements(false);
		$('#addProductBrandButton').show();
		$('#updateProductBrandButton').hide();
		$('#addProductBrandLabel').show();
		$('#readProductBrandLabel').hide();
		$('#updateProductBrandLabel').hide();
		$('#productBrandModal').modal('show');
	});
	
	var productBrandsTable = $("#productBrandsTable").bootgrid({
		columnSelection: false,
	    ajax: true,
	    requestHandler: function (request) {
	    	return request; 
	    },
	    url: "./getMatchedProductBrands.do",
	    formatters: {
	        "commands": function(column, row)
	        {
	            return "<button type=\"button\" class=\"btn btn-sm btn-default command-edit\" data-row-id=\"" + row.id + "\"><span class=\"glyphicon glyphicon-pencil\"></span></button> " + 
	                "<button type=\"button\" class=\"btn btn-sm btn-default command-delete\" data-row-id=\"" + row.id + "\"><span class=\"glyphicon glyphicon-trash\"></span></button>" +
	                "<button type=\"button\" class=\"btn btn-sm btn-default command-view\" data-row-id=\"" + row.id + "\"><span class=\"glyphicon glyphicon-eye-open\"></span></button>";
	        }
	    }
	}).on("loaded.rs.jquery.bootgrid", function() {
		/* Executes after data is loaded and rendered */
		productBrandsTable.find(".command-edit").on("click", function(e) {
			resetProductBrandForm();
			toggleProductBrandElements(false);
			readProductBrand($(this).data("row-id"));
			$('#addProductBrandButton').hide();
			$('#updateProductBrandButton').show();
			$('#addProductBrandLabel').hide();
			$('#readProductBrandLabel').hide();
			$('#updateProductBrandLabel').show();
			$('#productBrandModal').modal('show');
		}).end().find(".command-delete").on("click", function(e) {
			entity = "productBrand";
			productBrandId = $(this).data("row-id");
			$('#deleteConfirmationModal').modal('show');
		}).end().find(".command-view").on("click", function(e) {
			resetProductBrandForm();
			toggleProductBrandElements(true);
			readProductBrand($(this).data("row-id"));
			$('#addProductBrandButton').hide();
			$('#updateProductBrandButton').hide();
			$('#addProductBrandLabel').hide();
			$('#readProductBrandLabel').show();
			$('#updateProductBrandLabel').hide();
			$('#productBrandModal').modal('show');
		});
	});

	$('div#productBrand .search').before(exportQueryTableHTML("./rest/productBrands", '&searchPhrase=' + $('div#productBrand .search-field').val()));

	$('div#productBrand .search-field').keyup(function(e) {
		searchHTML = $('div#productBrand .search');
		searchPhrase = '&searchPhrase=' + $('div#productBrand .search-field').val();
		exportHTML = exportQueryTableHTML("./rest/productBrands", searchPhrase);
		if (searchHTML.prev().length == 0) {
			searchHTML.before(exportHTML);
		} else {
			searchHTML.prev().html(exportHTML);
		}
	});
	//Fin Modulo Marcas
	
	
	//Modulo Monodroga

	var productMonodrugId;
	
	var resetProductMonodrugForm = function() {
		$("#productMonodrugIdInput").val('');
		$("#productMonodrugCodeInput").val('');
		$("#productMonodrugDescriptionInput").val('');
		$("#productMonodrugActiveSelect").val($("#productMonodrugActiveSelect option:first").val());
	};
	
	var deleteProductMonodrug = function(productMonodrugId) {
		$.ajax({
			url: "deleteProductMonodrug.do",
			type: "POST",
			data: {
				productMonodrugId: productMonodrugId
			},
			async: true,
			success: function(response) {
				if (response === true) {
					myReload("success", "Se ha registrado la baja con identificador:  " + productMonodrugId);
				} else {
					myReload("danger", "El registro no existe o ya ha sido dado de baja.");
				}
				$("#productMonodrugsTable").bootgrid("reload");
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myDeleteError();
			}
		});
	};
	
	var readProductMonodrug = function(productMonodrugId) {
		$.ajax({
			url: "readProductMonodrug.do",
			type: "GET",
			data: {
				productMonodrugId: productMonodrugId
			},
			async: false,
			success: function(response) {
				$("#productMonodrugIdInput").val(response.id);
				$("#productMonodrugCodeInput").val(response.code);
				$("#productMonodrugDescriptionInput").val(response.description);
				var isActive = (response.active) ? "true" : "false";
				$("#productMonodrugActiveSelect").val(isActive).trigger('chosen:update');
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myDeleteError();
			}
		});
	};
	
	var toggleProductMonodrugElements = function(hidden) {
		$("#productMonodrugCodeInput").attr('disabled', hidden);
		$("#productMonodrugDescriptionInput").attr('disabled', hidden);
		$("#productMonodrugActiveSelect").prop('disabled', hidden).trigger('chosen:update');
	};
	
	$("#addProductMonodrug").click(function() {
		resetProductMonodrugForm();
		toggleProductMonodrugElements(false);
		$('#addProductMonodrugButton').show();
		$('#updateProductMonodrugButton').hide();
		$('#addProductMonodrugLabel').show();
		$('#readProductMonodrugLabel').hide();
		$('#updateProductMonodrugLabel').hide();
		$('#productMonodrugModal').modal('show');
	});
	
	var productMonodrugsTable = $("#productMonodrugsTable").bootgrid({
		columnSelection: false,
	    ajax: true,
	    requestHandler: function (request) {
	    	return request; 
	    },
	    url: "./getMatchedProductMonodrugs.do",
	    formatters: {
	        "commands": function(column, row)
	        {
	            return "<button type=\"button\" class=\"btn btn-sm btn-default command-edit\" data-row-id=\"" + row.id + "\"><span class=\"glyphicon glyphicon-pencil\"></span></button> " + 
	                "<button type=\"button\" class=\"btn btn-sm btn-default command-delete\" data-row-id=\"" + row.id + "\"><span class=\"glyphicon glyphicon-trash\"></span></button>" +
	                "<button type=\"button\" class=\"btn btn-sm btn-default command-view\" data-row-id=\"" + row.id + "\"><span class=\"glyphicon glyphicon-eye-open\"></span></button>";
	        }
	    }
	}).on("loaded.rs.jquery.bootgrid", function() {
		/* Executes after data is loaded and rendered */
		productMonodrugsTable.find(".command-edit").on("click", function(e) {
			resetProductMonodrugForm();
			toggleProductMonodrugElements(false);
			readProductMonodrug($(this).data("row-id"));
			$('#addProductMonodrugButton').hide();
			$('#updateProductMonodrugButton').show();
			$('#addProductMonodrugLabel').hide();
			$('#readProductMonodrugLabel').hide();
			$('#updateProductMonodrugLabel').show();
			$('#productMonodrugModal').modal('show');
		}).end().find(".command-delete").on("click", function(e) {
			entity = "productMonodrug";
			productMonodrugId = $(this).data("row-id");
			$('#deleteConfirmationModal').modal('show');
		}).end().find(".command-view").on("click", function(e) {
			resetProductMonodrugForm();
			toggleProductMonodrugElements(true);
			readProductMonodrug($(this).data("row-id"));
			$('#addProductMonodrugButton').hide();
			$('#updateProductMonodrugButton').hide();
			$('#addProductMonodrugLabel').hide();
			$('#readProductMonodrugLabel').show();
			$('#updateProductMonodrugLabel').hide();
			$('#productMonodrugModal').modal('show');
		});
	});

	$('div#productMonodrug .search').before(exportQueryTableHTML("./rest/productMonodrugs", '&searchPhrase=' + $('div#productMonodrug .search-field').val()));

	$('div#productMonodrug .search-field').keyup(function(e) {
		searchHTML = $('div#productMonodrug .search');
		searchPhrase = '&searchPhrase=' + $('div#productMonodrug .search-field').val();
		exportHTML = exportQueryTableHTML("./rest/productMonodrugs", searchPhrase);
		if (searchHTML.prev().length == 0) {
			searchHTML.before(exportHTML);
		} else {
			searchHTML.prev().html(exportHTML);
		}
	});
	//Fin Modulo Monodroga
	
	
	//Modulo Drug Category
	
	var productDrugCategoryId;
	
	var resetProductDrugCategoryForm = function() {
		$("#productDrugCategoryIdInput").val('');
		$("#productDrugCategoryCodeInput").val('');
		$("#productDrugCategoryDescriptionInput").val('');
		$("#productDrugCategoryActiveSelect").val($("#productDrugCategoryActiveSelect option:first").val());
	};
	
	var deleteProductDrugCategory = function(productDrugCategoryId) {
		$.ajax({
			url: "deleteProductDrugCategory.do",
			type: "POST",
			data: {
				productDrugCategoryId: productDrugCategoryId
			},
			async: true,
			success: function(response) {
				if (response === true) {
					myReload("success", "Se ha registrado la baja con identificador:  " + productDrugCategoryId);
				} else {
					myReload("danger", "El registro no existe o ya ha sido dado de baja.");
				}
				$("#productDrugCategorysTable").bootgrid("reload");
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myDeleteError();
			}
		});
	};
	
	var readProductDrugCategory = function(productDrugCategoryId) {
		$.ajax({
			url: "readProductDrugCategory.do",
			type: "GET",
			data: {
				productDrugCategoryId: productDrugCategoryId
			},
			async: false,
			success: function(response) {
				$("#productDrugCategoryIdInput").val(response.id);
				$("#productDrugCategoryCodeInput").val(response.code);
				$("#productDrugCategoryDescriptionInput").val(response.description);
				var isActive = (response.active) ? "true" : "false";
				$("#productDrugCategoryActiveSelect").val(isActive).trigger('chosen:update');
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myDeleteError();
			}
		});
	};
	
	var toggleProductDrugCategoryElements = function(hidden) {
		$("#productDrugCategoryCodeInput").attr('disabled', hidden);
		$("#productDrugCategoryDescriptionInput").attr('disabled', hidden);
		$("#productDrugCategoryActiveSelect").prop('disabled', hidden).trigger('chosen:update');
	};
	
	$("#addProductDrugCategory").click(function() {
		resetProductDrugCategoryForm();
		toggleProductDrugCategoryElements(false);
		$('#addProductDrugCategoryButton').show();
		$('#updateProductDrugCategoryButton').hide();
		$('#addProductDrugCategoryLabel').show();
		$('#readProductDrugCategoryLabel').hide();
		$('#updateProductDrugCategoryLabel').hide();
		$('#productDrugCategoryModal').modal('show');
	});
	
	var productDrugCategoriesTable = $("#productDrugCategoriesTable").bootgrid({
		columnSelection: false,
	    ajax: true,
	    requestHandler: function (request) {
	    	return request; 
	    },
	    url: "./getMatchedProductDrugCategories.do",
	    formatters: {
	        "commands": function(column, row)
	        {
	            return "<button type=\"button\" class=\"btn btn-sm btn-default command-edit\" data-row-id=\"" + row.id + "\"><span class=\"glyphicon glyphicon-pencil\"></span></button> " + 
	                "<button type=\"button\" class=\"btn btn-sm btn-default command-delete\" data-row-id=\"" + row.id + "\"><span class=\"glyphicon glyphicon-trash\"></span></button>" +
	                "<button type=\"button\" class=\"btn btn-sm btn-default command-view\" data-row-id=\"" + row.id + "\"><span class=\"glyphicon glyphicon-eye-open\"></span></button>";
	        }
	    }
	}).on("loaded.rs.jquery.bootgrid", function() {
		/* Executes after data is loaded and rendered */
		productDrugCategoriesTable.find(".command-edit").on("click", function(e) {
			resetProductDrugCategoryForm();
			toggleProductDrugCategoryElements(false);
			readProductDrugCategory($(this).data("row-id"));
			$('#addProductDrugCategoryButton').hide();
			$('#updateProductDrugCategoryButton').show();
			$('#addProductDrugCategoryLabel').hide();
			$('#readProductDrugCategoryLabel').hide();
			$('#updateProductDrugCategoryLabel').show();
			$('#productDrugCategoryModal').modal('show');
		}).end().find(".command-delete").on("click", function(e) {
			entity = "productDrugCategory";
			productDrugCategoryId = $(this).data("row-id");
			$('#deleteConfirmationModal').modal('show');
		}).end().find(".command-view").on("click", function(e) {
			resetProductDrugCategoryForm();
			toggleProductDrugCategoryElements(true);
			readProductDrugCategory($(this).data("row-id"));
			$('#addProductDrugCategoryButton').hide();
			$('#updateProductDrugCategoryButton').hide();
			$('#addProductDrugCategoryLabel').hide();
			$('#readProductDrugCategoryLabel').show();
			$('#updateProductDrugCategoryLabel').hide();
			$('#productDrugCategoryModal').modal('show');
		});
	});

	$('div#productDrugCategory .search').before(exportQueryTableHTML("./rest/productDrugCategories", '&searchPhrase=' + $('div#productDrugCategory .search-field').val()));

	$('div#productDrugCategory .search-field').keyup(function(e) {
		searchHTML = $('div#productDrugCategory .search');
		searchPhrase = '&searchPhrase=' + $('div#productDrugCategory .search-field').val();
		exportHTML = exportQueryTableHTML("./rest/productDrugCategories", searchPhrase);
		if (searchHTML.prev().length == 0) {
			searchHTML.before(exportHTML);
		} else {
			searchHTML.prev().html(exportHTML);
		}
	});
	//Fin Modulo Drug Category
	
	
	//Modulo Grupo de Productos	
	
	var productGroupId;
	
	var resetProductGroupForm = function() {
		$("#productGroupIdInput").val('');
		$("#productGroupCodeInput").val('');
		$("#productGroupDescriptionInput").val('');
		$("#productGroupActiveSelect").val($("#productGroupActiveSelect option:first").val());
	};
	
	var deleteProductGroup = function(productGroupId) {
		$.ajax({
			url: "deleteProductGroup.do",
			type: "POST",
			data: {
				productGroupId: productGroupId
			},
			async: true,
			success: function(response) {
				if (response === true) {
					myReload("success", "Se ha registrado la baja con identificador:  " + productGroupId);
				} else {
					myReload("danger", "El registro no existe o ya ha sido dado de baja.");
				}
				$("#productGroupsTable").bootgrid("reload");
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myDeleteError();
			}
		});
	};
	
	var readProductGroup = function(productGroupId) {
		$.ajax({
			url: "readProductGroup.do",
			type: "GET",
			data: {
				productGroupId: productGroupId
			},
			async: false,
			success: function(response) {
				$("#productGroupIdInput").val(response.id);
				$("#productGroupCodeInput").val(response.code);
				$("#productGroupDescriptionInput").val(response.description);
				var isActive = (response.active) ? "true" : "false";
				$("#productGroupActiveSelect").val(isActive).trigger('chosen:update');
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myDeleteError();
			}
		});
	};
	
	var toggleProductGroupElements = function(hidden) {
		$("#productGroupCodeInput").attr('disabled', hidden);
		$("#productGroupDescriptionInput").attr('disabled', hidden);
		$("#productGroupActiveSelect").prop('disabled', hidden).trigger('chosen:update');
	};
	
	$("#addProductGroup").click(function() {
		resetProductGroupForm();
		toggleProductGroupElements(false);
		$('#addProductGroupButton').show();
		$('#updateProductGroupButton').hide();
		$('#addProductGroupLabel').show();
		$('#readProductGroupLabel').hide();
		$('#updateProductGroupLabel').hide();
		$('#productGroupModal').modal('show');
	});

	var productGroupsTable = $("#productGroupsTable").bootgrid({
		columnSelection: false,
	    ajax: true,
	    requestHandler: function (request) {
	    	return request; 
	    },
	    url: "./getMatchedProductGroups.do",
	    formatters: {
	        "commands": function(column, row)
	        {
	            return "<button type=\"button\" class=\"btn btn-sm btn-default command-edit\" data-row-id=\"" + row.id + "\"><span class=\"glyphicon glyphicon-pencil\"></span></button> " + 
	                "<button type=\"button\" class=\"btn btn-sm btn-default command-delete\" data-row-id=\"" + row.id + "\"><span class=\"glyphicon glyphicon-trash\"></span></button>" +
	                "<button type=\"button\" class=\"btn btn-sm btn-default command-view\" data-row-id=\"" + row.id + "\"><span class=\"glyphicon glyphicon-eye-open\"></span></button>";
	        }
	    }
	}).on("loaded.rs.jquery.bootgrid", function() {
		/* Executes after data is loaded and rendered */
		productGroupsTable.find(".command-edit").on("click", function(e) {
			resetProductGroupForm();
			toggleProductGroupElements(false);
			readProductGroup($(this).data("row-id"));
			$('#addProductGroupButton').hide();
			$('#updateProductGroupButton').show();
			$('#addProductGroupLabel').hide();
			$('#readProductGroupLabel').hide();
			$('#updateProductGroupLabel').show();
			$('#productGroupModal').modal('show');
		}).end().find(".command-delete").on("click", function(e) {
			entity = "productGroup";
			productGroupId = $(this).data("row-id");
			$('#deleteConfirmationModal').modal('show');
		}).end().find(".command-view").on("click", function(e) {
			resetProductGroupForm();
			toggleProductGroupElements(true);
			readProductGroup($(this).data("row-id"));
			$('#addProductGroupButton').hide();
			$('#updateProductGroupButton').hide();
			$('#addProductGroupLabel').hide();
			$('#readProductGroupLabel').show();
			$('#updateProductGroupLabel').hide();
			$('#productGroupModal').modal('show');
		});
	});

	$('div#productGroup .search').before(exportQueryTableHTML("./rest/productGroups", '&searchPhrase=' + $('div#productGroup .search-field').val()));

	$('div#productGroup .search-field').keyup(function(e) {
		searchHTML = $('div#productGroup .search');
		searchPhrase = '&searchPhrase=' + $('div#productGroup .search-field').val();
		exportHTML = exportQueryTableHTML("./rest/productGroups", searchPhrase);
		if (searchHTML.prev().length == 0) {
			searchHTML.before(exportHTML);
		} else {
			searchHTML.prev().html(exportHTML);
		}
	});
	//Fin Modulo Grupo de Productos
	
	$("#deleteEntityButton").click(function() {
		if (entity === "product") {
			deleteProduct(productId);
		} else if (entity === "productBrand") {
			deleteProductBrand(productBrandId);
		} else if (entity === "productMonodrug") {
			deleteProductMonodrug(productMonodrugId);
		} else if (entity === "productDrugCategory") {
			deleteProductDrugCategory(productDrugCategoryId);
		} else {
			deleteProductGroup(productGroupId);
		}
	});
};