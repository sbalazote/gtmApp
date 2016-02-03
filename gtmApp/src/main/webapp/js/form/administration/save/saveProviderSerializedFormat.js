SaveProviderSerializedFormat = function() {
	var position = 0;
	var listOfFields = [];
	var fieldValue;
	var isOk = false;
	var validateForm = function() {
		var form = $("#providerSerializedFormatAdministrationForm");
		form.validate({
			rules: {
				length: {
					digits: true,
					required: true
				}
			},
			showErrors: myShowErrors,
			onsubmit: false
		});
		return form.valid();
	};
	
	$("#add").click(function() {
		if (validateForm()) {
			position += 1;
			var field = {};
			field.type = $("#fieldType option:selected").val();
			field.typeText = $("#fieldType option:selected").text();
			field.length = $("#lengthInput").val();
			listOfFields.push(field);
			if(field.type == "G"){
				isOk = true;
			}
			fillTable();
			$('#fieldType option[value="'+ field.type +'"]').remove();
			$('#fieldType').trigger('chosen:updated');
			$('#lengthInput').val('');
			if (listOfFields.length == 4) {
				$("#add").attr('disabled', true);
			} else {
				$("#add").attr('disabled', false);
			}
		}
	});
	
	
	var fillTable = function(){
		$('#fieldsTable tbody > tr').remove();
		for(var i = 0; i < listOfFields.length; i++) {
			var position = i+1;
			$("#fieldsTable > tbody").append("<tr><td>" + position + "</td><td>"+ listOfFields[i].typeText  +"<span class='span-field' style='display:none'>" + listOfFields[i].type + "</span></td><td>"+ listOfFields[i].length + "</td><td><a href='javascript:void(0);' class='delete-row'>Eliminar</a></td></tr>");
		}
	};
	
	$('#fieldsTable').on("click", ".delete-row", function() {
		var parent = $(this).parent().parent();
	
		currentRow = $(".delete-row").index(this);
		fieldValue = parent.find(".span-field").html();
		
		for(var i = listOfFields.length - 1; i >= 0; i--) {
		    if(listOfFields[i].type === fieldValue) {
		    	$('#fieldType').append("<option value='"+ listOfFields[i].type + "'>" + listOfFields[i].typeText + "</option>");
		    	$('#fieldType').trigger('chosen:updated');
		    	listOfFields.splice(i, 1);
		    }
		}
		fillTable();
		if (listOfFields.length == 4) {
			$("#add").attr('disabled', true);
		} else {
			$("#add").attr('disabled', false);
		}
	});

	var existsProviderSerializedFormat = function(jsonProviderSerializedFormat) {
		var exists = false;
		$.ajax({
			url: "existsProviderSerializedFormat.do",
			type: "POST",
			contentType:"application/json",
			data: JSON.stringify(jsonProviderSerializedFormat),
			async: false,
			success: function(response) {
				exists = response;
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myGenericError();
			}
		});
		return exists;
	};
	
	$("#confirm").click(function() {
		if (listOfFields.length > 0) {
			if(isOk){
				var jsonProviderSerializedFormat = {
						"id": null,
						"gtin": null,
						"serialNumber": null,
						"expirationDate": null,
						"batch": null,
						"sequence": null,
				};
				var secuence = "";
				for(var i = 0; i < listOfFields.length; i++) {
					switch(listOfFields[i].type) {
					case "G":
						jsonProviderSerializedFormat.gtin = listOfFields[i].length;
						break;
					case "S":
						jsonProviderSerializedFormat.serialNumber = listOfFields[i].length;
						break;
					case "E":
						jsonProviderSerializedFormat.expirationDate = listOfFields[i].length;
						break;
					case "B":
						jsonProviderSerializedFormat.batch = listOfFields[i].length;
						break;
					}
					secuence += listOfFields[i].type;
					if (i != listOfFields.length - 1) {
						secuence += "-";
					}
				}
				jsonProviderSerializedFormat.sequence = secuence;
				if(existsProviderSerializedFormat(jsonProviderSerializedFormat)){
					myShowAlert('danger', 'La parametrizacion ya existe.');
				}else{
					$.ajax({
						url: "saveProviderSerializedFormat.do",
						type: "POST",
						contentType:"application/json",
						data: JSON.stringify(jsonProviderSerializedFormat),
						async: true,
						success: function(response) {
							if ($("#idInput").length > 0) {
								myUpdateSuccessful();
							} else {
								myCreateSuccessful();
							}
						},
						error: function(jqXHR, textStatus, errorThrown) {
							myGenericError();
						}
					});
				}
			}else{
				myShowAlert('danger', 'El campo GTIN debe ser informado.');
			}
		} else {
			myShowAlert('danger', 'Por favor, al menos ingrese un campo parametrizable.');
		}
	});
	
	$(".alert .close").on('click', function(e) {
	    $(this).parent().hide();
	});
};