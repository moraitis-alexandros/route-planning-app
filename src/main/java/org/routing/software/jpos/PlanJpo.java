package org.routing.software.jpos;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "plan")
public class PlanJpo extends AbstractEntity implements IdentifiableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "timeslot_length")
    private Long timeslotLength;

    @Column(name = "best_solution")
    private String bestSolution; //will be probably number but we put string in any case

    @Column(name = "zone_polygon")
    private String zonePolygon; //for later use with maps

    @OneToMany(mappedBy = "plan", cascade = CascadeType.PERSIST)
    private List<AssignmentJpo> assignments;

    public void addAssignment(AssignmentJpo assignment) {
        if (assignment == null) {
            return;
        }

        if (assignments == null) {
            assignments = new ArrayList<>();
        }
        assignments.add(assignment);
        assignment.setPlan(this);
    }

    //TODO USERID

}
