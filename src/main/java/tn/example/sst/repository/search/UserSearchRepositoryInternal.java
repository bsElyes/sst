package tn.example.sst.repository.search;

import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import tn.example.sst.domain.User;

import java.util.stream.Stream;

interface UserSearchRepositoryInternal {
    Stream<User> search(String query);

    @Async
    @Transactional
    void index(User entity);

    @Async
    @Transactional
    void deleteFromIndex(User entity);
}

