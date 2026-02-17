package com.GoPass.GoPass.domain.address;


import com.GoPass.GoPass.domain.event.Event;
import jakarta.persistence.*;

import java.util.List;

@Table(name = "address")
@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String city;

    private String UF;

    // CORRETO
    @ManyToMany
    @JoinTable(
            name = "address_event", // Nome da tabela que será criada no banco
            joinColumns = @JoinColumn(name = "address_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    private List<Event> event;
}
