package pe.com.aldesa.aduanero.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import pe.com.aldesa.aduanero.constant.ApiError;
import pe.com.aldesa.aduanero.dto.ApiResponse;
import pe.com.aldesa.aduanero.entity.Cotizacion;
import pe.com.aldesa.aduanero.entity.CotizacionDetalle;
import pe.com.aldesa.aduanero.entity.Servicio;
import pe.com.aldesa.aduanero.exception.ApiException;
import pe.com.aldesa.aduanero.repository.CotizacionDetalleRepository;
import pe.com.aldesa.aduanero.repository.CotizacionRepository;
import pe.com.aldesa.aduanero.repository.ServicioRepository;

@Service
public class CotizacionDetalleService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CotizacionDetalleRepository cotizacionDetalleRepository;

	@Autowired
	private CotizacionRepository cotizacionRepository;

	@Autowired
	private ServicioRepository servicioRepository;

	public ApiResponse findAll() {
		List<CotizacionDetalle> cotizacionDetalles = cotizacionDetalleRepository.findAll();
		int total = cotizacionDetalles.size();
		logger.debug("Total CotizacionDetalle: {}", total);
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), cotizacionDetalles, total);
	}

	public ApiResponse searchByIdCotizacionAndItem(Long idCotizacion, Integer item) throws ApiException {
		Cotizacion cotizacion = cotizacionRepository.findById(idCotizacion)
				.orElseThrow(() -> new ApiException(ApiError.COTIZACION_NOT_FOUND.getCode(), ApiError.COTIZACION_NOT_FOUND.getMessage()));
		Optional<CotizacionDetalle> cotizacionDetalle = cotizacionDetalleRepository.searchByIdCotizacionAndItem(cotizacion, item);
		if (cotizacionDetalle.isPresent()) {
			return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), cotizacionDetalle.get());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), null);
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

		Cotizacion cotizacion = cotizacionRepository.findById(idCotizacion).
				orElseThrow(() -> new ApiException(ApiError.COTIZACION_NOT_FOUND.getCode(), ApiError.COTIZACION_NOT_FOUND.getMessage()));
		logger.debug("cotizacion: {}", cotizacion);

		try {
			CotizacionDetalle cotizacionDetalle = new CotizacionDetalle();
			cotizacionDetalle.setCotizacion(cotizacion);
			cotizacionDetalle.setItem(item);
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
		Cotizacion cotizacion = cotizacionRepository.findById(idCotizacion)
				.orElseThrow(() -> new ApiException(ApiError.COTIZACION_NOT_FOUND.getCode(), ApiError.COTIZACION_NOT_FOUND.getMessage()));
		CotizacionDetalle tmpCotDetalle = cotizacionDetalleRepository.searchByIdCotizacionAndItem(cotizacion, item).orElse(null);
		logger.debug("CotizacionDetalle: {}", tmpCotDetalle);
		if (null != tmpCotDetalle) {
			cotizacionDetalleRepository.deleteByIdCotizacionAndItem(cotizacion, item);
			logger.debug("Línea de cotización eliminada");
			return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), "Línea id: " + idCotizacion + " item: {}" + item + " eliminada");
		}
		throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
	}

}
