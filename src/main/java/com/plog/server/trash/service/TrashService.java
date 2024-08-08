package com.plog.server.trash.service;

import com.plog.server.trash.domain.Trash;
import com.plog.server.trash.dto.TrashDto;
import com.plog.server.trash.repository.TrashRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TrashService {
    private final TrashRepository trashRepository;

    public Trash createTrash(Trash trash) {
        return trashRepository.save(trash);
    }

    public List<Trash> getAllTrash() {
        return trashRepository.findAll();
    }
}