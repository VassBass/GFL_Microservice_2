package group.executor.service.proxy.sourceFile;

import group.executor.model.ProxyConfigHolder;
import group.executor.model.ProxyCredentials;
import group.executor.model.ProxyNetworkConfig;
import group.executor.service.handler.ProxySourceQueueHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
class DefaultProxySourceFileTest {
    private ProxySourceFile proxySourceFile;
    private ProxySourceQueueHandler queueHandler;

    private List<ProxyConfigHolder> listProxyLikeInFile;

    @Autowired
    private void getBeans(ProxySourceQueueHandler queueHandler, ProxySourceFile proxySourceFile) {
        this.queueHandler = queueHandler;
        this.proxySourceFile = proxySourceFile;
    }

    @BeforeEach
    void setUp() {
        listProxyLikeInFile = List.of(
                new ProxyConfigHolder(
                        new ProxyNetworkConfig("host_name_0", 0),
                        new ProxyCredentials("user_0", "123")),
                new ProxyConfigHolder(
                        new ProxyNetworkConfig("host_name_1", 1),
                        new ProxyCredentials("user_1", "456")),
                new ProxyConfigHolder(
                        new ProxyNetworkConfig("host_name_2", 2),
                        new ProxyCredentials("user_2", "789"))
        );

        proxySourceFile.setSourceFile(new File("testProxy.json"));
    }

    @Test
    void testExtractProxy() throws IOException {
        proxySourceFile.extractProxy();

        assertEquals(listProxyLikeInFile.get(0), queueHandler.pollProxy().orElseThrow());
        assertEquals(listProxyLikeInFile.get(1), queueHandler.pollProxy().orElseThrow());
        assertEquals(listProxyLikeInFile.get(2), queueHandler.pollProxy().orElseThrow());
    }
}