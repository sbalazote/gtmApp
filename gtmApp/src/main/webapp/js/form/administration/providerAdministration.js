$(document).ready(function() {
	
	var entity = "";
	var searchHTML;
	var searchPhrase;
	var exportHTML;

	// Modulo Proveedor
	
	var providerId;
	
	var resetProviderForm = function() {
		$("#providerIdInput").val('');
		$("#providerCodeInput").val('');
		$("#nameInput").val('');
		$("#taxIdInput").val('');
		$("#corporateNameInput").val('');
		$("#provinceSelect").val($("#provinceSelect option:first").val());
		$("#localityInput").val('');
		$("#addressInput").val('');
		$("#zipCodeInput").val('');
		$("#VATLiabilitySelect").val($("#VATLiabilitySelect option:first").val());
		$("#phoneInput").val('');
		$("#emailInput").val('');
		$("#glnInput").val('');
		$("#agentSelect").val($("#agentSelect option:first").val());
		$("#typeSelect").val($("#typeSelect option:first").val());
		$("#providerActiveSelect").val($("#providerActiveSelect option:first").val());
		$('#my-select').multiSelect('deselect_all');
	};
	
	var deleteProvider = function(providerId) {
		$.ajax({
			url: "deleteProvider.do",
			type: "POST",
			data: {
				providerId: providerId
			},
			async: true,
			success: function(response) {
				if (response === true) {
					myReload("success", "Se ha registrado la baja con identificador:  " + providerId);
				} else {
					myReload("danger", "El registro no existe o ya ha sido dado de baja.");
				}
				$("#providersTable").bootgrid("reload");
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myDeleteError();
			}
		});
	};
	
	var readProvider = function(providerId) {
		$.ajax({
			url: "readProvider.do",
			type: "GET",
			data: {
				providerId: providerId
			},
			async: false,
			success: function(response) {
				$("#providerIdInput").val(response.id);
				$("#providerCodeInput").val(response.code);
				$("#nameInput").val(response.name);
				$("#taxIdInput").val(response.taxId);
				$("#corporateNameInput").val(response.corporateName);
				$("#provinceSelect").val(response.province.id).trigger('chosen:update');
				$("#localityInput").val(response.locality);
				$("#addressInput").val(response.address);
				$("#zipCodeInput").val(response.zipCode);
				$("#VATLiabilitySelect").val(response.vatliability.id).trigger('chosen:update');
				$("#phoneInput").val(response.phone);
				$("#emailInput").val(response.mail);
				$("#glnInput").val(response.gln);
				$("#agentSelect").val(response.agent.id).trigger('chosen:update');
				$("#typeSelect").val(response.type.id).trigger('chosen:update');
				var isActive = (response.active) ? "true" : "false";
				$("#providerActiveSelect").val(isActive).trigger('chosen:update');
				$.each(response.logisticsOperators, function (idx, value) {
					$('#my-select').multiSelect('select', value.id.toString());
				});
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myDeleteError();
			}
		});
	};
	
	var toggleProviderElements = function(hidden) {
		$("#providerCodeInput").attr('disabled', hidden);
		$("#nameInput").attr('disabled', hidden);
		$("#taxIdInput").attr('disabled', hidden);
		$("#corporateNameInput").attr('disabled', hidden);
		$("#provinceSelect").prop('disabled', hidden).trigger('chosen:update');
		$("#localityInput").attr('disabled', hidden);
		$("#addressInput").attr('disabled', hidden);
		$("#zipCodeInput").attr('disabled', hidden);
		$("#VATLiabilitySelect").prop('disabled', hidden).trigger('chosen:update');
		$("#phoneInput").attr('disabled', hidden);
		$("#emailInput").attr('disabled', hidden);
		$("#glnInput").attr('disabled', hidden);
		$("#agentSelect").prop('disabled', hidden).trigger('chosen:update');
		$("#typeSelect").prop('disabled', hidden).trigger('chosen:update');
		$("#providerActiveSelect").prop('disabled', hidden).trigger('chosen:update');
	};
	
	$("#addProvider").click(function() {
		resetProviderForm();
		toggleProviderElements(false);
		$('#addProviderButton').show();
		$('#updateProviderButton').hide();
		$('#addProviderLabel').show();
		$('#readProviderLabel').hide();
		$('#updateProviderLabel').hide();
		$('#providerModal').modal('show');
	});
	
	var providersTable = $("#providersTable").bootgrid({
		columnSelection: false,
		sorting: false,
	    ajax: true,
	    requestHandler: function (request) {
	    	return request; 
	    },
	    url: "./getMatchedProviders.do",
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
		providersTable.find(".command-edit").on("click", function(e) {
			resetProviderForm();
			toggleProviderElements(false);
			readProvider($(this).data("row-id"));
			$('#addProviderButton').hide();
			$('#updateProviderButton').show();
			$('#addProviderLabel').hide();
			$('#readProviderLabel').hide();
			$('#updateProviderLabel').show();
			$('#providerModal').modal('show');
		}).end().find(".command-delete").on("click", function(e) {
			entity = "provider";
			providerId = $(this).data("row-id");
			$('#deleteConfirmationModal').modal('show');
		}).end().find(".command-view").on("click", function(e) {
			resetProviderForm();
			toggleProviderElements(true);
			readProvider($(this).data("row-id"));
			$('#addProviderButton').hide();
			$('#updateProviderButton').hide();
			$('#addProviderLabel').hide();
			$('#readProviderLabel').show();
			$('#updateProviderLabel').hide();
			$('#providerModal').modal('show');
		});
	});

	$('div#provider .search').before(exportQueryTableHTML("./rest/providers", '&searchPhrase=' + $('div#provider .search-field').val()));

	$('div#provider .search-field').keyup(function(e) {
		searchHTML = $('div#provider .search');
		searchPhrase = '&searchPhrase=' + $('div#provider .search-field').val();
		exportHTML = exportQueryTableHTML("./rest/providers", searchPhrase);
		if (searchHTML.prev().length == 0) {
			searchHTML.before(exportHTML);
		} else {
			searchHTML.prev().html(exportHTML);
		}
	});
	// Fin Modulo Proveedor
	
	// Modulo Tipo de Proveedor
	
	var providerTypeId;
	
	var resetProviderTypeForm = function() {
		$("#providerTypeIdInput").val('');
		$("#providerTypeCodeInput").val('');
		$("#descriptionInput").val('');
		$("#providerTypeActiveSelect").val($("#providerTypeActiveSelect option:first").val());
	};
	
	var deleteProviderType = function(providerTypeId) {
		$.ajax({
			url: "deleteProviderType.do",
			type: "POST",
			data: {
				providerTypeId: providerTypeId
			},
			async: true,
			success: function(response) {
				if (response === true) {
					myReload("success", "Se ha registrado la baja con identificador:  " + providerTypeId);
				} else {
					myReload("danger", "El registro no existe o ya ha sido dado de baja.");
				}
				$("#providerTypesTable").bootgrid("reload");
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myDeleteError();
			}
		});
	};
	
	var readProviderType = function(providerTypeId) {
		$.ajax({
			url: "readProviderType.do",
			type: "GET",
			data: {
				providerTypeId: providerTypeId
			},
			async: false,
			success: function(response) {
				$("#providerTypeIdInput").val(response.id);
				$("#providerTypeCodeInput").val(response.code);
				$("#descriptionInput").val(response.description);
				var isActive = (response.active) ? "true" : "false";
				$("#providerTypeActiveSelect").val(isActive).trigger('chosen:update');
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myDeleteError();
			}
		});
	};
	
	var toggleProviderTypeElements = function(hidden) {
		$("#providerTypeCodeInput").attr('disabled', hidden);
		$("#descriptionInput").attr('disabled', hidden);
		$("#providerTypeActiveSelect").prop('disabled', hidden).trigger('chosen:update');
	};
	
	$("#addProviderType").click(function() {
		resetProviderTypeForm();
		toggleProviderTypeElements(false);
		$('#addProviderTypeButton').show();
		$('#updateProviderTypeButton').hide();
		$('#addProviderTypeLabel').show();
		$('#readProviderTypeLabel').hide();
		$('#updateProviderTypeLabel').hide();
		$('#providerTypeModal').modal('show');
	});

	var providerTypesTable = $("#providerTypesTable").bootgrid({
		columnSelection: false,
		sorting: false,
	    ajax: true,
	    requestHandler: function (request) {
	    	return request; 
	    },
	    url: "./getMatchedProviderTypes.do",
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
		providerTypesTable.find(".command-edit").on("click", function(e) {
			resetProviderTypeForm();
			toggleProviderTypeElements(false);
			readProviderType($(this).data("row-id"));
			$('#addProviderTypeButton').hide();
			$('#updateProviderTypeButton').show();
			$('#addProviderTypeLabel').hide();
			$('#readProviderTypeLabel').hide();
			$('#updateProviderTypeLabel').show();
			$('#providerTypeModal').modal('show');
		}).end().find(".command-delete").on("click", function(e) {
			entity = "providerType";
			providerTypeId = $(this).data("row-id");
			$('#deleteConfirmationModal').modal('show');
		}).end().find(".command-view").on("click", function(e) {
			resetProviderTypeForm();
			toggleProviderTypeElements(true);
			readProviderType($(this).data("row-id"));
			$('#addProviderTypeButton').hide();
			$('#updateProviderTypeButton').hide();
			$('#addProviderTypeLabel').hide();
			$('#readProviderTypeLabel').show();
			$('#updateProviderTypeLabel').hide();
			$('#providerTypeModal').modal('show');
		});
	});

	$('div#providerType .search').before(exportQueryTableHTML("./rest/providerTypes", '&searchPhrase=' + $('div#providerType .search-field').val()));

	$('div#providerType .search-field').keyup(function(e) {
		searchHTML = $('div#providerType .search');
		searchPhrase = '&searchPhrase=' + $('div#providerType .search-field').val();
		exportHTML = exportQueryTableHTML("./rest/providerTypes", searchPhrase);
		if (searchHTML.prev().length == 0) {
			searchHTML.before(exportHTML);
		} else {
			searchHTML.prev().html(exportHTML);
		}
	});
	// Fin Modulo Tipo de Proveedor
	
	$("#deleteEntityButton").click(function() {
		if (entity === "provider") {
			deleteProvider(providerId);
		} else {
			deleteProviderType(providerTypeId);
		}
	});
});