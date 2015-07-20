var DeliveryNoteSheet = function() {
	
	var orderId = null;
	var ordersToPrint = [];
	
	$('#deliveryNoteTableBody').on("click", ".view-row", function() {
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
		if(ordersToPrint.length > 0){
			$.ajax({
				url: "printDeliveryNotes.do",
				type: "POST",
				contentType: "application/json",
				data: JSON.stringify(ordersToPrint),
				async: false,
				success: function(response) {
					myReload("success", "Se han Impreso los siguientes remitos: " + response);
				},
				error: function(jqXHR, textStatus, errorThrown) {
					myGenericError();
				}
			});
		}else{
			BootstrapDialog.show({
				type: BootstrapDialog.TYPE_INFO,
		        title: 'Atenci\u00f3n',
		        message: "Seleccione al menos un elemento",
				closable: false,
		        buttons: [{
	                label: 'Cerrar',
	                action: function(dialogItself){
	                    dialogItself.close();
	                }
	            }]
			});
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
					var orderDetail = {
						id: 0,
						agreement: "",
						client: "",
						option:""
					};
					
					orderDetail.id = response[i].id;
					orderDetail.agreement = response[i].provisioningRequest.agreement.description;
					orderDetail.client = response[i].provisioningRequest.client.name;
					orderDetail.option = "<a href='javascript:void(0);' class='view-row'>Consultar</a>";
					
					aaData.push(orderDetail);
				}
				
				$("#deliveryNoteTable").bootgrid({
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
						ordersToPrint.splice(ordersToPrint.indexOf(rows[i].id), 1);
					}
				});
				$("#deliveryNoteTable").bootgrid().bootgrid("clear");
				$("#deliveryNoteTable").bootgrid().bootgrid("append", aaData);
				$("#deliveryNoteTable").bootgrid().bootgrid("search", $(".search-field").val());
			},
			error: function(response) {
			}
		});	
	}
};