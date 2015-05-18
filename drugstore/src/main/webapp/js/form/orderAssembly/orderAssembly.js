
OrderAssembly = function() {

	var batchExpirationDate = new OrderAssemblyBatchExpirationDate();
	var serialized = new OrderAssemblySerialized();
	
	// Los orderDetails agrupados por fila
	var orderDetailGroup = [];
	
	// Mapa con los series que se cargaron por cada producto 
	var tempSerialNumberGroup = {};
	
	// Mapa con los stockIds que se cargaron por cada producto 
	var tempStockIdsGroup = {};

	// La fila donde se hace click (en el editar o borrar)
	var currentRow = 0;
	
	// Todos estos valores se cargan en los modals, no olvidarse de precargarlos en el assign-button
	var productDescription = "";
	var productAmount = "";
	var productId = "";
	var productType = "";
	var productGtin = "";
	
	var isButtonConfirm = false;
	
    $("#amountInput").numeric();
	
	$('#productInput').keydown(function(e) {
	    if(e.keyCode == 121){ // F10
	    	$.ajax({
				url: "getProductBySerialOrGtin.do",
				type: "GET",
				data: {
					serial: $(this).val()
				},
				success: function(response) {
					if (response != "") {
						$('#productInput').data("title", "").removeClass("has-error").tooltip("destroy");
						currentRow = findProduct(response.id);
						if (currentRow != null) {
							$(".assign-button").eq(currentRow).trigger("click");
						} else {
							$('#productInput').tooltip("destroy").data("title", "Producto no coincide con los precargados").addClass("has-error").tooltip();
							$('#productInput').focus();
						}						
					} else {
						$('#productInput').tooltip("destroy").data("title", "Producto Inexistente").addClass("has-error").tooltip();
						$('#productInput').focus();
					}
					return false;
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
	
	$('#productTableBody').on("click", ".assign-button", function() {
		var parent = $(this).parent().parent();
	
		currentRow = $(".assign-button").index(this);
		productDescription = parent.find(".td-description").html();
		productAmount = parent.find(".td-amount").html();
		productId = parent.find(".span-productId").html();
		productType = parent.find(".span-productType").html();
		productGtin = parent.find(".span-productGtin").html();
		
		openModal(orderDetailGroup[currentRow]);
	});
	
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
			
			orderDetailGroup[currentRow] = orderDetails;
			tempStockIdsGroup[productId] = tempStockIds;
			
			$("#batchExpirationDateModal").modal("hide");
			
			$(".assign-button")[currentRow].innerHTML = "Editar";
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

			orderDetailGroup[currentRow] = orderDetails;
			tempSerialNumberGroup[productId] = tempSerialNumber;
			
			$("#serializedModal").modal("hide");
			
			$(".assign-button")[currentRow].innerHTML = "Editar";
		} else {
			myShowAlert('danger', 'No se ha ingresado la totalidad de productos requeridos. Por favor ingrese los restantes.', "serializedModalAlertDiv");
		}
	});
	
	$("#confirmButton").click(function() {
		if (!validateRemainingProducts()) {
			myShowAlert('danger', 'No se ha asignado la totalidad de productos al pedido.');
		} else {

			$(this).attr("disabled", true);

			var jsonOrder = {
					"provisioningRequestId": $("#provisioningRequestId").val(),
					"deliveryNoteNumber": $("#deliveryNoteNumberInput").val(),
					"cancelled": false,
					"orderDetails": []
			};

			for (var i = 0, lengthI = orderDetailGroup.length; i < lengthI; i++) {
				for (var j = 0; lengthJ = orderDetailGroup[i].length, j < lengthJ; j++) {
					jsonOrder.orderDetails.push(orderDetailGroup[i][j]);
				}
			}

			isButtonConfirm = true;

			$.ajax({
				url: "saveOrder.do",
				type: "POST",
				contentType:"application/json",
				data: JSON.stringify(jsonOrder),
				async: false,
				success: function(response) {
					myRedirect("success", "Se ha generado exitosamente el pedido para la Solicitud n&uacute;mero: " + response.id, "orderAssemblySelection.do");
				},
				error: function(jqXHR, textStatus, errorThrown) {
					myGenericError();
				}
			});
		}
	});
	
	var validateRemainingProducts = function() {
		var buttons = $(".assign-button");
		for (var i = 0; i < buttons.length; i++) {
			if (buttons[i].innerHTML == "Asignar") {
				return false;
			}
		}
		return true;
	};
	
	var findProduct = function(productId) {
		var productIds = $(".span-productId");
		for (var i = 0, l = productIds.length; i < l; ++i) {
			if (productIds[i].innerHTML == productId) {
				return i;
			}
		}
		return null;
	};
	
	var hasChanged = function() {
		return (orderDetailGroup.length > 0);
	};
	
	$(window).bind("beforeunload", function(event) {
	    if (hasChanged() && isButtonConfirm == false) {
	    	return "Existen cambios que no fueron confirmados.";
	    } else {
	    	isButtonConfirm = false;
	    }
	});
	
	$("#orderAssemblyForm input, #orderAssemblyForm select").keypress(function(event) {
		return event.keyCode != 13;
	});
	
};