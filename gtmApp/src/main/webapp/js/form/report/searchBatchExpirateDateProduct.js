
SearchBatchExpirateDateProduct = function() {
	var productId;
	var orderId;
	var outputId;
	var deliveryNoteId;

	$("#dateSearch").datepicker();
	
	// Product autocomplete
	
	$("#productInput").autocomplete({
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
                        var cold = " Frio: ";
                        cold += item.cold == true ? "Si" : "No";
						return {
							id:	item.id,
							label: item.code + " - " + item.description + " - " + item.brand.description + " - " + item.monodrug.description + " - " + cold,
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
	    },
		minLength: 3,
		autoFocus: true
	});
	
	var validateForm = function() {
		var form = $("#searchSerializedProductForm");
		form.validate({
			rules: {
				productInput: {
					required: true
				},
				batchSearch: {
					required: true
				},
				dateSearch: {
					required: true,
					dateITA: true
				}
			},
			showErrors: myShowErrors,
			onsubmit: false
		});
		return form.valid();
	};
	
	$("#cleanButton").click(function() {
		$('#productInput').val('');
		productId = "";
		$('#batchSearch').val('');
        $('#productInput').data("title", "").removeClass("has-error").tooltip("destroy");
        $('#batchSearch').data("title", "").removeClass("has-error").tooltip("destroy");
		if($("#dateSearch").val()!= ""){
			$.datepicker._clearDate('#dateSearch');
		}
	});
	
	$("#searchButton").click(function() {
        myHideAlert();
		if(validateForm()){
			$.ajax({
				url: "getBatchExpirateDateProduct.do",
				type: "GET",
				contentType:"application/json",
				async: false,
				data: {
					productId: productId,
					batch: $("#batchSearch").val(),
					expirateDate: $("#dateSearch").val()
				},
				success: function(response) {
                    $('#divMovements').hide();

                    if(response.inputs.length > 0 || response.outputs.length > 0 || response.orders.length > 0 || response.deliveryNotes.length > 0 || response.supplyings.length > 0){
                        var aaData = [];
                        if(response.inputs != null){
                            if(response.inputs.length > 0){
                                $('#divMovements').show();
                            }
                            for (var i = 0, l = response.inputs.length; i < l; ++i) {
                                var audit = {
                                    id: 0,
                                    action: "",
                                    user: "",
                                    date: "",
                                    view: ""
                                };
                                audit.id = response.inputs[i].operationId;
                                audit.action = "Ingreso";
                                audit.user = response.inputs[i].username;
                                audit.date = response.inputs[i].date;
                                audit.view = "<button type=\"button\" class=\"btn btn-sm btn-default view-row-input\"><span class=\"glyphicon glyphicon-eye-open\"></span> Detalle</button>";
                                aaData.push(audit);
                            }
                        }

                        if(response.outputs != null){
                            if(response.outputs.length > 0){
                                $('#divMovements').show();
                            }
                            for (var i = 0, l = response.outputs.length; i < l; ++i) {
                                var audit = {
                                    id: 0,
                                    action: "",
                                    user: "",
                                    date: "",
                                    view: ""
                                };
                                audit.id = response.outputs[i].operationId;
                                audit.action = "Egreso";
                                audit.user = response.outputs[i].username;
                                audit.date = response.outputs[i].date;
                                audit.view = "<button type=\"button\" class=\"btn btn-sm btn-default view-row-output\"><span class=\"glyphicon glyphicon-eye-open\"></span> Detalle</button>";
                                aaData.push(audit);
                            }
                        }

                        if(response.orders != null){
                            if(response.orders.length > 0){
                                $('#divMovements').show();
                            }
                            for (var i = 0, l = response.orders.length; i < l; ++i) {
                                var audit = {
                                    id: 0,
                                    action: "",
                                    user: "",
                                    date: "",
                                    view: ""
                                };
                                audit.id = response.orders[i].operationId;
                                audit.action = "Armado";
                                audit.user = response.orders[i].username;
                                audit.date = response.orders[i].date;
                                audit.view = "<button type=\"button\" class=\"btn btn-sm btn-default view-row-order\"><span class=\"glyphicon glyphicon-eye-open\"></span> Detalle</button>";
                                aaData.push(audit);
                            }
                        }

                        if(response.deliveryNotes != null){
                            if(response.deliveryNotes.length > 0){
                                $('#divMovements').show();
                            }
                            for (var i = 0, l = response.deliveryNotes.length; i < l; ++i) {
                                var audit = {
                                    id: 0,
                                    action: "",
                                    user: "",
                                    date: "",
                                    view: ""
                                };
                                audit.id = response.deliveryNotes[i].operationId;
                                audit.action = "Remito";
                                audit.user = response.deliveryNotes[i].username;
                                audit.date = response.deliveryNotes[i].date;
                                audit.view = "<button type=\"button\" class=\"btn btn-sm btn-default view-row-deliveryNote\"><span class=\"glyphicon glyphicon-eye-open\"></span> Detalle</button>";
                                aaData.push(audit);
                            }
                        }

                        if(response.supplyings != null){
                            if(response.supplyings.length > 0){
                                $('#divMovements').show();
                            }
                            for (var i = 0, l = response.supplyings.length; i < l; ++i) {
                                var audit = {
                                    id: 0,
                                    action: "",
                                    user: "",
                                    date: "",
                                    view: ""
                                };
                                audit.id = response.supplyings[i].operationId;
                                audit.action = "Dispensa";
                                audit.user = response.supplyings[i].username;
                                audit.date = response.supplyings[i].date;
                                audit.view = "<button type=\"button\" class=\"btn btn-sm btn-default view-row-supplying\"><span class=\"glyphicon glyphicon-eye-open\"></span> Detalle</button>";
                                aaData.push(audit);
                            }
                        }
                        $("#movementsTable").bootgrid({
                            caseSensitive: false
                        });
                        $("#movementsTable").bootgrid("clear");
                        $("#movementsTable").bootgrid("append", aaData);
                        $("#movementsTable").bootgrid("search", $(".search-field").val());

                        var params = '&productId=' + productId +
                            '&batch=' + $("#batchSearch").val() +
                            '&expirateDate=' + $("#dateSearch").val();

                        var exportHTML = exportQueryTableHTML("./rest/batchExpirationDateProducts", params);
                        var searchHTML = $("#divMovements .search");

                        if (searchHTML.prev().length == 0) {
                            searchHTML.before(exportHTML);
                        } else {
                            searchHTML.prev().html(exportHTML);
                        }
                    }else{
                        myEmptyQueryAlert();
                    }

                },
				error: function(jqXHR, textStatus, errorThrown) {
					myGenericError();
				}
			});
			
		}
	});

    //Consulta de Ingresos

    $('#movementsTableBody').on("click", ".view-row-input", function() {
        var parent = $(this).parent().parent();
        inputId = parent.find("td:first-child").html();

        showInputModal(inputId);
    });

    //Consulta de Egresos

    $('#movementsTableBody').on("click", ".view-row-output", function() {
        var parent = $(this).parent().parent();
        outputId = parent.find("td:first-child").html();

        showOutputModal(outputId);
    });

    //Consulta de Armado

    $('#movementsTableBody').on("click", ".view-row-order", function() {
        var parent = $(this).parent().parent();
        orderId = parent.find("td:first-child").html();
        showOrderModal(orderId);
    });

    //Consulta de Remito
    $('#movementsTableBody').on("click", ".view-row-deliveryNote", function() {
        var parent = $(this).parent().parent();
        deliveryNoteId = parent.find("td:first-child").html();

        showDeliveryNoteByIdModal(deliveryNoteId);
    });

    //Consulta de Dispensa
    $('#movementsTableBody').on("click", ".view-row-supplying", function() {
        var parent = $(this).parent().parent();
        supplyingId = parent.find("td:first-child").html();

        showSupplyingModal(supplyingId);
    });
};