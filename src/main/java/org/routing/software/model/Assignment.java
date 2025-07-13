package org.routing.software.model;

import jakarta.persistence.*;
import lombok.*;
import org.routing.software.jpos.IdentifiableEntity;
import org.routing.software.jpos.LocationNodeJpo;
import org.routing.software.jpos.PlanJpo;
import org.routing.software.jpos.TruckJpo;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Assignment implements IdentifiableEntity {

    private Long id;

    private Long sequence;

    private LocationNode locationNode;

    private Truck truck;

}