/* ========================================================================
 * input: providerSerialized.js
======================================================================== */

/*
 * REGLA VALIDACION MASCARAS SERIALIZADOS ORIGEN
 * 01 + "0+GTIN" + 21(serie) de 20 d�gitos alfanum�ricos, 17(vencimiento) de 6 d�gitos AAMMDD y 10(lote) 20 d�gitos alfanum�ricos.
 * 
 * Ejemplos:
 * Etiqueta:  01 07798123400000 21 1234567890        (ID = 0)
 * Etiqueta:  01 07798123400000 21 1234567890 17 160830 10 aaa5s     (ID = 1)
 * Etiqueta:  01 07798123400000 10 55524758 17 140228 21 1234567890             (ID = 2)
 * Etiqueta:  01 07798123400000 17 150530 21 12345678901234567890 10 88895562   (ID = 3)
 *
 *
 *	en este caso valido el ID0 solamente
 */

ProviderSerialized = function() {
	
	// Atributos del objeto
	var tempSerialNumbers = [];
	var productSelectedGtin = null;
	var preloadedData = null;
	var preloadedAmount = null;
	var preloadedProduct = null;
	var preloadedProductId = null;
	
	var setTempSerialNumbers = function(value) {
		tempSerialNumbers = value || [];
	};
	
	var setProductSelectedGtin = function(value) {
		productSelectedGtin = value;
	};
	
	var getPreloadedData = function() {
		return preloadedData;
	};
	
	var setPreloadedData = function(value) {
		preloadedData = value;
	};
	
	var setPreloadedAmount = function(value) {
		preloadedAmount = value;
	};
	
	var setPreloadedProduct = function(value) {
		preloadedProduct = value;
	};
	
	var setPreloadedProductId = function(value) {
		preloadedProductId = value;
	};
	
	var formValidator = null;
	
	// TODO mejorar esto- ahora no hace el paginado.
	$("#providerSerializedTable").bootgrid({
		rowCount: -1
	});
	
	var validateForm = function() {
		var form = $("#providerSerializedModalForm");
		formValidator = form.validate({
			rules: {
				readSerialNumber: {
					required: true
				},
				providerSerializedBatch: {
					required: true
				},
				providerSerializedExpirationDate: {
					required: true,
					expirationDate: true
				}
			},
			showErrors: myShowErrors
		});
		
		return form.valid();
	};
	
	var addAmount = function(value) {
		updateAmounts(parseInt(value));
	};
	
	var subtractAmount = function(value) {
		updateAmounts(parseInt(-value));
	};
	
	var updateAmounts = function(amount) {
		var entered = parseInt($('#providerSerializedEnteredAmountLabel').text());
		var remaining = parseInt($('#providerSerializedRemainingAmountLabel').text());
		
		$('#providerSerializedEnteredAmountLabel').text(entered += amount);
		$('#providerSerializedRemainingAmountLabel').text(remaining -= amount);
		
		if (remaining == 0) {
			disableInputs();
		} else {
			enableInputs();
		}
	};
	
	var disableInputs = function() {
		$('#readSerialNumberInput').attr("disabled", true);
	};
	
	var enableInputs = function() {
		$('#readSerialNumberInput').attr("disabled", false);
	};
	
	var addToTable = function(gtin, serialNumber, batch, expirationDate) {
		var aaData = [];
		var row = {
			gtin: gtin,
			serialNumber: serialNumber,
			batch: batch,
			expirationDate: expirationDate,
			commands: "<button type=\"button\" class=\"btn btn-sm btn-default command-delete\" data-row-id=\"" + serialNumber + "\"><span class=\"glyphicon glyphicon-trash\"></span></button>"
		};
		aaData.push(row);
		$("#providerSerializedTable").bootgrid("append", aaData);
	};
	
	var generateRow = function() {
		var readSerialNumber = $("#readSerialNumberInput");
		$.ajax({
			url: "parseSerial.do",
			type: "GET",
			async: false,
			data: {
				serial: readSerialNumber.val()
			},
			success: function(response) {
				if (response == "") {
					readSerialNumber.tooltip("destroy").data("title", "Formato de Serie Inv\u00e1lido").addClass("has-error").tooltip();
					return;
				}
				var gtinFound = false;
                var gtin;
                if(response.gtin != null){
                    $.ajax({
                        url: "getGtins.do",
                        type: "GET",
                        async: false,
                        data: {
                            productId: preloadedProductId,
                        },
                        success: function(responseGtin) {
                            var gtins = responseGtin;
                            for (var i = 0; i < gtins.length; i++) {
                                if(gtins[i].number == response.gtin){
                                    gtinFound = true;
                                }
                            }
                        },
                        error: function(jqXHR, textStatus, errorThrown) {
                            myGenericError("providerSerializedModalAlertDiv");
                        }
                    });
                    gtin = response.gtin;
                }else{
                    gtin = productSelectedGtin;
                    gtinFound = true;
                }

				
				//	Si el Gtin leido no coincide con el seleccionado en la pantalla de input.
				if (gtinFound == false) {
					readSerialNumber.tooltip("destroy").data("title", "GTIN le\u00eddo no coincide con el seleccionado").addClass("has-error").tooltip();
					return;
				}
				
				var serialNumber = response.serialNumber;
				
				// Si el serial existe en la tabla temporal del input
				if ($.inArray(serialNumber, tempSerialNumbers) != -1) {
					readSerialNumber.val("");
					readSerialNumber.tooltip("destroy").data("title", "Serie ya ingresado").addClass("has-error").tooltip();
					return;
				}
				
				var batch = "";
				if (response.batch != null) {
					batch = response.batch;
					 $("#providerSerializedBatchInput").val("");
					 $("#providerSerializedBatchInput").attr("disabled", true);
				} else {
					 $("#providerSerializedBatchInput").attr("disabled", false);
				}
				
				var expirationDate = "";
				if (response.expirationDate != null) {
					expirationDate = response.expirationDate;
					
					//	Corto el expirationDate que viene en el serie dado que esta en formato (aammdd).
					var aa = expirationDate.slice(0,2);
					var mm = expirationDate.slice(2,4);
					var dd = expirationDate.slice(4,6);
					
					//	Verifico si la fecha es valida o no.
					var validExpirationDate = validateExpirationDate(parseInt(dd), parseInt(mm), parseInt("20"+aa));
					
					// Si no es valida la fecha de vencimiento que viene en el serie, dado que es anterior al dia de la fecha o mal formado.
					if (!validExpirationDate) {
						readSerialNumber.tooltip("destroy").data("title", "Fecha de vencimiento inv\u00e1lida o anterior a la fecha del d\u00eda.").addClass("has-error").tooltip();
						return;
					}
					
					//	Seteo expirationDate de la forma tradicional (ddmmaa).
					expirationDate = dd.concat(mm, aa);
					
					$("#providerSerializedExpirationDateInput").val("");
					$("#providerSerializedExpirationDateInput").attr("disabled", true);
				} else {
					$("#providerSerializedExpirationDateInput").attr("disabled", false);
				}
				//TODO agregar en la consulta el gtin
				//	Busco en el historial de inputs de la db para ver si existe o no el serie.
				$.ajax({
					url: "existsSerial.do",
					type: "GET",
					async: false,
					data: {
						serialNumber: serialNumber,
						productId: preloadedProductId,
						gtin: gtin
					},
					success: function(response) {
						var duplicateSerial = response;
						//	Si el serie existe marco la casilla input con error.
						if (duplicateSerial) {
							readSerialNumber.val("");
							readSerialNumber.tooltip("destroy").data("title", "Serie existente en la base de datos").addClass("has-error").tooltip();
							return;
						}

						if (validateForm()) {
							//	El serie no indica lte/vto. Lo leo de los inputs
							if ((batch == "") && (expirationDate == "")) {
								batch = $("#providerSerializedBatchInput").val(); 
								expirationDate = $("#providerSerializedExpirationDateInput").val();
							}

							addAmount(1);
							addToTable(gtin, serialNumber, batch, expirationDate);
							tempSerialNumbers.push(serialNumber);
							
							readSerialNumber.val("");
							formValidator.resetForm();
							
							readSerialNumber.focus();
						} else {
							if (batch == "") {
								$("#providerSerializedBatchInput").focus();
							} else if (expirationDate == "") {
								$("#providerSerializedExpirationDateInput").focus();
							}
						}
					},
					error: function(jqXHR, textStatus, errorThrown) {
						myGenericError("providerSerializedModalAlertDiv");
					}
				});
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myGenericError("providerSerializedModalAlertDiv");
			}
		});
	};
	
	$('#providerSerializedTable tbody').on("click", ".command-delete", function() {
		subtractAmount(1);
		var parent = $(this).parent().parent();
		var serialNumberToDelete = parent.find("td:nth(1)").html();
		var indexOfSerialNumberToDelete = tempSerialNumbers.indexOf(serialNumberToDelete);
		tempSerialNumbers.splice(indexOfSerialNumberToDelete,1);
		var rows = Array();
		rows[0] = $(this).attr("data-row-id");
		$("#providerSerializedTable").bootgrid("remove", rows);
	});
	
	var preloadModalData = function () {
		$("#providerSerializedTable").bootgrid("clear");
		$('#providerSerializedProductLabel').text(preloadedProduct);
		$('#providerSerializedRequestedAmountLabel').text(preloadedAmount);
		
		if (preloadedData != null) {
			for (var i = 0; i < preloadedData.length; i++) {
				addToTable(preloadedData[i].gtin, preloadedData[i].serialNumber, preloadedData[i].batch, preloadedData[i].expirationDate);
			}
			$('#providerSerializedEnteredAmountLabel').text(preloadedAmount);
			$('#providerSerializedRemainingAmountLabel').text(parseInt(0));
			disableInputs();
		} else {
			$('#providerSerializedEnteredAmountLabel').text(parseInt(0));
			$('#providerSerializedRemainingAmountLabel').text(preloadedAmount);
			enableInputs();
		}
	};
	
	$('#providerSerializedModal').on('shown.bs.modal', function () {
	    $('#readSerialNumberInput').focus();
	});
	
	$('#providerSerializedModal').on('hidden.bs.modal', function () {
	    myResetForm($("#providerSerializedModalForm")[0], formValidator);
	});
	
	$('#readSerialNumberInput').on('keypress', function(e) {
		//	Si la tecla presionada es 'ENTER'
	    if (e.keyCode === 13) {
	    	$("#providerSerializedAddButton").trigger('click');
	    	return false;
	    }
	});
	
	$('#providerSerializedBatchInput').on('keypress', function(e) {
		//	Si la tecla presionada es 'ENTER'
		if (e.keyCode === 13) {
			$('#providerSerializedExpirationDateInput').focus();
			return false;
		}
	});
	
	$('#providerSerializedExpirationDateInput').on('keypress', function(e) {
		//	Si la tecla presionada es 'ENTER'
		if (e.keyCode === 13) {
			$("#providerSerializedAddButton").trigger('click');
			return false;
		}
	});
	
	$('#providerSerializedAddButton').on('keypress', function(e) {
		//	Si la tecla presionada es 'ENTER'
	    if (e.keyCode === 13) {
	    	var remainingAmount = $('#providerSerializedRemainingAmountLabel').text();
			if (remainingAmount == 0) {
				$("#providerSerializedAcceptButton").trigger('click');
			} else {
				$("#providerSerializedAddButton").trigger('click');
			}
			return false;
	    }
	});
	
	$("#providerSerializedAddButton").click(function() {
		var remainingAmount = $('#providerSerializedRemainingAmountLabel').text();
		if (remainingAmount > 0) {
			generateRow();
			checkLast();
		} else {
			myShowAlert('danger', 'Ya se ha ingresado la totalidad de productos requeridos. Por favor presione el bot\u00f3n "Confirmar".', "providerSerializedModalAlertDiv");
		}
	});
	
	var checkLast = function() {
		var remaining = parseInt($('#providerSerializedRemainingAmountLabel').text());
		if (remaining == 0) {
			BootstrapDialog.show({
				title: 'Informacion',
				message: '<strong>Carga Completa.</strong> Confirma Operaci\u00f3n?',
				buttons: [{
					label: 'No',
					action: function(dialogItself) {
						dialogItself.close();
					}
				}, {
					label: 'Si',
					cssClass: 'btn-primary',
					action: function(dialogItself) {
						dialogItself.close();
						$("#providerSerializedAcceptButton").trigger('click');
					}
				}]
			});
		}
	};
	
	return {
		setTempSerialNumbers: setTempSerialNumbers,
		setProductSelectedGtin: setProductSelectedGtin,
		getPreloadedData: getPreloadedData,
		setPreloadedData: setPreloadedData,
		setPreloadedAmount: setPreloadedAmount,
		setPreloadedProduct: setPreloadedProduct,
		setPreloadedProductId: setPreloadedProductId,
		preloadModalData: preloadModalData
	};
};