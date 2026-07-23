package com.se1906.laptopshop.repository;

import com.se1906.laptopshop.entity.ConfigurationVersion;
import com.se1906.laptopshop.entity.Laptop;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class LaptopSpecification {

    public static Specification<Laptop> filterLaptops(
            Integer categoryId,
            Integer brandId,
            List<String> cpus,
            List<String> rams,
            List<String> storages,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            String keyword) {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Avoid duplicate rows when joining
            query.distinct(true);

            // Filter by Keyword
            if (keyword != null && !keyword.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("laptopName")),
                        "%" + keyword.trim().toLowerCase() + "%"
                ));
            }

            // Filter by Category
            if (categoryId != null) {
                predicates.add(criteriaBuilder.equal(root.get("category").get("categoryId"), categoryId));
            }

            // Filter by Brand
            if (brandId != null) {
                predicates.add(criteriaBuilder.equal(root.get("brand").get("brandId"), brandId));
            }

            // Join with ConfigurationVersion if needed
            boolean needConfigJoin = (cpus != null && !cpus.isEmpty()) ||
                                     (rams != null && !rams.isEmpty()) ||
                                     (storages != null && !storages.isEmpty()) ||
                                     minPrice != null || maxPrice != null;

            if (needConfigJoin) {
                Join<Laptop, ConfigurationVersion> configJoin = root.join("configurationVersions", JoinType.INNER);

                // Filter by CPU
                if (cpus != null && !cpus.isEmpty()) {
                    predicates.add(configJoin.get("cpu").in(cpus));
                }

                // Filter by RAM
                if (rams != null && !rams.isEmpty()) {
                    predicates.add(configJoin.get("ram").in(rams));
                }

                // Filter by Storage
                if (storages != null && !storages.isEmpty()) {
                    predicates.add(configJoin.get("storage").in(storages));
                }

                // Filter by Price range
                if (minPrice != null) {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(configJoin.get("price"), minPrice));
                }
                if (maxPrice != null) {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(configJoin.get("price"), maxPrice));
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
