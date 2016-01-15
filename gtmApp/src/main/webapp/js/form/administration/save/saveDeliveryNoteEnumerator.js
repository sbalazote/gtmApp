SaveDeliveryNoteEnumerator = function() {

    var validateForm = function() {
        var form = $("#deliveryNoteEnumeratorAdministrationForm");
        form.validate({
            rules: {
                deliveryNotePOS: {
                    required: true,
                    digits: true,
                    maxlength: 9,
                },
                lastDeliveryNoteNumber: {
                    required: true,
                    digits: true,
                    maxlength: 9,
                },
                active: {
                    required: true
                },
                fake: {
                    required: true
                }
            },
            showErrors: myShowErrors,
            onsubmit: false
        });
        return form.valid();
    };

    $('#deliveryNoteEnumeratorModal').on('shown.bs.modal', function () {
        $('.chosen-select', this).chosen('destroy').chosen();
    });

    var existsDeliveryNoteEnumerator = function() {
        var exists = false;
        $.ajax({
            url: "existsDeliveryNoteEnumerator.do",
            type: "GET",
            async: false,
            data: {
                deliveryNotePOS: $("#deliveryNotePOSInput").val(),
                fake: $("#fakeSelect option:selected").val()
            },
            success: function(response) {
                exists = response;
            },
            error: function(jqXHR, textStatus, errorThrown) {
                myGenericError();
            }
        });
        return exists;
    };

    var checkNewDeliveryNoteNumber = function() {
        var exists = false;
        $.ajax({
            url: "checkNewDeliveryNoteNumber.do",
            type: "GET",
            async: false,
            data: {
                deliveryNotePOS: $("#deliveryNotePOSInput").val(),
                lastDeliveryNoteNumberInput: $("#lastDeliveryNoteNumberInput").val()
            },
            success: function(response) {
                exists = response;
            },
            error: function(jqXHR, textStatus, errorThrown) {
                myGenericError();
            }
        });
        return exists;
    };

    $("#addButton, #updateButton").click(function(e) {
        if (validateForm()) {
            var jsonDeliveryNoteEnumerator = {
                "id": $("#idInput").val(),
                "deliveryNotePOS": $("#deliveryNotePOSInput").val(),
                "lastDeliveryNoteNumber": $("#lastDeliveryNoteNumberInput").val(),
                "active": $("#activeSelect option:selected").val(),
                "fake": $("#fakeSelect option:selected").val()
            };

            //	si existe el codigo y ademas no se trata de una actualizacion, lanzo modal.
            if (existsDeliveryNoteEnumerator() && (e.currentTarget.id === 'addButton')) {
                myExistentCodeError();
            } else {
                if(checkNewDeliveryNoteNumber()){
                    $.ajax({
                        url: "saveDeliveryNoteEnumerator.do",
                        type: "POST",
                        contentType:"application/json",
                        data: JSON.stringify(jsonDeliveryNoteEnumerator),
                        async: true,
                        success: function(response) {
                            if (response.id === parseInt($("#idInput").val())) {
                                myUpdateSuccessful();
                            } else {
                                myCreateSuccessful();
                            }
                            $('#deliveryNoteEnumeratorModal').modal('hide');
                            $("#deliveryNoteEnumeratorsTable").bootgrid("reload");
                        },
                        error: function(jqXHR, textStatus, errorThrown) {
                            myGenericError();
                        }
                    });
                }else{
                    myShowAlert('danger', 'El numero de remito no puede ser menor o igual a los existentes');
                }
            }
        }
    });

    $(".alert .close").on('click', function(e) {
        $(this).parent().hide();
    });

    $("#deliveryNoteEnumeratorAdministrationForm input, #deliveryNoteEnumeratorAdministrationForm select").keypress(function(event) {
        return event.keyCode != 13;
    });

};