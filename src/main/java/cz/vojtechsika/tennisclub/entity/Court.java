package cz.vojtechsika.tennisclub.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="court")
public class Court {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "id")
    private Long id;

    @Column(name= "court_number", nullable = false, unique = true)
    private int courtNumber;

    @Column(name= "deleted", nullable = false)
    private boolean deleted;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "surface_type_id")
    private SurfaceType surfaceType;

    // fetch = FetchType.LAZY jsem přidl pro přehlednost, @OneToMany to ma nasteveno v defaultu
    @OneToMany(mappedBy = "court", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    private List<Reservation> reservations;
}
