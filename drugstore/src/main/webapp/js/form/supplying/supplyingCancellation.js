var SupplyingCancellation = function() {
	
	var supplyingId = null;
	var supplyingsToCancel = [];
	
	$('#supplyingTableBody').on("click", ".view-row", function() {
		var parent = $(this).parent().parent();
	
		supplyingId = parent.attr("data-row-id");
		
		showSupplyingModal(supplyingId);
	});
	
	$("#confirmButton").click(function() {
		if(supplyingsToCancel.length > 0){
			$.ajax({
				url: "cancelSupplyings.do",
				type: "POST",
				contentType: "application/json",
				data: JSON.stringify(supplyingsToCancel),
				async: false,
				success: function(response) {
					myReload("success", "Se han anulado las siguientes dispensas: " + supplyingsToCancel);
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
	
	$("#supplyingTable").bootgrid({
	    selection: true,
	    multiSelect: true,
	    rowSelect: false,
	    keepSelection: true,
	    formatters: {
	        "action": function(column, row) {
	        	return "<a href=\"#\" class='view-row'>Consulta</a>";
	        }
	    }
	}).on("selected.rs.jquery.bootgrid", function(e, rows) {
	    for (var i = 0; i < rows.length; i++) {
	    	supplyingsToCancel.push(rows[i].id);
	    }
	}).on("deselected.rs.jquery.bootgrid", function(e, rows) {
	    for (var i = 0; i < rows.length; i++) {
	    	for(var j = supplyingsToCancel.length - 1; j >= 0; j--) {
			    if(supplyingsToCancel[j] === rows[i].id) {
			    	supplyingsToCancel.splice(j, 1);
			    }
			}
	    }
	});
};