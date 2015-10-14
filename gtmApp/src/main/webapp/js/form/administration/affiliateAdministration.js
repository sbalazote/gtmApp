$(document).ready(function() {
	
	var affiliateId;
	var clientId = "";
	
	var resetForm = function() {
		$("#idInput").val('');
		$("#codeInput").val('');
		$("#surnameInput").val('');
		$("#nameInput").val('');
		$('#documentTypeSelect').val($("#documentTypeSelect option:first").val());
		$('#documentInput').val('');
		$("#activeSelect").val($("#activeSelect option:first").val());
		$('#my-select').multiSelect('deselect_all');
	};
	
	var deleteAffiliate = function(affiliateId) {
		$.ajax({
			url: "deleteAffiliate.do",
			type: "POST",
			data: {
				affiliateId: affiliateId
			},
			async: true,
			success: function(response) {
				if (response === true) {
					myReload("success", "Se ha registrado la baja con identificador:  " + affiliateId);
				} else {
					myReload("danger", "El registro no existe o ya ha sido dado de baja.");
				}
				$("#affiliatesTable").bootgrid("reload");
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myDeleteError();
			}
		});
	};
	
	var readAffiliate = function(affiliateId) {
		$.ajax({
			url: "readAffiliate.do",
			type: "GET",
			data: {
				affiliateId: affiliateId
			},
			async: false,
			success: function(response) {
				$("#idInput").val(response.id);
				$("#codeInput").val(response.code);
				$("#surnameInput").val(response.surname);
				$("#nameInput").val(response.name);
				$('#documentTypeSelect').val(response.documentType).trigger('chosen:update');
				$('#documentInput').val(response.document);
				var isActive = (response.active) ? "true" : "false";
				$("#activeSelect").val(isActive).trigger('chosen:update');
				$.each(response.clients, function (idx, value) {
					$('#my-select').multiSelect('select', value.id.toString());
				});
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myDeleteError();
			}
		});
	};
	
	var toggleElements = function(hidden) {
		$("#codeInput").attr('disabled', hidden);
		$("#surnameInput").attr('disabled', hidden);
		$("#nameInput").attr('disabled', hidden);
		$('#documentTypeSelect').prop('disabled', hidden).trigger('chosen:update');
		$('#documentInput').attr('disabled', hidden);
		$("#activeSelect").prop('disabled', hidden).trigger('chosen:update');
	};
	
	$("#addAffiliate").click(function() {
		resetForm();
		toggleElements(false);
		$('#addButton').show();
		$('#updateButton').hide();
		$('#addAffiliateLabel').show();
		$('#readAffiliateLabel').hide();
		$('#updateAffiliateLabel').hide();
		$('#affiliateModal').modal('show');
	});
	
	var affiliatesTable = $("#affiliatesTable").bootgrid({
		columnSelection: false,
		sorting: false,
	    ajax: true,
	    requestHandler: function (request) {
			return {
				clientId: clientId,
				current: request.current,
				rowCount: request.rowCount,
				searchPhrase: request.searchPhrase
			};
	    },
	    url: "./getMatchedAffiliates.do",
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
		affiliatesTable.find(".command-edit").on("click", function(e) {
			resetForm();
			toggleElements(false);
			readAffiliate($(this).data("row-id"));
			$('#addButton').hide();
			$('#updateButton').show();
			$('#addAffiliateLabel').hide();
			$('#readAffiliateLabel').hide();
			$('#updateAffiliateLabel').show();
			$('#affiliateModal').modal('show');
		}).end().find(".command-delete").on("click", function(e) {
			affiliateId = $(this).data("row-id");
			$('#deleteConfirmationModal').modal('show');
		}).end().find(".command-view").on("click", function(e) {
			resetForm();
			toggleElements(true);
			readAffiliate($(this).data("row-id"));
			$('#addButton').hide();
			$('#updateButton').hide();
			$('#addAffiliateLabel').hide();
			$('#readAffiliateLabel').show();
			$('#updateAffiliateLabel').hide();
			$('#affiliateModal').modal('show');
		});
	});

	var searchHTML = $('.search');
	var searchPhrase = '&searchPhrase=' + $('.search-field').val();
	var client = '&clientId=' + clientId;
	var exportHTML = exportQueryTableHTML("./rest/affiliates", searchPhrase + client);
	searchHTML.before(exportHTML);

	$('.search-field').keyup(function(e) {
		searchPhrase = '&searchPhrase=' + $('.search-field').val();
		client = '&clientId=' + clientId;
		exportHTML = exportQueryTableHTML("./rest/affiliates", searchPhrase + client);
		if (searchHTML.prev().length == 0) {
			searchHTML.before(exportHTML);
		} else {
			searchHTML.prev().html(exportHTML);
		}
	});

	$("#deleteEntityButton").click(function() {
		deleteAffiliate(affiliateId);
	});

	$('#clientSearch').on('change', function(evt, params) {
		clientId = $("#clientSearch").val();
		searchPhrase = '&searchPhrase=' + $('.search-field').val();
		client = '&clientId=' + clientId;
		exportHTML = exportQueryTableHTML("./rest/affiliates", searchPhrase + client);
		if (searchHTML.prev().length == 0) {
			searchHTML.before(exportHTML);
		} else {
			searchHTML.prev().html(exportHTML);
		}
		affiliatesTable.bootgrid("reload");
	});
});