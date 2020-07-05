package com.kt.ibs.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.kt.ibs.entity.Currency;
import com.kt.ibs.exceptions.IBSException;
import com.kt.ibs.response.RestResponse;
import com.kt.ibs.security.JwtTokenUtil;
import com.kt.ibs.service.CurrencyService;

import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class CurrencyController {

	private static final String REQUEST = "REQUEST";
	private static final String RESPONSE = "RESPONSE";

	@Autowired
	private CurrencyService currencyService;

	@Value("${jwt.header}")
	private String tokenHeader;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	public CurrencyController() {
		super();
	}

	@RequestMapping(value = "/ibs/create-currency", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity createCurrency(@RequestBody @Valid Currency input,
			@ApiParam(value = "Authorization token", required = true) @RequestHeader("Authorization") String authorization)
			throws IBSException {

		log.info("IBS create currency : " + input);
		RestResponse restResponse = currencyService.createCurrency(input);
		return ResponseEntity.status(HttpStatus.OK).body(restResponse);
	}

	@RequestMapping(value = "/ibs/fetch-currencies", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<RestResponse> fetchCurrencies(
			@ApiParam(value = "Authorization token", required = true) @RequestHeader("Authorization") String authorization)
			throws IBSException {
		log.info("IBS fetch all currencies");

		RestResponse restResponse = currencyService.fetchCurrencies();
		return ResponseEntity.status(HttpStatus.OK).body(restResponse);
	}
}
