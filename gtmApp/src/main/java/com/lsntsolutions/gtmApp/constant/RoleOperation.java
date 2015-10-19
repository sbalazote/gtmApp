package com.lsntsolutions.gtmApp.constant;

public enum RoleOperation {
	INPUT("Ingreso", 1),
	OUTPUT("Egreso", 2),
	PROVISIONING_REQUEST("Solicitud de Abastecimiento", 3),
	PROVISIONING_REQUEST_UPDATE("Solicitud de Abastecimiento", 4),
	PROVISIONING_REQUEST_AUTHORIZATION("Autorizacion de Solicitud de Abastecimiento", 5),
	PROVISIONING_REQUEST_CANCELLATION("Anulación de Solicitud de Abastecimiento", 6),
	PROVISIONING_REQUEST_PRINT("Impresión de Hoja de Picking", 7),
	ORDER_ASSEMBLY("Armado de Pedido", 8),
	ORDER_ASSEMBLY_CANCELLATION(	"Anulación de Armado de Pedido", 9),
	DELIVERY_NOTE_PRINT("Impresión de Remito", 10),
	DELIVERY_NOTE_CANCELLATION("Anulación de Remito", 11),
	ENTITY_ADMINISTRATION("Administración de Entidades", 12),
	USER_ADMINISTRATION("Administración de Usuarios", 13),
	SERIALIZED_RETURNS("Devolución de Series", 14),
	INPUT_CANCELLATION("Anulación de Ingreso", 15),
	INPUT_AUTHORIZATION("Autorización de Ingreso", 16),
	OUTPUT_CANCELLATION("Anulación de Egreso", 17),
	AGREEMENT_TRANSFER("Transferencia de Convenio", 18),
	SUPPLYING("Dispensa", 19),
	SUPPLYING_CANCELLATION("Anulación de Dispensa", 20),
	PENDING_TRANSACTIONS("Transacciones Pendientes", 21),
	LOGISTIC_OPERATOR_ASSIGNMENT("Asignacion de Operador Logistico",22),
	SEARCH_INPUTS("Busqueda de Ingresos",23),
	SEARCH_OUTPUTS("Busqueda de Egresos",24),
	SEARCH_PROVISIONING_REQUEST("Busqueda de Pedidos",25),
	SEARCH_SUPPLYING("Busqueda de Dispensas",26),
	SEARCH_DELIVERY_NOTE("Busqueda de Remitos",27),
	SEARCH_AUDIT("Auditoria",28),
	SEARCH_STOCK("Busqueda de Stock",29),
	SEARCH_SERIALIZED_PRODUCT("Traza por Serie",30),
	SEARCH_BATCH_EXPIRATEDATE_PRODUCT("Traza por lote",31),
	AFFILIATE_ADMINISTRATION("Administracion de Afiliados",32),
	AGENT_ADMINISTRATION("Administracion de Agentes",33),
	CLIENT_ADMINISTRATION("Administracion de Clientes",34),
	CONCEPT_ADMINISTRATION("Administracion de Conceptos",35),
	AGREEMENT_ADMINISTRATION("Administracion de Convenios",36),
	EVENT_ADMINISTRATION("Administracion de Eventos",37),
	DELIVERY_LOCATION_ADMINISTRATION("Administracion de Lugares de Entrega",38),
	LOGISTIC_OPERATOR_ADMINISTRATION("Administracion de Operador Logistico",39),
	PRODUCT_ADMINISTRATION("Administracion de Productos",40),
	PROVIDER_ADMINISTRATION("Administracion de Proveedores",41),
	DELIVERY_NOTE_ENUMERATOR_ADMINISTRATION("Administracion de Puntos de Venta",42),
	PROVIDER_SERIALIZED_FORMAT_ADMINISTRATION("Administracion de Formatos de Serializacion",43),
	PROFILE_ADMINISTRATION("Administracion de Perfiles",44);

	private Integer id;
	private String description;

	private RoleOperation(String description, Integer id) {
		this.id = id;
		this.description = description;
	}

	public Integer getId() {
		return this.id;
	}

	public String getDescription() {
		return this.description;
	}

	public static RoleOperation fromId(Integer id) {
		for (RoleOperation roleOperation : RoleOperation.values()) {
			if (roleOperation.getId().equals(id)) {
				return roleOperation;
			}
		}
		return null;
	}
}