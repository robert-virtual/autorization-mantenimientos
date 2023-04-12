package com.example.autorizationmantenimientos.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "t_modules")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Module {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String status;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "module_id")
    List<Screen> screens = new ArrayList<>();
}
