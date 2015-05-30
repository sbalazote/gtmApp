
OrderAssemblyBatchExpirationDate = function() {
	
	// Atributos del objeto
	var preloadedData = null;
	var preloadedAmount = null;
	var preloadedProduct = null;
	var preloadedProductId = null;
	var preloadedStockIds = [];
	
	var setPreloadedStockIds = function(value) {
		preloadedStockIds = value || [];
	};

	var getPreloadedData = function() {
		return preloadedData;
	};
	
	var setPreloadedData = function(value) {
		preloadedData = value;
	};
	
	var setPreloadedAmount = function(value) {
		preloadedAmount = value;
	};
	
	var setPreloadedProduct = function(value) {
		preloadedProduct = value;
	};
	
	var setPreloadedProductId = function(value) {
		preloadedProductId = value;
	};
	
	var formValidator = null;
	var batchExpirationDates = null;
	
	var validateForm = function() {
		var maxAmount = parseInt($('#batchExpirationDateRemainingAmountLabel').text());
		
		var form = $("#batchExpirationDateModalForm");
		formValidator = form.validate({
			rules: {
				batchExpirationDate: {
					required: true
				},
				amount: {
					required: true,
					digits: true,
					min: 1,
					max: maxAmount
				}
			},
			showErrors: myShowErrors
		});
		
		$('input[name=amount]').rules("add", {
			max: maxAmount
		});
		
		return form.valid();
	};
	
	var addAmount = function(value) {
		updateAmounts(parseInt(value));
	};
	
	var subtractAmount = function(value) {
		updateAmounts(parseInt(-value));
	};
	
	var updateAmounts = function(amount) {
		var entered = parseInt($('#batchExpirationDateEnteredAmountLabel').text());
		var remaining = parseInt($('#batchExpirationDateRemainingAmountLabel').text());
		
		$('#batchExpirationDateEnteredAmountLabel').text(entered += amount);
		$('#batchExpirationDateRemainingAmountLabel').text(remaining -= amount);
		
		if (remaining == 0) {
			disableInputs();
		} else {
			enableInputs();
		}
	};
	
	var disableInputs = function() {
		$('#batchExpirationDateSelect').prop('disabled', true).trigger("chosen:updated");
		$('#amountInput').attr("disabled", true);
	};
	
	var enableInputs = function() {
		$('#batchExpirationDateSelect').prop('disabled', false).trigger("chosen:updated");
		$('#amountInput').attr("disabled", false);
	};
	
	var addToTable = function(batch, expirationDate, amount, stockId) {
		$("#batchExpirationDateTable tbody").append("<tr>"
			+ "<td class='batch'>"+batch+"</td>"
			+ "<td class='expirationDate'>"+expirationDate+"</td>"
			+ "<td class='amount'>"+amount+"</td>"
			+ "<td><span class='stockId' style='display:none'>"+stockId+"</span><button class='btnDelete' type='button'><span class='glyphicon glyphicon-remove'/></button></td>"
			+ "</tr>");
	};
	
	var generateRow = function() {
		if (validateForm()) {
			var stockId = $('#batchExpirationDateSelect').val();
			
			var amount = $("#amountInput").val();
			if (amount > batchExpirationDates[stockId]) {
				$('#amountInput').tooltip("destroy").data("title", "El lote seleccionado no posee esa cantidad").addClass("has-error").tooltip();
				return false;
			}
			
			if (alreadyExistsStockId(stockId)) {
				$('#amountInput').tooltip("destroy").data("title", "El lote/vto. ya ha sido seleccionado").addClass("has-error").tooltip();
				return false;
			}
			
			var batchExpirationDate = $("#batchExpirationDateSelect option:selected").html();
			var batch = batchExpirationDate.split("-")[0].trim();
			var expirationDate = batchExpirationDate.split("-")[1].trim();
			
			addToTable(batch, expirationDate, amount, stockId);
			
			addAmount(amount);
	
			$("#batchExpirationDateSelect").val("");
			$("#batchExpirationDateSelect").trigger("chosen:updated");
			$("#amountInput").val("");
			
			formValidator.resetForm();

			updateOptionsFromAdd(stockId, parseInt(amount), batchExpirationDates[stockId]);
		}
	};
	
	$('#batchExpirationDateTable tbody').on("click", ".btnDelete", function() {
		var parent = $(this).parent().parent();
		var amount = parent.find(".amount");
		subtractAmount(amount.text());
		
		var stockId = parent.find(".stockId").text();
		updateOptionsFromDelete(stockId, parseInt(amount.text()), batchExpirationDates[stockId]);
		
		parent.remove();
	});
	
	var preloadModalData = function () {
		$("#batchExpirationDateModal tbody").html("");
		$('#batchExpirationDateProductLabel').text(preloadedProduct);
		$('#batchExpirationDateRequestedAmountLabel').text(preloadedAmount);
		
		if (preloadedData != null) {
			for (var i = 0; i < preloadedData.length; i++) {
				addToTable(preloadedData[i].batch, preloadedData[i].expirationDate, preloadedData[i].amount, preloadedStockIds[i]);
			}
			$('#batchExpirationDateEnteredAmountLabel').text(preloadedAmount);
			$('#batchExpirationDateRemainingAmountLabel').text(parseInt(0));
			disableInputs();
		} else {
			$('#batchExpirationDateEnteredAmountLabel').text(parseInt(0));
			$('#batchExpirationDateRemainingAmountLabel').text(preloadedAmount);
			enableInputs();
		}
	};
	
	$('#batchExpirationDateModal').on('shown.bs.modal', function () {
		batchExpirationDates = {};
		$('#batchExpirationDateSelect', this).chosen('destroy').chosen();
		$.ajax({
			url: "getBatchExpirationDateStock.do?productId=" + preloadedProductId + "&agreementId=" + $("#agreementId").val(),
			type: "GET",
			async: false,
			success: function(response) {
				if (response != "") {
					var selectContent = "<option value=''>Seleccione Lote - Vencimiento</option>";
                    $('#batchExpirationDateSelect').empty();
					$('#batchExpirationDateSelect', this).append(selectContent);
					for (var i = 0, l = response.length; i < l; i++) {
						var stockId = response[i].id;
						var desc = response[i].batch + " - " + myParseDate(response[i].expirationDate) + " - Stock: " + response[i].amount;
						batchExpirationDates[stockId] = response[i].amount;
						
						if (preloadedStockIds.indexOf(stockId) == -1) {
							selectContent = "<option value='"+stockId+"'>"+desc+"</option>";
							$('#batchExpirationDateSelect').append(selectContent);
						} else {
							selectContent = "<option value='"+stockId+"' style='display:none'>"+desc+"</option>";
							$('#batchExpirationDateSelect').append(selectContent);
						}
					}
					//$("#batchExpirationDateSelect").html(selectContent);
					$('#batchExpirationDateSelect', this).append(selectContent);
				}
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myGenericError("batchExpirationDateModalAlertDiv");
			}
		});
		$('#batchExpirationDateSelect', this).trigger("chosen:updated");
		
	});
	
	$("#batchExpirationDateSelect").change(function() {
		var remainingAmount = $('#batchExpirationDateRemainingAmountLabel').text();
		var stockId = $(this).val();
		var stockAmount = batchExpirationDates[stockId];
		if (stockAmount >= remainingAmount) {
			$('#amountInput').val(remainingAmount);
		} else {
			$('#amountInput').val(stockAmount);
		}
		$('#amountInput').removeClass("has-error").tooltip("destroy");
		$('#amountInput').focus();
	});
	
	$('#batchExpirationDateModal').on('hidden.bs.modal', function () {
		var form = $("#batchExpirationDateModalForm");
		$("input", form).each(function() {
			$(this).data("title", "").removeClass("has-error").tooltip("destroy");
		});
		if (formValidator != null) {
			formValidator.resetForm();
		}
	});
	
	$('#batchExpirationDateModal').on('keypress', function(e) {
		//	Si la tecla presionada es 'ENTER'
        if (e.keyCode === 13) {
        	var remainingAmount = $('#batchExpirationDateRemainingAmountLabel').text();
			if (remainingAmount == 0) {
				$("#batchExpirationDateAcceptButton").trigger('click');
			} else {
				$("#batchExpirationDateAddButton").trigger('click');
			}
			return false;
        }
    });
	
	$("#batchExpirationDateAddButton").click(function() {
		var remainingAmount = $('#batchExpirationDateRemainingAmountLabel').text();
		if (remainingAmount > 0) {
			generateRow();
		} else {
			myShowAlert('danger', 'Ya se ha ingresado la totalidad de productos requeridos. Por favor presione el bot\u00f3n "Confirmar".', "batchExpirationDateModalAlertDiv");
		}
	});
	
	var updateOptionsFromAdd = function(stockId, requiredAmount, availableAmount) {
		var selectedOption = $("#batchExpirationDateSelect option[value="+stockId+"]");
		
		// si lo requerido es igual a lo disponible para esa opcion, la oculto.
		if (requiredAmount < availableAmount) {
			var splittedOption = selectedOption.html().split("-");
			$("#batchExpirationDateSelect option[value="+stockId+"]").html(splittedOption[0]+"-"+splittedOption[1]+"-"+" Stock: "+(availableAmount-requiredAmount));
		// en cambio, si lo requerido es inferior a lo disponible para esa opcion, resto y la sigo mostrando.
		} else {
			selectedOption.hide();
		}
		batchExpirationDates[stockId] -= requiredAmount;
		$("#batchExpirationDateSelect").trigger("chosen:updated");
	};
	
	var updateOptionsFromDelete = function(stockId, releasedAmount, availableAmount) {
		var optionToUpdate = $("#batchExpirationDateSelect option[value="+stockId+"]");
		
		//	si la opcion no estaba oculta significa que todavia quedaban prods. para ese lote/vto, actualizo los valores.
		if (!(optionToUpdate.css('display') == 'none')) {
			var splittedOption = optionToUpdate.html().split("-");
			$("#batchExpirationDateSelect option[value="+stockId+"]").html(splittedOption[0]+"-"+splittedOption[1]+"-"+" Stock: "+(availableAmount+releasedAmount));
		//	en cambio, si la opcion estaba oculta, la vuelvo a mostrar.
		} else {
			optionToUpdate.show();
		}
		batchExpirationDates[stockId] += releasedAmount;
		$("#batchExpirationDateSelect").trigger("chosen:updated");
	};
	
	var alreadyExistsStockId = function(stockId) {
		return ($(".stockId:contains("+stockId+")").length == 1);
	};
	
	return {
		getPreloadedData: getPreloadedData,
		setPreloadedData: setPreloadedData,
		setPreloadedAmount: setPreloadedAmount,
		setPreloadedProduct: setPreloadedProduct,
		setPreloadedProductId: setPreloadedProductId,
		setPreloadedStockIds: setPreloadedStockIds,
		preloadModalData: preloadModalData
	};
	
};