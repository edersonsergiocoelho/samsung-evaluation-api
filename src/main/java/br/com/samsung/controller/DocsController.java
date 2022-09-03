package br.com.samsung.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.samsung.dto.saida.DocsSaidaDTO;
import br.com.samsung.entrada.dto.DocsEntradaDTO;
import br.com.samsung.service.DocsService;

@RestController
@RequestMapping(value = "/docs")
public class DocsController {

	@Autowired
	DocsService docsService;
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<DocsSaidaDTO>> findAllDocs() throws Exception {
		return docsService.findAllDocs();
	}
	
	@RequestMapping(value = "/filter", method = RequestMethod.POST)
	public ResponseEntity<List<DocsSaidaDTO>> findAllDocsToFilter(@RequestBody DocsEntradaDTO docsEntradaDTO) throws Exception {
		return docsService.findAllDocsToFilter(docsEntradaDTO);
	}
}