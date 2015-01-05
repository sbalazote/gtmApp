package com.drogueria.dto;

import java.io.Serializable;

public class AlfabetaFileDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer priceFieldByteOffset;
	private Integer priceFieldLength;

	private Integer codeFieldByteOffset;
	private Integer codeFieldLength;

	private Integer gtinFieldByteOffset;
	private Integer gtinFieldLength;

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

}
