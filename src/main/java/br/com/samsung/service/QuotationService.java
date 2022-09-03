package br.com.samsung.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import br.com.samsung.dto.saida.QuotationSaidaDTO;

public interface QuotationService {

	ResponseEntity<List<QuotationSaidaDTO>> findAllQuotation();
}