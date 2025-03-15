package com.impacus.maketplace.entity.consumer;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "consumer_hourly_visitors")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConsumerHourlyVisitors {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consumer_hourly_visitors_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private LocalDate date;

    @Column(nullable = false)
    private int hour;

    @Column(nullable = false)
    private long visitors;

    public ConsumerHourlyVisitors(int hour, long visitors) {
        this.visitors = visitors;
        this.hour = hour;
        this.date = LocalDate.now();
    }

    public static ConsumerHourlyVisitors toEntity(int hour, long visitors) {
        return new ConsumerHourlyVisitors(hour, visitors);
    }
}
