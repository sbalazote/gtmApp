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
		if(ordersToPrint.length > 0) {
			$.ajax({
				url: "printDeliveryNotes.do",
				type: "POST",
				contentType: "application/json",
				data: JSON.stringify(ordersToPrint),
				async: false,
				success: function(response) {
					var message = "Se han Impreso los siguientes remitos: <p></p><strong>";
					$.each(response, function( index, value ) {
						message += "<p>" + value + "</p>";
					});
					message += "</strong>";
					myReload("success", message);
				},
				error: function(jqXHR, textStatus, errorThrown) {
					myGenericError();
				}
			});
		} else {
			myShowAlert('info', 'Seleccione al menos un Armado');
		}
	});
	
	function refreshTable() {
		$.ajax({
			url: "getOrdersToPrint.do",
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
						agreement: "",
						client: "",
						option:""
					};
					
					orderDetail.id = response[i].id;
					orderDetail.agreement = response[i].provisioningRequest.agreement.description;
					orderDetail.client = response[i].provisioningRequest.client.name;
					orderDetail.option = "<button type=\"button\" class=\"btn btn-sm btn-default view-row\"><span class=\"glyphicon glyphicon-eye-open\"></span> Detalle</button>";

					aaData.push(orderDetail);
				}
				
				$("#deliveryNoteTable").bootgrid({
					caseSensitive: false,
					selection: true,
					multiSelect: true,
					rowSelect: false,
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