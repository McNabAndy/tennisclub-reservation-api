package cz.vojtechsika.tennisclub.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name= "surface_type")
public class SurfaceType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "id")
    private Long id;

    @Column(name= "name", nullable = false)
    private String name;

    @Column(name= "minute_price", nullable = false)
    private BigDecimal minutePrice;

    @Column(name= "deleted", nullable = false)
    private boolean deleted;

    // fetch = FetchType.LAZY jsem přidl pro přehlednost, @OneToMany to ma nasteveno v defaultu
    @OneToMany(mappedBy = "surfaceType", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})  // tady to zkontrolovat zkontrolovat
    private List<Court> courts;

}
