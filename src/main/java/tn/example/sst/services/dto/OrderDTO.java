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
public class OrderDTO {
    private Long orderId;
    private String status;
    private List<IngredientDTO> ingredients;
}
