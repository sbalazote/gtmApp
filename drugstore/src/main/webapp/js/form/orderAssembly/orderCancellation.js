var OrderCancellation = function() {
	
	var orderId = null;
	var requestsToCancel = [];
	
	$('#orderTableBody').on("click", ".view-row", function() {
		var parent = $(this).parent().parent();
	
		orderId = parent.attr("data-row-id");
		
		showOrderModal(orderId);
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
	});
	
	$("#orderTable").bootgrid({
	    selection: true,
	    multiSelect: true,
	    rowSelect: true,
	    keepSelection: true,
	    formatters: {
	        "action": function(column, row) {
	        	return "<a href=\"#\" class='view-row'>Consulta</a>";
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