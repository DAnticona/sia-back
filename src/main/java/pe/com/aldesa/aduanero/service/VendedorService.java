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
import pe.com.aldesa.aduanero.entity.Direccion;
import pe.com.aldesa.aduanero.entity.TipoDocumento;
import pe.com.aldesa.aduanero.entity.Vendedor;
import pe.com.aldesa.aduanero.exception.ApiException;
import pe.com.aldesa.aduanero.repository.DireccionRepository;
import pe.com.aldesa.aduanero.repository.TipoDocumentoRepository;
import pe.com.aldesa.aduanero.repository.VendedorRepository;
import pe.com.aldesa.aduanero.util.DateUtil;

@Service
public class VendedorService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private VendedorRepository vendedorRepository;

	@Autowired
	private TipoDocumentoRepository tipoDocumentoRepository;

	@Autowired
	private DireccionRepository direccionRepository;

	public ApiResponse findAll() throws ApiException {
		List<Vendedor> vendedores = vendedorRepository.findAll();
		int total = vendedores.size();
		logger.debug("Total Vendedores: {}", total);
		if (vendedores.isEmpty()) {
			throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), vendedores, total);
	}

	public ApiResponse findById(Long id) throws ApiException {
		Vendedor tmpVendedor = vendedorRepository.findById(id).orElse(null);
		logger.debug("Vendedor: {}", tmpVendedor);
		if (null == tmpVendedor) {
			throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), tmpVendedor);
	}

	public ApiResponse save(String request) throws ApiException {
		Vendedor responseVendedor;

		JsonNode root;
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

		if (StringUtils.isBlank(numeroDocumento) || StringUtils.isBlank(nombres)
				|| StringUtils.isBlank(apellidoPaterno)	|| null == idTipoDocumento || idTipoDocumento == 0) {
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
			Vendedor vendedor = new Vendedor();
			vendedor.setNombres(nombres);
			vendedor.setApellidoPaterno(apellidoPaterno);
			vendedor.setApellidoMaterno(apellidoMaterno);
			vendedor.setTipoDocumento(tipoDocumento);
			vendedor.setNumeroDocumento(numeroDocumento);
			vendedor.setSexo(sexo.charAt(0));
			vendedor.setFechaNacimiento(DateUtil.of(fechaNacimiento));
			vendedor.setEmail(email);
			vendedor.setImagen(imagen);
			vendedor.setDireccion(direccion);

			responseVendedor = vendedorRepository.save(vendedor);
			logger.debug("Vendedor guardado");
		} catch (Exception e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), responseVendedor);
	}

	public ApiResponse update(String request) throws ApiException {
		Vendedor responseVendedor;

		JsonNode root;
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

			idPersona = root.path("idPersona").asLong();
			logger.debug("idPersona: {}", idPersona);

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

		if (StringUtils.isBlank(numeroDocumento) || StringUtils.isBlank(nombres)
				|| StringUtils.isBlank(apellidoPaterno)	|| null == idTipoDocumento || idTipoDocumento == 0) {
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

		boolean existsVendedor = vendedorRepository.existsById(idPersona);
		logger.debug("Vendedor existe? {}", existsVendedor);
		if (!existsVendedor) {
			throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
		}

		try {
			Vendedor vendedor = new Vendedor();
			vendedor.setIdPersona(idPersona);
			vendedor.setNombres(nombres);
			vendedor.setApellidoPaterno(apellidoPaterno);
			vendedor.setApellidoMaterno(apellidoMaterno);
			vendedor.setTipoDocumento(tipoDocumento);
			vendedor.setNumeroDocumento(numeroDocumento);
			vendedor.setSexo(sexo.charAt(0));
			vendedor.setFechaNacimiento(DateUtil.of(fechaNacimiento));
			vendedor.setEmail(email);
			vendedor.setImagen(imagen);
			vendedor.setDireccion(direccion);

			responseVendedor = vendedorRepository.save(vendedor);
			logger.debug("Vendedor actualizado");
		} catch (Exception e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), responseVendedor);
	}

	public ApiResponse delete(Long id) throws ApiException {
		Vendedor tmpVendedor = vendedorRepository.findById(id).orElse(null);
		logger.debug("Vendedor: {}", tmpVendedor);
		if (null != tmpVendedor) {
			vendedorRepository.deleteById(id);
			logger.debug("Vendedor eliminado");
			return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), "Vendedor " + id + " eliminado");
		}
		throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
	}

}
