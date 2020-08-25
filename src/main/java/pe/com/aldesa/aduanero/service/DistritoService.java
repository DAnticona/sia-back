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
import pe.com.aldesa.aduanero.entity.Distrito;
import pe.com.aldesa.aduanero.entity.Provincia;
import pe.com.aldesa.aduanero.exception.ApiException;
import pe.com.aldesa.aduanero.repository.DistritoRepository;
import pe.com.aldesa.aduanero.repository.ProvinciaRepository;

@Service
public class DistritoService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private DistritoRepository distritoRepository;

	@Autowired
	private ProvinciaRepository provinciaRepository;

	public ApiResponse findAll() {
		List<Distrito> distritos = distritoRepository.findAll();
		int total = distritos.size();
		logger.debug("Total Distritos: {}", total);
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), distritos, total);
	}

	public ApiResponse findById(Integer id) {
		Distrito tmpDistrito = distritoRepository.findById(id).orElse(null);
		logger.debug("Distrito: {}", tmpDistrito);
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), tmpDistrito);
	}

	public ApiResponse save(String request) throws ApiException {
		Distrito responseDistrito;

		JsonNode root;
		Integer	idProvincia = null;
		String	nombre = null;
		try {
			root = new ObjectMapper().readTree(request);

			idProvincia = root.path("idProvincia").asInt();
			logger.debug("idProvincia: {}", idProvincia);

			nombre = root.path("nombre").asText();
			logger.debug("nombre: {}", nombre);

		} catch (JsonProcessingException e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}

		if (null == idProvincia || idProvincia == 0  || StringUtils.isBlank(nombre)) {
			throw new ApiException(ApiError.EMPTY_OR_NULL_PARAMETER.getCode(), ApiError.EMPTY_OR_NULL_PARAMETER.getMessage());
		}

		Provincia provincia = provinciaRepository.findById(idProvincia)
				.orElseThrow(() -> new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage()));

		try {
			Distrito distrito = new Distrito();
			distrito.setProvincia(provincia);
			distrito.setNombre(nombre);

			responseDistrito = distritoRepository.save(distrito);
		} catch (Exception e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), responseDistrito);
	}

	public ApiResponse update(String request) throws ApiException {
		Distrito responseDistrito;

		JsonNode root;
		Integer	id = null;
		Integer	idProvincia = null;
		String	nombre = null;
		try {
			root = new ObjectMapper().readTree(request);

			id = root.path("id").asInt();
			logger.debug("id: {}", id);

			idProvincia = root.path("idProvincia").asInt();
			logger.debug("idProvincia: {}", idProvincia);

			nombre = root.path("nombre").asText();
			logger.debug("nombre: {}", nombre);

		} catch (JsonProcessingException e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}

		if (null == id || id == 0 || null == idProvincia || idProvincia == 0 || StringUtils.isBlank(nombre)) {
			throw new ApiException(ApiError.EMPTY_OR_NULL_PARAMETER.getCode(), ApiError.EMPTY_OR_NULL_PARAMETER.getMessage());
		}

		boolean existsDistrito = distritoRepository.existsById(id);
		logger.debug("Existe distrito? {}", existsDistrito);
		if (!existsDistrito) {
			throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
		}

		Provincia provincia = provinciaRepository.findById(idProvincia)
				.orElseThrow(() -> new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage()));

		try {
			Distrito distrito = new Distrito();
			distrito.setIdDistrito(id);
			distrito.setProvincia(provincia);
			distrito.setNombre(nombre);

			responseDistrito = distritoRepository.save(distrito);
		} catch (Exception e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), responseDistrito);
	}

	public ApiResponse delete(Integer id) throws ApiException {
		Distrito tmpDistrito = distritoRepository.findById(id).orElse(null);
		logger.debug("Distrito: {}", tmpDistrito);
		if (null != tmpDistrito) {
			distritoRepository.deleteById(id);
			return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), "Distrito " + id + " eliminado");
		}
		throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
	}
}
