package com.ecommerce.studentmarket.admin.configs;


import com.ecommerce.studentmarket.admin.systemwallet.domains.SystemWalletDomain;
import com.ecommerce.studentmarket.admin.systemwallet.repositories.SystemWalletRepository;
import com.ecommerce.studentmarket.admin.user.domains.AdminDomain;
import com.ecommerce.studentmarket.admin.user.repositories.AdminRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Slf4j
public class AdminApplicationInitConfig {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public ApplicationRunner applicationRunner (AdminRepository adminRepository, SystemWalletRepository systemWalletRepository) {
        return args -> {

            SystemWalletDomain systemWallet;

            if (systemWalletRepository.count() == 0) {
                systemWallet = new SystemWalletDomain();
                systemWallet = systemWalletRepository.save(systemWallet);
            } else {
                systemWallet = systemWalletRepository.findTopByOrderByMaVHTAsc().orElseThrow();
            }

            if (adminRepository.count() == 0) {

                AdminDomain admin = AdminDomain.builder()
                        .mscb("00001")
                        .hoTen("Root Admin")
                        .sdt("0913414775")
                        .password(passwordEncoder.encode("adminSystem1234"))
                        .role("admin")
                        .systemWallet(systemWallet)
                        .build();
                adminRepository.save(admin);
                log.warn("Root Admin 00001 đã được khởi tạo với mật khẩu: adminSystem1234");
            }
        };
    }
}
