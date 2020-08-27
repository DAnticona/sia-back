package pe.com.aldesa.aduanero.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import pe.com.aldesa.aduanero.constant.ApiError;
import pe.com.aldesa.aduanero.dto.ApiResponse;
import pe.com.aldesa.aduanero.dto.MenuContext;
import pe.com.aldesa.aduanero.dto.RolContext;
import pe.com.aldesa.aduanero.entity.Menu;
import pe.com.aldesa.aduanero.entity.Rol;
import pe.com.aldesa.aduanero.exception.ApiException;
import pe.com.aldesa.aduanero.repository.MenuRepository;
import pe.com.aldesa.aduanero.repository.RolRepository;

@Service
public class RolService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private RolRepository rolRepository;

	@Autowired
	private MenuRepository menuRepository;


	public ApiResponse findAll() {
		List<Rol> roles = rolRepository.findAll();
		int total = roles.size();
		logger.debug("Total Roles: {}", total);

		if (roles.isEmpty()) {
			return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), Collections.emptyList(), total);
		}

		List<RolContext> listRolContext = new ArrayList<>();
		for (Rol r : roles) {
			logger.debug("{}", r);

			List<MenuContext> listMContext = new ArrayList<>();
			Set<Menu> menus = r.getMenus();
			for (Menu m : menus) {
				logger.debug("{}", m);

				MenuContext mContext = new MenuContext();
				mContext.setId(m.getIdMenu());
				mContext.setNombre(m.getNombre());
				mContext.setOrden(m.getNumeroOrden());
				mContext.setIcono(m.getIcono());

				listMContext.add(mContext);
			}
			RolContext rolContext = new RolContext();
			rolContext.setId(r.getIdRol());
			rolContext.setNombre(r.getNombre());
			rolContext.setMenus(listMContext);

			listRolContext.add(rolContext);
		}

		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), listRolContext, total);
	}

	public ApiResponse findById(Integer id) {
		Rol tmpRol = rolRepository.findById(id).orElse(null);
		logger.debug("Rol: {}", tmpRol);

		if (null == tmpRol) {
			return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), tmpRol);
		}

		RolContext rolContext = null;
		List<MenuContext> listMContext = new ArrayList<>();
		Set<Menu> menus = tmpRol.getMenus();
		for (Menu m : menus) {
			logger.debug("{}", m);

			MenuContext mContext = new MenuContext();
			mContext.setId(m.getIdMenu());
			mContext.setNombre(m.getNombre());
			mContext.setOrden(m.getNumeroOrden());
			mContext.setIcono(m.getIcono());

			listMContext.add(mContext);
		}
		rolContext = new RolContext();
		rolContext.setId(tmpRol.getIdRol());
		rolContext.setNombre(tmpRol.getNombre());
		rolContext.setMenus(listMContext);

		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), rolContext);
	}

	public ApiResponse save(String request) throws ApiException {
		Rol responseRol;

		JsonNode root;
		String	nombre = null;
		Set<Menu> menusSet = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			root = mapper.readTree(request);

			nombre = root.path("nombre").asText();
			logger.debug("nombre: {}", nombre);

			JsonNode nodeMenu = root.path("menus");
			ObjectReader reader = mapper.readerFor(new TypeReference<List<Integer>>() {});
			List<Integer> menusAccess = null;
			if (null != reader) {
				menusAccess = reader.readValue(nodeMenu);
			}

			List<Menu> menus = new ArrayList<>();
			for (Integer idMenu : menusAccess) {
				logger.debug("idMenu: {}", idMenu);

				Menu menu = menuRepository.findById(idMenu).orElse(null);
				if (null == menu) {
					throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
				}
				menus.add(menu);
			}
			menusSet = new HashSet<>(menus);

		} catch (IOException e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}

		if (StringUtils.isBlank(nombre)) {
			throw new ApiException(ApiError.EMPTY_OR_NULL_PARAMETER.getCode(), ApiError.EMPTY_OR_NULL_PARAMETER.getMessage());
		}

		try {
			Rol rol = new Rol();
			rol.setNombre(nombre.toUpperCase());
			rol.setMenus(menusSet);

			responseRol = rolRepository.save(rol);
		} catch (Exception e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), responseRol);
	}

	public ApiResponse update(String request) throws ApiException {
		Rol responseRol;

		JsonNode root;
		Integer	id = null;
		String	nombre = null;
		Set<Menu> menusSet = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			root = mapper.readTree(request);

			id = root.path("id").asInt();
			logger.debug("id: {}", id);

			nombre = root.path("nombre").asText();
			logger.debug("nombre: {}", nombre);

			JsonNode nodeMenu = root.path("menus");
			ObjectReader reader = mapper.readerFor(new TypeReference<List<Integer>>() {});
			List<Integer> menusAccess = null;
			if (null != reader) {
				menusAccess = reader.readValue(nodeMenu);
			}

			List<Menu> menus = new ArrayList<>();
			for (Integer idMenu : menusAccess) {
				logger.debug("idMenu: {}", idMenu);

				Menu menu = menuRepository.findById(idMenu).orElse(null);
				if (null == menu) {
					throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
				}
				menus.add(menu);
			}
			menusSet = new HashSet<>(menus);

		} catch (IOException e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}

		if (null == id || id == 0 || StringUtils.isBlank(nombre)) {
			throw new ApiException(ApiError.EMPTY_OR_NULL_PARAMETER.getCode(), ApiError.EMPTY_OR_NULL_PARAMETER.getMessage());
		}

		try {
			Rol rol = new Rol();
			rol.setIdRol(id);
			rol.setNombre(nombre.toUpperCase());
			rol.setMenus(menusSet);

			responseRol = rolRepository.save(rol);
		} catch (Exception e) {
			throw new ApiException(ApiError.NO_APPLICATION_PROCESSED.getCode(), ApiError.NO_APPLICATION_PROCESSED.getMessage(), e.getMessage());
		}
		return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), responseRol);
	}

	public ApiResponse delete(Integer id) throws ApiException {
		Rol tmpRol = rolRepository.findById(id).orElse(null);
		logger.debug("Rol: {}", tmpRol);
		if (null != tmpRol) {
			rolRepository.deleteById(id);
			return ApiResponse.of(ApiError.SUCCESS.getCode(), ApiError.SUCCESS.getMessage(), "Rol " + id + " eliminado");
		}
		throw new ApiException(ApiError.RESOURCE_NOT_FOUND.getCode(), ApiError.RESOURCE_NOT_FOUND.getMessage());
	}

}
