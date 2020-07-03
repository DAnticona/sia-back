package pe.com.aldesa.aduanero.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 
 * @author Juan Pablo Canepa Alvarez
 *
 */
@Entity
@Table(name = "authorities")
public class Authority {

	@Id
	@Column(name = "authority")
	private String authority;
	@ManyToOne
	@JoinColumn(name = "username")
	private User user;

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
