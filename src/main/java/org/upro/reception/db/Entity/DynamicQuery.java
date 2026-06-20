package org.upro.reception.db.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "dynamic_query")
public class DynamicQuery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 100)
    @NotNull
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Size(max = 500)
    @Column(name = "fields", length = 500)
    private String fields;

    @NotNull
    @Column(name = "sql_text", nullable = false, length = Integer.MAX_VALUE)
    private String sqlText;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

}