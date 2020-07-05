package com.kt.ibs.configuration;

import com.kt.ibs.entity.*;
import com.kt.ibs.entity.Currency;
import com.kt.ibs.repository.*;
import com.kt.ibs.repository.CustomerRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.nio.file.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Stream;

@Component
@Slf4j
public class DataBootstrapListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BankStaffRepository bankStaffRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private BankBranchRepository bankBranchRepository;

    @Autowired
    private FinancialInstitutionRepository financialInstitutionRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CustomerAuditRepository customerAuditRepository;

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent contextRefreshedEvent) {
        Country country = null;
        if (!bankBranchRepository.findAll().iterator().hasNext()) {
            country = countryRepository.save(new Country("ZWE", "Zimbabwe"));
            countryRepository.save(new Country("RSA", "South Africa"));
            Map<String, List<BankBranch>> branches = new HashMap<>();
            try {
                final URI uri = getClass().getClassLoader()
                        .getResource("MainList-Table.csv").toURI();

                Path path = null;
                if (uri.getScheme().equals("jar")) {
                    FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
                    path = fileSystem.getPath("/BOOT-INF/classes/MainList-Table.csv");
                } else {
                    // Not running in a jar, so just use a regular filesystem path
                    path = Paths.get(uri);
                }
                StringBuilder data = new StringBuilder();
                Stream<String> lines = Files.lines(path);
                lines.forEach(line -> {
                    String[] split = line.split(";");
                    List<BankBranch> list = branches.get(split[0]);
                    if (list == null) {
                        list = new ArrayList<>();
                    }
                    list.add(new BankBranch(null, split[2], split[1]));
                    branches.put(split[0], list);
                    data.append(line).append("\n");

                });
                lines.close();

            } catch (Exception e) {
                log.error("Error loading data", e);
            }
            for (Entry<String, List<BankBranch>> entry : branches.entrySet()) {
                Bank bank = bankRepository.save(new Bank(entry.getKey(), entry.getKey(), country));
                for (BankBranch branch : entry.getValue()) {
                    branch.setBank(bank);
                    bankBranchRepository.save(branch);
                }

            }
        }
        if (authorityRepository.findAll().isEmpty()) {
            for (AuthorityName role : AuthorityName.values()) {
                authorityRepository.save(new Authority(role));
            }
            Customer customer = new Customer("tatenda", "t1234", authorityRepository.findByName(AuthorityName.ROLE_RETAIL_CUSTOMER));
            customer.setCustomerId("111423");
            customer.setNotificationType(NotificationType.EMAIL);
            customer.setEmail("evans.armitage@gmail.com");
            customer.setFullname("Tatenda");
            customer.setPhoneNumber("27835540735");
            customerRepository.save(customer);


            Customer customer2 = new Customer("mandela", "m1234", authorityRepository.findByName(AuthorityName.ROLE_RETAIL_CUSTOMER));
            customer2.setCustomerId("111457");
            customer2.setNotificationType(NotificationType.EMAIL);
            customer2.setEmail("cschira@gmail.com");
            customer2.setFullname("Nelson");
            customer2.setPhoneNumber("27786101500");
            customerRepository.save(customer2);

            currencyRepository.save(new Currency("NAD", "NAD", "$"));
            //currencyRepository.save(new Currency("NAD", "NAD", "$"));
            currencyRepository.save(new Currency("ZAR", "RAND", "R"));
            Currency currency = currencyRepository.save(new Currency("USD", "USD", "$"));

            FinancialInstitution defaultBank = new FinancialInstitution();
            defaultBank.setName("Metropolitan");
            defaultBank.setDefaultCurrency(currency);
            defaultBank.setCountry(country);
            defaultBank = financialInstitutionRepository.save(defaultBank);
            bankStaffRepository.save(new BankStaff("admin1", "admin@123", defaultBank, authorityRepository.findByName(AuthorityName.ROLE_ADMIN)));
            bankStaffRepository.save(new BankStaff("admin", "easy", defaultBank, authorityRepository.findByName(AuthorityName.ROLE_ADMIN)));

        }

    }
}
