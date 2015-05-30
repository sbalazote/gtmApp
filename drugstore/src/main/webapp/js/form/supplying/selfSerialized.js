/* ========================================================================
 * input: selfSerialized.js
======================================================================== */

SelfSerialized = function() {
	
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
		var maxAmount = parseInt($('#outOfStockSelfSerializedRemainingAmountLabel').text());
	
		var form = $("#outOfStockSelfSerializedModalForm");
		formValidator = form.validate({
			rules: {
				outOfStockSelfSerializedBatch: {
					required: true
				},
				outOfStockSelfSerializedExpirationDate: {
					required: true,
					expirationDate: true
				},
				outOfStockSelfSerializedAmount: {
					required: true,
					digits: true,
					min: 1,
					max: maxAmount
				}
			},
			showErrors: myShowErrors
		});
		
		$('input[name=outOfStockSelfSerializedAmount]').rules("add", {
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
		var entered = parseInt($('#outOfStockSelfSerializedEnteredAmountLabel').text());
		var remaining = parseInt($('#outOfStockSelfSerializedRemainingAmountLabel').text());
		
		$('#outOfStockSelfSerializedEnteredAmountLabel').text(entered += amount);
		$('#outOfStockSelfSerializedRemainingAmountLabel').text(remaining -= amount);
		
		if (remaining == 0) {
			disableInputs();
		} else {
			enableInputs();
		}
	};
	
	var disableInputs = function() {
		$('#outOfStockSelfSerializedBatchInput').attr("disabled", true);
		$('#outOfStockSelfSerializedExpirationDateInput').attr("disabled", true);
		$('#outOfStockSelfSerializedAmountInput').attr("disabled", true);
	};
	
	var enableInputs = function() {
		$('#outOfStockSelfSerializedBatchInput').attr("disabled", false);
		$('#outOfStockSelfSerializedExpirationDateInput').attr("disabled", false);
		$('#outOfStockSelfSerializedAmountInput').attr("disabled", false);
	};
	
	var addToTable = function(batch, expirationDate, amount) {
		$("#outOfStockSelfSerializedTable tbody").append("<tr>"
			+ "<td class='batch'>"+batch+"</td>"
			+ "<td class='expirationDate'>"+expirationDate+"</td>"
			+ "<td class='amount'>"+amount+"</td>"
			+ "<td><button class='btnDelete' type='button'><span class='glyphicon glyphicon-remove'/></button></td>"
			+ "</tr>");
	};
	
	var generateRow = function() {
		if (validateForm()) {
			var par = $("#outOfStockSelfSerializedGenerateButton").parent().parent().find("input");

			var batch = par.eq(0);
			var expirationDate = par.eq(1);
			var amount = par.eq(2);
			
			addAmount(amount.val());	
			
			addToTable(batch.val(), expirationDate.val(), amount.val());
	
			batch.val("");
			expirationDate.val("");
			amount.val("");
			
			formValidator.resetForm();
			
			$('#outOfStockSelfSerializedBatchInput').focus();
		}
	};
	
	$('#outOfStockSelfSerializedTable tbody').on("click", ".btnDelete", function() {
		var parent = $(this).parent().parent();
		var amount = parent.find(".amount");
		subtractAmount(amount.text());
		parent.remove();
	});
	
	var preloadModalData = function () {
		$("#outOfStockSelfSerializedModal tbody").html("");
		$('#outOfStockSelfSerializedProductLabel').text(preloadedProduct);
		$('#outOfStockSelfSerializedRequestedAmountLabel').text(preloadedAmount);
		
		if (preloadedData != null) {
			for (var i = 0; i < preloadedData.length; i++) {
				addToTable(preloadedData[i].batch, preloadedData[i].expirationDate, preloadedData[i].amount);
			}
			$('#outOfStockSelfSerializedEnteredAmountLabel').text(preloadedAmount);
			$('#outOfStockSelfSerializedRemainingAmountLabel').text(parseInt(0));
			disableInputs();
		} else {
			$('#outOfStockSelfSerializedEnteredAmountLabel').text(parseInt(0));
			$('#outOfStockSelfSerializedRemainingAmountLabel').text(preloadedAmount);
			enableInputs();
		}
	};
	
	$('#outOfStockSelfSerializedModal').on('shown.bs.modal', function () {
	    $('#outOfStockSelfSerializedBatchInput').focus();
	});
	
	$('#outOfStockSelfSerializedModal').on('hidden.bs.modal', function () {
	    myResetForm($("#outOfStockSelfSerializedModalForm")[0], formValidator);
	});
	
	$('#outOfStockSelfSerializedGenerateButton').on('keypress', function(e) {
		//	Si la tecla presionada es 'ENTER'
        if (e.keyCode === 13) {
        	var remainingAmount = $('#outOfStockSelfSerializedRemainingAmountLabel').text();
			if (remainingAmount == 0) {
				$("#outOfStockSelfSerializedAcceptButton").trigger('click');
			} else {
				$("#outOfStockSelfSerializedGenerateButton").trigger('click');
			}
			return false;
        }
    });
	
	$("#outOfStockSelfSerializedGenerateButton").click(function() {
		var remainingAmount = $('#outOfStockSelfSerializedRemainingAmountLabel').text();
		if (remainingAmount > 0) {
			generateRow();
		} else {
			myShowAlert('danger', 'Ya se ha ingresado la totalidad de productos requeridos. Por favor presione el bot\u00f3n "Confirmar".', "outOfStockSelfSerializedModalAlertDiv");
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