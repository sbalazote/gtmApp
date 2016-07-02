SearchProvisioningRequest = function() {
	// Affiliate autocomplete	
	
	$("#affiliateSearch").autocomplete({
		source: function(request, response) {
			$.ajax({
				url: "getAffiliates.do",
				type: "GET",
				async: false,
				data: {
					term: request.term,
					clientId: $("#clientSearch").val()
				},
				success: function(data) {
					var array = $.map(data, function(item) {
						return {
							id:	item.id,
							label: item.code + " - " + item.surname + " " + item.name,
							value: item.code + " - " + item.surname + " " + item.name
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
			$('#affiliateSearch').attr("affiliateId", ui.item.id);
		},
		minLength: 3,
		autoFocus: true
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
	
	$("#cleanButton").click(function() {
		if($("#dateFromSearch").val()!= ""){
			$.datepicker._clearDate('#dateFromSearch');
		}
		if($("#dateToSearch").val()!= ""){
			$.datepicker._clearDate('#dateToSearch');
		}
		$("#idSearch").val('');
		$("#commentTextArea").val('');
		$('#agreementSearch').val('').trigger('chosen:updated');
		$('#logisticsOperatorSearch').val('').trigger('chosen:updated');
		$('#deliveryLocationSearch').val('').trigger('chosen:updated');
		$('#clientSearch').val('').trigger('chosen:updated');
		$('#affiliateSearch').attr("affiliateId","");
		$('#affiliateSearch').val("");
		$('#stateSearch').val('').trigger('chosen:updated');
		$("#provisioningTable").bootgrid("clear");
		$("#productInput").removeAttr("productId");
		$("#productInput").val('');
		$("#monodrugSelect").val('').trigger('chosen:updated');
	});
	
	var validateForm = function() {
		var form = $("#searchProvisioningRequestForm");
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

	new ProductAutocomplete("productInput");

	$("#searchButton").click(function() {
		myHideAlert();
			if(validateForm()){
			var jsonProvisioningSearch = {
				"provisioningId": $("#idSearch").val().trim() || null,
				"dateFrom": $("#dateFromSearch").val(),
				"dateTo": $("#dateToSearch").val(),
				"clientId": $("#clientSearch").val() || null,
				"affiliateId": $("#affiliateSearch").attr("affiliateId") || null,
				"agreementId": $("#agreementSearch").val() || null,
				"deliveryLocation": $("#deliveryLocationSearch").val() || null,
				"logisticsOperator": $("#logisticsOperatorSearch").val() || null,
				"stateId": $("#stateSearch").val() || null,
				"productId": $("#productInput").attr("productId") || null,
				"productMonodrugId": $("#monodrugSelect").val() || null
			};
			
			$.ajax({
				url: "getCountOfProvisioningSearch.do",
				type: "POST",
				contentType:"application/json",
				async: true,
				data: JSON.stringify(jsonProvisioningSearch),
				success: function(response) {
					$.unblockUI();
					if(response == true){
						makeQuery(jsonProvisioningSearch);
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
	
	var makeQuery = function(jsonProvisioningSearch) {
		$.ajax({
			url: "getProvisioningForSearch.do",
			type: "POST",
			contentType:"application/json",
			async: true,
			data: JSON.stringify(jsonProvisioningSearch),
			beforeSend : function() {
				$.blockUI({ message: 'Obteniendo Resultados. Espere un Momento por favor...' });
			},
			success: function(response) {
				$.unblockUI();
				var aaData = [];
				for (var i = 0, l = response.length; i < l; ++i) {
					var provisioning = {
						id: 0,
						client: "",
						agreement: "",
						state: "",
						date: "",
						option: ""
					};
					provisioning.id = response[i].id;
					provisioning.client = response[i].client.name;
					provisioning.agreement = response[i].agreement.description;
					provisioning.state = response[i].state.description;
					provisioning.date = myParseDate(response[i].deliveryDate);
					provisioning.option = "<button type=\"button\" class=\"btn btn-sm btn-default view-row\"><span class=\"glyphicon glyphicon-eye-open\"></span> Detalle</button>";
					aaData.push(provisioning);
				}
				$("#provisioningTable").bootgrid({
					caseSensitive: false
				});
				$("#provisioningTable").bootgrid("clear");
				$("#provisioningTable").bootgrid("append", aaData);
				$("#provisioningTable").bootgrid("search", $(".search-field").val());
				
				var params = '&dateFrom=' + jsonProvisioningSearch.dateFrom + 
				'&id=' + jsonProvisioningSearch.provisioningId +
				'&dateTo=' + jsonProvisioningSearch.dateTo +
				'&clientId=' + jsonProvisioningSearch.clientId +
				'&affiliateId=' + jsonProvisioningSearch.affiliateId +
				'&agreementId=' + jsonProvisioningSearch.agreementId +
				'&comment=' + jsonProvisioningSearch.comment +
				'&deliveryLocation=' + jsonProvisioningSearch.deliveryLocation +
				'&logisticsOperator=' + jsonProvisioningSearch.logisticsOperator +
				'&stateId=' + jsonProvisioningSearch.stateId +
				'&productId=' + jsonProvisioningSearch.productId +
				'&productMonodrugId=' + jsonProvisioningSearch.productMonodrugId;

				var exportHTML = exportQueryTableHTML("./rest/provisionings", params);
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
	var provisioningId = null;
	
	$('#provisioningTableBody').on("click", ".view-row", function() {
		var parent = $(this).parent().parent();
		provisioningId = parent.find("td:first-child").html();
		
		showProvisioningRequestModal(provisioningId);
	});

	$("#affiliateSearch").attr("disabled", true);

	$('#clientSearch').on('change', function(evt, params) {
		if ($("#clientSearch").val() == "") {
			$("#affiliateSearch").attr("disabled", true);
		} else {
			$("#affiliateSearch").attr("disabled", false);
		}
		$("#affiliateSearch").val("");
	});
};