package org.example.vehicles_rental.configure;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.admin.setting.entity.SecuritySettings;
import org.example.vehicles_rental.admin.setting.repository.SecuritySettingsRepository;
import org.example.vehicles_rental.entity.Brand;
import org.example.vehicles_rental.entity.Categories;
import org.example.vehicles_rental.entity.PaymentMethod;
import org.example.vehicles_rental.entity.Vehicle;
import org.example.vehicles_rental.enums.PaymentMethodName;
import org.example.vehicles_rental.enums.PaymentMethodStatus;
import org.example.vehicles_rental.enums.Status;
import org.example.vehicles_rental.repository.BrandRepository;
import org.example.vehicles_rental.repository.CategoryRepository;
import org.example.vehicles_rental.repository.PaymentMethodRepository;
import org.example.vehicles_rental.repository.VehicleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final SecuritySettingsRepository securitySettingsRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final VehicleRepository vehicleRepository;

    @Bean
    CommandLineRunner initDefaultData() {
        return args -> {

            if (securitySettingsRepository.count() == 0) {

                SecuritySettings settings = new SecuritySettings();

                settings.setTwoFactorAuthentication(false);
                settings.setSessionTimeoutMinutes(30);

                securitySettingsRepository.save(settings);
            }

            paymentMethodRepository
                    .findByPaymentMethodNameAndStatus(
                            PaymentMethodName.BAKONG,
                            PaymentMethodStatus.ACTIVE
                    )
                    .orElseGet(() -> paymentMethodRepository.save(
                            PaymentMethod.builder()
                                    .paymentMethodName(PaymentMethodName.BAKONG)
                                    .description("Bakong KHQR")
                                    .status(PaymentMethodStatus.ACTIVE)
                                    .build()
                    ));

            Categories sedan = findOrCreateCategory("Sedan", "Comfortable daily rental cars");
            Categories suv = findOrCreateCategory("SUV", "Spacious vehicles for trips");
            Categories motorcycle = findOrCreateCategory("Motorcycle", "Light city transport");
            Categories luxury = findOrCreateCategory("Luxury", "Premium rental vehicles");

            Brand bmw = findOrCreateBrand("BMW", sedan);
            Brand toyota = findOrCreateBrand("Toyota", suv);
            Brand honda = findOrCreateBrand("Honda", motorcycle);
            Brand rollsRoyce = findOrCreateBrand("Rolls Royce", luxury);
            Brand audi = findOrCreateBrand("Audi", sedan);

            createVehicleIfMissing(
                    "BMW 5 Series",
                    "5 Series",
                    2024,
                    "PP-001-BMW",
                    "Automatic",
                    "Gasoline",
                    5,
                    new BigDecimal("0.10"),
                    Status.AVAILABLE,
                    "Premium sedan with smooth handling.",
                    sedan,
                    bmw);
            createVehicleIfMissing(
                    "Toyota RAV4 Hybrid",
                    "RAV4 Hybrid",
                    2024,
                    "PP-002-TOY",
                    "Automatic",
                    "Hybrid",
                    5,
                    new BigDecimal("0.25"),
                    Status.AVAILABLE,
                    "Efficient SUV for city and weekend trips.",
                    suv,
                    toyota);
            createVehicleIfMissing(
                    "Honda CBR650R",
                    "CBR650R",
                    2023,
                    "PP-003-HON",
                    "Manual",
                    "Gasoline",
                    2,
                    new BigDecimal("0.10"),
                    Status.AVAILABLE,
                    "Sport motorcycle for confident riders.",
                    motorcycle,
                    honda);
            createVehicleIfMissing(
                    "Rolls Royce Ghost",
                    "Ghost",
                    2023,
                    "PP-004-RR",
                    "Automatic",
                    "Gasoline",
                    4,
                    new BigDecimal("0.20"),
                    Status.MAINTENANCE,
                    "Luxury vehicle for special occasions.",
                    luxury,
                    rollsRoyce);
            createVehicleIfMissing(
                    "Audi A4 Premium",
                    "A4 Premium",
                    2024,
                    "PP-005-AUD",
                    "Automatic",
                    "Gasoline",
                    5,
                    new BigDecimal("0.95"),
                    Status.AVAILABLE,
                    "Refined sedan with a quiet cabin.",
                    sedan,
                    audi);
            createVehicleIfMissing(
                    "Toyota Corolla Cross",
                    "Corolla Cross",
                    2024,
                    "PP-006-TOY",
                    "Automatic",
                    "Hybrid",
                    5,
                    new BigDecimal("75.00"),
                    Status.AVAILABLE,
                    "Compact SUV with strong fuel economy.",
                    suv,
                    toyota);
        };
    }

    private Categories findOrCreateCategory(String name, String description) {
        return categoryRepository.findByCategoryNameIgnoreCase(name)
                .orElseGet(() -> categoryRepository.save(
                        Categories.builder()
                                .category_name(name)
                                .description(description)
                                .build()));
    }

    private Brand findOrCreateBrand(String name, Categories category) {
        return brandRepository.findByBrandNameIgnoreCase(name)
                .orElseGet(() -> brandRepository.save(
                        Brand.builder()
                                .brand_name(name)
                                .categories(List.of(category))
                                .build()));
    }

    private void createVehicleIfMissing(
            String name,
            String model,
            int year,
            String plateNumber,
            String transmission,
            String fuelType,
            int seats,
            BigDecimal pricePerDay,
            Status status,
            String description,
            Categories category,
            Brand brand) {

        vehicleRepository.findByNameIgnoreCase(name)
                .ifPresentOrElse(vehicle -> {
                    if (vehicle.getPricePerDay() == null
                            || vehicle.getPricePerDay().compareTo(pricePerDay) != 0) {
                        vehicle.setPricePerDay(pricePerDay);
                        vehicleRepository.save(vehicle);
                    }
                }, () -> vehicleRepository.save(
                        Vehicle.builder()
                                .name(name)
                                .model(model)
                                .year(year)
                                .plate_number(plateNumber)
                                .transmission(transmission)
                                .fuel_type(fuelType)
                                .seat(seats)
                                .pricePerDay(pricePerDay)
                                .status(status)
                                .description(description)
                                .category(category)
                                .brand(brand)
                                .build()));
    }
}
