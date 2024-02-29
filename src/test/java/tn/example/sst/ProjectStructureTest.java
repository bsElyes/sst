package tn.example.sst;

import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.base.DescribedPredicate.alwaysTrue;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.belongToAnyOf;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

@AnalyzeClasses(packagesOf = SstApplication.class, importOptions = DoNotIncludeTests.class)
class ProjectStructureTest {
    @ArchTest
    static final ArchRule respectsTechnicalArchitectureLayers = layeredArchitecture()
            .consideringAllDependencies()
            .layer("Config").definedBy("..config..")
            .layer("Web").definedBy("..rest..")
            .layer("VM").definedBy("..rest.vm..")
            .layer("DTO").definedBy("..service.dto..")
            .optionalLayer("Service").definedBy("..services..","..services.impl..","..services.broker..")
            .layer("Security").definedBy("..security..")
            .optionalLayer("Persistence").definedBy("..repository..")
            .layer("Domain").definedBy("..domain..")

            .whereLayer("Config").mayNotBeAccessedByAnyLayer()
            .whereLayer("Web").mayOnlyBeAccessedByLayers("Config")
            .whereLayer("VM").mayOnlyBeAccessedByLayers("Service","Web","Config")
            .whereLayer("DTO").mayOnlyBeAccessedByLayers("Service","Web","Config")
            .whereLayer("Service").mayOnlyBeAccessedByLayers("Web", "Config","DTO","VM")
            .whereLayer("Security").mayOnlyBeAccessedByLayers("Config", "Service", "Web")
            .whereLayer("Persistence").mayOnlyBeAccessedByLayers("Service", "Security", "Web", "Config")
            .whereLayer("Domain").mayOnlyBeAccessedByLayers("Persistence", "Service", "Security", "Web", "Config")

            .ignoreDependency(belongToAnyOf(SstApplication.class), alwaysTrue())
            .ignoreDependency(alwaysTrue(), belongToAnyOf(
                    tn.example.sst.config.Constants.class,
                    tn.example.sst.config.ApplicationProperties.class
            ));
}
