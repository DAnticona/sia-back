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
import pe.com.aldesa.aduanero.entity.TipoDocumento;
import pe.com.aldesa.aduanero.exception.ApiException;
import pe.com.aldesa.aduanero.repository.TipoDocumentoRepository;
import pe.com.aldesa.aduanero.util.NumberUtils;

@Service
public class TipoDocumentoService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private TipoDocumentoRepository tipoDocumentoRepository;
	
	@Autowired
	public TipoDocumentoService(TipoDocumentoRepository tipoDocumentoRepository) {
		this.tipoDocumentoRepository = tipoDocumentoRepository;
	}
	
	public ApiResponse findAll() throws ApiException {
		List<TipoDocumento> listTypeDocuments = tipoDocumentoRepository.findAll();
		int total = listTypeDocuments.size();
		logger.debug("Total Tipo de documentos: {}", total);
		if (listTypeDocuments.isEmpty()) {
			throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), listTypeDocuments, total);
	}
	
	public ApiResponse findById(Integer id) throws ApiException {
		TipoDocumento tmpTypeDocument = tipoDocumentoRepository.findById(id).orElse(null);
		logger.debug("Tipo documento: {}", tmpTypeDocument);
		if (null == tmpTypeDocument) {
			throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), tmpTypeDocument);
	}
	
	public ApiResponse save(String request) throws ApiException {
		TipoDocumento responseTipDoc;
		
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
			TipoDocumento tipoDocumento = new TipoDocumento();
			tipoDocumento.setNombre(nombre);
			tipoDocumento.setAbreviatura(abreviatura.toUpperCase());
			
			responseTipDoc = tipoDocumentoRepository.save(tipoDocumento);
		} catch (Exception e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), responseTipDoc);
	}
	
	public ApiResponse update(String request) throws ApiException {
		TipoDocumento responseTipDoc;
		
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
		
		if (NumberUtils.isNull(id) || StringUtils.isBlank(nombre) || StringUtils.isBlank(abreviatura)) {
			throw new ApiException(ApiError.EMPTY_OR_NULL_PARAMETER.getCode(), ApiError.EMPTY_OR_NULL_PARAMETER.getMessage());
		}
		
		try {
			TipoDocumento tipoDocumento = new TipoDocumento();
			tipoDocumento.setIdTipoDocumento(id);
			tipoDocumento.setNombre(nombre);
			tipoDocumento.setAbreviatura(abreviatura.toUpperCase());
			
			responseTipDoc = tipoDocumentoRepository.save(tipoDocumento);
		} catch (Exception e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), responseTipDoc);
	}
	
	public ApiResponse delete(Integer id) throws ApiException {
		TipoDocumento tmpTypeDocument = tipoDocumentoRepository.findById(id).orElse(null);
		logger.debug("Tipo documento: {}", tmpTypeDocument);
		if (null != tmpTypeDocument) {
			tipoDocumentoRepository.deleteById(id);
			return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), "Tipo Documento " + id + " eliminado");
		}
		throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
	}
	
}
