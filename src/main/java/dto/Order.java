package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    public int id;
    public int petId;
    public int quantity;
    public String shipDate;
    public String status;
    public boolean complete;
}
