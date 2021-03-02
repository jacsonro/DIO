package one.digitalinnovation.equipments.repository;

import one.digitalinnovation.equipments.entity.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

    Optional<Equipment> findByName(String name);
}
