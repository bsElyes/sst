package tn.example.sst;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import tn.example.sst.config.AsyncSyncConfiguration;
import tn.example.sst.config.EmbeddedElasticsearch;
import tn.example.sst.config.EmbeddedKafka;
import tn.example.sst.config.EmbeddedSQL;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = {SstApplication.class, AsyncSyncConfiguration.class})
@EmbeddedElasticsearch
@EmbeddedSQL
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@EmbeddedKafka
public @interface IntegrationTest {
}
