ProvisioningRequestAuthorization = function() {
	
	var provisioningId = null;
	var requestsToApprove = [];
	
	$('#provisioningTableBody').on("click", ".view-row", function() {
		var parent = $(this).parent().parent();
	
		provisioningId = parent.attr("data-row-id");
		
		showProvisioningRequestModal(provisioningId);
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
		if(requestsToApprove.length > 0){
			$.ajax({
				url: "authorizeProvisioningRequests.do",
				type: "POST",
				contentType: "application/json",
				data: JSON.stringify(requestsToApprove),
				async: false,
				success: function(response) {
					myReload("success", "Se han autorizado las siguientes solicitudes: " + requestsToApprove);
				},
				error: function(jqXHR, textStatus, errorThrown) {
					myGenericError();
				}
			});
		}else{
			myShowAlert('info', 'Seleccione al menos una Solicitud de Abastecimiento para AUTORIZAR');
		}
	});
	
	function refreshTable() {
		$.ajax({
			url: "getProvisioningsToAuthorize.do",
			type: "GET",
			async: false,
			data: {
				agreementId: $("#agreementSearch").val() || null,
				clientId: $("#clientSearch").val() || null,
				},
			success: function(response) {
				var aaData = [];
				for (var i = 0, l = response.length; i < l; ++i) {
					var orderDetail = {
							id: 0,
							client: "",
							agreement: "",
							date: "",
							action: ""
						};
						
						orderDetail.id = response[i].id;
						orderDetail.client = response[i].client.name;
						orderDetail.agreement = response[i].agreement.description;
						orderDetail.date = myParseDate(response[i].deliveryDate);
						orderDetail.action = "<a href='javascript:void(0);' class='view-row'>Consultar</a>";
						
						aaData.push(orderDetail);
					}
					
					$("#provisioningTable").bootgrid({
						caseSensitive: false,
						selection: true,
						multiSelect: true,
						rowSelect: false,
						keepSelection: true
					}).on("selected.rs.jquery.bootgrid", function(e, rows) {
						for (var i = 0; i < rows.length; i++) {
							requestsToApprove.push(rows[i].id);
						}
					}).on("deselected.rs.jquery.bootgrid", function(e, rows) {
						for (var i = 0; i < rows.length; i++) {
							for(var j = requestsToApprove.length - 1; j >= 0; j--) {
							    if(requestsToApprove[j] === rows[i].id) {
							    	requestsToApprove.splice(j, 1);
							    }
							}
						}
					});
					$("#provisioningTable").bootgrid().bootgrid("clear");
					$("#provisioningTable").bootgrid().bootgrid("append", aaData);
					$("#provisioningTable").bootgrid().bootgrid("search", $(".search-field").val());
			},
			error: function(response) {
			}
		});	
	}
};