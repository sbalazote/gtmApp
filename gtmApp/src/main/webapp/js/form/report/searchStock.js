SearchStock = function() {
	
	var productId;
	var productGtin;
	var productDescription;
	var productType;
	
	var autocomplete = false;

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

	function getStockFromProductAndAgreement(productId, agreementId) {
		var aaData = [];
		$.ajax({
			url: "getStockFromProductAndAgreement.do",
			type: "POST",
			async: false,
			data: {
				productId: productId,
				agreementId: agreementId
			},
			success: function (stock) {
				for (var i = 0, l = stock.length; i < l; ++i) {
					var gtin = "";
					if (stock[i].gtin != null) {
						gtin = stock[i].gtin.number;
					}
					var serialDetails = {
						id: i,
						gtin: gtin,
						amount: stock[i].amount,
						serialNumber: stock[i].serialNumber,
						batch: stock[i].batch,
						expirationDate: myParseDate(stock[i].expirationDate),
						viewTraceability: "<a type='button' class='btn btn-sm btn-default' href='searchSerializedProduct.do?productId=" + stock[i].product.id + "&serial=" + stock[i].serialNumber + "' target='_blank'><span class='glyphicon glyphicon-search'></span> Ver Traza" + "<//a>"
					};
					aaData.push(serialDetails);
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				myGenericError();
			}
		});
		return aaData;
	}
	
	$('#stockTable tbody').on("click", ".view-batchExpirationDateDetails-row", function(e) {
		$("#batchExpirationDatesTable").bootgrid("clear");
		var parent = $(this).parent().parent();
		var productId = parent.attr("data-row-id");
		var agreementId = parent.find(".span-agreementId").html();
		var productCode = parent.find("td:first-child").html();
		var productDescription = parent.find("td:nth(1)").html();
		$("#batchExpirationDateProductDescription").text(productCode + " - " + productDescription);
		
		var aaData = getStockFromProductAndAgreement(productId, agreementId);
		$("#batchExpirationDatesTable").bootgrid("append", aaData);
		$('#batchExpirationDatesModal').modal('show');
	});

	$('#stockTable tbody').on("click", ".view-serializedDetails-row", function(e) {
		$("#serialsTable").bootgrid("clear");
		var parent = $(this).parent().parent();
		var productId = parent.attr("data-row-id");
		var agreementId = parent.find(".span-agreementId").html();
		var productCode = parent.find("td:first-child").html();
		var productDescription = parent.find("td:nth(1)").html();
		$("#serializedProductDescription").text(productCode + " - " + productDescription);
		
		var aaData = getStockFromProductAndAgreement(productId, agreementId);
		$("#serialsTable").bootgrid("append", aaData);
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
						var cold = " Frio: ";
						cold += item.cold == true ? "Si" : "No";
						return {
							id:	item.id,
							label: item.code + " - " + item.description + " - " + item.brand.description + " - " + item.monodrug.description+ " - " + cold,
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
			$("#productInput").val(productDescription);
			$('#productInput').data("title", "").removeClass("has-error").tooltip("destroy");
			autocomplete = true;
	    },
		minLength: 3,
		autoFocus: true
	});

	$('#productInput').keydown(function(e) {
		if(e.keyCode == 13){ // Presiono Enter
			if (!autocomplete) {
				$.ajax({
					url: "getProductFromStockBySerialOrGtin.do",
					type: "GET",
					data: {
						serial: $(this).val(),
						agreementId: $("#agreementSearch").val() || null
					},
					success: function(response) {
						if (response != "") {
							productId = response.id;
							productGtin = response.lastGtin;
							productDescription = response.code + ' - ' + response.description;
							productType = response.type;

							$('#productInput').data("title", "").removeClass("has-error").tooltip("destroy");

							$("#productInput").val(productDescription);

							$("#searchButton").trigger("click");
						} else {
							$('#productInput').tooltip("destroy").data("title", "Producto Inexistente").addClass("has-error").tooltip();
							$('#productInput').val('');
							$('#productInput').focus();
						}
						return false;
					},
					error: function(jqXHR, textStatus, errorThrown) {
						myGenericError();
					}
				});
			} else {
				$('#productInput').data("title", "").removeClass("has-error").tooltip("destroy");
			}
			autocomplete = false;
		}
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
		$('#monodrugSearch').val('').trigger('chosen:updated');
		$('#productInput').val('');
		productId = "";
		
		$('#serialNumberSearch').val('');
		$('#batchNumberSearch').val('');
		refreshTable();
	});
	
	$("#searchButton").click(function() {
		myHideAlert();
		if(validateForm()){
			var jsonStockSearch = {
				"expirateDateFrom": $("#dateFromSearch").val(),
				"expirateDateTo": $("#dateToSearch").val(),
				"productId": productId || null,
				"agreementId": $("#agreementSearch").val() || null,
				"serialNumber": $("#serialNumberSearch").val().trim(),
				"batchNumber": $("#batchNumberSearch").val().trim(),
				"monodrugId": $("#monodrugSearch").val() || null
			};
			refreshTable(jsonStockSearch);
		}
	});
	
	function refreshTable(jsonStockSearch) {
		$("#stockTable").bootgrid("destroy").bootgrid({
			requestHandler: function (request) {
				request.stockSearch = jsonStockSearch;
				return request;
			},
			ajax: true,
			url: 'getStockForSearch.do',
			formatters: {
				"command": function(column, row)
				{
					if (row.serialNumber == null) {
						return "<button type=\"button\" class=\"btn btn-sm btn-default view-batchExpirationDateDetails-row\"><span class=\"glyphicon glyphicon-eye-open\"></span></button>";
					} else {
						return "<button type=\"button\" class=\"btn btn-sm btn-default view-serializedDetails-row\"><span class=\"glyphicon glyphicon-eye-open\"></span></button>";
					}
				},
				"agreement": function(column, row)
				{
					return "<span class='span-agreementId' style='display:none'>" + row.agreementId + "</span>" + row.agreement;
				}
			}
		});

		var params = '&expirateDateFrom=' + jsonStockSearch.expirateDateFrom +
			'&expirateDateTo=' + jsonStockSearch.expirateDateTo +
			'&productId=' + jsonStockSearch.productId +
			'&agreementId=' + jsonStockSearch.agreementId +
			'&serialNumber=' + jsonStockSearch.serialNumber +
			'&batchNumber=' + jsonStockSearch.batchNumber +
			'&monodrugId=' + jsonStockSearch.monodrugId;

		var exportHTML = exportQueryTableHTML("./rest/stocks", params);
		var searchHTML = $(".search");

		if (searchHTML.prev().length == 0) {
			$(".search").before(exportHTML);
		} else {
			$(".search").prev().html(exportHTML);
		}
	}
};