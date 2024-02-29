package tn.example.sst.repository.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import tn.example.sst.domain.User;

/**
 * Spring Data Elasticsearch repository for the User entity.
 */
public interface UserSearchRepository extends ElasticsearchRepository<User, Long>, UserSearchRepositoryInternal {
}


