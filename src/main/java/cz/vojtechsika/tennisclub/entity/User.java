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
@Table(name="app_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "id")
    private Long id;

    @Column(name= "phone_number", nullable = false, unique = true)  // zvážit validaci nechat jen čísla
    private String phoneNumber;

    @Column(name= "name", nullable = false)
    private String name;

    @Column(name= "deleted", nullable = false)
    private boolean deleted;

    // fetch = FetchType.LAZY jsem přidl pro přehlednost, @OneToMany to ma nasteveno v defaultu
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    private List<Reservation> reservations;
}
