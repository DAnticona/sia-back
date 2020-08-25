package pe.com.aldesa.aduanero.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import pe.com.aldesa.aduanero.constant.ApiError;
import pe.com.aldesa.aduanero.dto.ApiResponse;
import pe.com.aldesa.aduanero.entity.Direccion;
import pe.com.aldesa.aduanero.entity.Rol;
import pe.com.aldesa.aduanero.entity.TipoDocumento;
import pe.com.aldesa.aduanero.entity.Usuario;
import pe.com.aldesa.aduanero.exception.ApiException;
import pe.com.aldesa.aduanero.repository.DireccionRepository;
import pe.com.aldesa.aduanero.repository.RolRepository;
import pe.com.aldesa.aduanero.repository.TipoDocumentoRepository;
import pe.com.aldesa.aduanero.repository.UsuarioRepository;
import pe.com.aldesa.aduanero.util.DateUtil;

@Service
public class UsuarioService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private TipoDocumentoRepository tipoDocumentoRepository;

	@Autowired
	private RolRepository rolRepository;

	@Autowired
	private DireccionRepository direccionRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEnconder;

	public ApiResponse findAll() {
		List<Usuario> usuarios = usuarioRepository.findAll();
		int total = usuarios.size();
		logger.debug("Total Usuarios: {}", total);
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), usuarios, total);
	}

	public ApiResponse findById(Long id) {
		Usuario tmpUser = usuarioRepository.findById(id).orElse(null);
		logger.debug("Usuario found: {}", tmpUser);
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), tmpUser);
	}

	public ApiResponse save(String request) throws ApiException {
		Usuario responseUser;

		JsonNode root;
		String	username = null;
		String	password = null;
		Integer	idRol = null;
		String imagen = null;
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

			username = root.path("username").asText();
			logger.debug("username: {}", username);

			password = root.path("password").asText();

			idRol = root.path("idRol").asInt();
			logger.debug("idRol: {}", idRol);

			imagen = root.path("imagen").asText();
			logger.debug("imagen: {}", imagen);

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

		if (StringUtils.isBlank(username) || StringUtils.isBlank(password) || idRol == 0 || null == idRol ||
				StringUtils.isBlank(numeroDocumento) || StringUtils.isBlank(nombres)
				|| StringUtils.isBlank(apellidoPaterno)	|| null == idTipoDocumento || idTipoDocumento == 0) {
			throw new ApiException(ApiError.EMPTY_OR_NULL_PARAMETER.getCode(), ApiError.EMPTY_OR_NULL_PARAMETER.getMessage());
		}

		logger.debug("Buscando tipo de documento {}", idTipoDocumento);
		TipoDocumento tipoDocumento = tipoDocumentoRepository.findById(idTipoDocumento)
				.orElseThrow(() -> new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage()));
		logger.debug("Tipo de documento {} encontrado", idTipoDocumento);

		logger.debug("Buscando rol {}", idRol);
		Rol rol = rolRepository.findById(idRol)
				.orElseThrow(() -> new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage()));
		logger.debug("Rol {} encontrado", idRol);

		Direccion direccion = null;
		if (idDireccion != 0) {
			direccion = direccionRepository.findById(idDireccion)
					.orElseThrow(() -> new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage()));
		}
		logger.debug("Encontró dirección {} ? {}", idDireccion, (null != direccion));

		try {
			Usuario usuario = new Usuario();
			usuario.setUsername(username);
			usuario.setPassword(passwordEnconder.encode(password));
			usuario.setRol(rol);
			usuario.setImagen(imagen);
			usuario.setNombres(nombres);
			usuario.setApellidoPaterno(apellidoPaterno);
			usuario.setApellidoMaterno(apellidoMaterno);
			usuario.setTipoDocumento(tipoDocumento);
			usuario.setNumeroDocumento(numeroDocumento);
			if (StringUtils.isNotBlank(sexo))
				usuario.setSexo(sexo.charAt(0));
			if (StringUtils.isNotBlank(fechaNacimiento))
				usuario.setFechaNacimiento(DateUtil.of(fechaNacimiento));
			usuario.setEmail(email);
			usuario.setDireccion(direccion);

			responseUser = usuarioRepository.save(usuario);
			logger.debug("Usuario guardado");
		} catch (Exception e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), responseUser);
	}

	public ApiResponse update(String request) throws ApiException {
		Usuario responseUser;

		JsonNode root;
		String	username = null;
		Integer	idRol = null;
		Long idPersona = null;
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

			username = root.path("username").asText();
			logger.debug("username: {}", username);

			idPersona = root.path("idPersona").asLong();
			logger.debug("idPersona: {}", idPersona);

			idRol = root.path("idRol").asInt();
			logger.debug("idRol: {}", idRol);

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

			idDireccion = root.path("idDireccion").asInt();
			logger.debug("idDireccion: {}", idDireccion);

		} catch (JsonProcessingException e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}

		if (StringUtils.isBlank(username) || idRol == 0 || null == idRol ||
				StringUtils.isBlank(numeroDocumento) || StringUtils.isBlank(nombres)
				|| StringUtils.isBlank(apellidoPaterno)	|| idTipoDocumento == 0 || null == idTipoDocumento) {
			throw new ApiException(ApiError.EMPTY_OR_NULL_PARAMETER.getCode(), ApiError.EMPTY_OR_NULL_PARAMETER.getMessage());
		}

		logger.debug("Buscando tipo de documento {}", idTipoDocumento);
		TipoDocumento tipoDocumento = tipoDocumentoRepository.findById(idTipoDocumento)
				.orElseThrow(() -> new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage()));
		logger.debug("Tipo de documento {} encontrado", idTipoDocumento);

		logger.debug("Buscando rol {}", idRol);
		Rol rol = rolRepository.findById(idRol)
				.orElseThrow(() -> new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage()));
		logger.debug("Rol {} encontrado", idRol);

		Direccion direccion = null;
		if (0 != idDireccion) {
			direccion = direccionRepository.findById(idDireccion)
					.orElseThrow(() -> new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage()));
		}
		logger.debug("Encontró dirección {} ? {}", idDireccion, (null != direccion));

		boolean existsUsuario = usuarioRepository.existsById(idPersona);
		logger.debug("Usuario existe? {}", existsUsuario);
		if (!existsUsuario) {
			throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
		}

		try {
			usuarioRepository.updateUsuario(idPersona, username, rol, nombres, apellidoPaterno,
					apellidoMaterno, tipoDocumento, numeroDocumento, sexo.charAt(0), DateUtil.of(fechaNacimiento),
					email, imagen, direccion);
			logger.debug("Usuario actualizado");

			logger.debug("Obteniendo usuario actualizado");
			responseUser = usuarioRepository.findById(idPersona).orElse(null);
			logger.debug("Usuario: {}", responseUser);
			if (null == responseUser) {
				throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
			}

		} catch (Exception e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), responseUser);
	}

	public ApiResponse delete(Long id) throws ApiException {
		Usuario tmpUsuario = usuarioRepository.findById(id).orElse(null);
		logger.debug("Usuario: {}", tmpUsuario);
		if (null != tmpUsuario) {
			usuarioRepository.deleteById(id);
			logger.debug("Usuario eliminado");
			return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), "Usuario " + id + " eliminado");
		}
		throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
	}

	public ApiResponse updatePassword(String request) throws ApiException {
		Usuario responseUser;

		JsonNode root;
		Long idPersona = null;
		String	password = null;

		try {
			root = new ObjectMapper().readTree(request);

			idPersona = root.path("idPersona").asLong();
			password = root.path("password").asText();

			if (idPersona == 0 || idPersona == null || StringUtils.isBlank(password)) {
				throw new ApiException(ApiError.EMPTY_OR_NULL_PARAMETER.getCode(), ApiError.EMPTY_OR_NULL_PARAMETER.getMessage());
			}

			usuarioRepository.updatePassword(idPersona, passwordEnconder.encode(password));
			logger.debug("Contraseña actualizada");

			logger.debug("Obteniendo usuario actualizado");
			responseUser = usuarioRepository.findById(idPersona).orElse(null);
			logger.debug("Usuario: {}", responseUser);
			if (null == responseUser) {
				throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
			}

		} catch (JsonProcessingException e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), responseUser);
	}
}
