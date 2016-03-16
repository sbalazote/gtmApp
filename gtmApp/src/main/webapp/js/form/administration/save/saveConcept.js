SaveConcept = function() {

    var events = [];

    var setEvents = function(value) {
        events = value || [];
    };

    var validateForm = function () {
        var form = $("#conceptAdministrationForm");
        form.validate({
            rules: {
                code: {
                    required: true,
                    digits: true,
                    maxlength: 9
                },
                description: {
                    required: true,
                    maxlength: 45
                },
                deliveryNoteEnumerator: {
                    required: true
                },
                input: {
                    required: true
                },
                printDeliveryNote: {
                    required: true
                },
                refund: {
                    required: true
                },
                informAnmat: {
                    required: true
                },
                deliveryNotesCopies: {
                    required: true,
                    digits: true,
                    maxlength: 9
                },
                active: {
                    required: true
                },
                events: {
                    required: true
                },
                client: {
                    required: true
                }
            },
            showErrors: myShowErrors,
            onsubmit: false
        });
        return form.valid();
    };

    var existsConcept = function () {
        var exists = false;
        $.ajax({
            url: "existsConcept.do",
            type: "GET",
            async: false,
            data: {
                code: $("#codeInput").val()
            },
            success: function (response) {
                exists = response;
            },
            error: function (jqXHR, textStatus, errorThrown) {
                myGenericError();
            }
        });
        return exists;
    };

    $('#conceptModal').on('shown.bs.modal', function () {
        // si es un egreso se bloquea el '¿Es devol. cliente?'
        var refundSelectDisabled = $("#inputSelect").val() === "false";
        $("#refundSelect").prop('disabled', refundSelectDisabled).trigger('chosen:update');
        $('.chosen-select', this).chosen('destroy').chosen();
    });

    //$('#my-select').multiSelect();

    $('#my-select').multiSelect({
        afterSelect: function(value, text){
            if(value != null) {
                events.push(value[0]);
            }
            events = _.uniq(events);
        },
        afterDeselect: function(value, text){
            if(value != null){
                events.splice(events.indexOf(value[0]),1);
            }
            events = _.uniq(events);
        }
    });

    $("#addButton, #updateButton").click(function (e) {
        if (validateForm()) {
            var jsonConcept = {
                "id": $("#idInput").val(),
                "code": $("#codeInput").val(),
                "description": $("#descriptionInput").val(),
                "deliveryNoteEnumeratorId": $("#deliveryNoteEnumeratorSelect").val(),
                "input": $("#inputSelect").val(),
                "printDeliveryNote": $("#printDeliveryNoteSelect").val(),
                "refund": $("#refundSelect").val(),
                "informAnmat": $("#informAnmatSelect").val(),
                "deliveryNotesCopies": $("#deliveryNotesCopiesInput").val(),
                "active": $("#activeSelect option:selected").val(),
                "client": $("#clientSelect option:selected").val(),
                "destruction": $("#destructionSelect option:selected").val(),
                "events": events ? _.uniq(events) : []
            };

            //	si existe el codigo y ademas no se trata de una actualizacion, lanzo modal.
            if (existsConcept() && (e.currentTarget.id === 'addButton')) {
                myShowAlert('danger', 'C\u00f3digo existente. Por favor, ingrese uno diferente.', 'conceptModalAlertDiv');
            } else {
                $.ajax({
                    url: "saveConcept.do",
                    type: "POST",
                    contentType: "application/json",
                    data: JSON.stringify(jsonConcept),
                    async: true,
                    success: function (response) {
                        if (response.id === parseInt($("#idInput").val())) {
                            myUpdateSuccessful();
                        } else {
                            myCreateSuccessful();
                        }
                        $('#conceptModal').modal('hide');
                        $("#conceptsTable").bootgrid("reload");
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        myGenericError();
                    }
                });
            }
        }
    });

    $(".alert .close").on('click', function (e) {
        $(this).parent().hide();
    });

    return {
        setEvents: setEvents
    };
};