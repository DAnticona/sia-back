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
import pe.com.aldesa.aduanero.entity.Cliente;
import pe.com.aldesa.aduanero.entity.Cotizacion;
import pe.com.aldesa.aduanero.entity.Dam;
import pe.com.aldesa.aduanero.entity.Tarjeta;
import pe.com.aldesa.aduanero.entity.TipoMercancia;
import pe.com.aldesa.aduanero.entity.Ubicacion;
import pe.com.aldesa.aduanero.exception.ApiException;
import pe.com.aldesa.aduanero.repository.ClienteRepository;
import pe.com.aldesa.aduanero.repository.CotizacionRepository;
import pe.com.aldesa.aduanero.repository.DamRepository;
import pe.com.aldesa.aduanero.repository.TarjetaRepository;
import pe.com.aldesa.aduanero.repository.TipoMercanciaRepository;
import pe.com.aldesa.aduanero.repository.UbicacionRepository;
import pe.com.aldesa.aduanero.util.DateUtil;

@Service
public class TarjetaService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TarjetaRepository tarjetaRepository;

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private CotizacionRepository cotizacionRepository;

	@Autowired
	private DamRepository damRepository;

	@Autowired
	private UbicacionRepository ubicacionRepository;

	@Autowired
	private TipoMercanciaRepository tipoMercanciaRepository;

	public ApiResponse findAll() {
		List<Tarjeta> tarjetas = tarjetaRepository.findAll();
		int total = tarjetas.size();
		logger.debug("Total Tarjetas: {}", total);
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), tarjetas, total);
	}

	public ApiResponse findById(Long id) {
		Tarjeta tmpTarjeta = tarjetaRepository.findById(id).orElse(null);
		logger.debug("Tarjeta: {}", tmpTarjeta);
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), tmpTarjeta);
	}

	public ApiResponse save(String request) throws ApiException {
		Tarjeta responseTarjeta;

		JsonNode root;
		Long idCliente = null;
		Long idCotizacion = null;
		Long idDam = null;
		Long idUbicacion = null;
		Integer idTipoMercancia = null;
		String fecha = null;
		String vapAvi = null;
		String observacion = null;
		String levante = null;
		Integer nuCont20Suelta;
		Integer nuCont40;

		try {
			root = new ObjectMapper().readTree(request);

			idCliente = root.path("idCliente").asLong();
			logger.debug("idCliente: {}", idCliente);

			idCotizacion = root.path("idCotizacion").asLong();
			logger.debug("idCotizacion: {}", idCotizacion);

			idDam = root.path("idDam").asLong();
			logger.debug("idDam: {}", idDam);

			idUbicacion = root.path("idUbicacion").asLong();
			logger.debug("idUbicacion: {}", idUbicacion);

			idTipoMercancia = root.path("idTipoMercancia").asInt();
			logger.debug("idTipoMercancia: {}", idTipoMercancia);

			fecha = root.path("fecha").asText();
			logger.debug("fecha: {}", fecha);

			vapAvi = root.path("vapAvi").asText();
			logger.debug("vapAvi: {}", vapAvi);

			observacion = root.path("observacion").asText();
			logger.debug("observacion: {}", observacion);

			levante = root.path("levante").asText();
			logger.debug("levante: {}", levante);

			nuCont20Suelta = root.path("nuCont20Suelta").asInt();
			logger.debug("nuCont20Suelta: {}", nuCont20Suelta);

			nuCont40 = root.path("nuCont40").asInt();
			logger.debug("nuCont40: {}", nuCont40);

		} catch (JsonProcessingException e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}

		if (idCliente == 0 || idCotizacion == 0 || idDam == 0 || idUbicacion == 0 || idTipoMercancia == 0 || "null".equals(fecha) || StringUtils.isBlank(fecha)) {
			throw new ApiException(ApiError.EMPTY_OR_NULL_PARAMETER.getCode(), ApiError.EMPTY_OR_NULL_PARAMETER.getMessage());
		}

		Cliente cliente = clienteRepository.findById(idCliente)
				.orElseThrow(() -> new ApiException(ApiError.CLIENTE_NOT_FOUND.getCode(), ApiError.CLIENTE_NOT_FOUND.getMessage()));

		Cotizacion cotizacion = cotizacionRepository.findById(idCotizacion)
				.orElseThrow(() -> new ApiException(ApiError.COTIZACION_NOT_FOUND.getCode(), ApiError.COTIZACION_NOT_FOUND.getMessage()));

		Dam dam = damRepository.findById(idDam)
				.orElseThrow(() -> new ApiException(ApiError.DAM_NOT_FOUND.getCode(), ApiError.DAM_NOT_FOUND.getMessage()));

		Ubicacion ubicacion = ubicacionRepository.findById(idUbicacion)
				.orElseThrow(() -> new ApiException(ApiError.UBICACION_NOT_FOUND.getCode(), ApiError.UBICACION_NOT_FOUND.getMessage()));

		TipoMercancia tipoMercancia = tipoMercanciaRepository.findById(idTipoMercancia)
				.orElseThrow(() -> new ApiException(ApiError.TIPO_MERCANCIA_NOT_FOUND.getCode(), ApiError.TIPO_MERCANCIA_NOT_FOUND.getMessage()));

		try {
			Tarjeta tarjeta = new Tarjeta();
			tarjeta.setCliente(cliente);
			tarjeta.setCotizacion(cotizacion);
			tarjeta.setDam(dam);
			tarjeta.setUbicacion(ubicacion);
			tarjeta.setTipoMercancia(tipoMercancia);
			tarjeta.setFecha(DateUtil.of(fecha));
			tarjeta.setVapAvi(vapAvi);
			tarjeta.setObservacion(observacion);
			tarjeta.setLevante(levante);
			tarjeta.setNuCont20Suelta(nuCont20Suelta);
			tarjeta.setNuCont40(nuCont40);

			responseTarjeta = tarjetaRepository.save(tarjeta);
			logger.debug("Tarjeta guardada");
		} catch (Exception e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), responseTarjeta);
	}

	public ApiResponse update(String request) throws ApiException {
		Tarjeta responseTarjeta;

		JsonNode root;
		Long id = null;
		Long idCliente = null;
		Long idCotizacion = null;
		Long idDam = null;
		Long idUbicacion = null;
		Integer idTipoMercancia = null;
		String fecha = null;
		String vapAvi = null;
		String observacion = null;
		String levante = null;
		int nuCont20Suelta;
		int nuCont40;

		try {
			root = new ObjectMapper().readTree(request);

			id = root.path("id").asLong();
			logger.debug("id: {}", id);

			idCliente = root.path("idCliente").asLong();
			logger.debug("idCliente: {}", idCliente);

			idCotizacion = root.path("idCotizacion").asLong();
			logger.debug("idCotizacion: {}", idCotizacion);

			idDam = root.path("idDam").asLong();
			logger.debug("idDam: {}", idDam);

			idUbicacion = root.path("idUbicacion").asLong();
			logger.debug("idUbicacion: {}", idUbicacion);

			idTipoMercancia = root.path("idTipoMercancia").asInt();
			logger.debug("idTipoMercancia: {}", idTipoMercancia);

			fecha = root.path("fecha").asText();
			logger.debug("fecha: {}", fecha);

			vapAvi = root.path("vapAvi").asText();
			logger.debug("vapAvi: {}", vapAvi);

			observacion = root.path("observacion").asText();
			logger.debug("observacion: {}", observacion);

			levante = root.path("levante").asText();
			logger.debug("levante: {}", levante);

			nuCont20Suelta = root.path("nuCont20Suelta").asInt();
			logger.debug("nuCont20Suelta: {}", nuCont20Suelta);

			nuCont40 = root.path("nuCont40").asInt();
			logger.debug("nuCont40: {}", nuCont40);

		} catch (JsonProcessingException e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}

		if (id == 0 || idCliente == 0 || idCotizacion == 0 || idDam == 0 || idUbicacion == 0 || idTipoMercancia == 0 || "null".equals(fecha) || StringUtils.isBlank(fecha)) {
			throw new ApiException(ApiError.EMPTY_OR_NULL_PARAMETER.getCode(), ApiError.EMPTY_OR_NULL_PARAMETER.getMessage());
		}

		boolean existsTarjeta = tarjetaRepository.existsById(id);
		logger.debug("Exists Tarjeta? {}: {}",id , existsTarjeta);
		if (!existsTarjeta) {
			throw new ApiException(ApiError.TARJETA_NOT_FOUND.getCode(), ApiError.TARJETA_NOT_FOUND.getMessage());
		}

		Cliente cliente = clienteRepository.findById(idCliente)
				.orElseThrow(() -> new ApiException(ApiError.CLIENTE_NOT_FOUND.getCode(), ApiError.CLIENTE_NOT_FOUND.getMessage()));

		Cotizacion cotizacion = cotizacionRepository.findById(idCotizacion)
				.orElseThrow(() -> new ApiException(ApiError.COTIZACION_NOT_FOUND.getCode(), ApiError.COTIZACION_NOT_FOUND.getMessage()));

		Dam dam = damRepository.findById(idDam)
				.orElseThrow(() -> new ApiException(ApiError.DAM_NOT_FOUND.getCode(), ApiError.DAM_NOT_FOUND.getMessage()));

		Ubicacion ubicacion = ubicacionRepository.findById(idUbicacion)
				.orElseThrow(() -> new ApiException(ApiError.UBICACION_NOT_FOUND.getCode(), ApiError.UBICACION_NOT_FOUND.getMessage()));

		TipoMercancia tipoMercancia = tipoMercanciaRepository.findById(idTipoMercancia)
				.orElseThrow(() -> new ApiException(ApiError.TIPO_MERCANCIA_NOT_FOUND.getCode(), ApiError.TIPO_MERCANCIA_NOT_FOUND.getMessage()));

		try {
			Tarjeta tarjeta = new Tarjeta();
			tarjeta.setIdTarjeta(id);
			tarjeta.setCliente(cliente);
			tarjeta.setCotizacion(cotizacion);
			tarjeta.setDam(dam);
			tarjeta.setUbicacion(ubicacion);
			tarjeta.setTipoMercancia(tipoMercancia);
			tarjeta.setFecha(DateUtil.of(fecha));
			tarjeta.setVapAvi(vapAvi);
			tarjeta.setObservacion(observacion);
			tarjeta.setLevante(levante);
			tarjeta.setNuCont20Suelta(nuCont20Suelta);
			tarjeta.setNuCont40(nuCont40);

			responseTarjeta = tarjetaRepository.save(tarjeta);
			logger.debug("Tarjeta guardada");
		} catch (Exception e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), responseTarjeta);
	}

	public ApiResponse delete(Long id) throws ApiException {
		Tarjeta tmpTarjeta = tarjetaRepository.findById(id).orElse(null);
		if (null != tmpTarjeta) {
			tarjetaRepository.deleteById(id);
			logger.debug("Tarjeta eliminada");
			return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), "Tarjeta " + id + " eliminado");
		}
		throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
	}


}
