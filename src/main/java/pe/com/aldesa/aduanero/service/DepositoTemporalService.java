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
import pe.com.aldesa.aduanero.entity.DepositoTemporal;
import pe.com.aldesa.aduanero.entity.Empresa;
import pe.com.aldesa.aduanero.exception.ApiException;
import pe.com.aldesa.aduanero.repository.DepositoTemporalRepository;
import pe.com.aldesa.aduanero.repository.EmpresaRepository;

@Service
public class DepositoTemporalService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private DepositoTemporalRepository depositoTemporalRepository;

	@Autowired
	private EmpresaRepository empresaRepository;

	public ApiResponse findAll() {
		List<DepositoTemporal> depositos = depositoTemporalRepository.findAll();
		int total = depositos.size();
		logger.debug("Total DepositoTemporal: {}", total);
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), depositos, total);
	}

	public ApiResponse findById(Integer id) {
		DepositoTemporal tmpDeposito = depositoTemporalRepository.findById(id).orElse(null);
		logger.debug("DepositoTemporal: {}", tmpDeposito);
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), tmpDeposito);
	}

	public ApiResponse save(String request) throws ApiException {
		DepositoTemporal responseDepositoTemporal;

		JsonNode root;
		Long idEmpresa = null;
		String	codigoAduana = null;
		try {
			root = new ObjectMapper().readTree(request);

			idEmpresa = root.path("idEmpresa").asLong();
			logger.debug("idEmpresa: {}", idEmpresa);

			codigoAduana = root.path("codigoAduana").asText();
			logger.debug("codigoAduana: {}", codigoAduana);

		} catch (JsonProcessingException e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}

		if (StringUtils.isBlank(codigoAduana) || null == idEmpresa || idEmpresa == 0) {
			throw new ApiException(ApiError.EMPTY_OR_NULL_PARAMETER.getCode(), ApiError.EMPTY_OR_NULL_PARAMETER.getMessage());
		}

		Empresa empresa = empresaRepository.findById(idEmpresa)
				.orElseThrow(() -> new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage()));

		try {
			DepositoTemporal deposito = new DepositoTemporal();
			deposito.setEmpresa(empresa);
			deposito.setCodigoAduana(codigoAduana);

			responseDepositoTemporal = depositoTemporalRepository.save(deposito);
			logger.debug("Deposito guradado");
		} catch (Exception e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), responseDepositoTemporal);
	}

	public ApiResponse update(String request) throws ApiException {
		DepositoTemporal responseDepositoTemporal;

		JsonNode root;
		Integer id = null;
		Long idEmpresa = null;
		String	codigoAduana = null;
		try {
			root = new ObjectMapper().readTree(request);

			idEmpresa = root.path("idEmpresa").asLong();
			logger.debug("idEmpresa: {}", idEmpresa);

			id = root.path("id").asInt();
			logger.debug("id: {}", id);

			codigoAduana = root.path("codigoAduana").asText();
			logger.debug("codigoAduana: {}", codigoAduana);

		} catch (JsonProcessingException e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}

		if (null == id || id == 0 || StringUtils.isBlank(codigoAduana) || null == idEmpresa || idEmpresa == 0) {
			throw new ApiException(ApiError.EMPTY_OR_NULL_PARAMETER.getCode(), ApiError.EMPTY_OR_NULL_PARAMETER.getMessage());
		}

		boolean existsDeposito = depositoTemporalRepository.existsById(id);
		logger.debug("Existe depósito? {}", existsDeposito);
		if (!existsDeposito) {
			throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
		}

		Empresa empresa = empresaRepository.findById(idEmpresa)
				.orElseThrow(() -> new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage()));

		try {
			DepositoTemporal deposito = new DepositoTemporal();
			deposito.setIdDeposito(id);
			deposito.setEmpresa(empresa);
			deposito.setCodigoAduana(codigoAduana);

			responseDepositoTemporal = depositoTemporalRepository.save(deposito);
			logger.debug("Deposito actualizado");
		} catch (Exception e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), responseDepositoTemporal);
	}

	public ApiResponse delete(Integer id) throws ApiException {
		DepositoTemporal tmpDepositoTemporal = depositoTemporalRepository.findById(id).orElse(null);
		logger.debug("DepositoTemporal: {}", tmpDepositoTemporal);
		if (null != tmpDepositoTemporal) {
			depositoTemporalRepository.deleteById(id);
			logger.debug("DepositoTemporal eliminado");
			return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), "DepositoTemporal " + id + " eliminado");
		}
		throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
	}


}
