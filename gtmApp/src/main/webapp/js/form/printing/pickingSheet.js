PickingSheet = function() {
	
	var provisioningId = null;
	var provisioningsToPrint = [];
	
	refreshTable();
	window.setInterval(refreshTable, 60000);
	
	$('#provisioningTableBody').on("click", ".view-row", function() {
		var parent = $(this).parent().parent();
	
		provisioningId = parent.attr("data-row-id");
		
		showProvisioningRequestModal(provisioningId);
	});
	
	$("#searchButton").click(function() {
		refreshTable();
	});
	
	$("#cleanButton").click(function() {
		$('#clientSearch').val('').trigger('chosen:updated');
		$('#agreementSearch').val('').trigger('chosen:updated');
		refreshTable();
	});
	
	$("#confirmButton").click(function() {
		if (_.uniq(provisioningsToPrint).length > 0) {
			generatePickingSheetPDF(_.uniq(provisioningsToPrint));
		} else {
			myShowAlert('info', 'Seleccione al menos una Hoja de Picking');
		}
	});
	
	function refreshTable() {
		$.ajax({
			url: "getAuthorizedProvisioningsForOrders.do",
			type: "GET",
			async: false,
			data: {
				provisioningRequestId: null,
				agreementId: $("#agreementSearch").val() || null,
				clientId: $("#clientSearch").val() || null
				},
			success: function(response) {
				var aaData = [];
				for (var i = 0, l = response.length; i < l; ++i) {
					var orderDetail = {
						id: 0,
						client: "",
						agreement: "",
						state: "",
						date: "",
						action: ""
					};
					
					orderDetail.id = response[i].id;
					orderDetail.client = response[i].client.name;
					orderDetail.agreement = response[i].agreement.description;
					orderDetail.state =  response[i].state.description;
					orderDetail.date = myParseDate(response[i].deliveryDate);
					orderDetail.action = "<button type=\"button\" class=\"btn btn-sm btn-default view-row\"><span class=\"glyphicon glyphicon-eye-open\"></span> Detalle</button>";

					aaData.push(orderDetail);
				}
				
				$("#provisioningTable").bootgrid({
					caseSensitive: false,
					selection: true,
					multiSelect: true,
					keepSelection: true
				}).on("selected.rs.jquery.bootgrid", function(e, rows) {
					for (var i = 0; i < rows.length; i++) {
						provisioningsToPrint.push(rows[i].id);
					}
				}).on("deselected.rs.jquery.bootgrid", function(e, rows) {
					for (var i = 0; i < rows.length; i++) {
						provisioningsToPrint.splice(provisioningsToPrint.indexOf(rows[i].id), 1);
					}
				});
				$("#provisioningTable").bootgrid("clear");
				$("#provisioningTable").bootgrid("append", aaData);
				$("#provisioningTable").bootgrid("search", $(".search-field").val());
			},
			error: function(response) {
			}
		});	
	}
};