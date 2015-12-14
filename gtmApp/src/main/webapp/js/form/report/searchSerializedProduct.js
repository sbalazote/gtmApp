
SearchSerializedProduct = function() {
	var productId;
	var orderId;
	var outputId;
	var deliveryNoteId;
	var supplyingId;

    var productId;
	
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
	    },
		minLength: 3,
		autoFocus: true
	});
	
	var validateForm = function() {
		var form = $("#searchSerializedProductForm");
		form.validate({
			rules: {
				serialNumberSearch: {
					digits: true,
                    required: true
				},
				productInput: {
                    required: true
				}
			},
			showErrors: myShowErrors,
			onsubmit: false
		});
		return form.valid();
	};
	$("#searchButton").click(function() {
        myHideAlert();
        $("#cleanSerialButton").trigger("click");
        if(validateForm()){
            searchProduct(productId,$("#serialNumberSearch").val());
        }
	});

    $("#cleanButton").click(function() {
        $('#productInput').val('');
        $('#serialNumberSearch').val('');
        $('#productInput').data("title", "").removeClass("has-error").tooltip("destroy");
        $('#serialNumberSearch').data("title", "").removeClass("has-error").tooltip("destroy");
        productId = "";
        $("#movementsTable").bootgrid("clear");
        $('#divMovements').hide();
    });

    $("#searchSerialButton").click(function() {
        myHideAlert();
        $("#cleanButton").trigger("click");
        searchSerialParsed();
    });

    $("#cleanSerialButton").click(function() {
        productId = "";
        $('#serialParserSearch').val('');
        $("#movementsTable").bootgrid("clear");
        $('#divMovements').hide();
        $('#serialParserSearch').data("title", "").removeClass("has-error").tooltip("destroy");
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

    var searchProduct = function(productId,serial){
        $.ajax({
            url: "getSerializedProductAudit.do",
            type: "GET",
            contentType:"application/json",
            async: false,
            data: {
                productId: productId,
                serialNumber: serial
            },
            success: function(response) {
                $('#divMovements').show();

                if(response.inputs.length > 0 || response.outputs.length > 0 || response.orders.length > 0 || response.deliveryNotes.length > 0 || response.supplyings.length > 0){
                    var aaData = [];
                    if(response.inputs != null){
                        /*if(response.inputs.length > 0){
                            $('#divMovements').show();
                        }*/
                        for (var i = 0, l = response.inputs.length; i < l; ++i) {
                            var audit = {
                                id: 0,
                                action: "",
                                user: "",
                                date: "",
                                view: ""
                            };
                            audit.id = response.inputs[i].operationId;
                            audit.action = response.inputs[i].role;
                            audit.user = response.inputs[i].username;
                            audit.date = response.inputs[i].date;
                            audit.view = "<button type=\"button\" class=\"btn btn-sm btn-default view-row-input\"><span class=\"glyphicon glyphicon-eye-open\"></span> Detalle</button>";
                            aaData.push(audit);
                        }
                    }

                    if(response.outputs != null){
                        for (var i = 0, l = response.outputs.length; i < l; ++i) {
                            var audit = {
                                id: 0,
                                action: "",
                                user: "",
                                date: "",
                                view: ""
                            };
                            audit.id = response.outputs[i].operationId;
                            audit.action = response.outputs[i].role;
                            audit.user = response.outputs[i].username;
                            audit.date = response.outputs[i].date;
                            audit.view = "<button type=\"button\" class=\"btn btn-sm btn-default view-row-output\"><span class=\"glyphicon glyphicon-eye-open\"></span> Detalle</button>";
                            aaData.push(audit);
                        }
                    }

                    if(response.orders != null){
                        for (var i = 0, l = response.orders.length; i < l; ++i) {
                            var audit = {
                                id: 0,
                                action: "",
                                user: "",
                                date: "",
                                view: ""
                            };
                            audit.id = response.orders[i].operationId;
                            audit.action = response.orders[i].role;
                            audit.user = response.orders[i].username;
                            audit.date = response.orders[i].date;
                            audit.view = "<button type=\"button\" class=\"btn btn-sm btn-default view-row-order\"><span class=\"glyphicon glyphicon-eye-open\"></span> Detalle</button>";
                            aaData.push(audit);
                        }
                    }

                    if(response.deliveryNotes != null){
                        for (var i = 0, l = response.deliveryNotes.length; i < l; ++i) {
                            var audit = {
                                id: 0,
                                action: "",
                                user: "",
                                date: "",
                                view: ""
                            };
                            audit.id = response.deliveryNotes[i].operationId;
                            audit.action = response.deliveryNotes[i].role;
                            audit.user = response.deliveryNotes[i].username;
                            audit.date = response.deliveryNotes[i].date;
                            audit.view = "<button type=\"button\" class=\"btn btn-sm btn-default view-row-deliveryNote\"><span class=\"glyphicon glyphicon-eye-open\"></span> Detalle</button>";
                            aaData.push(audit);
                        }
                    }

                    if(response.supplyings != null){
                        for (var i = 0, l = response.supplyings.length; i < l; ++i) {
                            var audit = {
                                id: 0,
                                action: "",
                                operation: "",
                                user: "",
                                date: "",
                                view: ""
                            };
                            audit.id = response.supplyings[i].operationId;
                            audit.action = response.supplyings[i].role;
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
                        '&serialNumber=' + serial;

                    var exportHTML = exportQueryTableHTML("./rest/serializedProducts", params);
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

    };

	$('#serialParserSearch').keydown(function(e) {
		if(e.keyCode == 13){ // Presiono Enter
            searchSerialParsed($(this).val());
		}
	});

    var searchSerialParsed = function(serial){
        $.ajax({
            url: "parseSerial.do",
            type: "GET",
            data: {
                serial: $('#serialParserSearch').val()
            },
            success: function(response) {
                var serialFound = response.serialNumber;
                if (response != "") {
                    $.ajax({
                        url: "getProductBySerialOrGtin.do",
                        type: "GET",
                        data: {
                            serial: $('#serialParserSearch').val()
                        },
                        success: function(response) {
                            if (response != "") {
                                productId = response.id;
                                $('#serialParserSearch').val(response.description + " Serie: " + serialFound);
                                searchProduct(productId,serialFound);
                            } else
                            {
                                $('#serialParserSearch').tooltip("destroy").data("title", "Producto Inexistente o Inactivo").addClass("has-error").tooltip();
                                $('#serialParserSearch').val('');
                                $('#serialParserSearch').focus();
                            }
                            return false;
                        },
                        error: function(jqXHR, textStatus, errorThrown) {
                            myGenericError();
                        }
                    });
                } else
                {
                    $('#serialParserSearch').tooltip("destroy").data("title", "Producto Inexistente o Inactivo").addClass("has-error").tooltip();
                    $('#serialParserSearch').val('');
                    $('#serialParserSearch').focus();
                }
                return false;
            },
            error: function(jqXHR, textStatus, errorThrown) {
                myGenericError();
            }
        });
    };

    var productIdByUrl = getURLParameter("productId");
    var serialByUrl = getURLParameter("serial");

    if(productIdByUrl != undefined && serialByUrl != undefined){
        $.ajax({
            url: "getProduct.do",
            type: "GET",
            async: false,
            data: {
                productId: productIdByUrl
            },
            success: function(response) {
                $("#productInput").val(response.code + " - " + response.description + " - " + response.brand.description + " - " + response.monodrug.description);
                $("#serialNumberSearch").val(serialByUrl);
            },
            error: function(jqXHR, textStatus, errorThrown) {
                myGenericError();
            }
        });
        searchProduct(productIdByUrl,serialByUrl);
    }

};