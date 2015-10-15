
SerializedReturns = function() {
	var found = false;
	var inputDetails = [];
	var outputOrderDetails = [];
	var serialNumber = null;
	
	var currentDate = new Date();

    $("#deliveryNotePOSInput").numeric();

    $("#deliveryNoteNumberInput").numeric();
	
	var buildReturn = function(outputId, orderId) {
		if (outputId != null) {
			
			$.ajax({
				url: "getOutput.do",
				type: "GET",
				data: {
					outputId: outputId
				},
				success: function(response) {
					if(response.provider == null){
						$("#providerDiv").hide();
						$('#providerInput').val('').trigger('chosen:updated');
						$("#deliveryLocationDiv").show();
						$("#deliveryLocationInput").val(response.deliveryLocation.id).trigger("chosen:updated");
					}else{
						$("#providerDiv").show();
						$("#providerInput").val(response.provider.id).trigger("chosen:updated");
						$("#deliveryLocationDiv").hide();
						$('#deliveryLocationInput').val('').trigger('chosen:updated');
					}
					$("#agreementInput").val(response.agreement.id).trigger("chosen:updated");
					outputOrderDetails.push(response.outputDetails);
					populateSerializedReturnsDetailsTable();
				},
				error: function(jqXHR, textStatus, errorThrown) {
					myGenericError();
				}
			});
		}
		else if (orderId != null) {

			$("#providerDiv").hide();
			$("#deliveryLocationDiv").show();
			$('#providerInput').val('').trigger('chosen:updated');
			
			$.ajax({
				url: "getOrder.do",
				type: "GET",
				data: {
					orderId: orderId
				},
				success: function(response) {
					//$("#providerInput").val(response.provisioningRequest.provider.id).trigger("chosen:updated");
					$("#deliveryLocationInput").val(response.provisioningRequest.deliveryLocation.id).trigger("chosen:updated");
					$("#agreementInput").val(response.provisioningRequest.agreement.id).trigger("chosen:updated");
					outputOrderDetails.push(response.orderDetails);
					populateSerializedReturnsDetailsTable();
				},
				error: function(jqXHR, textStatus, errorThrown) {
					myGenericError();
				}
			});
		}

	};
	
	var populateSerializedReturnsDetailsTable = function() {
		var found = false;
		var indexOf = -1;
		var productId = null;
		var productDescription = null;
		var productSerialNumber = null;
		var productBatch = null;
		var productExpirationDate = null;
		$.each(outputOrderDetails, function( index, value ) {
			$.each(value, function( index, detail ) {
				if (detail.serialNumber == serialNumber) {
					found = true;
					indexOf = index;
					productId = detail.product.id;
					productType = detail.product.type;
					productDescription = detail.product.description;
					productSerialNumber = detail.serialNumber;
					productBatch = detail.batch;
					productExpirationDate = myParseDate(detail.expirationDate);
					populateInputDetails(inputDetails, productId, productType, productSerialNumber, productBatch, productExpirationDate, 1);

					var aaData = [];
					var row = {
						description: productDescription,
						serialNumber: productSerialNumber,
						batch: productBatch,
						expirationDate: productExpirationDate
					};
					aaData.push(row);
					$("#productTable").bootgrid("append", aaData);
					return;
				}
			});
		});
		if (found) {
			outputOrderDetails[0].splice(indexOf, 1);
			
		} else {
			myShowAlert('danger', 'Serie Inexistente o ya le\u00eddo.');
		}
	};
	
	var populateInputDetails = function(inputDetails, productId, productType, productSerialNumber, productBatch, productExpirationDate, productAmount) {
		var inputDetail = {
			"productId": productId,
			"productType": productType,
			"serialNumber": productSerialNumber,
			"batch": productBatch,
			"expirationDate": productExpirationDate.substring(0, 2) + productExpirationDate.substring(3, 5) + productExpirationDate.substring(8, 10),
			"amount": productAmount
		};
		inputDetails.push(inputDetail);
	};
	
	var validateForm = function() {
		var form = $("#serializedReturnsForm");
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
				deliveryNotePOS: {
					required: false,
					digits: true,
					minlength: 4,
					maxlength: 4
				},
				deliveryNoteNumber: {
					required: false,
					digits: true,
					minlength: 8,
					maxlength: 8
				}
			},
			showErrors: myShowErrors,
			onsubmit: false
		});
		return form.valid();
	};
	
	$("#currentDateInput").datepicker().datepicker("setDate", currentDate);
	
	$('#currentDateButton').click(function() {
		$("#currentDateInput").datepicker().focus();
	});
	
	$('#serialNumberInput').keydown(function(e) {
		if(e.keyCode == 13){ // Presiono Enter

			if (!found) {

				// Busco si se trata de un trazado de origen.
				$.ajax({
					url: "parseSerial.do",
					type: "GET",
					async: false,
					data: {
						serial: $('#serialNumberInput').val()
					},
					success: function(response) {
						// No es un trazado de origen; busco a ver si se trata de un trazado propio.
						if (response == "") {

							$.ajax({
								url: "isParseSelfSerial.do",
								type: "GET",
								async: false,
								data: {
									serial: $('#serialNumberInput').val()
								},
								success: function(response) {
									// No es un trazado propio, por lo tanto no es un serie v�lido.
									if (!response) {
										$('#serialNumberInput').val("");
										myShowAlert('danger', 'Formato de Serie Inv\u00e1lido.');
										return;
									}
									// Es un trazado propio.
									serialNumber = $('#serialNumberInput').val().substring(3, 16) + $('#serialNumberInput').val().substring(18);
									$.ajax({
										url: "searchSerialRecords.do",
										type: "GET",
										data: {
											serialNumber: serialNumber
										},
										success: function(response) {
											if (response.outputId == null && response.orderId == null) {
												myShowAlert('danger', 'Serie Inexistente.');
											}
											else {
												found = true;
												buildReturn(response.outputId, response.orderId);	
											}
										},
										error: function(jqXHR, textStatus, errorThrown) {
											myGenericError();
										}
									});
								},
								error: function(jqXHR, textStatus, errorThrown) {
									myGenericError();
								}
							});
						} else {
							// Es un trazado de origen.
							serialNumber = response.serialNumber;

							$.ajax({
								url: "getProductBySerialOrGtin.do",
								type: "GET",
								async: false,
								data: {
									serial: $('#serialNumberInput').val()
								},
								success: function(response) {
									if (response != "") {
										var productId = response.id;
										$.ajax({
											url: "searchSerialRecords.do",
											type: "GET",
											data: {
												productId: productId,
												serialNumber: serialNumber
											},
											success: function(response) {
												if (response.outputId == null && response.orderId == null) {
													myShowAlert('danger', 'Serie Inexistente.');
												}
												else {
													found = true;
													buildReturn(response.outputId, response.orderId);	
												}
											},
											error: function(jqXHR, textStatus, errorThrown) {
												myGenericError();
											}
										});
									} else {
										$('#serialNumberInput').tooltip("destroy").data("title", "Producto Inexistente o Inactivo").addClass("has-error").tooltip();
										$('#serialNumberInput').val('');
										$('#serialNumberInput').focus();
									}
									return false;
								}
							});
						}
					},
					error: function(jqXHR, textStatus, errorThrown) {
						myGenericError();
					}
				});
			} else {
				
				// Busco si se trata de un trazado de origen.
				$.ajax({
					url: "parseSerial.do",
					type: "GET",
					async: false,
					data: {
						serial: $('#serialNumberInput').val()
					},
					success: function(response) {
						// No es un trazado de origen; busco a ver si se trata de un trazado propio.
						if (response == "") {

							$.ajax({
								url: "isParseSelfSerial.do",
								type: "GET",
								async: false,
								data: {
									serial: $('#serialNumberInput').val()
								},
								success: function(response) {
									// No es un trazado propio, por lo tanto no es un serie v�lido.
									if (!response) {
										$('#serialNumberInput').val("");
										myShowAlert('danger', 'Formato de Serie Inv\u00e1lido.');
										return;
									}
									// Es un trazado propio.
									serialNumber = $('#serialNumberInput').val().substring(3, 16) + $('#serialNumberInput').val().substring(18);
									populateSerializedReturnsDetailsTable();
								},
								error: function(jqXHR, textStatus, errorThrown) {
									myGenericError();
								}
							});
						} else {
							// Es un trazado de origen.
							serialNumber = response.serialNumber;
							populateSerializedReturnsDetailsTable();
						}
					},
					error: function(jqXHR, textStatus, errorThrown) {
						myGenericError();
					}
				});
			}
		}
	});
	
	//Se esconde el div de cliente
	$("#deliveryLocationDiv").hide();
	$("#conceptInput").val(27).trigger("chosen:updated");
	$("#providerInput").attr('disabled', true).trigger("chosen:updated");
	$("#deliveryLocationInput").attr('disabled', true).trigger("chosen:updated");
	$("#agreementInput").attr('disabled', true).trigger("chosen:updated");
	
	$("#confirmButton").click(function() {
		if (validateForm()) {
			if (inputDetails.length > 0) {
				isButtonConfirm = true;
				
				var jsonInput = {
					//"id": $("#inputId").val(),
					"conceptId": $("#conceptInput").val(),
					"providerId": $("#providerInput").val(),
					"deliveryLocationId": $("#deliveryLocationInput").val(),
					"agreementId": $("#agreementInput").val(),
					"deliveryNoteNumber": $("#deliveryNotePOSInput").val() + $("#deliveryNoteNumberInput").val(),
					"date": $("#currentDateInput").val(),
					"inputDetails": []
				};
				
				for (var i = 0; i < inputDetails.length; i++) {
					jsonInput.inputDetails.push(inputDetails[i]);
				}
				
				$.ajax({
					url: "saveInput.do?isSerializedReturn=true",
					type: "POST",
					contentType:"application/json",
					data: JSON.stringify(jsonInput),
					async: false,
					success: function(response, textStatus, jqXHR) {
						myReload("success", "Se ha registrado la devoluci\u00f3n de serie con n\u00famero: " + response.id);
					},
					error: function(response, jqXHR, textStatus, errorThrown) {
						myGenericError();
					}
				});
				
			} else {
				myShowAlert('warning', 'Por favor, lea al menos un producto.');
			}
		}
	});
	
	var hasChanged = function() {
		if (inputDetails.length > 0) {
			return true;
		} else {
			return false;
		}
	};

	$(window).bind("beforeunload", function(event) {
		if (hasChanged() && isButtonConfirm == false) {
			return "Existen cambios que no fueron confirmados.";
		} else {
			isButtonConfirm = false;
		}
	});

	// TODO eliminar si con el keydown funciona correctamente.
	/*$("#serializedReturnsForm input, #serializedReturnsForm select").keypress(function(event) {
		return event.keyCode != 13;
	});*/

	$("#serializedReturnsForm input, #serializedReturnsForm select").keydown(function(event) {
		if(event.keyCode == 115) { // Presiono F4
			$("#confirmButton").trigger('click');
		} else {
			return event.keyCode != 13;
		}
	});
};