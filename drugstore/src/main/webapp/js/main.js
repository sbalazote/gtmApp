$(document).ready(function() {
	
	// To highlight menu options
	
	var path = window.location.pathname.split("/");
	var href = path[path.length-1];
	$("ul.navbar-nav li a[href='"+href+"'] ").closest("li.activable").addClass("active");
	
	// My Global Functions
	
	myShowErrors = function(errorMap, errorList) {
		// Limpio los tooltips para elementos validos.
		$.each(this.validElements(), function (index, element) {
			// Limpio el titulo del tooltip. No hay elementos invalidos asociados.
			if ($(element).hasClass("chosen-select")) {
				var chosenElement = $(element).next().find("a");
				chosenElement.data("title", "").removeClass("has-error").tooltip("destroy");
			} else {
				$(element).data("title", "").removeClass("has-error").tooltip("destroy");
			}
		});
		// 	Creo nuevos tooltips para elementos invalidos.
		$.each(errorList, function (index, error) {
			// Destruyo tooltips preexistentes y cargo otros con nuevo contenido.
			if ($(error.element).hasClass("chosen-select")) {
				var chosenElement = $(error.element).next().find("a");
				chosenElement.tooltip("destroy").data("title", error.message).addClass("has-error").tooltip();
			} else {
				$(error.element).tooltip("destroy").data("title", error.message).addClass("has-error").tooltip();
			}
		});
	};
	
	myResetForm = function(form, validator) {
		$("input", form).each(function() {
			$(this).data("title", "").removeClass("has-error").tooltip("destroy");
			$(this).val("");
		});
		if (validator != null) {
			validator.resetForm();
		}
	};
	
	myParseDate = function(date) {
		var myDate = new Date(date);
		var day = ("0" + myDate.getDate()).slice(-2);
		var month = ("0" + (myDate.getMonth() + 1)).slice(-2);
		var year = myDate.getFullYear();
		return day + "/" + month + "/" + year;
	};
	
	myParseDateTime = function(date) {
		var myDate = new Date(date);
		var day = ("0" + myDate.getDate()).slice(-2);
		var month = ("0" + (myDate.getMonth() + 1)).slice(-2);
		var year = myDate.getFullYear();
		var hour = ("0" + myDate.getHours()).slice(-2);
		var minutes = ("0" + myDate.getMinutes()).slice(-2);
		var seconds = ("0" + myDate.getSeconds()).slice(-2);
		return day + "/" + month + "/" + year + " " + hour + ":" + minutes + ":" + seconds;
	};
	
	validateExpirationDate = function(dd, mm, aaaa) {
		var valid = false;
		var xdata = new Date(aaaa,mm-1,dd);
		if ( ( xdata.getFullYear() === aaaa ) && ( xdata.getMonth() === mm - 1 ) && ( xdata.getDate() === dd ) ){
			var today = new Date();
			if (today <= xdata) {
				valid = true;
			}
		}
		return valid;
	};
	
	addLeadingZeros = function(n, length)
	{
	    var str = (n > 0 ? n : -n) + "";
	    var zeros = "";
	    for (var i = length - str.length; i > 0; i--)
	        zeros += "0";
	    zeros += str;
	    return n >= 0 ? zeros : "-" + zeros;
	};
	
	// My Custom Validators
	
    jQuery.validator.addMethod("expirationDate", function(value, element) {
        var check = false;
        var re = /^\d{6}$/;
        if (re.test(value)) {
            var dd = parseInt(value.substring(0,2));
            var mm = parseInt(value.substring(2,4));
            var aaaa = parseInt("20"+value.substring(4));
            var xdata = new Date(aaaa,mm-1,dd);
            if ( ( xdata.getFullYear() === aaaa ) && ( xdata.getMonth() === mm - 1 ) && ( xdata.getDate() === dd ) ){
            	var today = new Date();
            	if (today <= xdata) {
            		check = true;
            	}
            }
        }
        return this.optional(element) || check;
    }, "Por favor, escribe una fecha mayor a la fecha del d�a. (Formato: ddmmaa)");
    
    jQuery.validator.addMethod("minDate", function(value, element, param) {
    	var partsTo = value.split("/");
    	var partsFrom = param.val().split("/");
    	if (partsTo == "" || partsTo == "")
    		return true;
    	var dateF = new Date(partsFrom[2], partsFrom[1] - 1, partsFrom[0]);
    	var dateT = new Date(partsTo[2], partsTo[1] - 1, partsTo[0]);
    	return (dateT >= dateF);
    }, jQuery.format("La Fecha Hasta debe ser mayor o igual a la Fecha Desde."));
    
    jQuery.validator.addMethod("maxDate", function(value, element, param) {
    	var partsFrom = value.split("/");
    	var partsTo = param.val().split("/");
    	if (partsTo == "" || partsTo == "")
    		return true;
    	var dateF = new Date(partsFrom[2], partsFrom[1] - 1, partsFrom[0]);
    	var dateT = new Date(partsTo[2], partsTo[1] - 1, partsTo[0]);
    	return (dateF <= dateT);
    }, jQuery.format("La Fecha Desde debe ser menor o igual a la Fecha Hasta."));
    
    jQuery.validator.addMethod("exactLength", function(value, element, param) {
   	 return this.optional(element) || value.length == param;
   	}, jQuery.format("La longitud debe ser exactamente {0} caracteres."));

	// To activate chosen-select
	$(".chosen-select").chosen({
		allow_single_deselect: true,
		placeholder_text_single: 'Seleccione una opción',
        placeholder_text_multiple: 'Seleccione las opciones',
		no_results_text: "No se encontraron resultados."
	});
	
	// To validate chosen-select
	$.validator.setDefaults({ignore: ":hidden:not(select)"});
	
	$(".chosen-select").change(function() {
		if ($(this).val()) {
			$(this).next().find("a").data("title", "").removeClass("has-error").tooltip("destroy");
		}
	});
    
	//a minimum number value on a validate form
	$.validator.addMethod('minValue', function (value, el, param) {
	    return value > param;
	},"Por favor, ingrese un n�mero mayor a cero");
	
	printIOPDFHeader = function(doc, mode, title, currentSheet, totalSheets) {
		doc.setFontSize(16);
		doc.setFont("helvetica");
		doc.setFontType("bold");
		doc.text(20, 20, title);
		doc.setFont("courier");
		doc.setFontType("normal");
		doc.text(180, 10, 'Hoja ' + currentSheet + '/' + totalSheets);
		doc.text(150, 20, 'Fecha: ' + $("#currentDateInput").val());

		doc.text(20, 40, 'Concepto: ' + $("#conceptInput option:selected").text());
		if ($("#providerInput").val() != "") {
			doc.text(20, 50, 'Proveedor: ' + $("#providerInput option:selected").text());
		} else {
			if (mode == 'input') {
				doc.text(20, 50, 'Lugar de Entrega: ' + $("#deliveryLocationInput option:selected").text());
			} else {
				doc.text(20, 50, 'Cliente: ' + $("#clientInput option:selected").text());
			}
		}
		doc.text(20, 60, 'Convenio: ' + $("#agreementInput option:selected").text());
		if (mode == 'input') {
			doc.text(20, 70, 'N�mero de Remito: R' + $("#deliveryNotePOSInput").val() + '-' + $("#deliveryNoteNumberInput").val());
			doc.text(20, 80, 'Orden de Compra: ' + $("#purchaseOrderNumberInput").val().trim());
		}

		doc.setFontSize(8);
		doc.setFontType("bold");
		doc.text(10, 90, 'PRODUCTO');
		doc.text(70, 90, 'SERIE');
		doc.text(120, 90, 'LOTE');
		doc.text(160, 90, 'VENCIMIENTO');
		doc.text(190, 90, 'CANT.');
		doc.setFontType("normal");
		doc.setLineWidth(1);
		doc.line(5, 95, 205, 95);
	};
	
	printIOPDF = function(mode, id, details) {
		var doc = new jsPDF();
		var currentSheet = 1;
		var totalSheets = Math.floor(details.length / 20) + 1;
		var offsetY = 100;
		var title = (mode == 'input') ? 'Recepci�n de Mercader�a Nro.: ' : 'Egreso de Mercader�a Nro.: ';
		
		printIOPDFHeader(doc, mode, title + id.toString(), currentSheet, totalSheets);
		
		// Saltando de a 10mm por detalle -> entran 20 detalles por hoja, del offsetY = 100 hasta el offsetY = 290 (inclusive).
		$.each(details, function(index, value) {
			if ((index % 20 == 0) && (index > 0)) {
				doc.addPage();
				currentSheet++;
				offsetY = 100;
				printIOPDFHeader(doc, title + id.toString(), currentSheet, totalSheets);
			}
			if (value.serialNumber == null) {
				value.serialNumber = "-";
			}
			
			doc.text(10, offsetY, value.product.code.toString() + ' - ' + value.product.description);
			doc.text(70, offsetY, value.serialNumber);
			doc.text(120, offsetY, value.batch);
			doc.text(160, offsetY, myParseDate(value.expirationDate));
			doc.text(190, offsetY, value.amount.toString());
			offsetY += 10;
		});
		
		return doc;
	};
	
	var fileDownloadCheckTimer;
	blockUIForDownload = function(path) {
		var token = new Date().getTime(); //use the current timestamp as the token value
		$.download(path,'fileDownloadToken=' + token, 'POST' );
		$.blockUI({ message: 'Exportando. Espere un Momento por favor...' });
		fileDownloadCheckTimer = window.setInterval(function () {
			var cookieValue = $.cookie('fileDownloadToken');
			if (cookieValue == token)
				finishDownload();
		}, 1000);
	};

	finishDownload = function() {
		window.clearInterval(fileDownloadCheckTimer);
		$.removeCookie('fileDownloadToken', { path: '/drogueria/' }); //clears this cookie value
		$.unblockUI();
	};
	
	exportTableHTML = function(url) {
		return "<div class=\"btn-group\" style=\"margin-right: 20px;\">" +
		"<button class=\"btn btn-warning dropdown-toggle\" data-toggle=\"dropdown\">" +
		"<span class=\"icon glyphicon glyphicon-tasks\"></span> Exportar" +
		"</button>" +
		"<ul class=\"dropdown-menu \" role=\"menu\">" +
		"<li><a href=\"#\" onclick=\"blockUIForDownload('"+url+".json'\);\"> <img src=\"icons/json.png\" width=\"24px\"> JSON</a></li>" +
		"<li class=\"divider\"></li>" +
		"<li><a href=\"#\" onclick=\"blockUIForDownload('"+url+".xml');\"> <img src=\"icons/xml.png\" width=\"24px\"> XML</a></li>" +
		"<li class=\"divider\"></li>" +
		"<li><a href=\"#\" onclick=\"blockUIForDownload('"+url+".xlsx');\"> <img src=\"icons/xls.png\" width=\"24px\"> XLSX</a></li>" +
		"<li class=\"divider\"></li>" +
		"<li><a href=\"#\" onclick=\"blockUIForDownload('"+url+".pdf');\"> <img src=\"icons/pdf.png\" width=\"24px\"> PDF</a></li>" +
		"</ul>" +
		"</div>";
	};
	
	exportQueryTableHTML = function(url, params) {
		return "<div class=\"btn-group\" style=\"margin-right: 20px;\">" +
		"<button class=\"btn btn-warning dropdown-toggle\" data-toggle=\"dropdown\">" +
		"<span class=\"icon glyphicon glyphicon-tasks\"></span> Exportar" +
		"</button>" +
		"<ul class=\"dropdown-menu \" role=\"menu\">" +
		"<li><a href=\"#\" onclick=\"blockUIForDownloadQuery('"+url+".json','" + params + "'\);\"> <img src=\"icons/json.png\" width=\"24px\"> JSON</a></li>" +
		"<li class=\"divider\"></li>" +
		"<li><a href=\"#\" onclick=\"blockUIForDownloadQuery('"+url+".xml', '" + params + "\');\"> <img src=\"icons/xml.png\" width=\"24px\"> XML</a></li>" +
		"<li class=\"divider\"></li>" +
		"<li><a href=\"#\" onclick=\"blockUIForDownloadQuery('"+url+".xlsx', '" + params + "\');\"> <img src=\"icons/xls.png\" width=\"24px\"> XLSX</a></li>" +
		"<li class=\"divider\"></li>" +
		"<li><a href=\"#\" onclick=\"blockUIForDownloadQuery('"+url+".pdf', '" + params + "\');\"> <img src=\"icons/pdf.png\" width=\"24px\"> PDF</a></li>" +
		"</ul>" +
		"</div>";
	};
	
	var fileDownloadCheckTimer;
	blockUIForDownloadQuery = function(path, params) {
		var token = new Date().getTime(); //use the current timestamp as the token value
		$.download(path,'fileDownloadToken=' + token + params, 'POST' );
		$.blockUI({ message: 'Exportando. Espere un Momento por favor...' });
		fileDownloadCheckTimer = window.setInterval(function () {
			var cookieValue = $.cookie('fileDownloadToken');
			if (cookieValue == token)
				finishDownload();
		}, 1000);
	};
	
});