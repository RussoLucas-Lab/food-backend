package com.food_store.backend.entity;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter

@MappedSuperclass
@SQLRestriction("eliminado = false")
//permite definir una condición SQL global que Hibernate aplica automáticamente en todas las consultas de esa entidad.
//En nuestro caso, queremos que todas las consultas ignoren los registros donde eliminado = true.
public abstract class Base {

    @Column(name = "eliminado", nullable = false)
    private boolean eliminado = false;

    public boolean isEliminado() {
        return eliminado;
    }
}