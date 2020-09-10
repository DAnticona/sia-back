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
import pe.com.aldesa.aduanero.entity.Area;
import pe.com.aldesa.aduanero.exception.ApiException;
import pe.com.aldesa.aduanero.repository.AreaRepository;

@Service
public class AreaService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private AreaRepository areaRepository;

	@Autowired
	public AreaService(AreaRepository areaRepository) {
		this.areaRepository = areaRepository;
	}

	public ApiResponse findAll() {
		List<Area> areas = areaRepository.findAll();
		int total = areas.size();
		logger.debug("Total áreas: {}", total);
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), areas, total);
	}

	public ApiResponse findById(Integer id) {
		Area tmpArea = areaRepository.findById(id).orElse(null);
		logger.debug("Area: {}", tmpArea);
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), tmpArea);
	}

	public ApiResponse save(String request) throws ApiException {
		Area responseArea;

		JsonNode root;
		String	nombre = null;
		String	abreviatura = null;
		String	activo = null;
		try {
			root = new ObjectMapper().readTree(request);

			nombre = root.path("nombre").asText();
			logger.debug("nombre: {}", nombre);

			abreviatura = root.path("abreviatura").asText();
			logger.debug("abreviatura: {}", abreviatura);

			activo = root.path("activo").asText();
			logger.debug("activo: {}", activo);

		} catch (JsonProcessingException e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}

		if (StringUtils.isBlank(nombre) || StringUtils.isNotBlank(activo)) {
			throw new ApiException(ApiError.EMPTY_OR_NULL_PARAMETER.getCode(), ApiError.EMPTY_OR_NULL_PARAMETER.getMessage());
		}

		try {
			Area area = new Area();
			area.setNombre(nombre);
			area.setAbreviatura(abreviatura.toUpperCase());
			area.setActivo(activo);

			responseArea = areaRepository.save(area);
			logger.debug("Area guardada");
		} catch (Exception e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), responseArea);
	}

	public ApiResponse update(String request) throws ApiException {
		Area responseArea;

		JsonNode root;
		Integer	id = null;
		String	nombre = null;
		String	abreviatura = null;
		String	activo = null;
		try {
			root = new ObjectMapper().readTree(request);

			id = root.path("id").asInt();
			logger.debug("id: {}", id);

			nombre = root.path("nombre").asText();
			logger.debug("nombre: {}", nombre);

			abreviatura = root.path("abreviatura").asText();
			logger.debug("abreviatura: {}", abreviatura);

			activo = root.path("activo").asText();
			logger.debug("activo: {}", activo);

		} catch (JsonProcessingException e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}

		if (null == id || id == 0 || StringUtils.isBlank(nombre) || StringUtils.isBlank(activo)) {
			throw new ApiException(ApiError.EMPTY_OR_NULL_PARAMETER.getCode(), ApiError.EMPTY_OR_NULL_PARAMETER.getMessage());
		}

		boolean existsArea = areaRepository.existsById(id);
		logger.debug("Existe Area? {}", existsArea);
		if (!existsArea) {
			throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
		}

		try {
			Area area = new Area();
			area.setIdArea(id);
			area.setNombre(nombre);
			area.setAbreviatura(abreviatura.toUpperCase());
			area.setActivo(activo);

			responseArea = areaRepository.save(area);
			logger.debug("Area actualizada");
		} catch (Exception e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), responseArea);
	}

	public ApiResponse delete(Integer id) throws ApiException {
		Area tmpArea = areaRepository.findById(id).orElse(null);
		logger.debug("Area: {}", tmpArea);
		if (null != tmpArea) {
			areaRepository.deleteById(id);
			logger.debug("Area eliminada");
			return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), "Area " + id + " eliminado");
		}
		throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
	}


}
