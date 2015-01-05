package com.drogueria.dto;

import java.io.Serializable;

public class RoleDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String description;
	private boolean selected;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isSelected() {
		return this.selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}
