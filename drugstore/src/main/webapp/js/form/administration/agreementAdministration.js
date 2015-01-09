$(document).ready(function() {
	
	var agreementId;
	
	var resetForm = function() {
		$("#idInput").val('');
		$("#codeInput").val('');
		$("#descriptionInput").val('');
		$("#numberOfDeliveryNoteDetailsPerPageInput").val('');
		$("#pickingFilepathInput").val('');
		$("#orderLabelFilepathInput").val('');
		$("#deliveryNoteFilepathInput").val('');
		$("#activeSelect").val($("#activeSelect option:first").val());
		$("#deliveryNoteConceptSelect").val($("#deliveryNoteConceptSelect option:first").val());
		$("#destructionConceptSelect").val($("#destructionConceptSelect option:first").val());
	};
	
	var deleteAgreement = function(agreementId) {
		$.ajax({
			url: "deleteAgreement.do",
			type: "POST",
			data: {
				agreementId: agreementId
			},
			async: true,
			success: function(response) {
				if (response === true) {
					myReload("success", "Se ha registrado la baja con identificador:  " + agreementId);
				} else {
					myReload("danger", "El registro no existe o ya ha sido dado de baja.");
				}
				$("#agreementsTable").bootgrid("reload");
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myDeleteError();
			}
		});
	};
	
	var readAgreement = function(agreementId) {
		$.ajax({
			url: "readAgreement.do",
			type: "GET",
			data: {
				agreementId: agreementId
			},
			async: false,
			success: function(response) {
				$("#idInput").val(response.id);
				$("#codeInput").val(response.code);
				$("#descriptionInput").val(response.description);
				$("#numberOfDeliveryNoteDetailsPerPageInput").val(response.numberOfDeliveryNoteDetailsPerPage);
				$("#pickingFilepathInput").val(response.pickingFilepath);
				$("#orderLabelFilepathInput").val(response.orderLabelFilepath);
				$("#deliveryNoteFilepathInput").val(response.deliveryNoteFilepath);
				var isActive = (response.active) ? "true" : "false";
				$("#activeSelect").val(isActive).trigger('chosen:update');
				$("#deliveryNoteConceptSelect").val(response.deliveryNoteConcept.id).trigger('chosen:update');
				$("#destructionConceptSelect").val(response.destructionConcept.id).trigger('chosen:update');
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
		$("#numberOfDeliveryNoteDetailsPerPageInput").attr('disabled', hidden);
		$("#pickingFilepathInput").attr('disabled', hidden);
		$("#orderLabelFilepathInput").attr('disabled', hidden);
		$("#deliveryNoteFilepathInput").attr('disabled', hidden);
		$("#deliveryNoteConceptSelect").prop('disabled', hidden).trigger('chosen:update');
		$("#destructionConceptSelect").prop('disabled', hidden).trigger('chosen:update');
	};
	
	$("#addAgreement").click(function() {
		resetForm();
		toggleElements(false);
		$('#addButton').show();
		$('#updateButton').hide();
		$('#addAgreementLabel').show();
		$('#readAgreementLabel').hide();
		$('#updateAgreementLabel').hide();
		$('#agreementModal').modal('show');
	});
	
	var agreementsTable = $("#agreementsTable").bootgrid({
	    ajax: true,
	    requestHandler: function (request) {
	    	return request; 
	    },
	    url: "/drogueria/getMatchedAgreements.do",
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
		agreementsTable.find(".command-edit").on("click", function(e) {
			resetForm();
			toggleElements(false);
			readAgreement($(this).data("row-id"));
			$('#addButton').hide();
			$('#updateButton').show();
			$('#addAgreementLabel').hide();
			$('#readAgreementLabel').hide();
			$('#updateAgreementLabel').show();
			$('#agreementModal').modal('show');
		}).end().find(".command-delete").on("click", function(e) {
			agreementId = $(this).data("row-id");
			$('#deleteConfirmationModal').modal('show');
		}).end().find(".command-view").on("click", function(e) {
			resetForm();
			toggleElements(true);
			readAgreement($(this).data("row-id"));
			$('#addButton').hide();
			$('#updateButton').hide();
			$('#addAgreementLabel').hide();
			$('#readAgreementLabel').show();
			$('#updateAgreementLabel').hide();
			$('#agreementModal').modal('show');
		});
	});
	
	var exportHTML = exportTableHTML("/drogueria/rest/agreements");
	$(".search").before(exportHTML);
	
	$("#deleteEntityButton").click(function() {
		deleteAgreement(agreementId);
	});
});