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
import pe.com.aldesa.aduanero.entity.AgenciaAduanas;
import pe.com.aldesa.aduanero.entity.Direccion;
import pe.com.aldesa.aduanero.exception.ApiException;
import pe.com.aldesa.aduanero.repository.AgenciaAduanasRepository;
import pe.com.aldesa.aduanero.repository.DireccionRepository;

@Service
public class AgenciaAduanasService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private AgenciaAduanasRepository agenciaAduanasRepository;

	@Autowired
	private DireccionRepository direccionRepository;

	public ApiResponse findAll() {
		List<AgenciaAduanas> agencias = agenciaAduanasRepository.findAll();
		int total = agencias.size();
		logger.debug("Total AgenciaAduanas: {}", total);
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), agencias, total);
	}

	public ApiResponse findById(Long id) {
		AgenciaAduanas agencia = agenciaAduanasRepository.findById(id).orElse(null);
		logger.debug("Agencia: {}", agencia);
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), agencia);
	}

	public ApiResponse save(String request) throws ApiException {
		AgenciaAduanas responseAgencia;

		JsonNode root;
		Integer	codigoAgencia = null;
		String	ruc = null;
		String	razonSocial = null;
		String	nombreComercial = null;
		Integer	idDireccion = null;
		try {
			root = new ObjectMapper().readTree(request);

			codigoAgencia = root.path("codigoAgencia").asInt();
			logger.debug("codigoAgencia: {}", codigoAgencia);

			ruc = root.path("ruc").asText();
			logger.debug("ruc: {}", ruc);

			razonSocial = root.path("razonSocial").asText();
			logger.debug("razonSocial: {}", razonSocial);

			idDireccion = root.path("idDireccion").asInt();
			logger.debug("idDireccion:AgenciaAduanas {}", idDireccion);

			nombreComercial = root.path("nombreComercial").asText();
			logger.debug("nombreComercial: {}", nombreComercial);

		} catch (JsonProcessingException e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}

		if (null == codigoAgencia || codigoAgencia == 0 || StringUtils.isBlank(ruc) || StringUtils.isBlank(razonSocial)) {
			throw new ApiException(ApiError.EMPTY_OR_NULL_PARAMETER.getCode(), ApiError.EMPTY_OR_NULL_PARAMETER.getMessage());
		}

		Direccion direccion = null;
		if (idDireccion != 0) {
			direccion = direccionRepository.findById(idDireccion)
					.orElseThrow(() -> new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage()));
		}
		logger.debug("Encontró dirección {} ? {}", idDireccion, (null != direccion));

		try {
			AgenciaAduanas agencia = new AgenciaAduanas();
			agencia.setCodigoAduana(codigoAgencia);
			agencia.setRuc(ruc);
			agencia.setRazonSocial(razonSocial);
			agencia.setDireccion(direccion);
			agencia.setNombreComercial(nombreComercial);

			responseAgencia = agenciaAduanasRepository.save(agencia);
			logger.debug("Agencia guardada");
		} catch (Exception e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), responseAgencia);
	}

	public ApiResponse update(String request) throws ApiException {
		AgenciaAduanas responseAgencia;

		JsonNode root;
		Long id = null;
		Integer	codigoAgencia = null;
		String	ruc = null;
		String	razonSocial = null;
		String	nombreComercial = null;
		Integer	idDireccion = null;
		try {
			root = new ObjectMapper().readTree(request);

			id = root.path("id").asLong();
			logger.debug("id: {}", id);

			codigoAgencia = root.path("codigoAgencia").asInt();
			logger.debug("codigoAgencia: {}", codigoAgencia);

			ruc = root.path("ruc").asText();
			logger.debug("ruc: {}", ruc);

			razonSocial = root.path("razonSocial").asText();
			logger.debug("razonSocial: {}", razonSocial);

			idDireccion = root.path("idDireccion").asInt();
			logger.debug("idDireccion: {}", idDireccion);

			nombreComercial = root.path("nombreComercial").asText();
			logger.debug("nombreComercial: {}", nombreComercial);


		} catch (JsonProcessingException e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}

		if (null == codigoAgencia || codigoAgencia == 0 || null == id || id == 0 || StringUtils.isBlank(ruc) || StringUtils.isBlank(razonSocial)) {
			throw new ApiException(ApiError.EMPTY_OR_NULL_PARAMETER.getCode(), ApiError.EMPTY_OR_NULL_PARAMETER.getMessage());
		}

		boolean existsEmpresa = agenciaAduanasRepository.existsById(id);
		logger.debug("Existe empresa {}? {}", id, existsEmpresa);
		if (!existsEmpresa) {
			throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
		}

		//		boolean existsAgencia = agenciaAduanasRepository.existsByCodigoAgencia(codigoAgencia);
		//		logger.debug("Existe agencia {}? {}", id, existsAgencia);
		//		if (!existsAgencia) {
		//			throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
		//		}

		Direccion direccion = null;
		if (idDireccion != 0) {
			direccion = direccionRepository.findById(idDireccion)
					.orElseThrow(() -> new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage()));
		}
		logger.debug("Encontró dirección {} ? {}", idDireccion, (null != direccion));

		try {
			AgenciaAduanas agencia = new AgenciaAduanas();
			agencia.setIdEmpresa(id);
			agencia.setCodigoAduana(codigoAgencia);
			agencia.setRuc(ruc);
			agencia.setRazonSocial(razonSocial);
			agencia.setDireccion(direccion);
			agencia.setNombreComercial(nombreComercial);

			responseAgencia = agenciaAduanasRepository.save(agencia);
			logger.debug("Agencia actualizada");
		} catch (Exception e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), responseAgencia);
	}

	public ApiResponse delete(Long id) throws ApiException {
		AgenciaAduanas tmpAgencia = agenciaAduanasRepository.findById(id).orElse(null);
		logger.debug("Agencia: {}", tmpAgencia);
		if (null != tmpAgencia) {
			agenciaAduanasRepository.deleteById(id);
			logger.debug("Agencia eliminada");
			return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), "Agencia " + id + " eliminada");
		}
		throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
	}

}
