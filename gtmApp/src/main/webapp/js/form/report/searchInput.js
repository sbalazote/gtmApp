SearchInput = function() {
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

	new ProductAutocomplete("productInput");

	var inputId = null;
	
	$('#inputTableBody').on("click", ".view-row", function() {
		var parent = $(this).parent().parent();
		inputId = parent.find("td:first-child").html();
		
		showInputModal(inputId);
	});
	
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
		$('#deliveryLocationSearch').val('').trigger('chosen:updated');
		$('#conceptSearch').val('').trigger('chosen:updated');
		$('#agreementSearch').val('').trigger('chosen:updated');
		$('#deliveryNoteNumberSearch').val('');
		$('#purchaseOrderNumberSearch').val('');
		$('#cancelledSelect').val('').trigger('chosen:updated');
		$("#productInput").removeAttr("productId");
		$("#productInput").val('');
		$("#monodrugSelect").val('').trigger('chosen:updated');
		$("#inputTable").bootgrid("clear");
	});
	
	$("#searchButton").click(function() {
		myHideAlert();
		if(validateForm()){
			var jsonInputSearch = {
				"id": $("#idSearch").val().trim() || null,
				"dateFrom": $("#dateFromSearch").val(),
				"dateTo": $("#dateToSearch").val(),
				"conceptId": $("#conceptSearch").val() || null,
				"providerId": $("#providerSearch").val() || null,
				"deliveryLocationId": $("#deliveryLocationSearch").val() || null,
				"agreementId": $("#agreementSearch").val() || null,
				"productId": $("#productInput").attr("productId") || null,
				"deliveryNoteNumber": $("#deliveryNoteNumberSearch").val().trim(),
				"purchaseOrderNumber": $("#purchaseOrderNumberSearch").val().trim(),
				"cancelled": $("#cancelledSelect").val() || null,
				"productMonodrugId": $("#monodrugSelect").val() || null
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
						myQueryTooLargeAlert();
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
			url: "getInputForSearch.do",
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
						date: "",
						cancelled: "",
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
					input.date = myParseDate(response[i].date);
					if(response[i].cancelled){
						input.cancelled = "Si";
					}else{
						input.cancelled = "No";
					}
					input.option = "<button type=\"button\" class=\"btn btn-sm btn-default view-row\"><span class=\"glyphicon glyphicon-eye-open\"></span> Detalle</button>";
					aaData.push(input);
				}
				$("#inputTable").bootgrid({
					caseSensitive: false
				});
				$("#inputTable").bootgrid("clear");
				$("#inputTable").bootgrid("append", aaData);
				$("#inputTable").bootgrid("search", $(".search-field").val());
				
				var params = '&dateFrom=' + jsonInputSearch.dateFrom + 
				'&id=' + jsonInputSearch.id +
				'&dateTo=' + jsonInputSearch.dateTo +
				'&conceptId=' + jsonInputSearch.conceptId +
				'&providerId=' + jsonInputSearch.providerId +
				'&deliveryLocationId=' + jsonInputSearch.deliveryLocationId +
				'&agreementId=' + jsonInputSearch.agreementId + 
				'&deliveryNoteNumber=' + jsonInputSearch.deliveryNoteNumber +
				'&purchaseOrderNumber=' + jsonInputSearch.purchaseOrderNumber +
				'&cancelled=' + jsonInputSearch.cancelled +
				'&productId=' + jsonInputSearch.productId +
				'&productMonodrugId=' + jsonInputSearch.productMonodrugId;

				var exportHTML = exportQueryTableHTML("./rest/inputs", params);
				var searchHTML = $("#divTable .search");
				
				if (searchHTML.prev().length == 0) {
					searchHTML.before(exportHTML);
				} else {
					searchHTML.prev().html(exportHTML);
				}
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myGenericError();
			}
		});
	};
	
};