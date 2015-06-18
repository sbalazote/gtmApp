
SearchSerializedProduct = function() {
	var productId;
	var orderId;
	var outputId;
	var deliveryNoteId;
	
	// Product autocomplete
	
	$("#productInput").autocomplete({
		source: function(request, response) {
			$.ajax({
				url: "getProducts.do",
				type: "GET",
				async: false,
				data: {
					term: request.term,
					active: true
				},
				success: function(data) {
					var array = $.map(data, function(item) {
						return {
							id:	item.id,
							label: item.code + " - " + item.description + " - " + item.brand.description + " - " + item.monodrug.description,
							value: item.code + " - " + item.description,
							gtin: item.gtin,
							type: item.type
						};
					});
					response(array);
				},
				error: function(jqXHR, textStatus, errorThrown) {
					myGenericError();
				}
			});
		},
		select: function(event, ui) {
				productId = ui.item.id;
	    },
		minLength: 3,
		autoFocus: true
	});
	
	var validateForm = function() {
		var form = $("#searchSerializedProductForm");
		form.validate({
			rules: {
				serialNumberSearch: {
					digits: true,
					required: true
				},
				productInput: {
					required: true
				},
			},
			showErrors: myShowErrors,
			onsubmit: false
		});
		return form.valid();
	};
	
	$("#cleanButton").click(function() {
		$('#productInput').val('');
		productId = "";
		$('#serialNumberSearch').val('');
	});
	
	$("#searchButton").click(function() {
		if(validateForm()){
			$.ajax({
				url: "getSerializedProductAudit.do",
				type: "GET",
				contentType:"application/json",
				async: false,
				data: {
					productId: productId,
					serialNumber: $("#serialNumberSearch").val().trim(),
				},
				success: function(response) {
					$('#divMovements').hide();

					if(response.inputs.length > 0 || response.outputs.length > 0 || response.orders.length > 0 || response.deliveryNotes.length > 0){
						var aaData = [];
						if(response.inputs != null){
							if(response.inputs.length > 0){
								$('#divMovements').show();
							}
							for (var i = 0, l = response.inputs.length; i < l; ++i) {
								var audit = {
									id: 0,
									action: "",
									operation: "",
									user: "",
									date: "",
									view: ""
								};
								audit.id = response.inputs[i].operationId;
								audit.action = "Ingreso";
								audit.operation = response.inputs[i].auditAction;
								audit.user = response.inputs[i].username;
								audit.date = response.inputs[i].date;
								audit.view = "<a href='javascript:void(0);' class='view-row-input'>Consulta</a>";
								aaData.push(audit);
							}
						}

						if(response.outputs != null){
							for (var i = 0, l = response.outputs.length; i < l; ++i) {
								var audit = {
									id: 0,
									action: "",
									operation: "",
									user: "",
									date: "",
									view: ""
								};
								audit.id = response.outputs[i].operationId;
								audit.action = "Egreso";
								audit.operation = response.outputs[i].auditAction;
								audit.user = response.outputs[i].username;
								audit.date = response.outputs[i].date;
								audit.view = "<a href='javascript:void(0);' class='view-row-output'>Consulta</a>";
								aaData.push(audit);
							}
						}

						if(response.orders != null){
							for (var i = 0, l = response.orders.length; i < l; ++i) {
								var audit = {
									id: 0,
									action: "",
									operation: "",
									user: "",
									date: "",
									view: ""
								};
								audit.id = response.orders[i].operationId;
								audit.action = "Armado";
								audit.operation = response.orders[i].auditAction;
								audit.user = response.orders[i].username;
								audit.date = response.orders[i].date;
								audit.view = "<a href='javascript:void(0);' class='view-row-order'>Consulta</a>";
								aaData.push(audit);
							}
						}

						if(response.deliveryNotes != null){
							for (var i = 0, l = response.deliveryNotes.length; i < l; ++i) {
								var audit = {
									id: 0,
									action: "",
									operation: "",
									user: "",
									date: "",
									view: ""
								};
								audit.id = response.deliveryNotes[i].operationId;
								audit.action = "Remito";
								audit.operation = response.deliveryNotes[i].auditAction;
								audit.user = response.deliveryNotes[i].username;
								audit.date = response.deliveryNotes[i].date;
								audit.view = "<a href='javascript:void(0);' class='view-row-deliveryNote'>Consulta</a>";
								aaData.push(audit);
							}
						}
						$("#movementsTable").bootgrid({
							caseSensitive: false
						});
						$("#movementsTable").bootgrid().bootgrid("clear");
						$("#movementsTable").bootgrid().bootgrid("append", aaData);
						$("#movementsTable").bootgrid().bootgrid("search", $(".search-field").val());
					}else{
						
						BootstrapDialog.show({
							type: BootstrapDialog.TYPE_INFO,
					        title: 'Atenci\u00f3n',
					        message: "No se han encontrado elementos para la consulta realizada.",
					        buttons: [{
				                label: 'Cerrar',
				                action: function(dialogItself){
				                    dialogItself.close();
				                }
				            }]
						});
					}
						
				},
				error: function(jqXHR, textStatus, errorThrown) {
					myGenericError();
				}
			});
			
		}
	});
	
	//Consulta de Ingresos
	
	$('#movementsTableBody').on("click", ".view-row-input", function() {
		var parent = $(this).parent().parent();
		inputId = parent.find("td:first-child").html();
		
		showInputModal(inputId);
	});
	
	//Consulta de Egresos
	
	$('#movementsTableBody').on("click", ".view-row-output", function() {
		var parent = $(this).parent().parent();
		outputId = parent.find("td:first-child").html();
		
		showOutputModal(outputId);
	});
	
	//Consulta de Armado
	
	$('#movementsTableBody').on("click", ".view-row-order", function() {
		var parent = $(this).parent().parent();
		orderId = parent.find("td:first-child").html();
		showOrderModal(orderId);
	});
	
	//Consulta de Remito
	$('#movementsTableBody').on("click", ".view-row-deliveryNote", function() {
		var parent = $(this).parent().parent();
		deliveryNoteId = parent.find("td:first-child").html();
		
		showDeliveryNoteModal(deliveryNoteId);
	});
};