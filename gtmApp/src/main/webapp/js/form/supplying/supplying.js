var Supplying = function() {

	var assignOutOfStock = false;
	
	var currentRowElement = null;

	var addAffiliateModalFormValidator = null;
	var amountFormValidator = null;

	var currentDate = new Date();
	
	var batchExpirationDate = new OutputBatchExpirationDate();
	var serialized = new OutputSerialized();
	
	var outOfStockBatchExpirationDate = new BatchExpirationDate();
	var outOfStockProviderSerialized = new ProviderSerialized();
	var outOfStockSelfSerialized = new SelfSerialized();
	
	// Los productDetails agrupados por fila
	var supplyingDetailGroup = [];
	
	// Mapa con los series que se cargaron por cada producto 
	var tempSerialNumberGroup = {};
	
	// Mapa con los stockIds que se cargaron por cada producto 
	var tempStockIdsGroup = {};
	
	var productDescription = "";
	var productAmount = "";
	var productId = "";
	var productType = "";
	var productGtin = "";
	var productOutOfStock = "";
	
	// Para validar que no ingrese 2 veces el mismo producto 
	var productIds = [];
	
	// La fila donde se hace click (en el editar o borrar)
	var currentRow = 0;
	
	var isEdit = false;

	var isButtonConfirm = false;
	
	$("#productInput").attr("disabled", true);

    $("#affiliateDocumentTypeSelect").chosen(
            {
                width: '100%' /* desired width */
            });

	var cleanAddAffiliateModal = function() {
		$("#addAffiliateModal input").val("");
		$("#addAffiliateModal select").val("");
		$('#affiliateDocumentTypeSelect').val($("#affiliateDocumentTypeSelect option:first").val());
		myResetForm($("#addAffiliateModalForm")[0],
				addAffiliateModalFormValidator);
	};

	var cleanAmountModal = function() {
		myResetForm($("#productAmountModalForm")[0], amountFormValidator);
	};

	var cleanProductInput = function() {
		$('#productInput').val("");
	};

	var validateForm = function() {
		var form = $("#supplyingForm");
		$("#supplyingForm").validate({
			rules : {
				client : {
					required : true
				},
				affiliate : {
					required : true
				},
				agreement: {
					required: true
				},
				currentDate : {
					required : true,
					formatDate: true
				}
			},
			showErrors : myShowErrors,
			onsubmit : false
		});
		return form.valid();
	};

	var validateProductAmountForm = function() {
		var form = $("#productAmountModalForm");
		amountFormValidator = form.validate({
			rules : {
				productAmount : {
					required : true,
					digits : true,
					minValue : 0
				}
			},
			showErrors : myShowErrors,
			onsubmit : false
		});
		return form.valid();
	};

	$("#affiliateInput").select2(
			{
				// allowClear: true,
				placeholder : "Buscar afiliado...",
				minimumInputLength : 3,
				//theme: "classic",
				initSelection : function(element, callback) {
					var data = {
						code : element.attr("code"),
						name : element.attr("name"),
						surname : element.attr("surname")
					};
					callback(data);
				},
				ajax : {
					url : "getAffiliates.do",
					dataType : 'json',
					quietMillis : 250,
					data : function(term, page) { // page is the one-based
						// page number tracked by
						// Select2
						return {
							term : term, // search term
							pageNumber : page, // page number
							pageSize : 10, // page number
							clientId : parseInt($("#clientInput").val()),
							active : true
						};
					},
					results : function(data, page, query) {
						var more = (data.length == 10); // whether or not there
						// are more results
						// available

						var parsedResults = [];

						$.each(data, function(index, value) {
							parsedResults.push({
								id : value.id,
								text : value.code + " - " + value.surname + " "
										+ value.name
							});
						});

						// notice we return the value of more so Select2 knows
						// if more results can be loaded
						return {
							results : parsedResults,
							more : more
						};
					}
				},
				formatResult : function(data) {
					return "<div class='select2-user-result'>" + data.text
							+ "</div>";
				},
				formatSelection : function(data) {
					return data.text;
				}
			});

	$("#affiliateInput").select2("enable", false);

	$('.select2-input').on('keydown', function(e) {
		if(e.keyCode === 121) {
			cleanAddAffiliateModal();
			e.preventDefault();
			$("#affiliateInput").select2("close");
		}
	});

	$('#currentDateButton').click(function() {
		$("#currentDateInput").datepicker().focus();
	});

	$("#currentDateInput").datepicker().datepicker("setDate", currentDate);

	$('#currentDateButton').click(function() {
		$("#currentDateInput").datepicker().focus();
	});

	$("#productInput").autocomplete({
		source : function(request, response) {
			var url = assignOutOfStock ? "getProducts.do" : "getProductFromStock.do";
			var data = assignOutOfStock ? { term: request.term, active: true } : { term : request.term, agreementId : $("#agreementInput").val() };
			$.ajax({
				url : url,
				type : "GET",
				async : false,
				data : data,
				success : function(data) {
					var array = $.map(data, function(item) {
						var cold = " Frio: ";
						cold += item.cold == true ? "Si" : "No";
						return {
							id : item.id,
							label : item.code + " - " + item.description + " - " + item.brand.description + " - " + item.monodrug.description+ " - " + cold,
							value : item.code + " - " + item.description,
							gtin: item.lastGtin,
							type: item.type
						};
					});
					response(array);
				},
				error : function(jqXHR, textStatus, errorThrown) {
					myGenericError();
				}
			});
		},
		select : function(event, ui) {
			if (!productEntered(ui.item.id)) {
				productId = ui.item.id;
				productGtin = ui.item.gtin;
				productDescription = ui.item.value;
				productType = ui.item.type;
				$('#productInput').data("title", "").removeClass("has-error").tooltip("destroy");
				$("#productInput").val(productDescription);
				$('#amountModal').modal('show');
			} else {
				myShowAlert('danger', 'Producto ya Ingresado');
				$("#productInput").val("");
			}
			return false;
		},
		minLength : 3,
		autoFocus : true
	});
	
	$('#productInput').keydown(function(e) {
		if(e.keyCode == 13){ // Presiono Enter
	    	$.ajax({
				url: "getProductFromStockBySerialOrGtin.do",
				type: "GET",
				data: {
					serial: $(this).val(),
					agreementId: $("#agreementInput").val()
				},
				success: function(response) {
					if (response != "") {
						if (!productEntered(response.id)) {
							productId = response.id;
							productGtin = response.lastGtin;
							productDescription = response.code + ' - ' + response.description;
							productType = response.type;
							
							$('#productInput').data("title", "").removeClass("has-error").tooltip("destroy");
							
							$("#productInput").val(productDescription);
							$('#amountModal').modal('show');
						} else {
							myShowAlert('danger', 'Producto ya Ingresado');
							$("#productInput").val("");
						}
					} else {
						if(!assignOutOfStock){
							$('#productInput').tooltip("destroy").data("title", "No hay en Stock").addClass("has-error").tooltip();
							$('#productInput').val('');
							$('#productInput').focus();
						}
					}
					return false;
				},
				error: function(jqXHR, textStatus, errorThrown) {
					myGenericError();
				}
			});
	    }
		if(e.keyCode == 119) { // Presiono F8
			BootstrapDialog.show({
				type: BootstrapDialog.TYPE_WARNING,
				message: 'Desea asignar productos fuera de inventario?.',
				closable: false,
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
						assignOutOfStock = true;
						dialogItself.close();
					}
				}],
				onhidden: function(dialogRef) {
					myShowAlert('info', 'ASIGNACION FUERA DE STOCK.', null, 0);
					$("#productInput").focus();
				}
			});
		}
	});
/*
	$("#affiliateDocumentTypeInput").autocomplete({
		source : [ "DNI", "LC", "LE" ],
		minLength : 0,
		autoFocus : true,
		appendTo : "#addAffiliateModal"
	}).focus(function() {
		$(this).autocomplete("search", $("#affiliateDocumentTypeInput").val());
	});
	
	*/

	$('#amountModal').on('shown.bs.modal', function() {
		$('#productAmountInput').focus();

	});

	$('#amountModal').on('hidden.bs.modal', function() {
		cleanAmountModal();
		cleanProductInput();
	});
	
	$('#agreementInput').on('change', function(evt, params) {
		if($("#agreementInput").val() == ""){
			$("#productInput").attr("disabled", true);
		}else{
			$("#productInput").attr("disabled", false);
		}
	});

	$('#amountModal').on('keypress', function(e) {
		if (e.keyCode === 13) {
			$("#amountModalAcceptButton").trigger('click');
			return false;
		}
	});

	$("#amountModalAcceptButton").click(function() {
		if (validateProductAmountForm()) {
			productAmount = parseInt($("#productAmountInput").val());
			// Si asigno productos que estan en el inventario.
			if (!assignOutOfStock) {
				$.ajax({
					url : "getProductAmount.do",
					type : "GET",
					async : false,
					data : {
						productId : productId,
						agreementId : $("#agreementInput").val(),
						provisioningId : null
					},
					success : function(response) {
						if (response != "" && response >= productAmount) {
							$('#agreementInput').prop('disabled', true).trigger("chosen:updated");
							openModal(null);
							$("#amountModal").modal('hide');
						} else {
							$("#productAmountInput").tooltip("destroy").data("title", "Stock disponible: " + response).addClass("has-error").tooltip();
							return false;
						}
					},
					error : function(jqXHR, textStatus, errorThrown) {
						myGenericError();
					}
				});
				// Si asigno productos que no estan en inventario.
			} else {
				$('#agreementInput').prop('disabled', true).trigger("chosen:updated");
				outOfStockOpenModal(null);
				$("#amountModal").modal('hide');
			}
		}
	});

	var openModal = function(preloadedData) {
		if (productType == "BE") {
			batchExpirationDate.setPreloadedProduct(productDescription);
			batchExpirationDate.setPreloadedProductId(productId);
			batchExpirationDate.setPreloadedAmount(productAmount);
			batchExpirationDate.setPreloadedData(preloadedData);
			batchExpirationDate.setPreloadedStockIds(tempStockIdsGroup[productId]);
			
			batchExpirationDate.preloadModalData();
			$('#batchExpirationDateModal').modal('show');
			
		} else {
			serialized.setTempSerialNumbers(tempSerialNumberGroup[productId]);
			serialized.setPreloadedProduct(productDescription);
			serialized.setPreloadedProductId(productId);
			serialized.setPreloadedProductType(productType);
			serialized.setPreloadedAmount(productAmount);
			serialized.setPreloadedData(preloadedData);
			serialized.setProductSelectedGtin(productGtin);
			serialized.setFormatSerializedId(null);
			
			serialized.preloadModalData();
			$('#serializedModal').modal('show');
		}
	};
	
	var outOfStockOpenModal = function(preloadedData) {
		if (productType == "BE") {
			outOfStockBatchExpirationDate.setPreloadedProduct(productDescription);
			outOfStockBatchExpirationDate.setPreloadedAmount(productAmount);
			outOfStockBatchExpirationDate.setPreloadedData(preloadedData);
			outOfStockBatchExpirationDate.preloadModalData();
			$('#outOfStockBatchExpirationDateModal').modal('show');
			
		} else if (productType == "PS") {
			outOfStockProviderSerialized.setTempSerialNumbers(tempSerialNumberGroup[productId]);
			outOfStockProviderSerialized.setProductSelectedGtin(productGtin);
			outOfStockProviderSerialized.setPreloadedProduct(productDescription);
			outOfStockProviderSerialized.setPreloadedProductId(productId);
			outOfStockProviderSerialized.setPreloadedAmount(productAmount);
			outOfStockProviderSerialized.setPreloadedData(preloadedData);
			outOfStockProviderSerialized.setFormatSerializedId(null);
			outOfStockProviderSerialized.preloadModalData();
			$('#outOfStockProviderSerializedModal').modal('show');
			
		} else {
			outOfStockSelfSerialized.setPreloadedProduct(productDescription);
			outOfStockSelfSerialized.setPreloadedAmount(productAmount);
			outOfStockSelfSerialized.setPreloadedData(preloadedData);
			outOfStockSelfSerialized.preloadModalData();
			$('#outOfStockSelfSerializedModal').modal('show');
		}
	};
	
	var populateInputDetails = function(productDetails, serialNumber, batch, expirationDate, amount, gtin) {
		var orderDetail = {
			"productId": productId,
			"serialNumber": serialNumber,
			"batch": batch,
			"expirationDate": expirationDate,
			"amount": amount,
			"gtin": gtin,
			"inStock": !assignOutOfStock
		};
		productDetails.push(orderDetail);
	};
	
	$('#productTableBody').on("click", ".edit-row", function(e) {
		var parent = $(this).parent().parent();

		currentRow = $(".edit-row").index(this);
		productDescription = parent.find(".td-description").html();
		productAmount = parent.find(".td-amount").html();
		productId = parent.find(".span-productId").html();
		productType = parent.find(".span-productType").html();
		productGtin = parent.find(".span-productGtin").html();
		productOutOfStock = parent.find(".span-productOutOfStock").html();
		isEdit = true;
		
		if(productOutOfStock === 'false') {
			openModal(supplyingDetailGroup[currentRow]);
		} else {
			outOfStockOpenModal(supplyingDetailGroup[currentRow]);
		}
	});

	var currentRowElement = null;

	$('#productTableBody').on("click", ".delete-row", function(){
		currentRowElement = this;
		$('#deleteRowConfirmationModal').modal();
	});

	$("#inputDeleteRowConfirmationButton").click(function() {
		var parent = $(currentRowElement).parent().parent();
		var rows = Array();
		rows[0] = parent.attr("data-row-id");
		$("#productTable").bootgrid("remove", rows);

		currentRow = $(".delete-row").index(currentRowElement);
		supplyingDetailGroup.splice(currentRow, 1);
		productIds.splice(currentRow, 1);

		productId = parent.find(".span-productId").html();
		$(".alert").hide();

		var productType = parent.find(".span-productType").html();
		if (productType == "PS") {
			tempSerialNumberGroup[productId] = [];
			/*$.each(tempSerialNumberGroup[productId], function(idxSerialToDelete, serialToDelete) {
				var idxSerialStored = $.inArray(serialToDelete, tempSerialNumberGroup[productId]);
				if (idxSerialStored != -1) {
					tempSerialNumberGroup[productId].splice(idxSerialStored, 1);
				}
			});*/
		}
	});
	
	var populateProductsDetailsTable = function() {
		var aaData = [];
		var productTableIdDiscriminator = assignOutOfStock ? "(*)" : "";
		var productTableId = productDescription + productTableIdDiscriminator;
		var row = {
			description: productTableId,
			amount: productAmount,
			command: "<span class='span-productId' style='display:none'>" + productId + "</span>"+
			"<span class='span-productType' style='display:none'>" + productType + "</span>"+
			"<span class='span-productGtin' style='display:none'>" + productGtin + "</span>"+
			"<span class='span-productOutOfStock' style='display:none'>" + assignOutOfStock + "</span>" +
			"<button type=\"button\" class=\"btn btn-sm btn-default edit-row\"><span class=\"glyphicon glyphicon-pencil\"></span></button>"+
			"<button type=\"button\" class=\"btn btn-sm btn-default delete-row\"><span class=\"glyphicon glyphicon-trash\"></span></button>"
		};
		aaData.push(row);
		$("#productTable").bootgrid("append", aaData);
	};

	$("#batchExpirationDateAcceptButton").click(function() {
		remainingAmount = $('#batchExpirationDateRemainingAmountLabel').text();
		if (remainingAmount == 0) {
			
			var amounts = $("#batchExpirationDateTable td.amount");
			var batchs = $("#batchExpirationDateTable td.batch");
			var expirationDates = $("#batchExpirationDateTable td.expirationDate");
			var stockIds = $("#batchExpirationDateTable span.stockId");
			
			var productDetails = [];
			var tempStockIds = [];
			
			for (var i = 0; i < amounts.length; i++) {
				populateInputDetails(productDetails, null, batchs[i].innerHTML, expirationDates[i].innerHTML, amounts[i].innerHTML, productGtin);
				tempStockIds[i] = parseInt(stockIds[i].innerHTML);
			}
			if(isEdit){
				supplyingDetailGroup[currentRow] = productDetails;
			}else{
				supplyingDetailGroup.push(productDetails);
				populateProductsDetailsTable();
				productIds.push(productId);
			}
				
			tempStockIdsGroup[productId] = tempStockIds;
			
			$("#batchExpirationDateModal").modal("hide");
			$(".alert").hide();
			$('#productInput').focus();
		} else {
			myShowAlert('danger', 'No se ha ingresado la totalidad de productos requeridos. Por favor ingrese los restantes.', "batchExpirationDateModalAlertDiv");
		}
	});

	$("#serializedAcceptButton").click(function() {
		remainingAmount = $('#serializedRemainingAmountLabel').text();
		if (remainingAmount == 0) {
			
            var gtins = $("#serializedTable td.gtin");
			var serialNumbers = $("#serializedTable td.serialNumber");
			var batchs = $("#serializedTable td.batch");
			var expirationDates = $("#serializedTable td.expirationDate");
			
			var productDetails = [];
			var tempSerialNumber = [];
			
			for (var i = 0; i < serialNumbers.length; i++) {
				populateInputDetails(productDetails, serialNumbers[i].innerHTML, batchs[i].innerHTML, expirationDates[i].innerHTML, 1, gtins[i].innerHTML);
				tempSerialNumber[i] = serialNumbers[i].innerHTML;
			}

			if(isEdit){
				supplyingDetailGroup[currentRow] = productDetails;
			}else{
				supplyingDetailGroup.push(productDetails);
				populateProductsDetailsTable();
				productIds.push(productId);
			}
			tempSerialNumberGroup[productId] = tempSerialNumber;
			
			$("#serializedModal").modal("hide");
			$(".alert").hide();
			$('#productInput').focus();
		} else {
			myShowAlert('danger', 'No se ha ingresado la totalidad de productos requeridos. Por favor ingrese los restantes.', "serializedModalAlertDiv");
		}
	});
	
	$("#outOfStockBatchExpirationDateAcceptButton").click(function() {
		remainingAmount = $('#outOfStockBatchExpirationDateRemainingAmountLabel').text();
		if (remainingAmount == 0) {
			
			var amounts = $("#outOfStockBatchExpirationDateTable td.amount");
			var batchs = $("#outOfStockBatchExpirationDateTable td.batch");
			var expirationDates = $("#outOfStockBatchExpirationDateTable td.expirationDate");
			
			var productDetails = [];
			
			for (var i = 0; i < amounts.length; i++) {
				populateInputDetails(productDetails, null, batchs[i].innerHTML, expirationDates[i].innerHTML, amounts[i].innerHTML, productGtin);
			}
			
			if (outOfStockBatchExpirationDate.getPreloadedData() == null) {
				supplyingDetailGroup.push(productDetails);
				populateProductsDetailsTable();
				productIds.push(productId);
			} else {
				supplyingDetailGroup[currentRow] = productDetails;
				outOfStockBatchExpirationDate.setPreloadedData(null);
			}
			
			$('#outOfStockBatchExpirationDateModal').modal('hide');
			$(".alert").hide();
			$('#productInput').focus();
		} else {
			myShowAlert('danger', 'No se ha ingresado la totalidad de productos requeridos. Por favor ingrese los restantes.', "batchExpirationDateModalAlertDiv");
		}
	});

	$("#outOfStockProviderSerializedAcceptButton").click(function() {
		remainingAmount = $('#outOfStockProviderSerializedRemainingAmountLabel').text();
		if (remainingAmount == 0) {

			var gtins = $("#outOfStockProviderSerializedTable td.gtin");
			var serialNumbers = $("#outOfStockProviderSerializedTable td.serialNumber");
			var batchs = $("#outOfStockProviderSerializedTable td.batch");
			var expirationDates = $("#outOfStockProviderSerializedTable td.expirationDate");

			var productDetails = [];
			var tempSerialNumber = [];

			for (var i = 0; i < serialNumbers.length; i++) {
				populateInputDetails(productDetails, serialNumbers[i].innerHTML, batchs[i].innerHTML, expirationDates[i].innerHTML, 1, gtins[i].innerHTML);
				tempSerialNumber[i] = serialNumbers[i].innerHTML;
			}

			tempSerialNumberGroup[productId] = tempSerialNumber;

			if (outOfStockProviderSerialized.getPreloadedData() == null) {
				supplyingDetailGroup.push(productDetails);
				populateProductsDetailsTable();
				productIds.push(productId);
			} else {
				supplyingDetailGroup[currentRow] = productDetails;
				outOfStockProviderSerialized.setPreloadedData(null);
			}

			$('#outOfStockProviderSerializedModal').modal('hide');
			$(".alert").hide();
			$('#productInput').focus();
		} else {
			myShowAlert('danger', 'No se ha ingresado la totalidad de productos requeridos. Por favor ingrese los restantes.', "providerSerializedModalAlertDiv");
		}
	});

	$("#outOfStockSelfSerializedAcceptButton").click(function() {
		remainingAmount = $('#outOfStockSelfSerializedRemainingAmountLabel').text();
		if (remainingAmount == 0) {
			
			var amounts = $("#outOfStockSelfSerializedTable td.amount");
			var batchs = $("#outOfStockSelfSerializedTable td.batch");
			var expirationDates = $("#outOfStockSelfSerializedTable td.expirationDate");
			
			var productDetails = [];
			
			for (var i = 0; i < amounts.length; i++) {
				populateInputDetails(productDetails, null, batchs[i].innerHTML, expirationDates[i].innerHTML, amounts[i].innerHTML, productGtin);
			}
			
			if (outOfStockSelfSerialized.getPreloadedData() == null) {
				supplyingDetailGroup.push(productDetails);
				populateProductsDetailsTable();
				productIds.push(productId);
			} else {
				supplyingDetailGroup[currentRow] = productDetails;
				outOfStockSelfSerialized.setPreloadedData(null);
			}
			
			$('#outOfStockSelfSerializedModal').modal('hide');
			$(".alert").hide();
			$('#productInput').focus();
		} else {
			myShowAlert('danger', 'No se ha ingresado la totalidad de productos requeridos. Por favor ingrese los restantes.', "selfSerializedModalAlertDiv");
		}
	});
	
	$("#confirmButton").on('click', function(e) {
		if (validateForm()) {
			if (supplyingDetailGroup.length > 0) {
				if ($("#affiliateInput").val() != "") {
					$(this).attr("disabled", true);
					var jsonSupplying = {
						"id" : $("#supplyingId").val(),
						"clientId" : $("#clientInput").val(),
						"affiliateId" : $("#affiliateInput").val(),
						"agreementId": $("#agreementInput").val(),
						"date" : $("#currentDateInput").val(),
						"supplyingDetails" : []
					};

					for (var i = 0, lengthI = supplyingDetailGroup.length; i < lengthI; i++) {
						for (var j = 0; lengthJ = supplyingDetailGroup[i].length, j < lengthJ; j++) {
							jsonSupplying.supplyingDetails.push(supplyingDetailGroup[i][j]);
						}
					}

					isButtonConfirm = true;

					$.ajax({
						url : "saveSupplying.do",
						type : "POST",
						contentType : "application/json",
						data : JSON.stringify(jsonSupplying),
						async : true,
						beforeSend : function() {
							$.blockUI({ message: 'Espere un Momento por favor...' });
						},
						success : function(response) {
						},
						error : function(jqXHR,	textStatus, errorThrown) {
							myGenericError();
						},
						complete: function(jqXHR, textStatus) {
							$.unblockUI();
							if (textStatus === 'success') {
								generateSupplyingPDFReport(jqXHR.responseJSON);
							}
						}
					});
				} else {
					myShowAlert('warning', 'Por favor, seleccione un afiliado.');
				}
			} else {
				myShowAlert('warning', 'Por favor, ingrese al menos un producto.');
			}
		}
	});

	$('#addAffiliateModalForm').on('keypress', function(e) {
		// Si la tecla presionada es 'ENTER'
		if (e.keyCode === 13) {
			$("#addAffiliateModalAcceptButton").trigger('click');
			return false;
		}
	});

	$('#my-select').multiSelect();

	new AddAffiliate();

	var productEntered = function(productId) {
		if (!assignOutOfStock) {
			for (var i = 0, l = productIds.length; i < l; ++i) {
				if (productIds[i] == productId) {
					return true;
				}
			}
		}
		return false;
	};

	var hasChanged = function() {
		if (supplyingDetailGroup.length > 0) {
			return true;
		} else {
			return false;
		}
	};

	$(window).bind("beforeunload", function(event) {
		if (hasChanged() && isButtonConfirm == false) {
			return "Existen cambios que no fueron confirmados.";
		} /*else {
			isButtonConfirm = false;
		}*/
	});

	$('#clientInput').on('change', function(evt, params) {
		if ($("#clientInput").val() == "") {
			$("#affiliateInput").select2("enable", false);
		} else {
			$("#affiliateInput").select2("enable", true);
		}
		$("#affiliateInput").select2("val", "");
	});

	// TODO eliminar si con el keydown funciona correctamente.
	/*$("#supplyingForm input, #supplyingForm select").keypress(function(event) {
		return event.keyCode != 13;
	});*/

	$("#supplyingForm input, #supplyingForm select").keydown(function(event) {
		if(event.keyCode == 115) { // Presiono F4
			$("#confirmButton").trigger('click');
		} else {
			return event.keyCode != 13;
		}
	});

	$('#addAffiliateModal').on('shown.bs.modal', function (e) {
		$('#affiliateCodeInput').focus();
	});

	$('.select2-input').on("keydown", function(e) {
		if (e.keyCode === 121) {
			$('#addAffiliateModal').modal('show');
		}
	});
};