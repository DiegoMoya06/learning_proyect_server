package com.dm.learning.services.implementation.base;

import com.dm.learning.utils.BaseLogger;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

@Transactional
public abstract class BaseServiceImpl<R extends JpaRepository> extends BaseLogger {

    protected final R repository;

    @PersistenceContext
    protected EntityManager entityManager;

    protected BaseServiceImpl(R repository) {
        this.repository = repository;
    }
}
