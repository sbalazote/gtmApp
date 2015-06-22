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
	$("#selfSerializedTable").bootgrid({
		rowCount: -1
	});

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
		var aaData = [];
		var row = {
			id: rowId,
			batch: batch,
			expirationDate: expirationDate,
			amount: amount,
			commands: "<button type=\"button\" class=\"btn btn-sm btn-default command-delete\" data-row-id=\"" + rowId + "\"><span class=\"glyphicon glyphicon-trash\"></span></button>"
		};
		aaData.push(row);
		$("#selfSerializedTable").bootgrid("append", aaData);
		rowId++;
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
	
	$('#selfSerializedTable tbody').on("click", ".command-delete", function() {
		var parent = $(this).parent().parent();
		var amount = parent.find("td:nth(2)").html();
		subtractAmount(amount);
		var rows = Array();
		rows[0] = parseInt($(this).attr("data-row-id"));
		$("#selfSerializedTable").bootgrid("remove", rows);
	});
	
	var preloadModalData = function () {
		rowId = 0;
		$("#selfSerializedTable").bootgrid("clear");
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
	
	$('#selfSerializedBatchInput').on('keypress', function(e) {
		//	Si la tecla presionada es 'ENTER'
		if (e.keyCode === 13) {
			$('#selfSerializedExpirationDateInput').focus();
			return false;
		}
	});
	
	$('#selfSerializedExpirationDateInput').on('keypress', function(e) {
		//	Si la tecla presionada es 'ENTER'
		if (e.keyCode === 13) {
			$('#selfSerializedAmountInput').focus();
			return false;
		}
	});
	
	$('#selfSerializedAmountInput').on('keypress', function(e) {
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
			checkLast();
		} else {
			myShowAlert('danger', 'Ya se ha ingresado la totalidad de productos requeridos. Por favor presione el bot\u00f3n "Confirmar".', "selfSerializedModalAlertDiv");
		}
	});
	
	var checkLast = function() {
		var remaining = parseInt($('#selfSerializedRemainingAmountLabel').text());
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
						$("#selfSerializedAcceptButton").trigger('click');
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