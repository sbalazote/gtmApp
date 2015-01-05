package com.drogueria.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "drugstore_property")
public class DrugstoreProperty implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "code", unique = true, nullable = false)
	private Integer code;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "tax_id", nullable = false)
	private String taxId;

	@Column(name = "corporate_name", nullable = false)
	private String corporateName;

	@ManyToOne
	@JoinColumn(name = "province_id", nullable = false)
	private Province province;

	@Column(name = "locality", nullable = false)
	private String locality;

	@Column(name = "address", nullable = false)
	private String address;

	@Column(name = "zip_code", nullable = false)
	private String zipCode;

	@Column(name = "phone")
	private String phone;

	@Column(name = "mail")
	private String mail;

	@Column(name = "gln", nullable = false)
	private String gln;

	@ManyToOne
	@JoinColumn(name = "agent_id", nullable = false)
	private Agent agent;

	@ManyToOne
	@JoinColumn(name = "type_id", nullable = false)
	private ProviderType type;

	@Column(name = "days_ago_pending_transactions", nullable = false)
	private Integer daysAgoPendingTransactions;

	@Column(name = "number_of_delivery_note_details_per_page", nullable = false)
	private Integer numberOfDeliveryNoteDetailsPerPage;

	@Column(name = "last_tag", nullable = false)
	private Integer lastTag;

	@Column(name = "order_label_filepath", nullable = false)
	private String orderLabelFilepath;

	@Column(name = "self_serialized_tag_filepath", nullable = false)
	private String selfSerializedTagFilepath;

	@Column(name = "last_delivery_note_number", nullable = false)
	private Integer lastDeliveryNoteNumber;

	@Column(name = "delivery_note_filepath", nullable = false)
	private String deliveryNoteFilepath;

	@Column(name = "picking_filepath", nullable = false)
	private String pickingFilepath;

	@Column(name = "ANMAT_name", nullable = false)
	private String ANMATName;

	@Column(name = "ANMAT_password", nullable = false)
	private String ANMATPassword;

	@ManyToOne
	@JoinColumn(name = "print_delivery_note_concept_id", nullable = false)
	private Concept printDeliveryNoteConcept;

	@ManyToOne
	@JoinColumn(name = "start_trace_concept_id", nullable = false)
	private Concept startTraceConcept;

	@Column(name = "proxy")
	private String proxy;

	@Column(name = "proxy_port")
	private String proxyPort;

	@Column(name = "inform_proxy", nullable = false)
	private boolean hasProxy;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCode() {
		return this.code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTaxId() {
		return this.taxId;
	}

	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}

	public String getCorporateName() {
		return this.corporateName;
	}

	public void setCorporateName(String corporateName) {
		this.corporateName = corporateName;
	}

	public Province getProvince() {
		return this.province;
	}

	public void setProvince(Province province) {
		this.province = province;
	}

	public String getLocality() {
		return this.locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZipCode() {
		return this.zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMail() {
		return this.mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getGln() {
		return this.gln;
	}

	public void setGln(String gln) {
		this.gln = gln;
	}

	public Agent getAgent() {
		return this.agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}

	public ProviderType getType() {
		return this.type;
	}

	public void setType(ProviderType type) {
		this.type = type;
	}

	public Integer getDaysAgoPendingTransactions() {
		return this.daysAgoPendingTransactions;
	}

	public void setDaysAgoPendingTransactions(Integer daysAgoPendingTransactions) {
		this.daysAgoPendingTransactions = daysAgoPendingTransactions;
	}

	public Integer getNumberOfDeliveryNoteDetailsPerPage() {
		return this.numberOfDeliveryNoteDetailsPerPage;
	}

	public void setNumberOfDeliveryNoteDetailsPerPage(Integer numberOfDeliveryNoteDetailsPerPage) {
		this.numberOfDeliveryNoteDetailsPerPage = numberOfDeliveryNoteDetailsPerPage;
	}

	public Integer getLastTag() {
		return this.lastTag;
	}

	public void setLastTag(Integer lastTag) {
		this.lastTag = lastTag;
	}

	public String getOrderLabelFilepath() {
		return this.orderLabelFilepath;
	}

	public void setOrderLabelFilepath(String orderLabelFilepath) {
		this.orderLabelFilepath = orderLabelFilepath;
	}

	public String getSelfSerializedTagFilepath() {
		return this.selfSerializedTagFilepath;
	}

	public void setSelfSerializedTagFilepath(String selfSerializedTagFilepath) {
		this.selfSerializedTagFilepath = selfSerializedTagFilepath;
	}

	public Integer getLastDeliveryNoteNumber() {
		return this.lastDeliveryNoteNumber;
	}

	public void setLastDeliveryNoteNumber(Integer lastDeliveryNoteNumber) {
		this.lastDeliveryNoteNumber = lastDeliveryNoteNumber;
	}

	public String getDeliveryNoteFilepath() {
		return this.deliveryNoteFilepath;
	}

	public void setDeliveryNoteFilepath(String deliveryNoteFilepath) {
		this.deliveryNoteFilepath = deliveryNoteFilepath;
	}

	public String getPickingFilepath() {
		return this.pickingFilepath;
	}

	public void setPickingFilepath(String pickingFilepath) {
		this.pickingFilepath = pickingFilepath;
	}

	public String getANMATName() {
		return this.ANMATName;
	}

	public void setANMATName(String aNMATName) {
		this.ANMATName = aNMATName;
	}

	public String getANMATPassword() {
		return this.ANMATPassword;
	}

	public void setANMATPassword(String aNMATPassword) {
		this.ANMATPassword = aNMATPassword;
	}

	public Concept getPrintDeliveryNoteConcept() {
		return this.printDeliveryNoteConcept;
	}

	public void setPrintDeliveryNoteConcept(Concept printDeliveryNoteConcept) {
		this.printDeliveryNoteConcept = printDeliveryNoteConcept;
	}

	public Concept getStartTraceConcept() {
		return this.startTraceConcept;
	}

	public void setStartTraceConcept(Concept startTraceConcept) {
		this.startTraceConcept = startTraceConcept;
	}

	public String getProxy() {
		return this.proxy;
	}

	public void setProxy(String proxy) {
		this.proxy = proxy;
	}

	public String getProxyPort() {
		return this.proxyPort;
	}

	public void setProxyPort(String proxyPort) {
		this.proxyPort = proxyPort;
	}

	public boolean isHasProxy() {
		return this.hasProxy;
	}

	public void setHasProxy(boolean hasProxy) {
		this.hasProxy = hasProxy;
	}

}
