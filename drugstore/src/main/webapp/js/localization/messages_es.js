/*
 * Translated default messages for the jQuery validation plugin and jQuery Bootgrid plugin.
 * Locale: ES
 */
(function ($) {
	"use strict";
	$.extend($.validator.messages, {
		required: "Este campo es obligatorio.",
		remote: "Por favor, rellena este campo.",
		email: "Por favor, escribe un email v&aacute;lido.",
		url: "Por favor, escribe una URL v&aacute;lida.",
		date: "Por favor, escribe una fecha v&aacute;lida.",
		dateITA: "Por favor, escribe una fecha v&aacute;lida (formato dd/mm/aaaa).",
		expirationDate: "Por favor, escribe una fecha de vencimiento v&aacute;lida.",
		dateISO: "Por favor, escribe una fecha (ISO) v&aacute;lida.",
		number: "Por favor, escribe un n&uacute;mero entero v&aacute;lido.",
		digits: "Por favor, escribe s&oacute;lo d&iacute;gitos.",
		lettersonly: "Por favor, escribe s&oacute;lo letras.",
		creditcard: "Por favor, escribe un n&uacute;mero de tarjeta v&aacute;lido.",
		equalTo: "Por favor, escribe el mismo valor de nuevo.",
		accept: "Por favor, escribe un valor con una extensi&oacute;n aceptada.",
		maxlength: jQuery.validator.format("Por favor, no escribas m&aacute;s de {0} caracteres."),
		minlength: jQuery.validator.format("Por favor, no escribas menos de {0} caracteres."),
		rangelength: jQuery.validator.format("Por favor, escribe un valor entre {0} y {1} caracteres."),
		range: jQuery.validator.format("Por favor, escribe un valor entre {0} y {1}."),
		max: jQuery.validator.format("Por favor, escribe un valor menor o igual a {0}."),
		min: jQuery.validator.format("Por favor, escribe un valor mayor o igual a {0}."),
		nowhitespace: jQuery.validator.format("Por favor, sin espacios en blanco."),
		alphanumeric: jQuery.validator.format("Por favor, escribe solo letras, n&uacute;meros y guiones."),
		letterswithbasicpunc: jQuery.validator.format("Por favor, escribe solo letras, signos y espacios.")
	});

	$.extend($.fn.bootgrid.Constructor.defaults.labels, {
		all: "Todo",
		infos: "Mostrando del {{ctx.start}} al {{ctx.end}} de un total de {{ctx.total}} registros",
		loading: "Cargando...",
		noResults: "No se han encontrado resultados!",
		refresh: "Refrescar",
		search: "Buscar"
	});
})(jQuery);