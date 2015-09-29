ChangePassword = function() {

    $("#changePassword").click(function() {
        $('#changePasswordModal').modal('show');
    });

    var validateForm = function() {
        var form = $("#changePasswordForm");
        form.validate({
            rules: {
                actualPassword:{
                    required: true
                },
                passwordToChange: {
                    required: true,
                    minlength: 5,
                    maxlength: 15,
                },
                passwordToChangeCheck: {
                    required: true, equalTo: "#passwordToChangeInput",
                    minlength: 5,
                    maxlength: 15,
                },
            },
            showErrors: myShowErrors,
            onsubmit: false
        });
        return form.valid();
    };

    $("#confirmChangePassword").click(function() {
        if (validateForm()) {
                var jsonNewPassword = {
                    "actualPassword" : $("#actualPasswordInput").val(),
                    "newPassword" : $("#passwordToChangeInput").val(),
                };
                $.ajax({
                    url : "changePassword.do",
                    type : "POST",
                    contentType : "application/json",
                    data : JSON.stringify(jsonNewPassword),
                    async : true,
                    success : function(response) {
                        if(response == true){
                            $('#changePasswordModal').modal('hide');
                        }else{
                            myShowAlert("danger", "No se pudo realizar el cambio, verifique.", "changePasswordModalAlertDiv");
                        }
                    },
                    error : function(jqXHR, textStatus,
                                     errorThrown) {
                        myGenericError("changePasswordModalAlertDiv");
                    }
                });
        }
    });
};