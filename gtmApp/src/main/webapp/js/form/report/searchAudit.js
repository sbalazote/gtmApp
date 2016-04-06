SearchAudit = function() {

	var inputId;
	var outputId;
	var provisioningId;
	var orderId;
	var deliveryNoteId;
	var supplyingId;

	$('#dateFromButton').click(function() {
		var maxDate = $( "#dateToSearch" ).datepicker( "getDate" );
		$("#dateFromSearch").datepicker("option", "maxDate", maxDate);
		$("#dateFromSearch").datepicker().focus();
	});
	
	$('#dateToButton').click(function() {
		var minDate = $( "#dateFromSearch" ).datepicker( "getDate" );
		$("#dateToSearch").datepicker("option", "minDate", minDate);
		$("#dateToSearch").datepicker().focus();
	});

	$("#dateFromSearch").datepicker();
	$("#dateToSearch").datepicker();
	
	var validateForm = function() {
		var form = $("#searchAuditForm");
		form.validate({
			rules: {
				operationNumberSearch: {
					digits: true
				},
				dateFromSearch: {
					dateITA: true,
					maxDate: $("#dateToSearch")
				},
				dateToSearch: {
					dateITA: true,
					minDate: $("#dateFromSearch")
				}
			},
			showErrors: myShowErrors,
			onsubmit: false
		});
		return form.valid();
	};

	//Consulta de Ingresos
	$('#auditTableBody').on("click", ".view-row-input", function() {
		var parent = $(this).parent().parent();
		inputId = parent.find(":nth(2)").html();

		showInputModal(inputId);
	});

	//Consulta de Egresos
	$('#auditTableBody').on("click", ".view-row-output", function() {
		var parent = $(this).parent().parent();
		outputId = parent.find(":nth(2)").html();

		showOutputModal(outputId);
	});

	//Consulta de Pedido
	$('#auditTableBody').on("click", ".view-row-provisioning", function() {
		var parent = $(this).parent().parent();
		provisioningId = parent.find(":nth(2)").html();

		showProvisioningRequestModal(provisioningId);
	});

	//Consulta de Armado
	$('#auditTableBody').on("click", ".view-row-order", function() {
		var parent = $(this).parent().parent();
		orderId = parent.find(":nth(2)").html();

		showOrderModal(orderId);
	});

	//Consulta de Remito
	$('#auditTableBody').on("click", ".view-row-deliveryNote", function() {
		var parent = $(this).parent().parent();
		deliveryNoteId = parent.find(":nth(2)").html();

		showDeliveryNoteByIdModal(deliveryNoteId);
	});

	//Consulta de Dispensa
	$('#auditTableBody').on("click", ".view-row-supplying", function() {
		var parent = $(this).parent().parent();
		supplyingId = parent.find(":nth(2)").html();

		showSupplyingModal(supplyingId);
	});
	
	$("#cleanButton").click(function() {
		if($("#dateFromSearch").val()!= ""){
			$.datepicker._clearDate('#dateFromSearch');
		}
		if($("#dateToSearch").val()!= ""){
			$.datepicker._clearDate('#dateToSearch');
		}
		$('#roleSearch').val('').trigger('chosen:updated');
		$('#userSearch').val('').trigger('chosen:updated');
		$("#auditActionSearch").val('').trigger('chosen:updated');
		$('#operationNumberSearch').val('');
		$("#auditTable").bootgrid("clear");
	});
	
	$("#searchButton").click(function() {
		myHideAlert();
		if(validateForm()){
			var jsonAuditSearch = {
				"dateFrom": $("#dateFromSearch").val(),
				"dateTo": $("#dateToSearch").val(),
				"userId": $("#userSearch").val() || null,
				"operationId": $("#operationNumberSearch").val() || null,
				"roleId": $("#roleSearch").val() || null,
			};
			
			$.ajax({
				url: "getCountAuditSearch.do",
				type: "POST",
				contentType:"application/json",
				async: true,
				data: JSON.stringify(jsonAuditSearch),
				beforeSend : function() {
					$.blockUI({ message: 'Calculando Cantidad de Resultados. Espere un Momento por favor...' });
				},
				success: function(response) {
					$.unblockUI();
					if(response == true){
						makeQuery(jsonAuditSearch);
					}else{
						myQueryTooLargeAlert();
					}
				},
				error: function(jqXHR, textStatus, errorThrown) {
					$.unblockUI();
					myGenericError();
				}
			});
			
		}
	});
	
	var makeQuery = function(jsonAuditSearch) {
		$.ajax({
			url: "getAuditForSearch.do",
			type: "POST",
			contentType:"application/json",
			async: true,
			data: JSON.stringify(jsonAuditSearch),
			beforeSend : function() {
				$.blockUI({ message: 'Obteniendo Resultados. Espere un Momento por favor...' });
			},
			success: function(response) {
				$.unblockUI();
				var aaData = [];

				for (var i = 0, l = response.inputs.length; i < l; ++i) {
					var audit = {
						/*id: 0,*/
						date: "",
						role: "",
						operationNumber: 0,
						user: "",
						view: ""
					};
					/*audit.id = response.inputs[i].operationId;*/
					audit.date = myParseDateTime(response.inputs[i].date);
					audit.role = response.inputs[i].role.description;
					audit.operationNumber = response.inputs[i].operationId;
					audit.user = response.inputs[i].user.name;
					audit.view = "<button type=\"button\" class=\"btn btn-sm btn-default view-row-input\"><span class=\"glyphicon glyphicon-eye-open\"></span> Detalle</button>";
					aaData.push(audit);
				}
				for (var i = 0, l = response.outputs.length; i < l; ++i) {
					var audit = {
						/*id: 0,*/
						date: "",
						role: "",
						operationNumber: 0,
						user: "",
						view: ""
					};
					/*audit.id = response.outputs[i].operationId;*/
					audit.date = myParseDateTime(response.outputs[i].date);
					audit.role = response.outputs[i].role.description;
					audit.operationNumber = response.outputs[i].operationId;
					audit.user = response.outputs[i].user.name;
					audit.view = "<button type=\"button\" class=\"btn btn-sm btn-default view-row-output\"><span class=\"glyphicon glyphicon-eye-open\"></span> Detalle</button>";
					aaData.push(audit);
				}
				for (var i = 0, l = response.provisioningRequests.length; i < l; ++i) {
					var audit = {
						/*id: 0,*/
						date: "",
						role: "",
						operationNumber: 0,
						user: "",
						view: ""
					};
					/*audit.id = response.provisioningRequests[i].operationId;*/
					audit.date = myParseDateTime(response.provisioningRequests[i].date);
					audit.role = response.provisioningRequests[i].role.description;
					audit.operationNumber = response.provisioningRequests[i].operationId;
					audit.user = response.provisioningRequests[i].user.name;
					audit.view = "<button type=\"button\" class=\"btn btn-sm btn-default view-row-provisioning\"><span class=\"glyphicon glyphicon-eye-open\"></span> Detalle</button>";
					aaData.push(audit);
				}
				for (var i = 0, l = response.orders.length; i < l; ++i) {
					var audit = {
						/*id: 0,*/
						date: "",
						role: "",
						operationNumber: 0,
						user: "",
						view: ""
					};
					/*audit.id = response.orders[i].operationId;*/
					audit.date = myParseDateTime(response.orders[i].date);
					audit.role = response.orders[i].role.description;
					audit.operationNumber = response.orders[i].operationId;
					audit.user = response.orders[i].user.name;
					audit.view = "<button type=\"button\" class=\"btn btn-sm btn-default view-row-order\"><span class=\"glyphicon glyphicon-eye-open\"></span> Detalle</button>";
					aaData.push(audit);
				}
				for (var i = 0, l = response.deliveryNotes.length; i < l; ++i) {
					var audit = {
						/*id: 0,*/
						date: "",
						role: "",
						operationNumber: 0,
						user: "",
						view: ""
					};
					/*audit.id = response.deliveryNotes[i].operationId;*/
					audit.date = myParseDateTime(response.deliveryNotes[i].date);
					audit.role = response.deliveryNotes[i].role.description;
					audit.operationNumber = response.deliveryNotes[i].operationId;
					audit.user = response.deliveryNotes[i].user.name;
					audit.view = "<button type=\"button\" class=\"btn btn-sm btn-default view-row-deliveryNote\"><span class=\"glyphicon glyphicon-eye-open\"></span> Detalle</button>";
					aaData.push(audit);
				}
				for (var i = 0, l = response.supplyings.length; i < l; ++i) {
					var audit = {
						/*id: 0,*/
						date: "",
						role: "",
						operationNumber: 0,
						user: "",
						view: ""
					};
					/*audit.id = response.supplyings[i].operationId;*/
					audit.date = myParseDateTime(response.supplyings[i].date);
					audit.role = response.supplyings[i].role.description;
					audit.operationNumber = response.supplyings[i].operationId;
					audit.user = response.supplyings[i].user.name;
					audit.view = "<button type=\"button\" class=\"btn btn-sm btn-default view-row-supplying\"><span class=\"glyphicon glyphicon-eye-open\"></span> Detalle</button>";
					aaData.push(audit);
				}

				$("#auditTable").bootgrid({
					caseSensitive: false
				});
				$("#auditTable").bootgrid("clear");
				$("#auditTable").bootgrid("append", aaData);
				$("#auditTable").bootgrid("search", $(".search-field").val());
				
				var params = '&dateFrom=' + jsonAuditSearch.dateFrom + 
				'&dateTo=' + jsonAuditSearch.dateTo +
				'&operationId=' + jsonAuditSearch.operationId +
				'&userId=' + jsonAuditSearch.userId +
				'&roleId=' + jsonAuditSearch.roleId;
				
				var exportHTML = exportQueryTableHTML("./rest/audits", params);
				var searchHTML = $(".search");
				
				if (searchHTML.prev().length == 0) {
					$(".search").before(exportHTML);
				} else {
					$(".search").prev().html(exportHTML);
				}
			},
			error: function(jqXHR, textStatus, errorThrown) {
				$.unblockUI();
				myGenericError();
			}
		});
	};
};