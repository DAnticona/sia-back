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
import pe.com.aldesa.aduanero.entity.Movimiento;
import pe.com.aldesa.aduanero.entity.Tarjeta;
import pe.com.aldesa.aduanero.entity.Ticket;
import pe.com.aldesa.aduanero.exception.ApiException;
import pe.com.aldesa.aduanero.repository.MovimientoRepository;
import pe.com.aldesa.aduanero.repository.TarjetaRepository;
import pe.com.aldesa.aduanero.repository.TicketRepository;
import pe.com.aldesa.aduanero.util.DateUtil;

@Service
public class MovimientoService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MovimientoRepository movimientoRepository;

	@Autowired
	private TarjetaRepository tarjetaRepository;

	@Autowired
	private TicketRepository ticketRepository;

	public ApiResponse findAll() {
		List<Movimiento> movimientos = movimientoRepository.findAll();
		int total = movimientos.size();
		logger.debug("Total Movimientos: {}", total);
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), movimientos, total);
	}

	public ApiResponse findById(Long id) {
		Movimiento tmpMovimiento = movimientoRepository.findById(id).orElse(null);
		logger.debug("Movimiento: {}", tmpMovimiento);
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), tmpMovimiento);
	}

	public ApiResponse save(String request) throws ApiException {
		Movimiento responseMovimiento;

		JsonNode root;
		Long idTarjeta = null;
		Long idTicket = null;
		String fgMovimiento = null;
		String fechaMovimiento = null;

		try {
			root = new ObjectMapper().readTree(request);

			idTarjeta = root.path("idTarjeta").asLong();
			logger.debug("idTarjeta: {}", idTarjeta);

			idTicket = root.path("idTicket").asLong();
			logger.debug("idTicket: {}", idTicket);

			fgMovimiento = root.path("fgMovimiento").asText();
			logger.debug("fgMovimiento: {}", fgMovimiento);

			fechaMovimiento = root.path("fechaMovimiento").asText();
			logger.debug("fechaMovimiento: {}", fechaMovimiento);

		} catch (JsonProcessingException e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}

		if (idTarjeta == 0 || idTicket == 0 || StringUtils.isBlank(fgMovimiento) || StringUtils.isBlank(fechaMovimiento) || "null".equals(fechaMovimiento)) {
			throw new ApiException(ApiError.EMPTY_OR_NULL_PARAMETER.getCode(), ApiError.EMPTY_OR_NULL_PARAMETER.getMessage());
		}

		Tarjeta tarjeta = tarjetaRepository.findById(idTarjeta)
				.orElseThrow(() -> new ApiException(ApiError.TARJETA_NOT_FOUND.getCode(), ApiError.TARJETA_NOT_FOUND.getMessage()));


		Ticket ticket = ticketRepository.findById(idTicket)
				.orElseThrow(() -> new ApiException(ApiError.TICKET_NOT_FOUND.getCode(), ApiError.TICKET_NOT_FOUND.getMessage()));

		try {
			Movimiento movimiento = new Movimiento();
			movimiento.setTarjeta(tarjeta);
			movimiento.setTicket(ticket);
			movimiento.setFgMovimiento(fgMovimiento);
			movimiento.setFechaMovimiento(DateUtil.of(fechaMovimiento));

			responseMovimiento = movimientoRepository.save(movimiento);
			logger.debug("Movimiento guardado");
		} catch (Exception e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), responseMovimiento);
	}

	public ApiResponse update(String request) throws ApiException {
		Movimiento responseMovimiento;

		JsonNode root;
		Long id = null;
		Long idTarjeta = null;
		Long idTicket = null;
		String fgMovimiento = null;
		String fechaMovimiento = null;

		try {
			root = new ObjectMapper().readTree(request);

			id = root.path("id").asLong();
			logger.debug("id: {}", id);

			idTarjeta = root.path("idTarjeta").asLong();
			logger.debug("idTarjeta: {}", idTarjeta);

			idTicket = root.path("idTicket").asLong();
			logger.debug("idTicket: {}", idTicket);

			fgMovimiento = root.path("fgMovimiento").asText();
			logger.debug("fgMovimiento: {}", fgMovimiento);

			fechaMovimiento = root.path("fechaMovimiento").asText();
			logger.debug("fechaMovimiento: {}", fechaMovimiento);

		} catch (JsonProcessingException e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}

		if (id == 0 || idTarjeta == 0 || idTicket == 0 || StringUtils.isBlank(fgMovimiento) || StringUtils.isBlank(fechaMovimiento) || "null".equals(fechaMovimiento)) {
			throw new ApiException(ApiError.EMPTY_OR_NULL_PARAMETER.getCode(), ApiError.EMPTY_OR_NULL_PARAMETER.getMessage());
		}

		Tarjeta tarjeta = tarjetaRepository.findById(idTarjeta)
				.orElseThrow(() -> new ApiException(ApiError.TARJETA_NOT_FOUND.getCode(), ApiError.TARJETA_NOT_FOUND.getMessage()));

		Ticket ticket = ticketRepository.findById(idTicket)
				.orElseThrow(() -> new ApiException(ApiError.TICKET_NOT_FOUND.getCode(), ApiError.TICKET_NOT_FOUND.getMessage()));

		boolean existsMovimiento = movimientoRepository.existsById(id);
		logger.debug("Exists idMovimiento? {}: {}", id, existsMovimiento);
		if (!existsMovimiento) {
			throw new ApiException(ApiError.MOVIMIENTO_NOT_FOUND.getCode(), ApiError.MOVIMIENTO_NOT_FOUND.getMessage());
		}

		try {
			Movimiento movimiento = new Movimiento();
			movimiento.setIdMovimiento(id);
			movimiento.setTarjeta(tarjeta);
			movimiento.setTicket(ticket);
			movimiento.setFgMovimiento(fgMovimiento);
			movimiento.setFechaMovimiento(DateUtil.of(fechaMovimiento));

			responseMovimiento = movimientoRepository.save(movimiento);
			logger.debug("Movimiento actualizado");
		} catch (Exception e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), responseMovimiento);
	}

}
