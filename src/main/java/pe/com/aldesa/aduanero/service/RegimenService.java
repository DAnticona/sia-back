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
import pe.com.aldesa.aduanero.entity.Regimen;
import pe.com.aldesa.aduanero.exception.ApiException;
import pe.com.aldesa.aduanero.repository.RegimenRepository;

@Service
public class RegimenService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private RegimenRepository regimenRepository;

	public ApiResponse findAll() {
		List<Regimen> regimenes = regimenRepository.findAll();
		int total = regimenes.size();
		logger.debug("Total Regimen: {}", total);
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), regimenes, total);
	}

	public ApiResponse findById(Integer id) {
		Regimen regimen = regimenRepository.findById(id).orElse(null);
		logger.debug("Regimen: {}", regimen);
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), regimen);
	}

	public ApiResponse findByCodigoAduana(Integer codigoAduana) throws ApiException {
		List<Regimen> regimenes = regimenRepository.findByCodigoAduana(codigoAduana);
		if (regimenes.size() > 1) {
			throw new ApiException(ApiError.MULTIPLES_SIMILAR_ELEMENTS.getCode(), ApiError.MULTIPLES_SIMILAR_ELEMENTS.getMessage());
		}
		logger.debug("Regimenes: {}", regimenes.get(0));
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), regimenes.get(0));
	}

	public ApiResponse save(String request) throws ApiException {
		Regimen responseRegimen;

		JsonNode root;
		String	nombre = null;
		Integer	codigoAduana = null;
		try {
			root = new ObjectMapper().readTree(request);

			nombre = root.path("nombre").asText();
			logger.debug("nombre: {}", nombre);

			codigoAduana = root.path("codigoAduana").asInt();
			logger.debug("codigoAduana: {}", codigoAduana);

		} catch (JsonProcessingException e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}

		if (null == codigoAduana || codigoAduana == 0 || StringUtils.isBlank(nombre)) {
			throw new ApiException(ApiError.EMPTY_OR_NULL_PARAMETER.getCode(), ApiError.EMPTY_OR_NULL_PARAMETER.getMessage());
		}

		try {
			Regimen regimen = new Regimen();
			regimen.setCodigoAduana(codigoAduana);
			regimen.setNombre(nombre);

			responseRegimen = regimenRepository.save(regimen);
			logger.debug("Regimen guardado");
		} catch (Exception e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), responseRegimen);
	}

	public ApiResponse update(String request) throws ApiException {
		Regimen responseRegimen;

		JsonNode root;
		Integer id;
		String	nombre = null;
		Integer	codigoAduana = null;
		try {
			root = new ObjectMapper().readTree(request);

			id = root.path("id").asInt();
			logger.debug("id: {}", id);

			nombre = root.path("nombre").asText();
			logger.debug("nombre: {}", nombre);

			codigoAduana = root.path("codigoAduana").asInt();
			logger.debug("codigoAduana: {}", codigoAduana);

		} catch (JsonProcessingException e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}

		if (null == codigoAduana || codigoAduana == 0 || StringUtils.isBlank(nombre)) {
			throw new ApiException(ApiError.EMPTY_OR_NULL_PARAMETER.getCode(), ApiError.EMPTY_OR_NULL_PARAMETER.getMessage());
		}

		try {
			Regimen regimen = new Regimen();
			regimen.setIdRegimen(id);
			regimen.setCodigoAduana(codigoAduana);
			regimen.setNombre(nombre);

			responseRegimen = regimenRepository.save(regimen);
			logger.debug("Regimen actualizado");
		} catch (Exception e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), responseRegimen);
	}

	public ApiResponse delete(Integer id) throws ApiException {
		Regimen tmpRegimen = regimenRepository.findById(id).orElse(null);
		logger.debug("Regimen: {}", tmpRegimen);
		if (null != tmpRegimen) {
			regimenRepository.deleteById(id);
			logger.debug("Regimen eliminado");
			return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), "Regimen " + id + " eliminado");
		}
		throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
	}
}
