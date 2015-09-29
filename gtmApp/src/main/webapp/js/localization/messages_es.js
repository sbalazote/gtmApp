/*
 * Translated default messages for the jQuery validation plugin and jQuery Bootgrid plugin.
 * Locale: ES
 */
(function ($) {
	"use strict";
	$.extend($.validator.messages, {
		required: "Este campo es obligatorio.",
		remote: "Por favor, rellena este campo.",
		email: "Por favor, escribe un email v\u00e1lido.",
		url: "Por favor, escribe una URL v\u00e1lida.",
		date: "Por favor, escribe una fecha v\u00e1lida.",
		dateITA: "Por favor, escribe una fecha v\u00e1lida (formato dd/mm/aaaa).",
		expirationDate: "Por favor, escribe una fecha de vencimiento v\u00e1lida.",
		dateISO: "Por favor, escribe una fecha (ISO) v\u00e1lida.",
		number: "Por favor, escribe un n\u00famero entero v\u00e1lido.",
		digits: "Por favor, escribe s\u00f3lo d\u00edgitos.",
		lettersonly: "Por favor, escribe s\u00f3lo letras.",
		creditcard: "Por favor, escribe un n\u00famero de tarjeta v\u00e1lido.",
		equalTo: "Por favor, escribe el mismo valor de nuevo.",
		accept: "Por favor, escribe un valor con una extensi\u00f3n aceptada.",
		maxlength: jQuery.validator.format("Por favor, no escribas m\u00e1s de {0} caracteres."),
		minlength: jQuery.validator.format("Por favor, no escribas menos de {0} caracteres."),
		rangelength: jQuery.validator.format("Por favor, escribe un valor entre {0} y {1} caracteres."),
		range: jQuery.validator.format("Por favor, escribe un valor entre {0} y {1}."),
		max: jQuery.validator.format("Por favor, escribe un valor menor o igual a {0}."),
		min: jQuery.validator.format("Por favor, escribe un valor mayor o igual a {0}."),
		nowhitespace: jQuery.validator.format("Por favor, sin espacios en blanco."),
		alphanumeric: jQuery.validator.format("Por favor, escribe solo letras, n\u00fameros y guiones."),
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