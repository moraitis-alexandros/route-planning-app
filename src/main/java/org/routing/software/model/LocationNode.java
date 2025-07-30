package org.routing.software.model;

import lombok.*;
import org.routing.software.jpos.IdentifiableEntity;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class LocationNode {

    private int id;

    private boolean isSource;

    private Long coordinatesX; //TODO check to remove if needed because we have the matrix

    private Long coordinatesY; //TODO check to remove if needed because we have the matrix

    private Long capacity;

    private String description;



}
