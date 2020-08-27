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
import pe.com.aldesa.aduanero.entity.Aduana;
import pe.com.aldesa.aduanero.exception.ApiException;
import pe.com.aldesa.aduanero.repository.AduanaRepository;

@Service
public class AduanaService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private AduanaRepository aduanaRepository;

	@Autowired
	public AduanaService(AduanaRepository aduanaRepository) {
		this.aduanaRepository = aduanaRepository;
	}

	public ApiResponse findAll() {
		List<Aduana> aduanas = aduanaRepository.findAll();
		int total = aduanas.size();
		logger.debug("Total Aduanas: {}", total);
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), aduanas, total);
	}

	public ApiResponse findById(Integer id) {
		Aduana tmpAduana = aduanaRepository.findById(id).orElse(null);
		logger.debug("Aduana: {}", tmpAduana);
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), tmpAduana);
	}

	public ApiResponse save(String request) throws ApiException {
		Aduana responseAduana;

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

		if (StringUtils.isBlank(nombre) || null == codigoAduana || codigoAduana == 0) {
			throw new ApiException(ApiError.EMPTY_OR_NULL_PARAMETER.getCode(), ApiError.EMPTY_OR_NULL_PARAMETER.getMessage());
		}

		try {
			Aduana aduana = new Aduana();
			aduana.setNombre(nombre);
			aduana.setCodigoAduana(codigoAduana);

			responseAduana = aduanaRepository.save(aduana);
			logger.debug("Aduana guardada");
		} catch (Exception e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), responseAduana);
	}

	public ApiResponse update(String request) throws ApiException {
		Aduana responseAduana;

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

		if (null == id || id == 0 || StringUtils.isBlank(nombre) || null == codigoAduana || codigoAduana == 0) {
			throw new ApiException(ApiError.EMPTY_OR_NULL_PARAMETER.getCode(), ApiError.EMPTY_OR_NULL_PARAMETER.getMessage());
		}

		try {
			Aduana aduana = new Aduana();
			aduana.setIdAduana(id);
			aduana.setNombre(nombre);
			aduana.setCodigoAduana(codigoAduana);

			responseAduana = aduanaRepository.save(aduana);
			logger.debug("Aduana actualizada");
		} catch (Exception e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), responseAduana);
	}

	public ApiResponse delete(Integer id) throws ApiException {
		Aduana tmpAduana = aduanaRepository.findById(id).orElse(null);
		logger.debug("Aduana: {}", tmpAduana);
		if (null != tmpAduana) {
			aduanaRepository.deleteById(id);
			logger.debug("Aduana eliminada");
			return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), "Aduana " + id + " eliminado");
		}
		throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
	}


}
