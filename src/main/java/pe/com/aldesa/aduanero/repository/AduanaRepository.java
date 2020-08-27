package pe.com.aldesa.aduanero.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pe.com.aldesa.aduanero.entity.Aduana;

@Repository
public interface AduanaRepository extends JpaRepository<Aduana, Integer> {

}
