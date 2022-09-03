package br.com.samsung.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.samsung.dto.saida.CurrencySaidaDTO;
import br.com.samsung.service.CurrencyService;

@RestController
@RequestMapping(value = "/currency")
public class CurrencyController {

	@Autowired
	CurrencyService currencyService;
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<CurrencySaidaDTO>> findAllCurrency() throws Exception {
		return currencyService.findAllCurrency();
	}
}