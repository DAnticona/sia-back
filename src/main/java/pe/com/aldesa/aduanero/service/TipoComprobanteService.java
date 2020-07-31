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
import pe.com.aldesa.aduanero.entity.Moneda;
import pe.com.aldesa.aduanero.entity.TipoComprobante;
import pe.com.aldesa.aduanero.exception.ApiException;
import pe.com.aldesa.aduanero.repository.MonedaRepository;
import pe.com.aldesa.aduanero.repository.TipoComprobanteRepository;

@Service
public class TipoComprobanteService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private TipoComprobanteRepository tipoComprobanteRepository;
	
	@Autowired
	public TipoComprobanteService(TipoComprobanteRepository tipoComprobanteRepository) {
		this.tipoComprobanteRepository = tipoComprobanteRepository;
	}
	
	public ApiResponse findAll() throws ApiException {
		List<TipoComprobante> tiposComprobantes = tipoComprobanteRepository.findAll();
		int total = tiposComprobantes.size();
		logger.debug("Total tipos Comprobantes: {}", total);
		if (tiposComprobantes.isEmpty()) {
			throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), tiposComprobantes, total);
	}
	
	public ApiResponse findById(Integer id) throws ApiException {
		TipoComprobante tmpTipCompro = tipoComprobanteRepository.findById(id).orElse(null);
		logger.debug("Tipo Comprobante: {}", tmpTipCompro);
		if (null == tmpTipCompro) {
			throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), tmpTipCompro);
	}
	
	public ApiResponse save(String request) throws ApiException {
		TipoComprobante responseTipoComprobante;
		
		JsonNode root;
		String	nombre = null;
		String	abreviatura = null;
		Integer	tipoSunat = null;
		try {
			root = new ObjectMapper().readTree(request);
			
			nombre = root.path("nombre").asText();
			logger.debug("nombre: {}", nombre);
			
			abreviatura = root.path("abreviatura").asText();
			logger.debug("abreviatura: {}", abreviatura);
			
			tipoSunat = root.path("tipoSunat").asInt();
			logger.debug("tipoSunat: {}", tipoSunat);
			
		} catch (JsonProcessingException e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		
		if (StringUtils.isBlank(nombre) || StringUtils.isBlank(abreviatura)) {
			throw new ApiException(ApiError.EMPTY_OR_NULL_PARAMETER.getCode(), ApiError.EMPTY_OR_NULL_PARAMETER.getMessage());
		}
		
		try {
			TipoComprobante tipoComprobante = new TipoComprobante();
			tipoComprobante.setNombre(nombre);
			tipoComprobante.setAbreviatura(abreviatura.toUpperCase());
			tipoComprobante.setTipoSunat(tipoSunat);
			
			responseTipoComprobante = tipoComprobanteRepository.save(tipoComprobante);
			logger.debug("Tipo comprobante guardado");
		} catch (Exception e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), responseTipoComprobante);
	}
	
//	public ApiResponse update(String request) throws ApiException {
//		TipoComprobante responseTipoComprobante;
//		
//		JsonNode root;
//		Integer	id = null;
//		String	nombre = null;
//		String	simbolo = null;
//		String	abreviatura = null;
//		try {
//			root = new ObjectMapper().readTree(request);
//			
//			id = root.path("id").asInt();
//			logger.debug("id: {}", id);
//			
//			nombre = root.path("nombre").asText();
//			logger.debug("nombre: {}", nombre);
//			
//			simbolo = root.path("simbolo").asText();
//			logger.debug("simbolo: {}", simbolo);
//			
//			abreviatura = root.path("abreviatura").asText();
//			logger.debug("abreviatura: {}", abreviatura);
//			
//		} catch (JsonProcessingException e) {
//			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
//		}
//		
//		if (null == id || id == 0 || StringUtils.isBlank(nombre) || StringUtils.isBlank(simbolo) || StringUtils.isBlank(abreviatura)) {
//			throw new ApiException(ApiError.EMPTY_OR_NULL_PARAMETER.getCode(), ApiError.EMPTY_OR_NULL_PARAMETER.getMessage());
//		}
//		
//		boolean existsMoneda = tipoComprobanteRepository.existsById(id);
//		logger.debug("Existe Moneda? {}", existsMoneda);
//		if (!existsMoneda) {
//			throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
//		}
//		
//		try {
//			Moneda moneda = new Moneda();
//			moneda.setIdMoneda(id);
//			moneda.setNombre(nombre);
//			moneda.setSimbolo(simbolo);
//			moneda.setAbreviatura(abreviatura.toUpperCase());
//			
//			responseTipoComprobante = tipoComprobanteRepository.save(moneda);
//			logger.debug("Moneda actualizada");
//		} catch (Exception e) {
//			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
//		}
//		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), responseTipoComprobante);
//	}
//	
//	public ApiResponse delete(Integer id) throws ApiException {
//		Moneda tmpMoneda = tipoComprobanteRepository.findById(id).orElse(null);
//		logger.debug("Moneda: {}", tmpMoneda);
//		if (null != tmpMoneda) {
//			tipoComprobanteRepository.deleteById(id);
//			logger.debug("Moneda eliminada");
//			return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), "Moneda " + id + " eliminado");
//		}
//		throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
//	}
	
}
