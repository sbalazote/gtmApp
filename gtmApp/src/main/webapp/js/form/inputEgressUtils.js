ProductAutocomplete = function(name) {
    $("#" + name).autocomplete({
        source: function(request, response) {
            $.ajax({
                url: "getProducts.do",
                type: "GET",
                async: false,
                data: {
                    term: request.term,
                    active: true
                },
                success: function(data) {
                    var array = $.map(data, function(item) {
                        return {
                            id:	item.id,
                            label: item.code + " - " + item.description + " - " + item.brand.description + " - " + item.monodrug.description,
                            value: item.code + " - " + item.description,
                            gtin: item.lastGtin,
                            type: item.type
                        };
                    });
                    response(array);
                },
                error: function(jqXHR, textStatus, errorThrown) {
                    myGenericError();
                }
            });
        },
        select: function(event, ui) {
            $("#" + name).attr('productId', ui.item.id);
            $("#" + name).val(ui.item.value);
            return false;
        },
        minLength: 3,
        autoFocus: true
    });

};

AddAffiliate = function() {
    $("#addAffiliateModalAcceptButton").click(function() {
        if (validateAddAffiliateModalForm()) {
            var clients =  [];
            clients.push(parseInt($("#clientInput").val()));
            var jsonAffiliate = {
                "code" : $("#affiliateCodeInput").val(),
                "name" : $("#affiliateNameInput").val(),
                "surname" : $("#affiliateSurnameInput").val(),
                "documentType" : $("#affiliateDocumentTypeSelect option:selected").val() || null,
                "document" : $("#affiliateDocumentInput").val() || null,
                "clients": clients,
                "active" : true
            };
            if (existsAffiliate()) {
                $("#addAffiliateModal").modal('hide');
                $("#addExistAffiliateConfirmationModal").modal('show');
            } else {
                $.ajax({
                    url : "saveAffiliate.do",
                    type : "POST",
                    contentType : "application/json",
                    data : JSON.stringify(jsonAffiliate),
                    async : true,
                    success : function(response) {
                        $("#addAffiliateModal").modal('hide');
                        $('#affiliateInput').select2("data",
                            {
                                id : response.id,
                                text : response.code
                                + ' - '
                                + response.surname
                                + ' '
                                + response.name
                            });
                        $("#agreementInput").trigger('chosen:activate');
                    },
                    error : function(jqXHR, textStatus,
                                     errorThrown) {
                        myGenericError("addAffiliateModalAlertDiv");
                    }
                });
            }
        }
    });
    var validateAddAffiliateModalForm = function() {
        var form = $("#addAffiliateModalForm");
        addAffiliateModalFormValidator = form.validate({
            rules: {
                affiliateCode: {
                    required: true,
                    digits: true
                },
                affiliateSurname: {
                    required: true,
                    maxlength: 45,
                    letterswithbasicpunc: true
                },
                affiliateName: {
                    required: true,
                    maxlength: 45,
                    letterswithbasicpunc: true
                },
                affiliateDocument: {
                    digits: true
                }
            },
            showErrors: myShowErrors,
            onsubmit: false
        });
        return form.valid();
    };

    var existsAffiliate = function() {
        var exists = false;
        $.ajax({
            url : "existsAffiliate.do",
            type : "GET",
            async : false,
            data : {
                code : $("#affiliateCodeInput").val()
            },
            success : function(response) {
                exists = response;
            },
            error : function(jqXHR, textStatus, errorThrown) {
                myGenericError();
            }
        });
        return exists;
    };

    $("#addAffiliateToClient").click(function() {
        $.ajax({
            url : "addAffiliateToClient.do",
            type : "POST",
            data : {
                "code" : $("#affiliateCodeInput").val(),
                "clientId" : parseInt($("#clientInput").val())
            },
            async : true,
            success : function(response) {
                $("#addExistAffiliateConfirmationModal").modal('hide');
                $('#affiliateInput').select2("data",
                    {
                        id : response.id,
                        text : response.code
                        + ' - '
                        + response.surname
                        + ' '
                        + response.name
                    });
                $("#agreementInput").trigger('chosen:activate');
            },
            error : function(jqXHR, textStatus,
                             errorThrown) {
                myGenericError("addAffiliateModalAlertDiv");
            }
        });
    });
};