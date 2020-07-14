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
import pe.com.aldesa.aduanero.entity.Rol;
import pe.com.aldesa.aduanero.exception.ApiException;
import pe.com.aldesa.aduanero.repository.RolRepository;

@Service
public class DefaultRolService implements RolService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private RolRepository rolRepository;
	
	@Autowired
	public DefaultRolService(RolRepository rolRepository) {
		this.rolRepository = rolRepository;
	}

	@Override
	public ApiResponse findAll() throws ApiException {
		List<Rol> roles = rolRepository.findAll();
		int total = roles.size();
		logger.debug("Total Roles: {}", total);
		if (roles.isEmpty()) {
			throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), roles, total);
	}

	@Override
	public ApiResponse findById(Integer id) throws ApiException {
		Rol tmpRol = rolRepository.findById(id).orElse(null);
		logger.debug("Rol: {}", tmpRol);
		if (null == tmpRol) {
			throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), tmpRol);
	}

	@Override
	public ApiResponse save(String request) throws ApiException {
		Rol responseRol;
		
		JsonNode root;
		String	nombre = null;
		try {
			root = new ObjectMapper().readTree(request);
			
			nombre = root.path("nombre").asText();
			logger.debug("nombre: {}", nombre);
			
		} catch (JsonProcessingException e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		
		if (StringUtils.isBlank(nombre)) {
			throw new ApiException(ApiError.EMPTY_OR_NULL_PARAMETER.getCode(), ApiError.EMPTY_OR_NULL_PARAMETER.getMessage());
		}
		
		try {
			Rol rol = new Rol();
			rol.setNombre(nombre.toUpperCase());
			
			responseRol = rolRepository.save(rol);
		} catch (Exception e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), responseRol);
	}

	@Override
	public ApiResponse update(String request) throws ApiException {
		Rol responseRol;
		
		JsonNode root;
		Integer	id = null;
		String	nombre = null;
		try {
			root = new ObjectMapper().readTree(request);
			
			id = root.path("id").asInt();
			logger.debug("id: {}", id);
			
			nombre = root.path("nombre").asText();
			logger.debug("nombre: {}", nombre);
			
		} catch (JsonProcessingException e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		
		if (id == null || id == 0 || StringUtils.isBlank(nombre)) {
			throw new ApiException(ApiError.EMPTY_OR_NULL_PARAMETER.getCode(), ApiError.EMPTY_OR_NULL_PARAMETER.getMessage());
		}
		
		try {
			Rol rol = new Rol();
			rol.setIdRol(id);
			rol.setNombre(nombre.toUpperCase());
			
			responseRol = rolRepository.save(rol);
		} catch (Exception e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), responseRol);
	}

	@Override
	public ApiResponse delete(Integer id) throws ApiException {
		Rol tmpRol = rolRepository.findById(id).orElse(null);
		logger.debug("Tipo documento: {}", tmpRol);
		if (null != tmpRol) {
			rolRepository.deleteById(id);
			return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), "Rol " + id + " eliminado");
		}
		throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
	}
	
}
