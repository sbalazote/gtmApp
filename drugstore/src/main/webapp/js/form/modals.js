$(document).ready(function() {
	
	showInputModal = function(inputId) {
		$.ajax({
			url: "getInput.do",
			type: "GET",
			async: false,
			data: {
				inputId: inputId,
			},
			success: function(response) {
				populateInputModal(response);
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myGenericError();
			}
		});
	};
	
	var populateInputModal = function(response) {
		$('#productTableBody').empty();
		
		$("#inputId").text("Numero: " + response.id);
		if(response.cancelled){
			$("#cancelled").text("ANULADO");
		}else{
			$("#cancelled").text("");
		}
		if(response.transactionCodeANMAT != null){
			$("#ANMATCode").show();
			$("#transactionCode").text(response.transactionCodeANMAT);
		}else{
			$("#ANMATCode").hide();
			$("#transactionCode").text("");
		}
		$('#dateModal').val(myParseDate(response.date));
		$('#conceptModal').val(response.concept.code + " - " + response.concept.description);
		if(response.provider != null){
			$('#clientOrProviderModal').val(response.provider.code + " - " + response.provider.name);
		}
		if(response.deliveryLocation != null){
			$('#clientOrProviderModal').val(response.deliveryLocation.code + " - " + response.deliveryLocation.name);
		}
		$('#agreementModal').val(response.agreement.code + " - " + response.agreement.description);
		$('#deliveryNoteNumberModal').val(response.deliveryNoteNumber);
		$('#purchaseOrderNumberModal').val(response.purchaseOrderNumber);
		
		var found = false;
		var inputDetails = [];

		for (var i=0; i< response.inputDetails.length;i++) {
			found = false;
			for(var j=0;j<inputDetails.length;j++){
				if( response.inputDetails[i].product.id == inputDetails[j].id){
					inputDetails[j].amount += response.inputDetails[i].amount;
					found=true;
				}
			}
			if(!found){
				var inputDetail = {};
				inputDetail.id = response.inputDetails[i].product.id;
				inputDetail.amount = response.inputDetails[i].amount;
				inputDetail.description = response.inputDetails[i].product.description;
				if(response.inputDetails[i].serialNumber != null){
					inputDetail.serialNumber = response.inputDetails[i].serialNumber;
				}else{
					inputDetail.serialNumber = "";
				}
				inputDetail.batch = response.inputDetails[i].batch;
				inputDetail.expirationDate = myParseDate(response.inputDetails[i].expirationDate)
				inputDetails.push(inputDetail);
			}
		}
		
		var tableRow;
		
		for (var i=0; i< inputDetails.length;i++) {
			tableRow = "<tr><td>" +  inputDetails[i].id + " - " + inputDetails[i].description + "</td>" +
			"<td>" + inputDetails[i].amount + "</td>" +
			"<td>" + inputDetails[i].serialNumber + "</td>" +
			"<td>" + inputDetails[i].batch + "</td>" +
			"<td>" + inputDetails[i].expirationDate + "</td>" +
			+"</tr>";
			$("#productTableBody").append(tableRow);
		}
		
		$('#inputModal').modal('show');
	};

	
	showDeliveryNoteModal = function(deliveryNoteId) {
		$.ajax({
			url: "getDeliveryNote.do",
			type: "GET",
			async: false,
			data: {
                deliveryNoteNumber: deliveryNoteId
			},
			success: function(response) {
				populateDeliveryNoteModal(response);
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myGenericError();
			}
		});
	};
	
	var populateDeliveryNoteModal = function(response) {
		$('#productTableBodyDeliveryNote').empty();
		$("#deliveryNoteId").text("Numero: " + response.number);
		
		$('#dateDeliveryNoteModal').val(response.date);
		$('#clientOrProviderDeliveryNoteModal').val(response.deliveryLocation);
		$('#agreementDeliveryNoteModal').val(response.agreement);
		
		if(response.transactionCodeANMAT != null){
			$("#ANMATCode").show();
			$("#transactionCode").text(response.transactionCodeANMAT);
		}else{
			$("#ANMATCode").hide();
			$("#transactionCode").text("");
		}

		if(response.cancelled){
			$("#cancelled").text("ANULADO");
		}else{
			$("#cancelled").text("");
		}
		
		var found = false;
		var tableRow;
		
		for (var i=0; i< response.orderOutputDetails.length;i++) {
			var serialNumber = " ";
			if(response.orderOutputDetails[i].serialNumber != null){
				serialNumber = response.orderOutputDetails[i].serialNumber;
			}
			tableRow = "<tr><td>" +  response.orderOutputDetails[i].product + "</td>"+
				"<td>" +  response.orderOutputDetails[i].amount + "</td>" +
				"<td>"  + serialNumber + "</td>" + 
				"<td>"  + response.orderOutputDetails[i].batch + "</td>" +
				"<td>"  + response.orderOutputDetails[i].expirationDate + "</td>"+
				"</tr>";
			$("#productTableBodyDeliveryNote").append(tableRow);
		}
		
		$('#deliveryNoteModal').modal('show');
	};
	
	showOutputModal = function(outputId) {
		$.ajax({
			url: "getOutput.do",
			type: "GET",
			async: false,
			data: {
				outputId: outputId,
			},
			success: function(response) {
				populateOutputModal(response);
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myGenericError();
			}
		});
	};
	
	var populateOutputModal = function(response) {
		$('#productOutputTableBody').empty();
		
		$("#outputId").text("Numero: " + response.id);
		if(response.cancelled){
			$("#cancelled").text("ANULADO");
		}else{
			$("#cancelled").text("");
		}
		
		if(response.transactionCodeANMAT != null){
			$("#ANMATCode").show();
			$("#transactionCode").text(response.transactionCodeANMAT);
		}else{
			$("#ANMATCode").hide();
			$("#transactionCode").text("");
		}
		
		$('#dateModalOutput').val(myParseDate(response.date));
		$('#conceptModalOutput').val(response.concept.code + " - " + response.concept.description);
		if(response.provider != null){
			$('#clientOrProviderModalOutput').val(response.provider.code + " - " + response.provider.name);
		}
		if(response.deliveryLocation != null){
			$('#clientOrProviderModalOutput').val(response.deliveryLocation.code + " - " + response.deliveryLocation.name);
		}
		$('#agreementModalOutput').val(response.agreement.code + " - " + response.agreement.description);
		
		var found = false;
		var tableRow;
		
		for (var i=0; i< response.outputDetails.length;i++) {
			var serialNumber = " ";
			if(response.outputDetails[i].serialNumber != null){
				serialNumber = response.outputDetails[i].serialNumber;
			}
			tableRow = "<tr><td>" +  response.outputDetails[i].product.code + " - " + response.outputDetails[i].product.description + "</td>"+
				"<td>" +  response.outputDetails[i].amount + "</td>" +
				"<td>"  + serialNumber + "</td>" + 
				"<td>"  + response.outputDetails[i].batch + "</td>" +
				"<td>"  + myParseDate(response.outputDetails[i].expirationDate) + "</td>"+
				"</tr>";
			$("#productOutputTableBody").append(tableRow);
		}
		
		$('#outputModal').modal('show');
	};
	
	showOrderModal = function(orderId) {
		$.ajax({
			url: "getOrder.do",
			type: "GET",
			async: false,
			data: {
				orderId: orderId,
			},
			success: function(response) {
				populateOrderModal(response);
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myGenericError();
			}
		});
	};
	
	var populateOrderModal = function(response) {
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
		
		for (var i=0; i< response.orderDetails.length;i++) {
			var serialNumber = "";
			if(response.orderDetails[i].serialNumber != null){
				serialNumber = response.orderDetails[i].serialNumber;
			}
			tableRow = "<tr><td>" +  response.orderDetails[i].product.code + " - " 
			+ response.orderDetails[i].product.description + "</td>" +
			"<td>"+  response.orderDetails[i].amount + "</td>" +
			"<td>"+  serialNumber + "</td>" +
			"<td>"+  response.orderDetails[i].batch + "</td>" +
			"<td>"+  myParseDate(response.orderDetails[i].expirationDate) + "</td>" +
			"</tr>";
			$("#orderModalProductTableBody").append(tableRow);
		}
		
		$('#orderModal').modal('show');
	};
	
	showProvisioningRequestModal = function(provisioningId) {
		$.ajax({
			url: "getProvisioningRequest.do",
			type: "GET",
			async: false,
			data: {
				provisioningId: provisioningId,
			},
			success: function(response) {
				populateProvisioningModal(response);
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myGenericError();
			}
		});
	};
	
	var populateProvisioningModal = function(response) {
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
		
		for (var i=0; i< response.provisioningRequestDetails.length;i++) {
			tableRow = "<tr><td>" +  response.provisioningRequestDetails[i].product.code + " - " + response.provisioningRequestDetails[i].product.description + "</td><td>" +  response.provisioningRequestDetails[i].amount + "</td></tr>";
			$("#productTableBodyProvisioningRequest").append(tableRow);
		}
		
		$('#provisioningModal').modal('show');
	};
	
	
	showSupplyingModal = function(supplyingId) {
		$.ajax({
			url: "getSupplying.do",
			type: "GET",
			async: false,
			data: {
				supplyingId: supplyingId,
			},
			success: function(response) {
				populateSupplyingModal(response);
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myGenericError();
			}
		});
	};
	
	var populateSupplyingModal = function(response) {
		$('#supplyingModalProductTableBody').empty();
		
		
		$('#supplyingModalAgreementInput').val(response.agreement.code + " - " + response.agreement.description);
		$('#supplyingModalAffiliateInput').val(response.affiliate.code + " - " + response.affiliate.surname + " " + response.affiliate.name);
		$('#supplyingModalClientInput').val(response.client.code + " - " + response.client.name);
		
		var tableRow;
		
		for (var i=0; i< response.supplyingDetails.length;i++) {
			var serialNumber = "";
			if(response.supplyingDetails[i].serialNumber != null){
				serialNumber = response.supplyingDetails[i].serialNumber;
			}
			tableRow = "<tr><td>" +  response.supplyingDetails[i].product.code + " - " 
			+ response.supplyingDetails[i].product.description + "</td>" +
			"<td>"+  response.supplyingDetails[i].amount + "</td>" +
			"<td>"+  serialNumber + "</td>" +
			"<td>"+  response.supplyingDetails[i].batch + "</td>" +
			"<td>"+  myParseDate(response.supplyingDetails[i].expirationDate) + "</td>" +
			"</tr>";
			$("#supplyingModalProductTableBody").append(tableRow);
		}
		
		$('#supplyingModal').modal('show');
	};
	
});