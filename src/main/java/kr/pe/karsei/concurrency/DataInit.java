package kr.pe.karsei.concurrency;

import jakarta.annotation.PostConstruct;
import kr.pe.karsei.concurrency.item.entity.ItemJpaEntity;
import kr.pe.karsei.concurrency.item.repository.ItemJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInit {
    private final ItemJpaRepository repository;

    @PostConstruct
    public void init() {
        ItemJpaEntity pessimistic1 = new ItemJpaEntity(null, "에메랄드", 1000);
        ItemJpaEntity pessimistic2 = new ItemJpaEntity(null, "다이아몬드", 2000);
        ItemJpaEntity pessimistic3 = new ItemJpaEntity(null, "루비", 3000);
        ItemJpaEntity pessimistic4 = new ItemJpaEntity(null, "골드", 4000);

        repository.saveAll(List.of(pessimistic1, pessimistic2, pessimistic3, pessimistic4));
    }
}
