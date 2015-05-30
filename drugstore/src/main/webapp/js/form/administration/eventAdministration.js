$(document).ready(function() {
	
	var eventId;
	
	var resetForm = function() {
		$("#idInput").val('');
		$("#codeInput").val('');
		$("#descriptionInput").val('');
		$("#originAgentIdSelect").val($("#originAgentIdSelect option:first").val());
		$("#destinationAgentIdSelect").val($("#destinationAgentIdSelect option:first").val());
		$("#activeSelect").val($("#activeSelect option:first").val());
	};
	
	var deleteEvent = function(eventId) {
		$.ajax({
			url: "deleteEvent.do",
			type: "POST",
			data: {
				eventId: eventId
			},
			async: true,
			success: function(response) {
				if (response === true) {
					myReload("success", "Se ha registrado la baja con identificador:  " + eventId);
				} else {
					myReload("danger", "El registro no existe o ya ha sido dado de baja.");
				}
				$("#eventsTable").bootgrid("reload");
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myDeleteError();
			}
		});
	};
	
	var readEvent = function(eventId) {
		$.ajax({
			url: "readEvent.do",
			type: "GET",
			data: {
				eventId: eventId
			},
			async: false,
			success: function(response) {
				$("#idInput").val(response.id);
				$("#codeInput").val(response.code);
				$("#descriptionInput").val(response.description);
				$("#originAgentIdSelect").val(response.originAgent.id).trigger('chosen:update');
				$("#destinationAgentIdSelect").val(response.destinationAgent.id).trigger('chosen:update');
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
		$("#originAgentIdSelect").prop('disabled', hidden).trigger('chosen:update');
		$("#destinationAgentIdSelect").prop('disabled', hidden).trigger('chosen:update');
		$("#activeSelect").prop('disabled', hidden).trigger('chosen:update');
	};
	
	$("#addEvent").click(function() {
		resetForm();
		toggleElements(false);
		$('#addButton').show();
		$('#updateButton').hide();
		$('#addEventLabel').show();
		$('#readEventLabel').hide();
		$('#updateEventLabel').hide();
		$('#eventModal').modal('show');
	});
	
	var eventsTable = $("#eventsTable").bootgrid({
	    ajax: true,
	    requestHandler: function (request) {
	    	return request; 
	    },
	    url: "./getMatchedEvents.do",
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
		eventsTable.find(".command-edit").on("click", function(e) {
			resetForm();
			toggleElements(false);
			readEvent($(this).data("row-id"));
			$('#addButton').hide();
			$('#updateButton').show();
			$('#addEventLabel').hide();
			$('#readEventLabel').hide();
			$('#updateEventLabel').show();
			$('#eventModal').modal('show');
		}).end().find(".command-delete").on("click", function(e) {
			eventId = $(this).data("row-id");
			$('#deleteConfirmationModal').modal('show');
		}).end().find(".command-view").on("click", function(e) {
			resetForm();
			toggleElements(true);
			readEvent($(this).data("row-id"));
			$('#addButton').hide();
			$('#updateButton').hide();
			$('#addEventLabel').hide();
			$('#readEventLabel').show();
			$('#updateEventLabel').hide();
			$('#eventModal').modal('show');
		});
	});
	
	var exportHTML = exportTableHTML("./rest/events");
	$(".search").before(exportHTML);
	
	$("#deleteEntityButton").click(function() {
		deleteEvent(eventId);
	});
});