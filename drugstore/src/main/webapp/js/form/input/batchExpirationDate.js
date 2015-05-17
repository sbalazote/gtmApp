/* ========================================================================
 * input: batchExpirationDate.js
======================================================================== */
BatchExpirationDate = function() {
	
	// Atributos del objeto
	var preloadedData = null;
	var preloadedAmount = null;
	var preloadedProduct = null;

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
	
	var formValidator = null;
	
	var validateForm = function() {
		var maxAmount = parseInt($('#batchExpirationDateRemainingAmountLabel').text());
		
		var form = $("#batchExpirationDateModalForm");
		formValidator = form.validate({
			rules: {
				batch: {
					required: true
				},
				expirationDate: {
					required: true,
					expirationDate: true
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
		$('#batchInput').attr("disabled", true);
		$('#expirationDateInput').attr("disabled", true);
		$('#amountInput').attr("disabled", true);
	};
	
	var enableInputs = function() {
		$('#batchInput').attr("disabled", false);
		$('#expirationDateInput').attr("disabled", false);
		$('#amountInput').attr("disabled", false);
	};
	
	var addToTable = function(batch, expirationDate, amount) {
		$("#batchExpirationDateTable tbody").append("<tr>"
			+ "<td class='batch'>"+batch+"</td>"
			+ "<td class='expirationDate'>"+expirationDate+"</td>"
			+ "<td class='amount'>"+amount+"</td>"
			+ "<td><button class='btnDelete' type='button'><span class='glyphicon glyphicon-remove'/></button></td>"
			+ "</tr>");
	};
	
	var generateRow = function() {
		if (validateForm()) {
			var par = $("#batchExpirationDateAddButton").parent().parent().find("input");
			var batch = par.eq(0);
			var expirationDate = par.eq(1);
			var amount = par.eq(2);
			
			addAmount(amount.val());
	
			addToTable(batch.val(), expirationDate.val(), amount.val());
	
			batch.val("");
			expirationDate.val("");
			amount.val("");
			
			formValidator.resetForm();
			
			$('#batchInput').focus();
		}
	};
	
	$('#batchExpirationDateTable tbody').on("click", ".btnDelete", function() {
		var parent = $(this).parent().parent();
		var amount = parent.find(".amount");
		subtractAmount(amount.text());
		parent.remove();
	});
	
	var preloadModalData = function () {
		$("#batchExpirationDateModal tbody").html("");
		$('#batchExpirationDateProductLabel').text(preloadedProduct);
		$('#batchExpirationDateRequestedAmountLabel').text(preloadedAmount);
		
		if (preloadedData != null) {
			for (var i = 0; i < preloadedData.length; i++) {
				addToTable(preloadedData[i].batch, preloadedData[i].expirationDate, preloadedData[i].amount);
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
	    $('#batchInput').focus();
	});
	
	$('#batchExpirationDateModal').on('hidden.bs.modal', function () {
	    myResetForm($("#batchExpirationDateModalForm")[0], formValidator);
	});
	
	$('#batchExpirationDateAddButton').on('keypress', function(e) {
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
			myShowAlert('danger', 'Ya se ha ingresado la totalidad de productos requeridos. Por favor presione el bot&oacute;n "Confirmar".', "batchExpirationDateModalAlertDiv");
		}
	});
	
	return {
		getPreloadedData: getPreloadedData,
		setPreloadedData: setPreloadedData,
		setPreloadedAmount: setPreloadedAmount,
		setPreloadedProduct: setPreloadedProduct,
		preloadModalData: preloadModalData
	};
	
};