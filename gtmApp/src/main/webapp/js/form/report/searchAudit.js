
SearchAudit = function() {
	$('#dateFromButton').click(function() {
		var maxDate = $( "#dateToSearch" ).datepicker( "getDate" );
		$("#dateFromSearch").datepicker("option", "maxDate", maxDate);
		$("#dateFromSearch").datepicker().focus();
	});
	
	$('#dateToButton').click(function() {
		var minDate = $( "#dateFromSearch" ).datepicker( "getDate" );
		$("#dateToSearch").datepicker("option", "minDate", minDate);
		$("#dateToSearch").datepicker().focus();
	});

	$("#dateFromSearch").datepicker();
	$("#dateToSearch").datepicker();
	
	var validateForm = function() {
		var form = $("#searchAuditForm");
		form.validate({
			rules: {
				operationNumberSearch: {
					digits: true
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
	
	$("#cleanButton").click(function() {
		if($("#dateFromSearch").val()!= ""){
			$.datepicker._clearDate('#dateFromSearch');
		}
		if($("#dateToSearch").val()!= ""){
			$.datepicker._clearDate('#dateToSearch');
		}
		$('#roleSearch').val('').trigger('chosen:updated');
		$('#userSearch').val('').trigger('chosen:updated');
		$("#auditActionSearch").val('').trigger('chosen:updated');
		$('#operationNumberSearch').val('');
		$("#auditTable").bootgrid("clear");
	});
	
	$("#searchButton").click(function() {
		myHideAlert();
		if(validateForm()){
			var jsonAuditSearch = {
				"dateFrom": $("#dateFromSearch").val(),
				"dateTo": $("#dateToSearch").val(),
				"userId": $("#userSearch").val() || null,
				"operationId": $("#operationNumberSearch").val() || null,
				"roleId": $("#roleSearch").val() || null,
			};
			
			$.ajax({
				url: "getCountAuditSearch.do",
				type: "POST",
				contentType:"application/json",
				async: false,
				data: JSON.stringify(jsonAuditSearch),
				success: function(response) {
					if(response == true){
						makeQuery(jsonAuditSearch);
					}else{
						myQueryTooLargeAlert();
					}
				},
				error: function(jqXHR, textStatus, errorThrown) {
					myGenericError();
				}
			});
			
		}
	});
	
	var makeQuery = function(jsonAuditSearch) {
		$.ajax({
			url: "getAuditForSearch.do",
			type: "POST",
			contentType:"application/json",
			async: false,
			data: JSON.stringify(jsonAuditSearch),
			success: function(response) {
				var aaData = [];
				for (var i = 0, l = response.length; i < l; ++i) {
					var audit = {
						date: "",
						role: "",
						operationNumber: 0,
						action: "",
						user: ""
					};
					audit.date = myParseDateTime(response[i].date);
					audit.role = response[i].role.description;
					audit.operationNumber = response[i].operationId;
					audit.user = response[i].user.name;
					aaData.push(audit);
				}
				$("#auditTable").bootgrid({
					caseSensitive: false
				});
				$("#auditTable").bootgrid("clear");
				$("#auditTable").bootgrid("append", aaData);
				$("#auditTable").bootgrid("search", $(".search-field").val());
				
				var params = '&dateFrom=' + jsonAuditSearch.dateFrom + 
				'&dateTo=' + jsonAuditSearch.dateTo +
				'&operationId=' + jsonAuditSearch.operationId +
				'&userId=' + jsonAuditSearch.userId +
				'&roleId=' + jsonAuditSearch.roleId;
				
				var exportHTML = exportQueryTableHTML("./rest/audits", params);
				var searchHTML = $(".search");
				
				if (searchHTML.prev().length == 0) {
					$(".search").before(exportHTML);
				} else {
					$(".search").prev().html(exportHTML);
				}
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myGenericError();
			}
		});
	};
};