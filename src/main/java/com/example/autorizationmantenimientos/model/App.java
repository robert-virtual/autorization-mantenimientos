package com.example.autorizationmantenimientos.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity(name = "t_apps")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class App {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String status;
    public static final String STATUS_ACTIVE = "active";

}
