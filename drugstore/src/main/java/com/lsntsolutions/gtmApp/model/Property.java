package com.lsntsolutions.gtmApp.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.lsntsolutions.gtmApp.helper.EncryptionHelper;

@Entity
@Table(name = "property")
public class Property implements Serializable {

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

	@Column(name = "days_ago_pending_transactions", nullable = false)
	private Integer daysAgoPendingTransactions;

	@Column(name = "last_tag", nullable = false)
	private Integer lastTag;

	@Column(name = "self_serialized_tag_filepath", nullable = false)
	private String selfSerializedTagFilepath;

	@Column(name = "ANMAT_name", nullable = false)
	private String ANMATName;

	@Column(name = "ANMAT_password", nullable = false)
	private String ANMATPassword;

	@ManyToOne
	@JoinColumn(name = "start_trace_concept_id")
	private Concept startTraceConcept;

	@ManyToOne
	@JoinColumn(name = "supplying_concept_id")
	private Concept supplyingConcept;

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

	public Integer getDaysAgoPendingTransactions() {
		return this.daysAgoPendingTransactions;
	}

	public void setDaysAgoPendingTransactions(Integer daysAgoPendingTransactions) {
		this.daysAgoPendingTransactions = daysAgoPendingTransactions;
	}

	public Integer getLastTag() {
		return this.lastTag;
	}

	public void setLastTag(Integer lastTag) {
		this.lastTag = lastTag;
	}

	public String getSelfSerializedTagFilepath() {
		return this.selfSerializedTagFilepath;
	}

	public void setSelfSerializedTagFilepath(String selfSerializedTagFilepath) {
		this.selfSerializedTagFilepath = selfSerializedTagFilepath;
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

	public Concept getSupplyingConcept() {
		return this.supplyingConcept;
	}

	public void setSupplyingConcept(Concept supplyingConcept) {
		this.supplyingConcept = supplyingConcept;
	}

	public String getDecryptPassword() {
		return EncryptionHelper.AESDecrypt(this.getANMATPassword());
	}

}