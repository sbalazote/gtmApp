AlfabetaUpdateProducts = function() {
	
	$("#nameFieldByteOffsetInput").numeric();
	$("#nameFieldLengthInput").numeric();
	
	$("#presentationFieldByteOffsetInput").numeric();
	$("#presentationFieldLengthInput").numeric();
	
	$("#priceFieldByteOffsetInput").numeric();
	$("#priceFieldLengthInput").numeric();
	
	$("#codeFieldByteOffsetInput").numeric();
	$("#codeFieldLengthInput").numeric();
	
	$("#gtinFieldByteOffsetInput").numeric();
	$("#gtinFieldLengthInput").numeric();
	
	$("#coldFieldByteOffsetInput").numeric();
	$("#coldFieldLengthInput").numeric();
	
	$('#alfabetaUpdateFileInput').fileupload({
		add: function (e, data) {
            var uploadError = false;
			var extension = data.originalFiles[0].name.split('.').pop().toLowerCase();

			if ($.inArray(extension, ['dat']) == -1) {
            	uploadError = true;
                myShowAlert('danger', 'Tipo de archivo no v\u00e1lido.');
            }

            if (data.originalFiles[0]['size'] > (15*1024*1024)) {//5 MB
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
			var aaData = [];
			var row = {
				fileName: data.result.fileName,
				fileSize: data.result.fileSize
			};
			aaData.push(row);
			$("#uploaded-files").bootgrid("append", aaData);
        }
    });
	
	var validateForm = function() {
		var form = $("#alfabetaUpdateproductsAdministrationForm");
		form.validate({
			rules: {
				nameFieldByteOffset: {
					required: true
				},
				nameFieldLength: {
					required: true
				},
				presentationFieldByteOffset: {
					required: true
				},
				presentationFieldLength: {
					required: true
				},
				priceFieldByteOffset: {
					required: true
				},
				priceFieldLength: {
					required: true
				},
				codeFieldByteOffset: {
					required: true
				},
				codeFieldLength: {
					required: true
				},
				gtinFieldByteOffset: {
					required: true
				},
				gtinFieldLength: {
					required: true
				},
				coldFieldByteOffset: {
					required: true
				},
				coldFieldLength: {
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
			var jsonAlfabetaFile = {
					"nameFieldByteOffset": $("#nameFieldByteOffsetInput").val(),
					"nameFieldLength": $("#nameFieldLengthInput").val(),
					"presentationFieldByteOffset": $("#presentationFieldByteOffsetInput").val(),
					"presentationFieldLength": $("#presentationFieldLengthInput").val(),
					"priceFieldByteOffset": $("#priceFieldByteOffsetInput").val(),
					"priceFieldLength": $("#priceFieldLengthInput").val(),
					"codeFieldByteOffset": $("#codeFieldByteOffsetInput").val(),
					"codeFieldLength": $("#codeFieldLengthInput").val(),
					"gtinFieldByteOffset": $("#gtinFieldByteOffsetInput").val(),
					"gtinFieldLength": $("#gtinFieldLengthInput").val(),
					"coldFieldByteOffset": $("#coldFieldByteOffsetInput").val(),
					"coldFieldLength": $("#coldFieldLengthInput").val()
			};

			$.ajax({
				url: "updateProducts.do",
				type: "POST",
				contentType:"application/json",
				data: JSON.stringify(jsonAlfabetaFile),
				async: false,
				success: function(response) {
					myShowAlert('success', 'Se ha actualizado satisfactoriamente el listado de productos.');
					$("#uploaded-files").bootgrid("destroy");
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