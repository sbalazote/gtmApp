PendingInputs = function() {
	
	var inputId = null;
	var inputs = [];
	
	$('#inputTableBody').on("click", ".view-row", function() {
		var parent = $(this).parent().parent();
		inputId = parent.attr("data-row-id");
		
		showInputModal(inputId);
	});
	
	$("#inputTable").bootgrid({
		selection: true,
		multiSelect: true,
		rowSelect: false,
		keepSelection: true,
		formatters: {
			"option": function(column, row) {
				return "<button type=\"button\" class=\"btn btn-sm btn-default view-row\"><span class=\"glyphicon glyphicon-eye-open\"></span> Detalle</button>";
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
	