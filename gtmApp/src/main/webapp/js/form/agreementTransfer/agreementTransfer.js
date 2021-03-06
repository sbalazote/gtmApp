
AgreementTransfer = function() {

	var currentDate = new Date();
	
	//var batchExpirationDate = new OutputBatchExpirationDate();
	var serialized = new OutputSerialized();
	
	// Los orderDetails agrupados por fila
	var agreementTransferDetailGroup = [];
	
	// Mapa con los series que se cargaron por cada producto 
	var tempSerialNumberGroup = {};
	
	// Mapa con los stockIds que se cargaron por cada producto 
	var tempStockIdsGroup = {};
	
	var productDescription = "";
	var productAmount = "";
	var productId = "";
	var productType = "";
	var productGtin = "";
	
	// Para validar que no ingrese 2 veces el mismo producto 
	var productIds = [];
	
	// La fila donde se hace click (en el editar o borrar)
	var currentRow = 0;
	
	var isEdit = false;
	
	//Se esconde el div de cliente
	$("#deliveryLocationDiv").hide();
	$("#productOutput").attr("disabled", true);
	
	var validateProductAmountForm = function() {
		var form = $("#productAmountModalForm");
		amountFormValidator = form.validate({
			rules: {
				productAmount: {
					required: true,
					digits: true,
					minValue: 0
				}
			},
			showErrors: myShowErrors,
			onsubmit: false
		});
		return form.valid();
	};
	
	var validateForm = function() {
		var form = $("#agreementTransferForm");
		form.validate({
			rules: {
				agreementInput: {
					required: true
				},
				destinationAgreement: {
					required: true
				}
			},
			showErrors: myShowErrors,
			onsubmit: false
		});
		return form.valid();
	};
	
	var cleanAmountModal = function() {
		myResetForm($("#productAmountModalForm")[0], amountFormValidator);
	};
	
	var cleanProductInput = function() {
		$('#productOutput').val("");
	};
	
	$('#amountModal').on('shown.bs.modal', function () {
	    $('#productAmountInput').focus();
	});
	
	$('#amountModal').keypress(function(e) {
        if (e.keyCode === 13) {
        	$("#amountModalAcceptButton").trigger('click');
        	return false;
        }
    });
	
	$('#amountModal').on('hidden.bs.modal', function () {
	    cleanAmountModal();
	    cleanProductInput();
	});
	
	$('#agreementInput').on('change', function(evt, params) {
		if($("#agreementInput").val() == ""){
			$("#productOutput").attr("disabled", true);
		}else{
			$("#productOutput").attr("disabled", false);
		}
	});
	
	var productEntered = function(productId) {
		for (var i = 0, l = productIds.length; i < l; ++i) {
			if (productIds[i] == productId) {
				return true;
			}
		}
		return false;
	};
	
	
	$("#amountModalAcceptButton").click(function() {
		if (validateProductAmountForm()) {
			productAmount = parseInt($("#productAmountInput").val());
			isEdit = false;
			$.ajax({
				url: "getProductAmount.do",
				type: "GET",
				async: false,
				data: {
					productId: productId,
					agreementId: $("#agreementInput").val()
				},
				success: function(response) {
					if (response != "" && response >= productAmount) {
						$('#agreementInput').prop('disabled', true).trigger("chosen:updated");
						openModal(null);
						$("#amountModal").modal('hide');
					}else{
						$("#productAmountInput").tooltip("destroy").data("title", "Stock insuficiente").addClass("has-error").tooltip();
						return false;
					}
				},
				error: function(jqXHR, textStatus, errorThrown) {
					myGenericError();
				}
			});
		}
	});
	
	// 	Product autocomplete
	$("#productOutput").autocomplete({
		source: function(request, response) {
			$.ajax({
				url: "getProductFromStock.do",
				type: "GET",
				async: false,
				data: {
					term: request.term,
					agreementId: $("#agreementInput").val()
				},
				success: function(data) {
					var array = $.map(data, function(item) {
						return {
							id:	item.id,
							label: item.code + " - " + item.description + " - " + item.brand.description + " - " + item.monodrug.description,
							value: item.code + " - " + item.description,
							gtin: item.lastGtin,
							type: item.type
						};
					});
					response(array);
				},
				error: function(jqXHR, textStatus, errorThrown) {
					myGenericError();
				}
			});
		},
		select: function(event, ui) {
			if (!productEntered(ui.item.id)) {
				productId = ui.item.id;
				productGtin = ui.item.gtin;
				productDescription = ui.item.value;
				productType = ui.item.type;
				$("#productOutput").val(productDescription);
				$('#amountModal').modal('show');
			} else {
				myShowAlert('danger', 'Producto ya Ingresado');
				$("#productOutput").val("");
			}
			return false;
	    },
		minLength: 3,
		autoFocus: true
	});
	
	
	$('#productOutput').keydown(function(e) {
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
							
							$('#productOutput').data("title", "").removeClass("has-error").tooltip("destroy");
							
							$("#productOutput").val(productDescription);
							$('#amountModal').modal('show');
						} else {
							myShowAlert('danger', 'Producto ya Ingresado');
							$("#productOutput").val("");
						}
					} else {
						$('#productOutput').tooltip("destroy").data("title", "No hay en Stock").addClass("has-error").tooltip();
						$('#productOutput').val('');
						$('#productOutput').focus();
					}
					return false;
				},
				error: function(jqXHR, textStatus, errorThrown) {
					myGenericError();
				}
			});
	    }
	});
	
	$('#conceptInput').on('change', function(evt, params) {
		if($("#conceptInput").val() != ""){
			$.ajax({
				url: "isClientConcept.do",
				type: "GET",
				async: false,
				data: {
					conceptId: $("#conceptInput").val()
				},
				success: function(response) {
					if(response == true){
						$("#providerDiv").hide();
						$("#deliveryLocationDiv").show();
						$('#providerInput').val('').trigger('chosen:updated');
					}else{
						$("#providerDiv").show();
						$("#deliveryLocationDiv").hide();
						$('#deliveryLocationInput').val('').trigger('chosen:updated');
					}
				},
				error: function(jqXHR, textStatus, errorThrown) {
					myGenericError();
				}
			});
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
			serialized.setPreloadedProduct(productDescription);
			serialized.setPreloadedProductId(productId);
			serialized.setPreloadedProductType(productType);
			serialized.setPreloadedAmount(productAmount);
			serialized.setPreloadedData(preloadedData);
			serialized.setProductSelectedGtin(productGtin);
			serialized.setFormatSerializedId(null);
			serialized.setTempSerialNumbers(tempSerialNumberGroup[productId]);
			
			serialized.preloadModalData();
			$('#serializedModal').modal('show');
		}
	};
	
	var populateInputDetails = function(orderDetails, serialNumber, batch, expirationDate, amount, gtin) {
		var orderDetail = {
			"productId": productId,
			"serialNumber": serialNumber,
			"batch": batch,
			"expirationDate": expirationDate,
			"amount": amount,
			"gtin": gtin
		};
		orderDetails.push(orderDetail);
	};
	
	$('#productTableBody').on("click", ".edit-row", function(e) {
		var parent = $(this).parent().parent();

		currentRow = $(".edit-row").index(this);
		productDescription = parent.find(".td-description").html();
		productAmount = parent.find(".td-amount").html();
		productId = parent.find(".span-productId").html();
		productType = parent.find(".span-productType").html();
		productGtin = parent.find(".span-productGtin").html();
		isEdit = true;
		
		openModal(agreementTransferDetailGroup[currentRow]);
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
		agreementTransferDetailGroup.splice(currentRow, 1);
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
		var row = {
			description: productDescription,
			amount: productAmount,
			command: "<span class='span-productId' style='display:none'>" + productId + "</span>"+
			"<span class='span-productType' style='display:none'>" + productType + "</span>"+
			"<span class='span-productGtin' style='display:none'>" + productGtin + "</span>"+
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
			
			var orderDetails = [];
			var tempStockIds = [];
			
			for (var i = 0; i < amounts.length; i++) {
				populateInputDetails(orderDetails, null, batchs[i].innerHTML, expirationDates[i].innerHTML, amounts[i].innerHTML, productGtin);
				tempStockIds[i] = parseInt(stockIds[i].innerHTML);
			}
			if(isEdit){
				agreementTransferDetailGroup[currentRow] = orderDetails;
			}else{
				agreementTransferDetailGroup.push(orderDetails);
				populateProductsDetailsTable();
			}
				
			tempStockIdsGroup[productId] = tempStockIds;
			
			$("#batchExpirationDateModal").modal("hide");
			$(".alert").hide();
			$('#productOutput').focus();
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
			
			var orderDetails = [];
			var tempSerialNumber = [];
			
			for (var i = 0; i < serialNumbers.length; i++) {
				populateInputDetails(orderDetails, serialNumbers[i].innerHTML, batchs[i].innerHTML, expirationDates[i].innerHTML, 1, gtins[i].innerHTML);
				tempSerialNumber[i] = serialNumbers[i].innerHTML;
			}

			if(isEdit){
				agreementTransferDetailGroup[currentRow] = orderDetails;
			}else{
				agreementTransferDetailGroup.push(orderDetails);
				populateProductsDetailsTable();
			}
			tempSerialNumberGroup[productId] = tempSerialNumber;
			
			$("#serializedModal").modal("hide");
			$(".alert").hide();
			$('#productOutput').focus();
		} else {
			myShowAlert('danger', 'No se ha ingresado la totalidad de productos requeridos. Por favor ingrese los restantes.', "serializedModalAlertDiv");
		}
	});
	
	$("#confirmButton").click(function() {
		if (validateForm()) {
			if($("#agreementInput").val() != $("#destinationAgreementInput").val()){
				if (agreementTransferDetailGroup.length > 0) {
					$(this).attr("disabled", true);
					var jsonAgreementTransfer = {
							"originAgreementId": $("#agreementInput").val(),
							"destinationAgreementId": $("#destinationAgreementInput").val(),
							"agreementTransferDetails": []
					};

					for (var i = 0, lengthI = agreementTransferDetailGroup.length; i < lengthI; i++) {
						for (var j = 0; lengthJ = agreementTransferDetailGroup[i].length, j < lengthJ; j++) {
							jsonAgreementTransfer.agreementTransferDetails.push(agreementTransferDetailGroup[i][j]);
						}
					}

					isButtonConfirm = true;

					$.ajax({
						url: "updateProductsAgreement.do",
						type: "POST",
						contentType:"application/json",
						data: JSON.stringify(jsonAgreementTransfer),
						async: false,
						success: function(response) {
							myRedirect("success", "Se ha generado exitosamente la transferencia de convenio", "agreementTransfer.do");
						},
						error: function(jqXHR, textStatus, errorThrown) {
							myGenericError();
						}
					});
				} else {
					myShowAlert('warning', 'Por favor, ingrese al menos un producto.');
				}
			}else{
				myShowAlert('warning', 'El convenio de origen y destino deben ser distintos.');
			}
		}
	});
	
	var hasChanged = function() {
		if (agreementTransferDetailGroup.length > 0) {
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

	// TODO eliminar si con el keydown funciona correctamente.
	/*$("#agreementTransferForm input, #agreementTransferForm select").keypress(function(event) {
		return event.keyCode != 13;
	});*/

	$("#agreementTransferForm input, #agreementTransferForm select").keydown(function(event) {
		if(event.keyCode == 115) { // Presiono F4
			$("#confirmButton").trigger('click');
		} else {
			return event.keyCode != 13;
		}
	});
};
