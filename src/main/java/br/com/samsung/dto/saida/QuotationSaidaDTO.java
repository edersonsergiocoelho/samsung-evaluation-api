package br.com.samsung.dto.saida;

import java.math.BigDecimal;
import java.util.Date;

public class QuotationSaidaDTO {
	
	private String fromCurrencyCode;
	private String toCurrencyCode;
	private BigDecimal cotacao;
	private Date dataHoraCotacao;
	private String dataHoraCotacaoFormat;
	
	public String getFromCurrencyCode() {
		return fromCurrencyCode;
	}
	public void setFromCurrencyCode(String fromCurrencyCode) {
		this.fromCurrencyCode = fromCurrencyCode;
	}
	public String getToCurrencyCode() {
		return toCurrencyCode;
	}
	public void setToCurrencyCode(String toCurrencyCode) {
		this.toCurrencyCode = toCurrencyCode;
	}
	public BigDecimal getCotacao() {
		return cotacao;
	}
	public void setCotacao(BigDecimal cotacao) {
		this.cotacao = cotacao;
	}
	public Date getDataHoraCotacao() {
		return dataHoraCotacao;
	}
	public void setDataHoraCotacao(Date dataHoraCotacao) {
		this.dataHoraCotacao = dataHoraCotacao;
	}
	public String getDataHoraCotacaoFormat() {
		return dataHoraCotacaoFormat;
	}
	public void setDataHoraCotacaoFormat(String dataHoraCotacaoFormat) {
		this.dataHoraCotacaoFormat = dataHoraCotacaoFormat;
	}
}