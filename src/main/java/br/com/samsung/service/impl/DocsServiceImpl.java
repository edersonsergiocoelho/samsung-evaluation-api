package br.com.samsung.service.impl;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import br.com.samsung.dto.saida.CurrencySaidaDTO;
import br.com.samsung.dto.saida.DocsSaidaDTO;
import br.com.samsung.dto.saida.QuotationSaidaDTO;
import br.com.samsung.entrada.dto.DocsEntradaDTO;
import br.com.samsung.service.CurrencyService;
import br.com.samsung.service.DocsService;
import br.com.samsung.service.QuotationService;
import br.com.samsung.util.EvaluationAPI;

@Service
public class DocsServiceImpl implements DocsService {

	final static Logger logger = Logger.getLogger(DocsServiceImpl.class);

	@Autowired
	CurrencyService currencyService;
	
	@Autowired
	QuotationService quotationService;
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public ResponseEntity<List<DocsSaidaDTO>> findAllDocs() {
		
		try {
			
			EvaluationAPI evaluationAPI = new EvaluationAPI();
			
			String returnListDocsString = evaluationAPI.searchURLAPI("DOCS");
			
	        Gson gson = new Gson();
	        
	        Type docsListType = new TypeToken<ArrayList<DocsSaidaDTO>>(){}.getType();
	        
	        ArrayList<DocsSaidaDTO> arrayListDocs = gson.fromJson(returnListDocsString, docsListType);
	        
	        if (arrayListDocs == null || arrayListDocs.isEmpty()) {
				return new ResponseEntity<List<DocsSaidaDTO>>(HttpStatus.NOT_FOUND);
	        }
	        
        	ResponseEntity<List<CurrencySaidaDTO>> responseEntityListCurrencySaidaDTO = currencyService.findAllCurrency();
        	List<CurrencySaidaDTO> listCurrencySaidaDTO = responseEntityListCurrencySaidaDTO.getBody();
        	
        	ResponseEntity<List<QuotationSaidaDTO>> responseEntityListQuotationSaidaDTO = quotationService.findAllQuotation();
        	List<QuotationSaidaDTO> listQuotationSaidaDTO = responseEntityListQuotationSaidaDTO.getBody();
	        
        	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/YYYY");
        	
	        for (DocsSaidaDTO docsSaidaDTO : arrayListDocs) {
	        	
	        	docsSaidaDTO.setDocumentDateFormat(simpleDateFormat.format(docsSaidaDTO.getDocumentDate()));
	        	
	        	for (CurrencySaidaDTO currencySaidaDTO : listCurrencySaidaDTO) {

	        		if (docsSaidaDTO.getCurrencyCode().equalsIgnoreCase(currencySaidaDTO.getCurrencyCode())) {
	        			docsSaidaDTO.setCurrencyDesc(currencySaidaDTO.getCurrencyDesc());
	        		}
				}
	        	
	        	List<QuotationSaidaDTO> listQuotationCotacoesRecentesSaidaDTO = listQuotationSaidaDTO.stream()
	        																						 .filter(q -> q.getFromCurrencyCode().equalsIgnoreCase(docsSaidaDTO.getCurrencyCode()) &&
	        																								 q.getDataHoraCotacao().after(docsSaidaDTO.getDocumentDate()))
	        																						 .collect(Collectors.toList());

	        	if (listQuotationCotacoesRecentesSaidaDTO != null && ! listQuotationCotacoesRecentesSaidaDTO.isEmpty()) {
	        		
        			List<QuotationSaidaDTO> listQuotationSaidaDTOUSD = listQuotationCotacoesRecentesSaidaDTO.stream().filter(q -> q.getToCurrencyCode().equalsIgnoreCase("USD")).collect(Collectors.toList());
        			List<QuotationSaidaDTO> listQuotationSaidaDTOPEN = listQuotationCotacoesRecentesSaidaDTO.stream().filter(q -> q.getToCurrencyCode().equalsIgnoreCase("PEN")).collect(Collectors.toList());
        			List<QuotationSaidaDTO> listQuotationSaidaDTOBRL = listQuotationCotacoesRecentesSaidaDTO.stream().filter(q -> q.getToCurrencyCode().equalsIgnoreCase("BRL")).collect(Collectors.toList());
        			
        			if (listQuotationSaidaDTOUSD != null && ! listQuotationSaidaDTOUSD.isEmpty()) {
        				
        				QuotationSaidaDTO quotationSaidaDTOUSD = listQuotationSaidaDTOUSD.stream().max(Comparator.comparing(QuotationSaidaDTO::getDataHoraCotacao)).get();
            			
            			if (quotationSaidaDTOUSD != null) {
            				docsSaidaDTO.setValueUSD(docsSaidaDTO.getDocumentValue().multiply(quotationSaidaDTOUSD.getCotacao()));
            			} else {
            				docsSaidaDTO.setValueUSD(docsSaidaDTO.getDocumentValue());
            			}
        			} else {
        				docsSaidaDTO.setValueUSD(docsSaidaDTO.getDocumentValue());
        			}
        			
        			if (listQuotationSaidaDTOPEN != null && ! listQuotationSaidaDTOPEN.isEmpty()) {
        				
        				QuotationSaidaDTO quotationSaidaDTOPEN = listQuotationSaidaDTOPEN.stream().max(Comparator.comparing(QuotationSaidaDTO::getDataHoraCotacao)).get();
            			
            			if (quotationSaidaDTOPEN != null) {
            				docsSaidaDTO.setValuePEN(docsSaidaDTO.getDocumentValue().multiply(quotationSaidaDTOPEN.getCotacao()));
            			} else {
            				docsSaidaDTO.setValuePEN(docsSaidaDTO.getDocumentValue());
            			}     				
        			} else {
        				docsSaidaDTO.setValuePEN(docsSaidaDTO.getDocumentValue());
        			}
        			
        			if (listQuotationSaidaDTOBRL != null && ! listQuotationSaidaDTOBRL.isEmpty()) {
        				
        				QuotationSaidaDTO quotationSaidaDTOBRL = listQuotationSaidaDTOBRL.stream().max(Comparator.comparing(QuotationSaidaDTO::getDataHoraCotacao)).get();
            			
            			if (quotationSaidaDTOBRL != null) {
            				docsSaidaDTO.setValueBRL(docsSaidaDTO.getDocumentValue().multiply(quotationSaidaDTOBRL.getCotacao()));
            			} else {
            				docsSaidaDTO.setValueBRL(docsSaidaDTO.getDocumentValue());
            			}       				
        			} else {
        				docsSaidaDTO.setValueBRL(docsSaidaDTO.getDocumentValue());
        			}
        			
	        	} else {
	        		
		        	List<QuotationSaidaDTO> listQuotationCotacoesAntigasSaidaDTO = listQuotationSaidaDTO.stream()
							 																			.filter(q -> q.getFromCurrencyCode().equalsIgnoreCase(docsSaidaDTO.getCurrencyCode()) &&
							 																					q.getDataHoraCotacao().before(docsSaidaDTO.getDocumentDate()))
							 																			.collect(Collectors.toList());
	        		
		        	if (listQuotationCotacoesAntigasSaidaDTO != null && ! listQuotationCotacoesAntigasSaidaDTO.isEmpty()) {
		        		
	        			List<QuotationSaidaDTO> listQuotationSaidaDTOUSD = listQuotationCotacoesAntigasSaidaDTO.stream().filter(q -> q.getToCurrencyCode().equalsIgnoreCase("USD")).collect(Collectors.toList());
	        			List<QuotationSaidaDTO> listQuotationSaidaDTOPEN = listQuotationCotacoesAntigasSaidaDTO.stream().filter(q -> q.getToCurrencyCode().equalsIgnoreCase("PEN")).collect(Collectors.toList());
	        			List<QuotationSaidaDTO> listQuotationSaidaDTOBRL = listQuotationCotacoesAntigasSaidaDTO.stream().filter(q -> q.getToCurrencyCode().equalsIgnoreCase("BRL")).collect(Collectors.toList());
	        			
	        			if (listQuotationSaidaDTOUSD != null && ! listQuotationSaidaDTOUSD.isEmpty()) {
	        				
	        				QuotationSaidaDTO quotationSaidaDTOUSD = listQuotationSaidaDTOUSD.stream().max(Comparator.comparing(QuotationSaidaDTO::getDataHoraCotacao)).get();
	            			
	            			if (quotationSaidaDTOUSD != null) {
	            				docsSaidaDTO.setValueUSD(docsSaidaDTO.getDocumentValue().multiply(quotationSaidaDTOUSD.getCotacao()));
	            			} else {
	            				docsSaidaDTO.setValueUSD(docsSaidaDTO.getDocumentValue());
	            			}
	        			} else {
	        				docsSaidaDTO.setValueUSD(docsSaidaDTO.getDocumentValue());
	        			}
	        			
	        			if (listQuotationSaidaDTOPEN != null && ! listQuotationSaidaDTOPEN.isEmpty()) {
	        				
	        				QuotationSaidaDTO quotationSaidaDTOPEN = listQuotationSaidaDTOPEN.stream().max(Comparator.comparing(QuotationSaidaDTO::getDataHoraCotacao)).get();
	            			
	            			if (quotationSaidaDTOPEN != null) {
	            				docsSaidaDTO.setValuePEN(docsSaidaDTO.getDocumentValue().multiply(quotationSaidaDTOPEN.getCotacao()));
	            			} else {
	            				docsSaidaDTO.setValuePEN(docsSaidaDTO.getDocumentValue());
	            			}     				
	        			} else {
	        				docsSaidaDTO.setValuePEN(docsSaidaDTO.getDocumentValue());
	        			}
	        			
	        			if (listQuotationSaidaDTOBRL != null && ! listQuotationSaidaDTOBRL.isEmpty()) {
	        				
	        				QuotationSaidaDTO quotationSaidaDTOBRL = listQuotationSaidaDTOBRL.stream().max(Comparator.comparing(QuotationSaidaDTO::getDataHoraCotacao)).get();
	            			
	            			if (quotationSaidaDTOBRL != null) {
	            				docsSaidaDTO.setValueBRL(docsSaidaDTO.getDocumentValue().multiply(quotationSaidaDTOBRL.getCotacao()));
	            			} else {
	            				docsSaidaDTO.setValueBRL(docsSaidaDTO.getDocumentValue());
	            			}       				
	        			} else {
	        				docsSaidaDTO.setValueBRL(docsSaidaDTO.getDocumentValue());
	        			}
		        	}
	        	}
			}
	        
	        return new ResponseEntity<List<DocsSaidaDTO>>(arrayListDocs, HttpStatus.OK);
			
		} catch (Exception e) {
			return new ResponseEntity<List<DocsSaidaDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public ResponseEntity<List<DocsSaidaDTO>> findAllDocsToFilter(DocsEntradaDTO docsEntradaDTO) {
		
		try {
			
			EvaluationAPI evaluationAPI = new EvaluationAPI();
			
			String returnListDocsString = evaluationAPI.searchURLAPI("DOCS");
			
	        Gson gson = new Gson();
	        
	        Type docsListType = new TypeToken<ArrayList<DocsSaidaDTO>>(){}.getType();
	        
	        ArrayList<DocsSaidaDTO> arrayListDocs = gson.fromJson(returnListDocsString, docsListType);
	        
	        if (arrayListDocs == null || arrayListDocs.isEmpty()) {
				return new ResponseEntity<List<DocsSaidaDTO>>(HttpStatus.NOT_FOUND);
	        }
	        
        	ResponseEntity<List<CurrencySaidaDTO>> responseEntityListCurrencySaidaDTO = currencyService.findAllCurrency();
        	List<CurrencySaidaDTO> listCurrencySaidaDTO = responseEntityListCurrencySaidaDTO.getBody();
        	
        	ResponseEntity<List<QuotationSaidaDTO>> responseEntityListQuotationSaidaDTO = quotationService.findAllQuotation();
        	List<QuotationSaidaDTO> listQuotationSaidaDTO = responseEntityListQuotationSaidaDTO.getBody();
	        
        	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/YYYY");
        	
	        for (DocsSaidaDTO docsSaidaDTO : arrayListDocs) {
	        	
	        	docsSaidaDTO.setDocumentDateFormat(simpleDateFormat.format(docsSaidaDTO.getDocumentDate()));
	        	
	        	for (CurrencySaidaDTO currencySaidaDTO : listCurrencySaidaDTO) {

	        		if (docsSaidaDTO.getCurrencyCode().equalsIgnoreCase(currencySaidaDTO.getCurrencyCode())) {
	        			docsSaidaDTO.setCurrencyDesc(currencySaidaDTO.getCurrencyDesc());
	        		}
				}
	        	
	        	List<QuotationSaidaDTO> listQuotationCotacoesRecentesSaidaDTO = listQuotationSaidaDTO.stream()
	        																						 .filter(q -> q.getFromCurrencyCode().equalsIgnoreCase(docsSaidaDTO.getCurrencyCode()) &&
	        																								 q.getDataHoraCotacao().after(docsSaidaDTO.getDocumentDate()))
	        																						 .collect(Collectors.toList());

	        	if (listQuotationCotacoesRecentesSaidaDTO != null && ! listQuotationCotacoesRecentesSaidaDTO.isEmpty()) {
	        		
        			List<QuotationSaidaDTO> listQuotationSaidaDTOUSD = listQuotationCotacoesRecentesSaidaDTO.stream().filter(q -> q.getToCurrencyCode().equalsIgnoreCase("USD")).collect(Collectors.toList());
        			List<QuotationSaidaDTO> listQuotationSaidaDTOPEN = listQuotationCotacoesRecentesSaidaDTO.stream().filter(q -> q.getToCurrencyCode().equalsIgnoreCase("PEN")).collect(Collectors.toList());
        			List<QuotationSaidaDTO> listQuotationSaidaDTOBRL = listQuotationCotacoesRecentesSaidaDTO.stream().filter(q -> q.getToCurrencyCode().equalsIgnoreCase("BRL")).collect(Collectors.toList());
        			
        			if (listQuotationSaidaDTOUSD != null && ! listQuotationSaidaDTOUSD.isEmpty()) {
        				
        				QuotationSaidaDTO quotationSaidaDTOUSD = listQuotationSaidaDTOUSD.stream().max(Comparator.comparing(QuotationSaidaDTO::getDataHoraCotacao)).get();
            			
            			if (quotationSaidaDTOUSD != null) {
            				docsSaidaDTO.setValueUSD(docsSaidaDTO.getDocumentValue().multiply(quotationSaidaDTOUSD.getCotacao()));
            			} else {
            				docsSaidaDTO.setValueUSD(docsSaidaDTO.getDocumentValue());
            			}
        			} else {
        				docsSaidaDTO.setValueUSD(docsSaidaDTO.getDocumentValue());
        			}
        			
        			if (listQuotationSaidaDTOPEN != null && ! listQuotationSaidaDTOPEN.isEmpty()) {
        				
        				QuotationSaidaDTO quotationSaidaDTOPEN = listQuotationSaidaDTOPEN.stream().max(Comparator.comparing(QuotationSaidaDTO::getDataHoraCotacao)).get();
            			
            			if (quotationSaidaDTOPEN != null) {
            				docsSaidaDTO.setValuePEN(docsSaidaDTO.getDocumentValue().multiply(quotationSaidaDTOPEN.getCotacao()));
            			} else {
            				docsSaidaDTO.setValuePEN(docsSaidaDTO.getDocumentValue());
            			}     				
        			} else {
        				docsSaidaDTO.setValuePEN(docsSaidaDTO.getDocumentValue());
        			}
        			
        			if (listQuotationSaidaDTOBRL != null && ! listQuotationSaidaDTOBRL.isEmpty()) {
        				
        				QuotationSaidaDTO quotationSaidaDTOBRL = listQuotationSaidaDTOBRL.stream().max(Comparator.comparing(QuotationSaidaDTO::getDataHoraCotacao)).get();
            			
            			if (quotationSaidaDTOBRL != null) {
            				docsSaidaDTO.setValueBRL(docsSaidaDTO.getDocumentValue().multiply(quotationSaidaDTOBRL.getCotacao()));
            			} else {
            				docsSaidaDTO.setValueBRL(docsSaidaDTO.getDocumentValue());
            			}       				
        			} else {
        				docsSaidaDTO.setValueBRL(docsSaidaDTO.getDocumentValue());
        			}
        			
	        	} else {
	        		
		        	List<QuotationSaidaDTO> listQuotationCotacoesAntigasSaidaDTO = listQuotationSaidaDTO.stream()
							 																			.filter(q -> q.getFromCurrencyCode().equalsIgnoreCase(docsSaidaDTO.getCurrencyCode()) &&
							 																					q.getDataHoraCotacao().before(docsSaidaDTO.getDocumentDate()))
							 																			.collect(Collectors.toList());
	        		
		        	if (listQuotationCotacoesAntigasSaidaDTO != null && ! listQuotationCotacoesAntigasSaidaDTO.isEmpty()) {
		        		
	        			List<QuotationSaidaDTO> listQuotationSaidaDTOUSD = listQuotationCotacoesAntigasSaidaDTO.stream().filter(q -> q.getToCurrencyCode().equalsIgnoreCase("USD")).collect(Collectors.toList());
	        			List<QuotationSaidaDTO> listQuotationSaidaDTOPEN = listQuotationCotacoesAntigasSaidaDTO.stream().filter(q -> q.getToCurrencyCode().equalsIgnoreCase("PEN")).collect(Collectors.toList());
	        			List<QuotationSaidaDTO> listQuotationSaidaDTOBRL = listQuotationCotacoesAntigasSaidaDTO.stream().filter(q -> q.getToCurrencyCode().equalsIgnoreCase("BRL")).collect(Collectors.toList());
	        			
	        			if (listQuotationSaidaDTOUSD != null && ! listQuotationSaidaDTOUSD.isEmpty()) {
	        				
	        				QuotationSaidaDTO quotationSaidaDTOUSD = listQuotationSaidaDTOUSD.stream().max(Comparator.comparing(QuotationSaidaDTO::getDataHoraCotacao)).get();
	            			
	            			if (quotationSaidaDTOUSD != null) {
	            				docsSaidaDTO.setValueUSD(docsSaidaDTO.getDocumentValue().multiply(quotationSaidaDTOUSD.getCotacao()));
	            			} else {
	            				docsSaidaDTO.setValueUSD(docsSaidaDTO.getDocumentValue());
	            			}
	        			} else {
	        				docsSaidaDTO.setValueUSD(docsSaidaDTO.getDocumentValue());
	        			}
	        			
	        			if (listQuotationSaidaDTOPEN != null && ! listQuotationSaidaDTOPEN.isEmpty()) {
	        				
	        				QuotationSaidaDTO quotationSaidaDTOPEN = listQuotationSaidaDTOPEN.stream().max(Comparator.comparing(QuotationSaidaDTO::getDataHoraCotacao)).get();
	            			
	            			if (quotationSaidaDTOPEN != null) {
	            				docsSaidaDTO.setValuePEN(docsSaidaDTO.getDocumentValue().multiply(quotationSaidaDTOPEN.getCotacao()));
	            			} else {
	            				docsSaidaDTO.setValuePEN(docsSaidaDTO.getDocumentValue());
	            			}     				
	        			} else {
	        				docsSaidaDTO.setValuePEN(docsSaidaDTO.getDocumentValue());
	        			}
	        			
	        			if (listQuotationSaidaDTOBRL != null && ! listQuotationSaidaDTOBRL.isEmpty()) {
	        				
	        				QuotationSaidaDTO quotationSaidaDTOBRL = listQuotationSaidaDTOBRL.stream().max(Comparator.comparing(QuotationSaidaDTO::getDataHoraCotacao)).get();
	            			
	            			if (quotationSaidaDTOBRL != null) {
	            				docsSaidaDTO.setValueBRL(docsSaidaDTO.getDocumentValue().multiply(quotationSaidaDTOBRL.getCotacao()));
	            			} else {
	            				docsSaidaDTO.setValueBRL(docsSaidaDTO.getDocumentValue());
	            			}       				
	        			} else {
	        				docsSaidaDTO.setValueBRL(docsSaidaDTO.getDocumentValue());
	        			}
		        	}
	        	}
			}
	        
	        Predicate<DocsSaidaDTO> predicateDocumentNumber = null;
	        Predicate<DocsSaidaDTO> predicateCurrencyCode = null;
	        Predicate<DocsSaidaDTO> predicateDocumentDate = null;
	        
	        Stream<DocsSaidaDTO> streamDocsSaidaDTO = arrayListDocs.stream();
	        
	        if (docsEntradaDTO.getDocumentNumber() != null && docsEntradaDTO.getDocumentNumber() != 0) {
	            predicateDocumentNumber = d -> d.getDocumentNumber().equals(docsEntradaDTO.getDocumentNumber());
	            streamDocsSaidaDTO = streamDocsSaidaDTO.filter(predicateDocumentNumber);
	        }
	        
	        if (docsEntradaDTO.getCurrencyCode() != null && ! docsEntradaDTO.getCurrencyCode().isEmpty()) {
	        	predicateCurrencyCode = d -> d.getCurrencyCode().equals(docsEntradaDTO.getCurrencyCode());
	            streamDocsSaidaDTO = streamDocsSaidaDTO.filter(predicateCurrencyCode);
	        }

	        if (docsEntradaDTO.getDocumentDateInicio() != null && docsEntradaDTO.getDocumentDateFim() != null) {
	        	
	        	Calendar calendarDocumentDateInicio = Calendar.getInstance();
	        	calendarDocumentDateInicio.setTime(docsEntradaDTO.getDocumentDateInicio());
	        	calendarDocumentDateInicio.add(Calendar.DATE, -1);
	        	Date documentDateInicio = calendarDocumentDateInicio.getTime();
	        	
	        	Calendar calendarDocumentDateFim = Calendar.getInstance();
	        	calendarDocumentDateFim.setTime(docsEntradaDTO.getDocumentDateFim());
	        	calendarDocumentDateFim.add(Calendar.DATE, +1);
	        	Date documentDateFim = calendarDocumentDateFim.getTime();
	        	
	        	predicateDocumentDate = d -> d.getDocumentDate().after(documentDateInicio) && d.getDocumentDate().before(documentDateFim); 
	            streamDocsSaidaDTO = streamDocsSaidaDTO.filter(predicateDocumentDate);
	        }
	        
	        List<DocsSaidaDTO> listFilterDocsSaidaDTO = streamDocsSaidaDTO.collect(Collectors.toList());
	        
	        return new ResponseEntity<List<DocsSaidaDTO>>(listFilterDocsSaidaDTO, HttpStatus.OK);
			
		} catch (Exception e) {
			return new ResponseEntity<List<DocsSaidaDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}