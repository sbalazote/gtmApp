var OrderManagement = function() {
	
	var orderId = null;
	var requestsToCancel = [];
	var provisioningToCancel = [];
	
	$('#orderTableBody').on("click", ".view-row", function() {
		var parent = $(this).parent().parent();
	
		orderId = parent.attr("data-row-id");
		
		showOrderModal(orderId);
	});

	$('#orderTableBody').on("click", ".print-row", function() {
		var parent = $(this).parent().parent();

		orderId = parent.attr("data-row-id");

		$.ajax({
			url: "printOrderLabel.do",
			type: "POST",
			async: true,
			data: {
				orderId: orderId
			},
			beforeSend : function() {
				$.blockUI({ message: 'Espere un Momento por favor...' });
			},
			success: function(response) {
				var msgType = "success";
				var message = "Se han mandado a imprimir las etiquetas para el siguiente pedido: <strong>" + response.operationId + "</strong>";
				if (response.errorMessages.length > 0) {
					$.each(response.errorMessages, function (index, value) {
						message += "<strong><p>" + value + "</p></strong>";
					});
					msgType = "warning";
				}
				if (response.successMessages.length > 0) {
					$.each(response.successMessages, function (index, value) {
						message += "<strong><p>" + value + "</p></strong>";
					});
				}
				myReload(msgType, message);
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myGenericError();
			},
			complete: function(jqXHR, textStatus) {
				$.unblockUI();
			}
		});
	});
	
	$("#confirmButton").click(function() {
		if(_.uniq(requestsToCancel).length > 0){
			$.ajax({
				url: "cancelOrders.do",
				type: "POST",
				contentType: "application/json",
				data: JSON.stringify(_.uniq(requestsToCancel)),
				async: false,
				success: function(response) {
					myReload("success", "Se han anulado los armados correspondientes a los siguientes pedidos: " + provisioningToCancel);
				},
				error: function(jqXHR, textStatus, errorThrown) {
					myGenericError();
				}
			});
		}else{
			myShowAlert('info', 'Seleccione al menos un Armado para ANULAR');
		}
	});

	$("#searchButton").click(function() {
		var provisioningRequestId = "";
		if ($("#provisioningRequestSearch").val() != "") {
			provisioningRequestId = $("#provisioningRequestSearch").val();
		}
		makeQuery(provisioningRequestId);
	});

	var makeQuery = function(provisioningRequestId) {
		$.ajax({
			url: "getPrintableOrCancelableOrder.do?isCancellation="+$("#cancellation").val(),
			type: "POST",
			async: false,
			data: {provisioningId: provisioningRequestId},
			success: function(response) {
				var aaData = [];
				$.each(response, function( index, value ) {
					var row = {
						"orderId": value.id,
						"id": value.provisioningRequest.id,
						"client": value.provisioningRequest.client.code + " " + value.provisioningRequest.client.name,
						"agreement": value.provisioningRequest.agreement.description,
					};
					aaData.push(row);
				});

				$("#orderTable").bootgrid({
					selection: true,
					multiSelect: true,
					rowSelect: false,
					keepSelection: true,
					formatters: {
						"viewOrder": function(column, row) {
							return "<button type=\"button\" class=\"btn btn-sm btn-default view-row\">" +
								"<span class=\"glyphicon glyphicon-eye-open\"></span>" +
								" Ver</button>";
						},
						"printLabel": function(column, row) {
							return "<button type=\"button\" class=\"btn btn-sm btn-default print-row\">" +
								"<span class=\"glyphicon glyphicon-print\"></span>" +
								" Imprimir Etiqueta</button>";
						}
					}
				}).on("selected.rs.jquery.bootgrid", function(e, rows) {
					for (var i = 0; i < rows.length; i++) {
						requestsToCancel.push(rows[i].orderId);
						provisioningToCancel.push(rows[i].id);
					}
				}).on("deselected.rs.jquery.bootgrid", function(e, rows) {
					for (var i = 0; i < rows.length; i++) {
						for(var j = requestsToCancel.length - 1; j >= 0; j--) {
							if(requestsToCancel[j] === rows[i].orderId) {
								requestsToCancel.splice(j, 1);
								provisioningToCancel.splice(j,1);
							}
						}
					}
				});

				$("#orderTable").bootgrid("clear");
				$("#orderTable").bootgrid("append", aaData);
				$("#orderTable").bootgrid("search", $(".search-field").val());
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myGenericError();
			}
		});
	};
	

};