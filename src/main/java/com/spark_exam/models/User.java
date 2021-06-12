package com.spark_exam.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="users")
public class User implements Serializable {
    @Id
    @Column(name="ID")
    private Long id;
    @Column(name="NAME")
    private String name;
    @Column(name="LASTNAME")
    private String lastName;
    @Column(name="COUNTRYOFORIGIN")
    private String countryOfOrigin;
    @Column(name="EMAIL")
    private String email;
}
