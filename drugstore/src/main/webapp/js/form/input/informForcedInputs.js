PendingInputs = function() {

    var inputId = null;
    var inputs = [];

    $('#inputTableBody').on("click", ".view-row", function() {
        var parent = $(this).parent().parent();
        inputId = parent.attr("data-row-id");

        showInputModal(inputId);
    });

    $("#inputTable").bootgrid({
        selection: true,
        multiSelect: false,
        rowSelect: true,
        keepSelection: true,
        formatters: {
            "option": function(column, row) {
                return "<a href=\"#\" class='view-row'>Consulta</a>";
            }
        }
    }).on("selected.rs.jquery.bootgrid", function(e, rows) {
        for (var i = 0; i < rows.length; i++) {
            inputs.push(rows[i].input);
        }
    }).on("deselected.rs.jquery.bootgrid", function(e, rows) {
        for (var i = 0; i < rows.length; i++) {
            for(var i = input.length - 1; i >= 0; i--) {
                if(inputs[i] === rows[i].input) {
                    inputs.splice(i, 1);
                }
            }
        }
    });


    $("#confirmButton").click(function() {
        $.ajax({
            url: "updateForcedInput.do",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(inputs[0]),
            async: false,
            success: function(response) {
                if(response.resultado == true){
                        myRedirect("success","Se ha informado el ingreso de mercader\u00eda n\u00famero: " + response.id, "informForcedInputs.do");
                }else{
                    var errors = "";
                    for (var i = 0, lengthI = response.myOwnErrors.length; i < lengthI; i++) {
                        errors += response.myOwnErrors[i] + "<br />";
                    }

                    if(response.errores != null){
                        errors += "<strong>Errores informados por ANMAT:</strong><br />";
                        for (var i = 0, lengthI = response.errores.length; i < lengthI; i++) {
                            errors += response.errores[i]._c_error + " - " + response.errores[i]._d_error + "<br />";
                        }
                    }
                    myShowAlert("danger", errors,null);
                }
            },
            error: function(jqXHR, textStatus, errorThrown) {
                myGenericError();
            }
        });
    });

    $("#forcedInput").click(function() {
        $('#forcedInputConfirmationModal').modal();
    });


    $("#authorizeWithoutInform").click(function() {
        if(inputs.length == 1){
            $.ajax({
                url: "forceInputDefinitely.do",
                type: "POST",
                contentType:"application/json",
                data: JSON.stringify(inputs[0]),
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
            BootstrapDialog.show({
                type: BootstrapDialog.TYPE_INFO,
                title: 'Atenci\u00edn',
                message: "Seleccione un elemento",
                buttons: [{
                    label: 'Cerrar',
                    action: function(dialogItself){
                        dialogItself.close();
                    }
                }]
            });
        }
    });
};
	