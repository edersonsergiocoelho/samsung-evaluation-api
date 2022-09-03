package br.com.samsung.service.impl;

import java.lang.reflect.Type;
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

import br.com.samsung.dto.saida.CurrencySaidaDTO;
import br.com.samsung.service.CurrencyService;
import br.com.samsung.util.EvaluationAPI;

@Service
public class CurrencyServiceImpl implements CurrencyService {

	final static Logger logger = Logger.getLogger(CurrencyServiceImpl.class);
	final static Long TIME_OUT_SESSAO_USUARIO = 1L;

	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public ResponseEntity<List<CurrencySaidaDTO>> findAllCurrency() {
		
		try {
			
			EvaluationAPI evaluationAPI = new EvaluationAPI();
			
			String returnListCurrencyString = evaluationAPI.searchURLAPI("CURRENCY");
			
	        Gson gson = new Gson();
	        
	        Type currencyListType = new TypeToken<ArrayList<CurrencySaidaDTO>>(){}.getType();
	        
	        ArrayList<CurrencySaidaDTO> arrayListCurrency = gson.fromJson(returnListCurrencyString, currencyListType);
	        
	        if (arrayListCurrency == null || arrayListCurrency.isEmpty()) {
				return new ResponseEntity<List<CurrencySaidaDTO>>(HttpStatus.NOT_FOUND);
	        }
	        
	        return new ResponseEntity<List<CurrencySaidaDTO>>(arrayListCurrency, HttpStatus.OK);
			
		} catch (Exception e) {
			return new ResponseEntity<List<CurrencySaidaDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}