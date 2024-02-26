package tn.example.sst.rest.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderResult {
    private Long orderId;
    private String status;
    private List<SandwichDTO> order;

    public OrderResult(List<SandwichDTO> order) {
        this.order = order;
    }
}
