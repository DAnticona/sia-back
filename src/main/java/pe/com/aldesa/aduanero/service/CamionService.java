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
import pe.com.aldesa.aduanero.entity.Camion;
import pe.com.aldesa.aduanero.entity.TipoCamion;
import pe.com.aldesa.aduanero.exception.ApiException;
import pe.com.aldesa.aduanero.repository.CamionRepository;
import pe.com.aldesa.aduanero.repository.TipoCamionRepository;

@Service
public class CamionService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CamionRepository camionRepository;

	@Autowired
	private TipoCamionRepository tipoCamionRepository;

	public ApiResponse findAll() throws ApiException {
		List<Camion> camiones = camionRepository.findAll();
		int total = camiones.size();
		logger.debug("Total Camiones: {}", total);
		if (camiones.isEmpty()) {
			throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), camiones, total);
	}

	public ApiResponse findById(Long id) throws ApiException {
		Camion tmpCamion = camionRepository.findById(id).orElse(null);
		logger.debug("Camion: {}", tmpCamion);
		if (null == tmpCamion) {
			throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), tmpCamion);
	}

	public ApiResponse save(String request) throws ApiException {
		Camion responseCamion;

		JsonNode root;
		Integer codeTipoCamion = null;
		String placa = null;
		String marca = null;
		String certificado = null;
		Double largo = null;
		Double ancho = null;
		Double alto = null;
		Double peso = null;
		String eje = null;
		try {
			root = new ObjectMapper().readTree(request);

			codeTipoCamion = root.path("codeTipoCamion").asInt();
			logger.debug("codeTipoCamion: {}", codeTipoCamion);

			placa = root.path("placa").asText();
			logger.debug("placa: {}", placa);

			marca = root.path("marca").asText();
			logger.debug("marca: {}", marca);

			certificado = root.path("certificado").asText();
			logger.debug("certificado: {}", certificado);

			largo = root.path("largo").asDouble();
			logger.debug("largo: {}", largo);

			ancho = root.path("ancho").asDouble();
			logger.debug("ancho: {}", ancho);

			alto = root.path("alto").asDouble();
			logger.debug("alto: {}", alto);

			peso = root.path("peso").asDouble();
			logger.debug("peso: {}", peso);

			eje = root.path("eje").asText();
			logger.debug("eje: {}", eje);

		} catch (JsonProcessingException e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}

		if (null == codeTipoCamion || StringUtils.isBlank(placa) || StringUtils.isBlank(marca)
				|| StringUtils.isBlank(certificado) || null == largo || null == ancho
				|| null == alto || null == peso || null == eje) {
			throw new ApiException(ApiError.EMPTY_OR_NULL_PARAMETER.getCode(), ApiError.EMPTY_OR_NULL_PARAMETER.getMessage());
		}

		TipoCamion tipoCamion = tipoCamionRepository.findById(codeTipoCamion)
				.orElseThrow(() -> new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage()));

		try {
			Camion camion = new Camion();
			camion.setTipoCamion(tipoCamion);
			camion.setPlaca(placa);
			camion.setMarca(marca);
			camion.setCertificado(certificado);
			camion.setLargo(largo);
			camion.setAncho(ancho);
			camion.setAlto(alto);
			camion.setPeso(peso);
			camion.setEje(eje);

			responseCamion = camionRepository.save(camion);
		} catch (Exception e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), responseCamion);
	}

	public ApiResponse update(String request) throws ApiException {
		Camion responseCamion;

		JsonNode root;
		Long id = null;
		Integer codeTipoCamion = null;
		String placa = null;
		String marca = null;
		String certificado = null;
		Double largo = null;
		Double ancho = null;
		Double alto = null;
		Double peso = null;
		String eje = null;
		try {
			root = new ObjectMapper().readTree(request);

			id = root.path("id").asLong();
			logger.debug("id: {}", id);

			codeTipoCamion = root.path("codeTipoCamion").asInt();
			logger.debug("codeTipoCamion: {}", codeTipoCamion);

			placa = root.path("placa").asText();
			logger.debug("placa: {}", placa);

			marca = root.path("marca").asText();
			logger.debug("marca: {}", marca);

			certificado = root.path("certificado").asText();
			logger.debug("certificado: {}", certificado);

			largo = root.path("largo").asDouble();
			logger.debug("largo: {}", largo);

			ancho = root.path("ancho").asDouble();
			logger.debug("ancho: {}", ancho);

			alto = root.path("alto").asDouble();
			logger.debug("alto: {}", alto);

			peso = root.path("peso").asDouble();
			logger.debug("peso: {}", peso);

			eje = root.path("eje").asText();
			logger.debug("eje: {}", eje);

		} catch (JsonProcessingException e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}

		if (null == id || id == 0 || null == codeTipoCamion || StringUtils.isBlank(placa) || StringUtils.isBlank(marca)
				|| StringUtils.isBlank(certificado) || null == largo || null == ancho
				|| null == alto || null == peso || null == eje) {
			throw new ApiException(ApiError.EMPTY_OR_NULL_PARAMETER.getCode(), ApiError.EMPTY_OR_NULL_PARAMETER.getMessage());
		}

		TipoCamion tipoCamion = tipoCamionRepository.findById(codeTipoCamion)
				.orElseThrow(() -> new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage()));

		try {
			Camion camion = new Camion();
			camion.setIdCamion(id);
			camion.setTipoCamion(tipoCamion);
			camion.setPlaca(placa);
			camion.setMarca(marca);
			camion.setCertificado(certificado);
			camion.setLargo(largo);
			camion.setAncho(ancho);
			camion.setAlto(alto);
			camion.setPeso(peso);
			camion.setEje(eje);

			responseCamion = camionRepository.save(camion);
		} catch (Exception e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), responseCamion);
	}

	public ApiResponse delete(Long id) throws ApiException {
		Camion tmpCamion = camionRepository.findById(id).orElse(null);
		logger.debug("Camion: {}", tmpCamion);
		if (null != tmpCamion) {
			camionRepository.deleteById(id);
			return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), "Camion " + id + " eliminado");
		}
		throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
	}

}
