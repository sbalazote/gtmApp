OrderAssemblySelection = function() {

	$("#provisioningRequestSearch").numeric();

	$("#orderTableBody").on("click", ".a-select", function(){
		var provisioningId = $(this).siblings(".span-provisioningId").html();
		$("#provisioningRequestId").val(provisioningId);
		$("#myForm").submit();
	});

	$("#searchButton").click(function() {
		$.ajax({
			url: "getAuthorizedProvisioningsForOrders.do",
			type: "GET",
			async: false,
			data: {
				provisioningRequestId: $("#provisioningRequestSearch").val() || null,
				agreementId: $("#agreementSearch").val() || null,
				clientId: $("#clientSearch").val() || null
			},
			success: function(response) {
				var aaData = [];
				for (var i = 0, l = response.length; i < l; ++i) {
					var orderDetail = {
						id: 0,
						client: "",
						agreement: "",
						state: "",
						date: "",
						option: ""
					};
					orderDetail.id = response[i].id;
					orderDetail.client = response[i].client.name;
					orderDetail.agreement = response[i].agreement.description;
					orderDetail.state =  response[i].state.description,
						orderDetail.date = myParseDate(response[i].deliveryDate);
					orderDetail.option = "<span class='span-provisioningId' style='display:none'>" + response[i].id + "</span><a type='button' class='btn btn-sm btn-default a-select' href='#'><span class='glyphicon glyphicon-check'></span> Seleccionar</a>";

					aaData.push(orderDetail);
				}

				$("#orderTable").bootgrid({
					caseSensitive: false
				});
				$("#orderTable").bootgrid("clear");
				$("#orderTable").bootgrid("append", aaData);
				$("#orderTable").bootgrid("search", $(".search-field").val());
			},
			error: function(response) {
			}
		});
	});
	
	$("#cleanButton").click(function() {
		$("#provisioningRequestSearch").val('');
		$('#clientSearch').val('').trigger('chosen:updated');
		$('#agreementSearch').val('').trigger('chosen:updated');
	});
};