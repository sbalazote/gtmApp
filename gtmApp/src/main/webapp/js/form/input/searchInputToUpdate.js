var SearchInputToUpdate = function() {

    $("#mainTable").bootgrid({
        formatters: {
            "option": function (column, row) {
                return "<a type='button' class='btn btn-sm btn-default a-select' href='#'><span class='glyphicon glyphicon-check'></span> Seleccionar</a>";
            },
            "view": function (column, row) {
                return "<a type='button' class='btn btn-sm btn-default view-row' href='#'><span class='glyphicon glyphicon-eye-open'></span> Detalle</a>";
            }
        }
    });
    $('#inputTableBody').on("click", ".view-row", function () {
        var parent = $(this).parent().parent();
        var inputId = parent.attr("data-row-id");

        showInputModal(inputId);
    });

    $("#inputTableBody").on("click", ".a-select", function () {
        var parent = $(this).parent().parent();
        var inputId = parent.attr("data-row-id");

        $("#id").val(inputId);
        $("#myForm").submit();
    });
};