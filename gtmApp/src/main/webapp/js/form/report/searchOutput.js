SearchOutput = function() {

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

	var outputId = null;
	
	$('#outputTableBody').on("click", ".view-row", function() {
		var parent = $(this).parent().parent();
		outputId = parent.find("td:first-child").html();
		
		showOutputModal(outputId);
	});
	
	var validateForm = function() {
		var form = $("#searchOutputForm");
		form.validate({
			rules: {
				idSearch: {
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
		$('#cancelledSelect').val('').trigger('chosen:updated');
		$("#productInput").removeAttr("productId");
		$("#productInput").val('');
		$("#outputTable").bootgrid().bootgrid("clear");
	});
	
	$("#searchButton").click(function() {
		myHideAlert();
		if(validateForm()){
			var jsonOutputSearch = {
				"id": $("#idSearch").val().trim() || null,
				"dateFrom": $("#dateFromSearch").val(),
				"dateTo": $("#dateToSearch").val(),
				"conceptId": $("#conceptSearch").val() || null,
				"providerId": $("#providerSearch").val() || null,
				"deliveryLocationId": $("#deliveryLocationSearch").val() || null,
				"agreementId": $("#agreementSearch").val() || null,
				"productId": $("#productInput").attr("productId") || null,
				"cancelled": $("#cancelledSelect").val() || null
			};
			
			$.ajax({
				url: "getCountOutputSearch.do",
				type: "POST",
				contentType:"application/json",
				async: false,
				data: JSON.stringify(jsonOutputSearch),
				success: function(response) {
					if(response == true){
						makeQuery(jsonOutputSearch);
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
	
	var makeQuery = function(jsonOutputSearch) {
		$.ajax({
			url: "getOutputForSearch.do",
			type: "POST",
			contentType:"application/json",
			async: false,
			data: JSON.stringify(jsonOutputSearch),
			success: function(response) {
				var aaData = [];
				for (var i = 0, l = response.length; i < l; ++i) {
					var output = {
						id: 0,
						agreement: "",
						clientOrProvider: "",
						date: "",
						cancelled: "",
						option: ""
					};
					output.id = response[i].id;
					output.agreement = response[i].agreement.description;
					if(response[i].provider != null){
						output.clientOrProvider = response[i].provider.name;
					}
					if(response[i].deliveryLocation != null){
						output.clientOrProvider = response[i].deliveryLocation.name;
					}
					output.date = myParseDate(response[i].date);
					if(response[i].cancelled){
						output.cancelled = "Si";
					}else{
						output.cancelled = "No";
					}
					output.option = "<button type=\"button\" class=\"btn btn-sm btn-default view-row\"><span class=\"glyphicon glyphicon-eye-open\"></span> Detalle</button>";
					aaData.push(output);
				}
				$("#outputTable").bootgrid({
					caseSensitive: false
				});
				$("#outputTable").bootgrid().bootgrid("clear");
				$("#outputTable").bootgrid().bootgrid("append", aaData);
				$("#outputTable").bootgrid().bootgrid("search", $(".search-field").val());
				
				var params = '&dateFrom=' + jsonOutputSearch.dateFrom + 
				'&id=' + jsonOutputSearch.id +
				'&dateTo=' + jsonOutputSearch.dateTo +
				'&conceptId=' + jsonOutputSearch.conceptId +
				'&providerId=' + jsonOutputSearch.providerId +
				'&deliveryLocationId=' + jsonOutputSearch.deliveryLocationId +
				'&agreementId=' + jsonOutputSearch.agreementId +
				'&productId='+ jsonOutputSearch.productId +
				'&cancelled='	+ jsonOutputSearch.cancelled;
				
				var exportHTML = exportQueryTableHTML("./rest/outputs", params);
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