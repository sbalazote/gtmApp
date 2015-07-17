
Input = function() {

	var batchExpirationDate = new BatchExpirationDate();
	var providerSerialized = new ProviderSerialized();
	var selfSerialized = new SelfSerialized();
	
	var amountFormValidator = null;
	
	// Los inputDetails agrupados por fila
	var inputDetailGroup = [];
	
	// Para validar que no ingrese 2 veces el mismo producto 
	var productIds = [];
	
	// Mapa con los series que se cargaron por cada producto 
	var tempSerialNumberGroup = {};
	
	// La fila donde se hace click (en el editar o borrar)
	var currentRow = 0;
	
	// Todos estos valores se cargan en los modals, no olvidarse de precargarlos en el edit-row
	var productDescription = "";
	var productAmount = "";
	var productId = "";
	var productType = "";
	var productGtin = "";
	
	var currentDate = new Date();
	
	var isButtonConfirm = false;
	
	var isUpdate = false;
	
	var cleanAmountModal = function() {
		myResetForm($("#productAmountModalForm")[0], amountFormValidator);
	};
	
	var cleanProductInput = function() {
		$('#productInput').val("");
	};
	
	if($("#inputId").val() != ""){
		isUpdate = true;
	}

    $("#deliveryNotePOSInput").numeric();

    $("#deliveryNoteNumberInput").numeric();
    
    $("#expirationDateInput").numeric();
    $("#providerSerializedExpirationDateInput").numeric();
    $("#selfSerializedExpirationDateInput").numeric();

    $("#selfSerializedAmountInput").numeric();
    
    $("#amountInput").numeric();

    if(isUpdate){
		$('#productInput').prop('disabled', true);
		$('#agreementInput').prop('disabled', true).trigger("chosen:updated");
		$("#divInputId").show();
		currentDate = $("#currentDateInput").val();
		if($("#providerInput").val() != ""){
			$("#deliveryLocationDiv").hide();
		}else{
			$("#providerDiv").hide();
		}
	}else{
		//Se esconde el div de cliente
		$("#deliveryLocationDiv").hide();
	}
	
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
		var form = $("#inputForm");
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
				},
				purchaseOrderNumber: {
					nowhitespace: true,
					alphanumeric: true,
					maxlength: 30
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
	
	$('select').chosen().filter('[autofocus]').trigger('chosen:activate');
	
	// Product autocomplete
	$("#productInput").autocomplete({
		source: function(request, response) {
			$.ajax({
				url: "getProducts.do",
				type: "GET",
				async: false,
				data: {
					term: request.term,
					active: true
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
				$("#productInput").val(productDescription);
				$('#amountModal').modal('show');
			} else {
				myShowAlert('danger', 'Producto ya Ingresado');
				$("#productInput").val("");
			}
			return false;
	    },
		minLength: 3,
		autoFocus: true
	});
	
	$('#productInput').keydown(function(e) {
	    if(e.keyCode == 13){ // Presiono Enter
            var serial = $(this).val();
            $.ajax({
                url: "getProductBySerialOrGtin.do",
                type: "GET",
                data: {
                    serial: serial
                },
                success: function(response) {
                    if (response != "") {
                        if (!productEntered(response.id)) {
                            productId = response.id;
                            //Esto hay q ver pero es para que si leyo por gtin q se le asocie ese
                            // gtin a los productos a ingresar
                            if(serial.length == 13) {
                                productGtin = serial;
                            }else{
                                productGtin = response.lastGtin;
                            }
                            productDescription = response.code + ' - ' + response.description;
                            productType = response.type;

                            $('#productInput').data("title", "").removeClass("has-error").tooltip("destroy");

                            $("#productInput").val(productDescription);
                            $('#amountModal').modal('show');
                        } else {
                            myShowAlert('danger', 'Producto ya Ingresado');
                            $("#productInput").val("");
                        }
                    } else
                    {
                        $('#productInput').tooltip("destroy").data("title", "Producto Inexistente o Inactivo").addClass("has-error").tooltip();
                        $('#productInput').val('');
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

	$('#amountModal').on('shown.bs.modal', function () {
	    $('#productAmountInput').focus();
	});
	
	$("#othersSelfSerielizedAcceptButton").click(function() {
		$("#productOthersSelfSerielized").modal('hide');
		$('#amountModal').modal('show');
	});
	
	$('#amountModal').on('hidden.bs.modal', function () {
	    cleanAmountModal();
	    cleanProductInput();
	    $('#productInput').focus();
	});
	
	$('#amountModal').keypress(function(e) {
        if (e.keyCode === 13) {
        	$("#amountModalAcceptButton").trigger('click');
        	return false;
        }
        if (e.keyCode === 27) {
        	$('#productInput').focus();
        	return false;
        }
    });
	
	var openModal = function(preloadedData) {
		if (productType == "BE") {
			batchExpirationDate.setPreloadedProduct(productDescription);
			batchExpirationDate.setPreloadedAmount(productAmount);
			batchExpirationDate.setPreloadedData(preloadedData);
			batchExpirationDate.preloadModalData();
			$('#batchExpirationDateModal').modal('show');
			
		} else if (productType == "PS") {
			providerSerialized.setTempSerialNumbers(tempSerialNumberGroup[productId]);
			providerSerialized.setProductSelectedGtin(productGtin);
			providerSerialized.setPreloadedProduct(productDescription);
			providerSerialized.setPreloadedProductId(productId);
			providerSerialized.setPreloadedAmount(productAmount);
			providerSerialized.setPreloadedData(preloadedData);
			providerSerialized.preloadModalData();
			$('#providerSerializedModal').modal('show');
			
		} else {
			selfSerialized.setPreloadedProduct(productDescription);
			selfSerialized.setPreloadedAmount(productAmount);
			selfSerialized.setPreloadedData(preloadedData);
			selfSerialized.preloadModalData();
			$('#selfSerializedModal').modal('show');
		}
	};
		
	$("#amountModalAcceptButton").click(function() {
		if (validateProductAmountForm()) {
			productAmount = parseInt($("#productAmountInput").val());
			$("#amountModal").modal('hide');
			openModal(null);
		}
	});
	
	var populateInputDetailsTable = function() {
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
	
	var populateInputDetails = function(inputDetails, serialNumber, batch, expirationDate, amount, gtin) {
		var inputDetail = {
			"productId": productId,
			"productType": productType,
			"serialNumber": serialNumber,
			"batch": batch,
			"expirationDate": expirationDate,
			"amount": amount,
			"gtin": gtin
		};
		inputDetails.push(inputDetail);
	};

	$('#productTableBody').on("click", ".edit-row", function(e) {
		var parent = $(this).parent().parent();

		currentRow = $(".edit-row").index(this);
		productDescription = parent.find(".td-description").html();
		productAmount = parent.find(".td-amount").html();
		productId = parent.find(".span-productId").html();
		productType = parent.find(".span-productType").html();
		productGtin = parent.find(".span-productGtin").html();
		
		openModal(inputDetailGroup[currentRow]);
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
		inputDetailGroup.splice(currentRow, 1);
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
	
	$("#batchExpirationDateAcceptButton").click(function() {
		remainingAmount = $('#batchExpirationDateRemainingAmountLabel').text();
		if (remainingAmount == 0) {
			
			var amounts = $("#batchExpirationDateTable td.amount");
			var batchs = $("#batchExpirationDateTable td.batch");
			var expirationDates = $("#batchExpirationDateTable td.expirationDate");
			
			var inputDetails = [];
			
			for (var i = 0; i < amounts.length; i++) {
				populateInputDetails(inputDetails, null, batchs[i].innerHTML, expirationDates[i].innerHTML, amounts[i].innerHTML, productGtin);
			}
			
			if (batchExpirationDate.getPreloadedData() == null) {
				inputDetailGroup.push(inputDetails);
				populateInputDetailsTable();
				productIds.push(productId);
			} else {
				inputDetailGroup[currentRow] = inputDetails;
				batchExpirationDate.setPreloadedData(null);
			}
			
			$('#batchExpirationDateModal').modal('hide');
			$(".alert").hide();
			$('#productInput').focus();
		} else {
			myShowAlert('danger', 'No se ha ingresado la totalidad de productos requeridos. Por favor ingrese los restantes.', "batchExpirationDateModalAlertDiv");
		}
	});

	$("#providerSerializedAcceptButton").click(function() {
		remainingAmount = $('#providerSerializedRemainingAmountLabel').text();
		if (remainingAmount == 0) {

			var gtins = $("#providerSerializedTable td.gtin");
			var serialNumbers = $("#providerSerializedTable td.serialNumber");
			var batchs = $("#providerSerializedTable td.batch");
			var expirationDates = $("#providerSerializedTable td.expirationDate");

			var inputDetails = [];
			var tempSerialNumber = [];

			for (var i = 0; i < serialNumbers.length; i++) {
				populateInputDetails(inputDetails, serialNumbers[i].innerHTML, batchs[i].innerHTML, expirationDates[i].innerHTML, 1, gtins[i].innerHTML);
				tempSerialNumber[i] = serialNumbers[i].innerHTML;
			}

			tempSerialNumberGroup[productId] = tempSerialNumber;

			if (providerSerialized.getPreloadedData() == null) {
				inputDetailGroup.push(inputDetails);
				populateInputDetailsTable();
				productIds.push(productId);
			} else {
				inputDetailGroup[currentRow] = inputDetails;
				providerSerialized.setPreloadedData(null);
			}

			$('#providerSerializedModal').modal('hide');
			$(".alert").hide();
			$('#productInput').focus();
		} else {
			myShowAlert('danger', 'No se ha ingresado la totalidad de productos requeridos. Por favor ingrese los restantes.', "providerSerializedModalAlertDiv");
		}
	});

	$("#selfSerializedAcceptButton").click(function() {
		remainingAmount = $('#selfSerializedRemainingAmountLabel').text();
		if (remainingAmount == 0) {
			
			var amounts = $("#selfSerializedTable td.amount");
			var batchs = $("#selfSerializedTable td.batch");
			var expirationDates = $("#selfSerializedTable td.expirationDate");
			
			var inputDetails = [];
			
			for (var i = 0; i < amounts.length; i++) {
				populateInputDetails(inputDetails, null, batchs[i].innerHTML, expirationDates[i].innerHTML, amounts[i].innerHTML, productGtin);
			}
			
			if (selfSerialized.getPreloadedData() == null) {
				inputDetailGroup.push(inputDetails);
				populateInputDetailsTable();
				productIds.push(productId);
			} else {
				inputDetailGroup[currentRow] = inputDetails;
				selfSerialized.setPreloadedData(null);
			}
			
			$('#selfSerializedModal').modal('hide');
			$(".alert").hide();
			$('#productInput').focus();
		} else {
			myShowAlert('danger', 'No se ha ingresado la totalidad de productos requeridos. Por favor ingrese los restantes.', "selfSerializedModalAlertDiv");
		}
	});

	$("#confirmButton").click(function() {
		if (validateForm()) {
			if (inputDetailGroup.length > 0 || isUpdate ) {
				isButtonConfirm = true;
				var jsonInput = {
					"id": $("#inputId").val(),
					"conceptId": $("#conceptInput").val(),
					"providerId": $("#providerInput").val(),
					"deliveryLocationId": $("#deliveryLocationInput").val(),
					"agreementId": $("#agreementInput").val(),
					"deliveryNoteNumber": $("#deliveryNotePOSInput").val() + $("#deliveryNoteNumberInput").val(),
					"purchaseOrderNumber": $("#purchaseOrderNumberInput").val().trim(),
					"date": $("#currentDateInput").val(),
					"inputDetails": []
				};
				
				for (var i = 0, lengthI = inputDetailGroup.length; i < lengthI; i++) {
					for (var j = 0; lengthJ = inputDetailGroup[i].length, j < lengthJ; j++) {
						jsonInput.inputDetails.push(inputDetailGroup[i][j]);
					}
				}
	
				if(!isUpdate){
					$.ajax({
						url: "saveInput.do?isSerializedReturn=false",
						type: "POST",
						contentType:"application/json",
						data: JSON.stringify(jsonInput),
						async: true,
			            beforeSend : function() {
			                $.blockUI({ message: 'Espere un Momento por favor...' });
			             },
						success: function(response, textStatus, jqXHR) {
						},
						error: function(response, jqXHR, textStatus, errorThrown) {
							myGenericError();
						},
						complete: function(jqXHR, textStatus) {
							$.unblockUI();
							if (textStatus === 'success') {
								generateInputPDFReport(jqXHR.responseJSON.id,true);
							}
						}
					});
				}else{
					$.ajax({
						url: "updateInput.do",
						type: "POST",
						contentType:"application/json",
						data: JSON.stringify(jsonInput),
						async: true,
			            beforeSend : function() {
			            	$.blockUI({ message: 'Espere un Momento por favor...' });
			             }, 
						success: function(response, textStatus, jqXHR) {
							$.unblockUI();
							if(response.resultado == true){
								if (textStatus === 'success') {
									generateInputPDFReport(response.operationId,false);
								}
							}else{
								var errors = "";
								for (var i = 0, lengthI = response.myOwnErrors.length; i < lengthI; i++) {
									errors += response.myOwnErrors[i] + "<br />";
								}
								
								if(response.errores != null){
									errors += "<strong>Errores informados por ANMAT:</strong><br />";
									for (var i = 0, lengthI = response.errores.length; i < lengthI; i++) {
										errors += response.errores[i]._c_error + " - " + response.errores[i]._d_error + "<br />";
									}
								}
								myShowAlert("danger", errors,null);
							}
						},
						error: function(response, jqXHR, textStatus, errorThrown) {
							$.unblockUI();
							myGenericError();
						}
					});
				}
				
			} else {
				myShowAlert('danger', 'Por favor, ingrese al menos un producto.');
			}
		}
	});
	
	$("#forcedInput").click(function() {
		$('#forcedInputConfirmationModal').modal();
	});
	
	$("#authorizeWithoutInform").click(function() {
		//var popup = window.open();
		if (validateForm()) {
			isButtonConfirm = true;
			var jsonInput = {
				"id": $("#inputId").val(),
				"conceptId": $("#conceptInput").val(),
				"providerId": $("#providerInput").val(),
				"deliveryLocationId": $("#deliveryLocationInput").val(),
				"agreementId": $("#agreementInput").val(),
				"deliveryNoteNumber": $("#deliveryNotePOSInput").val() + $("#deliveryNoteNumberInput").val(),
				"purchaseOrderNumber": $("#purchaseOrderNumberInput").val().trim(),
				"date": $("#currentDateInput").val(),
				"inputDetails": []
			};
			
			for (var i = 0, lengthI = inputDetailGroup.length; i < lengthI; i++) {
				for (var j = 0; lengthJ = inputDetailGroup[i].length, j < lengthJ; j++) {
					jsonInput.inputDetails.push(inputDetailGroup[i][j]);
				}
			}

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
					if(response != null){
						var doc = printIOPDF('input', response.id, response.inputDetails);
						var string = doc.output('datauristring');
						var x = window.open('','_blank', '', false);
						x.document.open();
						x.document.location=string;
						
						myRedirect("success","Se ha autorizado el ingreso de mercader\u00eda n\u00famero: " + $("#inputId").val(), "searchInputToUpdate.do");
					}
				},
				error: function(response, jqXHR, textStatus, errorThrown) {
					$.unblockUI();
					myGenericError();
				}
			});
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
	
	var hasChanged = function() {
		if($("#conceptInput").val()!= "" || $("#providerInput").val() != "" || 
				$("#agreementInput").val() != "" || 
				$("#deliveryNotePOSInput").val() != "" ||
				$("#deliveryNoteNumberInput").val() != "" ||
				$("#purchaseOrderNumberInput").val() != "" || 
				inputDetailGroup.length > 0){
			return true;
		}else{
			return false;
		}
	};

	$(window).bind("beforeunload",function(event) {
	    if (hasChanged() && isButtonConfirm == false && isUpdate == false) {
	    	return "Existen cambios que no fueron confirmados.";
	    } /*else {
	    	isButtonConfirm = false;
	    }*/
	});
	
	$("input").blur(function() {
		if ($("#deliveryNotePOSInput").val() != "")
			$("#deliveryNotePOSInput").val(addLeadingZeros($("#deliveryNotePOSInput").val(), 4));
		 if ($("#deliveryNoteNumberInput").val() != "")
			 $("#deliveryNoteNumberInput").val(addLeadingZeros($("#deliveryNoteNumberInput").val(), 8));
	 });
	
	$("#inputForm input, #inputForm select").keypress(function(event) {
		return event.keyCode != 13;
	});
	
	//Seleccion de Proveedor o Cliente de acuerdo al tipo de concepto.
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
						$('#deliveryLocationInput').chosen().trigger('chosen:activate');
					}else{
						$("#providerDiv").show();
						$("#deliveryLocationDiv").hide();
						$('#deliveryLocationInput').val('').trigger('chosen:updated');
						$('#providerInput').chosen().trigger('chosen:activate');
					}
				},
				error: function(jqXHR, textStatus, errorThrown) {
					myGenericError();
				}
			});
		}
	});
	
	$('#providerInput').on('change', function(evt, params) {
		$('#agreementInput').chosen().trigger('chosen:activate');
	});
	
	$('#deliveryLocationInput').on('change', function(evt, params) {
		$('#agreementInput').chosen().trigger('chosen:activate');
	});
	
	$('#agreementInput').on('change', function(evt, params) {
		$('#deliveryNotePOSInput').focus();
	});
	
	$("#delete").click(function() {
		$.ajax({
			url: "cancelInputWithoutStock.do",
			type: "GET",
			contentType:"application/json",
			data: {
				inputId: $("#inputId").val()
				},
			async: true,
			success: function(response) {
				myRedirect("success", "Se ha realizado la anulaci\u00f3n del ingreso de mercader\u00eda correctamente","searchInputToUpdate.do");
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myDeleteInputError();
			}
		});
	});
};