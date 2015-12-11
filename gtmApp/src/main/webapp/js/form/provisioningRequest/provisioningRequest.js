
var ProvisioningRequest = function() {
	
	var productId = null;
	var productDescription = null;
	var cold = null;
	var avaiableAmount = null;
	var productAmount = null;
	var productDetails = [];
	
	var isUpdate = false;
	
	var isAdd = true;
	var currentRowElement = null;
	
	var addAffiliateModalFormValidator = null;
	var amountFormValidator = null;
	
	var isButtonConfirm = false;
	
	if($("#provisioningId").val() != ""){
		isUpdate = true;
	}
	
	var cleanAddAffiliateModal = function() {
		$("#addAffiliateModal input").val("");
		$("#addAffiliateModal select").val("");
		myResetForm($("#addAffiliateModalForm")[0], addAffiliateModalFormValidator);
	};
	
	var cleanAmountModal = function() {
		myResetForm($("#productAmountModalForm")[0], amountFormValidator);
	};
	
	var cleanProductInput = function() {
		$('#productInput').val("");
	};

	$("#affiliateDocumentTypeSelect").chosen(
		{
			width: '100%' /* desired width */
		});


	var validateForm = function() {
		var form = $("#provisioningRequestForm");
		$("#provisioningRequestForm").validate({
			rules: {
				client: {
					required: true
				},
				deliveryLocation: {
					required: true
				},
				agreement: {
					required: true
				},
				affiliate: {
					required: true
				},
				deliveryDate: {
					required: true
				}
			},
			showErrors: myShowErrors,
			onsubmit: false
		});
		return form.valid();
	};
	
//	var resetForm = function() {
//		$('#agreementInput').val('').trigger('chosen:updated');
//		$('#clientInput').val('').trigger('chosen:updated');
//		
//		$('#affiliateInput').val('').trigger('chosen:updated');
//		$("#affiliateInput").attr("disabled", true);
//		$("#affiliateInput").attr("affiliateId", "");
//		
//		$('#deliveryLocationInput').val('').trigger('chosen:updated');
//		$("#deliveryDateInput").datepicker().datepicker("setDate", currentDate);
//		$('#logisticsOperatorInput').val('').trigger('chosen:updated');
//		
//		$("#productInput").val('');
//		$("#productInput").attr("disabled", true);
//		
//		$("#commentTextarea").val('');
//		$("#productTableBody").html('');
//		productDetails = [];
//	};
	
	var validateAddAffiliateModalForm = function() {
		var form = $("#addAffiliateModalForm");
		addAffiliateModalFormValidator = form.validate({
			rules: {
				affiliateCode: {
					required: true,
					digits: true
				},
				affiliateSurname: {
					required: true,
					letterswithbasicpunc: true
				},
				affiliateName: {
					required: true,
					letterswithbasicpunc: true
				},
				affiliateDocument: {
					digits: true
				}
			},
			showErrors: myShowErrors,
			onsubmit: false
		});
		return form.valid();
	};
	
	var validateProductAmountForm = function() {
		var form = $("#productAmountModalForm");
		amountFormValidator = form.validate({
			rules: {
				productAmount: {
					required: true,
					digits: true,
					minValue: 0
				}
			},
			showErrors: myShowErrors,
			onsubmit: false
		});
		return form.valid();
	};

	if(isUpdate){
		$.ajax({
			url: "getProvisioningRequestDetails.do",
			type: "GET",
			async: false,
			data: {
				provisioningId: $("#provisioningId").val()
			},
			success: function(response) {
				for (var i = 0, l = response.length; i < l; ++i) {
					productDetails.push({
						productId: response[i].product.id,
						amount:  response[i].amount
					});
					var aaData = [];
					var row = {
						description: response[i].product.code + " - " + response[i].product.description,
						amount: response[i].amount,
						command: "<span class='span-productId' style='display:none'>" + response[i].product.id + "</span>"+
						"<button type=\"button\" class=\"btn btn-sm btn-default edit-row\"><span class=\"glyphicon glyphicon-pencil\"></span></button>"+
						"<button type=\"button\" class=\"btn btn-sm btn-default delete-row\"><span class=\"glyphicon glyphicon-trash\"></span></button>"
					};
					aaData.push(row);
					$("#productTable").bootgrid("append", aaData);
				}
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myGenericError();
			}
		});
	}
	
	$("#affiliateInput").select2({
		//allowClear: true,
	    placeholder: "Buscar afiliado...",
	    minimumInputLength: 3,
	    initSelection : function (element, callback) {
            var data = {
				id: $("#affiliateId").val(),
				text:  $("#affiliateCode").val() + "-" + $("#affiliateName").val() + " " + $("#affiliateSurname").val()
            };
            callback(data);
	    },
	    ajax: {
	        url: "getAffiliates.do",
	        dataType: 'json',
	        quietMillis: 250,
	        data: function (term, page) { // page is the one-based page number tracked by Select2
	            return {
	                term: term, //search term
	                pageNumber: page, // page number
	                pageSize: 10, // page number
	                clientId: parseInt($("#clientInput").val()),
	                active: true
	            };
	        },
	        results: function (data, page, query) {
	        	var more = (data.length == 10); // whether or not there are more results available
	 
	        	var parsedResults = [];
	        	
	        	$.each(data, function(index, value) {
	        		parsedResults.push({
	                    id: value.id, 
	                    text:  value.code + "-" + value.name + " " + value.surname
	                });
	        	});
	        	
	            // notice we return the value of more so Select2 knows if more results can be loaded
	            return { results: parsedResults, more: more };
	        }
	    },
	    formatResult: function(data) { 
	        return "<div class='select2-user-result'>" + data.text + "</div>"; 
	    },
	    formatSelection: function(data) { 
	        return data.text;
	    }
	}).select2('val', []);
	
	$("#affiliateInput").select2("enable", false);
	
	$('.select2-input').on('keydown', function(e) {
		   if(e.keyCode === 121) {
			   cleanAddAffiliateModal();
				$('#addAffiliateModal').modal('show');
		   }
		});

	$('#deliveryDateButton').click(function() {
		$("#deliveryDateInput").datepicker().focus();
	});
	
	$("#productInput").autocomplete({
		source: function(request, response) {
			$.ajax({
				url: "getProductFromStock.do",
				type: "GET",
				async: false,
				data: {
					term: request.term,
					agreementId: $("#agreementInput").val()
				},
				success: function(data) {
					var array = $.map(data, function(item) {
						var cold = " Frio: ";
						cold += item.cold == true ? "Si" : "No";
						return {
							id:	item.id,
							label: item.code + " - " + item.description + " - " + item.brand.description + " - " + item.monodrug.description + " - " + cold,
							value: item.code + " - " + item.description,
							cold: item.cold
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
			if (!productEntered(ui.item.id)) {
				productId = ui.item.id;
				productDescription = ui.item.value;
				isAdd = true;
				cold = ui.item.cold;
				
				$('#productInput').data("title", "").removeClass("has-error").tooltip("destroy");
				$("#productInput").val(productDescription);
				$('#amountModal').modal('show');
			} else {
				myShowAlert('danger', 'Producto ya Ingresado');
				$("#productInput").val("");
			}
			return false;
	    },
		minLength: 3,
		autoFocus: true
	});
	
	$("#affiliateDocumentTypeInput").autocomplete({
		source: [ "DNI", "LC", "LE" ],
		minLength: 0,
		autoFocus: true,
		appendTo: "#addAffiliateModal"
	}).focus(function () {
		$(this).autocomplete("search", $("#affiliateDocumentTypeInput").val());
	});
	
	$('#amountModal').on('shown.bs.modal', function () {
		getStockAmount();
		if(cold == true){
			$('#productColdInput').text("Si");
		}else{
			$('#productColdInput').text("No");
		}
		$('#productDescriptionInput').text(productDescription);
		$('#productAmountAvaiableInput').text(avaiableAmount);
	    $('#productAmountInput').focus();
	});

	var getStockAmount = function() {
		$.ajax({
			url: "getProductAmount.do",
			type: "GET",
			async: false,
			data: {
				productId: productId,
				agreementId: $("#agreementInput").val(),
				provisioningId: $("#provisioningId").val()
			},
			success: function (response) {
				avaiableAmount = response;
			},
			error: function (jqXHR, textStatus, errorThrown) {
				myGenericError();
			}
		});
	};

	var getProduct = function() {
		$.ajax({
			url: "getProduct.do",
			type: "GET",
			async: false,
			data: {
				productId: productId,
			},
			success: function (response) {
				return response;
			},
			error: function (jqXHR, textStatus, errorThrown) {
				myGenericError();
			}
		});
	};
	
	$('#amountModal').on('hidden.bs.modal', function () {
	    cleanAmountModal();
	    cleanProductInput();
		$('#productInput').focus();
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
			$.ajax({
				url: "getProductAmount.do",
				type: "GET",
				async: false,
				data: {
					productId: productId,
					agreementId: $("#agreementInput").val(),
					provisioningId: $("#provisioningId").val()
				},
				success: function(response) {
					if (response != "" && response >= productAmount) {
						if(isAdd){
							$('#agreementInput').prop('disabled', true).trigger("chosen:updated");
							populateProductsDetailsTable();
							populateProductsDetails(productDetails, productAmount);
						}else{
							setNewAmount(productAmount);
						}
						$("#amountModal").modal('hide');
					}else{
						$('#productAmountAvaiableInput').text(response);
						$("#productAmountInput").tooltip("destroy").data("title", "Stock disponible: " + response).addClass("has-error").tooltip();
						return false;
					}
				},
				error: function(jqXHR, textStatus, errorThrown) {
					myGenericError();
				}
			});
		}
	});
	
	var populateProductsDetailsTable = function() {
		var aaData = [];
		var row = {
			description: productDescription,
			amount: productAmount,
			command: "<span class='span-productId' style='display:none'>" + productId + "</span>"+
			"<button type=\"button\" class=\"btn btn-sm btn-default edit-row\"><span class=\"glyphicon glyphicon-pencil\"></span></button>"+
			"<button type=\"button\" class=\"btn btn-sm btn-default delete-row\"><span class=\"glyphicon glyphicon-trash\"></span></button>"
		};
		aaData.push(row);
		$("#productTable").bootgrid("append", aaData);
	};
	
	var populateProductsDetails = function(productDetails, amount) {
		var productDetail = {};
		productDetail.productId = productId;
		productDetail.amount = amount;
		
		productDetails.push(productDetail);
	};
	
	$('#productTableBody').on("click", ".delete-row", function(){
		currentRowElement = this;
		$('#deleteRowConfirmationModal').modal();
	});
	
	$('#productTableBody').on("click", ".edit-row", function(){
		currentRowElement = this;
		isAdd = false;
		var parent = $(currentRowElement).parent().parent();
		productId = parent.find(".span-productId").text();
		$('#amountModal').modal();
	});
	
	var setNewAmount = function(productAmount){
		var parent = $(currentRowElement).parent().parent();
		var currentRow = $(".edit-row").index(currentRowElement);
		productDetails[currentRow].amount = productAmount;
		parent.find(".td-amount").text(productAmount);
	};
	
	$("#provisoningDeleteRowConfirmationButton").click(function() {
		var parent = $(currentRowElement).parent().parent();
		var rows = Array();
		rows[0] = parent.attr("data-row-id");
		$("#productTable").bootgrid("remove", rows);

		var currentRow = $(".delete-row").index(currentRowElement);
		productDetails.splice(currentRow, 1);

		if (productDetails.length == 0) {
			$('#agreementInput').prop('disabled', false).trigger("chosen:updated");
		}
	});
	
	$("#confirmButton").click(function() {
		if (validateForm()) {	
			if (productDetails.length > 0) {
				if($("#affiliateInput").val() != ""){
					isButtonConfirm = true;
					var jsonProvisioningRequest = {
						"id": $("#provisioningId").val(),
						"deliveryLocationId": $("#deliveryLocationInput").val(),
						"agreementId": $("#agreementInput").val(),
						"logisticsOperatorId": $("#logisticsOperatorInput").val(),
						"affiliateId": $("#affiliateInput").val(),
						"deliveryDate": $("#deliveryDateInput").val(),
						"comment": $("#commentTextarea").val(),
						"clientId": $("#clientInput").val(),
						"products": productDetails
					};
					
					$.ajax({
						url: "saveProvisioningRequest.do",
						type: "POST",
						contentType:"application/json",
						data: JSON.stringify(jsonProvisioningRequest),
						async: true,
						beforeSend : function() {
							$.blockUI({ message: 'Espere un Momento por favor...' });
						},
						success: function(response, textStatus, jqXHR) {
						},
						error: function(jqXHR, textStatus, errorThrown) {
							myGenericError();
						},
						complete: function(jqXHR, textStatus) {
							$.unblockUI();
							if(jqXHR.responseJSON.operationId != null){
								if (textStatus === 'success') {
									generateProvisioningRequestPDFReport(jqXHR.responseJSON.operationId,false);
								}
							}else{
								myShowAlert('danger', jqXHR.responseJSON.myOwnErrors, null,0);
							}
						}
					});
				}else{
					myShowAlert('warning', 'Por favor, seleccione un afiliado.');
				}
			} else {
				myShowAlert('warning', 'Por favor, ingrese al menos un producto.');
			}
		}
	});
	
	$('#addAffiliateModalForm').on('keypress', function(e) {
		//	Si la tecla presionada es 'ENTER'
	    if (e.keyCode === 13) {
			$("#addAffiliateModalAcceptButton").trigger('click');
			return false;
	    }
	});
	
	var existsAffiliate = function() {
		var exists = false;
		$.ajax({
			url: "existsAffiliate.do",
			type: "GET",
			async: false,
			data: {
				code: $("#affiliateCodeInput").val()
			},
			success: function(response) {
				exists = response;
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myGenericError();
			}
		});
		return exists;
	};

	$("#addAffiliateModalAcceptButton").click(function() {
		if (validateAddAffiliateModalForm()) {
			var clients =  [];
			clients.push(parseInt($("#clientInput").val()));
			var jsonAffiliate = {
				"code" : $("#affiliateCodeInput").val(),
				"name" : $("#affiliateNameInput").val(),
				"surname" : $("#affiliateSurnameInput").val(),
				"documentType" : $("#affiliateDocumentTypeSelect option:selected").val() || null,
				"document" : $("#affiliateDocumentInput").val() || null,
				"clients": clients,
				"active" : true
			};
			if (existsAffiliate()) {
				$("#addAffiliateModal").modal('hide');
				$("#addExistAffiliateConfirmationModal").modal('show');
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
						$("#agreementInput").trigger('chosen:activate');
					},
					error : function(jqXHR, textStatus,
									 errorThrown) {
						myGenericError("addAffiliateModalAlertDiv");
					}
				});
			}
		}
	});

	$("#addAffiliateToClient").click(function() {
		$.ajax({
			url : "addAffiliateToClient.do",
			type : "POST",
			data : {
				"code" : $("#affiliateCodeInput").val(),
				"clientId" : parseInt($("#clientInput").val())
			},
			async : true,
			success : function(response) {
				$("#addExistAffiliateConfirmationModal").modal('hide');
				$('#affiliateInput').select2("data",
					{
						id : response.id,
						text : response.code
						+ ' - '
						+ response.surname
						+ ' '
						+ response.name
					});
				$("#agreementInput").trigger('chosen:activate');
			},
			error : function(jqXHR, textStatus,
							 errorThrown) {
				myGenericError("addAffiliateModalAlertDiv");
			}
		});
	});

	var productEntered = function(productId) {
		for (var i = 0, l = productDetails.length; i < l; ++i) {
			if (productDetails[i].productId == productId) {
				return true;
			}
		}
		return false;
	};
	
	var hasChanged = function() {
		if($("#deliveryLocationInput").val()!= "" || 
				$("#agreementInput").val()!= "" || 
				$("#logisticsOperatorInput").val()!= ""|| 
				$("#affiliateInput").val() != "" ||
				$("#commentTextarea").val() != "" || 
				$("#clientInput").val()!= "" ||
				productDetails.length > 0){
			return true;
		}else{
			return false;
		}
	};

	$(window).bind("beforeunload", function(event) {
		if (hasChanged() && isButtonConfirm == false) {
			return "Existen cambios que no fueron confirmados.";
		} /*else {
			isButtonConfirm = false;
		}*/
	});

	$('#clientInput').on('change', function(evt, params) {
		if ($("#clientInput").val() == "") {
			$("#affiliateInput").select2("enable", false);
		} else {
			$("#affiliateInput").select2("enable", true);
			getDeliveryLocations($("#clientInput").val());
		}
		$("#affiliateInput").select2("val", "");
	});

	var getDeliveryLocations = function(clientId) {
		$.ajax({
			url : "getDeliveryLocationsByClient.do",
			type : "GET",
			data : {
				"clientId" : clientId
			},
			async : true,
			success : function(response) {
				$('#deliveryLocationInput').empty();
				$('#deliveryLocationInput').append('<option value=""></option>');
				for(var i = response.length-1; i >= 0 ; i--){
					$('#deliveryLocationInput').append('<option value='+ response[i].id + '>' + response[i].code + " - " + response[i].name +'</option>');
				}
				$('#deliveryLocationInput').trigger('chosen:updated');
			},
			error : function(jqXHR, textStatus,
							 errorThrown) {
				myGenericError("addAffiliateModalAlertDiv");
			}
		});
	};

	$('#agreementInput').on('change', function(evt, params) {
		if($("#agreementInput").val() == ""){
			$("#productInput").attr("disabled", true);
		}else{
			$("#productInput").attr("disabled", false);
		}
	});
	
	if(isUpdate){
		$("#divProvisioningId" ).show();
		$('#productInput').prop('disabled', true);
		$('#agreementInput').prop('disabled', true).trigger("chosen:updated");
		$("#productTableBody tr").each(function() {
			var productDetail = {};
			productDetail.productId = parseInt($(this).find(".span-productId").html());
			productDetail.amount = parseInt($(this).find(".td-amount").html());
			productDetails.push(productDetail);
		});
        $("#affiliateInput").select2("enable", true);
		getDeliveryLocations($('#clientInput').val());
		$("#productInput").attr("disabled", false);
	}else{
		$("#affiliateInput").select2("enable", false);
	}

	// TODO eliminar si con el keydown funciona correctamente.
	/*$("#provisioningRequestForm input, #provisioningRequestForm select").keypress(function(event) {
		return event.keyCode != 13;
	});*/

	$("#provisioningRequestForm input, #provisioningRequestForm select, #provisioningRequestForm textarea").keydown(function(event) {
		if(event.keyCode == 115) { // Presiono F4
			$("#confirmButton").trigger('click');
		} else {
			return event.keyCode != 13;
		}
	});
};