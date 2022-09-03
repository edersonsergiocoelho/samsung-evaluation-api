package br.com.samsung.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import br.com.samsung.dto.saida.CurrencySaidaDTO;

public interface CurrencyService {

	ResponseEntity<List<CurrencySaidaDTO>> findAllCurrency();
}