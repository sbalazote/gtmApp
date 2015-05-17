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
		var maxAmount = parseInt($('#selfSerializedRemainingAmountLabel').text());
	
		var form = $("#selfSerializedModalForm");
		formValidator = form.validate({
			rules: {
				selfSerializedBatch: {
					required: true
				},
				selfSerializedExpirationDate: {
					required: true,
					expirationDate: true
				},
				selfSerializedAmount: {
					required: true,
					digits: true,
					min: 1,
					max: maxAmount
				}
			},
			showErrors: myShowErrors
		});
		
		$('input[name=selfSerializedAmount]').rules("add", {
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
		var entered = parseInt($('#selfSerializedEnteredAmountLabel').text());
		var remaining = parseInt($('#selfSerializedRemainingAmountLabel').text());
		
		$('#selfSerializedEnteredAmountLabel').text(entered += amount);
		$('#selfSerializedRemainingAmountLabel').text(remaining -= amount);
		
		if (remaining == 0) {
			disableInputs();
		} else {
			enableInputs();
		}
	};
	
	var disableInputs = function() {
		$('#selfSerializedBatchInput').attr("disabled", true);
		$('#selfSerializedExpirationDateInput').attr("disabled", true);
		$('#selfSerializedAmountInput').attr("disabled", true);
	};
	
	var enableInputs = function() {
		$('#selfSerializedBatchInput').attr("disabled", false);
		$('#selfSerializedExpirationDateInput').attr("disabled", false);
		$('#selfSerializedAmountInput').attr("disabled", false);
	};
	
	var addToTable = function(batch, expirationDate, amount) {
		$("#selfSerializedTable tbody").append("<tr>"
			+ "<td class='batch'>"+batch+"</td>"
			+ "<td class='expirationDate'>"+expirationDate+"</td>"
			+ "<td class='amount'>"+amount+"</td>"
			+ "<td><button class='btnDelete' type='button'><span class='glyphicon glyphicon-remove'/></button></td>"
			+ "</tr>");
	};
	
	var generateRow = function() {
		if (validateForm()) {
			var par = $("#selfSerializedGenerateButton").parent().parent().find("input");

			var batch = par.eq(0);
			var expirationDate = par.eq(1);
			var amount = par.eq(2);
			
			addAmount(amount.val());	
			
			addToTable(batch.val(), expirationDate.val(), amount.val());
	
			batch.val("");
			expirationDate.val("");
			amount.val("");
			
			formValidator.resetForm();
			
			$('#selfSerializedBatchInput').focus();
		}
	};
	
	$('#selfSerializedTable tbody').on("click", ".btnDelete", function() {
		var parent = $(this).parent().parent();
		var amount = parent.find(".amount");
		subtractAmount(amount.text());
		parent.remove();
	});
	
	var preloadModalData = function () {
		$("#selfSerializedModal tbody").html("");
		$('#selfSerializedProductLabel').text(preloadedProduct);
		$('#selfSerializedRequestedAmountLabel').text(preloadedAmount);
		
		if (preloadedData != null) {
			for (var i = 0; i < preloadedData.length; i++) {
				addToTable(preloadedData[i].batch, preloadedData[i].expirationDate, preloadedData[i].amount);
			}
			$('#selfSerializedEnteredAmountLabel').text(preloadedAmount);
			$('#selfSerializedRemainingAmountLabel').text(parseInt(0));
			disableInputs();
		} else {
			$('#selfSerializedEnteredAmountLabel').text(parseInt(0));
			$('#selfSerializedRemainingAmountLabel').text(preloadedAmount);
			enableInputs();
		}
	};
	
	$('#selfSerializedModal').on('shown.bs.modal', function () {
	    $('#selfSerializedBatchInput').focus();
	});
	
	$('#selfSerializedModal').on('hidden.bs.modal', function () {
	    myResetForm($("#selfSerializedModalForm")[0], formValidator);
	});
	
	$('#selfSerializedGenerateButton').on('keypress', function(e) {
		//	Si la tecla presionada es 'ENTER'
        if (e.keyCode === 13) {
        	var remainingAmount = $('#selfSerializedRemainingAmountLabel').text();
			if (remainingAmount == 0) {
				$("#selfSerializedAcceptButton").trigger('click');
			} else {
				$("#selfSerializedGenerateButton").trigger('click');
			}
			return false;
        }
    });
	
	$("#selfSerializedGenerateButton").click(function() {
		var remainingAmount = $('#selfSerializedRemainingAmountLabel').text();
		if (remainingAmount > 0) {
			generateRow();
		} else {
			myShowAlert('danger', 'Ya se ha ingresado la totalidad de productos requeridos. Por favor presione el bot&oacute;n "Confirmar".', "selfSerializedModalAlertDiv");
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