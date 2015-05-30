AlfabetaUpdateProducts = function() {
	
	$('#alfabetaUpdateFileInput').fileupload({
		add: function (e, data) {
            var uploadError = false;
            var acceptFileTypes = /\/(dat)$/i;

            if (data.originalFiles[0]['type'].length && !acceptFileTypes.test(data.originalFiles[0]['type'])) {
            	uploadError = true;
                myShowAlert('danger', 'Tipo de archivo no v\u00e1lido.');
            }

            if (data.originalFiles[0]['size'] > 5000000) {
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
        	$("#alfabetaUpdateFileInput").prop("disabled",true);
        	$(".fileinput-button").attr("disabled",true);
            $("tr:has(td)").remove();
 
            $("#uploaded-files tbody").append(
            	$('<tr/>')
            	.append($('<td/>').text(data.result.fileName))
            	.append($('<td/>').text(data.result.fileSize))
            );
        }
    });
	
	var validateForm = function() {
		var form = $("#alfabetaUpdateproductsAdministrationForm");
		form.validate({
			rules: {
				priceFieldByteOffset: {
					required: true
				},
				priceFieldLength: {
					required: true,
					digits: true
				},
				codeFieldByteOffset: {
					required: true
				},
				codeFieldLength: {
					required: true,
					digits: true
				},
				gtinFieldByteOffset: {
					required: true
				},
				GTINFieldLength: {
					required: true,
					digits: true
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
			var jsonAlfabetaFile = {
				"priceFieldByteOffset": $("#priceFieldByteOffsetInput").val(),
				"priceFieldLength": $("#priceFieldLengthInput").val(),
                "codeFieldByteOffset": $("#codeFieldByteOffsetInput").val(),
                "codeFieldLength": $("#codeFieldLengthInput").val(),
                "gtinFieldByteOffset": $("#gtinFieldByteOffsetInput").val(),
                "gtinFieldLength": $("#gtinFieldLengthInput").val()
			};

			$.ajax({
				url: "updateProducts.do",
				type: "POST",
				contentType:"application/json",
				data: JSON.stringify(jsonAlfabetaFile),
				async: false,
				success: function(response) {
					myShowAlert('success', 'Se ha actualizado satisfactoriamente el listado de productos.');
					$("#uploaded-files tbody").html('');
					$("#alfabetaUpdateFileInput").prop("disabled",false);
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
};