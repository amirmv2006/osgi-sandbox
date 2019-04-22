package ir.amv.snippets.my.runner;


import ir.amv.snippets.my.api.IMyOSGiService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.karaf.options.LogLevelOption;
import org.ops4j.pax.exam.options.MavenArtifactUrlReference;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;

import javax.inject.Inject;
import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.ops4j.pax.exam.CoreOptions.maven;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.*;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class ActivatorTest {

    @Configuration
    public Option[] config() {
        MavenArtifactUrlReference karafUrl = maven().groupId("org.apache.karaf").artifactId("apache-karaf").versionAsInProject().type("tar.gz");
        return new Option[]{
                //KarafDistributionOption.debugConfiguration("8889", true),
                karafDistributionConfiguration().frameworkUrl(karafUrl).name("Apache Karaf").unpackDirectory(new File("target/exam")),
                // enable JMX RBAC security, thanks to the KarafMBeanServerBuilder
                configureSecurity().disableKarafMBeanServerBuilder(),
                // configureConsole().ignoreLocalConsole(),
                keepRuntimeFolder(),
                logLevel(LogLevelOption.LogLevel.INFO),
                mavenBundle().groupId("ir.amv.snippets.sandbox").artifactId("my-enhancer").versionAsInProject(),
                mavenBundle().groupId("ir.amv.snippets.sandbox").artifactId("my-sample-api").versionAsInProject(),
                mavenBundle().groupId("ir.amv.snippets.sandbox").artifactId("my-sample-api-client").versionAsInProject(),
                debugConfiguration("5005", true)
        };
    }

    @Inject
    IMyOSGiService myOSGiService;

    @Test
    public void testSomething() {
        String concat = myOSGiService.concat("Tes", "ting");
        assertEquals("Testing", concat);
    }

}