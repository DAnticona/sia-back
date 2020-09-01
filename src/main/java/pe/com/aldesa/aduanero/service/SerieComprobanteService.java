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
import pe.com.aldesa.aduanero.entity.SerieComprobante;
import pe.com.aldesa.aduanero.entity.TipoComprobante;
import pe.com.aldesa.aduanero.exception.ApiException;
import pe.com.aldesa.aduanero.repository.SerieComprobanteRepository;
import pe.com.aldesa.aduanero.repository.TipoComprobanteRepository;

@Service
public class SerieComprobanteService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private SerieComprobanteRepository serieComprobanteRepository;

	@Autowired
	private TipoComprobanteRepository tipoComprobanteRespository;

	public ApiResponse findAll() {
		List<SerieComprobante> series = serieComprobanteRepository.findAll();
		int total = series.size();
		logger.debug("Total Series: {}", total);
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), series, total);
	}

	public ApiResponse findById(Integer id) {
		SerieComprobante agencia = serieComprobanteRepository.findById(id).orElse(null);
		logger.debug("Series: {}", agencia);
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), agencia);
	}

	public ApiResponse save(String request) throws ApiException {
		SerieComprobante responseSerie;

		JsonNode root;
		Integer	idTipoComprobante = null;
		String	serie = null;
		Integer	numeroMinimo = null;
		Integer	numeroMaximo = null;
		String descripcion = null;
		try {
			root = new ObjectMapper().readTree(request);

			idTipoComprobante = root.path("idTipoComprobante").asInt();
			logger.debug("idTipoComprobante: {}", idTipoComprobante);

			serie = root.path("serie").asText();
			logger.debug("serie: {}", serie);

			numeroMinimo = root.path("numeroMinimo").asInt();
			logger.debug("numeroMinimo {}", numeroMinimo);

			numeroMaximo = root.path("numeroMaximo").asInt();
			logger.debug("numeroMaximo {}", numeroMaximo);

			descripcion = root.path("descripcion").asText();
			logger.debug("descripcion: {}", descripcion);

		} catch (JsonProcessingException e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}

		if (null == idTipoComprobante || idTipoComprobante == 0 || StringUtils.isBlank(serie)
				|| null == numeroMinimo || numeroMinimo == 0 || null == numeroMaximo || numeroMaximo == 0) {
			throw new ApiException(ApiError.EMPTY_OR_NULL_PARAMETER.getCode(), ApiError.EMPTY_OR_NULL_PARAMETER.getMessage());
		}

		TipoComprobante tipoComprobante = tipoComprobanteRespository.findById(idTipoComprobante)
				.orElseThrow(() -> new ApiException(ApiError.TIPO_COMPROBANTE_NOT_FOUND.getCode(), ApiError.TIPO_COMPROBANTE_NOT_FOUND.getMessage()));

		try {
			SerieComprobante serieComprobante = new SerieComprobante();
			serieComprobante.setTipoComprobante(tipoComprobante);
			serieComprobante.setSerie(serie);
			serieComprobante.setActivado('S');
			serieComprobante.setNumeroMinimo(numeroMinimo);
			serieComprobante.setNumeroMaximo(numeroMaximo);

			responseSerie = serieComprobanteRepository.save(serieComprobante);
			logger.debug("SerieComprobante guardada");
		} catch (Exception e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), responseSerie);
	}

	public ApiResponse update(String request) throws ApiException {
		SerieComprobante responseSerie;

		JsonNode root;
		Integer	id = null;
		Integer	idTipoComprobante = null;
		String	serie = null;
		String	activado = null;
		Integer	numeroMinimo = null;
		Integer	numeroMaximo = null;
		String descripcion = null;
		try {
			root = new ObjectMapper().readTree(request);

			id = root.path("id").asInt();
			logger.debug("id: {}", id);

			idTipoComprobante = root.path("idTipoComprobante").asInt();
			logger.debug("idTipoComprobante: {}", idTipoComprobante);

			serie = root.path("serie").asText();
			logger.debug("serie: {}", serie);

			activado = root.path("activado").asText();
			logger.debug("activado: {}", activado);

			numeroMinimo = root.path("numeroMinimo").asInt();
			logger.debug("numeroMinimo {}", numeroMinimo);

			numeroMaximo = root.path("numeroMaximo").asInt();
			logger.debug("numeroMaximo {}", numeroMaximo);

			descripcion = root.path("descripcion").asText();
			logger.debug("descripcion: {}", descripcion);

		} catch (JsonProcessingException e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}

		if (null == id || id == 0 || null == idTipoComprobante || idTipoComprobante == 0 || StringUtils.isBlank(serie) || StringUtils.isBlank(activado)
				|| null == numeroMinimo || numeroMinimo == 0 || null == numeroMaximo || numeroMaximo == 0) {
			throw new ApiException(ApiError.EMPTY_OR_NULL_PARAMETER.getCode(), ApiError.EMPTY_OR_NULL_PARAMETER.getMessage());
		}

		boolean existsIdSerie = serieComprobanteRepository.existsById(id);
		logger.debug("Existe idSerie {}? {}", id, existsIdSerie);
		if (!existsIdSerie) {
			throw new ApiException(ApiError.SERIE_NOT_FOUND.getCode(), ApiError.SERIE_NOT_FOUND.getMessage());
		}

		TipoComprobante tipoComprobante = tipoComprobanteRespository.findById(idTipoComprobante)
				.orElseThrow(() -> new ApiException(ApiError.TIPO_COMPROBANTE_NOT_FOUND.getCode(), ApiError.TIPO_COMPROBANTE_NOT_FOUND.getMessage()));

		try {
			SerieComprobante serieComprobante = new SerieComprobante();
			serieComprobante.setIdSerieComprobante(id);
			serieComprobante.setTipoComprobante(tipoComprobante);
			serieComprobante.setSerie(serie);
			serieComprobante.setActivado(activado.toCharArray()[0]);
			serieComprobante.setNumeroMinimo(numeroMinimo);
			serieComprobante.setNumeroMaximo(numeroMaximo);

			responseSerie = serieComprobanteRepository.save(serieComprobante);
			logger.debug("SerieComprobante actualizada");
		} catch (Exception e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), responseSerie);
	}

	public ApiResponse delete(Integer id) throws ApiException {
		SerieComprobante tmpSerie = serieComprobanteRepository.findById(id).orElse(null);
		logger.debug("Serie Comprobante: {}", tmpSerie);
		if (null != tmpSerie) {
			serieComprobanteRepository.deleteById(id);
			return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), "Serie " + id + " eliminado");
		}
		throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
	}

}
