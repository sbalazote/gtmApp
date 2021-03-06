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

	var validateForm = function() {
		var form = $("#deliveryNoteCancellationForm");
		form.validate({
			rules: {
				POSDeliveryNoteNumberSearch: {
					required: true,
					digits: true
				},
				deliveryNoteNumberSearch: {
					required: true,
					digits: true
				}
			},
			showErrors: myShowErrors,
			onsubmit: false
		});
		return form.valid();
	};

	$("#cleanButton").click(function() {
		$("#POSDeliveryNoteNumberSearch").val('');
		$("#deliveryNoteNumberSearch").val('');
	});

	$("#searchButton").click(function() {
		if(validateForm()) {
			$("#POSDeliveryNoteNumberSearch").val(addLeadingZeros($("#POSDeliveryNoteNumberSearch").val(), 4));
			$("#deliveryNoteNumberSearch").val(addLeadingZeros($("#deliveryNoteNumberSearch").val(), 8));
			var deliveryNoteNumber = $("#POSDeliveryNoteNumberSearch").val() + "-" + $("#deliveryNoteNumberSearch").val();
			makeQuery(deliveryNoteNumber);
		}
	});

	var makeQuery = function(deliveryNoteNumber) {
		$.ajax({
			url: "getCancelableDeliveryNotes.do",
			type: "POST",
			async: false,
			data: {deliveryNoteNumber: deliveryNoteNumber},
			success: function(response) {
				var aaData = [];
				$.each(response, function(index, value) {
					var row = {
						"id": index,
						"orderAssemblyOrOutputNumber": index.substring(1),
						"class": (index.charAt(0) == "A") ? "ARMADO" : (index.charAt(0) == "E") ? "EGRESO" : "DISPENSA",
						"date": myParseDateTime(value[0].date),
						"deliveryNoteNumbers": "[" + _.pluck(value, 'number').join(',') + "]",
						"option": (index.charAt(0) == "A") ? "<button type=\"button\" class=\"btn btn-sm btn-default view-order-row\"><span class=\"glyphicon glyphicon-eye-open\"></span> Detalle</button>" : (index.charAt(0) == "E") ? "<button type=\"button\" class=\"btn btn-sm btn-default view-output-row\"><span class=\"glyphicon glyphicon-eye-open\"></span> Detalle</button>" : "<button type=\"button\" class=\"btn btn-sm btn-default view-supplying-row\"><span class=\"glyphicon glyphicon-eye-open\"></span> Detalle</button>"
					};
					aaData.push(row);
				});
				$("#deliveryNoteTable").bootgrid({
					caseSensitive: false,
					selection: true,
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
				$("#deliveryNoteTable").bootgrid("clear");
				$("#deliveryNoteTable").bootgrid("append", aaData);
				$("#deliveryNoteTable").bootgrid("search", $(".search-field").val());
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myGenericError();
			}
		});
	};

	$("#confirmButton").click(function() {
		if (_.uniq(deliveryNotesToCancel).length == 1) {
			cancelDeliveryNotes(deliveryNotesToCancel);
		} else if (_.uniq(deliveryNotesToCancel).length > 1) {
			if (cancellableDeliveryNoteCount(deliveryNotesToCancel) > 1) {
				BootstrapDialog.show({
					type: BootstrapDialog.TYPE_WARNING,
					size: BootstrapDialog.SIZE_LARGE,
					message: 'Hay uno o mas remitos asociados al indicado. Desea eliminarlos todos?.',
					closable: false,
					title: 'Advertencia!',
					closable: false,
					buttons: [{
						label: 'No',
						action: function (dialogItself) {
							dialogItself.close();
						}
					}, {
						label: 'Si',
						cssClass: 'btn-primary',
						action: function (dialogItself) {
							dialogItself.close();
							cancelDeliveryNotes(deliveryNotesToCancel);
						}
					}]
				});
			} else {
				cancelDeliveryNotes(deliveryNotesToCancel);
			}
		} else {
			myShowAlert('info', 'Seleccione al menos un Armado/Egreso/Dispensa');
		}
	});

	var cancellableDeliveryNoteCount = function(deliveryNotesToCancel) {
		var numberOfCancellableDeliveryNotes = 0;
		$.ajax({
			url: "cancellableDeliveryNoteCount.do",
			type: "POST",
			contentType: "application/json",
			data: JSON.stringify(_.uniq(deliveryNotesToCancel)),
			async: false,
			success: function(response) {
				numberOfCancellableDeliveryNotes = response;
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myGenericError();
			}
		});
		return numberOfCancellableDeliveryNotes;
	};

	var cancelDeliveryNotes = function(deliveryNotesToCancel) {
		$.ajax({
			url: "cancelDeliveryNotes.do",
			type: "POST",
			contentType: "application/json",
			data: JSON.stringify(_.uniq(deliveryNotesToCancel)),
			async: false,
			success: function(response) {
				var errors = "";
				for (var j = 0; j < response.length; j++) {
					if (response[j].resultado == true) {
						errors += "Se anulo correctamente remito: " + response[j].deliveryNoteNumber + "<br />";
					} else {
						errors += "Error al anular remito: " + response[j].deliveryNoteNumber + "<br />";
					}
					if (response[j].errores != null) {
						for (var i = 0, lengthI = response[j].errores.length; i < lengthI; i++) {
							errors += response[j].errores[i] + "<br />";
						}
					}
					if (response[j].error != null) {
						errors += response[j].error + "<br />";
					}
				}
				myReload("info", "Detalle de las anulaciones: <br /> " + errors);
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myGenericError();
			}
		});
	};
};