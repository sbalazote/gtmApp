/**
 * Select2 Spanish translation
 */
(function ($) {
    "use strict";

    $.fn.select2.locales['es'] = {
    	formatMatches: function (matches) { if (matches === 1) { return "Un resultado disponible, presione enter para seleccionarlo."; } return matches + " resultados disponibles, use las teclas de direcci&oacute;n para navegar."; },
        formatNoMatches: function () { return "No se encontraron resultados"; },
        formatInputTooShort: function (input, min) { var n = min - input.length; return "Por favor, introduzca " + n + " car" + (n == 1? "&aacute;cter" : "acteres"); },
        formatInputTooLong: function (input, max) { var n = input.length - max; return "Por favor, elimine " + n + " car" + (n == 1? "&aacute;cter" : "acteres"); },
        formatSelectionTooBig: function (limit) { return "S&oacute;lo puede seleccionar " + limit + " elemento" + (limit == 1 ? "" : "s"); },
        formatLoadMore: function (pageNumber) { return "Cargando m&aacute;s resultados"; },
        formatSearching: function () { return "Buscando"; },
        formatAjaxError: function() { return "La carga fall&oacute;"; }
    };

    $.extend($.fn.select2.defaults, $.fn.select2.locales['es']);
})(jQuery);
