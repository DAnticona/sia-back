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
import pe.com.aldesa.aduanero.entity.CotizacionDetalle;
import pe.com.aldesa.aduanero.entity.CotizacionDetalleId;
import pe.com.aldesa.aduanero.entity.Servicio;
import pe.com.aldesa.aduanero.exception.ApiException;
import pe.com.aldesa.aduanero.repository.CotizacionDetalleRepository;
import pe.com.aldesa.aduanero.repository.ServicioRepository;

@Service
public class CotizacionDetalleService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CotizacionDetalleRepository cotizacionDetalleRepository;

	@Autowired
	private ServicioRepository servicioRepository;

	public ApiResponse findAll() {
		List<CotizacionDetalle> cotizacionDetalles = cotizacionDetalleRepository.findAll();
		int total = cotizacionDetalles.size();
		logger.debug("Total CotizacionDetalle: {}", total);
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), cotizacionDetalles, total);
	}

	public ApiResponse findById(Long idCotizacion, Integer item) {
		CotizacionDetalleId cotizacionDetalleId = new CotizacionDetalleId(idCotizacion, item);
		CotizacionDetalle cotizacionDetalle = cotizacionDetalleRepository.findById(cotizacionDetalleId).orElse(null);
		logger.debug("CotizacionDetalle: {}", cotizacionDetalle);
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), cotizacionDetalle);
	}

	public ApiResponse saveOrUpdate(String request) throws ApiException {
		CotizacionDetalle responseCotizacionDetalle;

		JsonNode root;
		Long idCotizacion = null;
		Integer	item = null;
		Integer idServicio = null;
		Double precio = null;

		try {
			root = new ObjectMapper().readTree(request);

			idCotizacion = root.path("idCotizacion").asLong();
			logger.debug("idCotizacion: {}", idCotizacion);

			item = root.path("item").asInt();
			logger.debug("item: {}", item);

			idServicio = root.path("idServicio").asInt();
			logger.debug("idServicio: {}", idServicio);

			precio = root.path("precio").asDouble();
			logger.debug("precio: {}", precio);

		} catch (JsonProcessingException e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}

		if (idCotizacion == 0 || item == 0 || idServicio == 0) {
			throw new ApiException(ApiError.EMPTY_OR_NULL_PARAMETER.getCode(), ApiError.EMPTY_OR_NULL_PARAMETER.getMessage());
		}

		Servicio servicio = servicioRepository.findById(idServicio)
				.orElseThrow(() -> new ApiException(ApiError.SERVICIO_NOT_FOUND.getCode(), ApiError.SERVICIO_NOT_FOUND.getMessage()));
		logger.debug("servicio: {}", servicio);

		try {
			CotizacionDetalle cotizacionDetalle = new CotizacionDetalle();
			cotizacionDetalle.setDetalleCotizacionId(new CotizacionDetalleId(idCotizacion, item));
			cotizacionDetalle.setServicio(servicio);
			cotizacionDetalle.setPrecio(precio);

			responseCotizacionDetalle = cotizacionDetalleRepository.save(cotizacionDetalle);
			logger.debug("CotizacionDetalle guardada");
		} catch (Exception e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), responseCotizacionDetalle);
	}

	public ApiResponse delete(Long idCotizacion, Integer item) throws ApiException {
		CotizacionDetalleId cotizacionDetalleId = new CotizacionDetalleId(idCotizacion, item);
		CotizacionDetalle tmpCotDetalle = cotizacionDetalleRepository.findById(cotizacionDetalleId).orElse(null);
		logger.debug("CotizacionDetalle: {}", tmpCotDetalle);
		if (null != tmpCotDetalle) {
			cotizacionDetalleRepository.deleteById(cotizacionDetalleId);
			logger.debug("CotizacionDetalle eliminada");
			return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), "CotizacionDetalle id: " + idCotizacion + " item: {}" + item + " eliminado");
		}
		throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
	}


}
