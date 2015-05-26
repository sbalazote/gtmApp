var Supplying = function() {

	var productId = null;
	var productDescription = null;
	var productAmount = null;
	var productDetails = [];

	var isUpdate = false;

	var isAdd = true;
	var currentRowElement = null;

	var addAffiliateModalFormValidator = null;
	var amountFormValidator = null;
	
	var currentDate = new Date();

	var isButtonConfirm = false;

	if ($("#provisioningId").val() != "") {
		isUpdate = true;
	}

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

	$('.select2-input').on(
			'keydown',
			function(e) {
				if (e.keyCode === 121) {
					cleanAddAffiliateModal();
					$("#affiliateClientInput").val(
							$("#clientInput option:selected").html());
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
	
	$("#productInput").autocomplete(
			{
				source : function(request, response) {
					$.ajax({
						url : "getProductoFromStock.do",
						type : "GET",
						async : false,
						data : {
							term : request.term,
							agreementId : null,
						},
						success : function(data) {
							var array = $.map(data, function(item) {
								return {
									id : item.id,
									label : item.code + " - "
											+ item.description + " - "
											+ item.brand.description + " - "
											+ item.monodrug.description,
									value : item.code + " - "
											+ item.description,
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
						productDescription = ui.item.value;
						isAdd = true;

						$('#productInput').data("title", "").removeClass(
								"has-error").tooltip("destroy");
						$("#productInput").val(productDescription);
						$('#amountModal').modal('show');
					} else {
						myShowAlert('danger', 'Producto ya Ingresado');
					}
					return false;
				},
				minLength : 3,
				autoFocus : true
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

	$('#amountModal').on('keypress', function(e) {
		if (e.keyCode === 13) {
			$("#amountModalAcceptButton").trigger('click');
			return false;
		}
	});

	$("#amountModalAcceptButton").click(
			function() {
				if (validateProductAmountForm()) {
					productAmount = parseInt($("#productAmountInput").val());
					$.ajax({
						url : "getProductAmount.do",
						type : "GET",
						async : false,
						data : {
							productId : productId,
							agreementId : $("#agreementInput").val(),
							provisioningId : $("#provisioningId").val()
						},
						success : function(response) {
							if (response != "" && response >= productAmount) {
								if (isAdd) {
									$('#agreementInput').prop('disabled', true)
											.trigger("chosen:updated");
									populateProductsDetailsTable();
									populateProductsDetails(productDetails,
											productAmount);
								} else {
									setNewAmount(productAmount);
								}
								$("#amountModal").modal('hide');
							} else {
								$("#productAmountInput").tooltip("destroy")
										.data("title", "Stock insuficiente")
										.addClass("has-error").tooltip();
								return false;
							}
						},
						error : function(jqXHR, textStatus, errorThrown) {
							myGenericError();
						}
					});
				}
			});

	var populateProductsDetailsTable = function() {
		var tableRow = "<tr><td>"
				+ productDescription
				+ "<span class='span-productId' style='display:none'>"
				+ productId
				+ "</span></td>"
				+ "<td class='td-amount'>"
				+ productAmount
				+ "</td>"
				+ "<td><a href='javascript:void(0);' class='edit-row'>Editar</a></td><td><a href='javascript:void(0);' class='delete-row'>Eliminar</a></td></tr>";
		$("#productTableBody").append(tableRow);
	};

	var populateProductsDetails = function(productDetails, amount) {
		var productDetail = {};
		productDetail.productId = productId;
		productDetail.amount = amount;

		productDetails.push(productDetail);
	};

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

	var setNewAmount = function(productAmount) {
		var parent = $(currentRowElement).parent().parent();
		var currentRow = $(".edit-row").index(currentRowElement);
		productDetails[currentRow].amount = productAmount;
		parent.find(".td-amount").text(productAmount);
	};

	$("#provisoningDeleteRowConfirmationButton").click(
			function() {
				var parent = $(currentRowElement).parent().parent();
				var currentRow = $(".delete-row").index(currentRowElement);
				productDetails.splice(currentRow, 1);
				parent.remove();
				if (productDetails.length == 0) {
					$('#agreementInput').prop('disabled', false).trigger(
							"chosen:updated");
				}
			});

	$("#confirmButton")
			.click(
					function() {
						if (validateForm()) {
							if (productDetails.length > 0) {
								if ($("#affiliateInput").val() != "") {
									isButtonConfirm = true;
									var jsonProvisioningRequest = {
										"id" : $("#provisioningId").val(),
										"deliveryLocationId" : $(
												"#deliveryLocationInput").val(),
										"agreementId" : $("#agreementInput")
												.val(),
										"logisticsOperatorId" : $(
												"#logisticsOperatorInput")
												.val(),
										"affiliateId" : $("#affiliateInput")
												.val(),
										"deliveryDate" : $("#deliveryDateInput")
												.val(),
										"comment" : $("#commentTextarea").val(),
										"clientId" : $("#clientInput").val(),
										"products" : productDetails
									};

									$
											.ajax({
												url : "saveProvisioningRequest.do",
												type : "POST",
												contentType : "application/json",
												data : JSON
														.stringify(jsonProvisioningRequest),
												async : true,
												success : function(response) {
													myReload(
															"success",
															"Se ha registrado la solicitud de abastecimiento n&uacute;mero: "
																	+ response.id);
												},
												error : function(jqXHR,
														textStatus, errorThrown) {
													myGenericError();
												}
											});
								} else {
									myShowAlert('danger',
											'Por favor, seleccione un afiliado.');
								}
							} else {
								myShowAlert('danger',
										'Por favor, ingrese al menos un producto.');
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

	$("#addAffiliateModalAcceptButton")
			.click(
					function() {
						if (validateAddAffiliateModalForm()) {

							var jsonAffiliate = {
								"code" : $("#affiliateCodeInput").val(),
								"name" : $("#affiliateNameInput").val(),
								"surname" : $("#affiliateSurnameInput").val(),
								"documentType" : $(
										"#affiliateDocumentTypeInput").val(),
								"document" : $("#affiliateDocumentInput").val(),
								"clientId" : $("#clientInput").val(),
								"active" : true
							};
							if (existsAffiliate()) {
								$('#addAffiliateModalAlertDiv')
										.html(
												'<div class="alert alert-danger alert-block fade in">'
														+ '<button type="button" class="close" data-dismiss="alert">'
														+ '&times;</button>C&oacute;digo existente. Por favor, ingrese uno diferente.</div>');
							} else {
								$
										.ajax({
											url : "saveAffiliate.do",
											type : "POST",
											contentType : "application/json",
											data : JSON
													.stringify(jsonAffiliate),
											async : true,
											success : function(response) {
												$("#addAffiliateModal").modal(
														'hide');
												$('#affiliateInput')
														.select2(
																"data",
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
		for (var i = 0, l = productDetails.length; i < l; ++i) {
			if (productDetails[i].productId == productId) {
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
			// $("#affiliateInput").attr("disabled",
			// true).trigger("chosen:updated");
			// $("#affiliateInput").empty();
			// $('#affiliateInput').trigger("chosen:updated");
			$("#affiliateInput").select2("val", "");
		} else {
			$("#affiliateInput").select2("enable", true);
		}
	});

	$('#agreementInput').on('change', function(evt, params) {
		if ($("#agreementInput").val() == "") {
			$("#productInput").attr("disabled", true);
		} else {
			$("#productInput").attr("disabled", false);
		}
	});

	if (isUpdate) {
		$("#divProvisioningId").show();

		$("#productInput").attr("disabled", false);
		$('#agreementInput').prop('disabled', true).trigger("chosen:updated");
		$("#productTableBody tr").each(function() {
			var productDetail = {};
			productDetail.productId = $(this).find(".span-productId").html();
			productDetail.amount = $(this).find(".td-amount").html();
			productDetails.push(productDetail);
		});
	} else {
		$("#affiliateInput").select2("enable", false);
	}

	$("#provisioningRequestForm input, #provisioningRequestForm select")
			.keypress(function(event) {
				return event.keyCode != 13;
			});

};