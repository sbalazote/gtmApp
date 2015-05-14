$(document).ready(function() {
	
	var clientId;
	
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
		$("#VATLiabilitySelect").val($("#VATLiabilitySelect option:first").val());
		$("#phoneInput").val('');
		$("#activeSelect").val($("#activeSelect option:first").val());
		$('#my-select').multiSelect('deselect_all');
	};
	
	var deleteClient = function(clientId) {
		$.ajax({
			url: "deleteClient.do",
			type: "POST",
			data: {
				clientId: clientId
			},
			async: true,
			success: function(response) {
				if (response === true) {
					myReload("success", "Se ha registrado la baja con identificador:  " + clientId);
				} else {
					myReload("danger", "El registro no existe o ya ha sido dado de baja.");
				}
				$("#clientsTable").bootgrid("reload");
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myDeleteError();
			}
		});
	};
	
	var readClient = function(clientId) {
		$.ajax({
			url: "readClient.do",
			type: "GET",
			data: {
				clientId: clientId
			},
			async: false,
			success: function(response) {
				$("#idInput").val(response.id);
				$("#codeInput").val(response.code);
				$("#nameInput").val(response.name);
				$("#taxIdInput").val(response.taxId);
				$("#corporateNameInput").val(response.corporateName);
				$("#provinceSelect").val(response.province.id).trigger('chosen:update');
				$("#localityInput").val(response.locality);
				$("#addressInput").val(response.address);
				$("#zipCodeInput").val(response.zipCode);
				$("#VATLiabilitySelect").val(response.vatliability.id).trigger('chosen:update');
				$("#phoneInput").val(response.phone);
				var isActive = (response.active) ? "true" : "false";
				$("#activeSelect").val(isActive).trigger('chosen:update');
				$.each(response.deliveryLocations, function (idx, value) {
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
		$("#nameInput").attr('disabled', hidden);
		$("#taxIdInput").attr('disabled', hidden);
		$("#corporateNameInput").attr('disabled', hidden);
		$("#provinceSelect").prop('disabled', hidden).trigger('chosen:update');
		$("#localityInput").attr('disabled', hidden);
		$("#addressInput").attr('disabled', hidden);
		$("#zipCodeInput").attr('disabled', hidden);
		$("#VATLiabilitySelect").prop('disabled', hidden).trigger('chosen:update');
		$("#phoneInput").attr('disabled', hidden);
		$("#activeSelect").prop('disabled', hidden).trigger('chosen:update');
		// TODO KNOWN-BUG deshabilitar multiSelect https://github.com/lou/multi-select/issues/68
		/* $('#my-select').prop('disabled', true);
		$('#my-select').multiSelect('refresh'); */
	};
	
	$("#addClient").click(function() {
		resetForm();
		toggleElements(false);
		$('#addButton').show();
		$('#updateButton').hide();
		$('#addClientLabel').show();
		$('#readClientLabel').hide();
		$('#updateClientLabel').hide();
		$('#clientModal').modal('show');
	});
	
	var clientsTable = $("#clientsTable").bootgrid({
	    ajax: true,
	    requestHandler: function (request) {
	    	return request; 
	    },
	    url: "./getMatchedClients.do",
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
		clientsTable.find(".command-edit").on("click", function(e) {
			resetForm();
			toggleElements(false);
			readClient($(this).data("row-id"));
			$('#addButton').hide();
			$('#updateButton').show();
			$('#addClientLabel').hide();
			$('#readClientLabel').hide();
			$('#updateClientLabel').show();
			$('#clientModal').modal('show');
		}).end().find(".command-delete").on("click", function(e) {
			clientId = $(this).data("row-id");
			$('#deleteConfirmationModal').modal('show');
		}).end().find(".command-view").on("click", function(e) {
			resetForm();
			toggleElements(true);
			readClient($(this).data("row-id"));
			$('#addButton').hide();
			$('#updateButton').hide();
			$('#addClientLabel').hide();
			$('#readClientLabel').show();
			$('#updateClientLabel').hide();
			$('#clientModal').modal('show');
		});
	});
	
	var exportHTML = exportTableHTML("./rest/clients");
	$(".search").before(exportHTML);
	
	$("#deleteEntityButton").click(function() {
		deleteClient(clientId);
	});
});