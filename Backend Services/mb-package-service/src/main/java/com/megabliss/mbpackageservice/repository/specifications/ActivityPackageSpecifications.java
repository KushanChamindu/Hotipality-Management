package com.megabliss.mbpackageservice.repository.specifications;

import com.megabliss.mbpackageservice.model.activitypackage.ActivityPackage;
import com.megabliss.mbpackageservice.model.enums.EventLevel;
import com.megabliss.mbpackageservice.model.enums.EventType;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class ActivityPackageSpecifications {
    public static Specification<ActivityPackage> filter(Double latitude, Double longitude, Double distance,
                                                        EventType type, EventLevel level, Float review,
                                                        Boolean availability, Double lowerPrice, Double upperPrice) {
        return (root, query, criteriaBuilder) -> {
            Predicate packageType = type != null
                    ? criteriaBuilder.equal(root.get("eventType"), type)
                    : criteriaBuilder.conjunction();

            Predicate packageLevel = level != null
                    ? criteriaBuilder.equal(root.get("eventLevel"), level)
                    : criteriaBuilder.conjunction();

            Predicate packageReview = review != null
                    ? criteriaBuilder.greaterThanOrEqualTo(root.get("averageReview"), review)
                    : criteriaBuilder.conjunction();

            Predicate packageAvailability = availability != null
                    ? criteriaBuilder.equal(root.get("isActive"), availability)
                    : criteriaBuilder.conjunction();

            Predicate packagePrice;
            if (upperPrice != null && lowerPrice != null) {
                if (upperPrice < lowerPrice) {
                    throw new IllegalArgumentException("UpperPrice cannot be less than LowerPrice");
                }
                Expression<Double> price = root.get("price");
                packagePrice = criteriaBuilder.between(price, lowerPrice, upperPrice);
            } else {
                packagePrice = criteriaBuilder.conjunction();
            }

            Predicate nearestPackages;
            if (longitude != null && latitude != null && distance != null) {
                nearestPackages = findActivitiesWithinDistance(root, query, criteriaBuilder, latitude, longitude, distance);

            } else {
                nearestPackages = criteriaBuilder.conjunction();
            }
            return criteriaBuilder.and(packageType, packageLevel, packageReview, nearestPackages, packageAvailability
                    , packagePrice);

        };
    }

    public static Predicate findActivitiesWithinDistance(
            Root<ActivityPackage> root, CriteriaQuery<?> query,
            CriteriaBuilder cb, Double latitude, Double longitude, Double distance) {

        Expression<Double> latitudeAttribute = root.get("latitude");
        Expression<Double> longitudeAttribute = root.get("longitude");

        // Calculate intermediate values
        Expression<Double> radiansLatitude = cb.function("radians", Double.class, latitudeAttribute);
        Expression<Double> radiansLongitude = cb.function("radians", Double.class, longitudeAttribute);
        Expression<Double> radiansInputLatitude = cb.literal(Math.toRadians(latitude));
        Expression<Double> radiansInputLongitude = cb.literal(Math.toRadians(longitude));

        // Calculate the haversine formula expression
        Expression<Double> cos1 = cb.function("cos", Double.class, radiansInputLatitude);
        Expression<Double> cos2 = cb.function("cos", Double.class, radiansLatitude);
        Expression<Double> cos3 = cb.function("cos", Double.class,
                cb.diff(radiansLongitude, radiansInputLongitude));
        Expression<Double> sin1 = cb.function("sin", Double.class, radiansInputLatitude);
        Expression<Double> sin2 = cb.function("sin", Double.class, radiansLatitude);

        Expression<Double> haversineFormula = cb.prod(6371.0,
                cb.function("acos", Double.class,
                        cb.sum(
                                cb.prod(cb.prod(cos1, cos2), cos3),
                                cb.prod(sin1, sin2))));

        // Create a case expression for the haversine formula
        Expression<Double> latitudeInputExp = cb.literal(latitude);
        Expression<Double> longitudeInputExp = cb.literal(longitude);

        // Use the Double CriteriaBuilder to create the literal
        Expression<Double> zeroLiteral = cb.literal(0.0);
        Expression<Double> caseExpression = cb.<Double>selectCase()
                .when(
                        cb.and(
                                cb.equal(latitudeInputExp, latitudeAttribute),
                                cb.equal(longitudeInputExp, longitudeAttribute)),
                        zeroLiteral)
                .otherwise(haversineFormula);

        // Apply the predicate to the query
        Predicate whereClause = cb.lessThan(caseExpression, distance);

        // Set the where clause and ordering
        query.where(whereClause);
        query.orderBy(cb.asc(caseExpression));

        // Create the final predicate
        return query.getRestriction();
    }

    ;

}
