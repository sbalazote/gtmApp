SearchStock = function() {
	
	var productId;
	var productGtin;
	var productDescription;
	var productType;
	
	var id;
	var serialsMap = {};
	var serialDetails = {};
	
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
	
	$('#stockTable tbody').on("click", ".view-batchExpirationDateDetails-row", function(e) {
		$("#batchExpirationDatesTable").bootgrid("clear");
		var parent = $(this).parent().parent();
		var agreementId = parent.find(".span-agreementId").html();
		var productCode = parent.find("td:first-child").html();
		var productDescription = parent.find("td:nth(1)").html();
		$("#batchExpirationDateProductDescription").text(productCode + " - " + productDescription);
		
		var serialDetails = serialsMap[agreementId + "-" + productCode];
		$("#batchExpirationDatesTable").bootgrid("append", serialDetails);
		
		$('#batchExpirationDatesModal').modal('show');
	});
	
	$('#stockTable tbody').on("click", ".view-serializedDetails-row", function(e) {
		$("#serialsTable").bootgrid("clear");
		var parent = $(this).parent().parent();
		var agreementId = parent.find(".span-agreementId").html();
		var productCode = parent.find("td:first-child").html();
		var productDescription = parent.find("td:nth(1)").html();
		$("#serializedProductDescription").text(productCode + " - " + productDescription);
		
		var serialDetails = serialsMap[agreementId + "-" + productCode];
		$("#serialsTable").bootgrid("append", serialDetails);
		
		$('#serialsModal').modal('show');
	});
	
	// Product autocomplete
	$("#productInput").autocomplete({
		source: function(request, response) {
			$.ajax({
				url: "getProductFromStock.do",
				type: "GET",
				async: false,
				data: {
					term: request.term,
                    agreementId: $("#agreementSearch").val() || null
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
		$("#stockTable").bootgrid().bootgrid("clear");
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
				id = 0;
				serialsMap = {};
				serialDetails = {};
				var stockList = [];
				for (var i = 0, l = response.length; i < l; ++i) {
					var stock = {
						productId: response[i].product.id,
						productCode: response[i].product.code,
						productDescription: response[i].product.description,
						agreementId: response[i].agreement.id,
						agreementDescription: response[i].agreement.description,
						batch: response[i].batch,
						expirationDate: response[i].expirationDate,
						serialNumber: response[i].serialNumber,
						amount: response[i].amount
					};
					searchAndAdd(stockList, stock);
				}
				
				for (var i = 0, l = stockList.length; i < l; ++i) {
					var stock = {
						id: i,
						code: stockList[i].productCode,
						product: stockList[i].productDescription,
						agreement: "<span class='span-agreementId' style='display:none'>" + stockList[i].agreementId + "</span>" + stockList[i].agreementDescription,
						amount: stockList[i].amount,
						command: ""
					};
					if (stockList[i].serialNumber == null) {
						stock.command = "<button type=\"button\" class=\"btn btn-sm btn-default view-batchExpirationDateDetails-row\"><span class=\"glyphicon glyphicon-eye-open\"></span></button>";
					} else {
						stock.command = "<button type=\"button\" class=\"btn btn-sm btn-default view-serializedDetails-row\"><span class=\"glyphicon glyphicon-eye-open\"></span></button>";
					}
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
						(stockList[i].agreementId == stock.agreementId)){
					stockList[i].amount += stock.amount;
					found = true;
				}
			}
			if(!found){
				stockList.push(stock);
			}
		}
		// Guardo lote/vte y series para mostrar los detalles.
		serialDetails = {
				id: id,
				amount: stock.amount,
				serialNumber: stock.serialNumber,
				batch: stock.batch,
				expirationDate: myParseDate(stock.expirationDate)
		};
		var item = serialsMap[stock.agreementId + "-" + stock.productCode] || [];
		item.push(serialDetails);
		serialsMap[stock.agreementId + "-" + stock.productCode] = item;
		id++;
	}
};