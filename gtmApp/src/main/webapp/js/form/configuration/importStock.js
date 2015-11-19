ImportStock = function() {

    $('#importStockInput').fileupload({
        add: function (e, data) {
            var uploadError = false;
            var extension = data.originalFiles[0].name.split('.').pop().toLowerCase();

            if ($.inArray(extension, ['xls']) == -1) {
                uploadError = true;
                myShowAlert('danger', 'Tipo de archivo no v\u00e1lido.');
            }

            if (data.originalFiles[0]['size'] > (5*1024*1024)) {//5 MB
                uploadError = true;
                myShowAlert('danger', 'Tama&ntilde;o de archivo demasiado grande.');
            }
            if (!uploadError) {
                data.submit();
            }
        },

        dataType: 'json',

        maxNumberOfFiles: 1,

        done: function (e, data) {
            $("#importStockInput").prop("disabled",true);
            $(".fileinput-button").attr("disabled",true);
            var aaData = [];
            var row = {
                fileName: data.result.fileName,
                fileSize: data.result.fileSize
            };
            aaData.push(row);
            $("#uploaded-files").bootgrid("append", aaData);
        }
    });

    $("#firstRowInput").numeric();
    $("#typeColumnInput").numeric();
    $("#gtinColumnInput").numeric();
    $("#batchColumnInput").numeric();
    $("#expirationColumnInput").numeric();
    $("#serialColumnInput").numeric();
    $("#amountColumnInput").numeric();

    var validateForm = function() {
        var form = $("#importStockForm");
        form.validate({
            rules: {
                concept: {
                    required: true
                },
                provider: {
                    required: true
                },
                agreement: {
                    required: true
                },
                firstRow: {
                    required: true
                },
                typeColumn: {
                    required: true
                },
                gtinColumn: {
                    required: true
                },
                batchColumn: {
                    required: true
                },
                expirationColumn: {
                    required: true
                },
                serialColumn: {
                    required: true
                },
                amountColumn: {
                    required: true
                }
            },
            showErrors: myShowErrors,
            onsubmit: false
        });
        return form.valid();
    };

    $("#confirm").click(function() {
        var fileSelected = $("#uploaded-files tbody").html() != '';
        if (validateForm() && fileSelected) {
            var jsonImportStock = {
                "conceptId": $("#conceptInput").val(),
                "providerId": $("#providerInput").val(),
                "agreementId": $("#agreementInput").val(),
                "firstRow": $("#firstRowInput").val(),
                "typeColumn": $("#typeColumnInput").val(),
                "gtinColumn": $("#gtinColumnInput").val(),
                "batchColumn": $("#batchColumnInput").val(),
                "expirationColumn": $("#expirationColumnInput").val(),
                "serialColumn": $("#serialColumnInput").val(),
                "amountColumn": $("#amountColumnInput").val(),
            };

            $.ajax({
                url: "updateImportStock.do",
                type: "POST",
                contentType:"application/json",
                data: JSON.stringify(jsonImportStock),
                async: false,
                success: function(response) {
                    if(response.resultado == true){
                        $.unblockUI();
                    }else{
                        var errorsList = [];
                        $.unblockUI();
                        var errors = "<strong>Migracion</strong><br />";

                        for (var j = 0, lengthJ = response.myOwnErrors.length; j < lengthJ; j++) {
                            errors += response.myOwnErrors[j] + "<br />";
                        }
                        errorsList.push(errors);

                        myReload("danger", "Han surgido los siguientes errores: " +  errorsList);
                    }
                    myShowAlert('success', 'Se ha actualizado satisfactoriamente el listado de productos.');
                    $("#uploaded-files").bootgrid("destroy");
                    $("#importStockInput").prop("disabled",false);
                    $(".fileinput-button").attr("disabled",false);
                    fileSelected = false;
                },
                error: function(jqXHR, textStatus, errorThrown) {
                    myGenericError();
                }
            });
        } else {
            myShowAlert('danger', 'Por favor, seleccione un archivo de actualizacion alfabeta.');
        }
    });

}