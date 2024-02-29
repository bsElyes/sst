package tn.example.sst.services.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class IngredientDTO {
    private Long id;
    private Integer quantity;
}
