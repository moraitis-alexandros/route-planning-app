package org.routing.software.model;

import lombok.*;
import org.routing.software.jpos.IdentifiableEntity;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Truck {

    private int id;

    private Long unloadingTime;

    private Long maxSpeed;

    private Long convertedUnloadingTime;

    private Long convertedMaxSpeed;

    private Long capacity;

    private String description;

}
