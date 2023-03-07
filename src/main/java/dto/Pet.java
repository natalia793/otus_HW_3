package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pet {
  public int id;
  public Category category;
  public String name;
  public ArrayList<String> photoUrls;
  public ArrayList<Tags> tags;
  public String status;
}
