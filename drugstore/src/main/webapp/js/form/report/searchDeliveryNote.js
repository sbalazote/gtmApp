
SearchDeliveryNote = function() {
	var affiliateId = null;
	var deliveryNoteId = null;

	$("#affiliate").autocomplete({
		source: "getAffiliates.do",
		minLength: 3,
		select: function(event, ui) {
			affiliateId = ui.item.id;
			$("#affiliate").val(ui.item.code + ' - ' + ui.item.surname + ' ' + ui.item.name);
			return false;
	    }
	}).data("uiAutocomplete")._renderItem = function(ul, item) {
		return $("<li>").attr("data-value", item.id).append($("<a>").text(item.code + " - " + item.surname + " " + item.name)).appendTo(ul);
	};
	
	var validateForm = function() {
		var form = $("#searchDeliveryNoteForm");
		form.validate({
			rules: {
				deliveryNoteNumberSearch: {
					digits: true
				},
				provisioningRequestSearch: {
					digits: true
				}
			},
			showErrors: myShowErrors,
			onsubmit: false
		});
		return form.valid();
	};
	
	$("#cleanOrderButton").click(function() {
		$('#affiliate').val('');
		affiliateId = null;
		$('#deliveryNoteNumberOrderSearch').val('');
		$('#provisioningRequestSearch').val('');
	});
	
	$("#cleanOutputButton").click(function() {
		$('#deliveryLocationSearch').val('').trigger('chosen:updated');
		$('#providerSearch').val('').trigger('chosen:updated');
		$('#agreementSearch').val('').trigger('chosen:updated');
		$('#deliveryNoteNumberOutputSearch').val('');
	});
	
	
	$('#deliveryNoteTableOrderBody').on("click", ".view-row", function() {
		var parent = $(this).parent().parent();
		deliveryNoteId = parent.find("td:first-child").html();

		showDeliveryNoteByIdModal(deliveryNoteId);
	});
	
	$("#searchOrderButton").click(function() {
		if(validateForm()){
			var jsonDeliveryNoteSearch = {
				"affiliateId": affiliateId,
				"deliveryNoteNumber": $("#deliveryNoteNumberOrderSearch").val(),
				"provisioningRequestId": $("#provisioningRequestSearch").val(),
			};
			
			$.ajax({
				url: "getDeliveryNoteFromOrderForSearch.do",
				type: "POST",
				contentType:"application/json",
				async: false,
				data: JSON.stringify(jsonDeliveryNoteSearch),
				success: function(response) {
					var aaData = [];
					for (var i = 0, l = response.length; i < l; ++i) {
						var order = {
							id:"",
							number: 0,
							date: "",
							action: ""
						};
						output.id= response[i].id;
						order.number = response[i].number;
						order.date = myParseDate(response[i].date);
						order.action = "<a href='javascript:void(0);' class='view-row'>Consulta</a>";
						aaData.push(order);
					}
					$("#deliveryNoteTableOrder").bootgrid({
							caseSensitive: false
						});
						$("#deliveryNoteTableOrder").bootgrid().bootgrid("clear");
						$("#deliveryNoteTableOrder").bootgrid().bootgrid("append", aaData);
						$("#deliveryNoteTableOrder").bootgrid().bootgrid("search", $(".search-field").val());
				
						var exportHTML = exportTableHTML("deliveryNoteTableOrder");			
						var searchHTML = $("#divOrderTable").find(".search");
						
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
		}
	});
	
	$('#deliveryNoteTableOutputBody').on("click", ".view-row", function() {
		var parent = $(this).parent().parent();
		deliveryNoteId = parent.find("td:first-child").html();

		showDeliveryNoteByIdModal(deliveryNoteId);
	});
	
	
	$("#searchOutputButton").click(function() {
		if(validateForm()){
			var jsonDeliveryNoteSearch = {
				"deliveryNoteNumber": $("#deliveryNoteNumberOutputSearch").val(),
				"deliveryLocationId": $("#deliveryLocationSearch").val(),
				"providerId": $("#providerSearch").val(),
				"agreementId": $("#agreementSearch").val(),
			};
			
			$.ajax({
				url: "getDeliveryNoteFromOutputForSearch.do",
				type: "POST",
				contentType:"application/json",
				async: false,
				data: JSON.stringify(jsonDeliveryNoteSearch),
				success: function(response) {
					var aaData = [];
					for (var i = 0, l = response.length; i < l; ++i) {
						var output = {
							id:"",
							number: 0,
							date: "",
							action: ""
						};
						output.id= response[i].id;
						output.number = response[i].number;
						output.date = myParseDate(response[i].date);
						output.action = "<a href='javascript:void(0);' class='view-row'>Consulta</a>";
						aaData.push(output);
					}
					$("#deliveryNoteTableOutput").bootgrid({
							caseSensitive: false
						});
						$("#deliveryNoteTableOutput").bootgrid().bootgrid("clear");
						$("#deliveryNoteTableOutput").bootgrid().bootgrid("append", aaData);
						$("#deliveryNoteTableOutput").bootgrid().bootgrid("search", $(".search-field").val());
				
						var exportHTML = exportTableHTML("deliveryNoteTableOutput");
						var searchHTML = $("#divOutputTable").find(".search");
						
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
		}
	});

    $("#searchSupplyingButton").click(function() {
        if(validateForm()){
            var jsonDeliveryNoteSearch = {
                "deliveryNoteNumber": $("#deliveryNoteNumberSupplyingSearch").val(),
                "clientId": $("#clientSearch").val(),
                "agreementId": $("#agreementSupplyingSearch").val()
            };

            $.ajax({
                url: "getDeliveryNoteFromSupplyingForSearch.do",
                type: "POST",
                contentType:"application/json",
                async: false,
                data: JSON.stringify(jsonDeliveryNoteSearch),
                success: function(response) {
                    var aaData = [];
                    for (var i = 0, l = response.length; i < l; ++i) {
                        var output = {
							id:"",
                            number: 0,
                            date: "",
                            action: ""
                        };
						output.id= response[i].id;
                        output.number = response[i].number;
                        output.date = myParseDate(response[i].date);
                        output.action = "<a href='javascript:void(0);' class='view-row'>Consulta</a>";
                        aaData.push(output);
                    }
                    $("#deliveryNoteTableSupplying").bootgrid({
                        caseSensitive: false
                    });
                    $("#deliveryNoteTableSupplying").bootgrid().bootgrid("clear");
                    $("#deliveryNoteTableSupplying").bootgrid().bootgrid("append", aaData);
                    $("#deliveryNoteTableSupplying").bootgrid().bootgrid("search", $(".search-field").val());

                    var exportHTML = exportTableHTML("deliveryNoteTableSupplying");
                    var searchHTML = $("#divSupplyingTable").find(".search");

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
        }
    });

    $('#deliveryNoteTableSupplyingBody').on("click", ".view-row", function() {
        var parent = $(this).parent().parent();
        deliveryNoteId = parent.find("td:first-child").html();

		showDeliveryNoteByIdModal(deliveryNoteId);
    });

     $('.nav-tabs > li > a').click(function(event){
         event.preventDefault();//stop browser to take action for clicked anchor

         //get displaying tab content jQuery selector
         var active_tab_selector = $('.nav-tabs > li.active > a').attr('href');

         if($(this).text() == "Dispensas"){
             getClients();
             getAgreements();
         }
     });


    var getClients = function() {
        $.ajax({
            url: "getClients.do",
            type: "GET",
            success: function(response) {
                for(var i = response.length - 1; i >= 0; i--) {
                    $('#clientSearch').append("<option value='"+ response[i].id + "'>" + response[i].code + " " + response[i].name + "</option>");
                    $('#clientSearch').trigger('chosen:updated');
                }
            },
            error: function(jqXHR, textStatus, errorThrown) {
                myGenericError();
            }
        });
    };

    var getAgreements = function() {
        $.ajax({
            url: "getAgreements.do",
            type: "GET",
            success: function(response) {
                for(var i = response.length - 1; i >= 0; i--) {
                    $('#agreementSupplyingSearch').append("<option value='"+ response[i].id + "'>" + response[i].code + " " + response[i].description + "</option>");
                    $('#agreementSupplyingSearch').trigger('chosen:updated');
                }
            },
            error: function(jqXHR, textStatus, errorThrown) {
                myGenericError();
            }
        });
    };

    $("#clientSearch").chosen(
        {
            width: '100%' /* desired width */
        });
    $("#agreementSupplyingSearch").chosen(
        {
            width: '100%' /* desired width */
        });
};