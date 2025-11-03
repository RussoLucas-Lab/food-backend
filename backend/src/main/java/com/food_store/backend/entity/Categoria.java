package com.food_store.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE categoria SET eliminado = true WHERE id = ?")
@Where(clause = "eliminado = false")
public class Categoria extends Base {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "categoria_id") // FK en la tabla producto
    private List<Producto> productosLista = new ArrayList<>();

    @PreRemove
    //Callback de ciclo de vida de la entidad que se ejecuta justo antes de que Hibernate elimine una entidad de la base de datos.
    public void preEliminado(){
        for(Producto p : productosLista){
            p.setEliminado(true);
        }
    }
}
