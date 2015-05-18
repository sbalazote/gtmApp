$(document).ready(function() {
	
	// My Global Functions
	
	myReload = function(type, message) {
		var postObject = {
			"type": type,
			"message": message
		};
		sessionStorage.setItem("postSaveMessage", JSON.stringify(postObject));
		window.location.reload();
	};
	
	myRedirect = function(type, message, redirectLocation) {
		var postObject = {
			"type": type,
			"message": message
		};
		sessionStorage.setItem("postSaveMessage", JSON.stringify(postObject));
		window.location.href = redirectLocation;
	};
	
	myShowAlert = function(type, message, element) {
		var myDiv = "alertDiv";
		if (element) {
			myDiv = element;
		}
		$('#'+myDiv).html(
			'<div class="alert alert-' + type + ' alert-block fade in">' +
			'<span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>' +
			'<button type="button" class="close" data-dismiss="alert">' +
			'&times;</button>' + message + '</div>');
	};
	
//	myShowAlert = function(type, message, element) {
//		var myDiv = "alertDiv";
//		if (element) {
//			myDiv = element;
//		}
//		var header = "success" == type ? "Enhorabuena!" : "Error!";
//		$('#'+myDiv).html(
//			'<div class="alert alert-' + type + ' alert-block fade in">' +
//			'<button type="button" class="close" data-dismiss="alert">' +
//			'&times;</button><strong>' + header + ' </strong>' + message + '</div>');
//	};
	
	// Alerts
	
	myGenericError = function(element) {
		var message = 'Ha ocurrido un error al intentar procesar su solicitud. Por favor, comun&uacute;quese con el Administrador del Sistema.';
		myShowAlert('danger', message, element);
		BootstrapDialog.show({
			type: BootstrapDialog.TYPE_DANGER,
	        title: 'Operaci&oacute;n Fallida',
	        message: message,
	        buttons: [{
                label: 'Cerrar',
                action: function(dialogItself){
                    dialogItself.close();
                }
            }]
		});
	};
	
	myDeleteError = function() {
		myShowAlert('danger', 'No es posible eliminar el elemento porque se encuentra utilizado. Si lo desea, puede desactivarlo.');
	};
	
	myExistentCodeError = function() {
		myShowAlert('danger', 'C&oacute;digo existente. Por favor, ingrese uno diferente.');
	};
	
	myUpdateSuccessful = function() {
		myShowAlert('success', 'Se ha registrado la modificaci&oacute;n correctamente.');
	};
	
	// Alerts que redirigen o recargan la paginas
	
	myCreateSuccessful = function() {
		myReload('success', 'Se ha registrado el alta correctamente.');
	};
	
	myDeleteRedirect = function(redirectLocation) {
		myRedirect('success', 'Se ha registrado la baja correctamente.', redirectLocation);
	};
	
	myNotFoundDeleteError = function(redirectLocation) {
		myRedirect('danger', 'El registro no existe o ya ha sido dado de baja.', redirectLocation);
	};
	
	myDeleteInputError = function() {
		myShowAlert('danger', 'No es posible anular el ingreso porque se registran movimientos para alguno de los productos relacionados.');
	};
	
	myQueryTooLargeAlert = function() {
		myShowAlert('danger', 'Consulta demasiado amplia. Por favor sea m&aacute;s especifico.');
	};
	
	// Chequea si hay alerts que mostrar
	var postSaveMessage = sessionStorage.getItem("postSaveMessage");
	if (postSaveMessage) {
		var jsonObject = JSON.parse(postSaveMessage);
		myShowAlert(jsonObject.type, jsonObject.message);
		BootstrapDialog.show({
			type: BootstrapDialog.TYPE_SUCCESS,
	        title: 'Operaci&oacute;n Exitosa',
	        message: jsonObject.message,
	        buttons: [{
                label: 'Cerrar',
                action: function(dialogItself){
                    dialogItself.close();
                }
            }]
		});
		sessionStorage.removeItem("postSaveMessage");
	}
	
});