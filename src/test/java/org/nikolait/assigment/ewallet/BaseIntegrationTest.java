package org.nikolait.assigment.ewallet;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.nikolait.assigment.ewallet.confiog.TestcontainersConfiguration;
import org.nikolait.assigment.ewallet.repository.WalletJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.nikolait.assigment.ewallet.util.WalletBuilder.newWallet;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@Import(TestcontainersConfiguration.class)
public abstract class BaseIntegrationTest {

    protected static UUID wallet1Id;
    protected static UUID wallet2Id;
    protected static UUID wallet3Id;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected WalletJpaRepository walletJpaRepository;

    @BeforeEach
    void setUp() {
        wallet1Id = walletJpaRepository.save(newWallet(0)).getId();
        wallet2Id = walletJpaRepository.save(newWallet(100)).getId();
        wallet3Id = walletJpaRepository.save(newWallet(200)).getId();
    }

    @AfterEach
    void tearDown() {
        walletJpaRepository.deleteAll();
    }

}
