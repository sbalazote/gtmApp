var OutputCancellation = function() {
	
	var outputId = null;
	var outputsToCancel = [];
	
	$('#outputTableBody').on("click", ".view-row", function() {
		var parent = $(this).parent().parent();
	
		outputId = parent.attr("data-row-id");
		
		showOutputModal(outputId);
	});
	
	$("#confirmButton").click(function() {
		if(outputsToCancel.length > 0){
			$.ajax({
				url: "cancelOutputs.do",
				type: "POST",
				contentType: "application/json",
				data: JSON.stringify(outputsToCancel),
				async: false,
				success: function(response) {
					myReload("success", "Se han anulado los siguientes egresos: " + outputsToCancel);
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
	
	$("#outputTable").bootgrid({
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
	    	outputsToCancel.push(rows[i].id);
	    }
	}).on("deselected.rs.jquery.bootgrid", function(e, rows) {
	    for (var i = 0; i < rows.length; i++) {
	    	for(var j = outputsToCancel.length - 1; j >= 0; j--) {
			    if(outputsToCancel[j] === rows[i].id) {
			    	outputsToCancel.splice(j, 1);
			    }
			}
	    }
	});
};