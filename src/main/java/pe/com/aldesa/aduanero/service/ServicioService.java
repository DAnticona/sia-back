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
import pe.com.aldesa.aduanero.entity.GrupoServicio;
import pe.com.aldesa.aduanero.entity.Servicio;
import pe.com.aldesa.aduanero.exception.ApiException;
import pe.com.aldesa.aduanero.repository.GrupoServicioRepository;
import pe.com.aldesa.aduanero.repository.ServicioRepository;

@Service
public class ServicioService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ServicioRepository servicioRepository;

	@Autowired
	private GrupoServicioRepository grupoServicioRepository;

	public ApiResponse findAll() {
		List<Servicio> servicios = servicioRepository.findAll();
		int total = servicios.size();
		logger.debug("Total Servicio: {}", total);
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), servicios, total);
	}

	public ApiResponse findById(Integer id) {
		Servicio tmpServicio = servicioRepository.findById(id).orElse(null);
		logger.debug("Servicio: {}", tmpServicio);
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), tmpServicio);
	}

	public ApiResponse save(String request) throws ApiException {
		Servicio responseServicio;

		JsonNode root;
		Integer	idGrupoServicio = null;
		String	nombre = null;
		String pct = null;
		Double precioMonedaNacional = null;
		Double precioMonedaExtrangera = null;
		try {
			root = new ObjectMapper().readTree(request);

			idGrupoServicio = root.path("idGrupoServicio").asInt();
			logger.debug("idGrupoServicio: {}", idGrupoServicio);

			nombre = root.path("nombre").asText();
			logger.debug("nombre: {}", nombre);

			pct = root.path("pct").asText();
			logger.debug("pct: {}", pct);

			precioMonedaNacional = root.path("precioMonedaNacional").asDouble();
			logger.debug("precioMonedaNacional: {}", precioMonedaNacional);

			precioMonedaExtrangera = root.path("precioMonedaExtrangera").asDouble();
			logger.debug("precioMonedaExtrangera: {}", precioMonedaExtrangera);

		} catch (JsonProcessingException e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}

		if (StringUtils.isBlank(nombre) || null == idGrupoServicio || idGrupoServicio == 0 || StringUtils.isBlank(pct)) {
			throw new ApiException(ApiError.EMPTY_OR_NULL_PARAMETER.getCode(), ApiError.EMPTY_OR_NULL_PARAMETER.getMessage());
		}

		GrupoServicio grupoServicio = grupoServicioRepository.findById(idGrupoServicio)
				.orElseThrow(() -> new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage()));

		try {
			Servicio servicio = new Servicio();
			servicio.setNombre(nombre);
			servicio.setGrupoServicio(grupoServicio);
			servicio.setPct(pct);
			servicio.setPrecioMonedaNacional(precioMonedaNacional);
			servicio.setPrecioMonedaExtranjera(precioMonedaExtrangera);

			responseServicio = servicioRepository.save(servicio);
			logger.debug("Servicio guardado");
		} catch (Exception e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), responseServicio);
	}

	public ApiResponse update(String request) throws ApiException {
		Servicio responseServicio;

		JsonNode root;
		Integer id = null;
		Integer	idGrupoServicio = null;
		String	nombre = null;
		String pct = null;
		Double precioMonedaNacional = null;
		Double precioMonedaExtrangera = null;
		try {
			root = new ObjectMapper().readTree(request);

			id = root.path("id").asInt();
			logger.debug("id: {}", id);

			idGrupoServicio = root.path("idGrupoServicio").asInt();
			logger.debug("idGrupoServicio: {}", idGrupoServicio);

			nombre = root.path("nombre").asText();
			logger.debug("nombre: {}", nombre);

			pct = root.path("pct").asText();
			logger.debug("pct: {}", pct);

			precioMonedaNacional = root.path("precioMonedaNacional").asDouble();
			logger.debug("precioMonedaNacional: {}", precioMonedaNacional);

			precioMonedaExtrangera = root.path("precioMonedaExtrangera").asDouble();
			logger.debug("precioMonedaExtrangera: {}", precioMonedaExtrangera);


		} catch (JsonProcessingException e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}

		if (null == id || id == 0 || StringUtils.isBlank(nombre) || null == idGrupoServicio || idGrupoServicio == 0 || StringUtils.isBlank(pct)) {
			throw new ApiException(ApiError.EMPTY_OR_NULL_PARAMETER.getCode(), ApiError.EMPTY_OR_NULL_PARAMETER.getMessage());
		}

		GrupoServicio grupoServicio = grupoServicioRepository.findById(idGrupoServicio)
				.orElseThrow(() -> new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage()));

		boolean existServicio = servicioRepository.existsById(id);
		logger.debug("Existe servicio? {}", existServicio);

		if (!existServicio) {
			throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
		}

		try {
			Servicio servicio = new Servicio();
			servicio.setIdServicio(id);
			servicio.setNombre(nombre);
			servicio.setGrupoServicio(grupoServicio);
			servicio.setPct(pct);
			servicio.setPrecioMonedaNacional(precioMonedaNacional);
			servicio.setPrecioMonedaExtranjera(precioMonedaExtrangera);

			responseServicio = servicioRepository.save(servicio);
			logger.debug("Servicio guardado");
		} catch (Exception e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), responseServicio);
	}

	public ApiResponse delete(Integer id) throws ApiException {
		Servicio tmpServicio = servicioRepository.findById(id).orElse(null);
		logger.debug("Servicio: {}", tmpServicio);
		if (null != tmpServicio) {
			servicioRepository.deleteById(id);
			logger.debug("Servicio eliminado");
			return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), "Servicio " + id + " eliminado");
		}
		throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
	}


}
