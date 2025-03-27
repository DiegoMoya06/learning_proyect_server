package com.dm.learning.entities;

import com.dm.learning.entities.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "card")
public class Card extends BaseEntity {

    @Id
    @Column(nullable = false, updatable = false, columnDefinition = "CHAR(36)")
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deck_id")
    @JdbcTypeCode(SqlTypes.CHAR)
    private Deck deck;

    @Column(name = "rate", nullable = false)
    private Double rate;

    @Column(name = "times_displayed", nullable = false)
    private Integer timesDisplayed;

    @Column(name = "last_displayed")
    private Instant lastDisplayed;

    @Column(name = "probability", nullable = false)
    private Double probability;
}
