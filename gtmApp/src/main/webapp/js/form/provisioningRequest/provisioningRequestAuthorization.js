ProvisioningRequestAuthorization = function() {
	
	var provisioningId = null;
	var requestsToApprove = [];
	
	$('#provisioningTableBody').on("click", ".view-row", function() {
		var parent = $(this).parent().parent();
	
		provisioningId = parent.attr("data-row-id");
		
		showProvisioningRequestModal(provisioningId);
	});

	$("#searchButton").click(function() {
		$.ajax({
			url: "getProvisioningsToAuthorize.do",
			type: "GET",
			async: false,
			data: {
				agreementId: $("#agreementSearch").val() || null,
				clientId: $("#clientSearch").val() || null,
			},
			success: function(response) {
				var aaData = [];
				for (var i = 0, l = response.length; i < l; ++i) {
					var orderDetail = {
						id: 0,
						client: "",
						agreement: "",
						date: "",
						action: ""
					};

					orderDetail.id = response[i].id;
					orderDetail.client = response[i].client.name;
					orderDetail.agreement = response[i].agreement.description;
					orderDetail.date = myParseDate(response[i].deliveryDate);
					orderDetail.action = "<button type=\"button\" class=\"btn btn-sm btn-default view-row\"><span class=\"glyphicon glyphicon-eye-open\"></span> Detalle</button>";

					aaData.push(orderDetail);
				}

				$("#provisioningTable").bootgrid({
					caseSensitive: false,
					selection: true,
					multiSelect: true,
					keepSelection: true
				}).on("selected.rs.jquery.bootgrid", function(e, rows) {
					for (var i = 0; i < rows.length; i++) {
						requestsToApprove.push(rows[i].id);
					}
				}).on("deselected.rs.jquery.bootgrid", function(e, rows) {
					for (var i = 0; i < rows.length; i++) {
						for(var j = requestsToApprove.length - 1; j >= 0; j--) {
							if(requestsToApprove[j] === rows[i].id) {
								requestsToApprove.splice(j, 1);
							}
						}
					}
				});
				$("#provisioningTable").bootgrid("clear");
				$("#provisioningTable").bootgrid("append", aaData);
				$("#provisioningTable").bootgrid("search", $(".search-field").val());
			},
			error: function(response) {
			}
		});
	});
	
	$("#cleanButton").click(function() {
		$('#clientSearch').val('').trigger('chosen:updated');
		$('#agreementSearch').val('').trigger('chosen:updated');
	});
	
	$("#confirmButton").click(function() {
		requestsToApprove = jQuery.unique(requestsToApprove);
		if(requestsToApprove.length > 0){
			$.ajax({
				url: "authorizeProvisioningRequests.do",
				type: "POST",
				contentType: "application/json",
				data: JSON.stringify(requestsToApprove),
				async: true,
				beforeSend : function() {
                	$.blockUI({ message: 'Espere un Momento por favor...' });
                },
                success: function(response) {
                var msgType = "success";
                var message = "";
                $.each(response, function (index, result) {
                	if (result.errorMessages.length > 0) {
                		$.each(result.errorMessages, function (index, value) {
                		    message += "<strong><p>" + value + "</p></strong>";
                        });
                        msgType = "warning";
                	 }
                	if (result.successMessages.length > 0) {
                	    $.each(result.successMessages, function (index, value) {
                			message += "<strong><p>" + value + "</p></strong>";
                		});
                	}
                });
				myReload(msgType, message);
                },
                error: function(jqXHR, textStatus, errorThrown) {
                	myGenericError();
                },
                complete: function(jqXHR, textStatus) {
                	$.unblockUI();
                }
			});
		}else{
			myShowAlert('info', 'Seleccione al menos un Pedido para AUTORIZAR');
		}
	});
};