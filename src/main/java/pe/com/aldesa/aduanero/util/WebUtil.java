package pe.com.aldesa.aduanero.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.com.aldesa.aduanero.dto.MenuContext;
import pe.com.aldesa.aduanero.dto.SubmenuContext;
import pe.com.aldesa.aduanero.entity.Menu;
import pe.com.aldesa.aduanero.entity.Rol;
import pe.com.aldesa.aduanero.entity.SubMenu;
import pe.com.aldesa.aduanero.entity.Usuario;
import pe.com.aldesa.aduanero.security.auth.web.AuthUserData;

public class WebUtil {

	private static final Logger logger = LoggerFactory.getLogger(WebUtil.class);

	private WebUtil() {
		throw new IllegalStateException();
	}

	/**
	 * Este metodo crea un objeto que contiene los datos que enviarán junto
	 * con el token en la respuesta Http
	 *
	 */
	public static AuthUserData getAuthUser(Usuario usuario) {
		AuthUserData authUser = new AuthUserData();
		authUser.setId(usuario.getIdPersona());
		authUser.setName(usuario.getNombres());
		authUser.setLastname(usuario.getApellidoPaterno());
		authUser.setUsername(usuario.getUsername());
		authUser.setEmail(usuario.getEmail());
		authUser.setImagen(usuario.getImagen());

		Rol roltmp = usuario.getRol();
		authUser.setRol(roltmp.getNombre());

		Set<Menu> menus = roltmp.getMenus();
		logger.debug("Total de Menus: {}", menus.size());

		List<MenuContext> listMContext = new ArrayList<>();
		for (Menu m : menus) {
			logger.debug("{}", m);
			MenuContext mContext = new MenuContext();
			mContext.setId(m.getIdMenu());
			mContext.setNombre(m.getNombre());
			mContext.setOrden(m.getNumeroOrden());
			mContext.setIcono(m.getIcono());

			Set<SubMenu> submenus = m.getSubmenus();
			logger.debug("Total de Submenus: {}", submenus.size());

			List<SubmenuContext> listSmContext = new ArrayList<>();
			for (SubMenu sm : submenus) {
				logger.debug("{}", sm);

				SubmenuContext smContext = new SubmenuContext();
				smContext.setId(sm.getSubmenuId().getIdSubmenu());
				smContext.setNombre(sm.getNombre());
				smContext.setOrden(sm.getNumeroOrden());
				smContext.setRuta(sm.getRuta());

				listSmContext.add(smContext);
			}
			listSmContext.sort(Comparator.comparing(SubmenuContext::getId));

			mContext.setSubmenu(listSmContext);

			listMContext.add(mContext);
		}
		listMContext.sort(Comparator.comparing(MenuContext::getId));

		authUser.setMenus(listMContext);

		return authUser;
	}

}
