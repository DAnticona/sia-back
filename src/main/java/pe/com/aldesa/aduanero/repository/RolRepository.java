package pe.com.aldesa.aduanero.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pe.com.aldesa.aduanero.entity.Rol;

@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {

}
