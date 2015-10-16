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
		if(provisioningsToPrint.length > 0){
			generatePickingSheetPDF(provisioningsToPrint);
			/*$.ajax({
				url: "printPickingSheets.do",
				type: "POST",
				contentType: "application/json",
				data: JSON.stringify(provisioningsToPrint),
				async: true,
				beforeSend : function() {
					$.blockUI({ message: 'Espere un Momento por favor...' });
				},
				success: function(response) {
					myReload("success", "Se han mandado a cola de impresi\u00f3n las siguientes hojas de picking: " + provisioningsToPrint);
				},
				error: function(jqXHR, textStatus, errorThrown) {
					myGenericError();
				},
				complete: function(jqXHR, textStatus) {
					$.unblockUI();
					if (textStatus === 'success') {
						generateInputPDFReport(jqXHR.responseJSON.id,false);
					}
				}
			});*/
		}else{
			myShowAlert('info', 'Seleccione al menos una Hoja de Picking');

			/*BootstrapDialog.show({
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
			 });*/
		}
	});
	
	function refreshTable() {
		$.ajax({
			url: "getAuthorizedProvisioningsForOrders.do",
			type: "GET",
			async: false,
			data: {
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
						provisioningsToPrint.push(rows[i].id);
					}
				}).on("deselected.rs.jquery.bootgrid", function(e, rows) {
					for (var i = 0; i < rows.length; i++) {
						provisioningsToPrint.splice(provisioningsToPrint.indexOf(rows[i].id), 1);
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