package pe.com.aldesa.aduanero.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import pe.com.aldesa.aduanero.constant.ApiError;
import pe.com.aldesa.aduanero.dto.ApiResponse;
import pe.com.aldesa.aduanero.entity.TipoBulto;
import pe.com.aldesa.aduanero.exception.ApiException;
import pe.com.aldesa.aduanero.repository.TipoBultoRepository;

@Service
public class TipoBultoService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private TipoBultoRepository tipoBultoRepository;
	
	@Autowired
	public TipoBultoService(TipoBultoRepository tipoBultoRepository) {
		this.tipoBultoRepository = tipoBultoRepository;
	}
	
	public ApiResponse findAll() throws ApiException {
		List<TipoBulto> tiposBultos = tipoBultoRepository.findAll();
		int total = tiposBultos.size();
		logger.debug("Total Tipo de bultos: {}", total);
		if (tiposBultos.isEmpty()) {
			throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), tiposBultos, total);
	}
	
	public ApiResponse findById(Integer id) throws ApiException {
		TipoBulto tmpTipBulto = tipoBultoRepository.findById(id).orElse(null);
		logger.debug("Tipo bulto: {}", tmpTipBulto);
		if (null == tmpTipBulto) {
			throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), tmpTipBulto);
	}
	
	public ApiResponse save(String request) throws ApiException {
		TipoBulto responseTipoBulto;
		
		JsonNode root;
		String	nombre = null;
		String	abreviatura = null;
		try {
			root = new ObjectMapper().readTree(request);
			
			nombre = root.path("nombre").asText();
			logger.debug("nombre: {}", nombre);
			
			abreviatura = root.path("abreviatura").asText();
			logger.debug("abreviatura: {}", abreviatura);
			
		} catch (JsonProcessingException e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		
		if (StringUtils.isBlank(nombre) || StringUtils.isBlank(abreviatura)) {
			throw new ApiException(ApiError.EMPTY_OR_NULL_PARAMETER.getCode(), ApiError.EMPTY_OR_NULL_PARAMETER.getMessage());
		}
		
		try {
			TipoBulto tipoBulto = new TipoBulto();
			tipoBulto.setNombre(nombre);
			tipoBulto.setAbreviatura(abreviatura.toUpperCase());
			
			responseTipoBulto = tipoBultoRepository.save(tipoBulto);
			logger.debug("Tipo bulto guardado");
		} catch (Exception e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), responseTipoBulto);
	}
	
	public ApiResponse update(String request) throws ApiException {
		TipoBulto responseTipoBulto;
		
		JsonNode root;
		Integer	id = null;
		String	nombre = null;
		String	abreviatura = null;
		try {
			root = new ObjectMapper().readTree(request);
			
			id = root.path("id").asInt();
			logger.debug("id: {}", id);
			
			nombre = root.path("nombre").asText();
			logger.debug("nombre: {}", nombre);
			
			abreviatura = root.path("abreviatura").asText();
			logger.debug("abreviatura: {}", abreviatura);
			
		} catch (JsonProcessingException e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		
		if (null == id || id == 0 || StringUtils.isBlank(nombre) || StringUtils.isBlank(abreviatura)) {
			throw new ApiException(ApiError.EMPTY_OR_NULL_PARAMETER.getCode(), ApiError.EMPTY_OR_NULL_PARAMETER.getMessage());
		}
		
		try {
			TipoBulto tipoBulto = new TipoBulto();
			tipoBulto.setIdTipoBulto(id);
			tipoBulto.setNombre(nombre);
			tipoBulto.setAbreviatura(abreviatura.toUpperCase());
			
			responseTipoBulto = tipoBultoRepository.save(tipoBulto);
			logger.debug("Tipo bulto actualizado");
		} catch (Exception e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), responseTipoBulto);
	}
	
	public ApiResponse delete(Integer id) throws ApiException {
		TipoBulto tmpTipoBulto = tipoBultoRepository.findById(id).orElse(null);
		logger.debug("Tipo bulto: {}", tmpTipoBulto);
		if (null != tmpTipoBulto) {
			tipoBultoRepository.deleteById(id);
			logger.debug("Tipo bulto eliminado");
			return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), "Tipo bulto " + id + " eliminado");
		}
		throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
	}
	
}
