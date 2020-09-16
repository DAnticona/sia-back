package pe.com.aldesa.aduanero.service;

import java.util.List;
import java.util.Optional;

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
import pe.com.aldesa.aduanero.constant.PersonType;
import pe.com.aldesa.aduanero.dto.ApiResponse;
import pe.com.aldesa.aduanero.entity.Cliente;
import pe.com.aldesa.aduanero.entity.Empresa;
import pe.com.aldesa.aduanero.entity.Persona;
import pe.com.aldesa.aduanero.entity.TipoPersona;
import pe.com.aldesa.aduanero.exception.ApiException;
import pe.com.aldesa.aduanero.repository.ClienteRepository;
import pe.com.aldesa.aduanero.repository.EmpresaRepository;
import pe.com.aldesa.aduanero.repository.PersonaRepository;
import pe.com.aldesa.aduanero.repository.TipoPersonaRepository;

@Service
public class ClienteService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private TipoPersonaRepository tipoPersonaRepository;

	@Autowired
	private PersonaRepository personaRepository;

	@Autowired
	private EmpresaRepository empresaRepository;

	private static final int PAGE_LIMIT = 10;

	public ApiResponse findAll(Integer pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber - 1, PAGE_LIMIT);
		Page<Cliente> clientesPage = clienteRepository.findAll(pageable);
		logger.debug("Página {} de: {}", pageNumber, clientesPage.getTotalPages());
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), clientesPage.getContent(), Math.toIntExact(clientesPage.getTotalElements()));
	}

	public ApiResponse findById(Long id) {
		Cliente tmpCliente = clienteRepository.findById(id).orElse(null);
		logger.debug("Cliente: {}", tmpCliente);
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), tmpCliente);
	}

	public ApiResponse findByNombre(String nombre) {
		List<Cliente> clientes = clienteRepository.findByNombre(nombre);
		logger.debug("Cliente by Nombre: {}", clientes);
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), clientes, clientes.size());
	}

	public ApiResponse findByNumeroDocumento(String numeroDocumento) {
		Optional<Cliente> optCli = clienteRepository.findByNumeroDocumento(numeroDocumento);
		logger.debug("Cliente by Numero de documento: {}", optCli);
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), optCli);
	}

	public ApiResponse findByRazonSocial(String razonSocial) {
		List<Cliente> clientes = clienteRepository.findByRazonSocial(razonSocial);
		logger.debug("Cliente by Razon Social: {}", clientes);
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), clientes, clientes.size());
	}

	public ApiResponse findByRuc(String ruc) {
		Optional<Cliente> optCli = clienteRepository.findByRuc(ruc);
		logger.debug("Cliente by RUC: {}", optCli);
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), optCli);
	}

	public ApiResponse save(String request) throws ApiException {
		Cliente responseCliente;

		JsonNode root;
		Integer	idTipoPersona = null;
		Long idPersona = null;
		Long idEmpresa = null;
		try {
			root = new ObjectMapper().readTree(request);

			idTipoPersona = root.path("idTipoPersona").asInt();
			logger.debug("idTipoPersona: {}", idTipoPersona);

			idPersona = root.path("idPersona").asLong();
			logger.debug("idPersona: {}", idPersona);

			idEmpresa = root.path("idEmpresa").asLong();
			logger.debug("idEmpresa: {}", idEmpresa);

		} catch (JsonProcessingException e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}

		if (null == idTipoPersona || idTipoPersona == 0) {
			throw new ApiException(ApiError.EMPTY_OR_NULL_PARAMETER.getCode(), ApiError.EMPTY_OR_NULL_PARAMETER.getMessage());
		}

		logger.debug("Buscando tipo persona {}", idTipoPersona);
		TipoPersona tipoPersona = tipoPersonaRepository.findById(idTipoPersona)
				.orElseThrow(() -> new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage()));
		logger.debug("Tipo persona {} encontrado", idTipoPersona);

		Persona persona = null;
		if (PersonType.NATURAL.equals(idTipoPersona)) {
			logger.debug("Buscando Persona {}", idPersona);
			persona = personaRepository.findById(idPersona)
					.orElseThrow(() -> new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage()));
			logger.debug("Persona {} encontrado", idPersona);

		}

		// Valida que no se vuelva a repetir la combinación tipoPersona con Persona en la tabla cliente
		if (null != tipoPersona && null != persona) {
			boolean exists = clienteRepository.existsCliente(tipoPersona, persona);
			logger.debug("Exists cliente con tipoPersona: {} y persona: {}? {}", tipoPersona.getIdTipoPersona(), persona.getIdPersona(), exists);
			if (exists)
				throw new ApiException(ApiError.ALREADY_EXISTS.getCode(), ApiError.ALREADY_EXISTS.getMessage());
		}

		Empresa empresa = null;
		if (PersonType.LEGAL.equals(idTipoPersona)) {
			logger.debug("Buscando empresa {}", idEmpresa);
			empresa = empresaRepository.findById(idEmpresa)
					.orElseThrow(() -> new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage()));
			logger.debug("Empresa {} encontrado", idEmpresa);
		}

		// Valida que no se vuelva a repetir la combinación tipoPersona con Empresa en la tabla cliente
		if (null != tipoPersona && null != empresa) {
			boolean exists = clienteRepository.existsCliente(tipoPersona, empresa);
			logger.debug("Exists cliente con tipoPersona: {} y empresa: {}? {}", tipoPersona.getIdTipoPersona(), empresa.getIdEmpresa(), exists);
			if (exists)
				throw new ApiException(ApiError.ALREADY_EXISTS.getCode(), ApiError.ALREADY_EXISTS.getMessage());
		}

		try {
			Cliente cliente = new Cliente();
			cliente.setTipoPersona(tipoPersona);
			cliente.setPersona(persona);
			cliente.setEmpresa(empresa);

			responseCliente = clienteRepository.save(cliente);
			logger.debug("Cliente guardado");
		} catch (Exception e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), responseCliente);
	}

	public ApiResponse update(String request) throws ApiException {
		Cliente responseCliente;

		JsonNode root;
		Long id = null;
		Integer	idTipoPersona = null;
		Long idPersona = null;
		Long idEmpresa = null;
		try {
			root = new ObjectMapper().readTree(request);

			id = root.path("id").asLong();
			logger.debug("id: {}", id);

			idTipoPersona = root.path("idTipoPersona").asInt();
			logger.debug("idTipoPersona: {}", idTipoPersona);

			idPersona = root.path("idPersona").asLong();
			logger.debug("idPersona: {}", idPersona);

			idEmpresa = root.path("idEmpresa").asLong();
			logger.debug("idEmpresa: {}", idEmpresa);

		} catch (JsonProcessingException e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}

		if (null == id || id == 0 || null == idTipoPersona || idTipoPersona == 0) {
			throw new ApiException(ApiError.EMPTY_OR_NULL_PARAMETER.getCode(), ApiError.EMPTY_OR_NULL_PARAMETER.getMessage());
		}

		boolean existCliente = clienteRepository.existsById(id);
		logger.debug("Existe cliente? {} {}", id, existCliente);
		if (!existCliente ) {
			throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
		}

		logger.debug("Buscando tipo persona {}", idTipoPersona);
		TipoPersona tipoPersona = tipoPersonaRepository.findById(idTipoPersona)
				.orElseThrow(() -> new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage()));
		logger.debug("Tipo persona {} encontrado", idTipoPersona);

		Persona persona = null;
		if (PersonType.NATURAL.equals(idTipoPersona)) {
			logger.debug("Buscando Persona {}", idPersona);
			persona = personaRepository.findById(idPersona)
					.orElseThrow(() -> new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage()));
			logger.debug("Persona {} encontrado", idPersona);
		}

		Empresa empresa = null;
		if (PersonType.LEGAL.equals(idTipoPersona)) {
			logger.debug("Buscando empresa {}", idEmpresa);
			empresa = empresaRepository.findById(idEmpresa)
					.orElseThrow(() -> new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage()));
			logger.debug("Empresa {} encontrado", idEmpresa);
		}

		try {
			Cliente cliente = new Cliente();
			cliente.setIdCliente(id);
			cliente.setTipoPersona(tipoPersona);
			cliente.setPersona(persona);
			cliente.setEmpresa(empresa);

			responseCliente = clienteRepository.save(cliente);
			logger.debug("Cliente guardado");
		} catch (Exception e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), responseCliente);
	}

	public ApiResponse delete(Long id) throws ApiException {
		Cliente tmpCliente = clienteRepository.findById(id).orElse(null);
		logger.debug("Cliente: {}", tmpCliente);
		if (null != tmpCliente) {
			clienteRepository.deleteById(id);
			logger.debug("Cliente eliminado");
			return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), "Cliente " + id + " eliminado");
		}
		throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
	}

}
