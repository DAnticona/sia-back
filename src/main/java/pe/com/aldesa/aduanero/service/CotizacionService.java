package pe.com.aldesa.aduanero.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import pe.com.aldesa.aduanero.constant.ApiError;
import pe.com.aldesa.aduanero.dto.ApiResponse;
import pe.com.aldesa.aduanero.dto.CotizacionContext;
import pe.com.aldesa.aduanero.entity.AgenciaAduanas;
import pe.com.aldesa.aduanero.entity.Cliente;
import pe.com.aldesa.aduanero.entity.Cotizacion;
import pe.com.aldesa.aduanero.entity.CotizacionDetalle;
import pe.com.aldesa.aduanero.entity.Moneda;
import pe.com.aldesa.aduanero.entity.Servicio;
import pe.com.aldesa.aduanero.entity.Vendedor;
import pe.com.aldesa.aduanero.exception.ApiException;
import pe.com.aldesa.aduanero.repository.AgenciaAduanasRepository;
import pe.com.aldesa.aduanero.repository.ClienteRepository;
import pe.com.aldesa.aduanero.repository.CotizacionRepository;
import pe.com.aldesa.aduanero.repository.MonedaRepository;
import pe.com.aldesa.aduanero.repository.ServicioRepository;
import pe.com.aldesa.aduanero.repository.VendedorRepository;

@Service
public class CotizacionService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CotizacionRepository cotizacionRepository;

	@Autowired
	private VendedorRepository vendedorRepository;

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private AgenciaAduanasRepository agenciaAduanasRepository;

	@Autowired
	private ServicioRepository servicioRepository;

	@Autowired
	private MonedaRepository monedaRepository;

	public ApiResponse findAll() {
		List<Cotizacion> cotizaciones = cotizacionRepository.findAll();
		int total = cotizaciones.size();
		logger.debug("Total Cotizaciones: {}", total);

		List<CotizacionContext> allCotizaciones = new ArrayList<>();
		for (Cotizacion cotizacion : cotizaciones) {
			CotizacionContext cotContent = new CotizacionContext(cotizacion, cotizacion.getLineas());
			allCotizaciones.add(cotContent);
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), allCotizaciones, total);
	}

	public ApiResponse findById(Long id) {
		Cotizacion tmpCotizacion = cotizacionRepository.findById(id).orElse(null);
		logger.debug("Cotizacion: {}", tmpCotizacion);
		CotizacionContext cotContent = null;
		if (null != tmpCotizacion)
			cotContent = new CotizacionContext(tmpCotizacion, tmpCotizacion.getLineas());
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), cotContent);
	}

	public ApiResponse save(String request) throws ApiException {
		CotizacionContext responseCoti = null;

		JsonNode root;
		Long idVendedor = null;
		Long idCliente = null;
		Long idAgencia = null;
		Integer	idMoneda = null;
		String etapa = null;
		String referencia = null;
		String observaciones = null;
		ArrayNode lineas = null;
		try {
			root = new ObjectMapper().readTree(request);

			idVendedor = root.path("idVendedor").asLong();
			logger.debug("idVendedor: {}", idVendedor);

			idCliente = root.path("idCliente").asLong();
			logger.debug("idCliente: {}", idCliente);

			idAgencia = root.path("idAgencia").asLong();
			logger.debug("idAgencia: {}", idAgencia);

			idMoneda = root.path("idMoneda").asInt();
			logger.debug("idMoneda: {}", idMoneda);

			etapa = root.path("etapa").asText();
			logger.debug("etapa: {}", etapa);

			referencia = root.path("referencia").asText();
			logger.debug("referencia: {}", referencia);

			observaciones = root.path("observaciones").asText();
			logger.debug("observaciones: {}", observaciones);

			lineas = (ArrayNode) root.path("lineas");
			logger.debug("Total líneas: {}", lineas.size());

		} catch (JsonProcessingException e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}

		if (idVendedor == 0 || idCliente == 0 || idMoneda == 0 || StringUtils.isBlank(etapa) || lineas.size() < 1) {
			throw new ApiException(ApiError.EMPTY_OR_NULL_PARAMETER.getCode(), ApiError.EMPTY_OR_NULL_PARAMETER.getMessage());
		}

		Vendedor vendedor = vendedorRepository.findById(idVendedor)
				.orElseThrow(() -> new ApiException(ApiError.VENDEDOR_NOT_FOUND.getCode(), ApiError.VENDEDOR_NOT_FOUND.getMessage()));
		logger.debug("Vendedor: {}", vendedor);

		Cliente cliente = clienteRepository.findById(idCliente)
				.orElseThrow(() -> new ApiException(ApiError.CLIENTE_NOT_FOUND.getCode(), ApiError.CLIENTE_NOT_FOUND.getMessage()));
		logger.debug("Cliente: {}", cliente);

		AgenciaAduanas agenciaAduana = agenciaAduanasRepository.findById(idAgencia)
				.orElseThrow(() -> new ApiException(ApiError.AGENCIA_ADUANA_NOT_FOUND.getCode(), ApiError.AGENCIA_ADUANA_NOT_FOUND.getMessage()));
		logger.debug("Agencia Aduana: {}", agenciaAduana);

		Moneda moneda = monedaRepository.findById(idMoneda)
				.orElseThrow(() -> new ApiException(ApiError.MONEDA_NOT_FOUND.getCode(), ApiError.MONEDA_NOT_FOUND.getMessage()));
		logger.debug("Moneda: {}", moneda);

		try {
			Cotizacion cotizacion = new Cotizacion();
			List<CotizacionDetalle> detalles = new ArrayList<>();
			for (JsonNode node : lineas) {

				int item = node.path("item").asInt();
				logger.debug("item: {}", item);

				int idServicio = node.path("idServicio").asInt();
				logger.debug("idServicio: {}", idServicio);

				double precio = node.path("precio").asDouble();
				logger.debug("precio: {}", precio);

				if (item == 0 || idServicio == 0) {
					throw new ApiException(ApiError.QUOTATION_LINES.getCode(), ApiError.QUOTATION_LINES.getMessage());
				}

				Servicio servicio = servicioRepository.findById(idServicio)
						.orElseThrow(() -> new ApiException(ApiError.SERVICIO_NOT_FOUND.getCode(), ApiError.SERVICIO_NOT_FOUND.getMessage()));
				logger.debug("Servicio: {}", servicio);

				CotizacionDetalle detalle = new CotizacionDetalle();
				detalle.setIdDetalle(UUID.randomUUID().toString());
				detalle.setItem(item);
				detalle.setServicio(servicio);
				detalle.setPrecio(precio);
				detalle.setCotizacion(cotizacion);

				detalles.add(detalle);
			}

			cotizacion.setVendedor(vendedor);
			cotizacion.setCliente(cliente);
			cotizacion.setAgenciaAduana(agenciaAduana);
			cotizacion.setMoneda(moneda);
			cotizacion.setEtapa(etapa);
			cotizacion.setReferencia(referencia);
			cotizacion.setObservaciones(observaciones);
			cotizacion.setLineas(detalles);

			cotizacion = cotizacionRepository.save(cotizacion);
			logger.debug("Se guardó Cotizacion");

			responseCoti = new CotizacionContext(cotizacion, cotizacion.getLineas());

		} catch (Exception e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), responseCoti);
	}

	public ApiResponse update(String request) throws ApiException {
		CotizacionContext responseCoti;

		JsonNode root;
		Long id = null;
		Long idVendedor = null;
		Long idCliente = null;
		Long idAgencia = null;
		Integer	idMoneda = null;
		String etapa = null;
		String referencia = null;
		String observaciones = null;
		ArrayNode lineas = null;

		try {
			root = new ObjectMapper().readTree(request);

			id = root.path("id").asLong();
			logger.debug("id: {}", id);

			idVendedor = root.path("idVendedor").asLong();
			logger.debug("idVendedor: {}", idVendedor);

			idCliente = root.path("idCliente").asLong();
			logger.debug("idCliente: {}", idCliente);

			idAgencia = root.path("idAgencia").asLong();
			logger.debug("idAgencia: {}", idAgencia);

			idMoneda = root.path("idMoneda").asInt();
			logger.debug("idMoneda: {}", idMoneda);

			etapa = root.path("etapa").asText();
			logger.debug("etapa: {}", etapa);

			referencia = root.path("referencia").asText();
			logger.debug("referencia: {}", referencia);

			observaciones = root.path("observaciones").asText();
			logger.debug("observaciones: {}", observaciones);

			lineas = (ArrayNode) root.path("lineas");
			logger.debug("Total líneas: {}", lineas.size());

		} catch (JsonProcessingException e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}

		if (id == 0 || idVendedor == 0 || idCliente == 0 || idMoneda == 0 || StringUtils.isBlank(etapa) || lineas.size() < 1) {
			throw new ApiException(ApiError.EMPTY_OR_NULL_PARAMETER.getCode(), ApiError.EMPTY_OR_NULL_PARAMETER.getMessage());
		}

		boolean existCotizacion = cotizacionRepository.existsById(id);
		logger.debug("Exists cotización {}? {}", id, existCotizacion);
		if (!existCotizacion) {
			throw new ApiException(ApiError.COTIZACION_NOT_FOUND.getCode(), ApiError.COTIZACION_NOT_FOUND.getMessage());
		}

		Vendedor vendedor = vendedorRepository.findById(idVendedor)
				.orElseThrow(() -> new ApiException(ApiError.VENDEDOR_NOT_FOUND.getCode(), ApiError.VENDEDOR_NOT_FOUND.getMessage()));
		logger.debug("Vendedor: {}", vendedor);

		Cliente cliente = clienteRepository.findById(idCliente)
				.orElseThrow(() -> new ApiException(ApiError.CLIENTE_NOT_FOUND.getCode(), ApiError.CLIENTE_NOT_FOUND.getMessage()));
		logger.debug("Cliente: {}", cliente);

		AgenciaAduanas agenciaAduana = agenciaAduanasRepository.findById(idAgencia)
				.orElseThrow(() -> new ApiException(ApiError.AGENCIA_ADUANA_NOT_FOUND.getCode(), ApiError.AGENCIA_ADUANA_NOT_FOUND.getMessage()));
		logger.debug("Agencia Aduana: {}", agenciaAduana);

		Moneda moneda = monedaRepository.findById(idMoneda)
				.orElseThrow(() -> new ApiException(ApiError.MONEDA_NOT_FOUND.getCode(), ApiError.MONEDA_NOT_FOUND.getMessage()));
		logger.debug("Moneda: {}", moneda);

		try {
			Cotizacion cotizacion = new Cotizacion();
			cotizacion.setIdCotizacion(id);

			List<CotizacionDetalle> detalles = new ArrayList<>();
			for (JsonNode node : lineas) {

				String uuid = node.path("id").asText();
				logger.debug("id: {}", uuid);

				int item = node.path("item").asInt();
				logger.debug("item: {}", item);

				int idServicio = node.path("idServicio").asInt();
				logger.debug("idServicio: {}", idServicio);

				double precio = node.path("precio").asDouble();
				logger.debug("precio: {}", precio);

				if (item == 0 || idServicio == 0) {
					throw new ApiException(ApiError.QUOTATION_LINES.getCode(), ApiError.QUOTATION_LINES.getMessage());
				}

				Servicio servicio = servicioRepository.findById(idServicio)
						.orElseThrow(() -> new ApiException(ApiError.SERVICIO_NOT_FOUND.getCode(), ApiError.SERVICIO_NOT_FOUND.getMessage()));
				logger.debug("Servicio: {}", servicio);

				CotizacionDetalle detalle = new CotizacionDetalle();
				detalle.setIdDetalle(uuid);
				detalle.setItem(item);
				detalle.setServicio(servicio);
				detalle.setPrecio(precio);
				detalle.setCotizacion(cotizacion);

				detalles.add(detalle);
			}

			cotizacion.setVendedor(vendedor);
			cotizacion.setCliente(cliente);
			cotizacion.setAgenciaAduana(agenciaAduana);
			cotizacion.setMoneda(moneda);
			cotizacion.setEtapa(etapa);
			cotizacion.setReferencia(referencia);
			cotizacion.setObservaciones(observaciones);
			cotizacion.setLineas(detalles);

			cotizacion = cotizacionRepository.save(cotizacion);
			logger.debug("Se actualizó Cotizacion");

			responseCoti = new CotizacionContext(cotizacion, cotizacion.getLineas());

		} catch (Exception e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), responseCoti);
	}

}
