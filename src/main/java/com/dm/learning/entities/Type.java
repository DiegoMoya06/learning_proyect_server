package com.dm.learning.entities;

//import com.dm.learning.entities.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
//@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "type")
//public class Type extends BaseEntity {
public class Type {
    @Id
    @GeneratedValue
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "type", fetch = FetchType.LAZY)
    private Set<Deck> decks = new HashSet<>();
}
