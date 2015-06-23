var PendingTransactions = function() {
	
	var orderId = null;
	var outputId = null;
	var deliveryNotes = [];
	var ordersToReassembly = [];
	var outputsToReassembly = [];
	var outputs = [];
	
	$('#deliveryNoteTableBody').on("click", ".view-order-row-deliveryNoteTable", function() {
		var parent = $(this).parent().parent();
		orderId = parent.children().eq(2).text();
		showOrderModal(orderId);
	});
	
	$('#deliveryNoteTableBody').on("click", ".view-output-row-deliveryNoteTable", function() {
		var parent = $(this).parent().parent();
		outputId = parent.children().eq(2).text();
		showOutputModal(outputId);
	});
	
	$('#deliveryNoteTableBody').on("click", ".view-supplying-row-deliveryNoteTable", function() {
		var parent = $(this).parent().parent();
		supplyingId = parent.children().eq(2).text();
		showSupplyingModal(supplyingId);
	});
	
	$("#confirmDeliveryNotesButton").click(function() {
		if(deliveryNotes.length > 0 ){
			$.ajax({
				url: "confirmPendingDeliveryNotes.do",
				type: "POST",
				contentType: "application/json",
				data: JSON.stringify(deliveryNotes),
				async: true,
	            beforeSend : function() {
	            	$.blockUI({ message: 'Espere un Momento por favor...' });
	             },
				success: function(response) {
					if(response.length > 0 && response[0] != null){
						for (var i = 0, lengthI = response.length; i < lengthI; i++) {
							if(response[i].resultado == true){
								$.unblockUI();
								myShowAlert("success", "Se han informado los siguientes remitos: " +  response[i].operationId, null);
							}else{
								$.unblockUI();
								var errors = "";
								for (var j = 0, lengthJ = response[i].myOwnErrors.length; j < lengthJ; j++) {
									errors += response[i].myOwnErrors[j] + "<br />";
								}

								if(response[i].errores != null){
									errors += "<strong>Errores informados por ANMAT para egreso numero" + response[i].operationId + "</strong><br />";
									for (var j = 0, lengthJ = response[i].errores.length; j < lengthJ; j++) {
										errors += response[i].errores[j]._c_error + " - " + response[i].errores[j]._d_error + "<br />";
									}
								}
								myShowAlert("danger", errors,null);
							}
						}
					}
				},
				error: function(jqXHR, textStatus, errorThrown) {
					$.unblockUI();
					myGenericError();
				}
			});
		}else{
			BootstrapDialog.show({
				type: BootstrapDialog.TYPE_INFO,
		        title: 'Atenci\u00f3n',
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
	
//	$("#confirmOutputsButton").click(function() {
//		if(outputs.length > 0 ){
//			$.ajax({
//				url: "confirmPendingOutputs.do",
//				type: "POST",
//				contentType: "application/json",
//				data: JSON.stringify(outputs),
//				async: true,
//	            beforeSend : function() {
//	            	$.blockUI({ message: 'Espere un Momento por favor...' });
//	             }, 
//				success: function(response) {
//					if(response.length > 0 && response[0] != null){
//						for (var i = 0, lengthI = response.length; i < lengthI; i++) {
//							if(response[i].resultado == true){
//								$.unblockUI();
//								myShowAlert("success", "Se han informado los siguientes Egresos: " +  response[i].operationId, null);
//							}else{
//								$.unblockUI();
//								var errors = "";
//								for (var j = 0, lengthJ = response[i].myOwnErrors.length; j < lengthJ; j++) {
//									errors += response[i].myOwnErrors[j] + "<br />";
//								}
//								
//								if(response.errores != null){
//									errors += "<strong>Errores informados por ANMAT para egreso n�" + response[i].operationId + "</strong><br />";
//									for (var j = 0, lengthJ = response[i].errores.length; j < lengthJ; j++) {
//										errors += response[i].errores[j]._c_error + " - " + response[i].errores[j]._d_error + "<br />";
//									}
//								}
//								myShowAlert("danger", errors,null);
//							}
//						}
//					}
//				},
//				error: function(response, jqXHR, textStatus, errorThrown) {
//					$.unblockUI();
//					myGenericError();
//				}
//			});
//		}else{
//			BootstrapDialog.show({
//				type: BootstrapDialog.TYPE_INFO,
//		        title: 'Atenci�n',
//		        message: "Seleccione al menos un elemento",
//		        buttons: [{
//	                label: 'Cerrar',
//	                action: function(dialogItself){
//	                    dialogItself.close();
//	                }
//	            }]
//			});
//		}
//	});
	
	$("#deliveryNoteTable").bootgrid({
	    selection: true,
	    multiSelect: true,
	    rowSelect: true,
	    keepSelection: true,
	    formatters: {
	        "option": function(column, row)
	        {
	        	if (row.class === "ARMADO") {
	        		return "<a href=\"#\" class='view-order-row-deliveryNoteTable'>Consulta</a>";
	        	}
	        	if(row.class === "EGRESO") {
	        		return "<a href=\"#\" class='view-output-row-deliveryNoteTable'>Consulta</a>";
	        	}
	        	if(row.class === "DISPENSA") {
	        		return "<a href=\"#\" class='view-supplying-row-deliveryNoteTable'>Consulta</a>";
	        	}
	        }
	    }
	}).on("selected.rs.jquery.bootgrid", function(e, rows)
	{
	    for (var i = 0; i < rows.length; i++)
	    {
            var firstSplit = rows[i].deliveryNoteNumbers.replace(/\s+/g, '').split("[");
            var secondSplit = firstSplit[1].split("]");
            var deliveryNotesNumbers = secondSplit[0].split(",");
            var ids = deliveryNotesNumbers;
	    	if (rows[i].class === "ARMADO") {
	    		var orderId = rows[i].orderAssemblyOrOutputNumber;
	    		ordersToReassembly.push(orderId);
	    		for (var j = 0; j < ids.length; j++) {
	    			deliveryNotes.push(ids[j]);
	    		}
	    	} else {
	    		var outputId = rows[i].orderAssemblyOrOutputNumber;
	    		outputsToReassembly.push(outputId);
	    		for (var j = 0; j < ids.length; j++) {
	    			deliveryNotes.push(ids[j]);
	    		}
	    	}
	    }
	}).on("deselected.rs.jquery.bootgrid", function(e, rows)
	{
	    for (var i = 0; i < rows.length; i++)
	    {
            var firstSplit = rows[i].deliveryNoteNumbers.replace(/\s+/g, '').split("[");
            var secondSplit = firstSplit[1].split("]");
            var deliveryNotesNumbers = secondSplit[0].split(",");
            var ids = deliveryNotesNumbers;
	    	if (rows[i].class === "ARMADO") {
	    		var orderId = rows[i].orderAssemblyOrOutputNumber;
		    	ordersToReassembly.splice(ordersToReassembly.indexOf(orderId), 1);
				for (var j = 0; j < ids.length; j++) {
					deliveryNotes.splice(deliveryNotes.indexOf(ids[j]), 1);
				}
		    } else {
		    	var outputId = rows[i].orderAssemblyOrOutputNumber;
		    	outputsToReassembly.splice(ordersToReassembly.indexOf(outputId), 1);
				for (var j = 0; j < ids.length; j++) {
					deliveryNotes.splice(deliveryNotes.indexOf(ids[j]), 1);
				}
		    }
	    }
	});
	
//	$("#outputTable").bootgrid({
//	    selection: true,
//	    multiSelect: true,
//	    rowSelect: true,
//	    keepSelection: true,
//	    formatters: {
//	        "option": function(column, row)
//	        {
//	        	return "<a href=\"#\" class='view-output-row-outputTable'>Consulta</a>";
//	        }
//	    }
//	}).on("selected.rs.jquery.bootgrid", function(e, rows)
//	{
//	    for (var i = 0; i < rows.length; i++)
//	    {
//	    	outputs.push(rows[i].output);
//	    }
//	}).on("deselected.rs.jquery.bootgrid", function(e, rows)
//	{
//	    for (var i = 0; i < rows.length; i++)
//	    {
//	    	for(var i = outputs.length - 1; i >= 0; i--) {
//			    if(outputs[i] === rows[i].output) {
//			    	outputs.splice(i, 1);
//			    }
//			}
//	    }
//	});
//	
//	$("#confirmOutputWithoutInform").click(function() {
//		$('#forcedOutputConfirmationModal').modal();
//	});
//	
//	$("#confirmOutputsWithoutInformModal").click(function() {
//		if(outputs.length > 0 ){
//			$.ajax({
//				url: "authorizeOutputWithoutInform.do",
//				type: "POST",
//				contentType: "application/json",
//				data: JSON.stringify(outputs),
//				async: true,
//	            beforeSend : function() {
//	            	$.blockUI({ message: 'Espere un Momento por favor...' });
//	             }, 
//				success: function(response) {
//					myReload("success", "Se han cerrado los siguientes egresos informados oportunamente: " + response);
//				},
//				error: function(response, jqXHR, textStatus, errorThrown) {
//					$.unblockUI();
//					myGenericError();
//				}
//			});
//		}else{
//			BootstrapDialog.show({
//				type: BootstrapDialog.TYPE_INFO,
//		        title: 'Atenci�n',
//		        message: "Seleccione al menos un elemento",
//		        buttons: [{
//	                label: 'Cerrar',
//	                action: function(dialogItself){
//	                    dialogItself.close();
//	                }
//	            }]
//			});
//		}
//	});
//	
	$("#confirmDeliveryNotesWithoutInform").click(function() {
		$('#forcedDeliveryNoteConfirmationModal').modal();
	});
	
	$("#confirmDeliveryNotesWithoutInformModal").click(function() {
		if(deliveryNotes.length > 0 ){
			$.ajax({
				url: "authorizeDeliveryNotesWithoutInform.do",
				type: "POST",
				contentType: "application/json",
				data: JSON.stringify(deliveryNotes),
				async: true,
	            beforeSend : function() {
	            	$.blockUI({ message: 'Espere un Momento por favor...' });
	             }, 
				success: function(response) {
					myReload("success", "Se han cerrado los siguientes remitos informados oportunamente: " + response);
				},
				error: function(response, jqXHR, textStatus, errorThrown) {
					$.unblockUI();
					myGenericError();
				}
			});
		}else{
			BootstrapDialog.show({
				type: BootstrapDialog.TYPE_INFO,
		        title: 'Atenci\u00f3n',
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
	
	
};