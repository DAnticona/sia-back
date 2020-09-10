package pe.com.aldesa.aduanero.service;

import java.util.List;
import java.util.Optional;

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
import pe.com.aldesa.aduanero.entity.Direccion;
import pe.com.aldesa.aduanero.entity.Persona;
import pe.com.aldesa.aduanero.entity.TipoDocumento;
import pe.com.aldesa.aduanero.exception.ApiException;
import pe.com.aldesa.aduanero.repository.DireccionRepository;
import pe.com.aldesa.aduanero.repository.PersonaRepository;
import pe.com.aldesa.aduanero.repository.TipoDocumentoRepository;
import pe.com.aldesa.aduanero.util.DateUtil;

@Service
public class PersonaService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PersonaRepository personaRepository;

	@Autowired
	private TipoDocumentoRepository tipoDocumentoRepository;

	@Autowired
	private DireccionRepository direccionRepository;

	public ApiResponse findAll() {
		List<Persona> usuarios = personaRepository.findAll();
		int total = usuarios.size();
		logger.debug("Total Personas: {}", total);
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), usuarios, total);
	}

	public ApiResponse findById(Long id) {
		Persona tmpPersona = personaRepository.findById(id).orElse(null);
		logger.debug("Persona: {}", tmpPersona);
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), tmpPersona);
	}

	public ApiResponse searchByNombres(String nombre) {
		List<Persona> personas = personaRepository.searchByNombres(nombre);
		logger.debug("Personas by Name: {}", personas);
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), personas, personas.size());
	}

	public ApiResponse findByNumeroDocumento(String numeroDocumento) {
		Optional<Persona> optPerson = personaRepository.findByNumeroDocumento(numeroDocumento);
		logger.debug("Persona by Numero Documento: {}", optPerson);
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), optPerson);
	}

	public ApiResponse save(String request) throws ApiException {
		Persona responsePersona;

		JsonNode root;
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

		if (StringUtils.isBlank(numeroDocumento) || StringUtils.isBlank(nombres) || StringUtils.isBlank(apellidoPaterno)	|| null == idTipoDocumento || idTipoDocumento == 0) {
			throw new ApiException(ApiError.EMPTY_OR_NULL_PARAMETER.getCode(), ApiError.EMPTY_OR_NULL_PARAMETER.getMessage());
		}

		logger.debug("Buscando tipo de documento {}", idTipoDocumento);
		TipoDocumento tipoDocumento = tipoDocumentoRepository.findById(idTipoDocumento)
				.orElseThrow(() -> new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage()));
		logger.debug("Tipo de documento {} encontrado", idTipoDocumento);

		Direccion direccion = null;
		if (idDireccion != 0) {
			direccion = direccionRepository.findById(idDireccion)
					.orElseThrow(() -> new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage()));
		}
		logger.debug("Encontró dirección {} ? {}", idDireccion, (null != direccion));

		try {
			Persona persona = new Persona();
			persona.setNombres(nombres);
			persona.setApellidoPaterno(apellidoPaterno);
			persona.setApellidoMaterno(apellidoMaterno);
			persona.setTipoDocumento(tipoDocumento);
			persona.setNumeroDocumento(numeroDocumento);
			if (StringUtils.isNotBlank(sexo))
				persona.setSexo(sexo.charAt(0));
			if (StringUtils.isNotBlank(fechaNacimiento))
				persona.setFechaNacimiento(DateUtil.of(fechaNacimiento));
			persona.setEmail(email);
			persona.setDireccion(direccion);

			responsePersona = personaRepository.save(persona);
			logger.debug("Persona guardada");
		} catch (Exception e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), responsePersona);
	}

	public ApiResponse update(String request) throws ApiException {
		Persona responsePersona;

		JsonNode root;
		Long id = null;
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

			id = root.path("id").asLong();
			logger.debug("id: {}", id);

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

		if (id == 0 || null == id || StringUtils.isBlank(numeroDocumento) || StringUtils.isBlank(nombres) || StringUtils.isBlank(apellidoPaterno) || null == idTipoDocumento || idTipoDocumento == 0) {
			throw new ApiException(ApiError.EMPTY_OR_NULL_PARAMETER.getCode(), ApiError.EMPTY_OR_NULL_PARAMETER.getMessage());
		}

		boolean existsPersona = personaRepository.existsById(id);
		logger.debug("Existe persona {}? {} ", id, existsPersona);
		if (!existsPersona) {
			throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
		}

		logger.debug("Buscando tipo de documento {}", idTipoDocumento);
		TipoDocumento tipoDocumento = tipoDocumentoRepository.findById(idTipoDocumento)
				.orElseThrow(() -> new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage()));
		logger.debug("Tipo de documento {} encontrado", idTipoDocumento);

		Direccion direccion = null;
		if (idDireccion != 0) {
			direccion = direccionRepository.findById(idDireccion)
					.orElseThrow(() -> new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage()));
		}
		logger.debug("Encontró dirección {} ? {}", idDireccion, (null != direccion));

		try {
			Persona persona = new Persona();
			persona.setIdPersona(id);
			persona.setNombres(nombres);
			persona.setApellidoPaterno(apellidoPaterno);
			persona.setApellidoMaterno(apellidoMaterno);
			persona.setTipoDocumento(tipoDocumento);
			persona.setNumeroDocumento(numeroDocumento);
			if (StringUtils.isNotBlank(sexo))
				persona.setSexo(sexo.charAt(0));
			if (StringUtils.isNotBlank(fechaNacimiento))
				persona.setFechaNacimiento(DateUtil.of(fechaNacimiento));
			persona.setEmail(email);
			persona.setDireccion(direccion);

			responsePersona = personaRepository.save(persona);
			logger.debug("Persona actualizada");
		} catch (Exception e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), responsePersona);
	}

	public ApiResponse delete(Long id) throws ApiException {
		Persona tmpPersona = personaRepository.findById(id).orElse(null);
		logger.debug("Persona: {}", tmpPersona);
		if (null != tmpPersona) {
			personaRepository.deleteById(id);
			logger.debug("Persona eliminada");
			return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), "Persona " + id + " eliminada");
		}
		throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
	}

}
