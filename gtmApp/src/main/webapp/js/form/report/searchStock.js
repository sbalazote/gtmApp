SearchStock = function() {
	
	var productId;
	var productGtin;
	var productDescription;
	var productType;

	var stockTable;

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
				"expirateDateFrom": $("#dateFromSearch").val() || null,
				"expirateDateTo": $("#dateToSearch").val() || null,
				"productId": productId,
				"agreementId": agreementId,
				"serialNumber": $("#serialNumberSearch").val() || null,
				"batchNumber": $("#batchNumberSearch").val() || null,
				"monodrugId": $("#monodrugSearch").val() || null,
                "groupId": $("#groupSearch").val() || null
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
			$("#searchButton").focus();
			autocomplete = true;
	    },
		minLength: 3,
		autoFocus: true
	});

	$('#productInput').tooltip();

	$('#productInput').keydown(function(e) {
		if(e.keyCode == 13){ // Presiono Enter
			if (!autocomplete) {
				var serialNumber = $(this).val();
				$.ajax({
					url: "getProductFromStockBySerialOrGtin.do",
					type: "GET",
					async: false,
					data: {
						serial: serialNumber,
						agreementId: $("#agreementSearch").val() || null
					},
					success: function(response) {
						if (response != "") {
							productId = response.id;
							productGtin = response.lastGtin;
							productDescription = response.code + ' - ' + response.description;
							productType = response.type;

							$("#productInput").val(productDescription);

							$('#productInput').tooltip("hide").attr("data-original-title", "").removeClass("has-error");

							$("#searchButton").trigger("click");
						} else {
							var productRead = "";
							$.ajax({
								url: "getProductBySerialOrGtin.do",
								type: "GET",
								async: false,
								data: {
									serial: serialNumber,
									agreementId: $("#agreementSearch").val() || null
								},
								success: function(response) {
									if (response != "") {
										productRead = response.code + " - " + response.description;
									} else {
										productRead = "Producto inactivo o inexistente."
									}
								}
							});
							$('#productInput').attr('data-original-title', "No hay en Stock " + productRead).addClass("has-error").tooltip("show");
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
				$('#productInput').tooltip("hide").attr("data-original-title", "").removeClass("has-error");
			}
			autocomplete = false;
		}
	});
	
	var validateForm = function() {
		var form = $("#searchStockForm");
		var dateFromSearch = $("#dateFromSearch");
		var dateToSearch = $("#dateToSearch");

		form.validate({
			rules: {
				dateFromSearch: {
					dateITA: true
				},
				dateToSearch: {
					dateITA: true
				}
			},
			showErrors: myShowErrors,
			onsubmit: false
		});

		$('input[name=dateFromSearch]').rules("remove", "maxDate");
		$('input[name=dateToSearch]').rules("remove", "minDate");

		if (dateFromSearch.val() != "" && dateToSearch.val() != "") {
			$('input[name=dateFromSearch]').rules("add", {
				maxDate: dateToSearch
			});

			$('input[name=dateToSearch]').rules("add", {
				minDate: dateFromSearch
			});
		}

		return form.valid();
	};
	
	$("#cleanButton").click(function() {
		if($("#dateFromSearch").val()!= ""){
			$.datepicker._clearDate('#dateFromSearch');
		}
		if($("#dateToSearch").val()!= ""){
			$.datepicker._clearDate('#dateToSearch');
		}
		$('input[name=dateFromSearch]').rules("remove", "maxDate");
		$('input[name=dateToSearch]').rules("remove", "minDate");
		$('#agreementSearch').val('').trigger('chosen:updated');
		$('#monodrugSearch').val('').trigger('chosen:updated');
        $('#groupSearch').val('').trigger('chosen:updated');
		$('#productInput').val('');
		productId = "";
		
		$('#serialNumberSearch').val('');
		$('#batchNumberSearch').val('');
		$("#stockTable").bootgrid("destroy");
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
				"monodrugId": $("#monodrugSearch").val() || null,
                "groupId": $("#groupSearch").val() || null,
				"searchPhrase": $(".search-field").val() || null
			};
			refreshTable(jsonStockSearch);
		}
	});
	
	function refreshTable(jsonStockSearch) {
		stockTable = $("#stockTable").bootgrid({
			requestHandler: function (request) {
				//request.stockSearch = jsonStockSearch;
				//request.searchVal1 = $('#search-field-1').val();
				request.expirateDateFrom =  $("#dateFromSearch").val();
				request.expirateDateTo = $("#dateToSearch").val();
				request.productId = productId || null;
				request.agreementId = $("#agreementSearch").val() || null;
				request.serialNumber = $("#serialNumberSearch").val().trim();
				request.batchNumber = $("#batchNumberSearch").val().trim();
				request.monodrugId = $("#monodrugSearch").val() || null;
                request.groupId = $("#groupSearch").val() || null;
				return request;
			},
			ajax: true,
			url: 'getStockForSearch.do',
			formatters: {
				"command": function(column, row) {
					if (row.serialNumber == null) {
						return "<button type=\"button\" class=\"btn btn-sm btn-default view-batchExpirationDateDetails-row\"><span class=\"glyphicon glyphicon-eye-open\"></span></button>";
					} else {
						return "<button type=\"button\" class=\"btn btn-sm btn-default view-serializedDetails-row\"><span class=\"glyphicon glyphicon-eye-open\"></span></button>";
					}
				},
				"agreement": function(column, row) {
					return "<span class='span-agreementId' style='display:none'>" + row.agreementId + "</span>" + row.agreement;
				}
			}
		}).bootgrid("reload").on("loaded.rs.jquery.bootgrid", function() {
			/* Executes after data is loaded and rendered */
			stockTable.find(".view-batchExpirationDateDetails-row").on("click", function(e) {
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
			}).end().find(".view-serializedDetails-row").on("click", function(e) {
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
		});

		var params = '&expirateDateFrom=' + (jsonStockSearch.expirateDateFrom || null) +
			'&expirateDateTo=' + (jsonStockSearch.expirateDateTo || null) +
			'&productId=' + (jsonStockSearch.productId || null) +
			'&agreementId=' + (jsonStockSearch.agreementId || null) +
			'&serialNumber=' + (jsonStockSearch.serialNumber || null) +
			'&batchNumber=' + (jsonStockSearch.batchNumber || null) +
			'&monodrugId=' + (jsonStockSearch.monodrugId || null) +
            '&groupId=' + (jsonStockSearch.groupId || null) +
			'&searchPhrase=' + (jsonStockSearch.searchPhrase || null);

		var exportHTML = exportQueryTableHTML("./rest/stocks", params);
		var searchHTML = $(".search");

		if (searchHTML.prev().length == 0) {
			$(".search").before(exportHTML);
		} else {
			$(".search").prev().html(exportHTML);
		}
	}
};