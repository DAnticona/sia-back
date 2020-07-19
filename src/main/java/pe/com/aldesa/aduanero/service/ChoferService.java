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
import pe.com.aldesa.aduanero.entity.Chofer;
import pe.com.aldesa.aduanero.entity.Direccion;
import pe.com.aldesa.aduanero.entity.TipoDocumento;
import pe.com.aldesa.aduanero.exception.ApiException;
import pe.com.aldesa.aduanero.repository.ChoferRepository;
import pe.com.aldesa.aduanero.repository.DireccionRepository;
import pe.com.aldesa.aduanero.repository.PersonaRepository;
import pe.com.aldesa.aduanero.repository.TipoDocumentoRepository;
import pe.com.aldesa.aduanero.util.DateUtil;
import pe.com.aldesa.aduanero.util.NumberUtils;

@Service
public class ChoferService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ChoferRepository choferRepository;
	
	@Autowired
	private TipoDocumentoRepository tipoDocumentoRepository;
	
	@Autowired
	private DireccionRepository direccionRepository;
	
	@Autowired
	private PersonaRepository personaRepository;
	
	public ApiResponse findAll() throws ApiException {
		List<Chofer> choferes = choferRepository.findAll();
		int total = choferes.size();
		logger.debug("Total Choferes: {}", total);
		if (choferes.isEmpty()) {
			throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), choferes, total);
	}

	public ApiResponse findById(Long id) throws ApiException {
		Chofer tmpChofer = choferRepository.findById(id).orElse(null);
		logger.debug("Chofer: {}", tmpChofer);
		if (null == tmpChofer) {
			throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
		}
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
		String imagen = null;
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
			
			imagen = root.path("imagen").asText();
			logger.debug("imagen: {}", imagen);
			
			idDireccion = root.findParent("idDireccion").asInt();
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
		if (NumberUtils.isNotNull(idDireccion)) {
			direccion = direccionRepository.findById(idDireccion)
					.orElseThrow(() -> new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage()));
		}
		
		Long idPersona = personaRepository.findLastIdPersona();
		logger.debug("Current idPersona: {}", idPersona);
		try {
			Chofer chofer = new Chofer();
			chofer.setNumeroLicencia(numeroLicencia);
			chofer.setTipoDocumento(tipoDocumento);
			chofer.setNumeroDocumento(numeroDocumento);
			chofer.setNombres(nombres);
			chofer.setApellidoPaterno(apellidoPaterno);
			chofer.setApellidoMaterno(apellidoMaterno);
			chofer.setSexo(sexo.charAt(0));
			chofer.setFechaNacimiento(DateUtil.of(fechaNacimiento));
			chofer.setEmail(email);
			chofer.setImagen(imagen);
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
		String imagen = null;
		Integer idTipoDocumento = null;
		
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
			
			imagen = root.path("imagen").asText();
			logger.debug("imagen: {}", imagen);
			
			idTipoDocumento = root.path("idTipoDocumento").asInt();
			logger.debug("idTipoDocumento: {}", idTipoDocumento);
			
		} catch (JsonProcessingException e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		
		if (null == idPersona || idPersona == 0 || StringUtils.isBlank(numeroLicencia) || StringUtils.isBlank(numeroDocumento) || StringUtils.isBlank(nombres)
				|| StringUtils.isBlank(apellidoPaterno) || StringUtils.isBlank(apellidoMaterno) || StringUtils.isBlank(sexo)
				|| StringUtils.isBlank(fechaNacimiento) || StringUtils.isBlank(email) || StringUtils.isBlank(imagen)
				|| null == idTipoDocumento || idTipoDocumento == 0 ) {
			throw new ApiException(ApiError.EMPTY_OR_NULL_PARAMETER.getCode(), ApiError.EMPTY_OR_NULL_PARAMETER.getMessage());
		}
		
		TipoDocumento tipoDocumento = tipoDocumentoRepository.findById(idTipoDocumento)
				.orElseThrow(() -> new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage()));
		
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
			chofer.setImagen(imagen);
			chofer.setTipoDocumento(tipoDocumento);
			
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
