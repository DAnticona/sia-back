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
import pe.com.aldesa.aduanero.entity.TipoVehiculo;
import pe.com.aldesa.aduanero.entity.Vehiculo;
import pe.com.aldesa.aduanero.exception.ApiException;
import pe.com.aldesa.aduanero.repository.TipoVehiculoRepository;
import pe.com.aldesa.aduanero.repository.VehiculoRepository;

@Service
public class VehiculoService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private VehiculoRepository vehiculoRepository;

	@Autowired
	private TipoVehiculoRepository tipoVehiculoRepository;

	public ApiResponse findAll() {
		List<Vehiculo> vehiculos = vehiculoRepository.findAll();
		int total = vehiculos.size();
		logger.debug("Total Vehiculos: {}", total);
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), vehiculos, total);
	}

	public ApiResponse findById(Long id) {
		Vehiculo tmpCamion = vehiculoRepository.findById(id).orElse(null);
		logger.debug("Vehiculo: {}", tmpCamion);
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), tmpCamion);
	}

	public ApiResponse save(String request) throws ApiException {
		Vehiculo responseVehiculo;

		JsonNode root;
		Integer codeTipoVehiculo = null;
		String placa = null;
		String marca = null;
		String certificado = null;
		Double largo = null;
		Double ancho = null;
		Double alto = null;
		Double peso = null;
		String eje = null;
		try {
			root = new ObjectMapper().readTree(request);

			codeTipoVehiculo = root.path("codeTipoVehiculo").asInt();
			logger.debug("codeTipoVehiculo: {}", codeTipoVehiculo);

			placa = root.path("placa").asText();
			logger.debug("placa: {}", placa);

			marca = root.path("marca").asText();
			logger.debug("marca: {}", marca);

			certificado = root.path("certificado").asText();
			logger.debug("certificado: {}", certificado);

			largo = root.path("largo").asDouble();
			logger.debug("largo: {}", largo);

			ancho = root.path("ancho").asDouble();
			logger.debug("ancho: {}", ancho);

			alto = root.path("alto").asDouble();
			logger.debug("alto: {}", alto);

			peso = root.path("peso").asDouble();
			logger.debug("peso: {}", peso);

			eje = root.path("eje").asText();
			logger.debug("eje: {}", eje);

		} catch (JsonProcessingException e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}

		if (0 == codeTipoVehiculo || StringUtils.isBlank(placa) || StringUtils.isBlank(marca)
				|| StringUtils.isBlank(certificado) || null == largo || null == ancho
				|| null == alto || null == peso || null == eje) {
			throw new ApiException(ApiError.EMPTY_OR_NULL_PARAMETER.getCode(), ApiError.EMPTY_OR_NULL_PARAMETER.getMessage());
		}

		TipoVehiculo tipoVehiculo = tipoVehiculoRepository.findById(codeTipoVehiculo)
				.orElseThrow(() -> new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage()));

		try {
			Vehiculo vehiculo = new Vehiculo();
			vehiculo.setTipoVehiculo(tipoVehiculo);
			vehiculo.setPlaca(placa);
			vehiculo.setMarca(marca);
			vehiculo.setCertificado(certificado);
			vehiculo.setLargo(largo);
			vehiculo.setAncho(ancho);
			vehiculo.setAlto(alto);
			vehiculo.setPeso(peso);
			vehiculo.setEje(eje);

			responseVehiculo = vehiculoRepository.save(vehiculo);
			logger.debug("Vehiculo guardado");
		} catch (Exception e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), responseVehiculo);
	}

	public ApiResponse update(String request) throws ApiException {
		Vehiculo responseVehiculo;

		JsonNode root;
		Long id = null;
		Integer codeTipoVehiculo = null;
		String placa = null;
		String marca = null;
		String certificado = null;
		Double largo = null;
		Double ancho = null;
		Double alto = null;
		Double peso = null;
		String eje = null;
		try {
			root = new ObjectMapper().readTree(request);

			id = root.path("id").asLong();
			logger.debug("id: {}", id);

			codeTipoVehiculo = root.path("codeTipoVehiculo").asInt();
			logger.debug("codeTipoVehiculo: {}", codeTipoVehiculo);

			placa = root.path("placa").asText();
			logger.debug("placa: {}", placa);

			marca = root.path("marca").asText();
			logger.debug("marca: {}", marca);

			certificado = root.path("certificado").asText();
			logger.debug("certificado: {}", certificado);

			largo = root.path("largo").asDouble();
			logger.debug("largo: {}", largo);

			ancho = root.path("ancho").asDouble();
			logger.debug("ancho: {}", ancho);

			alto = root.path("alto").asDouble();
			logger.debug("alto: {}", alto);

			peso = root.path("peso").asDouble();
			logger.debug("peso: {}", peso);

			eje = root.path("eje").asText();
			logger.debug("eje: {}", eje);

		} catch (JsonProcessingException e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}

		if (null == id || id == 0 || null == codeTipoVehiculo || StringUtils.isBlank(placa) || StringUtils.isBlank(marca)
				|| StringUtils.isBlank(certificado) || null == largo || null == ancho
				|| null == alto || null == peso || null == eje) {
			throw new ApiException(ApiError.EMPTY_OR_NULL_PARAMETER.getCode(), ApiError.EMPTY_OR_NULL_PARAMETER.getMessage());
		}

		TipoVehiculo tipoVehiculo = tipoVehiculoRepository.findById(codeTipoVehiculo)
				.orElseThrow(() -> new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage()));

		try {
			Vehiculo vehiculo = new Vehiculo();
			vehiculo.setIdVehiculo(id);
			vehiculo.setTipoVehiculo(tipoVehiculo);
			vehiculo.setPlaca(placa);
			vehiculo.setMarca(marca);
			vehiculo.setCertificado(certificado);
			vehiculo.setLargo(largo);
			vehiculo.setAncho(ancho);
			vehiculo.setAlto(alto);
			vehiculo.setPeso(peso);
			vehiculo.setEje(eje);

			responseVehiculo = vehiculoRepository.save(vehiculo);
			logger.debug("Vehiculo actualizado");
		} catch (Exception e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), responseVehiculo);
	}

	public ApiResponse delete(Long id) throws ApiException {
		Vehiculo tmpCamion = vehiculoRepository.findById(id).orElse(null);
		logger.debug("Camion: {}", tmpCamion);
		if (null != tmpCamion) {
			vehiculoRepository.deleteById(id);
			logger.debug("Vehiculo eliminado");
			return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), "Camion " + id + " eliminado");
		}
		throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
	}

}
