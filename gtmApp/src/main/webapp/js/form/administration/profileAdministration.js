$(document).ready(function() {

    var profileId;

    var resetForm = function() {
        $("#idInput").val('');
        $("#descriptionInput").val('');
        $('#my-select').multiSelect('deselect_all');
    };

    var deleteProfile = function(profileId) {
        $.ajax({
            url: "deleteProfile.do",
            type: "POST",
            data: {
                profileId: profileId
            },
            async: true,
            success: function(response) {
                if (response === true) {
                    myReload("success", "Se ha registrado la baja con identificador:  " + profileId);
                } else {
                    myReload("danger", "El registro no existe o ya ha sido dado de baja.");
                }
                $("#profilesTable").bootgrid("reload");
            },
            error: function(jqXHR, textStatus, errorThrown) {
                myDeleteError();
            }
        });
    };

    var readProfile = function(profileId) {
        $.ajax({
            url: "readProfile.do",
            type: "GET",
            data: {
                profileId: profileId
            },
            async: false,
            success: function(response) {
                $("#idInput").val(response.id);
                $("#descriptionInput").val(response.description);
                $.each(response.roles, function (idx, value) {
                    $('#my-select').multiSelect('select', value.id.toString());
                });
            },
            error: function(jqXHR, textStatus, errorThrown) {
                myDeleteError();
            }
        });
    };

    var toggleElements = function(hidden) {
        $("#descriptionInput").attr('disabled', hidden);
    };

    $("#addProfile").click(function() {
        resetForm();
        toggleElements(false);
        $('#addButton').show();
        $('#updateButton').hide();
        $('#addProfileLabel').show();
        $('#readProfileLabel').hide();
        $('#updateProfileLabel').hide();
        $('#profileModal').modal('show');
    });

    var profilesTable = $("#profilesTable").bootgrid({
        columnSelection: false,
        ajax: true,
        requestHandler: function (request) {
            return request;
        },
        url: "./getMatchedProfiles.do",
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
        profilesTable.find(".command-edit").on("click", function(e) {
            resetForm();
            toggleElements(false);
            readProfile($(this).data("row-id"));
            $('#addButton').hide();
            $('#updateButton').show();
            $('#addProfileLabel').hide();
            $('#readProfileLabel').hide();
            $('#updateProfileLabel').show();
            $('#profileModal').modal('show');
        }).end().find(".command-delete").on("click", function(e) {
            profileId = $(this).data("row-id");
            $('#deleteConfirmationModal').modal('show');
        }).end().find(".command-view").on("click", function(e) {
            resetForm();
            toggleElements(true);
            readProfile($(this).data("row-id"));
            $('#addButton').hide();
            $('#updateButton').hide();
            $('#addProfileLabel').hide();
            $('#readProfileLabel').show();
            $('#updateProfileLabel').hide();
            $('#profileModal').modal('show');
        });
    });

    var exportHTML = exportTableHTML("./rest/profiles");
    $(".search").before(exportHTML);

    $("#deleteEntityButton").click(function() {
        deleteProfile(profileId);
    });
});