/* ========================================================================
 * input: batchExpirationDate.js
======================================================================== */
BatchExpirationDate = function() {
	
	// Atributos del objeto
	var rowId = 0;
	var preloadedData = null;
	var preloadedAmount = null;
	var preloadedProduct = null;
    var isUpdate = false;

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

    var setIsUpdate = function(value) {
        isUpdate = value;
    };
	
	var formValidator = null;
	
	// TODO mejorar esto- ahora no hace el paginado.
	$("#batchExpirationDateTable").bootgrid({
		rowCount: -1
	});
	
	var validateForm = function() {
		var maxAmount = parseInt($('#batchExpirationDateRemainingAmountLabel').text());
		
		var form = $("#batchExpirationDateModalForm");
		formValidator = form.validate({
			rules: {
				batchExpirationDateBatch: {
					required: true
				},
				batchExpirationDateExpirationDate: {
					required: true,
					expirationDate: true
				},
				batchExpirationDateAmount: {
					required: true,
					digits: true,
					min: 1,
					max: maxAmount
				}
			},
			showErrors: myShowErrors
		});

        if (!isUpdate) {
            $('input[name=batchExpirationDateAmount]').rules("add", {
                max: maxAmount
            });
        } else {
            $('input[name=batchExpirationDateAmount]').rules('remove', 'max');
        }
		
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
		if (!isUpdate) {
			var remaining = parseInt($('#batchExpirationDateRemainingAmountLabel').text());

			$('#batchExpirationDateEnteredAmountLabel').text(entered += amount);
			$('#batchExpirationDateRemainingAmountLabel').text(remaining -= amount);

			if (remaining == 0) {
				disableInputs();
			} else {
				enableInputs();
			}
		} else {
            $('#batchExpirationDateEnteredAmountLabel').text(entered += amount);
		}
	};
	
	var disableInputs = function() {
		$('#batchExpirationDateBatchInput').attr("disabled", true);
		$('#batchExpirationDateExpirationDateInput').attr("disabled", true);
		$('#batchExpirationDateAmountInput').attr("disabled", true);
	};
	
	var enableInputs = function() {
		$('#batchExpirationDateBatchInput').attr("disabled", false);
		$('#batchExpirationDateExpirationDateInput').attr("disabled", false);
		$('#batchExpirationDateAmountInput').attr("disabled", false);
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
		$("#batchExpirationDateTable").bootgrid("append", aaData);
		rowId++;
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
			
			$('#batchExpirationDateBatchInput').focus();
		}
	};
	
	$('#batchExpirationDateTable tbody').on("click", ".command-delete", function() {
		var parent = $(this).parent().parent();
		var amount = parent.find("td:nth(2)").html();
		subtractAmount(amount);
		var rows = Array();
		rows[0] = parseInt($(this).attr("data-row-id"));
		$("#batchExpirationDateTable").bootgrid("remove", rows);
	});
	
	var preloadModalData = function () {
		rowId = 0;
		$("#batchExpirationDateTable").bootgrid("clear");
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

        if (isUpdate) {
            $('#batchExpirationDateRequestedAmountDiv').hide();
            $('#batchExpirationDateRemainingAmountDiv').hide();
            enableInputs();
        }
	};
	
	$('#batchExpirationDateModal').on('shown.bs.modal', function () {
	    $('#batchExpirationDateBatchInput').focus();
	});
	
	$('#batchExpirationDateModal').on('hidden.bs.modal', function () {
	    myResetForm($("#batchExpirationDateModalForm")[0], formValidator);
	});
	
	$('#batchExpirationDateBatchInput').on('keypress', function(e) {
		//	Si la tecla presionada es 'ENTER'
		if (e.keyCode === 13) {
			$('#batchExpirationDateExpirationDateInput').focus();
			return false;
		}
	});
	
	$('#batchExpirationDateExpirationDateInput').on('keypress', function(e) {
		//	Si la tecla presionada es 'ENTER'
		if (e.keyCode === 13) {
			$('#batchExpirationDateAmountInput').focus();
			return false;
		}
	});
	
	$('#batchExpirationDateAmountInput').on('keypress', function(e) {
		//	Si la tecla presionada es 'ENTER'
		if (e.keyCode === 13) {
            if (!isUpdate) {
                var remainingAmount = $('#batchExpirationDateRemainingAmountLabel').text();
                if (remainingAmount == 0) {
                    $("#batchExpirationDateAcceptButton").trigger('click');
                } else {
                    $("#batchExpirationDateAddButton").trigger('click');
                }
            } else {
                $("#batchExpirationDateAddButton").trigger('click');
            }
			return false;
        }
    });
	
	$("#batchExpirationDateAddButton").click(function() {
        if (!isUpdate) {
            var remainingAmount = $('#batchExpirationDateRemainingAmountLabel').text();
            if (remainingAmount > 0) {
                generateRow();
                checkLast();
            } else {
                myShowAlert('danger', 'Ya se ha ingresado la totalidad de productos requeridos. Por favor presione el bot\u00f3n "Confirmar".', "batchExpirationDateModalAlertDiv");
            }
        } else {
            generateRow();
        }
		return false;
	});
	
	var checkLast = function() {
		var remaining = parseInt($('#batchExpirationDateRemainingAmountLabel').text());
		if (remaining == 0) {
			BootstrapDialog.show({
				title: 'Informacion',
				message: '<strong>Carga Completa.</strong> Confirma Operaci\u00f3n?',
				closable: false,
				buttons: [{
					label: 'No',
					action: function(dialogItself) {
						dialogItself.close();
					}
				}, {
					label: 'Si',
					hotkey: 115,	// F4.
					cssClass: 'btn-primary',
					action: function(dialogItself) {
						dialogItself.close();
						$("#batchExpirationDateAcceptButton").trigger('click');
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
        setIsUpdate: setIsUpdate,
		preloadModalData: preloadModalData
	};
};