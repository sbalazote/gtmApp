$(document).ready(function() {

	var serialsMap = {};
	var serialDetails = {};
	var modal;
	var dataSrc;
	var dataCode;
	var dataDescription;
	var dataInStock;

	$("#batchExpirationDatesTable").bootgrid()
		.on("cleared.rs.jquery.bootgrid", function (e)
		{
			$(this).bootgrid("append", serialDetails);
		})
		.on("appended.rs.jquery.bootgrid", function (e)
		{
			$(dataSrc).modal('hide');
		});

	$("#serialsTable").bootgrid()
		.on("cleared.rs.jquery.bootgrid", function (e)
		{
			$(this).bootgrid("append", serialDetails);
		})
		.on("appended.rs.jquery.bootgrid", function (e)
		{
			$(dataSrc).modal('hide');
		});

	$('#batchExpirationDatesModal').on('show.bs.modal', function () {
		var productCode = parseInt(dataCode);
		var productDescription = dataDescription;

		$("#batchExpirationDateProductDescription").text(productCode + " - " + productDescription);
		serialDetails = serialsMap[productCode+dataInStock];
		$("#batchExpirationDatesTable").bootgrid("clear");
	});

	$('#batchExpirationDatesModal').on('hide.bs.modal', function () {
		$(modal).modal('show');
	});

	$('#serialsModal').on('show.bs.modal', function (event) {
		var productCode = parseInt(dataCode);
		var productDescription = dataDescription;

		$("#serializedProductDescription").text(productCode + " - " + productDescription);
		serialDetails = serialsMap[productCode+dataInStock];
		$("#serialsTable").bootgrid("clear");
	});

	$('#serialsModal').on('hide.bs.modal', function () {
		$(modal).modal('show');
	});

	showInputModal = function (inputId) {
		$.ajax({
			url: "getInput.do",
			type: "GET",
			async: false,
			data: {
				inputId: inputId
			},
			success: function (response) {
				populateInputModal(response);
			},
			error: function (jqXHR, textStatus, errorThrown) {
				myGenericError();
			}
		});
	};

	var populateInputModal = function (response) {
		var id = addLeadingZeros(response.id,8);
		$("#inputId").text("Numero: " + id);

		if (response.cancelled) {
			$("#cancelled").text("ANULADO");
		} else {
			$("#cancelled").text("");
		}

		$("#ANMATCode").show();
		if (response.transactionCodeANMAT != null) {
			$("#transactionCode").text(response.transactionCodeANMAT);
		} else {
			if(response.informAnmat == true) {
				$("#transactionCode").text("Pendiente");
			}else{
				$("#transactionCode").text("No informa");
			}
		}
		if (response.selfSerializedTransactionCodeANMAT != null) {
			$("#selfSerializedTransactionCode").text(response.selfSerializedTransactionCodeANMAT);
		} else {
			if(response.hasToInformSelfSerialized == true) {
				$("#selfSerializedTransactionCode").text("Pendiente");
			}else{
				$("#selfSerializedTransactionCode").text("No informa");
			}
		}
		$('#dateModal').val(myParseDate(response.date));
		var conceptCode = addLeadingZeros(response.concept.code,4);
		$('#conceptModal').val(conceptCode + " - " + response.concept.description);
		var clientOrProviderCode;
		if (response.provider != null) {
			clientOrProviderCode= addLeadingZeros(response.provider.code,4);
			$('#clientOrProviderModal').val(clientOrProviderCode + " - " + response.provider.name);
		}
		if (response.deliveryLocation != null) {
			clientOrProviderCode= response.deliveryLocation.code;
			$('#clientOrProviderModal').val(clientOrProviderCode + " - " + response.deliveryLocation.name);
		}
		if (response.logisticsOperator != null) {
			$('#logisticsOperatorModal').val(response.logisticsOperator.code + " - " + response.logisticsOperator.name);
		}else{
			$('#logisticsOperatorModal').val("");
		}
		var code = addLeadingZeros(response.agreement.code,5);
		$('#agreementModal').val(code + " - " + response.agreement.description);
		$('#deliveryNoteNumberModal').val(response.deliveryNoteNumber);
		$('#purchaseOrderNumberModal').val(response.purchaseOrderNumber);

		var id = 0;
		var found = false;
		var inputDetails = [];
		serialsMap = {};
		serialDetails = {};

		for (var i = 0; i < response.inputDetails.length; i++) {
			found = false;
			for (var j = 0; j < inputDetails.length; j++) {
				if (response.inputDetails[i].product.id == inputDetails[j].id) {
					inputDetails[j].amount += response.inputDetails[i].amount;
					found = true;
				}
			}
			if (!found) {
				var inputDetail = {};
				inputDetail.id = response.inputDetails[i].product.id;
				inputDetail.code = response.inputDetails[i].product.code;
				inputDetail.description = response.inputDetails[i].product.description;
				inputDetail.amount = response.inputDetails[i].amount;
				inputDetail.serialNumber = response.inputDetails[i].serialNumber;
				inputDetails.push(inputDetail);
			}
			// Guardo lote/vte y series para mostrar los detalles.
			var gtinNumber = "";
			if(response.inputDetails[i].gtin != null){
				gtinNumber = response.inputDetails[i].gtin.number;
			}
			serialDetails = {
				id: id,
				gtin: gtinNumber,
				amount: response.inputDetails[i].amount,
				serialNumber: response.inputDetails[i].serialNumber,
				batch: response.inputDetails[i].batch,
				expirationDate: myParseDate(response.inputDetails[i].expirationDate),
				viewTraceability: "<a type='button' class='btn btn-sm btn-default' href='searchSerializedProduct.do?productId="+ response.inputDetails[i].product.id + "&serial=" + response.inputDetails[i].serialNumber + "' target='_blank'><span class='glyphicon glyphicon-search'></span> Ver Traza" + "<//a>"
			};
			var item = serialsMap[response.inputDetails[i].product.code] || [];
			item.push(serialDetails);
			serialsMap[response.inputDetails[i].product.code] = item;
			id++;
		}

		var tableRow;
		var command;
		var aaData = [];
		for (var i = 0; i < inputDetails.length; i++) {
			if (inputDetails[i].serialNumber == null) {
				command = "<button type=\"button\" data-toggle=\"modal\" data-src=\"#inputModal\" data-target=\"#batchExpirationDatesModal\" data-code=\"" + inputDetails[i].code + "\" data-description=\"" + inputDetails[i].description + "\" class=\"btn btn-sm btn-default command-view\"><span class=\"glyphicon glyphicon-eye-open\"></span></button>";
			} else {
				command = "<button type=\"button\" data-toggle=\"modal\" data-src=\"#inputModal\" data-target=\"#serialsModal\" data-code=\"" + inputDetails[i].code + "\" data-description=\"" + inputDetails[i].description + "\" class=\"btn btn-sm btn-default command-view\"><span class=\"glyphicon glyphicon-eye-open\"></span></button>";
			}
			tableRow = {
				code: inputDetails[i].code,
				description: inputDetails[i].description,
				amount: inputDetails[i].amount,
				command: command
			};
			aaData.push(tableRow);
		}
		$("#inputModalProductTable").bootgrid({caseSensitive: false}).bootgrid("clear").bootgrid("append", aaData);
		modal = '#inputModal';
		$('#inputModal').modal('show');
	};

	showDeliveryNoteByIdModal = function (deliveryNoteId) {
		$.ajax({
			url: "getDeliveryNoteById.do",
			type: "GET",
			async: false,
			data: {
				deliveryNoteId: deliveryNoteId
			},
			success: function (response) {
				populateDeliveryNoteModal(response);
			},
			error: function (jqXHR, textStatus, errorThrown) {
				myGenericError();
			}
		});
	};

	var populateDeliveryNoteModal = function (response) {
		if (response.cancelled) {
			$("#deliveryNoteCancelled").show();
		} else {
			$("#deliveryNoteCancelled").hide();
		}

		$("#deliveryNoteId").text("Numero: " + response.number);

		$('#dateDeliveryNoteModal').val(response.date);
		//var clientCode = addLeadingZeros(response.deliveryLocation.code, 4);
		//$('#clientOrProviderDeliveryNoteModal').val(clientCode + " - " + response.deliveryLocation);
		$('#deliveryLocationOrProviderDeliveryNoteModal').val(response.deliveryLocation);
		$('#clientDeliveryNoteModal').val(response.client);

		$('#agreementDeliveryNoteModal').val(response.agreement);

		$('#affiliateDeliveryNoteModal').val(response.affiliate);

		if(response.concept != null){
			$("#conceptDeliveryNote").show();
			$("#conceptDeliveryNoteModal").val(response.concept);
		}else {
			$("#conceptDeliveryNote").hide();
		}

		var value;
		var entity;
		if(response.supplyingId != null){
			value = response.supplyingId;
			entity = "Dispensa: ";
		}
		if(response.outputId != null){
			value = response.outputId;
			entity = "Egreso: ";
		}
		if(response.orderId != null){
			value = response.orderId;
			entity = "Armado: ";
		}
		$("#entityId").empty();
		$("#provisioningRequestId").empty();
		$("#entityId").append("<span class=\"label label-info\">" + entity + value + "</span> ");
		if(response.orderId != null) {
			$("#provisioningRequestId").append("<span class=\"label label-default\">" + "Pedido Nro.: " + response.provisioningRequestId + "</span> ");
		}

		$("#deliveryNoteModalANMATCode").show();

		if (response.transactionCodeANMAT != null) {
			$("#deliveryNoteModalTransactionCode").text(response.transactionCodeANMAT);
		} else {
			if(response.informAnmat == true) {
				$("#deliveryNoteModalTransactionCode").text("Pendiente");
			}else{
				$("#deliveryNoteModalTransactionCode").text("No informa");
			}
		}

		var id = 0;
		var found = false;
		var deliveryNoteDetails = [];
		serialsMap = {};
		serialDetails = {};

		for (var i = 0; i < response.deliveryNoteDetails.length; i++) {
			found = false;
			for (var j = 0; j < deliveryNoteDetails.length; j++) {
				if (response.supplyingId != null) {
					if ((response.deliveryNoteDetails[i].product + (response.deliveryNoteDetails[i].inStock ? "" : "(*)")) == deliveryNoteDetails[j].description) {
						deliveryNoteDetails[j].amount += response.deliveryNoteDetails[i].amount;
						found = true;
					}
				} else {
					if (response.deliveryNoteDetails[i].productId == deliveryNoteDetails[j].id) {
						deliveryNoteDetails[j].amount += response.deliveryNoteDetails[i].amount;
						found = true;
					}
				}
			}
			if (!found) {
				var deliveryNoteDetail = {};
				var descriptionDiscriminator;
				if (response.supplyingId != null) {
					descriptionDiscriminator = (response.deliveryNoteDetails[i].inStock) ? "" : "(*)";
				} else {
					descriptionDiscriminator = "";
				}
				deliveryNoteDetail.id = response.deliveryNoteDetails[i].id;
				deliveryNoteDetail.code = response.deliveryNoteDetails[i].code;
				deliveryNoteDetail.description = response.deliveryNoteDetails[i].product + descriptionDiscriminator;
				deliveryNoteDetail.amount = response.deliveryNoteDetails[i].amount;
				deliveryNoteDetail.serialNumber = response.deliveryNoteDetails[i].serialNumber;
				if (response.supplyingId != null) {
					deliveryNoteDetail.inStock = descriptionDiscriminator;
				} else {
					deliveryNoteDetail.inStock = "";
				}
				deliveryNoteDetails.push(deliveryNoteDetail);
			}
			// Guardo lote/vte y series para mostrar los detalles.
			var gtinNumber = "";
			if(response.deliveryNoteDetails[i].gtinNumber != null){
				gtinNumber = response.deliveryNoteDetails[i].gtinNumber;
			}
			serialDetails = {
				id: id,
				gtin: gtinNumber,
				amount: response.deliveryNoteDetails[i].amount,
				serialNumber: response.deliveryNoteDetails[i].serialNumber,
				batch: response.deliveryNoteDetails[i].batch,
				expirationDate: response.deliveryNoteDetails[i].expirationDate,
				viewTraceability: "<a type='button' class='btn btn-sm btn-default' href='searchSerializedProduct.do?productId="+ response.deliveryNoteDetails[i].productId + "&serial=" + response.deliveryNoteDetails[i].serialNumber + "' target='_blank'><span class='glyphicon glyphicon-search'></span> Ver Traza" + "<//a>"

			};
			if (response.supplyingId != null) {
				var item = serialsMap[response.deliveryNoteDetails[i].code + (response.deliveryNoteDetails[i].inStock ? "" : "(*)")] || [];
				item.push(serialDetails);
				serialsMap[response.deliveryNoteDetails[i].code + (response.deliveryNoteDetails[i].inStock ? "" : "(*)")] = item;
			} else {
				var item = serialsMap[response.deliveryNoteDetails[i].code] || [];
				item.push(serialDetails);
				serialsMap[response.deliveryNoteDetails[i].code] = item;
			}
			id++;
		}

		var tableRow;
		var command;
		var aaData = [];
		for (var i = 0; i < deliveryNoteDetails.length; i++) {
			if (deliveryNoteDetails[i].serialNumber == null) {
				command = "<button type=\"button\" data-toggle=\"modal\" data-src=\"#deliveryNoteModal\" data-target=\"#batchExpirationDatesModal\" data-code=\"" + deliveryNoteDetails[i].code + "\" data-description=\"" + deliveryNoteDetails[i].description + "\" data-inStock=\"" + deliveryNoteDetails[i].inStock + "\" class=\"btn btn-sm btn-default command-view\"><span class=\"glyphicon glyphicon-eye-open\"></span></button>";
			} else {
				command = "<button type=\"button\" data-toggle=\"modal\" data-src=\"#deliveryNoteModal\" data-target=\"#serialsModal\" data-code=\"" + deliveryNoteDetails[i].code + "\" data-description=\"" + deliveryNoteDetails[i].description + "\" data-inStock=\"" + deliveryNoteDetails[i].inStock + "\" class=\"btn btn-sm btn-default command-view\"><span class=\"glyphicon glyphicon-eye-open\"></span></button>";
			}
			tableRow = {
				code: deliveryNoteDetails[i].code,
				description: deliveryNoteDetails[i].description,
				amount: deliveryNoteDetails[i].amount,
				command: command
			};
			aaData.push(tableRow);
		}
		$("#deliveryNoteModalProductTable").bootgrid({caseSensitive: false}).bootgrid("clear").bootgrid("append", aaData);
		modal = '#deliveryNoteModal';

		$('#deliveryNoteModal').modal('show');
	};

	showOutputModal = function (outputId) {
		$.ajax({
			url: "getOutput.do",
			type: "GET",
			async: false,
			data: {
				outputId: outputId
			},
			success: function (response) {
				populateOutputModal(response);
			},
			error: function (jqXHR, textStatus, errorThrown) {
				myGenericError();
			}
		});
	};

	var populateOutputModal = function (response) {

		var deliveriesNotesNumbers = [];
		$.ajax({
			url: "getOutputsDeliveriesNoteNumbers.do",
			type: "GET",
			async: false,
			data: {
				outputId: response.id
			},
			success: function (res) {
				deliveriesNotesNumbers = res;
			},
			error: function (jqXHR, textStatus, errorThrown) {
				myGenericError();
			}
		});

		var id = addLeadingZeros(response.id,8);
		$("#outputId").text("Numero: " + id);

		if (response.cancelled) {
			$("#outputCancelled").show();
		} else {
			$("#outputCancelled").hide();
		}

		$("#outputModalTransactionCode").html("");

		if(deliveriesNotesNumbers.length > 0){
			$("#outputDeliveriesNotesLabel").show();
			$("#outputDeliveriesNotesNumbers").html("");
			$.each(deliveriesNotesNumbers, function( index, value ) {
				$("#outputDeliveriesNotesNumbers").append("<span class=\"label label-info\">" + value + "</span> ");
			});
		} else {
			$("#outputDeliveriesNotesLabel").hide();
			$("#outputDeliveriesNotesNumbers").html("");
		}

		var jsonDeliveryNoteSearch = {
			"deliveryNoteNumber": "",
			"dateFrom": "",
			"dateTo": "",
			"conceptId": null,
			"providerId": null,
			"deliveryLocationId": null,
			"agreementId": null,
			"productId": null,
			"clientId": null,
			"affiliateId": null,
			"outputId": response.id,
			"supplyingId": null,
			"provisioningRequestId": null,
			"cancelled": null,
			"productMonodrugId": null
		};

		$.ajax({
			url: "getDeliveryNoteFromOutputForSearch.do",
			type: "POST",
			contentType:"application/json",
			async: false,
			data: JSON.stringify(jsonDeliveryNoteSearch),
			success: function(deliveryNoteList) {
				for (var i = 0, l = deliveryNoteList.length; i < l; ++i) {
					if (deliveryNoteList[i].transactionCodeANMAT != null) {
						//$("#outputModalTransactionCode").text(deliveryNoteList[i].transactionCodeANMAT);
						$("#outputModalTransactionCode").append("<span class=\"label label-info\">" + deliveryNoteList[i].transactionCodeANMAT + "</span> ");
					} else {
						if(deliveryNoteList[i].informAnmat == true) {
							//$("#outputModalTransactionCode").text("Pendiente");
                            $("#outputModalTransactionCode").append("<span class=\"label label-warning\">Pendiente</span> ");
                        }else{
							//$("#outputModalTransactionCode").text("No informa");
                            $("#outputModalTransactionCode").append("<span class=\"label label-warning\">No informa</span> ");
                        }
					}
				}
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myGenericError();
			}
		});

		$('#dateModalOutput').val(myParseDate(response.date));
		var conceptCode = addLeadingZeros(response.concept.code,4);
		$('#conceptModalOutput').val(conceptCode + " - " + response.concept.description);
		var clientOrProviderCode;
		if (response.provider != null) {
			clientOrProviderCode = addLeadingZeros(response.provider.code,4);
			$('#clientOrProviderModalOutput').val(clientOrProviderCode + " - " + response.provider.name);
		}
		if (response.deliveryLocation != null) {
			clientOrProviderCode = addLeadingZeros(response.deliveryLocation.code,4);
			$('#clientOrProviderModalOutput').val(clientOrProviderCode + " - " + response.deliveryLocation.name);
		}
		var code = addLeadingZeros(response.agreement.code,5);
		$('#agreementModalOutput').val(code + " - " + response.agreement.description);

		var id = 0;
		var found = false;
		var outputDetails = [];
		serialsMap = {};
		serialDetails = {};

		for (var i = 0; i < response.outputDetails.length; i++) {
			found = false;
			for (var j = 0; j < outputDetails.length; j++) {
				if (response.outputDetails[i].product.id == outputDetails[j].id) {
					outputDetails[j].amount += response.outputDetails[i].amount;
					found = true;
				}
			}
			if (!found) {
				var outputDetail = {};
				outputDetail.id = response.outputDetails[i].product.id;
				outputDetail.code = response.outputDetails[i].product.code;
				outputDetail.description = response.outputDetails[i].product.description;
				outputDetail.amount = response.outputDetails[i].amount;
				outputDetail.serialNumber = response.outputDetails[i].serialNumber;
				outputDetails.push(outputDetail);
			}
			// Guardo lote/vte y series para mostrar los detalles.
			var gtinNumber = "";
			if(response.outputDetails[i].gtin != null){
				gtinNumber = response.outputDetails[i].gtin.number;
			}
			serialDetails = {
				id: id,
				gtin: gtinNumber,
				amount: response.outputDetails[i].amount,
				serialNumber: response.outputDetails[i].serialNumber,
				batch: response.outputDetails[i].batch,
				expirationDate: myParseDate(response.outputDetails[i].expirationDate),
				viewTraceability: "<a type='button' class='btn btn-sm btn-default' href='searchSerializedProduct.do?productId="+ response.outputDetails[i].product.id + "&serial=" + response.outputDetails[i].serialNumber + "' target='_blank'><span class='glyphicon glyphicon-search'></span> Ver Traza" + "<//a>"

			};
			var item = serialsMap[response.outputDetails[i].product.code] || [];
			item.push(serialDetails);
			serialsMap[response.outputDetails[i].product.code] = item;
			id++;
		}

		var tableRow;
		var command;
		var aaData = [];
		for (var i = 0; i < outputDetails.length; i++) {
			if (outputDetails[i].serialNumber == null) {
				command = "<button type=\"button\" data-toggle=\"modal\" data-src=\"#outputModal\" data-target=\"#batchExpirationDatesModal\" data-code=\"" + outputDetails[i].code + "\" data-description=\"" + outputDetails[i].description + "\" class=\"btn btn-sm btn-default command-view\"><span class=\"glyphicon glyphicon-eye-open\"></span></button>";
			} else {
				command = "<button type=\"button\" data-toggle=\"modal\" data-src=\"#outputModal\" data-target=\"#serialsModal\" data-code=\"" + outputDetails[i].code + "\" data-description=\"" + outputDetails[i].description + "\" class=\"btn btn-sm btn-default command-view\"><span class=\"glyphicon glyphicon-eye-open\"></span></button>";
			}
			tableRow = {
				code: outputDetails[i].code,
				description: outputDetails[i].description,
				amount: outputDetails[i].amount,
				command: command
			};
			aaData.push(tableRow);
		}
		$("#outputModalProductTable").bootgrid({caseSensitive: false}).bootgrid("clear").bootgrid("append", aaData);
		modal = '#outputModal';
		$('#outputModal').modal('show');
	};

	showOrderModal = function (orderId) {
		$.ajax({
			url: "getOrder.do",
			type: "GET",
			async: false,
			data: {
				orderId: orderId
			},
			success: function (response) {
				populateOrderModal(response);
			},
			error: function (jqXHR, textStatus, errorThrown) {
				myGenericError();
			}
		});
	};

	var populateOrderModal = function (response) {

		var deliveriesNotesNumbers = [];
		$.ajax({
			url: "getOrdersDeliveriesNoteNumbers.do",
			type: "GET",
			async: false,
			data: {
				orderId: response.id
			},
			success: function (res) {
				deliveriesNotesNumbers = res;
			},
			error: function (jqXHR, textStatus, errorThrown) {
				myGenericError();
			}
		});

		var provisioningId = addLeadingZeros(response.provisioningRequest.id,8);
		$("#provisioningId").text("Numero Pedido: " + provisioningId);

		if (response.cancelled) {
			$("#orderCancelled").show();
		} else {
			$("#orderCancelled").hide();
		}

		if(deliveriesNotesNumbers.length > 0){
			$("#orderDeliveriesNotesLabel").show();
			$("#orderDeliveriesNotesNumbers").html("");
			$.each(deliveriesNotesNumbers, function( index, value ) {
				$("#orderDeliveriesNotesNumbers").append("<span class=\"label label-info\">" + value + "</span> ");
			});
		} else {
			$("#orderDeliveriesNotesLabel").hide();
			$("#orderDeliveriesNotesNumbers").html("");
		}

		$('#orderModalDeliveryLocationInput').val(response.provisioningRequest.deliveryLocation.code + " - " + response.provisioningRequest.deliveryLocation.name);
		var code = addLeadingZeros(response.provisioningRequest.agreement.code,5);
		$('#orderModalAgreementInput').val(code + " - " + response.provisioningRequest.agreement.description);
		if (response.provisioningRequest.logisticsOperator) {
			$('#orderModalLogisticsOperatorInput').val(response.provisioningRequest.logisticsOperator.code + " - " + response.provisioningRequest.logisticsOperator.name);
		}
		$('#orderModalAffiliateInput').val(response.provisioningRequest.affiliate.code + " - " + response.provisioningRequest.affiliate.surname + " " + response.provisioningRequest.affiliate.name);
		$('#orderModalDeliveryDateInput').val(myParseDate(response.provisioningRequest.deliveryDate));
		$('#orderModalCommentTextarea').val(response.provisioningRequest.comment);
		var clientCode = addLeadingZeros(response.provisioningRequest.client.code,4);
		$('#orderModalClientInput').val(clientCode + " - " + response.provisioningRequest.client.name);

		var id = 0;
		var found = false;
		var orderDetails = [];
		serialsMap = {};
		serialDetails = {};

		for (var i = 0; i < response.orderDetails.length; i++) {
			found = false;
			for (var j = 0; j < orderDetails.length; j++) {
				if (response.orderDetails[i].product.id == orderDetails[j].id) {
					orderDetails[j].amount += response.orderDetails[i].amount;
					found = true;
				}
			}
			if (!found) {
				var orderDetail = {};
				orderDetail.id = response.orderDetails[i].product.id;
				orderDetail.code = response.orderDetails[i].product.code;
				orderDetail.description = response.orderDetails[i].product.description;
				orderDetail.amount = response.orderDetails[i].amount;
				orderDetail.serialNumber = response.orderDetails[i].serialNumber;
				orderDetails.push(orderDetail);
			}
			// Guardo lote/vte y series para mostrar los detalles.
			var gtinNumber = "";
			if(response.orderDetails[i].gtin != null){
				gtinNumber = response.orderDetails[i].gtin.number;
			}
			serialDetails = {
				id: id,
				gtin: gtinNumber,
				amount: response.orderDetails[i].amount,
				serialNumber: response.orderDetails[i].serialNumber,
				batch: response.orderDetails[i].batch,
				expirationDate: myParseDate(response.orderDetails[i].expirationDate),
				viewTraceability: "<a type='button' class='btn btn-sm btn-default' href='searchSerializedProduct.do?productId="+ response.orderDetails[i].product.id + "&serial=" + response.orderDetails[i].serialNumber + "' target='_blank'><span class='glyphicon glyphicon-search'></span> Ver Traza" + "<//a>"
			};
			var item = serialsMap[response.orderDetails[i].product.code] || [];
			item.push(serialDetails);
			serialsMap[response.orderDetails[i].product.code] = item;
			id++;
		}

		var tableRow;
		var command;
		var aaData = [];
		for (var i = 0; i < orderDetails.length; i++) {
			if (orderDetails[i].serialNumber == null) {
				command = "<button type=\"button\" data-toggle=\"modal\" data-src=\"#orderModal\" data-target=\"#batchExpirationDatesModal\" data-code=\"" + orderDetails[i].code + "\" data-description=\"" + orderDetails[i].description + "\" class=\"btn btn-sm btn-default command-view\"><span class=\"glyphicon glyphicon-eye-open\"></span></button>";
			} else {
				command = "<button type=\"button\" data-toggle=\"modal\" data-src=\"#orderModal\" data-target=\"#serialsModal\" data-code=\"" + orderDetails[i].code + "\" data-description=\"" + orderDetails[i].description + "\" class=\"btn btn-sm btn-default command-view\"><span class=\"glyphicon glyphicon-eye-open\"></span></button>";
			}
			tableRow = {
				code: orderDetails[i].code,
				description: orderDetails[i].description,
				amount: orderDetails[i].amount,
				command: command
			};
			aaData.push(tableRow);
		}
		$("#orderModalProductTable").bootgrid({caseSensitive: false}).bootgrid("clear").bootgrid("append", aaData);
		modal = '#orderModal';
		$('#orderModal').modal('show');
	};

	showProvisioningRequestModal = function (provisioningId) {
		$.ajax({
			url: "getProvisioningRequest.do",
			type: "GET",
			async: false,
			data: {
				provisioningId: provisioningId
			},
			success: function (response) {
				populateProvisioningModal(response);
			},
			error: function (jqXHR, textStatus, errorThrown) {
				myGenericError();
			}
		});
	};

	var populateProvisioningModal = function (response) {
		var id = addLeadingZeros(response.id,8);
		$("#provisioningRequestId").text("Numero: " + id);

		var provisioningRequestState = response.state.description;

		$("#provisioningRequestState").text(provisioningRequestState);

		$("#deliveryNoteNumbers").empty();

		if (provisioningRequestState === "REMITO IMPRESO") {
			$.ajax({
				url: "getAssociatedDeliveryNotes.do",
				type: "GET",
				async: false,
				data: {
					provisioningId: response.id
				},
				success: function (response) {
					$.each(response, function(index, value) {
						if(value.cancelled == false){
							$("#deliveryNoteNumbers").append("<span class=\"label label-info\">" + value.number + "</span> ");
						}else{
							$("#deliveryNoteNumbers").append("<span class=\"label label-danger\">" + value.number + "</span> ");
						}

					});

				}/*,
				error: function (jqXHR, textStatus, errorThrown) {
					myGenericError();
				}*/
			});
		}

		$('#deliveryLocationProvisioningRequestModal').val(response.deliveryLocation.code + " - " + response.deliveryLocation.name);
		var code = addLeadingZeros(response.agreement.code,5);
		$('#agreementProvisioningRequestModal').val(code + " - " + response.agreement.description);
		if (response.logisticsOperator) {
			$('#logisticsOperatorProvisioningRequestModal').val(response.logisticsOperator.code + " - " + response.logisticsOperator.name);
		}
		$('#affiliateProvisioningRequestModal').val(response.affiliate.code + " - " + response.affiliate.surname + " " + response.affiliate.name);
		$('#deliveryDateProvisioningRequestModal').val(myParseDate(response.deliveryDate));
		$('#commentProvisioningRequestModal').val(response.comment);
		var clientCode = addLeadingZeros(response.client.code,4);
		$('#clientProvisioningRequestModal').val(clientCode + " - " + response.client.name);

		var found = false;
		var provisioningRequestDetails = [];

		for (var i = 0; i < response.provisioningRequestDetails.length; i++) {
			found = false;
			for (var j = 0; j < provisioningRequestDetails.length; j++) {
				if (response.provisioningRequestDetails[i].product.id == provisioningRequestDetails[j].id) {
					provisioningRequestDetails[j].amount += response.provisioningRequestDetails[i].amount;
					found = true;
				}
			}
			if (!found) {
				var provisioningRequestDetail = {};
				provisioningRequestDetail.id = response.provisioningRequestDetails[i].product.id;
				provisioningRequestDetail.code = response.provisioningRequestDetails[i].product.code;
				provisioningRequestDetail.description = response.provisioningRequestDetails[i].product.description;
				provisioningRequestDetail.amount = response.provisioningRequestDetails[i].amount;
				provisioningRequestDetail.serialNumber = response.provisioningRequestDetails[i].serialNumber;
				provisioningRequestDetails.push(provisioningRequestDetail);
			}
		}
		var tableRow;
		var aaData = [];
		for (var i = 0; i < provisioningRequestDetails.length; i++) {
			tableRow = {
				code: provisioningRequestDetails[i].code,
				description: provisioningRequestDetails[i].description,
				amount: provisioningRequestDetails[i].amount
			};
			aaData.push(tableRow);
		}
		$("#provisioningRequestModalProductTable").bootgrid({caseSensitive: false}).bootgrid("clear").bootgrid("append", aaData);
		modal = '#provisioningRequestModal';
		$('#provisioningRequestModal').modal('show');

	};


	showSupplyingModal = function (supplyingId) {
		$.ajax({
			url: "getSupplying.do",
			type: "GET",
			async: false,
			data: {
				supplyingId: supplyingId
			},
			success: function (response) {
				populateSupplyingModal(response);
			},
			error: function (jqXHR, textStatus, errorThrown) {
				myGenericError();
			}
		});
	};

	var populateSupplyingModal = function (response) {

		var deliveriesNotesNumbers = [];
		$.ajax({
			url: "getSupplyingsDeliveriesNoteNumbers.do",
			type: "GET",
			async: false,
			data: {
				supplyingId: response.id
			},
			success: function (res) {
				deliveriesNotesNumbers = res;
			},
			error: function (jqXHR, textStatus, errorThrown) {
				myGenericError();
			}
		});

		var id = addLeadingZeros(response.id,8);
		$("#supplyingId").text("Numero: " + id);

		if (response.cancelled) {
			$("#supplyingCancelled").show();
		} else {
			$("#supplyingCancelled").hide();
		}

		if(deliveriesNotesNumbers.length > 0){
			$("#supplyingDeliveriesNotesLabel").show();
			$("#supplyingDeliveriesNotesNumbers").html("");
			$.each(deliveriesNotesNumbers, function( index, value ) {
				$("#supplyingDeliveriesNotesNumbers").append("<span class=\"label label-info\">" + value + "</span> ");
			});
		} else {
			$("#supplyingDeliveriesNotesLabel").hide();
			$("#supplyingDeliveriesNotesNumbers").html("");
		}

		if (response.transactionCodeANMAT != null) {
			$("#supplyingModalANMATCode").show();
			$("#supplyingModalTransactionCode").text(response.transactionCodeANMAT);
		} else {
			$("#supplyingModalANMATCode").hide();
			$("#supplyingModalTransactionCode").text("");
		}
		var code = addLeadingZeros(response.agreement.code,5);
		$('#supplyingModalAgreementInput').val(code + " - " + response.agreement.description);

		$('#supplyingModalDateInput').val(myParseDate(response.date));
		$('#supplyingModalAffiliateInput').val(response.affiliate.code + " - " + response.affiliate.surname + " " + response.affiliate.name);
		var clientCode = addLeadingZeros(response.client.code,4);
		$('#supplyingModalClientInput').val(clientCode + " - " + response.client.name);

		var id = 0;
		var found = false;
		var supplyingDetails = [];
		serialsMap = {};
		serialDetails = {};

		for (var i = 0; i < response.supplyingDetails.length; i++) {
			found = false;
			for (var j = 0; j < supplyingDetails.length; j++) {
				if ((response.supplyingDetails[i].product.description + (response.supplyingDetails[i].inStock ? "" : "(*)")) == supplyingDetails[j].description) {
					supplyingDetails[j].amount += response.supplyingDetails[i].amount;
					found = true;
				}
			}
			if (!found) {
				var supplyingDetail = {};
				var descriptionDiscriminator = response.supplyingDetails[i].inStock ? "" : "(*)";
				supplyingDetail.id = response.supplyingDetails[i].product.id;
				supplyingDetail.code = response.supplyingDetails[i].product.code;
				supplyingDetail.description = response.supplyingDetails[i].product.description + descriptionDiscriminator;
				supplyingDetail.amount = response.supplyingDetails[i].amount;
				supplyingDetail.serialNumber = response.supplyingDetails[i].serialNumber;
				supplyingDetail.inStock = descriptionDiscriminator;
				supplyingDetails.push(supplyingDetail);
			}
			// Guardo lote/vte y series para mostrar los detalles.
			var gtinNumber = "";
			if(response.supplyingDetails[i].gtin != null){
				gtinNumber = response.supplyingDetails[i].gtin.number;
			}

			serialDetails = {
				id: id,
				gtin: gtinNumber,
				amount: response.supplyingDetails[i].amount,
				serialNumber: response.supplyingDetails[i].serialNumber,
				batch: response.supplyingDetails[i].batch,
				expirationDate: myParseDate(response.supplyingDetails[i].expirationDate),
				viewTraceability: "<a type='button' class='btn btn-sm btn-default' href='searchSerializedProduct.do?productId="+ response.supplyingDetails[i].product.id + "&serial=" + response.supplyingDetails[i].serialNumber + "' target='_blank'><span class='glyphicon glyphicon-search'></span> Ver Traza" + "<//a>"
			};
			var item = serialsMap[response.supplyingDetails[i].product.code + (response.supplyingDetails[i].inStock ? "" : "(*)")] || [];
			item.push(serialDetails);
			serialsMap[response.supplyingDetails[i].product.code + (response.supplyingDetails[i].inStock ? "" : "(*)")] = item;
			id++;
		}

		var tableRow;
		var command;
		var aaData = [];
		for (var i = 0; i < supplyingDetails.length; i++) {
			if (supplyingDetails[i].serialNumber == null) {
				command = "<button type=\"button\" data-toggle=\"modal\" data-src=\"#supplyingModal\" data-target=\"#batchExpirationDatesModal\" data-code=\"" + supplyingDetails[i].code + "\" data-description=\"" + supplyingDetails[i].description + "\" data-inStock=\"" + supplyingDetails[i].inStock + "\" class=\"btn btn-sm btn-default command-view\"><span class=\"glyphicon glyphicon-eye-open\"></span></button>";
			} else {
				command = "<button type=\"button\" data-toggle=\"modal\" data-src=\"#supplyingModal\" data-target=\"#serialsModal\" data-code=\"" + supplyingDetails[i].code + "\" data-description=\"" + supplyingDetails[i].description + "\" data-inStock=\"" + supplyingDetails[i].inStock + "\" class=\"btn btn-sm btn-default command-view\"><span class=\"glyphicon glyphicon-eye-open\"></span></button>";
			}
			tableRow = {
				code: supplyingDetails[i].code,
				description: supplyingDetails[i].description,
				amount: supplyingDetails[i].amount,
				command: command
			};
			aaData.push(tableRow);
		}
		$("#supplyingModalProductTable").bootgrid({caseSensitive: false}).bootgrid("clear").bootgrid("append", aaData);
		modal = '#supplyingModal';
		$('#supplyingModal').modal('show');

	};

	$("#inputModalProductTableBody, #outputModalProductTableBody, #supplyingModalProductTableBody, #deliveryNoteModalProductTableBody, #orderModalProductTableBody").on("click", ".command-view", function(e) {
		dataSrc = $(this).attr("data-src");
		dataCode = $(this).attr("data-code");
		dataDescription = $(this).attr("data-description");
		dataInStock = (dataSrc == "#supplyingModal" || dataSrc == "#deliveryNoteModal") ? $(this).attr("data-inStock") : "";
		$($(this).attr("data-target")).modal("show");
	});
});