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
        $('#clientSearch').val('').trigger('chosen:updated');
        $('#affiliateSearch').val('').trigger('chosen:updated');
        $('#agreementSearch').val('').trigger('chosen:updated');
    });

    $("#searchButton").click(function() {
        if(validateForm()){
            var jsonSupplyingSearch = {
                "id": $("#idSearch").val().trim() || null,
                "dateFrom": $("#dateFromSearch").val(),
                "dateTo": $("#dateToSearch").val(),
                "clientId": $("#clientSearch").val() || null,
                "affiliateId": $("#affiliateSearch").val() || null,
                "agreementId": $("#agreementSearch").val() || null,
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
                    '&clientId=' + jsonSupplyingSearch.clientId +
                    '&affiliateId=' + jsonSupplyingSearch.affiliateId +
                    '&agreementId=' + jsonSupplyingSearch.agreementId;

                var exportHTML = exportQueryTableHTML("./rest/supplyings", params);
                var searchHTML = $(".search");

                if (searchHTML.prev().length == 0) {
                    $(".search").before(exportHTML);
                } else {
                    $(".search").prev().html(exportHTML);
                }
            },
            error: function(jqXHR, textStatus, errorThrown) {
                myGenericError();
            }
        });
    };

};