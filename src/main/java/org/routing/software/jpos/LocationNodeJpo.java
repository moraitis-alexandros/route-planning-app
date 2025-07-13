package org.routing.software.jpos;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "location")
public class LocationNodeJpo extends PlannableAsset implements IdentifiableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_source")
    private boolean isSource;

    @Column(name= "coordinatesX")
    private Long coordinatesX;

    @Column(name = "coordinatesY")
    private Long coordinatesY;

}
