$(document).ready(function() {

	var serialsMap = {};
	var serialDetails = {};
	var modal;
	var dataSrc;
	var dataCode;
	var dataDescription;

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
		serialDetails = serialsMap[productCode];
		$("#batchExpirationDatesTable").bootgrid("clear");
	});

	$('#batchExpirationDatesModal').on('hide.bs.modal', function () {
		$(modal).modal('show');
	});

	$('#serialsModal').on('show.bs.modal', function (event) {
		var productCode = parseInt(dataCode);
		var productDescription = dataDescription;

		$("#serializedProductDescription").text(productCode + " - " + productDescription);
		serialDetails = serialsMap[productCode];
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
		$("#inputId").text("Numero: " + response.id);
		if (response.cancelled) {
			$("#cancelled").text("ANULADO");
		} else {
			$("#cancelled").text("");
		}
		if (response.transactionCodeANMAT != null) {
			$("#ANMATCode").show();
			$("#transactionCode").text(response.transactionCodeANMAT);
		} else {
			$("#ANMATCode").hide();
			$("#transactionCode").text("");
		}
		$('#dateModal').val(myParseDate(response.date));
		$('#conceptModal').val(response.concept.code + " - " + response.concept.description);
		if (response.provider != null) {
			$('#clientOrProviderModal').val(response.provider.code + " - " + response.provider.name);
		}
		if (response.deliveryLocation != null) {
			$('#clientOrProviderModal').val(response.deliveryLocation.code + " - " + response.deliveryLocation.name);
		}
		$('#agreementModal').val(response.agreement.code + " - " + response.agreement.description);
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
				viewTraceability: "<a href='searchSerializedProduct.do?productId="+ response.inputDetails[i].product.id + "&serial=" + response.inputDetails[i].serialNumber + "' target='_blank'>Ver Traza" + "<//a>"
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
		$("#inputModalProductTable").bootgrid("clear").bootgrid("append", aaData);
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
		$('#clientOrProviderDeliveryNoteModal').val(response.deliveryLocation);
		$('#agreementDeliveryNoteModal').val(response.agreement);

		if (response.transactionCodeANMAT != null) {
			$("#deliveryNoteModalANMATCode").show();
			$("#deliveryNoteModalTransactionCode").text(response.transactionCodeANMAT);
		} else {
			$("#deliveryNoteModalANMATCode").hide();
			$("#deliveryNoteModalTransactionCode").text("");
		}

		var id = 0;
		var found = false;
		var orderOutputDetails = [];
		serialsMap = {};
		serialDetails = {};

		for (var i = 0; i < response.orderOutputDetails.length; i++) {
			found = false;
			for (var j = 0; j < orderOutputDetails.length; j++) {
				if (response.orderOutputDetails[i].product.id == orderOutputDetails[j].id) {
					orderOutputDetails[j].amount += response.orderOutputDetails[i].amount;
					found = true;
				}
			}
			if (!found) {
				var deliveryNoteDetail = {};
				deliveryNoteDetail.id = response.orderOutputDetails[i].id;
				deliveryNoteDetail.code = response.orderOutputDetails[i].code;
				deliveryNoteDetail.description = response.orderOutputDetails[i].product;
				deliveryNoteDetail.amount = response.orderOutputDetails[i].amount;
				deliveryNoteDetail.serialNumber = response.orderOutputDetails[i].serialNumber;
				orderOutputDetails.push(deliveryNoteDetail);
			}
			// Guardo lote/vte y series para mostrar los detalles.
            var gtinNumber = "";
            if(response.orderOutputDetails[i].gtin != null){
                gtinNumber = response.orderOutputDetails[i].gtinNumber;
            }
			serialDetails = {
				id: id,
                gtin: gtinNumber,
				amount: response.orderOutputDetails[i].amount,
				serialNumber: response.orderOutputDetails[i].serialNumber,
				batch: response.orderOutputDetails[i].batch,
				expirationDate: response.orderOutputDetails[i].expirationDate,
                viewTraceability: "<a href='searchSerializedProduct.do?productId="+ response.orderOutputDetails[i].productId + "&serial=" + response.orderOutputDetails[i].serialNumber + "' target='_blank'>Ver Traza" + "<//a>"
			};
			var item = serialsMap[response.orderOutputDetails[i].code] || [];
			item.push(serialDetails);
			serialsMap[response.orderOutputDetails[i].code] = item;
			id++;
		}

		var tableRow;
		var command;
		var aaData = [];
		for (var i = 0; i < orderOutputDetails.length; i++) {
			if (orderOutputDetails[i].serialNumber == null) {
				command = "<button type=\"button\" data-toggle=\"modal\" data-src=\"#deliveryNoteModal\" data-target=\"#batchExpirationDatesModal\" data-code=\"" + orderOutputDetails[i].code + "\" data-description=\"" + orderOutputDetails[i].description + "\" class=\"btn btn-sm btn-default command-view\"><span class=\"glyphicon glyphicon-eye-open\"></span></button>";
			} else {
				command = "<button type=\"button\" data-toggle=\"modal\" data-src=\"#deliveryNoteModal\" data-target=\"#serialsModal\" data-code=\"" + orderOutputDetails[i].code + "\" data-description=\"" + orderOutputDetails[i].description + "\" class=\"btn btn-sm btn-default command-view\"><span class=\"glyphicon glyphicon-eye-open\"></span></button>";
			}
			tableRow = {
				code: orderOutputDetails[i].code,
				description: orderOutputDetails[i].description,
				amount: orderOutputDetails[i].amount,
				command: command
			};
			aaData.push(tableRow);
		}
		$("#deliveryNoteModalProductTable").bootgrid("clear").bootgrid("append", aaData);
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

        var id = addLeadingZeros(response.id,6);
        $("#outputId").text("Numero: " + id);

        if (response.cancelled) {
            $("#outputCancelled").show();
        } else {
            $("#outputCancelled").hide();
        }

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

		if (response.transactionCodeANMAT != null) {
			$("#outputModalANMATCode").show();
			$("#outputModalTransactionCode").text(response.transactionCodeANMAT);
		} else {
			$("#outputModalANMATCode").hide();
			$("#outputModalTransactionCode").text("");
		}

		$('#dateModalOutput').val(myParseDate(response.date));
		$('#conceptModalOutput').val(response.concept.code + " - " + response.concept.description);
		if (response.provider != null) {
			$('#clientOrProviderModalOutput').val(response.provider.code + " - " + response.provider.name);
		}
		if (response.deliveryLocation != null) {
			$('#clientOrProviderModalOutput').val(response.deliveryLocation.code + " - " + response.deliveryLocation.name);
		}
		$('#agreementModalOutput').val(response.agreement.code + " - " + response.agreement.description);

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
                viewTraceability: "<a href='searchSerializedProduct.do?productId="+ response.outputDetails[i].product.id + "&serial=" + response.outputDetails[i].serialNumber + "' target='_blank'>Ver Traza" + "<//a>"

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
		$("#outputModalProductTable").bootgrid("clear").bootgrid("append", aaData);
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
		$('#orderModalProductTableBody').empty();

		$('#orderModalDeliveryLocationInput').val(response.provisioningRequest.deliveryLocation.code + " - " + response.provisioningRequest.deliveryLocation.name);
		$('#orderModalAgreementInput').val(response.provisioningRequest.agreement.code + " - " + response.provisioningRequest.agreement.description);
		if (response.provisioningRequest.logisticsOperator) {
			$('#orderModalLogisticsOperatorInput').val(response.provisioningRequest.logisticsOperator.code + " - " + response.provisioningRequest.logisticsOperator.name);
		}
		$('#orderModalAffiliateInput').val(response.provisioningRequest.affiliate.code + " - " + response.provisioningRequest.affiliate.surname + " " + response.provisioningRequest.affiliate.name);
		$('#orderModalDeliveryDateInput').val(myParseDate(response.provisioningRequest.deliveryDate));
		$('#orderModalCommentTextarea').val(response.provisioningRequest.comment);
		$('#orderModalClientInput').val(response.provisioningRequest.client.code + " - " + response.provisioningRequest.client.name);

		var tableRow;

		for (var i = 0; i < response.orderDetails.length; i++) {
			var serialNumber = "";
			if (response.orderDetails[i].serialNumber != null) {
				serialNumber = response.orderDetails[i].serialNumber;
			}
			tableRow = "<tr><td>" + response.orderDetails[i].product.code + " - "
			+ response.orderDetails[i].product.description + "</td>" +
			"<td>" + response.orderDetails[i].amount + "</td>" +
			"<td>" + serialNumber + "</td>" +
			"<td>" + response.orderDetails[i].batch + "</td>" +
			"<td>" + myParseDate(response.orderDetails[i].expirationDate) + "</td>" +
			"</tr>";
			$("#orderModalProductTableBody").append(tableRow);
		}

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
		$('#productTableBodyProvisioningRequest').empty();

		$('#deliveryLocationProvisioningRequestModal').val(response.deliveryLocation.code + " - " + response.deliveryLocation.name);
		$('#agreementProvisioningRequestModal').val(response.agreement.code + " - " + response.agreement.description);
		if (response.logisticsOperator) {
			$('#logisticsOperatorProvisioningRequestModal').val(response.logisticsOperator.code + " - " + response.logisticsOperator.name);
		}
		$('#affiliateProvisioningRequestModal').val(response.affiliate.code + " - " + response.affiliate.surname + " " + response.affiliate.name);
		$('#deliveryDateProvisioningRequestModal').val(myParseDate(response.deliveryDate));
		$('#commentProvisioningRequestModalTextArea').val(response.comment);
		$('#clientProvisioningRequestModal').val(response.client.code + " - " + response.client.name);

		var tableRow;

		for (var i = 0; i < response.provisioningRequestDetails.length; i++) {
			tableRow = "<tr><td>" + response.provisioningRequestDetails[i].product.code + " - " + response.provisioningRequestDetails[i].product.description + "</td><td>" + response.provisioningRequestDetails[i].amount + "</td></tr>";
			$("#productTableBodyProvisioningRequest").append(tableRow);
		}

		$('#provisioningModal').modal('show');
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

        var id = addLeadingZeros(response.id,6);
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

		$('#supplyingModalAgreementInput').val(response.agreement.code + " - " + response.agreement.description);
		$('#supplyingModalAffiliateInput').val(response.affiliate.code + " - " + response.affiliate.surname + " " + response.affiliate.name);
		$('#supplyingModalClientInput').val(response.client.code + " - " + response.client.name);

		var id = 0;
		var found = false;
		var supplyingDetails = [];
		serialsMap = {};
		serialDetails = {};

		for (var i = 0; i < response.supplyingDetails.length; i++) {
			found = false;
			for (var j = 0; j < supplyingDetails.length; j++) {
				if (response.supplyingDetails[i].product.id == supplyingDetails[j].id) {
					supplyingDetails[j].amount += response.supplyingDetails[i].amount;
					found = true;
				}
			}
			if (!found) {
				var supplyingDetail = {};
				supplyingDetail.id = response.supplyingDetails[i].product.id;
				supplyingDetail.code = response.supplyingDetails[i].product.code;
				supplyingDetail.description = response.supplyingDetails[i].product.description;
				supplyingDetail.amount = response.supplyingDetails[i].amount;
				supplyingDetail.serialNumber = response.supplyingDetails[i].serialNumber;
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
                viewTraceability: "<a href='searchSerializedProduct.do?productId="+ response.supplyingDetails[i].product.id + "&serial=" + response.supplyingDetails[i].serialNumber + "' target='_blank'>Ver Traza" + "<//a>"
			};
			var item = serialsMap[response.supplyingDetails[i].product.code] || [];
			item.push(serialDetails);
			serialsMap[response.supplyingDetails[i].product.code] = item;
			id++;
		}

		var tableRow;
		var command;
		var aaData = [];
		for (var i = 0; i < supplyingDetails.length; i++) {
			if (supplyingDetails[i].serialNumber == null) {
				command = "<button type=\"button\" data-toggle=\"modal\" data-src=\"#supplyingModal\" data-target=\"#batchExpirationDatesModal\" data-code=\"" + supplyingDetails[i].code + "\" data-description=\"" + supplyingDetails[i].description + "\" class=\"btn btn-sm btn-default command-view\"><span class=\"glyphicon glyphicon-eye-open\"></span></button>";
			} else {
				command = "<button type=\"button\" data-toggle=\"modal\" data-src=\"#supplyingModal\" data-target=\"#serialsModal\" data-code=\"" + supplyingDetails[i].code + "\" data-description=\"" + supplyingDetails[i].description + "\" class=\"btn btn-sm btn-default command-view\"><span class=\"glyphicon glyphicon-eye-open\"></span></button>";
			}
			tableRow = {
				code: supplyingDetails[i].code,
				description: supplyingDetails[i].description,
				amount: supplyingDetails[i].amount,
				command: command
			};
			aaData.push(tableRow);
		}
		$("#supplyingModalProductTable").bootgrid("clear").bootgrid("append", aaData);
		modal = '#supplyingModal';
		$('#supplyingModal').modal('show');

	};

	$("#inputModalProductTableBody, #outputModalProductTableBody, #supplyingModalProductTableBody, #deliveryNoteModalProductTableBody").on("click", ".command-view", function(e) {
		dataSrc = $(this).attr("data-src");
		dataCode = $(this).attr("data-code");
		dataDescription = $(this).attr("data-description");
		$($(this).attr("data-target")).modal("show");
	});
});