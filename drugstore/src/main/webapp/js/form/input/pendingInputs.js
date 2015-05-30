PendingInputs = function() {
	
	var inputId = null;
	var inputs = [];
	
	$('#inputTableBody').on("click", ".view-row", function() {
		var parent = $(this).parent().parent();
		inputId = parent.attr("data-row-id");
		
		showInputModal(inputId);
	});
	
	var validateForm = function() {
		var form = $("#searchInputForm");
		form.validate({
			rules: {
				idSearch: {
					digits: true
				},
				deliveryNoteNumberSearch: {
					digits: true
				},
				purchaseOrderNumberSearch: {
					alphanumeric: true
				},
				dateFromSearch: {
					dateITA: true,
					maxDate: $("#dateToSearch")
				},
				dateToSearch: {
					dateITA: true,
					minDate: $("#dateFromSearch")
				}
			},
			showErrors: myShowErrors,
			onsubmit: false
		});
		return form.valid();
	};
	
	$("#inputTable").bootgrid({
		selection: true,
		multiSelect: true,
		rowSelect: true,
		keepSelection: true,
		formatters: {
			"option": function(column, row) {
				return "<a href=\"#\" class='view-row'>Consulta</a>";
			}
		}
	}).on("selected.rs.jquery.bootgrid", function(e, rows) {
		for (var i = 0; i < rows.length; i++) {
			inputs.push(rows[i].input);
		}
	}).on("deselected.rs.jquery.bootgrid", function(e, rows) {
		for (var i = 0; i < rows.length; i++) {
			for(var i = input.length - 1; i >= 0; i--) {
				if(inputs[i] === rows[i].input) {
					inputs.splice(i, 1);
				}
			}
		}
	});

};
	