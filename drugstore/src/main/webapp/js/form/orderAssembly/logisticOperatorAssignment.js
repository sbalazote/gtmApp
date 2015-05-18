var LogisticOperatorAssignment = function() {
	
	var orderId = null;
	var ordersToPrint = [];
	
	$('#logisticsOperatorTableBody').on("click", ".view-row", function() {
		var parent = $(this).parent().parent();
	
		orderId = parent.attr("data-row-id");
		
		showOrderModal(orderId);
	});
	
	refreshTable();
	window.setInterval(refreshTable, 60000);
	
	$("#searchButton").click(function() {
		refreshTable();
	});
	
	$("#cleanButton").click(function() {
		$('#clientSearch').val('').trigger('chosen:updated');
		$('#agreementSearch').val('').trigger('chosen:updated');
		refreshTable();
	});
	
	$("#confirmButton").click(function() {
		
		var jsonInput = {
				"ordersIdsToReassign": ordersToPrint,
				"logisticOperatorId": $("#logisticsOperatorInput").val(),
			};
		
		if(ordersToPrint.length > 0 && $("#logisticsOperatorInput").val() != ""){
			$.ajax({
				url: "assignOperatorToOrders.do",
				type: "POST",
				contentType: "application/json",
				data: JSON.stringify(jsonInput),
				async: false,
				success: function(response) {
					myReload("success", "Se han reasignado el operador logistico para los siguientes armados: " + response);
				},
				error: function(jqXHR, textStatus, errorThrown) {
					myGenericError();
				}
			});
		}else{
			if($("#logisticsOperatorInput").val() == ""){
				myShowAlert('danger', 'Por favor, seleccione un operador logistico.');
			}else{
				BootstrapDialog.show({
					type: BootstrapDialog.TYPE_INFO,
			        title: 'Atenci&oacute;n',
			        message: "Seleccione al menos un elemento",
			        buttons: [{
		                label: 'Cerrar',
		                action: function(dialogItself){
		                    dialogItself.close();
		                }
		            }]
				});
			}
		}
	});
	
	function refreshTable() {
		$.ajax({
			url: "getOrdersToPrint.do",
			type: "GET",
			async: false,
			data: {
				agreementId: $("#agreementSearch").val() || null,
				clientId: $("#clientSearch").val() || null,
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
							
							orderDetail.id = response[i].id;
							orderDetail.client = response[i].provisioningRequest.client.name;
							orderDetail.agreement = response[i].provisioningRequest.agreement.description;
							orderDetail.logisticsOperator = response[i].provisioningRequest.logisticsOperator.name;
							orderDetail.action = "<a href='javascript:void(0);' class='view-row'>Consultar</a>";
					
					aaData.push(orderDetail);
				}

				$("#logisticsOperatorTable").bootgrid({
					caseSensitive: false,
					selection: true,
					multiSelect: true,
					rowSelect: true,
					keepSelection: true
				}).on("selected.rs.jquery.bootgrid", function(e, rows) {
					for (var i = 0; i < rows.length; i++) {
						ordersToPrint.push(rows[i].id);
					}
				}).on("deselected.rs.jquery.bootgrid", function(e, rows) {
					for (var i = 0; i < rows.length; i++) {
						for(var j = ordersToPrint.length - 1; j >= 0; j--) {
						    if(ordersToPrint[j] === rows[i].id) {
						    	ordersToPrint.splice(j, 1);
						    }
						}
					}
				});
				$("#logisticsOperatorTable").bootgrid().bootgrid("clear");
				$("#logisticsOperatorTable").bootgrid().bootgrid("append", aaData);
				$("#logisticsOperatorTable").bootgrid().bootgrid("search", $(".search-field").val());
			},
			error: function(response) {
			}
		});	
	}
};