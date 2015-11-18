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

    $("#confirm").click(function() {
        var fileSelected = $("#uploaded-files tbody").html() != '';
        if (fileSelected) {
            var jsonImportStock = {
                "agreementId": 1,
            };

            $.ajax({
                url: "updateImportStock.do",
                type: "POST",
                data: {
                    agreementId : "1"
                },
                async: false,
                success: function(response) {
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