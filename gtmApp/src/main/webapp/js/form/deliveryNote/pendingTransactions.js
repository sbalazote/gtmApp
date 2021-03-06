var PendingTransactions = function() {
	
	var orderId = null;
	var outputId = null;
	var deliveryNotes = [];
	var ordersToReassembly = [];
	var outputsToReassembly = [];
	var map = {};
	
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
						var confirmedDeliveryNotes = [];
						var errorsList = [];
						for (var i = 0, lengthI = response.length; i < lengthI; i++) {
							if(response[i].resultado == true){
								$.unblockUI();
								var operation = {
									deliveryNoteNumber : response[i].operationId,
									transactionCode: response[i].codigoTransaccion
								}
								confirmedDeliveryNotes.push(operation);
							}else{
								$.unblockUI();
								var errors = "<strong>Remito " + response[i].operationId + " </strong>";

								for (var j = 0, lengthJ = response[i].myOwnErrors.length; j < lengthJ; j++) {
									errors += response[i].myOwnErrors[j] + "<br />";
								}

								if(response[i].errores != null){
									errors += "<strong>Errores informados por ANMAT para egreso numero" + response[i].operationId + "</strong><br />";
									for (var j = 0, lengthJ = response[i].errores.length; j < lengthJ; j++) {
										errors += response[i].errores[j]._c_error + " - " + response[i].errores[j]._d_error + "<br />";
									}
								}
								errorsList.push(errors);
							}
						}
						if(confirmedDeliveryNotes.length > 0 && errorsList.length == 0){
							var message = "Se han informado los siguientes remitos: <p></p><strong>";
							$.each(confirmedDeliveryNotes, function( index, value ) {
								message += "<p>" + value.deliveryNoteNumber +"  <span class='label label-success'> Trx: " + value.transactionCode +" </span></p>";
							});
							message += "</strong>";
							myReload("success", message);
						}else{
							if(errorsList.length > 0 && confirmedDeliveryNotes.length > 0){
								var message = "Se han informado los siguientes remitos:  <p></p><strong>";
								$.each(confirmedDeliveryNotes, function( index, value ) {
									message += "<p>" + value.deliveryNoteNumber +"  <span class='label label-success'> Trx: " + value.transactionCode +" </span></p>";
								});
								message += "</strong><br />" +"Pero ocurrieron los siguientes errores:" + "<br />";
								$.each(errorsList, function( index, value ) {
									message += "<p>" + value + "</p>";
								});
								myReload("success", message);
							}else{
								myReload("danger", "Han surgido los siguientes errores: " +  errorsList);
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
			myShowAlert('info', 'Seleccione al menos un Armado/Egreso/Dispensa');
		}
	});
	
	$("#deliveryNoteTable").bootgrid({
	    selection: true,
	    multiSelect: true,
	    rowSelect: false,
	    keepSelection: true,
		converters: {
			datetime: {
				from: function (value) {
					return moment(value);
				},
				to: function (value) {
					return value.format("DD/MM/YYYY HH:mm:ss a");
				}
			}
		},
	    formatters: {
	        "option": function(column, row)
	        {
	        	if (row.class === "ARMADO") {
					return "<button type=\"button\" class=\"btn btn-sm btn-default view-order-row-deliveryNoteTable\"><span class=\"glyphicon glyphicon-eye-open\"></span> Detalle</button>";
	        	}
	        	if(row.class === "EGRESO") {
					return "<button type=\"button\" class=\"btn btn-sm btn-default view-output-row-deliveryNoteTable\"><span class=\"glyphicon glyphicon-eye-open\"></span> Detalle</button>";
				}
	        	if(row.class === "DISPENSA") {
					return "<button type=\"button\" class=\"btn btn-sm btn-default view-supplying-row-deliveryNoteTable\"><span class=\"glyphicon glyphicon-eye-open\"></span> Detalle</button>";
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
	
	$("#confirmDeliveryNotesWithoutInform").click(function() {
		if(deliveryNotes.length > 0 ){
			for (var i = 0; i < deliveryNotes.length; i++){
				map[deliveryNotes[i]] = null;

				var aaData = [];
				var row = {
					deliveryNoteAlreadyInformId: deliveryNotes[i],
					deliveryNoteAlreadyInformTransactionCode: "<input type='text' class='form-control'>"
				};
				aaData.push(row);
				$("#deliveryNotesAlreadyInformsTable").bootgrid("append", aaData);
			}
			$('#forcedDeliveryNoteConfirmationModal').modal();
		}else{
			myShowAlert('info', 'Seleccione al menos un Remito');
		}
	});
	
	$("#confirmDeliveryNotesWithoutInformModal").click(function() {
		$('#deliveryNotesAlreadyInformsTable > tbody  > tr').each(function() {
			var deliveryNote = $(this).children().eq(0).text();
			var transactionCode = $(this).children().children().val();
			map[deliveryNote] = transactionCode;
		});

		$.ajax({
			url: "authorizeDeliveryNotesWithoutInform.do",
			type: "POST",
			contentType: "application/json",
			data: JSON.stringify(map),
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

	});


	
};