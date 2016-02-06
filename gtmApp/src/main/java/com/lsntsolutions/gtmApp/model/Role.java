package com.lsntsolutions.gtmApp.model;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "role")
public class Role implements Serializable, GrantedAuthority {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "code", unique = true, nullable = false)
	private String code;

	@Column(name = "description", nullable = false)
	private String description;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String getAuthority() {
		return this.getCode();
	}

	@Override
	public String toString() {
		return "ROL{" + id  + ": " + description.toUpperCase() + "}";
	}
}
