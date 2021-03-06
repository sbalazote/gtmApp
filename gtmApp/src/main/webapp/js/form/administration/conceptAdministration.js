
ConceptAdministration = function() {
	
	var conceptId;
	var events = [];
    var conceptInUse;

	var saveConcept = new SaveConcept();

	var resetForm = function() {
		$("#idInput").val('');
		$("#codeInput").val('');
		$("#descriptionInput").val('');
		$("#deliveryNoteEnumeratorSelect").val($("#inputSelect option:first").val());
		$("#inputSelect").val($("#inputSelect option:first").val());
		$("#printDeliveryNoteSelect").val($("#printDeliveryNoteSelect option:first").val());
		$("#refundSelect").val($("#refundSelect option:first").val());
		$("#informAnmatSelect").val($("#informAnmatSelect option:first").val());
		$("#deliveryNotesCopiesInput").val('');
		$("#activeSelect").val($("#activeSelect option:first").val());
		$("#clientSelect").val($("#clientSelect option:first").val());
		$("#destructionSelect").val($("#destructionSelect option:first").val());
		$('#my-select').multiSelect('deselect_all');
        events = [];
		saveConcept.setEvents(events);
	};
	
	var deleteConcept = function(conceptId) {
		$.ajax({
			url: "deleteConcept.do",
			type: "POST",
			data: {
				conceptId: conceptId
			},
			async: true,
			success: function(response) {
				if (response === true) {
					myReload("success", "Se ha registrado la baja con identificador:  " + conceptId);
				} else {
					myReload("danger", "El registro no existe o ya ha sido dado de baja.");
				}
				$("#conceptsTable").bootgrid("reload");
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myDeleteError();
			}
		});
	};
	
	var readConcept = function(conceptId) {
		$.ajax({
			url: "readConcept.do",
			type: "GET",
			data: {
				conceptId: conceptId
			},
			async: false,
			success: function(response) {
				$("#idInput").val(response.id);
				$("#codeInput").val(response.code);
				$("#descriptionInput").val(response.description);
				var isInput = (response.input) ? "true" : "false";
				$("#inputSelect").val(isInput).trigger('chosen:update');
				var isPrintDeliveryNote = (response.printDeliveryNote) ? "true" : "false";
				$("#printDeliveryNoteSelect").val(isPrintDeliveryNote).trigger('chosen:update');
				var isRefund = (response.refund) ? "true" : "false";
				$("#refundSelect").val(isRefund).trigger('chosen:update');
				var isInformAnmat = (response.informAnmat) ? "true" : "false";
				$("#informAnmatSelect").val(isInformAnmat).trigger('chosen:update');
				$("#deliveryNotesCopiesInput").val(response.deliveryNoteCopies);
				var isActive = (response.active) ? "true" : "false";
				$("#activeSelect").val(isActive).trigger('chosen:update');
				var isClient = (response.client) ? "true" : "false";
				$("#clientSelect").val(isClient).trigger('chosen:update');
				var isDestruction = (response.destruction) ? "true" : "false";
				$("#destructionSelect").val(isDestruction).trigger('chosen:update');

				getEvents();
				getDeliveryNoteEnumerators();
				if(response.deliveryNoteEnumerator != null){
					$("#deliveryNoteEnumeratorSelect").val(response.deliveryNoteEnumerator.id).trigger('chosen:update');
				}
				$.each(_.uniq(_.pluck(response.events, "id")), function (idx, value) {
					$('#my-select').multiSelect('select', value.toString());
					events.push(value.toString());
				});
				saveConcept.setEvents(events);
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myDeleteError();
			}
		});
	};


	var isConceptInUse = function(conceptId) {
		$.ajax({
			url: "isConceptInUse.do",
			type: "GET",
			data: {
				conceptId: conceptId
			},
			async: false,
			success: function(response) {
                conceptInUse = response;
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myDeleteError();
			}
		});
	};

	var getEvents = function() {
		$.ajax({
			url: "getInputOutputEvents.do",
			type: "GET",
			data: {
				input: $("#inputSelect").val()
			},
			async: false,
			success: function(response) {
				$('#my-select').multiSelect('refresh');
				for(var i = response.length-1; i >= 0 ; i--){
					var event = response[i];
					$('#my-select').multiSelect('addOption', { value: event.id , text: event.code + "-" +  event.description +": ORIGEN: " + event.originAgent.description + " - DESTINO: " + event.destinationAgent.description });
				}
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myGenericError();
			}
		});
	};
	
	var getDeliveryNoteEnumerators = function() {
		var fake;
		if($("#printDeliveryNoteSelect").val() == "true"){
			fake = false;
		}else{
			fake = true;
		}
		$.ajax({
			url: "getDeliveryNoteEnumerators.do",
			type: "GET",
			data: {
				fake: fake
			},
			async: false,
			success: function(response) {
				$('#deliveryNoteEnumeratorSelect').empty();
				$('#deliveryNoteEnumeratorSelect').append('<option value="">Punto de Venta...</option>');
				for(var i = response.length-1; i >= 0 ; i--){
					var enumerator = response[i];
					var deliveryNotePOS = addLeadingZeros(enumerator.deliveryNotePOS, 4);
					$('#deliveryNoteEnumeratorSelect').append('<option value='+ enumerator.id + '>' + deliveryNotePOS +'</option>');
				}
				$('#deliveryNoteEnumeratorSelect').trigger('chosen:updated');
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myGenericError();
			}
		});
	};

    /*$('#my-select').multiSelect({
        afterSelect: function(value, text){
            if(value != null) {
                events.push(value[0]);
				saveConcept.setEvents(events);
            }
        },
        afterDeselect: function(value, text){
            if(value != null){
                events.splice(events.indexOf(value[0]),1);
				saveConcept.setEvents(events);
            }
        }
    });*/

    $('#inputSelect').on('change', function(evt, params) {
        $('#my-select').multiSelect('deselect_all');
        getEvents();

		// si es un egreso se bloquea el '¿Es devol. cliente?'
		var refundSelectDisabled = params.selected === "false";
		$("#refundSelect").prop('disabled', refundSelectDisabled).trigger('chosen:updated');
    });
    
    $('#printDeliveryNoteSelect').on('change', function(evt, params) {
    	getDeliveryNoteEnumerators();
    });
    
	var toggleElements = function(hidden) {
		$("#codeInput").attr('disabled', hidden);
		$("#descriptionInput").attr('disabled', hidden);
		$("#deliveryNoteEnumeratorSelect").prop('disabled', hidden).trigger('chosen:update');
		$("#inputSelect").prop('disabled', hidden).trigger('chosen:update');
		$("#printDeliveryNoteSelect").prop('disabled', hidden).trigger('chosen:update');
		$("#refundSelect").prop('disabled', hidden).trigger('chosen:update');
		$("#informAnmatSelect").prop('disabled', hidden).trigger('chosen:update');
		$("#deliveryNotesCopiesInput").attr('disabled', hidden);
		$("#activeSelect").prop('disabled', hidden).trigger('chosen:update');
		$("#clientSelect").prop('disabled', hidden).trigger('chosen:update');
		$("#destructionSelect").prop('disabled', hidden).trigger('chosen:update');
		// TODO KNOWN-BUG deshabilitar multiSelect https://github.com/lou/multi-select/issues/68
		/* $('#my-select').prop('disabled', true);
		$('#my-select').multiSelect('refresh'); */
	};
	
	$("#addConcept").click(function() {
		resetForm();
		toggleElements(false);
		$('#addButton').show();
		$('#updateButton').hide();
		$('#addConceptLabel').show();
		$('#readConceptLabel').hide();
		$('#updateConceptLabel').hide();
		getEvents();
    	getDeliveryNoteEnumerators();
		$('#conceptModal').modal('show');
	});
	
	var conceptsTable = $("#conceptsTable").bootgrid({
		columnSelection: false,
	    ajax: true,
	    requestHandler: function (request) {
	    	return request; 
	    },
	    url: "./getMatchedConcepts.do",
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
		conceptsTable.find(".command-edit").on("click", function(e) {
			resetForm();
			toggleElements(false);
			$("#codeInput").attr('disabled', true);
			readConcept($(this).data("row-id"));
            conceptInUse = false;
            isConceptInUse($(this).data("row-id"));
            if(conceptInUse == true){
                toggleElementsForInUseConcept(true);
                $('#conceptAlreadyInUseLabel').show();
            }else{
                $('#conceptAlreadyInUseLabel').hide();
            }
			$('#addButton').hide();
			$('#updateButton').show();
			$('#addConceptLabel').hide();
			$('#readConceptLabel').hide();
			$('#updateConceptLabel').show();
			$('#conceptModal').modal('show');
		}).end().find(".command-delete").on("click", function(e) {
			conceptId = $(this).data("row-id");
			$('#deleteConfirmationModal').modal('show');
		}).end().find(".command-view").on("click", function(e) {
			resetForm();
			toggleElements(true);
			readConcept($(this).data("row-id"));
			$('#addButton').hide();
			$('#updateButton').hide();
			$('#addConceptLabel').hide();
			$('#readConceptLabel').show();
			$('#updateConceptLabel').hide();
			$('#conceptModal').modal('show');
		});
	});

	var searchHTML = $('.search');
	var searchPhrase = '&searchPhrase=' + $('.search-field').val();
	var exportHTML = exportQueryTableHTML("./rest/concepts", searchPhrase);
	searchHTML.before(exportHTML);

	$('.search-field').keyup(function(e) {
		searchPhrase = '&searchPhrase=' + $('.search-field').val();
		exportHTML = exportQueryTableHTML("./rest/concepts", searchPhrase);
		if (searchHTML.prev().length == 0) {
			searchHTML.before(exportHTML);
		} else {
			searchHTML.prev().html(exportHTML);
		}
	});
	
	$("#deleteEntityButton").click(function() {
		deleteConcept(conceptId);
	});


    var toggleElementsForInUseConcept = function(hidden) {
        $("#codeInput").attr('disabled', hidden);
        //$("#deliveryNoteEnumeratorSelect").prop('disabled', hidden).trigger('chosen:update');
        $("#inputSelect").prop('disabled', hidden).trigger('chosen:update');
        $("#refundSelect").prop('disabled', hidden).trigger('chosen:update');
        $("#informAnmatSelect").prop('disabled', hidden).trigger('chosen:update');
		$("#destructionSelect").prop('disabled', hidden).trigger('chosen:update');
    };
};