
OutputSerialized = function() {
	
	// Atributos del objeto
	var tempSerialNumbers = [];
	var productSelectedGtin = null;
	var preloadedData = null;
	var preloadedAmount = null;
	var preloadedProduct = null;
	var preloadedProductId = null;
	var preloadedProductType = null;
	
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
		$("#serializedTable tbody").append("<tr>"
			+ "<td class='gtin' style='display: none;'>"+gtin+"</td>"
			+ "<td class='serialNumber'>"+serialNumber+"</td>"
			+ "<td class='batch'>"+batch+"</td>"
			+ "<td class='expirationDate'>"+expirationDate+"</td>"
			+ "<td><button class='btnDelete' type='button'><span class='glyphicon glyphicon-remove'/></button></td>"
			+ "</tr>");
	};
	
	var generateRow = function() {
		var readSerialNumber = $("#readSerialNumberInput");
		
		if (preloadedProductType == "PS") {
		
			$.ajax({
				url: "parseSerial.do",
				type: "GET",
				async: false,
				data: {
					serial: readSerialNumber.val()
				},
				success: function(response) {
					if (response == "") {
						readSerialNumber.tooltip("destroy").data("title", "Formato de Serie Inv&aacute;lido").addClass("has-error").tooltip();
						return;
					}
					
		            var gtinFound = false;
                    var gtin;
                    if(response.gtin != null) {
                        $.ajax({
                            url: "getGtins.do",
                            type: "GET",
                            async: false,
                            data: {
                                productId: preloadedProductId,
                            },
                            success: function (responseGtin) {
                                var gtins = responseGtin;
                                for (var i = 0; i < gtins.length; i++) {
                                    if (gtins[i].number == response.gtin) {
                                        gtinFound = true;
                                    }
                                }
                            },
                            error: function (jqXHR, textStatus, errorThrown) {
                                myGenericError("providerSerializedModalAlertDiv");
                            }
                        });

                        var gtin = response.gtin;
                    }else{
                        gtin = productSelectedGtin;
                        gtinFound = true;
                    }

					//	Si el Gtin leido no coincide con el seleccionado en la pantalla de input.
					if (gtinFound == false) {
						readSerialNumber.tooltip("destroy").data("title", "GTIN le&iacute;do no coincide con el seleccionado").addClass("has-error").tooltip();
						return;
					}
					//TODO seguir desde aca
					findStock(response.serialNumber, gtin);
				},
				error: function(jqXHR, textStatus, errorThrown) {
					myGenericError("serializedModalAlertDiv");
				}
			});
			
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
						readSerialNumber.tooltip("destroy").data("title", "Formato de Serie Inv&aacute;lido").addClass("has-error").tooltip();
						return;
					}

					findStock(readSerialNumber.val().substring(3, 16) + readSerialNumber.val().substring(18), productSelectedGtin);
				},
				error: function(jqXHR, textStatus, errorThrown) {
					myGenericError("serializedModalAlertDiv");
				}
			});
			
		}
	};
	
	var findStock = function(serialNumber, gtin) {
		var readSerialNumber = $("#readSerialNumberInput");
		
		// Si el serial existe en la tabla temporal del input
		if ($.inArray(serialNumber, tempSerialNumbers) != -1) {
			readSerialNumber.tooltip("destroy").data("title", "Serie ya ingresado").addClass("has-error").tooltip();
			return;
		}
		
		$.ajax({
			url: "getSerializedStock.do",
			type: "GET",
			async: false,
			data: {
				productId: preloadedProductId,
				serialNumber: serialNumber,
				gtin: gtin
			},
			success: function(response) {
				if (response) {
					var batch = response.batch;
					var expirationDate = response.expirationDate;
					
					addAmount(1);
					addToTable(gtin, serialNumber, batch, myParseDate(expirationDate));
					tempSerialNumbers.push(serialNumber);
					
					readSerialNumber.val("");
					readSerialNumber.data("title", "").removeClass("has-error").tooltip("destroy");
					
					readSerialNumber.focus();
				} else {
					readSerialNumber.tooltip("destroy").data("title", "El producto le&iacute;do no se encuentra en stock").addClass("has-error").tooltip();
				}
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myGenericError("serializedModalAlertDiv");
			}
		});
	};
	
	$('#serializedTable tbody').on("click", ".btnDelete", function() {
		subtractAmount(1);
		var parent = $(this).parent().parent();
		var serialNumberToDelete = parent.children().eq(1).text();
		var indexOfSerialNumberToDelete = tempSerialNumbers.indexOf(serialNumberToDelete);
		tempSerialNumbers.splice(indexOfSerialNumberToDelete,1);
		parent.remove();
	});
	
	var preloadModalData = function () {
		$("#serializedModal tbody").html("");
		$('#serializedProductLabel').text(preloadedProduct);
		$('#serializedRequestedAmountLabel').text(preloadedAmount);
		
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
		} else {
			myShowAlert('danger', 'Ya se ha ingresado la totalidad de productos requeridos. Por favor presione el bot&oacute;n "Confirmar".', "serializedModalAlertDiv");
		}
	});
	
	return {
		setTempSerialNumbers: setTempSerialNumbers,
		setProductSelectedGtin: setProductSelectedGtin,
		getPreloadedData: getPreloadedData,
		setPreloadedData: setPreloadedData,
		setPreloadedAmount: setPreloadedAmount,
		setPreloadedProduct: setPreloadedProduct,
		setPreloadedProductId: setPreloadedProductId,
		setPreloadedProductType: setPreloadedProductType,
		preloadModalData: preloadModalData
	};
	
};
