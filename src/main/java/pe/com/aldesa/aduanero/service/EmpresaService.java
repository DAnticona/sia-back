package pe.com.aldesa.aduanero.service;

import java.util.List;

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
import pe.com.aldesa.aduanero.entity.Direccion;
import pe.com.aldesa.aduanero.entity.Empresa;
import pe.com.aldesa.aduanero.exception.ApiException;
import pe.com.aldesa.aduanero.repository.DireccionRepository;
import pe.com.aldesa.aduanero.repository.EmpresaRepository;

@Service
public class EmpresaService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private EmpresaRepository empresaRepository;

	@Autowired
	private DireccionRepository direccionRepository;

	private static final int PAGE_LIMIT = 10;

	public ApiResponse findAll(Integer pageNumber) throws ApiException {
		Pageable pageable = PageRequest.of(pageNumber - 1, PAGE_LIMIT);
		Page<Empresa> empresasPage = empresaRepository.findAll(pageable);
		logger.debug("Empresa {} de: {}", pageNumber, empresasPage.getTotalPages());

		if (empresasPage.isEmpty()) {
			throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), empresasPage.getContent(), Math.toIntExact(empresasPage.getTotalElements()));
	}

	public ApiResponse findById(Long id) throws ApiException {
		Empresa empresa = empresaRepository.findById(id).orElse(null);
		logger.debug("Moneda: {}", empresa);
		if (null == empresa) {
			throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), empresa);
	}

	public ApiResponse findByRuc(String ruc) throws ApiException {
		List<Empresa> empresas = empresaRepository.findByRuc(ruc);
		if (empresas.size() > 1) {
			throw new ApiException(ApiError.MULTIPLES_SIMILAR_ELEMENTS.getCode(), ApiError.MULTIPLES_SIMILAR_ELEMENTS.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), empresas.get(0));
	}

	public ApiResponse findByRazonSocial(String razonSocial) {
		List<Empresa> empresas = empresaRepository.findByRazonSocial(razonSocial);
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), empresas);
	}

	public ApiResponse findByNombreComercial(String nombrecomercial) {
		List<Empresa> empresas = empresaRepository.findByNombreComercial(nombrecomercial);
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), empresas);
	}

	public ApiResponse save(String request) throws ApiException {
		Empresa responseEmpresa;

		JsonNode root;
		String	ruc = null;
		String	razonSocial = null;
		String	nombreComercial = null;
		Integer	idDireccion = null;
		try {
			root = new ObjectMapper().readTree(request);

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

		if (StringUtils.isBlank(ruc) || StringUtils.isBlank(razonSocial)) {
			throw new ApiException(ApiError.EMPTY_OR_NULL_PARAMETER.getCode(), ApiError.EMPTY_OR_NULL_PARAMETER.getMessage());
		}

		Direccion direccion = null;
		if (idDireccion != 0) {
			direccion = direccionRepository.findById(idDireccion)
					.orElseThrow(() -> new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage()));
		}
		logger.debug("Encontró dirección {} ? {}", idDireccion, (null != direccion));

		try {
			Empresa empresa = new Empresa();
			empresa.setRuc(ruc);
			empresa.setRazonSocial(razonSocial);
			empresa.setDireccion(direccion);
			empresa.setNombreComercial(nombreComercial);

			responseEmpresa = empresaRepository.save(empresa);
			logger.debug("Empresa guardada");
		} catch (Exception e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), responseEmpresa);
	}

	public ApiResponse update(String request) throws ApiException {
		Empresa responseEmpresa;

		JsonNode root;
		Long id = null;
		String	ruc = null;
		String	razonSocial = null;
		String	nombreComercial = null;
		Integer	idDireccion = null;
		try {
			root = new ObjectMapper().readTree(request);

			id = root.path("id").asLong();
			logger.debug("id: {}", id);

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

		if (null == id || id == 0 || StringUtils.isBlank(ruc) || StringUtils.isBlank(razonSocial)) {
			throw new ApiException(ApiError.EMPTY_OR_NULL_PARAMETER.getCode(), ApiError.EMPTY_OR_NULL_PARAMETER.getMessage());
		}

		boolean existsEmpresa = empresaRepository.existsById(id);
		logger.debug("Existe empresa {}? {}", id, existsEmpresa);
		if (!existsEmpresa) {
			throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
		}

		Direccion direccion = null;
		if (idDireccion != 0) {
			direccion = direccionRepository.findById(idDireccion)
					.orElseThrow(() -> new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage()));
		}
		logger.debug("Encontró dirección {} ? {}", idDireccion, (null != direccion));

		try {
			Empresa empresa = new Empresa();
			empresa.setIdEmpresa(id);
			empresa.setRuc(ruc);
			empresa.setRazonSocial(razonSocial);
			empresa.setDireccion(direccion);
			empresa.setNombreComercial(nombreComercial);

			responseEmpresa = empresaRepository.save(empresa);
			logger.debug("Empresa actualizada");
		} catch (Exception e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), responseEmpresa);
	}

	public ApiResponse delete(Long id) throws ApiException {
		Empresa tmpEmpresa = empresaRepository.findById(id).orElse(null);
		logger.debug("Empresa: {}", tmpEmpresa);
		if (null != tmpEmpresa) {
			empresaRepository.deleteById(id);
			logger.debug("Empresa eliminada");
			return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), "Empresa " + id + " eliminada");
		}
		throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
	}

}
