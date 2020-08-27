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
import pe.com.aldesa.aduanero.entity.TipoMercancia;
import pe.com.aldesa.aduanero.exception.ApiException;
import pe.com.aldesa.aduanero.repository.TipoMercanciaRepository;

@Service
public class TipoMercanciaService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private TipoMercanciaRepository tipoMercanciaRepository;

	@Autowired
	public TipoMercanciaService(TipoMercanciaRepository aduanaRepository) {
		this.tipoMercanciaRepository = aduanaRepository;
	}

	public ApiResponse findAll() {
		List<TipoMercancia> tipoMercancia = tipoMercanciaRepository.findAll();
		int total = tipoMercancia.size();
		logger.debug("Total TipoMercancia: {}", total);
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), tipoMercancia, total);
	}

	public ApiResponse findById(Integer id) {
		TipoMercancia tmpTipoMercancia = tipoMercanciaRepository.findById(id).orElse(null);
		logger.debug("TipoMercancia: {}", tmpTipoMercancia);
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), tmpTipoMercancia);
	}

	public ApiResponse save(String request) throws ApiException {
		TipoMercancia responseTipoMercancia;

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
			TipoMercancia tipoMercancia = new TipoMercancia();
			tipoMercancia.setNombre(nombre);

			responseTipoMercancia = tipoMercanciaRepository.save(tipoMercancia);
			logger.debug("TipoMercancia guardada");
		} catch (Exception e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), responseTipoMercancia);
	}

	public ApiResponse update(String request) throws ApiException {
		TipoMercancia responseTipoMercancia;

		JsonNode root;
		Integer id = null;
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

		if (null == id || id == 0 || StringUtils.isBlank(nombre)) {
			throw new ApiException(ApiError.EMPTY_OR_NULL_PARAMETER.getCode(), ApiError.EMPTY_OR_NULL_PARAMETER.getMessage());
		}

		try {
			TipoMercancia tipoMercancia = new TipoMercancia();
			tipoMercancia.setIdTipoMercancia(id);
			tipoMercancia.setNombre(nombre);

			responseTipoMercancia = tipoMercanciaRepository.save(tipoMercancia);
			logger.debug("TipoMercancia actualizada");
		} catch (Exception e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), responseTipoMercancia);
	}

	public ApiResponse delete(Integer id) throws ApiException {
		TipoMercancia tmpTipoMercancia = tipoMercanciaRepository.findById(id).orElse(null);
		logger.debug("TipoMercancia: {}", tmpTipoMercancia);
		if (null != tmpTipoMercancia) {
			tipoMercanciaRepository.deleteById(id);
			logger.debug("TipoMercancia eliminada");
			return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), "TipoMercancia " + id + " eliminado");
		}
		throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
	}


}
