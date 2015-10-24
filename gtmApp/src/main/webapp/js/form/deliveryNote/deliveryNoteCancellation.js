DeliveryNoteCancellation = function() {
	
	var orderId = null;
	var outputId = null;
	var supplyingId = null;
	var deliveryNotesToCancel = [];
	var ordersToReassembly = [];
	var outputsToReassembly = [];
	
	$('#deliveryNoteTableBody').on("click", ".view-order-row", function() {
		var parent = $(this).parent().parent();
		orderId = parent.children().eq(2).text();
		showOrderModal(orderId);
	});
	
	$('#deliveryNoteTableBody').on("click", ".view-output-row", function() {
		var parent = $(this).parent().parent();
		outputId = parent.children().eq(2).text();
		showOutputModal(outputId);
	});
	
	$('#deliveryNoteTableBody').on("click", ".view-supplying-row", function() {
		var parent = $(this).parent().parent();
		supplyingId = parent.children().eq(2).text();
		showSupplyingModal(supplyingId);
	});

	$("#POSDeliveryNoteNumberSearch").numeric();
	$("#deliveryNoteNumberSearch").numeric();

	$("input").blur(function() {
		if ($("#POSDeliveryNoteNumberSearch").val() != "")
			$("#POSDeliveryNoteNumberSearch").val(addLeadingZeros($("#POSDeliveryNoteNumberSearch").val(), 4));
		if ($("#deliveryNoteNumberSearch").val() != "")
			$("#deliveryNoteNumberSearch").val(addLeadingZeros($("#deliveryNoteNumberSearch").val(), 8));
	});

	$("#cleanButton").click(function() {
		$("#POSDeliveryNoteNumberSearch").val('');
		$("#deliveryNoteNumberSearch").val('');
	});

	$("#searchButton").click(function() {
		/*$.ajax({
			url: "getCountDeliveryNoteSearch.do",
			type: "POST",
			contentType:"application/json",
			async: false,
			data: JSON.stringify(jsonInputSearch),
			success: function(response) {
				if(response == true){
					makeQuery(jsonInputSearch);
				}else{
					$('#queryTooLarge').modal('show');
				}
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myGenericError();
			}
		});*/
		var deliveryNoteNumber = "";
		if($("#POSDeliveryNoteNumberSearch").val() != "" && $("#deliveryNoteNumberSearch").val() != ""){
			deliveryNoteNumber = $("#POSDeliveryNoteNumberSearch").val() + "-" + $("#deliveryNoteNumberSearch").val();
			deliveryNoteNumber = $("#POSDeliveryNoteNumberSearch").val() + "-" + $("#deliveryNoteNumberSearch").val();
		}
		makeQuery(deliveryNoteNumber);
	});

	var makeQuery = function(deliveryNoteNumber) {
		$.ajax({
			url: "getCancelableDeliveryNotes.do",
			type: "POST",
			async: false,
			data: {deliveryNoteNumber: deliveryNoteNumber},
			success: function(response) {
				var aaData = [];
				$.each(response, function( index, value ) {
					var row = {
						"id": index,
						"orderAssemblyOrOutputNumber": index.substring(1),
						"class": (index.charAt(0) == "A") ? "ARMADO" : (index.charAt(0) == "E") ? "EGRESO" : "DISPENSA",
						"deliveryNoteNumbers": "[" + value.join(", ") + "]",
						"option": (index.charAt(0) == "A") ? "<button type=\"button\" class=\"btn btn-sm btn-default view-order-row\"><span class=\"glyphicon glyphicon-eye-open\"></span> Detalle</button>" : (index.charAt(0) == "E") ? "<button type=\"button\" class=\"btn btn-sm btn-default view-output-row\"><span class=\"glyphicon glyphicon-eye-open\"></span> Detalle</button>" : "<button type=\"button\" class=\"btn btn-sm btn-default view-supplying-row\"><span class=\"glyphicon glyphicon-eye-open\"></span> Detalle</button>"
					};
					aaData.push(row);

				});
				$("#deliveryNoteTable").bootgrid({
					caseSensitive: false,
					selection: true,
					rowSelect: false,
					keepSelection: true
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
								deliveryNotesToCancel.push(ids[j]);
							}
						} else {
							var outputId = rows[i].orderAssemblyOrOutputNumber;
							outputsToReassembly.push(outputId);
							for (var j = 0; j < ids.length; j++) {
								deliveryNotesToCancel.push(ids[j]);
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
								deliveryNotesToCancel.splice(deliveryNotesToCancel.indexOf(ids[j]), 1);
							}
						} else {
							var outputId = rows[i].orderAssemblyOrOutputNumber;
							outputsToReassembly.splice(outputsToReassembly.indexOf(outputId), 1);
							for (var j = 0; j < ids.length; j++) {
								deliveryNotesToCancel.splice(deliveryNotesToCancel.indexOf(ids[j]), 1);
							}
						}
					}
				});
				$("#deliveryNoteTable").bootgrid().bootgrid("clear");
				$("#deliveryNoteTable").bootgrid().bootgrid("append", aaData);
				$("#deliveryNoteTable").bootgrid().bootgrid("search", $(".search-field").val());
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myGenericError();
			}
		});
	};

	$("#confirmButton").click(function() {
		if (deliveryNotesToCancel.length == 1) {
			cancelDeliveryNotes(deliveryNotesToCancel);
		} else if (deliveryNotesToCancel.length > 1) {
			BootstrapDialog.show({
				type: BootstrapDialog.TYPE_WARNING,
				size: BootstrapDialog.SIZE_LARGE,
				message: 'Hay uno o mas remitos asociados al indicado. Desea eliminarlos todos?.',
				closable: false,
				title: 'Advertencia!',
				closable: false,
				buttons: [{
					label: 'No',
					action: function(dialogItself) {
						dialogItself.close();
					}
				}, {
					label: 'Si',
					cssClass: 'btn-primary',
					action: function(dialogItself) {
						dialogItself.close();
						cancelDeliveryNotes(deliveryNotesToCancel);
					}
				}]
			});
		} else {
			myShowAlert('info', 'Seleccione al menos un Armado/Egreso/Dispensa');
		}
	});

	var cancelDeliveryNotes = function(deliveryNotesToCancel) {
		$.ajax({
			url: "cancelDeliveryNotes.do",
			type: "POST",
			contentType: "application/json",
			data: JSON.stringify(deliveryNotesToCancel),
			async: false,
			success: function(response) {
				myReload("success", "Se han anulado los siguientes remitos: " + deliveryNotesToCancel);
				$.ajax({
					url: "reassemblyOrders.do",
					type: "POST",
					contentType: "application/json",
					data: JSON.stringify(ordersToReassembly),
					async: false,
					success: function(response) {
						//myReload("success", "Se han anulado los siguientes remitos: " + deliveryNotesToCancel);
					},
					error: function(jqXHR, textStatus, errorThrown) {
						myGenericError();
					}
				});
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myGenericError();
			}
		});
	};
};