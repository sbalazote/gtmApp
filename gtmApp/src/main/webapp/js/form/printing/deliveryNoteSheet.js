var DeliveryNoteSheet = function() {
	
	var orderId = null;
	var ordersToPrint = [];

	$("#idSearch").numeric();
	
	$('#deliveryNoteTableBody').on("click", ".view-row", function() {
		var parent = $(this).parent().parent();
	
		orderId = parent.attr("data-row-id");
		
		showOrderModal(orderId);
	});
	
	$("#searchButton").click(function() {
		refreshTable();
	});
	
	$("#cleanButton").click(function() {
		$('#idSearch').val('').trigger('chosen:updated');
		$('#logisticsOperatorSearch').val('').trigger('chosen:updated');
		$('#clientSearch').val('').trigger('chosen:updated');
		$('#deliveryLocationSearch').val('').trigger('chosen:updated');
		$('#agreementSearch').val('').trigger('chosen:updated');
		refreshTable();
	});

	$("#confirmButton").click(function() {
		if(_.uniq(ordersToPrint).length > 0) {
			$.ajax({
				url: "printDeliveryNotes.do",
				type: "POST",
				contentType: "application/json",
				data: JSON.stringify(_.uniq(ordersToPrint)),
				async: false,
				success: function(response) {
					var msgType = "success";
					var message;
					if(response.deliveryNoteNumbers.length > 0){
						message = "Se han generado los siguientes remitos: <p></p><strong>";
						$.each(response.deliveryNoteNumbers, function( index, value ) {
							message += "<p>" + value + "</p>";
						});
					}
					message = (message != undefined) ? "</strong>" : "";

					if (response.errorMessages.length > 0) {
						$.each(response.errorMessages, function (index, value) {
							message += "<strong><p>" + value + "</p></strong>";
						});
						msgType = "warning";
					}
					if (response.successMessages.length > 0) {
						$.each(response.successMessages, function (index, value) {
							message += "<strong><p>" + value + "</p></strong>";
						});
					}
					myReload(msgType, message);
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
				provisioningRequestId: $('#idSearch').val() || null,
				agreementId: $("#agreementSearch").val() || null,
				logisticsOperatorId: $('#logisticsOperatorSearch').val() || null,
				clientId: $("#clientSearch").val() || null,
				deliveryLocationId: $('#deliveryLocationSearch').val() || null
			},
			success: function(response) {
				var aaData = [];
				for (var i = 0, l = response.length; i < l; ++i) {
					var orderDetail = {
						orderId: 0,
						id: 0,
						deliveryDate: "",
						agreement: "",
						logisticsOperator: "",
						client: "",
						deliveryLocation: "",
						option:""
					};

					orderDetail.orderId = response[i].id;
					orderDetail.id = response[i].provisioningRequest.id;
					orderDetail.deliveryDate = response[i].provisioningRequest.deliveryDate;
					orderDetail.agreement = response[i].provisioningRequest.agreement.description;
					orderDetail.logisticsOperator = (response[i].provisioningRequest.logisticsOperator != null) ?  response[i].provisioningRequest.logisticsOperator.name : "N/I";
					orderDetail.client = response[i].provisioningRequest.client.name;
					orderDetail.deliveryLocation = response[i].provisioningRequest.deliveryLocation.name;
					orderDetail.option = "<button type=\"button\" class=\"btn btn-sm btn-default view-row\"><span class=\"glyphicon glyphicon-eye-open\"></span> Detalle</button>";

					aaData.push(orderDetail);
				}
				
				$("#deliveryNoteTable").bootgrid({
					caseSensitive: false,
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
								//return value.format("DD/MM/YYYY");
								return moment(value).format("DD/MM/YYYY");
							}
						}
					}
				}).on("selected.rs.jquery.bootgrid", function(e, rows) {
					for (var i = 0; i < rows.length; i++) {
						ordersToPrint.push(rows[i].orderId);
					}
				}).on("deselected.rs.jquery.bootgrid", function(e, rows) {
					for (var i = 0; i < rows.length; i++) {
						ordersToPrint.splice(ordersToPrint.indexOf(rows[i].orderId), 1);
					}
				});
				$("#deliveryNoteTable").bootgrid("clear");
				$("#deliveryNoteTable").bootgrid("append", aaData);
				$("#deliveryNoteTable").bootgrid("search", $(".search-field").val());
			},
			error: function(response) {
			}
		});	
	}

	setTimeout(function(){
		window.location.reload();
	}, 300000);
};