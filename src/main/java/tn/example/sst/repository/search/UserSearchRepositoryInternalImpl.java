package tn.example.sst.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import tn.example.sst.domain.User;
import tn.example.sst.repository.UserRepository;

import java.util.stream.Stream;

class UserSearchRepositoryInternalImpl implements UserSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final UserRepository repository;

    UserSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, UserRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<User> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return elasticsearchTemplate.search(nativeQuery, User.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(User entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndex(User entity) {
        elasticsearchTemplate.delete(entity);
    }
}
