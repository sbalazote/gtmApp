$(document).ready(function() {
	
	var userId;
	
	var resetForm = function() {
		$("#idInput").val('');
		$("#nameInput").val('');
		$("#passwordInput").val('');
		$("#passwordInputCheck").val('');
		$("#activeSelect").val($("#activeSelect option:first").val());
		$('#my-select').multiSelect('deselect_all');
	};
	
	var deleteUser = function(userId) {
		$.ajax({
			url: "deleteUser.do",
			type: "POST",
			data: {
				userId: userId
			},
			async: true,
			success: function(response) {
				if (response === true) {
					myReload("success", "Se ha registrado la baja con identificador:  " + userId);
				} else {
					myReload("danger", "El registro no existe o ya ha sido dado de baja.");
				}
				$("#usersTable").bootgrid("reload");
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myDeleteError();
			}
		});
	};
	
	var readUser = function(userId) {
		$.ajax({
			url: "readUser.do",
			type: "GET",
			data: {
				userId: userId
			},
			async: false,
			success: function(response) {
				$("#idInput").val(response.id);
				$("#nameInput").val(response.name);
				$("#passwordInput").val(response.password);
				$("#passwordInputCheck").val(response.password);
				var isActive = (response.active) ? "true" : "false";
				$("#activeSelect").val(isActive).trigger('chosen:update');
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
		$("#nameInput").attr('disabled', hidden);
		$("#passwordInput").attr('disabled', hidden);
		$("#passwordInputCheck").attr('disabled', hidden);
		$("#activeSelect").prop('disabled', hidden).trigger('chosen:update');
		// TODO KNOWN-BUG deshabilitar multiSelect https://github.com/lou/multi-select/issues/68
		/* $('#my-select').prop('disabled', true);
		$('#my-select').multiSelect('refresh'); */
	};
	
	$("#addUser").click(function() {
		resetForm();
		toggleElements(false);
		$('#addButton').show();
		$('#updateButton').hide();
		$('#addUserLabel').show();
		$('#readUserLabel').hide();
		$('#updateUserLabel').hide();
		$('#userModal').modal('show');
	});
	
	var usersTable = $("#usersTable").bootgrid({
	    ajax: true,
	    requestHandler: function (request) {
	    	return request; 
	    },
	    url: "./getMatchedUsers.do",
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
		usersTable.find(".command-edit").on("click", function(e) {
			resetForm();
			toggleElements(false);
			readUser($(this).data("row-id"));
			$('#addButton').hide();
			$('#updateButton').show();
			$('#addUserLabel').hide();
			$('#readUserLabel').hide();
			$('#updateUserLabel').show();
			$('#userModal').modal('show');
		}).end().find(".command-delete").on("click", function(e) {
			userId = $(this).data("row-id");
			$('#deleteConfirmationModal').modal('show');
		}).end().find(".command-view").on("click", function(e) {
			resetForm();
			toggleElements(true);
			readUser($(this).data("row-id"));
			$('#addButton').hide();
			$('#updateButton').hide();
			$('#addUserLabel').hide();
			$('#readUserLabel').show();
			$('#updateUserLabel').hide();
			$('#userModal').modal('show');
		});
	});
	
	var exportHTML = exportTableHTML("./rest/users");
	$(".search").before(exportHTML);
	
	$("#deleteEntityButton").click(function() {
		deleteUser(userId);
	});
});