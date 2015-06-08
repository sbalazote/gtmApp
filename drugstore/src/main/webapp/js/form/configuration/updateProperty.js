UpdateProperty = function() {	
	
	var validateForm = function() {
		var form = $("#updatePropertyForm");
		form.validate({
			rules: {
				code: {
					required: true,
					digits: true,
					maxlength: 9,
				},
				name: {
					required: true,
					maxlength: 45,
				},
				taxId: {
					required: true,
					digits: true,
					exactLength: 11
				},
				corporateName: {
					required: true,
					maxlength: 45,
				},
				province: {
					required: true
				},
				locality: {
					required: true,
					maxlength: 45,
				},
				address: {
					required: true,
					maxlength: 45,
				},
				mail: {
					email: true,
					maxlength: 45,
				},
				zipCode: {
					required: true,
					maxlength: 10,
				},
				gln: {
					required: true,
					exactLength: 13
				},
				agent: {
					required: true
				},
				type: {
					required: true
				},
				daysAgoPendingTransactions: {
					required: true,
					digits: true
				},
				numberOfDeliveryNoteDetailsPerPage: {
					required: true,
					digits: true
				},
				phone: {
					maxlength: 45,
				},
				orderLabelFilepath: {
					required: true
				},
				selfSerializedTagFilepath: {
					required: true
				},
				deliveryNoteFilepath: {
					required: true
				},
				pickingFilepath: {
					required: true
				},
				ANMATName: {
					required: true,
					maxlength: 45,
				},
				ANMATPassword: {
					required: true, 
					minlength: 5,
					maxlength: 15,
				},
				repeatANMATPassword: { 
	                required: true, equalTo: "#ANMATPasswordInput", 
	                minlength: 5,
					maxlength: 15,
		        },
				proxyName: {
			        required: function(element) {
			            return $("#proxySelect").val() == "true";
			        }
				},
				proxyNumber: {
			        required: function(element) {
			            return $("#proxySelect").val() == "true";
			        },
					digits: true
				},
			},
			showErrors: myShowErrors,
			onsubmit: false
		});
		return form.valid();
	};
	
	$("#confirm").click(function() {
		if (validateForm()) {
			var jsonProperties = {
					"id": $("#idInput").val(),
					"code": $("#codeInput").val(),
					"name": $("#nameInput").val(),
					"taxId": $("#taxIdInput").val(),
					"corporateName": $("#corporateNameInput").val(),
					"provinceId": $("#provinceSelect option:selected").val(),
					"locality": $("#localityInput").val(),
					"address": $("#addressInput").val(),
					"zipCode": $("#zipCodeInput").val(),
					"phone": $("#phoneInput").val(),
					"mail": $("#mailInput").val(),
					"gln": $("#glnInput").val(),
					"agentId": $("#agentSelect option:selected").val(),
					"typeId": $("#typeSelect option:selected").val(),
					"daysAgoPendingTransactions": $("#daysAgoPendingTransactionsInput").val(),
					"numberOfDeliveryNoteDetailsPerPage": $("#numberOfDeliveryNoteDetailsPerPageInput").val(),
					"orderLabelFilepath": $("#orderLabelFilepathInput").val(),
					"selfSerializedTagFilepath": $("#selfSerializedTagFilepathInput").val(),
					"deliveryNoteFilepath": $("#deliveryNoteFilepathInput").val(),
					"pickingFilepath": $("#pickingFilepathInput").val(),
					"ANMATName":  $("#ANMATNameInput").val(),
					"ANMATPassword": $("#ANMATPasswordInput").val(),
					"printDeliveryNoteConceptId": $("#conceptSelect option:selected").val(),
					"startTraceConceptSelectId": $("#startTraceConceptSelect option:selected").val(),
					"proxy":  $("#proxyNameInput").val(),
					"proxyNumber":  $("#proxyNumberInput").val(),
					"informProxy": $("#proxySelect").val(),
					"supplyingConceptSelectId": $("#supplyingConceptSelect option:selected").val(),
			};

			$.ajax({
				url: "saveProperty.do",
				type: "POST",
				contentType:"application/json",
				data: JSON.stringify(jsonProperties),
				async: true,
				success: function(response) {
					myUpdateSuccessful();
				},
				error: function(jqXHR, textStatus, errorThrown) {
					myGenericError();
				}
			});
		}
	});
	
	$("#changePasswordSelect").change(function(){
		var value = $(this).find("option:selected").val();
		if (value == "true") {
			$("#ANMATPasswordInput").prop("disabled", false);
			$("#repeatANMATPasswordInput").prop("disabled", false);
		} else {
			$("#ANMATPasswordInput").prop("disabled", true);
			$("#repeatANMATPasswordInput").prop("disabled", true);
			$("#ANMATPasswordInput").val("");
			$("#repeatANMATPasswordInput").val("");
			$('#ANMATPasswordInput').data("title", "").removeClass("has-error").tooltip("destroy");
			$('#repeatANMATPasswordInput').data("title", "").removeClass("has-error").tooltip("destroy");
		}
	});
	
	$("#proxySelect").change(function(){
		var value = $(this).find("option:selected").val();
		if (value == "true") {
			$("#proxyNameInput").prop("disabled", false);
			$("#proxyNumberInput").prop("disabled", false);
		} else {
			$("#proxyNameInput").prop("disabled", true);
			$("#proxyNumberInput").prop("disabled", true);
			$("#proxyNameInput").val("");
			$("#proxyNumberInput").val("");
			$('#proxyNameInput').data("title", "").removeClass("has-error").tooltip("destroy");
			$('#proxyNumberInput').data("title", "").removeClass("has-error").tooltip("destroy");
		}
	});
	
	/* ensure any open panels are closed before showing selected */
	$('#accordion').on('show.bs.collapse', function () {
	    $('#accordion .in').collapse('hide');
	});

    $("#supplyingConceptSelect").chosen(
        {
            width: '100%' /* desired width */
        });
    $("#startTraceConceptSelect").chosen(
        {
            width: '100%' /* desired width */
        });
    $("#changePasswordSelect").chosen(
        {
            width: '100%' /* desired width */
        });
    $("#proxySelect").chosen(
        {
            width: '100%' /* desired width */
        });

};