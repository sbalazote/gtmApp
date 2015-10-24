var OrderCancellation = function() {
	
	var orderId = null;
	var requestsToCancel = [];
	
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
				myShowAlert('success', 'Se ha mandado a imprimir el rotulo de la orden nro.: ' + orderId);
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
		if(requestsToCancel.length > 0){
			$.ajax({
				url: "cancelOrders.do",
				type: "POST",
				contentType: "application/json",
				data: JSON.stringify(requestsToCancel),
				async: false,
				success: function(response) {
					myReload("success", "Se han anulado las siguientes solicitudes: " + requestsToCancel);
				},
				error: function(jqXHR, textStatus, errorThrown) {
					myGenericError();
				}
			});
		}else{
			myShowAlert('info', 'Seleccione al menos un Armado para ANULAR');
		}
	});
	
	$("#orderTable").bootgrid({
	    selection: true,
	    multiSelect: true,
	    rowSelect: false,
	    keepSelection: true,
	    formatters: {
			"action": function(column, row) {
				return "<button type=\"button\" class=\"btn btn-sm btn-default view-row\">" +
					"<span class=\"glyphicon glyphicon-eye-open\"></span>" +
					" Ver</button>  " +
					"<button type=\"button\" class=\"btn btn-sm btn-default print-row\">" +
					"<span class=\"glyphicon glyphicon-print\"></span>" +
					" Imprimir Etiqueta</button>";
			}
	    }
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
};