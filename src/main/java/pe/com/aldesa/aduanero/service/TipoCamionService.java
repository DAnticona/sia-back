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
import pe.com.aldesa.aduanero.entity.TipoCamion;
import pe.com.aldesa.aduanero.exception.ApiException;
import pe.com.aldesa.aduanero.repository.TipoCamionRepository;

@Service
public class TipoCamionService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private TipoCamionRepository tipoCamionRepository;

	@Autowired
	public TipoCamionService(TipoCamionRepository tipoCamionRepository) {
		this.tipoCamionRepository = tipoCamionRepository;
	}

	public ApiResponse findAll() {
		List<TipoCamion> tiposCamion = tipoCamionRepository.findAll();
		int total = tiposCamion.size();
		logger.debug("Total Tipos camiones: {}", total);
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), tiposCamion, total);
	}

	public ApiResponse findById(Integer id) {
		TipoCamion tmpTipoCamion = tipoCamionRepository.findById(id).orElse(null);
		logger.debug("Tipo Camion: {}", tmpTipoCamion);
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), tmpTipoCamion);
	}

	public ApiResponse save(String request) throws ApiException {
		TipoCamion responseTipoCamion;

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
			TipoCamion tipoCamion = new TipoCamion();
			tipoCamion.setNombre(nombre);
			tipoCamion.setAbreviatura(abreviatura.toUpperCase());

			responseTipoCamion = tipoCamionRepository.save(tipoCamion);
		} catch (Exception e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), responseTipoCamion);
	}

	public ApiResponse update(String request) throws ApiException {
		TipoCamion responseTipoCamion;

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
			TipoCamion tipoCamion = new TipoCamion();
			tipoCamion.setIdTipoCamion(id);
			tipoCamion.setNombre(nombre);
			tipoCamion.setAbreviatura(abreviatura.toUpperCase());

			responseTipoCamion = tipoCamionRepository.save(tipoCamion);
		} catch (Exception e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), responseTipoCamion);
	}

	public ApiResponse delete(Integer id) throws ApiException {
		TipoCamion tmpTipoCamion = tipoCamionRepository.findById(id).orElse(null);
		logger.debug("Tipo camion: {}", tmpTipoCamion);
		if (null != tmpTipoCamion) {
			tipoCamionRepository.deleteById(id);
			return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), "Tipo camion " + id + " eliminado");
		}
		throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
	}

}
