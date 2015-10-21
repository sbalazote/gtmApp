package com.lsntsolutions.gtmApp.model;

import com.lsntsolutions.gtmApp.util.StringUtility;
import org.codehaus.jackson.annotate.JsonBackReference;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "provider")
public class Provider implements Serializable {

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

	@ManyToOne
	@JoinColumn(name = "VAT_liability_id", nullable = false)
	private VATLiability VATLiability;

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

	@Column(name = "active", nullable = false)
	private boolean active;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "provider_logistics_operator", joinColumns = @JoinColumn(name = "provider_id", nullable = false), inverseJoinColumns = @JoinColumn(name = "logistics_operator_id", nullable = false))
	private List<LogisticsOperator> logisticsOperators;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCode() {
		return this.code;
	}

	public String getFormatCode() {
		return StringUtility.addLeadingZeros(this.getCode().toString(), 5);
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

	public VATLiability getVATLiability() {
		return this.VATLiability;
	}

	public void setVATLiability(VATLiability vATLiability) {
		this.VATLiability = vATLiability;
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

	public boolean isActive() {
		return this.active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public List<LogisticsOperator> getLogisticsOperators() {
		return logisticsOperators;
	}

	public void setLogisticsOperators(List<LogisticsOperator> logisticsOperators) {
		this.logisticsOperators = logisticsOperators;
	}
}