$(document).ready(function() {

    // disabled zoom in
    $(document).keydown(function(event) {
        if (event.ctrlKey==true && (event.which == '107' || event.which == '109')) {
            event.preventDefault();
        }
        
        if (event.which == '171' || event.which == '187') {
            event.preventDefault();
        }
        
        $(window).bind('mousewheel DOMMouseScroll', function(event)
        {
            if(event.ctrlKey == true)
            {
                event.preventDefault();
            }
        });
    });

    $(document).on('touchstart', function (evt) {
        evt.preventDefault();
    });

	// To highlight menu options
	
	var path = window.location.pathname.split("/");
	var href = path[path.length-1];
	$("ul.navbar-nav li a[href='"+href+"'] ").closest("li.activable").addClass("active");

    // Disable backspace to go back
    $(document).on("keydown", function (e) {
        if (e.which === 8 && !$(e.target).is("input, textarea")) {
            e.preventDefault();
        }
    });

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

	myParseDateShort = function(date) {
		var myDate = new Date(date);
		var day = ("0" + myDate.getDate()).slice(-2);
		var month = ("0" + (myDate.getMonth() + 1)).slice(-2);
		var year = ("0"+myDate.getFullYear()).slice(-2);
		return day + month + year;
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
    }, "Por favor, escribe una fecha mayor a la fecha del d\u00eda. (Formato: ddmmaa)");

	jQuery.validator.addMethod("formatDate", function(value, element) {
		var check = false;
        var re = re = /^\d{1,2}\/\d{1,2}\/\d{4}$/;
        if (re.test(value)) {
			check = true;
		}
		return this.optional(element) || check;
	}, "Por favor, escribe una fecha con Formato: dd/mm/aaaa");

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
    
    jQuery.validator.addMethod("priceWithCents", function (value, element) {
        return this.optional(element) || /^\d{1,7}\,\d{2}$/i.test(value);
    }, jQuery.format("El Precio debe ser del formato xxxxxxx,xx."));

	jQuery.validator.addMethod("stringWithSpecialChars", function (value, element) {
		var check = false;
		var re = /^[a-zA-Z\u00C0-\u00ff]+$/;
		if (re.test(value)) {
			check = true;
		}
		return this.optional(element) || check;
	}, jQuery.format("Solo se permiten letras"));

	$.validator.addMethod("maxCurrentDate", function(value, element) {
		var curDate = new Date();
		var partsFrom = value.split("/");
		var currentDateOtherFormat = partsFrom[1] + "/" + partsFrom[0] + "/"+ partsFrom[2];
		var inputDate = new Date(currentDateOtherFormat);
		inputDate.setHours(0,0,0,0);
		curDate.setHours(0,0,0,0);
		if (inputDate <= curDate)
			return true;
		return false;
	}, "La fecha debe ser menor o igual a hoy.");

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
	},"Por favor, ingrese un n\u00famero mayor a cero");
	
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
		$.removeCookie('fileDownloadToken'/*, { path: '/gtm-app/' }*/); //clears this cookie value
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
		"<li><a href=\"#\" onclick=\"blockUIForDownloadQuery('"+url+".json','" + params + "\');\"> <img src=\"icons/json.png\" width=\"24px\"> JSON</a></li>" +
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

	var fileDownloadCheckTimer;
	generateInputPDFReport = function(inputId, isUpdate, isSerializedReturn) {
		var token = new Date().getTime(); //use the current timestamp as the token value
		$.download('./rest/inputs.pdf', 'fileDownloadToken=' + token + '&dateFrom=&id=' + inputId + '&dateTo=&conceptId=null&providerId=null&deliveryLocationId=null&agreementId=null&deliveryNoteNumber=&purchaseOrderNumber=&cancelled=null&productId=null&productMonodrugId=null', 'POST');
		$.blockUI({message: 'Generando Reporte de Ingreso. Espere un Momento por favor...'});
		fileDownloadCheckTimer = window.setInterval(function () {
			var cookieValue = $.cookie('fileDownloadToken');
			if (cookieValue == token) {
				finishDownload();
                if (typeof(isSerializedReturn) === 'undefined') {
                    if (isUpdate == true) {
                        myRedirect("success", "Se ha autorizado el Ingreso de mercader\u00eda n\u00famero: " + inputId, "searchInputToUpdate.do");
                    } else {
                        myRedirect("success", "Se ha registrado el Ingreso de mercader\u00eda n\u00famero: " + inputId, "input.do");
                    }
                }
			}
		}, 1000);
	};

	var fileDownloadCheckTimer;
	generateSupplyingPDFReport = function(response) {
		var token = new Date().getTime(); //use the current timestamp as the token value
		$.download('./rest/supplyings.pdf', 'fileDownloadToken=' + token + '&dateFrom=&id=' + response.operationId + '&dateTo=&affiliateId=null&clientId=null&agreementId=null&productId=null&cancelled=null', 'POST');
		$.blockUI({message: 'Generando Reporte de Dispensa. Espere un Momento por favor...'});
		fileDownloadCheckTimer = window.setInterval(function () {
			var cookieValue = $.cookie('fileDownloadToken');
			if (cookieValue == token) {
				finishDownload();
				var msgType = "success";
				var message = "Se ha registrado la Dispensa de mercader\u00eda n\u00famero: " + response.operationId;
				if (response.errorMessages.length > 0) {
					$.each(response.errorMessages, function (index, value) {
						message += "<strong><p>" + value + "</p></strong>";
					});
					msgType = "warning";
				}
				if (response.successMessages.length > 0) {
					$.each(response.successMessages, function (index, value) {
						message += "<strong><p>" + value + "</p></strong>";
					});
				}
				myRedirect(msgType, message, "supplying.do");
			}
		}, 1000);
	};

	var fileDownloadCheckTimer;
	generateOutputPDFReport = function(response, isSerializedReturn) {
		var token = new Date().getTime(); //use the current timestamp as the token value
		$.download('./rest/outputs.pdf', 'fileDownloadToken=' + token + '&dateFrom=&id=' + response.operationId + '&dateTo=&conceptId=null&providerId=null&deliveryLocationId=null&agreementId=null&productId=null&cancelled=null', 'POST');
		$.blockUI({message: 'Generando Reporte de Egreso. Espere un Momento por favor...'});
		fileDownloadCheckTimer = window.setInterval(function () {
			var cookieValue = $.cookie('fileDownloadToken');
			if (cookieValue == token) {
				finishDownload();
                if (typeof(isSerializedReturn) === 'undefined') {
                    var msgType = "success";
                    var message = "Se ha registrado el Egreso de mercader\u00eda n\u00famero: " + response.operationId;
                    if (response.errorMessages.length > 0) {
                        $.each(response.errorMessages, function (index, value) {
                            message += "<strong><p>" + value + "</p></strong>";
                        });
                        msgType = "warning";
                    }
                    if (response.successMessages.length > 0) {
                        $.each(response.successMessages, function (index, value) {
                            message += "<strong><p>" + value + "</p></strong>";
                        });
                    }
                    myRedirect(msgType, message, "output.do");
                }
			}
		}, 1000);
	};

	var fileDownloadCheckTimer;
	generateProvisioningRequestPDFReport = function(provisioningRequestId) {
		var token = new Date().getTime(); //use the current timestamp as the token value
		$.download('./rest/provisionings.pdf', 'fileDownloadToken=' + token + '&dateFrom=&id=' + provisioningRequestId + '&dateTo=&clientId=null&affiliateId=null&agreementId=null&comment=&deliveryLocation=null&logisticsOperator=null&stateId=null&productId=null&productMonodrugId=null', 'POST');
		$.blockUI({message: 'Generando Reporte de Sol. de Abast. Espere un Momento por favor...'});
		fileDownloadCheckTimer = window.setInterval(function () {
			var cookieValue = $.cookie('fileDownloadToken');
			if (cookieValue == token) {
				finishDownload();
				myRedirect("success", "Se ha registrado el Pedido n\u00famero: " + provisioningRequestId, "provisioningRequest.do");
			}
		}, 1000);
	};

	var fileDownloadCheckTimer;
	generatePickingSheetPDF = function(provisioningRequestIds) {
		var token = new Date().getTime(); //use the current timestamp as the token value
		$.download('./rest/pickingSheets.pdf', 'fileDownloadToken=' + token + '&provisioningIds=' + provisioningRequestIds, 'POST');
		$.blockUI({message: 'Generando hojas de Picking. Espere un Momento por favor...'});
		fileDownloadCheckTimer = window.setInterval(function () {
			var cookieValue = $.cookie('fileDownloadToken');
			if (cookieValue == token) {
				finishDownload();
				myRedirect("success", "Se han generado las hojas de Picking para los pedidos n\u00famero: " + provisioningRequestIds, "pickingSheet.do");
			}
		}, 1000);
	};

    var fileDownloadCheckTimer;
    generateSerializedReturnPDFReport = function(inputId, outputId) {
        var token = new Date().getTime(); //use the current timestamp as the token value
        $.download('./rest/serializedReturns.pdf', 'fileDownloadToken=' + token + '&inputId=' + inputId + '&outputId=' + outputId, 'POST');
        $.blockUI({message: 'Generando Reporte de Devolucion de Series. Espere un Momento por favor...'});
        fileDownloadCheckTimer = window.setInterval(function () {
            var cookieValue = $.cookie('fileDownloadToken');
            if (cookieValue == token) {
                finishDownload();
                if (outputId != "") {
                    myRedirect("success", "Se ha registrado la devoluci\u00f3n de serie con n\u00famero: " + inputId + " y destrucci\u00f3n de serie con n\u00famero: " + outputId, "serializedReturns.do");
                } else {
                    myRedirect("success", "Se ha registrado la devoluci\u00f3n de serie con n\u00famero: " + inputId, "serializedReturns.do");
                }
            }
        }, 1000);
    };


	getURLParameter = function(sParam) {
		var sPageURL = window.location.search.substring(1);
		var sURLVariables = sPageURL.split('&');
		for (var i = 0; i < sURLVariables.length; i++)
		{
			var sParameterName = sURLVariables[i].split('=');
			if (sParameterName[0] == sParam)
			{
				return sParameterName[1];
			}
		}
	}
});