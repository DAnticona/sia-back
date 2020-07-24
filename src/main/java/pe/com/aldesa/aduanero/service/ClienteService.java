package pe.com.aldesa.aduanero.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import pe.com.aldesa.aduanero.constant.ApiError;
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
	
	public ApiResponse findAll() throws ApiException {
		List<Cliente> clientes = clienteRepository.findAll();
		int total = clientes.size();
		logger.debug("Total clientes: {}", total);
		if (clientes.isEmpty()) {
			throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), clientes, total);
	}
	
	public ApiResponse findById(Long id) throws ApiException {
		Cliente tmpCliente = clienteRepository.findById(id).orElse(null);
		logger.debug("Cliente: {}", tmpCliente);
		if (null == tmpCliente) {
			throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), tmpCliente);
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
		
		if (idTipoPersona == 0 || idTipoPersona == null || idPersona == null || idPersona == 0 || idEmpresa == null || idEmpresa == 0) {
			throw new ApiException(ApiError.EMPTY_OR_NULL_PARAMETER.getCode(), ApiError.EMPTY_OR_NULL_PARAMETER.getMessage());
		}
		
		logger.debug("Buscando tipo persona {}", idTipoPersona);
		TipoPersona tipoPersona = tipoPersonaRepository.findById(idTipoPersona)
				.orElseThrow(() -> new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage()));
		logger.debug("Tipo persona {} encontrado", idTipoPersona);
		
		logger.debug("Buscando Persona {}", idPersona);
		Persona persona = personaRepository.findById(idPersona)
				.orElseThrow(() -> new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage()));
		logger.debug("Persona {} encontrado", idPersona);
		
		logger.debug("Buscando empresa {}", idEmpresa);
		Empresa empresa = empresaRepository.findById(idEmpresa)
				.orElseThrow(() -> new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage()));
		logger.debug("Empresa {} encontrado", idEmpresa);
		
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
		
		if (id == 0 || null == id || idTipoPersona == 0 || idTipoPersona == null 
				|| idPersona == null || idPersona == 0 || idEmpresa == null || idEmpresa == 0) {
			throw new ApiException(ApiError.EMPTY_OR_NULL_PARAMETER.getCode(), ApiError.EMPTY_OR_NULL_PARAMETER.getMessage());
		}
		
		boolean existsCliente = clienteRepository.existsById(id);
		logger.debug("Existe Cliente? {}", existsCliente);
		if (!existsCliente) {
			throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
		}
		
		logger.debug("Buscando tipo persona {}", idTipoPersona);
		TipoPersona tipoPersona = tipoPersonaRepository.findById(idTipoPersona)
				.orElseThrow(() -> new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage()));
		logger.debug("Tipo persona {} encontrado", idTipoPersona);
		
		logger.debug("Buscando Persona {}", idPersona);
		Persona persona = personaRepository.findById(idPersona)
				.orElseThrow(() -> new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage()));
		logger.debug("Persona {} encontrado", idPersona);
		
		logger.debug("Buscando empresa {}", idEmpresa);
		Empresa empresa = empresaRepository.findById(idEmpresa)
				.orElseThrow(() -> new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage()));
		logger.debug("Empresa {} encontrado", idEmpresa);
		
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
