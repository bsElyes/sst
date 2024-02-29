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
public class OrderRequestVM {
    private List<SandwichDTO> order;
}
