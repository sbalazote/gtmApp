Output = function() {
	var currentDate = new Date();
	
	var batchExpirationDate = new OutputBatchExpirationDate();
	var serialized = new OutputSerialized();
	
	// Los orderDetails agrupados por fila
	var outputDetailGroup = [];
	
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
	
	//Se esconde el div de Lugares de Entrega.
	$("#deliveryLocationDiv").hide();
	$("#productOutput").attr("disabled", true);
	
    $("#amountInput").numeric();
	
	var validateProductAmountForm = function() {
		var form = $("#productAmountModalForm");
		amountFormValidator = form.validate({
			rules: {
				productAmount: {
					required: true,
					digits: true,
					minValue: 0,
				}
			},
			showErrors: myShowErrors,
			onsubmit: false
		});
		return form.valid();
	};
	
	var validateForm = function() {
		var form = $("#outputForm");
		form.validate({
			rules: {
				currentDate: {
					required: true
				},
				concept: {
					required: true
				},
				agreement: {
					required: true
				},
				provider:{
			        required: function(element) {
			            return $("#deliveryLocationInput").val() == "";
			        }
				},
				deliveryLocation: {
					required: function(element) {
			            return $("#providerInput").val() == "";
			        }
				},
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
	
	$("#currentDateInput").datepicker().datepicker("setDate", currentDate);
	
	$('#currentDateButton').click(function() {
		$("#currentDateInput").datepicker().focus();
	});
	
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
	
	
	var currentRowElement = null;
	
	$('#productTableBody').on("click", ".delete-row", function(){
		currentRowElement = this;
		$('#deleteRowConfirmationModal').modal();
	});
	
	$("#inputDeleteRowConfirmationButton").click(function() {
		var parent = $(currentRowElement).parent().parent();
		currentRow = $(".delete-row").index(currentRowElement);
		outputDetailGroup.splice(currentRow, 1);
		productIds.splice(currentRow, 1);
		
		productId = parent.find(".span-productId").html();
		parent.remove();
		$(".alert").hide();
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
					agreementId: $("#agreementInput").val(),
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
				url: "getProductoFromStock.do",
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
	    if(e.keyCode == 121){ // F10
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
                            if(serial.length == 13) {
                                productGtin = serial;
                            }else{
                                productGtin = response.lastGtin;
                            }
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
						$('#productOutput').tooltip("destroy").data("title", "Producto Inexistente").addClass("has-error").tooltip();
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
					conceptId: $("#conceptInput").val(),
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
	
	$('#productTableBody').on("click", ".edit-button", function() {
		var parent = $(this).parent().parent();
	
		currentRow = $(".edit-button").index(this);
		productDescription = parent.find(".td-description").html();
		productAmount = parent.find(".td-amount").html();
		productId = parent.find(".span-productId").html();
		productType = parent.find(".span-productType").html();
		productGtin = parent.find(".span-productGtin").html();
		isEdit = true;
		
		openModal(outputDetailGroup[currentRow]);
	});
	
	var populateProductsDetailsTable = function() {
		var tableRow = "<tr><td class='td-description'>" + productDescription + "</td>" +
		"<td class='td-amount'>" + productAmount + "</td>" +
		"<td>" +
		"<span class='span-productId' style='display:none'>" + productId + "</c:out></span>" +
		"<span class='span-productType' style='display:none'>" + productType + "</c:out></span>" + 
		"<span class='span-productGtin' style='display:none'>" + productGtin + "</c:out></span>" +
		"<a href='javascript:void(0);' class='edit-button'>Editar</a>  " +
		"<a href='javascript:void(0);' class='delete-row'>Eliminar</a></td></tr>";
		$("#productTableBody").append(tableRow);
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
				outputDetailGroup[currentRow] = orderDetails;
			}else{
				outputDetailGroup.push(orderDetails);
				populateProductsDetailsTable();
			}
				
			tempStockIdsGroup[productId] = tempStockIds;
			
			$("#batchExpirationDateModal").modal("hide");
			$(".alert").hide();
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
				outputDetailGroup[currentRow] = orderDetails;
			}else{
				outputDetailGroup.push(orderDetails);
				populateProductsDetailsTable();
			}
			tempSerialNumberGroup[productId] = tempSerialNumber;
			
			$("#serializedModal").modal("hide");
			$(".alert").hide();
		} else {
			myShowAlert('danger', 'No se ha ingresado la totalidad de productos requeridos. Por favor ingrese los restantes.', "serializedModalAlertDiv");
		}
	});
	
	$("#confirmButton").click(function() {
		if (validateForm()) {
			if (outputDetailGroup.length > 0) {
				$(this).attr("disabled", true);
				var jsonOutput = {
						"conceptId": $("#conceptInput").val(),
						"providerId": $("#providerInput").val(),
						"deliveryLocationId": $("#deliveryLocationInput").val(),
						"agreementId": $("#agreementInput").val(),
						"date": $("#currentDateInput").val(),
						"outputDetails": []
				};
		
				for (var i = 0, lengthI = outputDetailGroup.length; i < lengthI; i++) {
					for (var j = 0; lengthJ = outputDetailGroup[i].length, j < lengthJ; j++) {
						jsonOutput.outputDetails.push(outputDetailGroup[i][j]);
					}
				}
		
				isButtonConfirm = true;
		
				$.ajax({
					url: "saveOutput.do",
					type: "POST",
					contentType:"application/json",
					data: JSON.stringify(jsonOutput),
					async: false,
					beforeSend : function() {
		                $.blockUI({ message: 'Espere un Momento por favor...' });
		             },
					success: function(response) {
						if (!response.concept.printDeliveryNote) {
							var doc = printIOPDF('output', response.id, response.outputDetails);
							var string = doc.output('datauristring');
							var x = window.open('','_blank', '', false);
							x.document.open();
							x.document.location=string;
						}
						
						myRedirect("success", "Se ha generado exitosamente el Egreso de Mercaderia: " + response.id, "output.do");
					},
					error: function(jqXHR, textStatus, errorThrown) {
						$.unblockUI();
						myGenericError();
					}
				});
			} else {
				myShowAlert('danger', 'Por favor, ingrese al menos un producto.');
			}
		}
	});
};
