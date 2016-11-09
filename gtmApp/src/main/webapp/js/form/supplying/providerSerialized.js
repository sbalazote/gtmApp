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
	var formatSerializedId = null;
	
	var getTempSerialNumbers = function(value) {
		return tempSerialNumbers;
	};
	
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

	var setFormatSerializedId = function(value) {
		formatSerializedId = value;
	};
	
	var formValidator = null;
	
	// TODO mejorar esto- ahora no hace el paginado.
	$("#outOfStockProviderSerializedTable").bootgrid({
		rowCount: -1
	});

	var validateForm = function() {
		var form = $("#outOfStockProviderSerializedModalForm");
		formValidator = form.validate({
			rules: {
				outOfStockReadSerialNumber: {
					required: true
				},
				outOfStockProviderSerializedBatch: {
					required: true
				},
				outOfStockProviderSerializedExpirationDate: {
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
		var entered = parseInt($('#outOfStockProviderSerializedEnteredAmountLabel').text());
		var remaining = parseInt($('#outOfStockProviderSerializedRemainingAmountLabel').text());
		
		$('#outOfStockProviderSerializedEnteredAmountLabel').text(entered += amount);
		$('#outOfStockProviderSerializedRemainingAmountLabel').text(remaining -= amount);
		
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
		$("#outOfStockProviderSerializedTable").bootgrid("append", aaData);
	};

	var addToLabels = function(gtin, serialNumber, batch, expirationDate) {
		$("#outOfStockProviderSerializedLastGtin").text(gtin);
		$("#outOfStockProviderSerializedLastSerialNumber").text(serialNumber);
		$("#outOfStockProviderSerializedLastBatch").text(batch);
		$("#outOfStockProviderSerializedLastExpirationDate").text(expirationDate);
	};

	var clearLabels = function() {
		$("#outOfStockProviderSerializedLastGtin").text('');
		$("#outOfStockProviderSerializedLastSerialNumber").text('');
		$("#outOfStockProviderSerializedLastBatch").text('');
		$("#outOfStockProviderSerializedLastExpirationDate").text('');
	};

	var generateRow = function() {
		var readSerialNumber = $("#outOfStockReadSerialNumberInput");
		if(formatSerializedId == null){
			getMatches(readSerialNumber);
		}

		if(formatSerializedId != null){
			parseSerial(readSerialNumber);
		}
	};

	var getMatches = function(readSerialNumber){
		formatSerializedId = null;
		$.ajax({
			url: "getMatchParsers.do",
			type: "GET",
			async: false,
			data: {
				serial: readSerialNumber.val().trim()
			},
			success: function (response) {
				if(response.length > 1){
					$('#formatSerializedInput').empty();
					$('#formatSerializedInput').append("<option value=''></option>");
					for(var i=0;i < response.length;i++){
						$('#formatSerializedInput').append("<option value=" + response[i].providerSerializedFormatId + ">" + response[i].serialNumber + "</option>");
					}
					$('#formatSerializedInput').trigger("chosen:updated");
					$('#formatSerializedModal').modal('show');
				}else if(response.length == 1){
					formatSerializedId = response[0].providerSerializedFormatId;
				}else{
					readSerialNumber.val("");
					readSerialNumber.tooltip("destroy").data("title", "Formato de Serie Inv\u00e1lido").addClass("has-error").tooltip();
					addToLabels("None", "None", "None", "None");
					return;
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				myGenericError("providerSerializedModalAlertDiv");
			}
		});
	};

	var parseSerial = function (readSerialNumber) {
		$.ajax({
			url: "parseSerial.do",
			type: "GET",
			async: false,
			data: {
				serial: readSerialNumber.val().trim(),
				formatSerializedId: null
			},
			success: function(response) {
				if (response == "") {
					if(formatSerializedId == null){
						readSerialNumber.val("");
						readSerialNumber.tooltip("destroy").data("title", "Formato de Serie Inv\u00e1lido").addClass("has-error").tooltip();
						addToLabels("None", "None", "None", "None");
						return;
					}else{
						getMatches(readSerialNumber);
					}
				}
				var gtinFound = false;
				var gtin;
				if(response.gtin != null){
					$.ajax({
						url: "getGtins.do",
						type: "GET",
						async: false,
						data: {
							productId: preloadedProductId
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
							myGenericError("outOfStockProviderSerializedModalAlertDiv");
						}
					});
					gtin = response.gtin;
				}else{
					gtin = productSelectedGtin;
					gtinFound = true;
				}


				//	Si el Gtin leido no coincide con el seleccionado en la pantalla de input.
				if (gtinFound == false) {
					readSerialNumber.val("");
					readSerialNumber.tooltip("destroy").data("title", "GTIN le\u00eddo no coincide con el seleccionado").addClass("has-error").tooltip();
					addToLabels(gtin, response.serialNumber, response.batch, response.expirationDate);
					return;
				}

				var serialNumber = response.serialNumber;

				// Si el serial existe en la tabla temporal del input
				if ($.inArray(serialNumber, tempSerialNumbers) != -1) {
					readSerialNumber.val("");
					readSerialNumber.tooltip("destroy").data("title", "Serie ya ingresado").addClass("has-error").tooltip();
					addToLabels(gtin, serialNumber, response.batch, response.expirationDate);
					return;
				}

				var batch = "";
				if (response.batch != null) {
					batch = response.batch;
					$("#outOfStockProviderSerializedBatchInput").val("");
					$("#outOfStockProviderSerializedBatchInput").attr("disabled", true);
				} else {
					$("#outOfStockProviderSerializedBatchInput").attr("disabled", false);
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
						readSerialNumber.val("");
						readSerialNumber.tooltip("destroy").data("title", "Fecha de vencimiento inv\u00e1lida o anterior a la fecha del d\u00eda.").addClass("has-error").tooltip();
						addToLabels(gtin, serialNumber, batch, expirationDate);
						return;
					}

					//	Seteo expirationDate de la forma tradicional (ddmmaa).
					expirationDate = dd.concat(mm, aa);

					$("#outOfStockProviderSerializedExpirationDateInput").val("");
					$("#outOfStockProviderSerializedExpirationDateInput").attr("disabled", true);
				} else {
					$("#outOfStockProviderSerializedExpirationDateInput").attr("disabled", false);
				}
				if (validateForm()) {
					//	El serie no indica lte/vto. Lo leo de los inputs
					if ((batch == "") && (expirationDate == "")) {
						batch = $("#outOfStockProviderSerializedBatchInput").val();
						expirationDate = $("#outOfStockProviderSerializedExpirationDateInput").val();
					}

					addAmount(1);
					addToLabels(gtin, serialNumber, batch, expirationDate);
					addToTable(gtin, serialNumber, batch, expirationDate);
					tempSerialNumbers.push(serialNumber);

					readSerialNumber.val("");
					formValidator.resetForm();

					readSerialNumber.focus();
				} else {
					if (batch == "") {
						$("#outOfStockProviderSerializedBatchInput").focus();
					} else if (expirationDate == "") {
						$("#outOfstockProviderSerializedExpirationDateInput").focus();
					}
				}
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myGenericError("outOfStockProviderSerializedModalAlertDiv");
			}
		});
	}
	
	$('#outOfStockProviderSerializedTable tbody').on("click", ".command-delete", function() {
		subtractAmount(1);
		var parent = $(this).parent().parent();
		var serialNumberToDelete = parent.find("td:nth(1)").html();
		var indexOfSerialNumberToDelete = tempSerialNumbers.indexOf(serialNumberToDelete);
		tempSerialNumbers.splice(indexOfSerialNumberToDelete,1);
		var rows = Array();
		rows[0] = $(this).attr("data-row-id");
		$("#outOfStockProviderSerializedTable").bootgrid("remove", rows);
	});
	
	var preloadModalData = function () {
		$("#outOfStockProviderSerializedTable").bootgrid("clear");
		$('#outOfStockProviderSerializedProductLabel').text(preloadedProduct);
		$('#outOfStockProviderSerializedRequestedAmountLabel').text(preloadedAmount);

		clearLabels();

		if (preloadedData != null) {
			for (var i = 0; i < preloadedData.length; i++) {
				addToTable(preloadedData[i].gtin, preloadedData[i].serialNumber, preloadedData[i].batch, preloadedData[i].expirationDate);
			}
			$('#outOfStockProviderSerializedEnteredAmountLabel').text(preloadedAmount);
			$('#outOfStockProviderSerializedRemainingAmountLabel').text(parseInt(0));
			disableInputs();
		} else {
			$('#outOfStockProviderSerializedEnteredAmountLabel').text(parseInt(0));
			$('#outOfStockProviderSerializedRemainingAmountLabel').text(preloadedAmount);
			enableInputs();
		}
	};
	
	$('#outOfStockProviderSerializedModal').on('shown.bs.modal', function () {
	    $('#outOfStockReadSerialNumberInput').focus();
	});
	
	$('#outOfStockProviderSerializedModal').on('hidden.bs.modal', function () {
	    myResetForm($("#outOfStockProviderSerializedModalForm")[0], formValidator);
	});
	
	$('#outOfStockReadSerialNumberInput').on('keypress', function(e) {
		//	Si la tecla presionada es 'ENTER'
	    if (e.keyCode === 13) {
			$("#outOfStockProviderSerializedAddButton").trigger('click');
			return false;
		}
	});

	$('#outOfStockProviderSerializedBatchInput').on('keypress', function(e) {
		//	Si la tecla presionada es 'ENTER'
		if (e.keyCode === 13) {
			$('#outOfStockProviderSerializedExpirationDateInput').focus();
			return false;
		}
	});

	$('#outOfStockProviderSerializedExpirationDateInput').on('keypress', function(e) {
		//	Si la tecla presionada es 'ENTER'
		if (e.keyCode === 13) {
			$("#outOfStockProviderSerializedAddButton").trigger('click');
			return false;
		}
	});

	$('#outOfStockProviderSerializedAddButton').on('keypress', function(e) {
		//	Si la tecla presionada es 'ENTER'
		if (e.keyCode === 13) {
			var remainingAmount = $('#outOfStockProviderSerializedRemainingAmountLabel').text();
			if (remainingAmount == 0) {
				$("#outOfStockProviderSerializedAcceptButton").trigger('click');
			} else {
				$("#outOfStockProviderSerializedAddButton").trigger('click');
			}
			return false;
		}
	});
	
	$("#outOfStockProviderSerializedAddButton").click(function() {
		var remainingAmount = $('#outOfStockProviderSerializedRemainingAmountLabel').text();
		if (remainingAmount > 0) {
			generateRow();
			checkLast();
		} else {
			myShowAlert('danger', 'Ya se ha ingresado la totalidad de productos requeridos. Por favor presione el bot\u00f3n "Confirmar".', "providerSerializedModalAlertDiv");
		}
	});
	
	var checkLast = function() {
		var remaining = parseInt($('#outOfStockProviderSerializedRemainingAmountLabel').text());
		if (remaining == 0) {
			BootstrapDialog.show({
				title: 'Informacion',
				message: '<strong>Carga Completa.</strong> Confirma Operaci\u00f3n?',
				closable: false,
				buttons: [{
					label: 'No',
					action: function(dialogItself) {
						dialogItself.close();
					}
				}, {
					label: 'Si',
					hotkey: 115,	// F4.
					cssClass: 'btn-primary',
					action: function(dialogItself) {
						dialogItself.close();
						$("#outOfStockProviderSerializedAcceptButton").trigger('click');
					}
				}]
			});
		}
	};

	return {
		getTempSerialNumbers: getTempSerialNumbers,
		setTempSerialNumbers: setTempSerialNumbers,
		setProductSelectedGtin: setProductSelectedGtin,
		getPreloadedData: getPreloadedData,
		setPreloadedData: setPreloadedData,
		setPreloadedAmount: setPreloadedAmount,
		setPreloadedProduct: setPreloadedProduct,
		setPreloadedProductId: setPreloadedProductId,
		setFormatSerializedId: setFormatSerializedId,
		preloadModalData: preloadModalData
	};
};