package br.com.samsung.service.impl;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import br.com.samsung.dto.saida.QuotationSaidaDTO;
import br.com.samsung.service.QuotationService;
import br.com.samsung.util.EvaluationAPI;

@Service
public class QuotationServiceImpl implements QuotationService {

	final static Logger logger = Logger.getLogger(QuotationServiceImpl.class);
	final static Long TIME_OUT_SESSAO_USUARIO = 1L;

	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public ResponseEntity<List<QuotationSaidaDTO>> findAllQuotation() {
		
		try {
			
			EvaluationAPI evaluationAPI = new EvaluationAPI();
			
			String returnListCurrencyString = evaluationAPI.searchURLAPI("QUOTATION");
			
	        Gson gson = new Gson();
	        
	        Type quotationListType = new TypeToken<ArrayList<QuotationSaidaDTO>>(){}.getType();
	        
	        ArrayList<QuotationSaidaDTO> arrayListQuotation = gson.fromJson(returnListCurrencyString, quotationListType);
	        
	        if (arrayListQuotation == null || arrayListQuotation.isEmpty()) {
				return new ResponseEntity<List<QuotationSaidaDTO>>(HttpStatus.NOT_FOUND);
	        }
	        
        	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/YYYY");
        	
        	for (QuotationSaidaDTO quotationSaidaDTO : arrayListQuotation) {
				quotationSaidaDTO.setDataHoraCotacaoFormat(simpleDateFormat.format(quotationSaidaDTO.getDataHoraCotacao()));
			}
	        
	        return new ResponseEntity<List<QuotationSaidaDTO>>(arrayListQuotation, HttpStatus.OK);
			
		} catch (Exception e) {
			return new ResponseEntity<List<QuotationSaidaDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}