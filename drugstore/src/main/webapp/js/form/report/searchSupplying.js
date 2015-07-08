SearchSupplying = function() {

    $('#dateFromButton').click(function() {
        var maxDate = $( "#dateToSearch" ).datepicker( "getDate" );
        $("#dateFromSearch").datepicker("option", "maxDate", maxDate);
        $("#dateFromSearch").datepicker().focus();
    });

    $('#dateToButton').click(function() {
        var minDate = $( "#dateFromSearch" ).datepicker( "getDate" );
        $("#dateToSearch").datepicker("option", "minDate", minDate);
        $("#dateToSearch").datepicker().focus();
    });

    $("#dateFromSearch").datepicker();
    $("#dateToSearch").datepicker();

    $("#productInput").autocomplete({
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
            $("#productInput").attr('productId', ui.item.id);
            $("#productInput").val(ui.item.value);
            return false;
        },
        minLength: 3,
        autoFocus: true
    });

    var supplyingId = null;

    $('#supplyingTableBody').on("click", ".view-row", function() {
        var parent = $(this).parent().parent();
        supplyingId = parent.find("td:first-child").html();

        showSupplyingModal(supplyingId);
    });

    var validateForm = function() {
        var form = $("#searchSupplyingForm");
        form.validate({
            rules: {
                idSearch: {
                    digits: true
                },
                dateFromSearch: {
                    dateITA: true,
                    maxDate: $("#dateToSearch")
                },
                dateToSearch: {
                    dateITA: true,
                    minDate: $("#dateFromSearch")
                }
            },
            showErrors: myShowErrors,
            onsubmit: false
        });
        return form.valid();
    };

    $("#cleanButton").click(function() {
        if($("#dateFromSearch").val()!= ""){
            $.datepicker._clearDate('#dateFromSearch');
        }
        if($("#dateToSearch").val()!= ""){
            $.datepicker._clearDate('#dateToSearch');
        }
        $("#idSearch").val('');
        $("#clientSearch").val('').trigger('chosen:updated');
        $("#affiliateInput").select2("val", "");
        $('#agreementSearch').val('').trigger('chosen:updated');
        $('#cancelledSelect').val('').trigger('chosen:updated');
        $("#productInput").removeAttr("productId");
        $("#productInput").val('');
        $("#supplyingTable").bootgrid().bootgrid("clear");
        $('#divTable').hide();
    });

    $("#searchButton").click(function() {
        if(validateForm()){
            var jsonSupplyingSearch = {
                "id": $("#idSearch").val().trim() || null,
                "dateFrom": $("#dateFromSearch").val(),
                "dateTo": $("#dateToSearch").val(),
                "affiliateId": $("#affiliateInput").val() || null,
                "clientId": $("#clientSearch").val() || null,
                "agreementId": $("#agreementSearch").val() || null,
                "productId": $("#productInput").attr("productId") || null,
                "cancelled": $("#cancelledSelect").val() || null
            };

            $.ajax({
                url: "getCountSupplyingSearch.do",
                type: "POST",
                contentType:"application/json",
                async: false,
                data: JSON.stringify(jsonSupplyingSearch),
                success: function(response) {
                    if(response == true){
                        makeQuery(jsonSupplyingSearch);
                    }else{
                        myQueryTooLargeAlert();
                    }
                },
                error: function(jqXHR, textStatus, errorThrown) {
                    myGenericError();
                }
            });

        }
    });

    var makeQuery = function(jsonSupplyingSearch) {
        $.ajax({
            url: "getSupplyingForSearch.do",
            type: "POST",
            contentType:"application/json",
            async: false,
            data: JSON.stringify(jsonSupplyingSearch),
            success: function(response) {
                var aaData = [];
                for (var i = 0, l = response.length; i < l; ++i) {
                    var supplying = {
                        id: 0,
                        agreement: "",
                        client: "",
                        date: "",
                        cancelled: "",
                        option: ""
                    };
                    supplying.id = response[i].id;
                    supplying.agreement = response[i].agreement.description;
                    supplying.client = response[i].client.corporateName;
                    supplying.date = myParseDate(response[i].date);
                    if(response[i].cancelled){
                        supplying.cancelled = "Si";
                    }else{
                        supplying.cancelled = "No";
                    }
                    supplying.option = "<a href='javascript:void(0);' class='view-row'>Consulta</a>";
                    aaData.push(supplying);
                }
                $("#supplyingTable").bootgrid({
                    caseSensitive: false
                });
                $("#supplyingTable").bootgrid().bootgrid("clear");
                $("#supplyingTable").bootgrid().bootgrid("append", aaData);
                $("#supplyingTable").bootgrid().bootgrid("search", $(".search-field").val());

                var params = '&dateFrom=' + jsonSupplyingSearch.dateFrom +
                    '&id=' + jsonSupplyingSearch.id +
                    '&dateTo=' + jsonSupplyingSearch.dateTo +
                    '&affiliateId=' + jsonSupplyingSearch.affiliateId +
                    '&agreementId=' + jsonSupplyingSearch.agreementId +
                    '&cancelled=' + jsonSupplyingSearch.cancelled;

                var exportHTML = exportQueryTableHTML("./rest/supplyings", params);
                var searchHTML = $("#divTable .search");

                if (searchHTML.prev().length == 0) {
                    searchHTML.before(exportHTML);
                } else {
                    searchHTML.prev().html(exportHTML);
                }
            },
            error: function(jqXHR, textStatus, errorThrown) {
                myGenericError();
            }
        });
    };


    $("#affiliateInput").select2({
        //allowClear: true,
        placeholder: "Buscar afiliado...",
        minimumInputLength: 3,
        initSelection : function (element, callback) {
            var data = {
                code: element.attr("code"),
                name: element.attr("name"),
                surname: element.attr("surname")
            };
            callback(data);
        },
        ajax: {
            url: "getAffiliates.do",
            dataType: 'json',
            quietMillis: 250,
            data: function (term, page) { // page is the one-based page number tracked by Select2
                return {
                    term: term, //search term
                    pageNumber: page, // page number
                    pageSize: 10, // page number
                    active: true
                };
            },
            results: function (data, page, query) {
                var more = (data.length == 10); // whether or not there are more results available

                var parsedResults = [];

                $.each(data, function(index, value) {
                    parsedResults.push({
                        id: value.id,
                        text:  value.code + "-" + value.name + " " + value.surname
                    });
                });

                // notice we return the value of more so Select2 knows if more results can be loaded
                return { results: parsedResults, more: more };
            }
        },
        formatResult: function(data) {
            return "<div class='select2-user-result'>" + data.text + "</div>";
        },
        formatSelection: function(data) {
            return data.text;
        }
    });

    $("#affiliateInput").select2("enable", true);
};