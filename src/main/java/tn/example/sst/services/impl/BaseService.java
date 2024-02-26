package tn.example.sst.services.impl;

import java.util.List;
import java.util.Optional;

public interface BaseService<T> {
    T save(T ingredient);

    T update(T ingredient);

    Optional<T> partialUpdate(T ingredient);

    List<T> findAll();

    Optional<T> findOne(Long id);

    void delete(Long id);
}
