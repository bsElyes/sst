package tn.example.sst.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.example.sst.domain.Ingredient;
import tn.example.sst.repository.IngredientRepository;
import tn.example.sst.rest.exceptions.BadRequestAlertException;
import tn.example.sst.services.impl.IngredientServiceImpl;
import tn.example.sst.utils.HeaderUtil;
import tn.example.sst.utils.ResponseUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link tn.example.sst.domain.Ingredient}.
 */
@RestController
@RequestMapping("/api")
public class IngredientController {

    private static final String ENTITY_NAME = "ingredient";
    private final Logger log = LoggerFactory.getLogger(IngredientController.class);
    private final IngredientServiceImpl ingredientServiceImpl;
    private final IngredientRepository ingredientRepository;

    @Value("${spring.application.name}")
    private String applicationName;

    public IngredientController(IngredientServiceImpl ingredientServiceImpl, IngredientRepository ingredientRepository) {
        this.ingredientServiceImpl = ingredientServiceImpl;
        this.ingredientRepository = ingredientRepository;
    }

    /**
     * {@code POST  /ingredients} : Create a new ingredient.
     *
     * @param ingredient the ingredient to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ingredient, or with status {@code 400 (Bad Request)} if the ingredient has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("ingredients")
    public ResponseEntity<Ingredient> addIngredient(@RequestBody Ingredient ingredient) throws URISyntaxException {
        log.debug("REST request to save Ingredient : {}", ingredient);
        if (ingredient.getIngredientId() != null) {
            throw new BadRequestAlertException("A new ingredient cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Ingredient result = ingredientServiceImpl.save(ingredient);
        return ResponseEntity
                .created(new URI("/api/ingredients/" + result.getIngredientId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getIngredientId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /ingredients/:id} : Updates an existing ingredient.
     *
     * @param id         the id of the ingredient to save.
     * @param ingredient the ingredient to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ingredient,
     * or with status {@code 400 (Bad Request)} if the ingredient is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ingredient couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ingredients/{id}")
    public ResponseEntity<Ingredient> updateIngredient(
            @PathVariable(value = "id", required = false) final Long id,
            @RequestBody Ingredient ingredient
    ) throws URISyntaxException {
        log.debug("REST request to update Ingredient : {}, {}", id, ingredient);
        if (ingredient.getIngredientId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "id_null");
        }
        if (!Objects.equals(id, ingredient.getIngredientId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "id_invalid");
        }

        if (!ingredientRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "id_not_found");
        }

        Ingredient result = ingredientServiceImpl.update(ingredient);
        return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ingredient.getIngredientId().toString()))
                .body(result);
    }

    /**
     * {@code PATCH  /ingredients/:id} : Partial updates given fields of an existing ingredient, field will ignore if it is null
     *
     * @param id         the id of the ingredient to save.
     * @param ingredient the ingredient to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ingredient,
     * or with status {@code 400 (Bad Request)} if the ingredient is not valid,
     * or with status {@code 404 (Not Found)} if the ingredient is not found,
     * or with status {@code 500 (Internal Server Error)} if the ingredient couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ingredients/{id}",
            consumes = {"application/json", "application/merge-patch+json"}
    )
    public ResponseEntity<Ingredient> partialUpdateIngredient(
            @PathVariable(value = "id", required = false) final Long id,
            @RequestBody Ingredient ingredient
    ) throws URISyntaxException {
        log.debug("REST request to partial update Ingredient partially : {}, {}", id, ingredient);
        if (ingredient.getIngredientId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "id_null");
        }
        if (!Objects.equals(id, ingredient.getIngredientId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "id_invalid");
        }
        if (!ingredientRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "id_not_found");
        }
        Optional<Ingredient> result = ingredientServiceImpl.partialUpdate(ingredient);
        return ResponseUtil.wrapOrNotFound(
                result,
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ingredient.getIngredientId().toString())
        );
    }

    /**
     * {@code GET  /ingredients} : get all the ingredients.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ingredients in body.
     */
    @GetMapping("/ingredients")
    public List<Ingredient> getAllIngredients() {
        log.debug("REST request to get all Ingredients");
        return ingredientServiceImpl.findAll();
    }

    /**
     * {@code GET  /ingredients/:id} : get the "id" ingredient.
     *
     * @param id the id of the ingredient to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ingredient, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ingredients/{id}")
    public ResponseEntity<Ingredient> getIngredient(@PathVariable("id") Long id) {
        log.debug("REST request to get Ingredient : {}", id);
        Optional<Ingredient> ingredient = ingredientServiceImpl.findOne(id);
        return ResponseUtil.wrapOrNotFound(ingredient);
    }

    /**
     * {@code DELETE  /ingredients/:id} : delete the "id" ingredient.
     *
     * @param id the id of the ingredient to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ingredients/{id}")
    public ResponseEntity<Void> deleteIngredient(@PathVariable("id") Long id) {
        log.debug("REST request to delete Ingredient : {}", id);
        ingredientServiceImpl.delete(id);
        return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                .build();
    }
}
