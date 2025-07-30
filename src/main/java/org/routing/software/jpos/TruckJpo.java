package org.routing.software.jpos;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "truck")
public class TruckJpo extends PlannableAsset implements IdentifiableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name= "unloading_time")
    private Long unloadingTime;

    @Column(name = "max_speed")
    private Long maxSpeed;

    @Transient
    private Long convertedUnloadingTime; //it will not be included in DB

    @Transient
    private Long convertedMaxSpeed; //it will not be included in DB

}
