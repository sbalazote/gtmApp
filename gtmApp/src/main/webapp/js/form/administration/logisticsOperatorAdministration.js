$(document).ready(function() {
	
	var logisticsOperatorId;
	
	var resetForm = function() {
		$("#idInput").val('');
		$("#codeInput").val('');
		$("#nameInput").val('');
		$("#taxIdInput").val('');
		$("#corporateNameInput").val('');
		$("#provinceSelect").val($("#provinceSelect option:first").val());
		$("#localityInput").val('');
		$("#addressInput").val('');
		$("#zipCodeInput").val('');
		$("#phoneInput").val('');
		$("#activeSelect").val($("#activeSelect option:first").val());
	};
	
	var deleteLogisticsOperator = function(logisticsOperatorId) {
		$.ajax({
			url: "deleteLogisticsOperator.do",
			type: "POST",
			data: {
				logisticsOperatorId: logisticsOperatorId
			},
			async: true,
			success: function(response) {
				if (response === true) {
					myReload("success", "Se ha registrado la baja con identificador:  " + logisticsOperatorId);
				} else {
					myReload("danger", "El registro no existe o ya ha sido dado de baja.");
				}
				$("#logisticsOperatorsTable").bootgrid("reload");
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myDeleteError();
			}
		});
	};
	
	var readLogisticsOperator = function(logisticsOperatorId) {
		$.ajax({
			url: "readLogisticsOperator.do",
			type: "GET",
			data: {
				logisticsOperatorId: logisticsOperatorId
			},
			async: false,
			success: function(response) {
				$("#idInput").val(response.id);
				$("#codeInput").val(response.code);
				$("#nameInput").val(response.name);
				$("#taxIdInput").val(response.taxId);
				$("#corporateNameInput").val(response.corporateName);
				$("#provinceSelect").val(response.province.id).trigger('chosen:update');
				$("#localityInput").val(response.province.locality);
				$("#addressInput").val(response.address);
				$("#zipCodeInput").val(response.zipCode);
				$("#phoneInput").val(response.phone);
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
		$("#nameInput").attr('disabled', hidden);
		$("#taxIdInput").attr('disabled', hidden);
		$("#corporateNameInput").attr('disabled', hidden);
		$("#provinceSelect").prop('disabled', hidden).trigger('chosen:update');
		$("#localityInput").attr('disabled', hidden);
		$("#addressInput").attr('disabled', hidden);
		$("#zipCodeInput").attr('disabled', hidden);
		$("#phoneInput").attr('disabled', hidden);
		$("#activeSelect").prop('disabled', hidden).trigger('chosen:update');
	};
	
	$("#addLogisticsOperator").click(function() {
		resetForm();
		toggleElements(false);
		$('#addButton').show();
		$('#updateButton').hide();
		$('#addLogisticsOperatorLabel').show();
		$('#readLogisticsOperatorLabel').hide();
		$('#updateLogisticsOperatorLabel').hide();
		$('#logisticsOperatorModal').modal('show');
	});
	
	var logisticsOperatorsTable = $("#logisticsOperatorsTable").bootgrid({
		columnSelection: false,
		sorting: false,
	    ajax: true,
	    requestHandler: function (request) {
	    	return request; 
	    },
	    url: "./getMatchedLogisticsOperators.do",
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
		logisticsOperatorsTable.find(".command-edit").on("click", function(e) {
			resetForm();
			toggleElements(false);
			readLogisticsOperator($(this).data("row-id"));
			$('#addButton').hide();
			$('#updateButton').show();
			$('#addLogisticsOperatorLabel').hide();
			$('#readLogisticsOperatorLabel').hide();
			$('#updateLogisticsOperatorLabel').show();
			$('#logisticsOperatorModal').modal('show');
		}).end().find(".command-delete").on("click", function(e) {
			logisticsOperatorId = $(this).data("row-id");
			$('#deleteConfirmationModal').modal('show');
		}).end().find(".command-view").on("click", function(e) {
			resetForm();
			toggleElements(true);
			readLogisticsOperator($(this).data("row-id"));
			$('#addButton').hide();
			$('#updateButton').hide();
			$('#addLogisticsOperatorLabel').hide();
			$('#readLogisticsOperatorLabel').show();
			$('#updateLogisticsOperatorLabel').hide();
			$('#logisticsOperatorModal').modal('show');
		});
	});

	var searchHTML = $('.search');
	var searchPhrase = '&searchPhrase=' + $('.search-field').val();
	var exportHTML = exportQueryTableHTML("./rest/logisticsOperators", searchPhrase);
	searchHTML.before(exportHTML);

	$('.search-field').keyup(function(e) {
		searchPhrase = '&searchPhrase=' + $('.search-field').val();
		exportHTML = exportQueryTableHTML("./rest/logisticsOperators", searchPhrase);
		if (searchHTML.prev().length == 0) {
			searchHTML.before(exportHTML);
		} else {
			searchHTML.prev().html(exportHTML);
		}
	});

	$("#deleteEntityButton").click(function() {
		deleteLogisticsOperator(logisticsOperatorId);
	});
});