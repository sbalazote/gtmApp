var LogisticOperatorAssignment = function() {

	var orderId = null;
	var provisioningsToAssign = [];

	$("#idSearch").numeric();

	$('#logisticsOperatorTableBody').on("click", ".view-row", function() {
		var parent = $(this).parent().parent();

		orderId = parent.attr("data-row-id");

		showOrderModal(orderId);
	});

	$("#searchButton").click(function() {
		refreshTable();
	});

	$("#cleanButton").click(function() {
		$('#idSearch').val('').trigger('chosen:updated');
		$('#logisticsOperatorSearch').val('').trigger('chosen:updated');
		$('#clientSearch').val('').trigger('chosen:updated');
		$('#deliveryLocationSearch').val('').trigger('chosen:updated');
		$('#agreementSearch').val('').trigger('chosen:updated');
		refreshTable();
	});

	$("#confirmButton").click(function() {

		var jsonAssignOperator = {
			"provisioningsIdsToReassign": _.uniq(provisioningsToAssign),
			"logisticOperatorId": $("#logisticsOperatorInput").val(),
		};

		if(_.uniq(provisioningsToAssign).length > 0 && $("#logisticsOperatorInput").val() != ""){
			$.ajax({
				url: "assignOperatorToProvisionings.do",
				type: "POST",
				contentType: "application/json",
				data: JSON.stringify(jsonAssignOperator),
				async: false,
				success: function(response) {
					myReload("success", "Se han reasignado el operador logistico para los siguientes pedidos: " + response);
				},
				error: function(jqXHR, textStatus, errorThrown) {
					myGenericError();
				}
			});
		}else{
			if($("#logisticsOperatorInput").val() == ""){
				myShowAlert('danger', 'Por favor, seleccione un Operador Logistico.');
			}else{
				myShowAlert('info', 'Seleccione al menos un Pedido');
			}
		}
	});

	function refreshTable() {
		$.ajax({
			url: "getOrdersToPrint.do",
			type: "GET",
			async: false,
			data: {
				provisioningRequestId: $('#idSearch').val() || null,
				agreementId: $("#agreementSearch").val() || null,
				logisticsOperatorId: $('#logisticsOperatorSearch').val() || null,
				clientId: $("#clientSearch").val() || null,
				deliveryLocationId: $('#deliveryLocationSearch').val() || null
			},
			success: function(response) {
				var aaData = [];
				for (var i = 0, l = response.length; i < l; ++i) {
					var orderDetail= {
						id: 0,
						client: "",
						agreement: "",
						logisticsOperator: "",
						action: ""
					};
					orderDetail.orderId = response[i].id;
					orderDetail.id = response[i].provisioningRequest.id;
					orderDetail.client = response[i].provisioningRequest.client.name;
					orderDetail.agreement = response[i].provisioningRequest.agreement.description;
					if (response[i].provisioningRequest.logisticsOperator != null) {
						orderDetail.logisticsOperator = response[i].provisioningRequest.logisticsOperator.name;
					} else {
						orderDetail.logisticsOperator = "NO INFORMADO";
					}
					orderDetail.action = "<button type=\"button\" class=\"btn btn-sm btn-default view-row\"><span class=\"glyphicon glyphicon-eye-open\"></span> Detalle</button>";

					aaData.push(orderDetail);
				}

				$("#logisticsOperatorTable").bootgrid({
					caseSensitive: false,
					selection: true,
					multiSelect: true,
					rowSelect: false,
					keepSelection: true
				}).on("selected.rs.jquery.bootgrid", function(e, rows) {
					for (var i = 0; i < rows.length; i++) {
						provisioningsToAssign.push(rows[i].id);
					}
				}).on("deselected.rs.jquery.bootgrid", function(e, rows) {
					for (var i = 0; i < rows.length; i++) {
						for(var j = provisioningsToAssign.length - 1; j >= 0; j--) {
							if(provisioningsToAssign[j] === rows[i].id) {
								provisioningsToAssign.splice(j, 1);
							}
						}
					}
				});
				$("#logisticsOperatorTable").bootgrid("clear");
				$("#logisticsOperatorTable").bootgrid("append", aaData);
				$("#logisticsOperatorTable").bootgrid("search", $(".search-field").val());
			},
			error: function(response) {
			}
		});
	}
};