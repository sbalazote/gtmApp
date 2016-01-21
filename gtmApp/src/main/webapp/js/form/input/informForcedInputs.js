PendingInputs = function() {

    var inputId = null;
    var inputs = [];
    var transactionCode = null;
    var selfSerializedtransactionCode = null;

    $("#transactionCodeInput").numeric();

    $('#inputTableBody').on("click", ".view-row", function() {
        var parent = $(this).parent().parent();
        inputId = parent.attr("data-row-id");

        showInputModal(inputId);
    });

    $("#inputTable").bootgrid({
        selection: true,
        multiSelect: false,
        rowSelect: false,
        keepSelection: true,
        formatters: {
            "option": function(column, row) {
                return "<button type=\"button\" class=\"btn btn-sm btn-default view-row\"><span class=\"glyphicon glyphicon-eye-open\"></span> Detalle</button>";
            }
        }
    }).on("selected.rs.jquery.bootgrid", function(e, rows) {
        for (var i = 0; i < rows.length; i++) {
            inputs.push(rows[i].input);
        }
    }).on("deselected.rs.jquery.bootgrid", function(e, rows) {
        for (var i = 0; i < rows.length; i++) {
            for(var j = inputs.length - 1; j >= 0; j--) {
                if(inputs[j] === rows[i].input) {
                    inputs.splice(j, 1);
                }
            }
        }
    });


    $("#confirmButton").click(function() {
        if (inputs.length == 1) {
            $.ajax({
                url: "updateForcedInput.do",
                type: "POST",
                contentType: "application/json",
                data: JSON.stringify(inputs[0]),
                async: false,
                beforeSend: function () {
                    $.blockUI({message: 'Espere un Momento por favor...'});
                },
                success: function (response) {
                    if (response.resultado == true) {
                        $.unblockUI();
                        myRedirect("success", "Se ha informado el ingreso de mercader\u00eda n\u00famero: " + response.operationId, "informForcedInputs.do");
                    } else {
                        $.unblockUI();
                        var errors = "";
                        for (var i = 0, lengthI = response.myOwnErrors.length; i < lengthI; i++) {
                            errors += response.myOwnErrors[i] + "<br />";
                        }

                        for (var i = 0, lengthI = response.mySelfSerializedOwnErrors.length; i < lengthI; i++) {
                            errors += response.mySelfSerializedOwnErrors[i] + "<br />";
                        }

                        if (response.errores != null) {
                            errors += "<strong>Errores informados por ANMAT:</strong><br />";
                            for (var i = 0, lengthI = response.errores.length; i < lengthI; i++) {
                                errors += response.errores[i]._c_error + " - " + response.errores[i]._d_error + "<br />";
                            }
                        }
                        myShowAlert("danger", errors, null, 0);
                    }
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    myGenericError();
                }
            });
        } else {
            myShowAlert('info', 'Seleccione al menos un Ingreso de Mercader\u00eda');
        }
    });

    $("#forcedInput").click(function() {
        if(inputs.length == 1){
            $('#forcedInputConfirmationModal').modal();
        }else{
            myShowAlert('info', 'Seleccione al menos un Ingreso de Mercader\u00eda');
        }
    });


    $("#authorizeWithoutInform").click(function() {
        transactionCode = $("#transactionCodeInput").val() || null;
        if(transactionCode == ""){
            transactionCode = null;
        }
        selfSerializedtransactionCode = $("#selfSerializedTransactionCodeInput").val() || null;
        if(selfSerializedtransactionCode == ""){
            selfSerializedtransactionCode = null;
        }
        if(inputs.length == 1){
            $.ajax({
                url: "forceInputDefinitely.do",
                type: "POST",
                data: {
                    inputId: inputs[0],
                    transactionCode: transactionCode,
                    selfSerializedTransactionCode: selfSerializedtransactionCode
                },
                async: true,
                beforeSend : function() {
                    $.blockUI({ message: 'Espere un Momento por favor...' });
                },
                success: function(response, textStatus, jqXHR) {
                    myRedirect("success","Se ha cerrado el ingreso de mercader\u00eda n\u00famero " + inputs[0] + " sin informar a ANMAT","informForcedInputs.do" );
                },
                error: function(response, jqXHR, textStatus, errorThrown) {
                    $.unblockUI();
                    myGenericError();
                }
            });
        }else{
            myShowAlert('info', 'Seleccione al menos un Ingreso de Mercader\u00eda');
        }
    });
};
	