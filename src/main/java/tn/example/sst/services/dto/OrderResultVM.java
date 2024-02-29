package tn.example.sst.rest.vm;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import tn.example.sst.services.dto.SandwichDTO;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderResultVM {
    private Long orderId;
    private String status;
    private List<SandwichDTO> order;

    public OrderResultVM(List<SandwichDTO> order) {
        this.order = order;
    }
}
