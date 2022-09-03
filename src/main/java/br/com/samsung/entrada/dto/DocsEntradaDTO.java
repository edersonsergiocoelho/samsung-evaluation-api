package br.com.samsung.entrada.dto;

import java.util.Date;

public class DocsEntradaDTO {
	
	private Long documentNumber;
	private String currencyCode;
	private Date documentDateInicio;
	private Date documentDateFim;

	public Long getDocumentNumber() {
		return documentNumber;
	}
	public void setDocumentNumber(Long documentNumber) {
		this.documentNumber = documentNumber;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public Date getDocumentDateInicio() {
		return documentDateInicio;
	}
	public void setDocumentDateInicio(Date documentDateInicio) {
		this.documentDateInicio = documentDateInicio;
	}
	public Date getDocumentDateFim() {
		return documentDateFim;
	}
	public void setDocumentDateFim(Date documentDateFim) {
		this.documentDateFim = documentDateFim;
	}
}
