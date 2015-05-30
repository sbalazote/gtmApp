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
		var maxAmount = parseInt($('#outOfStockBatchExpirationDateRemainingAmountLabel').text());
		
		var form = $("#outOfStockBatchExpirationDateModalForm");
		formValidator = form.validate({
			rules: {
				outOfStockBatch: {
					required: true
				},
				outOfStockExpirationDate: {
					required: true,
					expirationDate: true
				},
				outOfStockAmount: {
					required: true,
					digits: true,
					min: 1,
					max: maxAmount
				}
			},
			showErrors: myShowErrors
		});
		
		$('input[name=outOfStockAmount]').rules("add", {
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
		var entered = parseInt($('#outOfStockBatchExpirationDateEnteredAmountLabel').text());
		var remaining = parseInt($('#outOfStockBatchExpirationDateRemainingAmountLabel').text());
		
		$('#outOfStockBatchExpirationDateEnteredAmountLabel').text(entered += amount);
		$('#outOfStockBatchExpirationDateRemainingAmountLabel').text(remaining -= amount);
		
		if (remaining == 0) {
			disableInputs();
		} else {
			enableInputs();
		}
	};
	
	var disableInputs = function() {
		$('#outOfStockBatchInput').attr("disabled", true);
		$('#outOfStockExpirationDateInput').attr("disabled", true);
		$('#outOfStockAmountInput').attr("disabled", true);
	};
	
	var enableInputs = function() {
		$('#outOfStockBatchInput').attr("disabled", false);
		$('#outOfStockExpirationDateInput').attr("disabled", false);
		$('#outOfStockAmountInput').attr("disabled", false);
	};
	
	var addToTable = function(batch, expirationDate, amount) {
		$("#outOfStockBatchExpirationDateTable tbody").append("<tr>"
			+ "<td class='batch'>"+batch+"</td>"
			+ "<td class='expirationDate'>"+expirationDate+"</td>"
			+ "<td class='amount'>"+amount+"</td>"
			+ "<td><button class='btnDelete' type='button'><span class='glyphicon glyphicon-remove'/></button></td>"
			+ "</tr>");
	};
	
	var generateRow = function() {
		if (validateForm()) {
			var par = $("#outOfStockBatchExpirationDateAddButton").parent().parent().find("input");
			var batch = par.eq(0);
			var expirationDate = par.eq(1);
			var amount = par.eq(2);
			
			addAmount(amount.val());
	
			addToTable(batch.val(), expirationDate.val(), amount.val());
	
			batch.val("");
			expirationDate.val("");
			amount.val("");
			
			formValidator.resetForm();
			
			$('#outOfStockBatchInput').focus();
		}
	};
	
	$('#outOfStockBatchExpirationDateTable tbody').on("click", ".btnDelete", function() {
		var parent = $(this).parent().parent();
		var amount = parent.find(".amount");
		subtractAmount(amount.text());
		parent.remove();
	});
	
	var preloadModalData = function () {
		$("#outOfStockBatchExpirationDateModal tbody").html("");
		$('#outOfStockBatchExpirationDateProductLabel').text(preloadedProduct);
		$('#outOfStockBatchExpirationDateRequestedAmountLabel').text(preloadedAmount);
		
		if (preloadedData != null) {
			for (var i = 0; i < preloadedData.length; i++) {
				addToTable(preloadedData[i].batch, preloadedData[i].expirationDate, preloadedData[i].amount);
			}
			$('#outOfStockBatchExpirationDateEnteredAmountLabel').text(preloadedAmount);
			$('#outOfStockBatchExpirationDateRemainingAmountLabel').text(parseInt(0));
			disableInputs();
		} else {
			$('#outOfStockBatchExpirationDateEnteredAmountLabel').text(parseInt(0));
			$('#outOfStockBatchExpirationDateRemainingAmountLabel').text(preloadedAmount);
			enableInputs();
		}
	};
	
	$('#outOfStockBatchExpirationDateModal').on('shown.bs.modal', function () {
	    $('#outOfStockBatchInput').focus();
	});
	
	$('#outOfStockBatchExpirationDateModal').on('hidden.bs.modal', function () {
	    myResetForm($("#outOfStockBatchExpirationDateModalForm")[0], formValidator);
	});
	
	$('#outOfStockBatchInput').on('keypress', function(e) {
		//	Si la tecla presionada es 'ENTER'
		if (e.keyCode === 13) {
			$('#outOfStockExpirationDateInput').focus();
			return false;
		}
	});
	
	$('#outOfStockExpirationDateInput').on('keypress', function(e) {
		//	Si la tecla presionada es 'ENTER'
		if (e.keyCode === 13) {
			$('#outOfStockAmountInput').focus();
			return false;
		}
	});
	
	$('#outOfStockAmountInput').on('keypress', function(e) {
		//	Si la tecla presionada es 'ENTER'
		if (e.keyCode === 13) {
        	var remainingAmount = $('#outOfStockBatchExpirationDateRemainingAmountLabel').text();
			if (remainingAmount == 0) {
				$("#outOfStockBatchExpirationDateAcceptButton").trigger('click');
			} else {
				$("#outOfStockBatchExpirationDateAddButton").trigger('click');
			}
			return false;
        }
    });
	
	$("#outOfStockBatchExpirationDateAddButton").click(function() {
		var remainingAmount = $('#outOfStockBatchExpirationDateRemainingAmountLabel').text();
		if (remainingAmount > 0) {
			generateRow();
		} else {
			myShowAlert('danger', 'Ya se ha ingresado la totalidad de productos requeridos. Por favor presione el bot\u00f3n "Confirmar".', "outOfStockBatchExpirationDateModalAlertDiv");
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