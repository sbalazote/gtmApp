SearchDeliveryNote = function() {
	$('#outputDateFromButton').click(function() {
		var maxDate = $( "#outputDateToSearch" ).datepicker( "getDate" );
		$("#outputDateFromSearch").datepicker("option", "maxDate", maxDate);
		$("#outputDateFromSearch").datepicker().focus();
	});

	$('#outputDateToButton').click(function() {
		var minDate = $( "#outputDateFromSearch" ).datepicker( "getDate" );
		$("#outputDateToSearch").datepicker("option", "minDate", minDate);
		$("#outputDateToSearch").datepicker().focus();
	});

	$('#supplyingDateFromButton').click(function() {
		var maxDate = $( "#supplyingDateToSearch" ).datepicker( "getDate" );
		$("#supplyingDateFromSearch").datepicker("option", "maxDate", maxDate);
		$("#supplyingDateFromSearch").datepicker().focus();
	});

	$('#supplyingDateToButton').click(function() {
		var minDate = $( "#supplyingDateFromSearch" ).datepicker( "getDate" );
		$("#supplyingDateToSearch").datepicker("option", "minDate", minDate);
		$("#supplyingDateToSearch").datepicker().focus();
	});

	$('#orderDateFromButton').click(function() {
		var maxDate = $( "#orderDateToSearch" ).datepicker( "getDate" );
		$("#orderDateFromSearch").datepicker("option", "maxDate", maxDate);
		$("#orderDateFromSearch").datepicker().focus();
	});

	$('#orderDateToButton').click(function() {
		var minDate = $( "#orderDateFromSearch" ).datepicker( "getDate" );
		$("#orderDateToSearch").datepicker("option", "minDate", minDate);
		$("#orderDateToSearch").datepicker().focus();
	});

	$("#outputDateFromSearch").datepicker();
	$("#outputDateToSearch").datepicker();

	$("#supplyingDateFromSearch").datepicker();
	$("#supplyingDateToSearch").datepicker();

	$("#orderDateFromSearch").datepicker();
	$("#orderDateToSearch").datepicker();

    $("#outputPOSDeliveryNoteNumberSearch").numeric();
    $("#outputDeliveryNoteNumberSearch").numeric();

    $("#supplyingPOSDeliveryNoteNumberSearch").numeric();
    $("#supplyingDeliveryNoteNumberSearch").numeric();

    $("#orderPOSDeliveryNoteNumberSearch").numeric();
    $("#orderDeliveryNoteNumberSearch").numeric();

    $("input").blur(function() {
        if ($("#outputPOSDeliveryNoteNumberSearch").val() != "")
            $("#outputPOSDeliveryNoteNumberSearch").val(addLeadingZeros($("#outputPOSDeliveryNoteNumberSearch").val(), 4));
        if ($("#outputDeliveryNoteNumberSearch").val() != "")
            $("#outputDeliveryNoteNumberSearch").val(addLeadingZeros($("#outputDeliveryNoteNumberSearch").val(), 8));

        if ($("#supplyingPOSDeliveryNoteNumberSearch").val() != "")
            $("#supplyingPOSDeliveryNoteNumberSearch").val(addLeadingZeros($("#supplyingPOSDeliveryNoteNumberSearch").val(), 4));
        if ($("#supplyingDeliveryNoteNumberSearch").val() != "")
            $("#supplyingDeliveryNoteNumberSearch").val(addLeadingZeros($("#supplyingDeliveryNoteNumberSearch").val(), 8));

        if ($("#orderPOSDeliveryNoteNumberSearch").val() != "")
            $("#orderPOSDeliveryNoteNumberSearch").val(addLeadingZeros($("#orderPOSDeliveryNoteNumberSearch").val(), 4));
        if ($("#orderDeliveryNoteNumberSearch").val() != "")
            $("#orderDeliveryNoteNumberSearch").val(addLeadingZeros($("#orderDeliveryNoteNumberSearch").val(), 8));

    });

	$("#outputProductInput, #supplyingProductInput, #orderProductInput").autocomplete({
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
							gtin: item.lastGtin,
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
			$("#"+this.id).attr('productId', ui.item.id);
			$("#"+this.id).val(ui.item.value);
			return false;
		},
		minLength: 3,
		autoFocus: true
	});

	var affiliateId = null;
	var deliveryNoteId = null;

	$("#orderAffiliate, #supplyingAffiliate").select2({
		allowClear: true,
		placeholder : "Buscar afiliado...",
		minimumInputLength : 3,
		//theme: "classic",
		initSelection : function(element, callback) {
			var data = {
				code : element.attr("code"),
				name : element.attr("name"),
				surname : element.attr("surname")
			};
			callback(data);
		},
		ajax : {
			url : "getAffiliates.do",
			dataType : 'json',
			quietMillis : 250,
			data : function(term, page) { // page is the one-based
				// page number tracked by
				// Select2
				return {
					term : term, // search term
					pageNumber : page, // page number
					pageSize : 10, // page number
					active : true
				};
			},
			results : function(data, page, query) {
				var more = (data.length == 10); // whether or not there
				// are more results
				// available

				var parsedResults = [];

				$.each(data, function(index, value) {
					parsedResults.push({
						id : value.id,
						text : value.code + " - " + value.surname + " "
						+ value.name
					});
				});

				// notice we return the value of more so Select2 knows
				// if more results can be loaded
				return {
					results : parsedResults,
					more : more
				};
			}
		},
		formatResult : function(data) {
			return "<div class='select2-user-result'>" + data.text
				+ "</div>";
		},
		formatSelection : function(data) {
			return data.text;
		}
	});
	
	var validateForm = function() {
		var form = $("#searchDeliveryNoteForm");
		form.validate({
			rules: {
				outputDateFromSearch: {
					dateITA: true,
					maxDate: $("#outputDateToSearch")
				},
				outputDateToSearch: {
					dateITA: true,
					minDate: $("#outputDateFromSearch")
				},
				orderDateFromSearch: {
					dateITA: true,
					maxDate: $("#orderDateToSearch")
				},
				orderDateToSearch: {
					dateITA: true,
					minDate: $("#orderDateFromSearch")
				},
				supplyingDateFromSearch: {
					dateITA: true,
					maxDate: $("#supplyingDateToSearch")
				},
				supplyingDateToSearch: {
					dateITA: true,
					minDate: $("#supplyingDateFromSearch")
				},
				/*outputDeliveryNoteNumberSearch: {
					digits: true
				},*/
				outputIdSearch: {
					digits: true
				},
				/*orderDeliveryNoteNumberSearch: {
					digits: true
				},*/
				provisioningIdSearch: {
					digits: true
				},
				/*supplyingDeliveryNoteNumberSearch: {
					digits: true
				},*/
				supplyingIdSearch: {
					digits: true
				}
			},
			showErrors: myShowErrors,
			onsubmit: false
		});
		return form.valid();
	};
	
	$("#cleanOutputButton").click(function() {
		$("#outputDeliveryNoteNumberSearch").val('');
        $("#outputPOSDeliveryNoteNumberSearch").val('');
		if($("#outputDateFromSearch").val()!= ""){
			$.datepicker._clearDate('#outputDateFromSearch');
		}
		if($("#outputDateToSearch").val()!= ""){
			$.datepicker._clearDate('#outputDateToSearch');
		}
		$("#outputConceptSearch").val('').trigger('chosen:updated');
		$("#outputProviderSearch").val('').trigger('chosen:updated');
		$("#outputDeliveryLocationSearch").val('').trigger('chosen:updated');
		$("#outputAgreementSearch").val('').trigger('chosen:updated');
		$('#cancelledDeliveryNotesOutputsSelect').val('').trigger('chosen:updated');
		$("#outputProductInput").removeAttr("productId");
		$("#outputProductInput").val('');
		$("#outputIdSearch").val('');
		$("#deliveryNoteTableOutput").bootgrid("clear");
	});

	$("#cleanOrderButton").click(function() {
		$("#orderDeliveryNoteNumberSearch").val('');
        $("#orderPOSDeliveryNoteNumberSearch").val('');
		if($("#orderDateFromSearch").val()!= ""){
			$.datepicker._clearDate('#orderDateFromSearch');
		}
		if($("#orderDateToSearch").val()!= ""){
			$.datepicker._clearDate('#orderDateToSearch');
		}
		$("#orderDeliveryLocationSearch").val('').trigger('chosen:updated');
		$("#orderAgreementSearch").val('').trigger('chosen:updated');
		$('#cancelledDeliveryNotesOrdersSelect').val('').trigger('chosen:updated');
		$("#orderProductInput").removeAttr("productId");
		$("#orderProductInput").val('');
		$("#orderClientSearch").val('').trigger('chosen:updated');
		$("#orderAffiliate").select2("val", "");
		affiliateId = null;
		$("#provisioningIdSearch").val('');
		$("#deliveryNoteTableOrder").bootgrid("clear");
		$('#logisticsOperatorOrderSearch').val('').trigger('chosen:updated');
		$('#monodrugOrderSelect').val('').trigger('chosen:updated');
	});

	$("#cleanSupplyingButton").click(function() {
		$("#supplyingDeliveryNoteNumberSearch").val('');
        $("#supplyingPOSDeliveryNoteNumberSearch").val('');
		if($("#supplyingDateFromSearch").val()!= ""){
			$.datepicker._clearDate('#supplyingDateFromSearch');
		}
		if($("#supplyingDateToSearch").val()!= ""){
			$.datepicker._clearDate('#supplyingDateToSearch');
		}
		$("#supplyingAgreementSearch").val('').trigger('chosen:updated');
		$('#cancelledDeliveryNotesSupplyingsSelect').val('').trigger('chosen:updated');
		$("#supplyingProductInput").removeAttr("productId");
		$("#supplyingProductInput").val('');
		$("#supplyingClientSearch").val('').trigger('chosen:updated');
		$("#supplyingAffiliate").select2("val", "");
		affiliateId = null;
		$("#supplyingIdSearch").val('');
		$("#deliveryNoteTableSupplying").bootgrid("clear");
	});

	
	$('#deliveryNoteTableOutputBody').on("click", ".view-row", function() {
		var parent = $(this).parent().parent();
		deliveryNoteId = parent.find("td:first-child").html();

		showDeliveryNoteByIdModal(deliveryNoteId);
	});

	$("#searchOutputButton").click(function() {
		myHideAlert();
		if(validateForm()){
			var deliveryNoteNumber = "";
			if($("#outputPOSDeliveryNoteNumberSearch").val() != "" && $("#outputDeliveryNoteNumberSearch").val() != ""){
				deliveryNoteNumber = $("#outputPOSDeliveryNoteNumberSearch").val() + "-" + $("#outputDeliveryNoteNumberSearch").val();
			}
			var jsonDeliveryNoteSearch = {
				"deliveryNoteNumber": deliveryNoteNumber,
				"dateFrom": $("#outputDateFromSearch").val(),
				"dateTo": $("#outputDateToSearch").val(),
				"conceptId": $("#outputConceptSearch").val() || null,
				"providerId": $("#outputProviderSearch").val() || null,
				"deliveryLocationId": $("#outputDeliveryLocationSearch").val() || null,
				"agreementId": $("#outputAgreementSearch").val() || null,
				"productId": $("#outputProductInput").attr("productId") || null,
				"clientId": null,
				"affiliateId": null,
				"outputId": $("#outputIdSearch").val(),
				"supplyingId": null,
				"provisioningRequestId": null,
				"cancelled": $("#cancelledDeliveryNotesOutputsSelect").val() || null,
				"productMonodrugId": $("#monodrugOutputSelect").val() || null
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
							cancelled: "",
							action: ""
						};
						output.id= response[i].id;
						output.number = response[i].number;
						output.date = myParseDate(response[i].date);
						if(response[i].cancelled){
							output.cancelled = "Si";
						}else{
							output.cancelled = "No";
						}
						output.action = "<button type=\"button\" class=\"btn btn-sm btn-default view-row\"><span class=\"glyphicon glyphicon-eye-open\"></span> Detalle</button>";
						aaData.push(output);
					}
					$("#deliveryNoteTableOutput").bootgrid({
							caseSensitive: false
						});
						$("#deliveryNoteTableOutput").bootgrid("clear");
						$("#deliveryNoteTableOutput").bootgrid("append", aaData);
						$("#deliveryNoteTableOutput").bootgrid("search", $(".search-field").val());
				},
				error: function(jqXHR, textStatus, errorThrown) {
					myGenericError();
				}
			});
		}
	});

	$('#deliveryNoteTableOrderBody').on("click", ".view-row", function() {
		var parent = $(this).parent().parent();
		deliveryNoteId = parent.find("td:first-child").html();

		showDeliveryNoteByIdModal(deliveryNoteId);
	});

	$("#searchOrderButton").click(function() {
		myHideAlert();
		if(validateForm()){
            var deliveryNoteNumber = "";
            if($("#orderPOSDeliveryNoteNumberSearch").val() != "" && $("#orderDeliveryNoteNumberSearch").val() != ""){
                deliveryNoteNumber = $("#orderPOSDeliveryNoteNumberSearch").val() + "-" + $("#orderDeliveryNoteNumberSearch").val();
            }
			var jsonDeliveryNoteSearch = {
				"deliveryNoteNumber": deliveryNoteNumber,
				"dateFrom": $("#orderDateFromSearch").val(),
				"dateTo": $("#orderDateToSearch").val(),
				"conceptId": null,
				"providerId": null,
				"deliveryLocationId": $("#orderDeliveryLocationSearch").val() || null,
				"agreementId": $("#orderAgreementSearch").val() || null,
				"productId": $("#orderProductInput").attr("productId") || null,
				"clientId": $("#orderClientSearch").val() || null,
				"affiliateId": $("#orderAffiliate").val() || null,
				"outputId": null,
				"supplyingId": null,
				"provisioningRequestId": $("#provisioningIdSearch").val() || null,
				"cancelled": $("#cancelledDeliveryNotesOrdersSelect").val() || null,
				"productMonodrugId": $("#monodrugOrderSelect").val() || null,
				"logisticsOperatorId": $("#logisticsOperatorOrderSearch").val() || null
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
						var deliveryNoteFromOrder = {
							id:"",
							number: 0,
							date: "",
							cancelled: "",
							action: ""
						};
						deliveryNoteFromOrder.id = response[i].deliveryNote.id;
						deliveryNoteFromOrder.number = response[i].deliveryNote.number;
						deliveryNoteFromOrder.provisioningRequestId = response[i].order.provisioningRequest.id;
						deliveryNoteFromOrder.date = myParseDate(response[i].deliveryNote.date);
						if(response[i].deliveryNote.cancelled){
							deliveryNoteFromOrder.cancelled = "Si";
						}else{
							deliveryNoteFromOrder.cancelled = "No";
						}
						deliveryNoteFromOrder.action = "<button type=\"button\" class=\"btn btn-sm btn-default view-row\"><span class=\"glyphicon glyphicon-eye-open\"></span> Detalle</button>";
						aaData.push(deliveryNoteFromOrder);
					}
					$("#deliveryNoteTableOrder").bootgrid({
						caseSensitive: false
					});
					$("#deliveryNoteTableOrder").bootgrid("clear");
					$("#deliveryNoteTableOrder").bootgrid("append", aaData);
					$("#deliveryNoteTableOrder").bootgrid("search", $(".search-field").val());
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

	$("#searchSupplyingButton").click(function() {
		myHideAlert();
		if(validateForm()){
            var deliveryNoteNumber = "";
            if($("#supplyingPOSDeliveryNoteNumberSearch").val() != "" && $("#supplyingDeliveryNoteNumberSearch").val() != ""){
                deliveryNoteNumber = $("#supplyingPOSDeliveryNoteNumberSearch").val() + "-" + $("#supplyingDeliveryNoteNumberSearch").val();
            }
			var jsonDeliveryNoteSearch = {
				"deliveryNoteNumber": deliveryNoteNumber,
				"dateFrom": $("#supplyingDateFromSearch").val(),
				"dateTo": $("#supplyingDateToSearch").val(),
				"conceptId": null,
				"providerId": null,
				"deliveryLocationId": null,
				"agreementId": $("#supplyingAgreementSearch").val() || null,
				"productId": $("#supplyingProductInput").attr("productId") || null,
				"clientId": $("#supplyingClientSearch").val() || null,
				"affiliateId": $("#supplyingAffiliate").val() || null,
				"outputId": null,
				"supplyingId": $("#supplyingIdSearch").val() || null,
				"provisioningRequestId": null,
				"cancelled": $("#cancelledDeliveryNotesSupplyingsSelect").val() || null,
				"productMonodrugId": $("#monodrugSupplyingSelect").val() || null
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
							cancelled: "",
							action: ""
						};
						output.id= response[i].id;
						output.number = response[i].number;
						output.date = myParseDate(response[i].date);
						if(response[i].cancelled){
							output.cancelled = "Si";
						}else{
							output.cancelled = "No";
						}
						output.action = "<button type=\"button\" class=\"btn btn-sm btn-default view-row\"><span class=\"glyphicon glyphicon-eye-open\"></span> Detalle</button>";

						aaData.push(output);
					}
					$("#deliveryNoteTableSupplying").bootgrid({
						caseSensitive: false
					});
					$("#deliveryNoteTableSupplying").bootgrid("clear");
					$("#deliveryNoteTableSupplying").bootgrid("append", aaData);
					$("#deliveryNoteTableSupplying").bootgrid("search", $(".search-field").val());
				},
				error: function(jqXHR, textStatus, errorThrown) {
					myGenericError();
				}
			});
		}
	});

     $('.nav-tabs > li > a').click(function(event){
         event.preventDefault();//stop browser to take action for clicked anchor

         //get displaying tab content jQuery selector
         var active_tab_selector = $('.nav-tabs > li.active > a').attr('href');
     });

	$("#supplyingAgreementSearch").chosen({
		width: '100%'
	});

	$("#orderAgreementSearch").chosen({
		width: '100%'
	});

	$("#orderDeliveryLocationSearch").chosen({
		width: '100%'
	});

	$("#supplyingClientSearch").chosen({
		width: '100%'
	});

	$("#orderClientSearch").chosen({
		width: '100%'
	});

	$("#supplyingAgreementSearch").chosen({
		width: '100%'
	});

	$("#outputAgreementSearch").chosen({
		width: '100%'
	});

	$("#orderAgreementSearch").chosen({
		width: '100%'
	});

	$("#cancelledDeliveryNotesOrdersSelect").chosen({
		width: '100%'
	});

	$("#cancelledDeliveryNotesOrdersSelect").chosen({
		width: '100%'
	});

	$("#cancelledDeliveryNotesSupplyingsSelect").chosen({
		width: '100%'
	});

	$("#monodrugOrderSelect").chosen({
		width: '100%'
	});

	$("#logisticsOperatorOrderSearch").chosen({
		width: '100%'
	});

	$("#monodrugOutputSelect").chosen({
		width: '100%'
	});

	$("#monodrugSupplyingSelect").chosen({
		width: '100%'
	});
};