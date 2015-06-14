package com.drogueria.dto;

import java.io.Serializable;

public class AlfabetaFileDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer nameFieldByteOffset;
	private Integer nameFieldLength;

	private Integer presentationFieldByteOffset;
	private Integer presentationFieldLength;

	private Integer priceFieldByteOffset;
	private Integer priceFieldLength;

	private Integer codeFieldByteOffset;
	private Integer codeFieldLength;

	private Integer gtinFieldByteOffset;
	private Integer gtinFieldLength;

	private Integer coldFieldByteOffset;
	private Integer coldFieldLength;

	public Integer getNameFieldByteOffset() {
		return this.nameFieldByteOffset;
	}

	public void setNameFieldByteOffset(Integer nameFieldByteOffset) {
		this.nameFieldByteOffset = nameFieldByteOffset;
	}

	public Integer getNameFieldLength() {
		return this.nameFieldLength;
	}

	public void setNameFieldLength(Integer nameFieldLength) {
		this.nameFieldLength = nameFieldLength;
	}

	public Integer getPresentationFieldByteOffset() {
		return this.presentationFieldByteOffset;
	}

	public void setPresentationFieldByteOffset(Integer presentationFieldByteOffset) {
		this.presentationFieldByteOffset = presentationFieldByteOffset;
	}

	public Integer getPresentationFieldLength() {
		return this.presentationFieldLength;
	}

	public void setPresentationFieldLength(Integer presentationFieldLength) {
		this.presentationFieldLength = presentationFieldLength;
	}

	public Integer getPriceFieldByteOffset() {
		return this.priceFieldByteOffset;
	}

	public void setPriceFieldByteOffset(Integer priceFieldByteOffset) {
		this.priceFieldByteOffset = priceFieldByteOffset;
	}

	public Integer getPriceFieldLength() {
		return this.priceFieldLength;
	}

	public void setPriceFieldLength(Integer priceFieldLength) {
		this.priceFieldLength = priceFieldLength;
	}

	public Integer getCodeFieldByteOffset() {
		return this.codeFieldByteOffset;
	}

	public void setCodeFieldByteOffset(Integer codeFieldByteOffset) {
		this.codeFieldByteOffset = codeFieldByteOffset;
	}

	public Integer getCodeFieldLength() {
		return this.codeFieldLength;
	}

	public void setCodeFieldLength(Integer codeFieldLength) {
		this.codeFieldLength = codeFieldLength;
	}

	public Integer getGtinFieldByteOffset() {
		return this.gtinFieldByteOffset;
	}

	public void setGtinFieldByteOffset(Integer gtinFieldByteOffset) {
		this.gtinFieldByteOffset = gtinFieldByteOffset;
	}

	public Integer getGtinFieldLength() {
		return this.gtinFieldLength;
	}

	public void setGtinFieldLength(Integer gtinFieldLength) {
		this.gtinFieldLength = gtinFieldLength;
	}

	public Integer getColdFieldByteOffset() {
		return this.coldFieldByteOffset;
	}

	public void setColdFieldByteOffset(Integer coldFieldByteOffset) {
		this.coldFieldByteOffset = coldFieldByteOffset;
	}

	public Integer getColdFieldLength() {
		return this.coldFieldLength;
	}

	public void setColdFieldLength(Integer coldFieldLength) {
		this.coldFieldLength = coldFieldLength;
	}
}