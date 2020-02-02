package at.spengergasse.reactivedemo.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Chocolate {

    public String id;

    public String name;

    public String brand;

    private int price;
}