
SearchStock = function() {
	
	var productId;
	var productGtin;
	var productDescription;
	var productType;
	
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
	
	
	// Product autocomplete
	
	$("#productInput").autocomplete({
		source: function(request, response) {
			$.ajax({
				url: "getProductoFromStock.do",
				type: "GET",
				async: false,
				data: {
					term: request.term,
                    agreementId: null
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
				productGtin = ui.item.gtin;
				productDescription = ui.item.value;
				productType = ui.item.type;
	    },
		minLength: 3,
		autoFocus: true
	});
	
	var validateForm = function() {
		var form = $("#searchStockForm");
		form.validate({
			rules: {
				serialNumberSearch: {
					digits: true
				},
				batchNumberSearch: {
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
		$('#agreementSearch').val('').trigger('chosen:updated');
		$('#productInput').val('');
		productId = "";
		
		$('#serialNumberSearch').val('');
		$('#batchNumberSearch').val('');
	});
	
	$("#searchButton").click(function() {
		if(validateForm()){
			var jsonStockSearch = {
				"expirateDateFrom": $("#dateFromSearch").val(),
				"expirateDateTo": $("#dateToSearch").val(),
				"productId": productId || null,
				"agreementId": $("#agreementSearch").val() || null,
				"serialNumber": $("#serialNumberSearch").val().trim(),
				"batchNumber": $("#batchNumberSearch").val().trim()
			};
			jsonStock = jsonStockSearch;
			$.ajax({
				url: "getCountStockSearch.do",
				type: "POST",
				contentType:"application/json",
				async: false,
				data: JSON.stringify(jsonStockSearch),
				success: function(response) {
					if(response == true){
						makeQuery(jsonStockSearch);
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
	
	var makeQuery = function(jsonStockSearch) {
		$.ajax({
			url: "getStockForSearch.do",
			type: "POST",
			contentType:"application/json",
			async: false,
			data: JSON.stringify(jsonStockSearch),
			success: function(response) {
				var aaData = [];
				
				var stockList = [];
				for (var i = 0, l = response.length; i < l; ++i) {
					var stock = {
						productId: "",
						productDescription: "",
						agreementId: "",
						agreementDescription: "",
						batch: "",
						expirationDate: "",
						amount: 0
					};
					stock.productId = response[i].product.id;
					stock.productDescription = response[i].product.code + " - " + response[i].product.description;
					stock.agreementId = response[i].agreement.id;
					stock.agreementDescription = response[i].agreement.description;
					stock.batch = response[i].batch;
					stock.expirationDate = response[i].expirationDate;
					stock.amount = response[i].amount;
					searchAndAdd(stockList, stock);
				}
				
				for (var i = 0, l = stockList.length; i < l; ++i) {
					var stock = {
						product: "",
						agreement: "",
						serialNumber: "",
						batch: "",
						expirationDate: "",
						amount: 0
					};
					stock.product = stockList[i].productDescription;
					stock.agreement = stockList[i].agreementDescription;
					stock.batch = stockList[i].batch;
					stock.expirationDate = myParseDate(stockList[i].expirationDate);
					stock.amount = stockList[i].amount;
					aaData.push(stock);
				}
				$("#stockTable").bootgrid({
					caseSensitive: false
				});
				$("#stockTable").bootgrid().bootgrid("clear");
				$("#stockTable").bootgrid().bootgrid("append", aaData);
				$("#stockTable").bootgrid().bootgrid("search", $(".search-field").val());
				
				var params = '&expirateDateFrom=' + jsonStockSearch.expirateDateFrom + 
				'&expirateDateTo=' + jsonStockSearch.expirateDateTo +
				'&productId=' + jsonStockSearch.productId +
				'&agreementId=' + jsonStockSearch.agreementId +
				'&serialNumber=' + jsonStockSearch.serialNumber +
				'&batchNumber=' + jsonStockSearch.batchNumber; 
				
				var exportHTML = exportQueryTableHTML("./rest/stocks", params);
				var searchHTML = $(".search");
				
				if (searchHTML.prev().length == 0) {
					$(".search").before(exportHTML);
				} else {
					$(".search").prev().html(exportHTML);
				}
				
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myGenericError();
			}
		});
	};
	
	var searchAndAdd = function(stockList, stock) {
		if(stockList.length == 0){
			stockList.push(stock);
		}else{
			var found = false;
			for(var i = 0, l = stockList.length; i < l; ++i){
				if((stockList[i].productId == stock.productId) && 
						(stockList[i].agreementId == stock.agreementId) &&
						(stockList[i].batch == stock.batch) &&
						(stockList[i].expirationDate == stock.expirationDate)){
					stockList[i].amount += stock.amount;
					found = true;
				}
			}
			if(!found){
				stockList.push(stock);
			}
		}
	}
	
};