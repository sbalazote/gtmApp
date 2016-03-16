SaveProfile = function() {

    var validateForm = function() {
        var form = $("#profileAdministrationForm");
        form.validate({
            rules: {
                description: {
                    required: true,
                    maxlength: 45
                }
            },
            showErrors: myShowErrors,
            onsubmit: false
        });
        return form.valid();
    };

    var existsProfile = function() {
        var exists = false;
        $.ajax({
            url: "existsProfile.do",
            type: "GET",
            async: false,
            data: {
                description: $("#descriptionInput").val()
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

    $('#profileModal').on('shown.bs.modal', function () {
        $('.chosen-select', this).chosen('destroy').chosen();
    });

    $('#my-select').multiSelect();

    $("#addButton, #updateButton").click(function(e) {
        if (validateForm()) {
            var jsonProfile = {
                "id": $("#idInput").val(),
                "description": $("#descriptionInput").val(),
                "roles": $("#my-select").val() || []
            };

            //	si existe el codigo y ademas no se trata de una actualizacion, lanzo modal.
            if (existsProfile() && (e.currentTarget.id === 'addButton')) {
                myShowAlert('danger', 'Perfil existente. Por favor, ingrese uno diferente.', 'profileModalAlertDiv');
            } else {
                $.ajax({
                    url: "saveProfile.do",
                    type: "POST",
                    contentType:"application/json",
                    data: JSON.stringify(jsonProfile),
                    async: true,
                    success: function(response) {
                        if (response.id === parseInt($("#idInput").val())) {
                            myUpdateSuccessful();
                        } else {
                            myCreateSuccessful();
                        }
                        $('#profileModal').modal('hide');
                        $("#profilesTable").bootgrid("reload");
                    },
                    error: function(jqXHR, textStatus, errorThrown) {
                        myGenericError();
                    }
                });
            }
        }
    });

    $(".alert .close").on('click', function(e) {
        $(this).parent().hide();
    });

    $("#profileAdministrationForm input, #profileAdministrationForm select").keypress(function(event) {
        return event.keyCode != 13;
    });

};