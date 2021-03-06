package com.lsntsolutions.gtmApp.constant;

import java.util.ArrayList;
import java.util.List;

public enum RoleOperation {
	INPUT("Ingreso", 1),
	OUTPUT("Egreso", 2),
	PROVISIONING_REQUEST("Pedido", 3),
	PROVISIONING_REQUEST_UPDATE("Modificación de Pedidos", 4),
	PROVISIONING_REQUEST_AUTHORIZATION("Autorizacion de Pedidos", 5),
	PROVISIONING_REQUEST_CANCELLATION("Anulación de Pedidos", 6),
	PROVISIONING_REQUEST_PRINT("Impresión de Hoja de Picking", 7),
	ORDER_ASSEMBLY("Armado de Pedido", 8),
	ORDER_ASSEMBLY_CANCELLATION("Anulación de Armado de Pedido", 9),
	DELIVERY_NOTE_PRINT("Impresión de Remito", 10),
	DELIVERY_NOTE_CANCELLATION("Anulación de Remito", 11),
	USER_ADMINISTRATION("Administración de Usuarios", 12),
	SERIALIZED_RETURNS("Devolución de Series", 13),
	INPUT_CANCELLATION("Anulación de Ingreso", 14),
	INPUT_AUTHORIZATION("Autorización de Ingreso", 15),
	PRODUCT_DESTRUCTION("Destrucción de Mercadería", 16),
	AGREEMENT_TRANSFER("Transferencia de Convenio", 17),
	SUPPLYING("Dispensa", 18),
	PENDING_TRANSACTIONS("Transacciones Pendientes", 19),
	LOGISTIC_OPERATOR_ASSIGNMENT("Asignacion de Operador Logistico",20),
	SEARCH_INPUTS("Busqueda de Ingresos",21),
	SEARCH_OUTPUTS("Busqueda de Egresos",22),
	SEARCH_PROVISIONING_REQUEST("Busqueda de Pedidos",23),
	SEARCH_SUPPLYING("Busqueda de Dispensas",24),
	SEARCH_DELIVERY_NOTE("Busqueda de Remitos",25),
	SEARCH_AUDIT("Auditoria",26),
	SEARCH_STOCK("Busqueda de Stock",27),
	SEARCH_SERIALIZED_PRODUCT("Traza por Serie",28),
	SEARCH_BATCH_EXPIRATEDATE_PRODUCT("Traza por lote",29),
	AFFILIATE_ADMINISTRATION("Administracion de Afiliados",30),
	AGENT_ADMINISTRATION("Administracion de Agentes",31),
	CLIENT_ADMINISTRATION("Administracion de Clientes",32),
	CONCEPT_ADMINISTRATION("Administracion de Conceptos",33),
	AGREEMENT_ADMINISTRATION("Administracion de Convenios",34),
	EVENT_ADMINISTRATION("Administracion de Eventos",35),
	DELIVERY_LOCATION_ADMINISTRATION("Administracion de Lugares de Entrega",36),
	LOGISTIC_OPERATOR_ADMINISTRATION("Administracion de Operador Logistico",37),
	PRODUCT_ADMINISTRATION("Administracion de Productos",38),
	PROVIDER_ADMINISTRATION("Administracion de Proveedores",39),
	DELIVERY_NOTE_ENUMERATOR_ADMINISTRATION("Administracion de Puntos de Venta",40),
	PROVIDER_SERIALIZED_FORMAT_ADMINISTRATION("Administracion de Formatos de Serializacion",41),
	PROFILE_ADMINISTRATION("Administracion de Perfiles",42),
	PROPERTY_ADMINISTRATION("Administracion de Propiedades", 43),
	ORDER_LABEL_PRINT("Impresión de Rótulos", 44),
	FAKE_DELIVERY_NOTE_PRINT("Impresión de Remito Propio", 45),
	FORCED_INPUT("Ingreso Forzado", 46),
    FORCED_INPUT_UPDATE("Modificación de Ingreso Forzado", 47),
	BATCH_EXPIRATION_DATE_ORDER_ASSEMBLY("Armado en Colectora",48);

	private Integer id;
	private String description;

	RoleOperation(String description, Integer id) {
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

	public static List<RoleOperation> getAuditRoles() {
		List<RoleOperation> auditRoles = new ArrayList<>();
		auditRoles.add(RoleOperation.INPUT);
		auditRoles.add(RoleOperation.OUTPUT);
		auditRoles.add(RoleOperation.PROVISIONING_REQUEST);
		auditRoles.add(RoleOperation.PROVISIONING_REQUEST_UPDATE);
		auditRoles.add(RoleOperation.PROVISIONING_REQUEST_AUTHORIZATION);
		auditRoles.add(RoleOperation.PROVISIONING_REQUEST_CANCELLATION);
		auditRoles.add(RoleOperation.PROVISIONING_REQUEST_PRINT);
		auditRoles.add(RoleOperation.ORDER_ASSEMBLY);
		auditRoles.add(RoleOperation.ORDER_ASSEMBLY_CANCELLATION);
		auditRoles.add(RoleOperation.DELIVERY_NOTE_PRINT);
		auditRoles.add(RoleOperation.DELIVERY_NOTE_CANCELLATION);
		auditRoles.add(RoleOperation.SERIALIZED_RETURNS);
		auditRoles.add(RoleOperation.INPUT_CANCELLATION);
		auditRoles.add(RoleOperation.INPUT_AUTHORIZATION);
		auditRoles.add(RoleOperation.PRODUCT_DESTRUCTION);
		auditRoles.add(RoleOperation.AGREEMENT_TRANSFER);
		auditRoles.add(RoleOperation.SUPPLYING);
		auditRoles.add(RoleOperation.PENDING_TRANSACTIONS);
		auditRoles.add(RoleOperation.LOGISTIC_OPERATOR_ASSIGNMENT);
		auditRoles.add(RoleOperation.ORDER_LABEL_PRINT);
		auditRoles.add(RoleOperation.FAKE_DELIVERY_NOTE_PRINT);
		auditRoles.add(RoleOperation.FORCED_INPUT);
		auditRoles.add(RoleOperation.FORCED_INPUT_UPDATE);

		return  auditRoles;
	}
}