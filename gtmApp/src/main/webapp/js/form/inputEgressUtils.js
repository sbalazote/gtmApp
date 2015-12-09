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
