$(document).ready(function() {
	
	var serializedFormatId = null;

	$("#addProviderSerializedFormat").click(function() {
		window.location="addProviderSerializedFormat.do";
	});
	
	var deleteProviderSerializedFormat = function(serializedFormatId) {
		$.ajax({
			url: "deleteProviderSerializedFormat.do",
			type: "POST",
			data: {
				serializedFormatId: serializedFormatId,
			},
			async: true,
			success: function(response) {
				if (response === true) {
					myDeleteRedirect("providerSerializedFormatAdministration.do");
				} else {
					myNotFoundDeleteError("providerSerializedFormatAdministration.do");
				}
			},
			error: function(jqXHR, textStatus, errorThrown) {
				myDeleteError();
			}
		});
	};
	
	$.ajax({
		url: "getProviderSerializedFormats.do",
		type: "POST",
		contentType:"application/json",
		async: false,
		success: function(response) {
			var aaData = [];
			for (var i = 0, l = response.length; i < l; ++i) {
				var providerSerializedFormat = {
					id: 0,
					gtinLength: 0,
					serialNumberLength: 0,
					expirationDateLength: 0,
					batchLength: 0,
					sequence: ""
				};
				providerSerializedFormat.id = response[i].id;
				providerSerializedFormat.gtinLength = response[i].gtinLength;
				providerSerializedFormat.serialNumberLength = response[i].serialNumberLength;
				providerSerializedFormat.expirationDateLength = response[i].expirationDateLength;
				providerSerializedFormat.batchLength = response[i].batchLength;
				providerSerializedFormat.sequence = response[i].sequence;
				
				aaData.push(providerSerializedFormat);
			}
			var serializedFormatTable = $("#serializedFormatTable").bootgrid({
				caseSensitive: false,
				formatters: {
					"commands": function(column, row) {
						return "<button type=\"button\" class=\"btn btn-sm btn-default command-delete\" data-row-id=\"" + row.id + "\"><span class=\"glyphicon glyphicon-trash\"></span></button>";
					}
				}
			}).on("loaded.rs.jquery.bootgrid", function() {
				serializedFormatTable.find(".command-delete").on("click", function(e) {
					serializedFormatId = $(this).data("row-id");
					$('#deleteConfirmationModal').modal('show');
				});
			});
			serializedFormatTable.bootgrid("append", aaData);
			serializedFormatTable.bootgrid("search", $(".search-field").val());
			
			var exportHTML = exportTableHTML("serializedFormatTable");
			$(".search").before(exportHTML);
		},
		error: function(jqXHR, textStatus, errorThrown) {
			myGenericError();
		}
	});

	$("#deleteEntityButton").click(function() {
		deleteProviderSerializedFormat(serializedFormatId);
	});

});