package kr.pe.karsei.concurrency.item.adapter.out;

import jakarta.persistence.EntityNotFoundException;
import kr.pe.karsei.concurrency.item.domain.Item;
import kr.pe.karsei.concurrency.item.entity.ItemJpaEntity;
import kr.pe.karsei.concurrency.item.mapper.ItemMapper;
import kr.pe.karsei.concurrency.item.port.out.ItemLoadPort;
import kr.pe.karsei.concurrency.item.port.out.ItemSavePort;
import kr.pe.karsei.concurrency.item.repository.ItemJpaRepository;
import kr.pe.karsei.concurrency.item.repository.ItemWithLockJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemJpaAdapter implements ItemLoadPort, ItemSavePort {
    private final ItemJpaRepository repository;
    private final ItemWithLockJpaRepository lockRepository;

    @Override
    public Item findById(Long seqNo) {
        ItemJpaEntity found = repository.findById(seqNo)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 아이템입니다."));
        return ItemMapper.mapItemPessimisticJpaToPessimistic(found);
    }

    @Override
    public Item findByIdWithLock(Long seqNo) {
        ItemJpaEntity found = lockRepository.findBySeqNo(seqNo)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 아이템입니다."));
        return ItemMapper.mapItemPessimisticJpaToPessimistic(found);
    }

    @Override
    public ItemJpaEntity save(Item item) {
        return repository.save(ItemMapper.mapPessimisticToItemPessimisticJpa(item));
    }
}
