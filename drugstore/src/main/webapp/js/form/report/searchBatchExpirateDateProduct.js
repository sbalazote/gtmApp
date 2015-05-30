
SearchBatchExpirateDateProduct = function() {
	var productId;
	var orderId;
	var outputId;
	var deliveryNoteId;

	$("#dateSearch").datepicker();
	
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
				productInput: {
					required: true
				},
				batchSearch: {
					required: true
				},
				dateSearch: {
					required: true,
					dateITA: true,
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
		$('#batchSearch').val('');
		if($("#dateSearch").val()!= ""){
			$.datepicker._clearDate('#dateSearch');
		}
	});
	
	$("#searchButton").click(function() {
		if(validateForm()){
			$.ajax({
				url: "getBatchExpirateDateProduct.do",
				type: "GET",
				contentType:"application/json",
				async: false,
				data: {
					productId: productId,
					batch: $("#batchSearch").val(),
					expirateDate: $("#dateSearch").val(),
				},
				success: function(response) {
					$('#divInput').hide();
					$('#divOutput').hide();
					$('#divOrder').hide();
					$('#divDeliveryNote').hide();
					
					if(response.inputs.length > 0 || response.outputs.length > 0 || response.orders.length > 0 || response.deliveryNotes.length > 0){
						var aaData = [];
						if(response.inputs != null){
							if(response.inputs.length > 0){
								$('#divInput').show();
							}
							for (var i = 0, l = response.inputs.length; i < l; ++i) {
								var audit = {
									id: 0,
									operation: "",
									user: "",
									date: "",
									action: ""
								};
								audit.id = response.inputs[i].operationId;
								audit.operation = response.inputs[i].auditAction;
								audit.user = response.inputs[i].username;
								audit.date = response.inputs[i].date;
								audit.action = "<a href='javascript:void(0);' class='view-row'>Consulta</a>";
								aaData.push(audit);
							}
						}
						$("#inputTable").bootgrid({
							caseSensitive: false
						});
						$("#inputTable").bootgrid().bootgrid("clear");
						$("#inputTable").bootgrid().bootgrid("append", aaData);
						$("#inputTable").bootgrid().bootgrid("search", $(".search-field").val());
				
						var exportHTML = exportTableHTML("inputTable");
						$("#divInput").find(".search").before(exportHTML);
						
						var aaData = [];
						if(response.outputs != null){
							if(response.outputs.length > 0){
								$('#divOutput').show();
							}
							for (var i = 0, l = response.outputs.length; i < l; ++i) {
								var audit = {
									id: 0,
									operation: "",
									user: "",
									date: "",
									action: ""
								};
								audit.id = response.outputs[i].operationId;
								audit.operation = response.outputs[i].auditAction;
								audit.user = response.outputs[i].username;
								audit.date = response.outputs[i].date;
								audit.action = "<a href='javascript:void(0);' class='view-row'>Consulta</a>";
								aaData.push(audit);
							}
						}
						$("#outputTable").bootgrid({
							caseSensitive: false
						});
						$("#outputTable").bootgrid().bootgrid("clear");
						$("#outputTable").bootgrid().bootgrid("append", aaData);
						$("#outputTable").bootgrid().bootgrid("search", $(".search-field").val());
				
						var exportHTML = exportTableHTML("outputTable");
						$("#divOutput").find(".search").before(exportHTML);
						
						var aaData = [];
						if(response.orders != null){
							if(response.orders.length > 0){
								$('#divOrder').show();
							}
							for (var i = 0, l = response.orders.length; i < l; ++i) {
								var audit = {
									id: 0,
									operation: "",
									user: "",
									date: "",
									action: ""
								};
								audit.id = response.orders[i].operationId;
								audit.operation = response.orders[i].auditAction;
								audit.user = response.orders[i].username;
								audit.date = response.orders[i].date;
								audit.action = "<a href='javascript:void(0);' class='view-row'>Consulta</a>";
								aaData.push(audit);
							}
						}
						$("#orderTable").bootgrid({
							caseSensitive: false
						});
						$("#orderTable").bootgrid().bootgrid("clear");
						$("#orderTable").bootgrid().bootgrid("append", aaData);
						$("#orderTable").bootgrid().bootgrid("search", $(".search-field").val());
				
						var exportHTML = exportTableHTML("orderTable");
						$("#divOrder").find(".search").before(exportHTML);
						
						var aaData = [];
						if(response.deliveryNotes != null){
							if(response.deliveryNotes.length > 0){
								$('#divDeliveryNote').show();
							}
							for (var i = 0, l = response.deliveryNotes.length; i < l; ++i) {
								var audit = {
									id: 0,
									operation: "",
									user: "",
									date: "",
									action: ""
								};
								audit.id = response.deliveryNotes[i].operationId;
								audit.operation = response.deliveryNotes[i].auditAction;
								audit.user = response.deliveryNotes[i].username;
								audit.date = response.deliveryNotes[i].date;
								audit.action = "<a href='javascript:void(0);' class='view-row'>Consulta</a>";
								aaData.push(audit);
							}
						}
						$("#deliveryNoteTable").bootgrid({
							caseSensitive: false
						});
						$("#deliveryNoteTable").bootgrid().bootgrid("clear");
						$("#deliveryNoteTable").bootgrid().bootgrid("append", aaData);
						$("#deliveryNoteTable").bootgrid().bootgrid("search", $(".search-field").val());
				
						var exportHTML = exportTableHTML("deliveryNoteTable");
						$("#divDeliveryNote").find(".search").before(exportHTML);
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
	
	$('#inputTableBody').on("click", ".view-row", function() {
		var parent = $(this).parent().parent();
		inputId = parent.find("td:first-child").html();
		
		showInputModal(inputId);
	});
	
	//Consulta de Egresos
	
	$('#outputTableBody').on("click", ".view-row", function() {
		var parent = $(this).parent().parent();
		outputId = parent.find("td:first-child").html();
		
		showOutputModal(outputId);
	});
	
	//Consulta de Armado
	
	$('#orderTableBody').on("click", ".view-row", function() {
		var parent = $(this).parent().parent();
		orderId = parent.find("td:first-child").html();
		showOrderModal(orderId);
	});
	
	//Consulta de Remito
	$('#deliveryNoteTableBody').on("click", ".view-row", function() {
		var parent = $(this).parent().parent();
		deliveryNoteId = parent.find("td:first-child").html();
		
		showDeliveryNoteModal(deliveryNoteId);
	});
};