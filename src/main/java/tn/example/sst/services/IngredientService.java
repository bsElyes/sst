package tn.example.sst.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.example.sst.domain.Ingredient;
import tn.example.sst.repository.IngredientRepository;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link tn.example.sst.domain.Ingredient}.
 */
@Service
@Transactional
public class IngredientService {

    private final Logger log = LoggerFactory.getLogger(IngredientService.class);

    private final IngredientRepository ingredientRepository;


    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    /**
     * Save a ingredient.
     *
     * @param ingredient the entity to save.
     * @return the persisted entity.
     */
    public Ingredient save(Ingredient ingredient) {
        log.debug("Request to save Ingredient : {}", ingredient);
        return ingredientRepository.save(ingredient);
    }

    /**
     * Update a ingredient.
     *
     * @param ingredient the entity to save.
     * @return the persisted entity.
     */
    public Ingredient update(Ingredient ingredient) {
        log.debug("Request to update Ingredient : {}", ingredient);
        return ingredientRepository.save(ingredient);
    }

    /**
     * Partially update an ingredient.
     *
     * @param ingredient the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Ingredient> partialUpdate(Ingredient ingredient) {
        log.debug("Request to partially update Ingredient : {}", ingredient);

        return ingredientRepository
                .findById(ingredient.getIngredientId())
                .map(existingIngredient -> {
                    if (ingredient.getName() != null) {
                        existingIngredient.setName(ingredient.getName());
                    }
                    if (ingredient.getPrice() != null) {
                        existingIngredient.setPrice(ingredient.getPrice());
                    }
                    if (ingredient.getAvailableQuantity() != null) {
                        existingIngredient.setAvailableQuantity(ingredient.getAvailableQuantity());
                    }
                    if (ingredient.getType() != null) {
                        existingIngredient.setType(ingredient.getType());
                    }

                    return existingIngredient;
                })
                .map(ingredientRepository::save);
    }

    /**
     * Get all the ingredients.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Ingredient> findAll() {
        log.debug("Request to get all Ingredients");
        return ingredientRepository.findAll();
    }

    /**
     * Get one ingredient by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Ingredient> findOne(Long id) {
        log.debug("Request to get Ingredient : {}", id);
        return ingredientRepository.findById(id);
    }

    /**
     * Delete the ingredient by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Ingredient : {}", id);
        ingredientRepository.deleteById(id);
    }
}
