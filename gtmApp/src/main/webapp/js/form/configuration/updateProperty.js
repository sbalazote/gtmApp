UpdateProperty = function() {

	myShowAlert("info", $("#licenseInfo").html(), null, 0);

	var validateForm = function() {
		var form = $("#updatePropertyForm");
		form.validate({
			rules: {
				code: {
					required: true,
					digits: true,
					maxlength: 9
				},
				name: {
					required: true,
					maxlength: 45
				},
				taxId: {
					required: true,
					digits: true,
					exactLength: 11
				},
				corporateName: {
					required: true,
					maxlength: 45
				},
				province: {
					required: true
				},
				locality: {
					required: true,
					maxlength: 45
				},
				address: {
					required: true,
					maxlength: 45
				},
				mail: {
					email: true,
					maxlength: 45
				},
				zipCode: {
					required: true,
					maxlength: 10
				},
				gln: {
					required: true,
					exactLength: 13
				},
				agent: {
					required: true
				},
				VATLiability: {
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
					maxlength: 45
				},
				selfSerializedTagFilepath: {
					required: true
				},
				ANMATName: {
					required: true,
					maxlength: 45
				},
				ANMATPassword: {
					required: true, 
					minlength: 5,
					maxlength: 15
				},
				repeatANMATPassword: { 
	                required: true,
					equalTo: "#ANMATPasswordInput",
	                minlength: 5,
					maxlength: 15
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
				informAnmat: {
					required: true
				},
				fontSize: {
					required: true,
					digits: true
				},
				numberX: {
					required: true, digits: true
				},
				numberY: {
					required: true, digits: true},
				dateX: {required: true, digits: true},
				dateY: {required: true, digits: true},
				issuerCorporateNameX: {required: true, digits: true},
				issuerCorporateNameY: {required: true, digits: true},
				issuerAddressX: {required: true, digits: true},
				issuerAddressY: {required: true, digits: true},
				issuerLocalityX: {required: true, digits: true},
				issuerLocalityY: {required: true, digits: true},
				issuerZipcodeX: {required: true, digits: true},
				issuerZipcodeY: {required: true, digits: true},
				issuerProvinceX: {required: true, digits: true},
				issuerProvinceY: {required: true, digits: true},
				issuerVatliabilityX: {required: true, digits: true},
				issuerVatliabilityY: {required: true, digits: true},
				issuerTaxX: {required: true, digits: true},
				issuerTaxY: {required: true, digits: true},
				deliveryLocationCorporateNameX: {required: true, digits: true},
				deliveryLocationCorporateNameY: {required: true, digits: true},
				deliveryLocationAddressX: {required: true, digits: true},
				deliveryLocationAddressY: {required: true, digits: true},
				deliveryLocationLocalityX: {required: true, digits: true},
				deliveryLocationLocalityY: {required: true, digits: true},
				deliveryLocationZipcodeX: {required: true, digits: true},
				deliveryLocationZipcodeY: {required: true, digits: true},
				deliveryLocationProvinceX: {required: true, digits: true},
				deliveryLocationProvinceY: {required: true, digits: true},
				deliveryLocationVatliabilityX: {required: true, digits: true},
				deliveryLocationVatliabilityY: {required: true, digits: true},
				deliveryLocationTaxX: {required: true, digits: true},
				deliveryLocationTaxY: {required: true, digits: true},
				affiliateX: {required: true, digits: true},
				affiliateY: {required: true, digits: true},
				orderX: {required: true, digits: true},
				orderY: {required: true, digits: true},
				issuerGlnX: {required: true, digits: true},
				issuerGlnY: {required: true, digits: true},
				deliveryLocationGlnX: {required: true, digits: true},
				deliveryLocationGlnY: {required: true, digits: true},
				productDetailsY: {required: true, digits: true},
				productDescriptionX: {required: true, digits: true},
				productMonodrugX: {required: true, digits: true},
				productBrandX: {required: true, digits: true},
				productAmountX: {required: true, digits: true},
				productBatchExpirationdateX: {required: true, digits: true},
				serialColumn1X: {required: true, digits: true},
				serialColumn2X: {required: true, digits: true},
				serialColumn3X: {required: true, digits: true},
				serialColumn4X: {required: true, digits: true},
				numberOfItemsX: {required: true, digits: true},
				numberOfItemsY: {required: true, digits: true},
			},
			showErrors: myShowErrors,
			onsubmit: false
		});
		return form.valid();
	};

	$('#uploadLogoInput').fileupload({
		url: '/uploadLogo.do',
		dataType: 'json',
		autoUpload: true,
		acceptFileTypes: /(\.|\/)(gif|jpe?g|png)$/i,
		loadImageFileTypes: /^image\/(gif|jpeg|png)$/,
		maxFileSize: 999000,
		previewCrop: true,
		imageMaxWidth: 256,
		imageMaxHeight: 256,
		imageMinWidth: 256,
		imageMinHeight: 256,
		imageForceResize: true,
		// Enable image resizing, except for Android and Opera,
		// which actually support image resizing, but fail to
		// send Blob objects via XHR requests:
		disableImageResize: /Android(?!.*Chrome)|Opera/.test(window.navigator.userAgent),
		previewMaxWidth: 100,
		previewMaxHeight: 100
	}).on('fileuploadadd', function (e, data) {
		data.context = $('<div/>').appendTo('#files');
		$.each(data.files, function (index, file) {
			var node = $('<p/>').append($('<span/>').text(file.name));
			node.appendTo(data.context);
		});
	}).on('fileuploadprocessalways', function (e, data) {
		var index = data.index,
			file = data.files[index],
			node = $(data.context.children()[index]);
		if (file.preview) {
			node.prepend('<br>').prepend(file.preview);
		}
		if (file.error) {
			node.append('<br>').append($('<span class="text-danger"/>').text(file.error));
		}
	}).on('fileuploadprogressall', function (e, data) {
		var progress = parseInt(data.loaded / data.total * 100, 10);
		$('#progress .progress-bar').css('width', progress + '%').attr('aria-valuenow', progress);
	}).on('fileuploaddone', function (e, data) {
		$("#uploadLogoInput").prop("disabled",true);
		$(".fileinput-button").attr("disabled",true);
	}).on('fileuploadfail', function (e, data) {
		$.each(data.files, function (index) {
			var error = $('<span class="text-danger"/>').text('Error! No se pudo actualizar el logo.');
			$(data.context.children()[index]).append('<br>').append(error);
		});
	}).prop('disabled', !$.support.fileInput).parent().addClass($.support.fileInput ? undefined : 'disabled');
	
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
				"VATLiabilityId": $("#VATLiabilitySelect option:selected").val(),
				"typeId": $("#typeSelect option:selected").val(),
				"daysAgoPendingTransactions": $("#daysAgoPendingTransactionsInput").val(),
				"numberOfDeliveryNoteDetailsPerPage": $("#numberOfDeliveryNoteDetailsPerPageInput").val(),
				"selfSerializedTagFilepath": $("#selfSerializedTagFilepathInput").val(),
				"ANMATName":  $("#ANMATNameInput").val(),
				"ANMATPassword": $("#ANMATPasswordInput").val(),
				"printDeliveryNoteConceptId": $("#conceptSelect option:selected").val(),
				"startTraceConceptSelectId": $("#startTraceConceptSelect option:selected").val(),
				"proxy":  $("#proxyNameInput").val(),
				"proxyNumber":  $("#proxyNumberInput").val(),
				"informProxy": $("#proxySelect").val(),
				"informAnmat": $("#informAnmatSelect").val(),
				"supplyingConceptSelectId": $("#supplyingConceptSelect option:selected").val(),
				"fontSize": $("#fontSizeInput").val(),
				"numberX": $("#numberXInput").val(),
				"numberY": $("#numberYInput").val(),
				"numberPrint": $("#numberPrintInput").is(":checked"),
				"dateX": $("#dateXInput").val(),
				"dateY": $("#dateYInput").val(),
				"datePrint": $("#datePrintInput").is(":checked"),
				"issuerCorporateNameX": $("#issuerCorporateNameXInput").val(),
				"issuerCorporateNameY": $("#issuerCorporateNameYInput").val(),
				"issuerCorporateNamePrint": $("#issuerCorporateNamePrintInput").is(":checked"),
				"issuerAddressX": $("#issuerAddressXInput").val(),
				"issuerAddressY": $("#issuerAddressYInput").val(),
				"issuerAddressPrint": $("#issuerAddressPrintInput").is(":checked"),
				"issuerLocalityX": $("#issuerLocalityXInput").val(),
				"issuerLocalityY": $("#issuerLocalityYInput").val(),
				"issuerLocalityPrint": $("#issuerLocalityPrintInput").is(":checked"),
				"issuerZipcodeX": $("#issuerZipcodeXInput").val(),
				"issuerZipcodeY": $("#issuerZipcodeYInput").val(),
				"issuerZipcodePrint": $("#issuerZipcodePrintInput").is(":checked"),
				"issuerProvinceX": $("#issuerProvinceXInput").val(),
				"issuerProvinceY": $("#issuerProvinceYInput").val(),
				"issuerProvincePrint": $("#issuerProvincePrintInput").is(":checked"),
				"issuerVatliabilityX": $("#issuerVatliabilityXInput").val(),
				"issuerVatliabilityY": $("#issuerVatliabilityYInput").val(),
				"issuerVatliabilityPrint": $("#issuerVatliabilityPrintInput").is(":checked"),
				"issuerTaxX": $("#issuerTaxXInput").val(),
				"issuerTaxY": $("#issuerTaxYInput").val(),
				"issuerTaxPrint": $("#issuerTaxPrintInput").is(":checked"),
				"deliveryLocationCorporateNameX": $("#deliveryLocationCorporateNameXInput").val(),
				"deliveryLocationCorporateNameY": $("#deliveryLocationCorporateNameYInput").val(),
				"deliveryLocationCorporateNamePrint": $("#deliveryLocationCorporateNamePrintInput").is(":checked"),
				"deliveryLocationAddressX": $("#deliveryLocationAddressXInput").val(),
				"deliveryLocationAddressY": $("#deliveryLocationAddressYInput").val(),
				"deliveryLocationAddressPrint": $("#deliveryLocationAddressPrintInput").is(":checked"),
				"deliveryLocationLocalityX": $("#deliveryLocationLocalityXInput").val(),
				"deliveryLocationLocalityY": $("#deliveryLocationLocalityYInput").val(),
				"deliveryLocationLocalityPrint": $("#deliveryLocationLocalityPrintInput").is(":checked"),
				"deliveryLocationZipcodeX": $("#deliveryLocationZipcodeXInput").val(),
				"deliveryLocationZipcodeY": $("#deliveryLocationZipcodeYInput").val(),
				"deliveryLocationZipcodePrint": $("#deliveryLocationZipcodePrintInput").is(":checked"),
				"deliveryLocationProvinceX": $("#deliveryLocationProvinceXInput").val(),
				"deliveryLocationProvinceY": $("#deliveryLocationProvinceYInput").val(),
				"deliveryLocationProvincePrint": $("#deliveryLocationProvincePrintInput").is(":checked"),
				"deliveryLocationVatliabilityX": $("#deliveryLocationVatliabilityXInput").val(),
				"deliveryLocationVatliabilityY": $("#deliveryLocationVatliabilityYInput").val(),
				"deliveryLocationVatliabilityPrint": $("#deliveryLocationVatliabilityPrintInput").is(":checked"),
				"deliveryLocationTaxX": $("#deliveryLocationTaxXInput").val(),
				"deliveryLocationTaxY": $("#deliveryLocationTaxYInput").val(),
				"deliveryLocationTaxPrint": $("#deliveryLocationTaxPrintInput").is(":checked"),
				"affiliateX": $("#affiliateXInput").val(),
				"affiliateY": $("#affiliateYInput").val(),
				"affiliatePrint": $("#affiliatePrintInput").is(":checked"),
				"orderX": $("#orderXInput").val(),
				"orderY": $("#orderYInput").val(),
				"orderPrint": $("#orderPrintInput").is(":checked"),
				"issuerGlnX": $("#issuerGlnXInput").val(),
				"issuerGlnY": $("#issuerGlnYInput").val(),
				"issuerGlnPrint": $("#issuerGlnPrintInput").is(":checked"),
				"deliveryLocationGlnX": $("#deliveryLocationGlnXInput").val(),
				"deliveryLocationGlnY": $("#deliveryLocationGlnYInput").val(),
				"deliveryLocationGlnPrint": $("#deliveryLocationGlnPrintInput").is(":checked"),
				"productDetailsY": $("#productDetailsYInput").val(),
				"productDescriptionX": $("#productDescriptionXInput").val(),
				"productDescriptionPrint": $("#productDescriptionPrintInput").is(":checked"),
				"productMonodrugX": $("#productMonodrugXInput").val(),
				"productMonodrugPrint": $("#productMonodrugPrintInput").is(":checked"),
				"productBrandX": $("#productBrandXInput").val(),
				"productBrandPrint": $("#productBrandPrintInput").is(":checked"),
				"productAmountX": $("#productAmountXInput").val(),
				"productAmountPrint": $("#productAmountPrintInput").is(":checked"),
				"productBatchExpirationdateX": $("#productBatchExpirationdateXInput").val(),
				"productBatchExpirationdatePrint": $("#productBatchExpirationdatePrintInput").is(":checked"),
				"serialColumn1X": $("#serialColumn1XInput").val(),
				"serialColumn2X": $("#serialColumn2XInput").val(),
				"serialColumn3X": $("#serialColumn3XInput").val(),
				"serialColumn4X": $("#serialColumn4XInput").val(),
				"serialColumn1Print": $("#serialColumn1PrintInput").is(":checked"),
				"serialColumn2Print": $("#serialColumn2PrintInput").is(":checked"),
				"serialColumn3Print": $("#serialColumn3PrintInput").is(":checked"),
				"serialColumn4Print": $("#serialColumn4PrintInput").is(":checked"),
				"numberOfItemsX": $("#numberOfItemsXInput").val(),
				"numberOfItemsY": $("#numberOfItemsYInput").val(),
				"numberOfItemsPrint": $("#numberOfItemsPrintInput").is(":checked")
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

	$("#supplyingConceptSelect").chosen({
		width: '100%' /* desired width */
	});

	$("#startTraceConceptSelect").chosen({
		width: '100%' /* desired width */
	});

	$("#changePasswordSelect").chosen({
		width: '100%' /* desired width */
	});

	$("#proxySelect").chosen({
		width: '100%' /* desired width */
	});

	$("#provinceSelect").chosen({
		width: '100%' /* desired width */
	});

	$("#agentSelect").chosen({
		width: '100%' /* desired width */
	});

	$("#VATLiabilitySelect").chosen({
		width: '100%' /* desired width */
	});

	$("#informAnmatSelect").chosen({
		width: '100%' /* desired width */
	});
};