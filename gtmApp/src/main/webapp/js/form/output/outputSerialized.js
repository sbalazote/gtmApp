
OutputSerialized = function() {
	
	// Atributos del objeto
	var tempSerialNumbers = [];
	var productSelectedGtin = null;
	var preloadedData = null;
	var preloadedAmount = null;
	var preloadedProduct = null;
	var preloadedProductId = null;
	var preloadedProductType = null;
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
	
	var setPreloadedProductType = function(value) {
		preloadedProductType = value;
	};

	var setFormatSerializedId = function(value) {
		formatSerializedId = value;
	};
	
	// TODO mejorar esto- ahora no hace el paginado.
	$("#serializedTable").bootgrid({
		rowCount: -1
	});
	
	var addAmount = function(value) {
		updateAmounts(parseInt(value));
	};
	
	var subtractAmount = function(value) {
		updateAmounts(parseInt(-value));
	};
	
	var updateAmounts = function(amount) {
		var entered = parseInt($('#serializedEnteredAmountLabel').text());
		var remaining = parseInt($('#serializedRemainingAmountLabel').text());
		
		$('#serializedEnteredAmountLabel').text(entered += amount);
		$('#serializedRemainingAmountLabel').text(remaining -= amount);
		
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
		$("#serializedTable").bootgrid("append", aaData);
	};

	var addToLabels = function(gtin, serialNumber, batch, expirationDate) {
		$("#providerSerializedLastGtin").text(gtin);
		$("#providerSerializedLastSerialNumber").text(serialNumber);
		$("#providerSerializedLastBatch").text(batch);
		$("#providerSerializedLastExpirationDate").text(expirationDate);
	};

	var clearLabels = function() {
		$("#providerSerializedLastGtin").text('');
		$("#providerSerializedLastSerialNumber").text('');
		$("#providerSerializedLastBatch").text('');
		$("#providerSerializedLastExpirationDate").text('');
	};

	var isProviderSelfSerialized = false;

	var generateRow = function() {
		var readSerialNumber = $("#readSerialNumberInput");

		isSelfSerialized(readSerialNumber.val().trim());
		if (preloadedProductType == "PS") {
			if(isProviderSelfSerialized){
				parseSerial(readSerialNumber);
			}else {
				findStockBySerial(readSerialNumber.val().trim());
			}
		} else {
			// if self serialized

			$.ajax({
				url: "isParseSelfSerial.do",
				type: "GET",
				async: false,
				data: {
					serial: readSerialNumber.val()
				},
				success: function(response) {
					if (!response) {
						readSerialNumber.val("");
						readSerialNumber.tooltip("destroy").data("title", "Formato de Serie Inv\u00e1lido").addClass("has-error").tooltip();
						addToLabels("None", "None", "None", "None");
						return;
					}

					findStock(readSerialNumber.val().substring(3, 16) + readSerialNumber.val().substring(18),null, null, productSelectedGtin);
				},
				error: function(jqXHR, textStatus, errorThrown) {
					myGenericError("serializedModalAlertDiv");
				}
			});

		}
	};

	var isSelfSerialized = function(serialNumber){
		$.ajax({
			url: "isParseSelfSerial.do",
			type: "GET",
			async: false,
			data: {
				serial: serialNumber
			},
			success: function(response) {
				isProviderSelfSerialized = response;
			},
			error: function(jqXHR, textStatus, errorThrown) {
				isProviderSelfSerialized = false;
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
				formatSerializedId: formatSerializedId
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
				if(response.gtin != null) {
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
					readSerialNumber.val("");
					readSerialNumber.tooltip("destroy").data("title", "GTIN le\u00eddo no coincide con el seleccionado").addClass("has-error").tooltip();
					addToLabels(gtin, response.serialNumber, response.batch, response.expirationDate);
					return;
				}
				//TODO seguir desde aca
				findStock(response.serialNumber, response.batch, response.expirationDate, gtin);
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myGenericError("serializedModalAlertDiv");
			}
		});
	};

	var findStockBySerial = function(serialNumber, gtin) {
		var readSerialNumber = $("#readSerialNumberInput");
		$.ajax({
			url: "getStockByParseSerial.do",
			type: "GET",
			async: false,
			data: {
				productId: preloadedProductId,
				serialNumber: serialNumber,
				agreementId: $("#agreementInput").val()
			},
			success: function(response) {
				if (response) {
					// Si el serial existe en la tabla temporal del input
					if ($.inArray(response.serialNumber, tempSerialNumbers) != -1) {
						readSerialNumber.val("");
						readSerialNumber.tooltip("destroy").data("title", "Serie ya ingresado").addClass("has-error").tooltip();
						addToLabels(response.gtin.number, response.serialNumber, response.batch, myParseDateShort(response.expirationDate));
						return;
					}

					var batch = response.batch;
					var expirationDate = response.expirationDate;

					var gtin = null;
					if(response.gtin != null){
						gtin = response.gtin.number;
					}

					addAmount(1);
					addToLabels(gtin, response.serialNumber, batch, myParseDateShort(expirationDate));
					addToTable(gtin, response.serialNumber, batch, myParseDate(expirationDate));
					tempSerialNumbers.push(response.serialNumber);

					readSerialNumber.val("");
					readSerialNumber.data("title", "").removeClass("has-error").tooltip("destroy");

					readSerialNumber.focus();
				} else {
					readSerialNumber.val("");
					readSerialNumber.tooltip("destroy").data("title", "El producto le\u00eddo no se encuentra en stock").addClass("has-error").tooltip();
					addToLabels("None", "None", "None", "None");
				}
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myGenericError("serializedModalAlertDiv");
			}
		});
	};

	var findStock = function(serialNumber, batch, expirationDate, gtin) {
		var serialNumber = serialNumber;
		var batch = batch;
		var expirationDate = expirationDate;
		var gtin = gtin;
		var readSerialNumber = $("#readSerialNumberInput");
		
		// Si el serial existe en la tabla temporal del input
		if ($.inArray(serialNumber, tempSerialNumbers) != -1) {
			readSerialNumber.val("");
			readSerialNumber.tooltip("destroy").data("title", "Serie ya ingresado").addClass("has-error").tooltip();
			addToLabels(gtin, serialNumber, response.batch, response.expirationDate);
			return;
		}
		
		$.ajax({
			url: "getSerializedStock.do",
			type: "GET",
			async: false,
			data: {
				productId: preloadedProductId,
				serialNumber: serialNumber,
				batch: batch,
				expirationDate: expirationDate,
				gtin: gtin,
				agreementId: $("#agreementInput").val()
			},
			success: function(response) {
				if (response) {
					addAmount(1);
					addToLabels((gtin != null) ? gtin : "None", serialNumber, batch, expirationDate);
					addToTable((gtin != null) ? gtin : null, response.serialNumber, response.batch, myParseDate(response.expirationDate));
					tempSerialNumbers.push(serialNumber);
					
					readSerialNumber.val("");
					readSerialNumber.data("title", "").removeClass("has-error").tooltip("destroy");
					
					readSerialNumber.focus();
				} else {
					readSerialNumber.val("");
					addToLabels((gtin != null) ? gtin : "None", serialNumber, batch, expirationDate);
					readSerialNumber.tooltip("destroy").data("title", "El producto le\u00eddo no se encuentra en stock").addClass("has-error").tooltip();

					readSerialNumber.focus();
				}
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myGenericError("serializedModalAlertDiv");
			}
		});
	};
	
	$('#serializedTable tbody').on("click", ".command-delete", function() {
		subtractAmount(1);
		var parent = $(this).parent().parent();
		var serialNumberToDelete = parent.find("td:nth(1)").html();
		var indexOfSerialNumberToDelete = tempSerialNumbers.indexOf(serialNumberToDelete);
		tempSerialNumbers.splice(indexOfSerialNumberToDelete,1);
		var rows = Array();
		rows[0] = $(this).attr("data-row-id");
		$("#serializedTable").bootgrid("remove", rows);
	});
	
	var preloadModalData = function () {
		$("#serializedTable").bootgrid("clear");
		$('#serializedProductLabel').text(preloadedProduct);
		$('#serializedRequestedAmountLabel').text(preloadedAmount);

		clearLabels();
		
		if (preloadedData != null) {
			for (var i = 0; i < preloadedData.length; i++) {
				addToTable(preloadedData[i].gtin, preloadedData[i].serialNumber, preloadedData[i].batch, preloadedData[i].expirationDate);
			}
			$('#serializedEnteredAmountLabel').text(preloadedAmount);
			$('#serializedRemainingAmountLabel').text(parseInt(0));
			disableInputs();
		} else {
			$('#serializedEnteredAmountLabel').text(parseInt(0));
			$('#serializedRemainingAmountLabel').text(preloadedAmount);
			enableInputs();
		}
	};
	
	$('#serializedModal').on('shown.bs.modal', function () {
	    $('#readSerialNumberInput').focus();
	});
	
	$('#serializedModal').on('hidden.bs.modal', function () {
	    myResetForm($("#serializedModalForm")[0], null);
	});
	
	$('#serializedModal').on('keypress', function(e) {
		//	Si la tecla presionada es 'ENTER'
	    if (e.keyCode === 13) {
	    	var remainingAmount = $('#serializedRemainingAmountLabel').text();
			if (remainingAmount == 0) {
				$("#serializedAcceptButton").trigger('click');
			} else {
				$("#serializedAddButton").trigger('click');
			}
			return false;
	    }
	});
	
	$("#serializedAddButton").click(function() {
		var remainingAmount = $('#serializedRemainingAmountLabel').text();
		if (remainingAmount > 0) {
			generateRow();
			checkLast();
		} else {
			myShowAlert('danger', 'Ya se ha ingresado la totalidad de productos requeridos. Por favor presione el bot\u00f3n "Confirmar".', "serializedModalAlertDiv");
		}
	});
	
	var checkLast = function() {
		var remaining = parseInt($('#serializedRemainingAmountLabel').text());
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
						$("#serializedAcceptButton").trigger('click');
						$('#productInput').focus();
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
		setPreloadedProductType: setPreloadedProductType,
		setFormatSerializedId: setFormatSerializedId,
		preloadModalData: preloadModalData
	};
};