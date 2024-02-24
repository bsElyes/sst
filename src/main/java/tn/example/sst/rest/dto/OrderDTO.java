package tn.example.sst.rest.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(includeFieldNames = true)
public class OrderDTO {
    List<IngredientDTO> ingredients;
    String status;
}
