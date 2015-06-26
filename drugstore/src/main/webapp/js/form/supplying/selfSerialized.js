/* ========================================================================
 * input: selfSerialized.js
======================================================================== */

SelfSerialized = function() {
	
	// Atributos del objeto
	var rowId = 0;
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

	// TODO mejorar esto- ahora no hace el paginado.
	$("#outOfStockSelfSerializedTable").bootgrid({
		rowCount: -1
	});

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
		var aaData = [];
		var row = {
			id: rowId,
			batch: batch,
			expirationDate: expirationDate,
			amount: amount,
			commands: "<button type=\"button\" class=\"btn btn-sm btn-default command-delete\" data-row-id=\"" + rowId + "\"><span class=\"glyphicon glyphicon-trash\"></span></button>"
		};
		aaData.push(row);
		$("#outOfStockSelfSerializedTable").bootgrid("append", aaData);
		rowId++;
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
	
	$('#outOfStockSelfSerializedTable tbody').on("click", ".command-delete", function() {
		var parent = $(this).parent().parent();
		var amount = parent.find("td:nth(2)").html();
		subtractAmount(amount);
		var rows = Array();
		rows[0] = parseInt($(this).attr("data-row-id"));
		$("#outOfStockSelfSerializedTable").bootgrid("remove", rows);
	});
	
	var preloadModalData = function () {
		rowId = 0;
		$("#outOfStockSelfSerializedTable").bootgrid("clear");
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
			$('#outOfStockSelfSerializedExpirationDateInput').focus();
			return false;
		}
	});

	$('#outOfStockSelfSerializedExpirationDateInput').on('keypress', function(e) {
		//	Si la tecla presionada es 'ENTER'
		if (e.keyCode === 13) {
			$('#outOfStockSelfSerializedAmountInput').focus();
			return false;
		}
	});

	$('#outOfStockSelfSerializedAmountInput').on('keypress', function(e) {
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
			checkLast();
		} else {
			myShowAlert('danger', 'Ya se ha ingresado la totalidad de productos requeridos. Por favor presione el bot\u00f3n "Confirmar".', "outOfStockSelfSerializedModalAlertDiv");
		}
	});

	var checkLast = function() {
		var remaining = parseInt($('#outOfStockSelfSerializedRemainingAmountLabel').text());
		if (remaining == 0) {
			BootstrapDialog.show({
				title: 'Informacion',
				message: '<strong>Carga Completa.</strong> Confirma Operaci\u00f3n?',
				buttons: [{
					label: 'No',
					action: function(dialogItself) {
						dialogItself.close();
					}
				}, {
					label: 'Si',
					cssClass: 'btn-primary',
					action: function(dialogItself) {
						dialogItself.close();
						$("#outOfStockSelfSerializedAcceptButton").trigger('click');
					}
				}]
			});
		}
	};

	return {
		getPreloadedData: getPreloadedData,
		setPreloadedData: setPreloadedData,
		setPreloadedAmount: setPreloadedAmount,
		setPreloadedProduct: setPreloadedProduct,
		preloadModalData: preloadModalData
	};
	
};