var ProvisioningRequestCancellation = function() {
	
	var provisioningId = null;
	var requestsToCancel = [];
	
	$('#provisioningTableBody').on("click", ".view-row", function() {
		var parent = $(this).parent().parent();
	
		provisioningId = parent.attr("data-row-id");
		
		showProvisioningRequestModal(provisioningId);
	});
	
	$("#confirmButton").click(function() {
		if (requestsToCancel.length == 1) {
			$.ajax({
				url: "cancelProvisioningRequests.do",
				type: "POST",
				contentType: "application/json",
				data: JSON.stringify(requestsToCancel[0]),
				async: false,
				success: function(response) {
					if(response == true){
						myReload("success", "Se han anulado los siguientes pedidos: " + requestsToCancel);
					}else{
						myShowAlert('danger', 'No es posible anular el pedido dado el estado actual del mismo.');
					}

				},
				error: function(jqXHR, textStatus, errorThrown) {
					myGenericError();
				}
			});
		} else if (requestsToCancel.length > 1) {
			myShowAlert('info', 'Seleccione solo un Pedido');
		} else {
			myShowAlert('info', 'Seleccione al menos un Pedido');
		}
	});

	$("#searchButton").click(function() {
		var provisioningRequestId = "";
		if($("#provisioningRequestSearch").val() != ""){
			provisioningRequestId = $("#provisioningRequestSearch").val();
		}
		makeQuery(provisioningRequestId);
	});

	var makeQuery = function(provisioningRequestId) {
		$.ajax({
			url: "getProvisioningRequest.do",
			type: "GET",
			async: false,
			data: {provisioningId: provisioningRequestId},
			success: function(response) {
				var aaData = [];
				//$.each(response, function( index, value ) {
					var row = {
						"id": response.id,
						"client": response.client.code + " " + response.client.name,
						"agreement": response.agreement.code + " " + response.agreement.description,
						"state": response.state.description,
						"date": myParseDate(response.deliveryDate),
						"action": "<button type=\"button\" class=\"btn btn-sm btn-default view-row\"><span class=\"glyphicon glyphicon-eye-open\"></span> Detalle</button>"
					};
					aaData.push(row);
				//});

				$("#provisioningTable").bootgrid({
					selection: true,
					multiSelect: false,
					rowSelect: false,
					keepSelection: true,
				}).on("selected.rs.jquery.bootgrid", function(e, rows) {
					for (var i = 0; i < rows.length; i++) {
						requestsToCancel.push(rows[i].id);
					}
				}).on("deselected.rs.jquery.bootgrid", function(e, rows) {
					for (var i = 0; i < rows.length; i++) {
						for(var j = requestsToCancel.length - 1; j >= 0; j--) {
							if(requestsToCancel[j] === rows[i].id) {
								requestsToCancel.splice(j, 1);
							}
						}
					}
				});

				$("#provisioningTable").bootgrid().bootgrid("clear");
				$("#provisioningTable").bootgrid().bootgrid("append", aaData);
				$("#provisioningTable").bootgrid().bootgrid("search", $(".search-field").val());
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myGenericError();
			}
		});
	};
	

};