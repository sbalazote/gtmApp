$(document).ready(function() {
	
	var agentId;
	
	var resetForm = function() {
		$("#idInput").val('');
		$("#codeInput").val('');
		$("#descriptionInput").val('');
		$("#activeSelect").val($("#activeSelect option:first").val());
	};
	
	var deleteAgent = function(agentId) {
		$.ajax({
			url: "deleteAgent.do",
			type: "POST",
			data: {
				agentId: agentId
			},
			async: true,
			success: function(response) {
				if (response === true) {
					myReload("success", "Se ha registrado la baja con identificador:  " + agentId);
				} else {
					myReload("danger", "El registro no existe o ya ha sido dado de baja.");
				}
				$("#agentsTable").bootgrid("reload");
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myDeleteError();
			}
		});
	};
	
	var readAgent = function(agentId) {
		$.ajax({
			url: "readAgent.do",
			type: "GET",
			data: {
				agentId: agentId
			},
			async: false,
			success: function(response) {
				$("#idInput").val(response.id);
				$("#codeInput").val(response.code);
				$("#descriptionInput").val(response.description);
				var isActive = (response.active) ? "true" : "false";
				$("#activeSelect").val(isActive).trigger('chosen:update');
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myDeleteError();
			}
		});
	};
	
	var toggleElements = function(hidden) {
		$("#codeInput").attr('disabled', hidden);
		$("#descriptionInput").attr('disabled', hidden);
		$("#activeSelect").prop('disabled', hidden).trigger('chosen:update');
	};
	
	$("#addAgent").click(function() {
		resetForm();
		toggleElements(false);
		$('#addButton').show();
		$('#updateButton').hide();
		$('#addAgentLabel').show();
		$('#readAgentLabel').hide();
		$('#updateAgentLabel').hide();
		$('#agentModal').modal('show');
	});
	
	var agentsTable = $("#agentsTable").bootgrid({
	    ajax: true,
	    requestHandler: function (request) {
	    	return request; 
	    },
	    url: "/drogueria/getMatchedAgents.do",
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
		agentsTable.find(".command-edit").on("click", function(e) {
			resetForm();
			toggleElements(false);
			readAgent($(this).data("row-id"));
			$('#addButton').hide();
			$('#updateButton').show();
			$('#addAgentLabel').hide();
			$('#readAgentLabel').hide();
			$('#updateAgentLabel').show();
			$('#agentModal').modal('show');
		}).end().find(".command-delete").on("click", function(e) {
			agentId = $(this).data("row-id");
			$('#deleteConfirmationModal').modal('show');
		}).end().find(".command-view").on("click", function(e) {
			resetForm();
			toggleElements(true);
			readAgent($(this).data("row-id"));
			$('#addButton').hide();
			$('#updateButton').hide();
			$('#addAgentLabel').hide();
			$('#readAgentLabel').show();
			$('#updateAgentLabel').hide();
			$('#agentModal').modal('show');
		});
	});
		
	var exportHTML = exportTableHTML("/drogueria/rest/agents");
	$(".search").before(exportHTML);
	
	$("#deleteEntityButton").click(function() {
		deleteAgent(agentId);
	});
});