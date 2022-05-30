package by.incubator.application.entity;

import by.incubator.application.infrastructure.orm.annotations.Column;
import by.incubator.application.infrastructure.orm.annotations.ID;
import by.incubator.application.infrastructure.orm.annotations.Table;
import lombok.*;

@Table(name = "orders")
@Builder
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Orders {
    @ID(name = "id")
    private Long id;
    @Column(name = "vehicleId")
    private Long vehicleId;
    @Column(name = "defect")
    private String defect;
    @Column(name = "breakingAmount")
    private Integer breakingAmount;
}
