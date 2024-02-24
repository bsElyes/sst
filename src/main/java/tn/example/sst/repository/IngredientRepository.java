package tn.example.sst.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.example.sst.domain.Ingredient;

/**
 * Spring Data JPA repository for the Ingredient entity.
 */
@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    Ingredient findByName(String name);
}
