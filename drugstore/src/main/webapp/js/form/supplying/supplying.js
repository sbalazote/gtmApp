var Supplying = function() {

	var isOutOfStock = false;
	
	var currentRowElement = null;

	var addAffiliateModalFormValidator = null;
	var amountFormValidator = null;

	var currentDate = new Date();
	
	var batchExpirationDate = new OutputBatchExpirationDate();
	var serialized = new OutputSerialized();
	
	// Los orderDetails agrupados por fila
	var supplyingDetailGroup = [];
	
	// Mapa con los series que se cargaron por cada producto 
	var tempSerialNumberGroup = {};
	
	// Mapa con los stockIds que se cargaron por cada producto 
	var tempStockIdsGroup = {};
	
	var productDescription = "";
	var productAmount = "";
	var productId = "";
	var productType = "";
	var productGtin = "";
	
	// Para validar que no ingrese 2 veces el mismo producto 
	var productIds = [];
	
	// La fila donde se hace click (en el editar o borrar)
	var currentRow = 0;
	
	var isEdit = false;

	var isButtonConfirm = false;
	
	$("#productInput").attr("disabled", true);

	var cleanAddAffiliateModal = function() {
		$("#addAffiliateModal input").val("");
		$("#addAffiliateModal select").val("");
		myResetForm($("#addAffiliateModalForm")[0],
				addAffiliateModalFormValidator);
	};

	var cleanAmountModal = function() {
		myResetForm($("#productAmountModalForm")[0], amountFormValidator);
	};

	var cleanProductInput = function() {
		$('#productInput').val("");
	};

	var validateForm = function() {
		var form = $("#supplyingForm");
		$("#supplyingForm").validate({
			rules : {
				client : {
					required : true
				},
				affiliate : {
					required : true
				},
				agreement: {
					required: true
				},
				currentDate : {
					required : true
				}
			},
			showErrors : myShowErrors,
			onsubmit : false
		});
		return form.valid();
	};

	var validateAddAffiliateModalForm = function() {
		var form = $("#addAffiliateModalForm");
		addAffiliateModalFormValidator = form.validate({
			rules : {
				affiliateCode : {
					required : true,
					digits : true
				},
				affiliateClient : {
					required : true
				},
				affiliateSurname : {
					required : true,
					letterswithbasicpunc : true
				},
				affiliateName : {
					required : true,
					letterswithbasicpunc : true
				},
				affiliateDocument : {
					digits : true
				}
			},
			showErrors : myShowErrors,
			onsubmit : false
		});
		return form.valid();
	};

	var validateProductAmountForm = function() {
		var form = $("#productAmountModalForm");
		amountFormValidator = form.validate({
			rules : {
				productAmount : {
					required : true,
					digits : true,
					minValue : 0,
				}
			},
			showErrors : myShowErrors,
			onsubmit : false
		});
		return form.valid();
	};

	$("#affiliateInput").select2(
			{
				// allowClear: true,
				placeholder : "Buscar afiliado...",
				minimumInputLength : 3,
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
							clientId : parseInt($("#clientInput").val()),
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
								text : value.code + "-" + value.name + " "
										+ value.surname
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

	$("#affiliateInput").select2("enable", false);

	$('.select2-input').on('keydown', function(e) {
		if (e.keyCode === 121) {
			cleanAddAffiliateModal();
			$("#affiliateClientInput").val($("#clientInput option:selected").html());
			$('#addAffiliateModal').modal('show');
		}
	});

	$('#currentDateButton').click(function() {
		$("#currentDateInput").datepicker().focus();
	});

	$("#currentDateInput").datepicker().datepicker("setDate", currentDate);

	$('#currentDateButton').click(function() {
		$("#currentDateInput").datepicker().focus();
	});

	$("#productInput").autocomplete({
		source : function(request, response) {
			var url = isOutOfStock ? "getProducts.do" : "getProductoFromStock.do";
			var data = isOutOfStock ? { term: request.term, active: true } : { term : request.term, agreementId : $("#agreementInput").val(), };
			$.ajax({
				url : url,
				type : "GET",
				async : false,
				data : data,
				success : function(data) {
					var array = $.map(data, function(item) {
						return {
							id : item.id,
							label : item.code + " - " + item.description + " - " + item.brand.description + " - " + item.monodrug.description,
							value : item.code + " - " + item.description,
							gtin: item.lastGtin,
							type: item.type
						};
					});
					response(array);
				},
				error : function(jqXHR, textStatus, errorThrown) {
					myGenericError();
				}
			});
		},
		select : function(event, ui) {
			if (!productEntered(ui.item.id)) {
				productId = ui.item.id;
				productGtin = ui.item.gtin;
				productDescription = ui.item.value;
				productType = ui.item.type;
				$('#productInput').data("title", "").removeClass("has-error").tooltip("destroy");
				$("#productInput").val(productDescription);
				$('#amountModal').modal('show');
			} else {
				myShowAlert('danger', 'Producto ya Ingresado');
				$("#productInput").val("");
			}
			return false;
		},
		minLength : 3,
		autoFocus : true,
		response: function(event, ui) {
            if ((ui.content.length === 0) && (!isOutOfStock)) {
            	isOutOfStock = true;
            	noStockSupplyingAlert();
            }
        }
	});
	
	$('#productInput').keydown(function(e) {
	    if(e.keyCode == 121){ // F10
	    	$.ajax({
				url: "getProductFromStockBySerialOrGtin.do",
				type: "GET",
				data: {
					serial: $(this).val(),
					agreementId: $("#agreementInput").val()
				},
				success: function(response) {
					if (response != "") {
						if (!productEntered(response.id)) {
							productId = response.id;
                            if(serial.length == 13) {
                                productGtin = serial;
                            }else{
                                productGtin = response.lastGtin;
                            }
							productDescription = response.code + ' - ' + response.description;
							productType = response.type;
							
							$('#productInput').data("title", "").removeClass("has-error").tooltip("destroy");
							
							$("#productInput").val(productDescription);
							$('#amountModal').modal('show');
						} else {
							myShowAlert('danger', 'Producto ya Ingresado');
							$("#productInput").val("");
						}
					} else {
						$('#productInput').tooltip("destroy").data("title", "Producto Inexistente").addClass("has-error").tooltip();
						$('#productInput').focus();
					}
					return false;
				},
				error: function(jqXHR, textStatus, errorThrown) {
					myGenericError();
				}
			});
	    }
	});

	$("#affiliateDocumentTypeInput").autocomplete({
		source : [ "DNI", "LC", "LE" ],
		minLength : 0,
		autoFocus : true,
		appendTo : "#addAffiliateModal"
	}).focus(function() {
		$(this).autocomplete("search", $("#affiliateDocumentTypeInput").val());
	});

	$('#amountModal').on('shown.bs.modal', function() {
		$('#productAmountInput').focus();
	});

	$('#amountModal').on('hidden.bs.modal', function() {
		cleanAmountModal();
		cleanProductInput();
	});
	
	$('#agreementInput').on('change', function(evt, params) {
		if($("#agreementInput").val() == ""){
			$("#productInput").attr("disabled", true);
		}else{
			$("#productInput").attr("disabled", false);
		}
	});

	$('#amountModal').on('keypress', function(e) {
		if (e.keyCode === 13) {
			$("#amountModalAcceptButton").trigger('click');
			return false;
		}
	});

	$("#amountModalAcceptButton").click(function() {
		if (validateProductAmountForm()) {
			productAmount = parseInt($("#productAmountInput").val());
			if (!isOutOfStock) {
				$.ajax({
					url : "getProductAmount.do",
					type : "GET",
					async : false,
					data : {
						productId : productId,
						agreementId : $("#agreementInput").val(),
						provisioningId : null
					},
					success : function(response) {
						if (response != "" && response >= productAmount) {
							$('#agreementInput').prop('disabled', true).trigger("chosen:updated");
							openModal(null);
							$("#amountModal").modal('hide');
						} else {
							$("#productAmountInput").tooltip("destroy").data("title", "Stock insuficiente").addClass("has-error").tooltip();
							return false;
						}
					},
					error : function(jqXHR, textStatus, errorThrown) {
						myGenericError();
					}
				});
			} else {
				$('#agreementInput').prop('disabled', true).trigger("chosen:updated");
				openModal(null);
				$("#amountModal").modal('hide');
			}
		}
	});

	var openModal = function(preloadedData) {
		if (productType == "BE") {
			batchExpirationDate.setPreloadedProduct(productDescription);
			batchExpirationDate.setPreloadedProductId(productId);
			batchExpirationDate.setPreloadedAmount(productAmount);
			batchExpirationDate.setPreloadedData(preloadedData);
			batchExpirationDate.setPreloadedStockIds(tempStockIdsGroup[productId]);
			
			batchExpirationDate.preloadModalData();
			$('#batchExpirationDateModal').modal('show');
			
		} else {
			serialized.setPreloadedProduct(productDescription);
			serialized.setPreloadedProductId(productId);
			serialized.setPreloadedProductType(productType);
			serialized.setPreloadedAmount(productAmount);
			serialized.setPreloadedData(preloadedData);
			serialized.setProductSelectedGtin(productGtin);
			serialized.setTempSerialNumbers(tempSerialNumberGroup[productId]);
			
			serialized.preloadModalData();
			$('#serializedModal').modal('show');
		}
	};
	
	var populateInputDetails = function(orderDetails, serialNumber, batch, expirationDate, amount, gtin) {
		var orderDetail = {
			"productId": productId,
			"serialNumber": serialNumber,
			"batch": batch,
			"expirationDate": expirationDate,
			"amount": amount,
			"gtin": gtin,
			"inStock": inStock
		};
		orderDetails.push(orderDetail);
	};
	
	$('#productTableBody').on("click", ".edit-button", function() {
		var parent = $(this).parent().parent();
	
		currentRow = $(".edit-button").index(this);
		productDescription = parent.find(".td-description").html();
		productAmount = parent.find(".td-amount").html();
		productId = parent.find(".span-productId").html();
		productType = parent.find(".span-productType").html();
		productGtin = parent.find(".span-productGtin").html();
		isEdit = true;
		
		openModal(supplyingDetailGroup[currentRow]);
	});
	
	var populateProductsDetailsTable = function() {
		var tableRow = "<tr><td class='td-description'>" + productDescription + "</td>" +
		"<td class='td-amount'>" + productAmount + "</td>" +
		"<td>" +
		"<span class='span-productId' style='display:none'>" + productId + "</c:out></span>" +
		"<span class='span-productType' style='display:none'>" + productType + "</c:out></span>" + 
		"<span class='span-productGtin' style='display:none'>" + productGtin + "</c:out></span>" +
		"<a href='javascript:void(0);' class='edit-button'>Editar</a>  " +
		"<a href='javascript:void(0);' class='delete-row'>Eliminar</a></td></tr>";
		$("#productTableBody").append(tableRow);
	};
	
	$("#batchExpirationDateAcceptButton").click(function() {
		remainingAmount = $('#batchExpirationDateRemainingAmountLabel').text();
		if (remainingAmount == 0) {
			
			var amounts = $("#batchExpirationDateTable td.amount");
			var batchs = $("#batchExpirationDateTable td.batch");
			var expirationDates = $("#batchExpirationDateTable td.expirationDate");
			var stockIds = $("#batchExpirationDateTable span.stockId");
			
			var orderDetails = [];
			var tempStockIds = [];
			
			for (var i = 0; i < amounts.length; i++) {
				populateInputDetails(orderDetails, null, batchs[i].innerHTML, expirationDates[i].innerHTML, amounts[i].innerHTML, productGtin);
				tempStockIds[i] = parseInt(stockIds[i].innerHTML);
			}
			if(isEdit){
				supplyingDetailGroup[currentRow] = orderDetails;
			}else{
				supplyingDetailGroup.push(orderDetails);
				populateProductsDetailsTable();
			}
				
			tempStockIdsGroup[productId] = tempStockIds;
			
			$("#batchExpirationDateModal").modal("hide");
			$(".alert").hide();
		} else {
			myShowAlert('danger', 'No se ha ingresado la totalidad de productos requeridos. Por favor ingrese los restantes.', "batchExpirationDateModalAlertDiv");
		}
	});

	$("#serializedAcceptButton").click(function() {
		remainingAmount = $('#serializedRemainingAmountLabel').text();
		if (remainingAmount == 0) {
			
            var gtins = $("#serializedTable td.gtin");
			var serialNumbers = $("#serializedTable td.serialNumber");
			var batchs = $("#serializedTable td.batch");
			var expirationDates = $("#serializedTable td.expirationDate");
			
			var orderDetails = [];
			var tempSerialNumber = [];
			
			for (var i = 0; i < serialNumbers.length; i++) {
				populateInputDetails(orderDetails, serialNumbers[i].innerHTML, batchs[i].innerHTML, expirationDates[i].innerHTML, 1, gtins[i].innerHTML);
				tempSerialNumber[i] = serialNumbers[i].innerHTML;
			}

			if(isEdit){
				supplyingDetailGroup[currentRow] = orderDetails;
			}else{
				supplyingDetailGroup.push(orderDetails);
				populateProductsDetailsTable();
			}
			tempSerialNumberGroup[productId] = tempSerialNumber;
			
			$("#serializedModal").modal("hide");
			$(".alert").hide();
		} else {
			myShowAlert('danger', 'No se ha ingresado la totalidad de productos requeridos. Por favor ingrese los restantes.', "serializedModalAlertDiv");
		}
	});

	$('#productTableBody').on("click", ".delete-row", function() {
		currentRowElement = this;
		$('#deleteRowConfirmationModal').modal();
	});

	$('#productTableBody').on("click", ".edit-row", function() {
		currentRowElement = this;
		isAdd = false;
		var parent = $(currentRowElement).parent().parent();
		productId = parent.find(".span-productId").text();
		$('#amountModal').modal();
	});

	$("#provisoningDeleteRowConfirmationButton").click(
			function() {
				var parent = $(currentRowElement).parent().parent();
				var currentRow = $(".delete-row").index(currentRowElement);
				productDetails.splice(currentRow, 1);
				parent.remove();
				if (productDetails.length == 0) {
					$('#agreementInput').prop('disabled', false).trigger("chosen:updated");
				}
			});

	
	
	$("#confirmButton").click(function() {
		if (validateForm()) {
			if (supplyingDetailGroup.length > 0) {
				if ($("#affiliateInput").val() != "") {
					$(this).attr("disabled", true);
					var jsonSupplying = {
							"id" : $("#supplyingId").val(),
							"clientId" : $("#clientInput").val(),
							"affiliateId" : $("#affiliateInput").val(),
							"agreementId": $("#agreementInput").val(),
							"date" : $("#currentDateInput").val(),
							"supplyingDetails" : []
					};

					for (var i = 0, lengthI = supplyingDetailGroup.length; i < lengthI; i++) {
					for (var j = 0; lengthJ = supplyingDetailGroup[i].length, j < lengthJ; j++) {
						jsonOutput.outputDetails.push(supplyingDetailGroup[i][j]);
					}
				}
				
					isButtonConfirm = true;
	
					$.ajax({
						url : "saveSupplying.do",
						type : "POST",
						contentType : "application/json",
						data : JSON.stringify(jsonSupplying),
						async : true,
						success : function(response) {
							myReload("success",	"Se ha registrado la solicitud de abastecimiento n&uacute;mero: " + response.id);
						},
						error : function(jqXHR,	textStatus, errorThrown) {
							myGenericError();
						}
					});
				} else {
					myShowAlert('danger', 'Por favor, seleccione un afiliado.');
				}
			} else {
				myShowAlert('danger', 'Por favor, ingrese al menos un producto.');
			}
		}
	});

	$('#addAffiliateModalForm').on('keypress', function(e) {
		// Si la tecla presionada es 'ENTER'
		if (e.keyCode === 13) {
			$("#addAffiliateModalAcceptButton").trigger('click');
			return false;
		}
	});

	var existsAffiliate = function() {
		var exists = false;
		$.ajax({
			url : "existsAffiliate.do",
			type : "GET",
			async : false,
			data : {
				code : $("#affiliateCodeInput").val()
			},
			success : function(response) {
				exists = response;
			},
			error : function(jqXHR, textStatus, errorThrown) {
				myGenericError();
			}
		});
		return exists;
	};

	$("#addAffiliateModalAcceptButton").click(function() {
		if (validateAddAffiliateModalForm()) {

			var jsonAffiliate = {
					"code" : $("#affiliateCodeInput").val(),
					"name" : $("#affiliateNameInput").val(),
					"surname" : $("#affiliateSurnameInput").val(),
					"documentType" : $("#affiliateDocumentTypeInput").val(),
					"document" : $("#affiliateDocumentInput").val(),
					"clientId" : $("#clientInput").val(),
					"active" : true
			};
			if (existsAffiliate()) {
				$('#addAffiliateModalAlertDiv').html(
						'<div class="alert alert-danger alert-block fade in">'
						+ '<button type="button" class="close" data-dismiss="alert">'
						+ '&times;</button>C&oacute;digo existente. Por favor, ingrese uno diferente.</div>');
			} else {
				$.ajax({
					url : "saveAffiliate.do",
					type : "POST",
					contentType : "application/json",
					data : JSON.stringify(jsonAffiliate),
					async : true,
					success : function(response) {
						$("#addAffiliateModal").modal('hide');
						$('#affiliateInput').select2("data",
							{
							id : response.id,
							text : response.code
							+ ' - '
							+ response.surname
							+ ' '
							+ response.name
								});
					},
					error : function(jqXHR, textStatus,
							errorThrown) {
						myGenericError("addAffiliateModalAlertDiv");
					}
				});
			}
		}
	});

	var productEntered = function(productId) {
		for (var i = 0, l = productIds.length; i < l; ++i) {
			if (productIds[i] == productId) {
				return true;
			}
		}
		return false;
	};

	var hasChanged = function() {
		if ($("#deliveryLocationInput").val() != ""
				|| $("#agreementInput").val() != ""
				|| $("#logisticsOperatorInput").val() != ""
				|| $("#affiliateInput").val() != ""
				|| $("#commentTextarea").val() != ""
				|| $("#clientInput").val() != "" || productDetails.length > 0) {
			return true;
		} else {
			return false;
		}
	};

	$(window).bind("beforeunload", function(event) {
		if (hasChanged() && isButtonConfirm == false) {
			return "Existen cambios que no fueron confirmados.";
		} else {
			isButtonConfirm = false;
		}
	});

	$('#clientInput').on('change', function(evt, params) {
		if ($("#clientInput").val() == "") {
			$("#affiliateInput").select2("val", "");
		} else {
			$("#affiliateInput").select2("enable", true);
		}
	});

	$("#provisioningRequestForm input, #provisioningRequestForm select").keypress(function(event) {
		return event.keyCode != 13;
	});
};