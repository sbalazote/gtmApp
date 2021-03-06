$(document).ready(function() {
	
	var agreementId;
	
	var resetForm = function() {
		$("#idInput").val('');
		$("#codeInput").val('');
		$("#descriptionInput").val('');
		$("#numberOfDeliveryNoteDetailsPerPageInput").val('');
		$("#orderLabelPrinterSelect").val($("#orderLabelPrinterSelect option:first").val());
		$("#deliveryNotePrinterSelect").val($("#orderLabelPrinterSelect option:first").val());
		$("#activeSelect").val($("#activeSelect option:first").val());
		$("#pickingListSelect").val($("#pickingListSelect option:first").val());
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
				$("#orderLabelPrinterSelect").val(response.orderLabelPrinter).trigger('chosen:update');
				$("#deliveryNotePrinterSelect").val(response.deliveryNotePrinter).trigger('chosen:update');
				var isActive = (response.active) ? "true" : "false";
				$("#activeSelect").val(isActive).trigger('chosen:update');
				var isPickingList = (response.pickingList) ? "true" : "false";
				$("#pickingListSelect").val(isPickingList).trigger('chosen:update');
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
		$("#pickingListSelect").prop('disabled', hidden).trigger('chosen:update');
		$("#numberOfDeliveryNoteDetailsPerPageInput").attr('disabled', hidden);
		$("#orderLabelPrinterSelect").attr('disabled', hidden).trigger('chosen:update');
		$("#deliveryNotePrinterSelect").attr('disabled', hidden).trigger('chosen:update');
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
		columnSelection: false,
	    ajax: true,
	    requestHandler: function (request) {
	    	return request; 
	    },
	    url: "./getMatchedAgreements.do",
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
			$("#codeInput").attr('disabled', true);
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

	var searchHTML = $('.search');
	var searchPhrase = '&searchPhrase=' + $('.search-field').val();
	var exportHTML = exportQueryTableHTML("./rest/agreements", searchPhrase);
	searchHTML.before(exportHTML);

	$('.search-field').keyup(function(e) {
		searchPhrase = '&searchPhrase=' + $('.search-field').val();
		exportHTML = exportQueryTableHTML("./rest/agreements", searchPhrase);
		if (searchHTML.prev().length == 0) {
			searchHTML.before(exportHTML);
		} else {
			searchHTML.prev().html(exportHTML);
		}
	});

	$("#deleteEntityButton").click(function() {
		deleteAgreement(agreementId);
	});
});