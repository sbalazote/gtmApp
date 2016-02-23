
SerializedReturns = function() {
	var found = false;
	var inputDetails = [];
	var outputOrderDetails = [];
	var serialNumber = null;
	var inputId = "";
	var currentDate = new Date();
	var jsonInput;

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
		var gtin = null;
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
					gtin = detail.gtin.number;
					populateInputDetails(inputDetails, productId, productType, productSerialNumber, productBatch, productExpirationDate, 1, gtin);

					var aaData = [];
					var row = {
						description: productDescription,
						gtin: gtin,
						serialNumber: productSerialNumber,
						batch: productBatch,
						expirationDate: productExpirationDate
					};
					aaData.push(row);
					$("#productTable").bootgrid("append", aaData);

				}
			});
		});
		if (found) {
			outputOrderDetails[0].splice(indexOf, 1);
			
		} else {
			myShowAlert('danger', 'Serie Inexistente o ya le\u00eddo.');
		}
	};
	
	var populateInputDetails = function(inputDetails, productId, productType, productSerialNumber, productBatch, productExpirationDate, productAmount, gtin) {
		var inputDetail = {
			"productId": productId,
			"productType": productType,
			"serialNumber": productSerialNumber,
			"batch": productBatch,
			"expirationDate": productExpirationDate.substring(0, 2) + productExpirationDate.substring(3, 5) + productExpirationDate.substring(8, 10),
			"amount": productAmount,
			"gtin": gtin
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
					required: true,
					digits: true,
					minlength: 4,
					maxlength: 4
				},
				deliveryNoteNumber: {
					required: true,
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
						serial: $('#serialNumberInput').val(),
						formatSerializedId: null
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
												$("#productInput").val("");
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
													$("#serialNumberInput").val("");
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
						serial: $('#serialNumberInput').val(),
						formatSerializedId: null
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
									$("#serialNumberInput").val("");
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

	$("input").blur(function() {
		if ($("#deliveryNotePOSInput").val() != "")
			$("#deliveryNotePOSInput").val(addLeadingZeros($("#deliveryNotePOSInput").val(), 4));
		if ($("#deliveryNoteNumberInput").val() != "")
			$("#deliveryNoteNumberInput").val(addLeadingZeros($("#deliveryNoteNumberInput").val(), 8));
	});


	//Se esconde el div de proveedor
	$("#providerDiv").hide();
	$("#conceptInput").val(27).trigger("chosen:updated");
	$("#providerInput").attr('disabled', true).trigger("chosen:updated");
	$("#deliveryLocationInput").attr('disabled', true).trigger("chosen:updated");
	$("#agreementInput").attr('disabled', true).trigger("chosen:updated");
	
	$("#confirmButton").click(function() {
		if (validateForm()) {
			if (inputDetails.length > 0) {
				isButtonConfirm = true;
				
				jsonInput = {
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
					url: "saveRefundInput.do",
					type: "POST",
					contentType:"application/json",
					data: JSON.stringify(jsonInput),
					async: true,
					beforeSend : function() {
						$.blockUI({ message: 'Espere un Momento por favor...' });
					},
					success: function(response, textStatus, jqXHR) {
						$.unblockUI();
						inputId = response.operationId;
						if(response.resultado == true){
							if (textStatus === 'success') {
								$('#destructionModal').modal({backdrop: 'static', keyboard: false})
							}
						}else{
							var errors = "";
							for (var i = 0, lengthI = response.myOwnErrors.length; i < lengthI; i++) {
								errors += response.myOwnErrors[i] + "<br />";
							}

							for (var i = 0, lengthI = response.mySelfSerializedOwnErrors.length; i < lengthI; i++) {
								errors += response.mySelfSerializedOwnErrors[i] + "<br />";
							}

							if(response.errores != null){
								errors += "<strong>Errores informados por ANMAT:</strong><br />";
								for (var i = 0, lengthI = response.errores.length; i < lengthI; i++) {
									errors += response.errores[i]._c_error + " - " + response.errores[i]._d_error + "<br />";
								}
							}

							mySerializedRefundAlert("danger", errors,null,0);
						}
					},
					error: function(response, jqXHR, textStatus, errorThrown) {
						$.unblockUI();
						myGenericError();
					}
				});
				
			} else {
				myShowAlert('warning', 'Por favor, lea al menos un producto.');
			}
		}
	});

	$("#cancelDestruction").click(function() {
		myReload("success", "Se ha registrado la devoluci\u00f3n de serie con n\u00famero: " + inputId);
	});

	$("#destructionAccept").click(function() {

		var jsonOutput = {
			"conceptId": $("#destructionConceptInput").val(),
			"providerId": $("#providerInput").val(),
			"deliveryLocationId": $("#deliveryLocationInput").val(),
			"agreementId": $("#agreementInput").val(),
			"date": $("#currentDateInput").val(),
			"outputDetails": []
		};

		for (var i = 0; i < inputDetails.length; i++) {
			inputDetails[i].expirationDate = inputDetails[i].expirationDate.substring(0, 2) + "/" + inputDetails[i].expirationDate.substring(2, 4) +  "/" + inputDetails[i].expirationDate.substring(4, 6);
			jsonOutput.outputDetails.push(inputDetails[i]);
		}

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
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myGenericError();
			},
			complete: function(jqXHR, textStatus) {
				$.unblockUI();
				if (textStatus === 'success') {
					generateOutputPDFReport(jqXHR.responseJSON);
				}
			}
		});
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

	$("#destructionConceptInput").chosen({
		width: '100%'
	});


	mySerializedRefundAlert = function(type, message, element, time) {
		var myDiv = "alertSerializedReturnModalDiv";
		if (element) {
			myDiv = element;
		}

		toastr.options = {
			"closeButton": false,
			"debug": false,
			"newestOnTop": false,
			"progressBar": true,
			"preventDuplicates": false,
			"onclick": null,
			"showDuration": "300",
			"hideDuration": "1000",
			"timeOut": 0,
			"extendedTimeOut": time,
			"showEasing": "swing",
			"hideEasing": "linear",
			"showMethod": "fadeIn",
			"hideMethod": "fadeOut",
			"tapToDismiss" : false,
			"target": "#"+myDiv
		};
		// Muestro mensaje dependiendo el tipo.
		switch (type) {
			case 'danger':
				toastr.error(message);
				break;
			case 'success':
				toastr.success(message);
				break;
			case 'info':
				toastr.info(message);
				break;
			case 'warning':
				toastr.warning(message);
				break;
		}
		$("#serializedReturnErrorModal").modal();
	};


	$("#forcedInput").click(function() {
		//var popup = window.open();
		jsonInput["id"] = inputId;
		if (validateForm()) {
			isButtonConfirm = true;
			$.ajax({
				url: "authorizeInputWithoutInform.do",
				type: "POST",
				contentType:"application/json",
				data: JSON.stringify(jsonInput),
				async: true,
				beforeSend : function() {
					$.blockUI({ message: 'Espere un Momento por favor...' });
				},
				success: function(response, textStatus, jqXHR) {
					$.unblockUI();
					$('#destructionModal').modal({backdrop: 'static', keyboard: false})
				},
				error: function(response, jqXHR, textStatus, errorThrown) {
					$.unblockUI();
					myGenericError();
				}
			});
		}
	});

	$("#cancel").click(function() {
		myRedirect("success", "Se ha registrado el Ingreso de mercader\u00eda n\u00famero: " + inputId, "serializedReturns.do" );
	});
};