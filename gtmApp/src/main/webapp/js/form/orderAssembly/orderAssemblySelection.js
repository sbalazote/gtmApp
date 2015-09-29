OrderAssemblySelection = function() {
	
	refreshTable();
	window.setInterval(refreshTable, 60000);
	
	$("#orderTableBody").on("click", ".a-select", function(){
		var provisioningId = $(this).siblings(".span-provisioningId").html();
		$("#provisioningRequestId").val(provisioningId);
		$("#myForm").submit();
	});
		
	$("#searchButton").click(function() {
		refreshTable();
	});
	
	$("#cleanButton").click(function() {
		$('#clientSearch').val('').trigger('chosen:updated');
		$('#agreementSearch').val('').trigger('chosen:updated');
		refreshTable();
	});
	
	function refreshTable() {
		$.ajax({
			url: "getAuthorizedProvisioningsForOrders.do",
			type: "GET",
			async: false,
			data: {
				agreementId: $("#agreementSearch").val() || null,
				clientId: $("#clientSearch").val() || null,
				},
			success: function(response) {
				var aaData = [];
				for (var i = 0, l = response.length; i < l; ++i) {
					var orderDetail = {
						id: 0,
						client: "",
						agreement: "",
						option: ""
					};
					orderDetail.id = response[i].id;
					orderDetail.client = response[i].client.name;
					orderDetail.agreement = response[i].agreement.description;
					orderDetail.option = "<span class='span-provisioningId' style='display:none'>" + response[i].id + 
							"</span><a class='a-select' href='javascript:void(0);'>Seleccionar</a>";
					
					aaData.push(orderDetail);
				}
				
				$("#orderTable").bootgrid({
					caseSensitive: false
				});
				$("#orderTable").bootgrid().bootgrid("clear");
				$("#orderTable").bootgrid().bootgrid("append", aaData);
				$("#orderTable").bootgrid().bootgrid("search", $(".search-field").val());
			},
			error: function(response) {
			}
		});	
	}
};