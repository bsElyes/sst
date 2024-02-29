package tn.example.sst.services.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SandwichDTO {
    private Integer quantity;
    private List<IngredientDTO> ingredients;
}
