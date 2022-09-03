package br.com.samsung.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import br.com.samsung.dto.saida.DocsSaidaDTO;
import br.com.samsung.entrada.dto.DocsEntradaDTO;

public interface DocsService {

	ResponseEntity<List<DocsSaidaDTO>> findAllDocs();
	ResponseEntity<List<DocsSaidaDTO>> findAllDocsToFilter(DocsEntradaDTO docsEntradaDTO);
}