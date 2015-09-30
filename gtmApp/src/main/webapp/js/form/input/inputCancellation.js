InputCancellation = function() {
	
	var inputId = null;
	var requestsToCancel = [];
	
	$('#inputTableBody').on("click", ".view-row", function() {
		var parent = $(this).parent().parent();
		
		inputId = parent.attr("data-row-id");
		
		showInputModal(inputId);
	});
	
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
		var form = $("#searchInputForm");
		form.validate({
			rules: {
				idSearch: {
					digits: true
				},
				deliveryNoteNumberSearch: {
					digits: true
				},
				purchaseOrderNumberSearch: {
					alphanumeric: true
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
	
	$("#cleanButton").click(function() {
		if($("#dateFromSearch").val()!= ""){
			$.datepicker._clearDate('#dateFromSearch');
		}
		if($("#dateToSearch").val()!= ""){
			$.datepicker._clearDate('#dateToSearch');
		}
		$("#idSearch").val('');
		$('#providerSearch').val('').trigger('chosen:updated');
		$('#conceptSearch').val('').trigger('chosen:updated');
		$('#agreementSearch').val('').trigger('chosen:updated');
		$('#deliveryNoteNumberSearch').val('');
		$('#purchaseOrderNumberSearch').val('');
	});
	
	$("#searchButton").click(function() {
		if(validateForm()){
			var jsonInputSearch = {
				"id": $("#idSearch").val(),
				"dateFrom": $("#dateFromSearch").val(),
				"dateTo": $("#dateToSearch").val(),
				"conceptId": $("#conceptSearch").val(),
				"providerId": $("#providerSearch").val() || null,
				"deliveryLocationId": $("#deliveryLocationSearch").val() || null,
				"agreementId": $("#agreementSearch").val() || null,
				"deliveryNoteNumber": $("#deliveryNoteNumberSearch").val().trim(),
				"purchaseOrderNumber": $("#purchaseOrderNumberSearch").val().trim(),
				"cancelled": false
			};
			
			$.ajax({
				url: "getCountInputSearch.do",
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
			});
			
		}
	});
	
	var makeQuery = function(jsonInputSearch) {
		$.ajax({
			url: "getCancelables.do",
			type: "POST",
			contentType:"application/json",
			async: false,
			data: JSON.stringify(jsonInputSearch),
			success: function(response) {
				var aaData = [];
				for (var i = 0, l = response.length; i < l; ++i) {
					var input = {
							id: 0,
							agreement: "",
							providerOrDeliveryLocation: "",
							concept: "",
							date: "",
							option: ""
					};
					input.id = response[i].id;
					input.agreement = response[i].agreement.description;
					if(response[i].provider != null){
						input.providerOrDeliveryLocation = response[i].provider.name;
					}
					if(response[i].deliveryLocation != null){
						input.providerOrDeliveryLocation = response[i].deliveryLocation.name;
					}
					input.concept = response[i].concept.description;
					input.date = myParseDate(response[i].date);
					input.option = "<a class='view-row' href='javascript:void(0);'>Consulta</a>";
					aaData.push(input);
				}
				$("#inputTable").bootgrid({
					caseSensitive: false,
					selection: true,
					rowSelect: false
				}).on("selected.rs.jquery.bootgrid", function(e, rows) {
					for (var i = 0; i < rows.length; i++) {
						requestsToCancel.push(rows[i].id);
					}
				}).on("deselected.rs.jquery.bootgrid", function(e, rows) {
					for (var i = 0; i < rows.length; i++) {
						for(var j = requestsToCancel.length - 1; j >= 0; j--) {
							if(requestsToCancel[j] === rows[i].id) {
								requestsToCancel.splice(j, 1);
							}
						}
					}
				});
				$("#inputTable").bootgrid().bootgrid("clear");
				$("#inputTable").bootgrid().bootgrid("append", aaData);
				$("#inputTable").bootgrid().bootgrid("search", $(".search-field").val());
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myGenericError();
			}
		});
	};
	

	$("#confirmButton").click(function() {
		if(requestsToCancel.length == 1){
			$.ajax({
				url: "cancelInput.do",
				type: "POST",
				contentType: "application/json",
				data: JSON.stringify(requestsToCancel[0]),
				async: false,
				success: function(response) {
					if(response == true){
						myReload("success", "Se ha anulado el ingreso: " + requestsToCancel);
					}else{
						myReload("warning", "No puede ser anulado el ingreso: " + requestsToCancel);
					}
				},
				error: function(jqXHR, textStatus, errorThrown) {
					myGenericError();
				}
			});
		}else{
			myShowAlert('info', 'Seleccione al menos un Ingreso de Mercader\u00eda para ANULAR');

			/*BootstrapDialog.show({
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
			 });*/
		}
	});
	
	
};