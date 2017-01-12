$(document).ready(function() {
	
	var affiliateId;
	var clientId = "";
	var clientAffiliateId;
	
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
	
	var readAffiliate = function(id) {
		affiliateId = id;
		$.ajax({
			url: "readAffiliate.do",
			type: "GET",
			data: {
				affiliateId: id
			},
			async: false,
			success: function(response) {
				$("#affiliateClientsTable").bootgrid("destroy");
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
	    ajax: true,
	    requestHandler: function (request) {
			return {
				clientId: clientId,
				current: request.current,
				rowCount: request.rowCount,
				searchPhrase: request.searchPhrase,
				id: request.sort.id || null,
				name: request.sort.name || null,
				surname: request.sort.surname || null,
				document: request.sort.document || null,
				documentType: request.sort.documentType || null,
				active: request.sort.active || null,
				code: request.sort.code || null
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
			$("#codeInput").attr('disabled', true);
			readAffiliate($(this).data("row-id"));
			$('#addButton').hide();
			$('#updateButton').show();
			$('#addAffiliateLabel').hide();
			$('#readAffiliateLabel').hide();
			$('#updateAffiliateLabel').show();
			$('#affiliateModal').modal('show');
			loadAffiliateClientsTable($(this).data("row-id"));
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

	var loadAffiliateClientsTable = function(affiliateId) {
		var affiliateClientsTable = $("#affiliateClientsTable").bootgrid({
			columnSelection: false,
			ajax: true,
			requestHandler: function (request) {
				return {
					affiliateId: affiliateId,
					current: request.current,
					rowCount: request.rowCount
				};
			},
			url: "./getClientAffiliates.do",
			formatters: {
				"commands": function (column, row) {
					// return "<button type=\"button\" class=\"btn btn-sm btn-default edit\" data-row-id=\"" + row.id + "\"><span class=\"glyphicon glyphicon-pencil\"></span></button> " +
						return "<button type=\"button\" class=\"btn btn-sm btn-default delete\" data-row-id=\"" + row.id + "\"><span class=\"glyphicon glyphicon-trash\"></span></button>";
				}
			}
		}).on("loaded.rs.jquery.bootgrid", function () {
			/* Executes after data is loaded and rendered */
			affiliateClientsTable.find(".edit").on("click", function (e) {
				//TODO editar la relacion
			}).end().find(".delete").on("click", function (e) {
				clientAffiliateId = $(this).data("row-id");
				$('#deleteClientAffiliateConfirmationModal').modal('show');
			});
		});
	};

	var deleteClientAffiliate = function(clientAffiliateId) {
		$.ajax({
			url: "deleteClientAffiliate.do",
			type: "POST",
			data: {
				clientAffiliateId: clientAffiliateId
			},
			async: true,
			success: function(response) {
				$("#affiliateClientsTable").bootgrid("reload");
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myDeleteError();
			}
		});
	};

	$("#addClientAffiliate").click(function() {
		$('#addClientAffiliateDiv').show();
		$('#addClientAffiliate').hide();
		$.ajax({
			url: "getClientToAssociate.do",
			type: "GET",
			data: {
				affiliateId: affiliateId
			},
			async: false,
			success: function(response) {
				$('#clientInput').empty();
				$('#clientInput').append("<option value=''></option>");
				for(var i=0;i < response.length;i++){
					$('#clientInput').append("<option value=" + response[i].id + ">" + response[i].code + " " + response[i].name + "</option>");
				}
				$('#clientInput').trigger("chosen:updated");
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myDeleteError();
			}
		});
	});

	$("#deleteClientAffiliateButton").click(function() {
		deleteClientAffiliate(clientAffiliateId);
	});

	$("#cancelAddClientAffiliateButton").click(function() {
		$('#addClientAffiliateDiv').hide();
		$('#addClientAffiliate').show();
	});

	$("#addClientAffiliateButton").click(function() {
		$.ajax({
			url: "saveClientAffiliate.do",
			type: "POST",
			data: {
				affiliateId: affiliateId,
				clientId: $("#clientInput").val(),
				associateNumber: $("#associateNumberInput").val()
			},
			async: false,
			success: function(response) {
				$('#addClientAffiliateDiv').hide();
				$('#addClientAffiliate').show();
				$("#affiliateClientsTable").bootgrid("reload");
				$('#associateNumberInput').val('');
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myDeleteError();
			}
		});
	});

	$('#addClientAffiliateDiv').hide();

	$("#clientInput").chosen({
		width: '100%' /* desired width */
	});
});