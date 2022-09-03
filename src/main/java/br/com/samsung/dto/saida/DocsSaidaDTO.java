package br.com.samsung.dto.saida;

import java.math.BigDecimal;
import java.util.Date;

public class DocsSaidaDTO {
	
	private Integer documentId;
	private Long documentNumber;
	private Long notaFiscal;
	private Date documentDate;
	private String documentDateFormat;
	private BigDecimal documentValue;
	private String currencyCode;
	private String currencyDesc;
	private BigDecimal valueUSD;
	private BigDecimal valuePEN;
	private BigDecimal valueBRL;

	public Integer getDocumentId() {
		return documentId;
	}
	public void setDocumentId(Integer documentId) {
		this.documentId = documentId;
	}
	public Long getDocumentNumber() {
		return documentNumber;
	}
	public void setDocumentNumber(Long documentNumber) {
		this.documentNumber = documentNumber;
	}
	public Long getNotaFiscal() {
		return notaFiscal;
	}
	public void setNotaFiscal(Long notaFiscal) {
		this.notaFiscal = notaFiscal;
	}
	public Date getDocumentDate() {
		return documentDate;
	}
	public void setDocumentDate(Date documentDate) {
		this.documentDate = documentDate;
	}
	public String getDocumentDateFormat() {
		return documentDateFormat;
	}
	public void setDocumentDateFormat(String documentDateFormat) {
		this.documentDateFormat = documentDateFormat;
	}
	public BigDecimal getDocumentValue() {
		return documentValue;
	}
	public void setDocumentValue(BigDecimal documentValue) {
		this.documentValue = documentValue;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getCurrencyDesc() {
		return currencyDesc;
	}
	public void setCurrencyDesc(String currencyDesc) {
		this.currencyDesc = currencyDesc;
	}
	public BigDecimal getValueUSD() {
		return valueUSD;
	}
	public void setValueUSD(BigDecimal valueUSD) {
		this.valueUSD = valueUSD;
	}
	public BigDecimal getValuePEN() {
		return valuePEN;
	}
	public void setValuePEN(BigDecimal valuePEN) {
		this.valuePEN = valuePEN;
	}
	public BigDecimal getValueBRL() {
		return valueBRL;
	}
	public void setValueBRL(BigDecimal valueBRL) {
		this.valueBRL = valueBRL;
	}
}