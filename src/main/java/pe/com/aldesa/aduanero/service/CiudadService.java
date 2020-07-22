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
import pe.com.aldesa.aduanero.entity.Ciudad;
import pe.com.aldesa.aduanero.entity.Distrito;
import pe.com.aldesa.aduanero.exception.ApiException;
import pe.com.aldesa.aduanero.repository.CiudadRepository;
import pe.com.aldesa.aduanero.repository.DistritoRepository;

@Service
public class CiudadService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private CiudadRepository ciudadRepository;
	
	@Autowired
	private DistritoRepository distritoRepository;
	

	public ApiResponse findAll() throws ApiException {
		List<Ciudad> ciudades = ciudadRepository.findAll();
		int total = ciudades.size();
		logger.debug("Total Ciudades: {}", total);
		if (ciudades.isEmpty()) {
			throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), ciudades, total);
	}

	public ApiResponse findById(Integer id) throws ApiException {
		Ciudad tmpCiudad = ciudadRepository.findById(id).orElse(null);
		logger.debug("Ciudad: {}", tmpCiudad);
		if (null == tmpCiudad) {
			throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), tmpCiudad);
	}

	public ApiResponse save(String request) throws ApiException {
		Ciudad responseCiudad;
		
		JsonNode root;
		Integer	idDistrito = null;
		String	nombre = null;
		try {
			root = new ObjectMapper().readTree(request);
			
			idDistrito = root.path("idDistrito").asInt();
			logger.debug("idDistrito: {}", idDistrito);
			
			nombre = root.path("nombre").asText();
			logger.debug("nombre: {}", nombre);
			
		} catch (JsonProcessingException e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		
		if (null == idDistrito || idDistrito == 0  || StringUtils.isBlank(nombre)) {
			throw new ApiException(ApiError.EMPTY_OR_NULL_PARAMETER.getCode(), ApiError.EMPTY_OR_NULL_PARAMETER.getMessage());
		}
		
		logger.debug("Buscando distrito {}", idDistrito);
		Distrito distrito = distritoRepository.findById(idDistrito)
				.orElseThrow(() -> new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage()));
		logger.debug("Distrito {} encontrado", idDistrito);
		
		try {
			Ciudad ciudad = new Ciudad();
			ciudad.setDistrito(distrito);
			ciudad.setNombre(nombre);
			
			responseCiudad = ciudadRepository.save(ciudad);
			logger.debug("Ciudad guardada");
		} catch (Exception e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), responseCiudad);
	}

	public ApiResponse update(String request) throws ApiException {
		Ciudad responseCiudad;
		
		JsonNode root;
		Integer	id = null;
		Integer	idDistrito = null;
		String	nombre = null;
		try {
			root = new ObjectMapper().readTree(request);
			
			id = root.path("id").asInt();
			logger.debug("id: {}", id);
			
			idDistrito = root.path("idDistrito").asInt();
			logger.debug("idDistrito: {}", idDistrito);
			
			nombre = root.path("nombre").asText();
			logger.debug("nombre: {}", nombre);
			
		} catch (JsonProcessingException e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		
		if (null == id || id == 0 || null == idDistrito || idDistrito == 0 || StringUtils.isBlank(nombre)) {
			throw new ApiException(ApiError.EMPTY_OR_NULL_PARAMETER.getCode(), ApiError.EMPTY_OR_NULL_PARAMETER.getMessage());
		}
		
		boolean existsCiudad = ciudadRepository.existsById(id);
		logger.debug("Existe ciudad? {}", existsCiudad);
		if (!existsCiudad) {
			throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
		}
		
		Distrito distrito = distritoRepository.findById(idDistrito)
				.orElseThrow(() -> new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage()));
		logger.debug("Encontró distrito {} ? {}", idDistrito, (null != distrito));
		
		try {
			Ciudad ciudad = new Ciudad();
			ciudad.setIdCiudad(id);
			ciudad.setDistrito(distrito);
			ciudad.setNombre(nombre);
			
			responseCiudad = ciudadRepository.save(ciudad);
			logger.debug("Ciudad guardada");
		} catch (Exception e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), responseCiudad);
	}

	public ApiResponse delete(Integer id) throws ApiException {
		Ciudad tmpCiudad = ciudadRepository.findById(id).orElse(null);
		logger.debug("Ciudad: {}", tmpCiudad);
		if (null != tmpCiudad) {
			ciudadRepository.deleteById(id);
			logger.debug("Ciudad eliminada");
			return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), "Ciudad " + id + " eliminado");
		}
		throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
	}
}
