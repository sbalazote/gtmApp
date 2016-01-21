PickingSheet = function() {
	
	var provisioningId = null;
	var provisioningsToPrint = [];

	$("#idSearch").numeric();
	
	$('#provisioningTableBody').on("click", ".view-row", function() {
		var parent = $(this).parent().parent();
	
		provisioningId = parent.attr("data-row-id");
		
		showProvisioningRequestModal(provisioningId);
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
		$("#provisioningTable").bootgrid("clear");
	});

	$("#confirmButton").click(function() {
		if (provisioningsToPrint.length > 0) {
			generatePickingSheetPDF(_.uniq(_.pluck(_.sortBy(provisioningsToPrint, 'deliveryLocation'), 'id')));
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
				provisioningRequestId: $('#idSearch').val() || null,
				agreementId: $("#agreementSearch").val() || null,
				logisticsOperatorId: $('#logisticsOperatorSearch').val() || null,
				clientId: $("#clientSearch").val() || null,
				deliveryLocationId: $('#deliveryLocationSearch').val() || null
			},
			success: function(response) {
				var aaData = [];
				for (var i = 0, l = response.length; i < l; ++i) {
					var orderDetail = {
						id: 0,
						client: "",
						deliveryLocation: "",
						agreement: "",
						state: "",
						date: "",
						action: ""
					};
					
					orderDetail.id = response[i].id;
					orderDetail.client = response[i].client.name;
					orderDetail.deliveryLocation = response[i].deliveryLocation.name;
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
					rowSelect: false,
					keepSelection: true
				}).on("selected.rs.jquery.bootgrid", function(e, rows) {
					for (var i = 0; i < rows.length; i++) {
						//provisioningsToPrint.push(rows[i].id);
						var elem = {
							id: rows[i].id,
							deliveryLocation: rows[i].deliveryLocation
						};
						provisioningsToPrint.push(elem);
					}
				}).on("deselected.rs.jquery.bootgrid", function(e, rows) {
					for (var i = 0; i < rows.length; i++) {
						//provisioningsToPrint.splice(provisioningsToPrint.indexOf(rows[i].id), 1);
						provisioningsToPrint = _.reject(provisioningsToPrint, function(obj) {
							return obj.id === rows[i].id;
						});
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