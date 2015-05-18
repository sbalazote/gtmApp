/*
 * Translated default messages for the jQuery validation plugin and jQuery Bootgrid plugin.
 * Locale: ES
 */
(function ($) {
	"use strict";
	$.extend($.validator.messages, {
		required: "Este campo es obligatorio.",
		remote: "Por favor, rellena este campo.",
		email: "Por favor, escribe un email válido.",
		url: "Por favor, escribe una URL válida.",
		date: "Por favor, escribe una fecha válida.",
		dateITA: "Por favor, escribe una fecha válida (formato dd/mm/aaaa).",
		expirationDate: "Por favor, escribe una fecha de vencimiento válida.",
		dateISO: "Por favor, escribe una fecha (ISO) válida.",
		number: "Por favor, escribe un número entero válido.",
		digits: "Por favor, escribe sólo dígitos.",
		lettersonly: "Por favor, escribe sólo letras.",
		creditcard: "Por favor, escribe un número de tarjeta válido.",
		equalTo: "Por favor, escribe el mismo valor de nuevo.",
		accept: "Por favor, escribe un valor con una extensión aceptada.",
		maxlength: jQuery.validator.format("Por favor, no escribas más de {0} caracteres."),
		minlength: jQuery.validator.format("Por favor, no escribas menos de {0} caracteres."),
		rangelength: jQuery.validator.format("Por favor, escribe un valor entre {0} y {1} caracteres."),
		range: jQuery.validator.format("Por favor, escribe un valor entre {0} y {1}."),
		max: jQuery.validator.format("Por favor, escribe un valor menor o igual a {0}."),
		min: jQuery.validator.format("Por favor, escribe un valor mayor o igual a {0}."),
		nowhitespace: jQuery.validator.format("Por favor, sin espacios en blanco."),
		alphanumeric: jQuery.validator.format("Por favor, escribe solo letras, números y guiones."),
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