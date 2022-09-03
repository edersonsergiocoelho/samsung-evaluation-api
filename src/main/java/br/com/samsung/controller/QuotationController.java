package br.com.samsung.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.samsung.dto.saida.QuotationSaidaDTO;
import br.com.samsung.service.QuotationService;

@RestController
@RequestMapping(value = "/quotation")
public class QuotationController {

	@Autowired
	QuotationService quotationService;
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<QuotationSaidaDTO>> findAllQuotation() throws Exception {
		return quotationService.findAllQuotation();
	}
}