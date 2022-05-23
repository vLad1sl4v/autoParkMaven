package by.incubator.application.entity;

import by.incubator.application.infrastructure.orm.annotations.Column;
import by.incubator.application.infrastructure.orm.annotations.ID;
import by.incubator.application.infrastructure.orm.annotations.Table;
import lombok.*;

@Table(name = "vehicles")
@Builder
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Vehicles {
    @ID(name = "id")
    private Long id;
    @Column(name = "typesId", nullable = false)
    private Long typesId;
    @Column(name = "model", nullable = false)
    private String model;
    @Column(name = "registrationNumber", unique = true, nullable = false)
    private String registrationNumber;
    @Column(name = "weight", nullable = false)
    private Integer weight;
    @Column(name = "manufactureYear")
    private Integer manufactureYear;
    @Column(name = "mileage")
    private Integer mileage;
    @Column(name = "color")
    private String color;
    @Column(name = "engineType")
    private String engineType;
}
