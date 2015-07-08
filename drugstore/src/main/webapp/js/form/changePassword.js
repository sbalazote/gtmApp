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
                password: {
                    required: true,
                    minlength: 5,
                    maxlength: 15,
                },
                passwordCheck: {
                    required: true, equalTo: "#passwordInput",
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
                    "newPassword" : $("#passwordInput").val(),
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