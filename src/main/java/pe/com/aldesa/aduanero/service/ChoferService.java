package pe.com.aldesa.aduanero.service;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import pe.com.aldesa.aduanero.constant.ApiError;
import pe.com.aldesa.aduanero.dto.ApiResponse;
import pe.com.aldesa.aduanero.entity.Chofer;
import pe.com.aldesa.aduanero.entity.Direccion;
import pe.com.aldesa.aduanero.entity.TipoDocumento;
import pe.com.aldesa.aduanero.exception.ApiException;
import pe.com.aldesa.aduanero.repository.ChoferRepository;
import pe.com.aldesa.aduanero.repository.DireccionRepository;
import pe.com.aldesa.aduanero.repository.TipoDocumentoRepository;
import pe.com.aldesa.aduanero.util.DateUtil;

@Service
public class ChoferService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ChoferRepository choferRepository;

	@Autowired
	private TipoDocumentoRepository tipoDocumentoRepository;

	@Autowired
	private DireccionRepository direccionRepository;

	private static final int PAGE_LIMIT = 10;

	public ApiResponse findAll(Integer pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber - 1, PAGE_LIMIT);
		Page<Chofer> choferPage = choferRepository.findAll(pageable);
		logger.debug("Página {} de: {}", pageNumber, choferPage.getTotalPages());
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), choferPage.getContent(), Math.toIntExact(choferPage.getTotalElements()));
	}

	public ApiResponse findById(Long id) {
		Chofer tmpChofer = choferRepository.findById(id).orElse(null);
		logger.debug("Chofer: {}", tmpChofer);
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), tmpChofer);
	}

	public ApiResponse save(String request) throws ApiException {
		Chofer responseChofer;

		JsonNode root;
		String numeroLicencia = null;
		String numeroDocumento = null;
		String nombres = null;
		String apellidoPaterno = null;
		String apellidoMaterno = null;
		String sexo = null;
		String fechaNacimiento = null;
		String email = null;
		Integer idTipoDocumento = null;
		Integer idDireccion = null;

		try {
			root = new ObjectMapper().readTree(request);

			numeroLicencia = root.path("numeroLicencia").asText();
			logger.debug("numeroLicencia: {}", numeroLicencia);

			idTipoDocumento = root.path("idTipoDocumento").asInt();
			logger.debug("idTipoDocumento: {}", idTipoDocumento);

			numeroDocumento = root.path("numeroDocumento").asText();
			logger.debug("numeroDocumento: {}", numeroDocumento);

			nombres = root.path("nombres").asText();
			logger.debug("nombres: {}", nombres);

			apellidoPaterno = root.path("apellidoPaterno").asText();
			logger.debug("apellidoPaterno: {}", apellidoPaterno);

			apellidoMaterno = root.path("apellidoMaterno").asText();
			logger.debug("apellidoMaterno: {}", apellidoMaterno);

			sexo = root.path("sexo").asText();
			logger.debug("sexo: {}", sexo);

			fechaNacimiento = root.path("fechaNacimiento").asText();
			logger.debug("fechaNacimiento: {}", fechaNacimiento);

			email = root.path("email").asText();
			logger.debug("email: {}", email);

			idDireccion = root.path("idDireccion").asInt();
			logger.debug("idDireccion: {}", idDireccion);

		} catch (JsonProcessingException e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}

		if (StringUtils.isBlank(numeroLicencia) || StringUtils.isBlank(numeroDocumento) || StringUtils.isBlank(nombres)
				|| StringUtils.isBlank(apellidoPaterno)	|| null == idTipoDocumento || idTipoDocumento == 0) {
			throw new ApiException(ApiError.EMPTY_OR_NULL_PARAMETER.getCode(), ApiError.EMPTY_OR_NULL_PARAMETER.getMessage());
		}

		TipoDocumento tipoDocumento = tipoDocumentoRepository.findById(idTipoDocumento)
				.orElseThrow(() -> new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage()));

		Direccion direccion = null;
		if (0 != idDireccion) {
			direccion = direccionRepository.findById(idDireccion)
					.orElseThrow(() -> new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage()));
		}

		try {
			Chofer chofer = new Chofer();
			chofer.setNumeroLicencia(numeroLicencia);
			chofer.setTipoDocumento(tipoDocumento);
			chofer.setNumeroDocumento(numeroDocumento);
			chofer.setNombres(nombres);
			chofer.setApellidoPaterno(apellidoPaterno);
			chofer.setApellidoMaterno(apellidoMaterno);
			if (StringUtils.isNotBlank(sexo))
				chofer.setSexo(sexo.charAt(0));
			if (StringUtils.isNotBlank(fechaNacimiento))
				chofer.setFechaNacimiento(DateUtil.of(fechaNacimiento));
			chofer.setEmail(email);
			chofer.setDireccion(direccion);

			responseChofer = choferRepository.save(chofer);
		} catch (Exception e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), responseChofer);
	}

	public ApiResponse update(String request) throws ApiException {
		Chofer responseChofer;

		JsonNode root;
		Long idPersona = null;
		String numeroLicencia = null;
		String numeroDocumento = null;
		String nombres = null;
		String apellidoPaterno = null;
		String apellidoMaterno = null;
		String sexo = null;
		String fechaNacimiento = null;
		String email = null;
		Integer idTipoDocumento = null;
		Integer idDireccion = null;

		try {
			root = new ObjectMapper().readTree(request);

			idPersona = root.path("idPersona").asLong();
			logger.debug("idPersona: {}", idPersona);

			numeroLicencia = root.path("numeroLicencia").asText();
			logger.debug("numeroLicencia: {}", numeroLicencia);

			numeroDocumento = root.path("numeroDocumento").asText();
			logger.debug("numeroDocumento: {}", numeroDocumento);

			nombres = root.path("nombres").asText();
			logger.debug("nombres: {}", nombres);

			apellidoPaterno = root.path("apellidoPaterno").asText();
			logger.debug("apellidoPaterno: {}", apellidoPaterno);

			apellidoMaterno = root.path("apellidoMaterno").asText();
			logger.debug("apellidoMaterno: {}", apellidoMaterno);

			sexo = root.path("sexo").asText();
			logger.debug("sexo: {}", sexo);

			fechaNacimiento = root.path("fechaNacimiento").asText();
			logger.debug("fechaNacimiento: {}", fechaNacimiento);

			email = root.path("email").asText();
			logger.debug("email: {}", email);

			idTipoDocumento = root.path("idTipoDocumento").asInt();
			logger.debug("idTipoDocumento: {}", idTipoDocumento);

			idDireccion = root.path("idDireccion").asInt();
			logger.debug("idDireccion: {}", idDireccion);

		} catch (JsonProcessingException e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}

		if (StringUtils.isBlank(numeroLicencia) || StringUtils.isBlank(numeroDocumento) || StringUtils.isBlank(nombres)
				|| StringUtils.isBlank(apellidoPaterno)	|| null == idTipoDocumento || idTipoDocumento == 0) {
			throw new ApiException(ApiError.EMPTY_OR_NULL_PARAMETER.getCode(), ApiError.EMPTY_OR_NULL_PARAMETER.getMessage());
		}

		TipoDocumento tipoDocumento = tipoDocumentoRepository.findById(idTipoDocumento)
				.orElseThrow(() -> new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage()));

		Direccion direccion = null;
		if (0 != idDireccion) {
			direccion = direccionRepository.findById(idDireccion)
					.orElseThrow(() -> new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage()));
		}

		try {
			Chofer chofer = new Chofer();
			chofer.setNumeroLicencia(numeroLicencia);
			chofer.setNumeroDocumento(numeroDocumento);
			chofer.setNombres(nombres);
			chofer.setApellidoPaterno(apellidoPaterno);
			chofer.setApellidoMaterno(apellidoMaterno);
			chofer.setSexo(sexo.charAt(0));
			chofer.setFechaNacimiento(DateUtil.of(fechaNacimiento));
			chofer.setEmail(email);
			chofer.setTipoDocumento(tipoDocumento);
			chofer.setDireccion(direccion);

			responseChofer = choferRepository.save(chofer);
		} catch (Exception e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), responseChofer);
	}

	public ApiResponse delete(Long id) throws ApiException {
		Chofer tmpChofer = choferRepository.findById(id).orElse(null);
		logger.debug("Chofer: {}", tmpChofer);
		if (null != tmpChofer) {
			choferRepository.deleteById(id);
			return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), "Chofer " + id + " eliminado");
		}
		throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
	}
}
