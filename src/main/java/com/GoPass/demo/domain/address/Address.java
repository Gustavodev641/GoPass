package com.GoPass.demo.domain.address;


import com.GoPass.demo.domain.event.Event;
import jakarta.persistence.*;

@Table(name = "address")
@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String city;

    private String UF;

    @ManyToMany
    @JoinColumn(name = "event_id")
    private Event event;
}
