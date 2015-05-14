$(document).ready(function() {
	
	var deliveryLocationId;
	
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
		$("#emailInput").val('');
		$("#glnInput").val('');
		$("#agentSelect").val($("#agentSelect option:first").val());
		$("#activeSelect").val($("#activeSelect option:first").val());
	};
	
	var deleteDeliveryLocation = function(deliveryLocationId) {
		$.ajax({
			url: "deleteDeliveryLocation.do",
			type: "POST",
			data: {
				deliveryLocationId: deliveryLocationId
			},
			async: true,
			success: function(response) {
				if (response === true) {
					myReload("success", "Se ha registrado la baja con identificador:  " + deliveryLocationId);
				} else {
					myReload("danger", "El registro no existe o ya ha sido dado de baja.");
				}
				$("#deliveryLocationsTable").bootgrid("reload");
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myDeleteError();
			}
		});
	};
	
	var readDeliveryLocation = function(deliveryLocationId) {
		$.ajax({
			url: "readDeliveryLocation.do",
			type: "GET",
			data: {
				deliveryLocationId: deliveryLocationId
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
				$("#VATLiabilitySelect").val(response.vatliability.id).trigger('chosen:update');
				$("#phoneInput").val(response.phone);
				$("#emailInput").val(response.mail);
				$("#glnInput").val(response.gln);
				$("#agentSelect").val(response.agent.id).trigger('chosen:update');
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
		$("#VATLiabilitySelect").prop('disabled', hidden).trigger('chosen:update');
		$("#phoneInput").attr('disabled', hidden);
		$("#emailInput").attr('disabled', hidden);
		$("#glnInput").attr('disabled', hidden);
		$("#agentSelect").prop('disabled', hidden).trigger('chosen:update');
		$("#activeSelect").prop('disabled', hidden).trigger('chosen:update');
	};
	
	$("#addDeliveryLocation").click(function() {
		resetForm();
		toggleElements(false);
		$('#addButton').show();
		$('#updateButton').hide();
		$('#addDeliveryLocationLabel').show();
		$('#readDeliveryLocationLabel').hide();
		$('#updateDeliveryLocationLabel').hide();
		$('#deliveryLocationModal').modal('show');
	});
	
	var deliveryLocationsTable = $("#deliveryLocationsTable").bootgrid({
	    ajax: true,
	    requestHandler: function (request) {
	    	return request; 
	    },
	    url: "./getMatchedDeliveryLocations.do",
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
		deliveryLocationsTable.find(".command-edit").on("click", function(e) {
			resetForm();
			toggleElements(false);
			readDeliveryLocation($(this).data("row-id"));
			$('#addButton').hide();
			$('#updateButton').show();
			$('#addDeliveryLocationLabel').hide();
			$('#readDeliveryLocationLabel').hide();
			$('#updateDeliveryLocationLabel').show();
			$('#deliveryLocationModal').modal('show');
		}).end().find(".command-delete").on("click", function(e) {
			deliveryLocationId = $(this).data("row-id");
			$('#deleteConfirmationModal').modal('show');
		}).end().find(".command-view").on("click", function(e) {
			resetForm();
			toggleElements(true);
			readDeliveryLocation($(this).data("row-id"));
			$('#addButton').hide();
			$('#updateButton').hide();
			$('#addDeliveryLocationLabel').hide();
			$('#readDeliveryLocationLabel').show();
			$('#updateDeliveryLocationLabel').hide();
			$('#deliveryLocationModal').modal('show');
		});
	});
	
	var exportHTML = exportTableHTML("./rest/deliveryLocations");
	$(".search").before(exportHTML);
	
	$("#deleteEntityButton").click(function() {
		deleteDeliveryLocation(deliveryLocationId);
	});
});