$(document).ready(function() {

    var deleteDeliveryNoteEnumeratorId;

    $("input").blur(function() {
        if ($("#deliveryNotePOSInput").val() != "")
            $("#deliveryNotePOSInput").val(addLeadingZeros($("#deliveryNotePOSInput").val(), 4));
        if ($("#lastDeliveryNoteNumberInput").val() != "")
            $("#lastDeliveryNoteNumberInput").val(addLeadingZeros($("#lastDeliveryNoteNumberInput").val(), 8));
    });

    var resetForm = function() {
        $("#idInput").val('');
        $("#deliveryNotePOSInput").val('');
        $("#lastDeliveryNoteNumberInput").val('');
        $("#activeSelect").val($("#activeSelect option:first").val());
        $("#fakeSelect").val($("#activeSelect option:first").val());
    };

    var deleteAgent = function(deleteDeliveryNoteEnumeratorId) {
        $.ajax({
            url: "deleteDeliveryNoteEnumerator.do",
            type: "POST",
            data: {
                deleteDeliveryNoteEnumeratorId: deleteDeliveryNoteEnumeratorId
            },
            async: true,
            success: function(response) {
                if (response === true) {
                    myReload("success", "Se ha registrado la baja con identificador:  " + deleteDeliveryNoteEnumeratorId);
                } else {
                    myReload("danger", "El registro no existe o ya ha sido dado de baja.");
                }
                $("#deliveryNoteEnumeratorsTable").bootgrid("reload");
            },
            error: function(jqXHR, textStatus, errorThrown) {
                myDeleteError();
            }
        });
    };

    var readDeliveryNoteEnumerator = function(deleteDeliveryNoteEnumeratorId) {
        $.ajax({
            url: "readDeliveryNoteEnumerator.do",
            type: "GET",
            data: {
                deleteDeliveryNoteEnumeratorId: deleteDeliveryNoteEnumeratorId
            },
            async: false,
            success: function(response) {
                $("#idInput").val(response.id);
                $("#deliveryNotePOSInput").val(response.deliveryNotePOS);
                $("#lastDeliveryNoteNumberInput").val(response.lastDeliveryNoteNumber);
                var isActive = (response.active) ? "true" : "false";
                $("#activeSelect").val(isActive).trigger('chosen:update');
                var isFake = (response.fake) ? "true" : "false";
                $("#fakeSelect").val(isFake).trigger('chosen:update');
            },
            error: function(jqXHR, textStatus, errorThrown) {
                myDeleteError();
            }
        });
    };

    var toggleElements = function(hidden) {
        $("#deliveryNotePOSInput").attr('disabled', hidden);
        $("#lastDeliveryNoteNumberInput").attr('disabled', hidden);
        $("#activeSelect").prop('disabled', hidden).trigger('chosen:update');
        $("#fakeSelect").prop('disabled', hidden).trigger('chosen:update');
    };

    $("#addAgent").click(function() {
        resetForm();
        toggleElements(false);
        $('#addButton').show();
        $('#updateButton').hide();
        $('#addDeliveryNoteEnumeratorLabel').show();
        $('#readDeliveryNoteEnumeratorLabel').hide();
        $('#updateDeliveryNoteEnumeratorLabel').hide();
        $('#deliveryNoteEnumeratorModal').modal('show');
    });

    var deliveryNoteEnumeratorsTable = $("#deliveryNoteEnumeratorsTable").bootgrid({
        ajax: true,
        requestHandler: function (request) {
            return request;
        },
        url: "./getMatchedDeliveryNoteEnumerators.do",
        formatters: {
            "commands": function(column, row)
            {
                return "<button type=\"button\" class=\"btn btn-sm btn-default command-edit\" data-row-id=\"" + row.id + "\"><span class=\"glyphicon glyphicon-pencil\"></span></button> " +
                    "<button type=\"button\" class=\"btn btn-sm btn-default command-delete\" data-row-id=\"" + row.id + "\"><span class=\"glyphicon glyphicon-trash\"></span></button>" +
                    "<button type=\"button\" class=\"btn btn-sm btn-default command-view\" data-row-id=\"" + row.id + "\"><span class=\"glyphicon glyphicon-eye-open\"></span></button>";
            }
        }
    }).on("loaded.rs.jquery.bootgrid", function() {
        /* Executes after data is loaded and rendered */
        deliveryNoteEnumeratorsTable.find(".command-edit").on("click", function(e) {
            resetForm();
            toggleElements(false);
            readDeliveryNoteEnumerator($(this).data("row-id"));
            $('#addButton').hide();
            $('#updateButton').show();
            $('#addDeliveryNoteEnumeratorLabel').hide();
            $('#readDeliveryNoteEnumeratorLabel').hide();
            $('#updateDeliveryNoteEnumeratorLabel').show();
            $('#deliveryNoteEnumeratorModal').modal('show');
        }).end().find(".command-delete").on("click", function(e) {
            deleteDeliveryNoteEnumeratorId = $(this).data("row-id");
            $('#deleteConfirmationModal').modal('show');
        }).end().find(".command-view").on("click", function(e) {
            resetForm();
            toggleElements(true);
            readDeliveryNoteEnumerator($(this).data("row-id"));
            $('#addButton').hide();
            $('#updateButton').hide();
            $('#addDeliveryNoteEnumeratorLabel').hide();
            $('#readDeliveryNoteEnumeratorLabel').show();
            $('#updateDeliveryNoteEnumeratorLabel').hide();
            $('#deliveryNoteEnumeratorModal').modal('show');
        });
    });

    var exportHTML = exportTableHTML("./rest/agents");
    $(".search").before(exportHTML);

    $("#deleteEntityButton").click(function() {
        deleteAgent(deleteDeliveryNoteEnumeratorId);
    });
});