package kr.pe.karsei.concurrency.item.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "ITEM")
@Builder
@Getter
@DynamicInsert @DynamicUpdate
@NoArgsConstructor @AllArgsConstructor
public class ItemJpaEntity {
    @Id
    @GeneratedValue
    private Long seqNo;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "STOCK", nullable = false)
    private Integer stock;
}
