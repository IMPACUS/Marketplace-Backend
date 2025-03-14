package com.impacus.maketplace.entity.consumer;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "consumer_daily_visitors")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConsumerDailyVisitors {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consumer_daily_visitors_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private LocalDate date;

    @Column(nullable = false)
    private long visitors;

    public ConsumerDailyVisitors(long visitors) {
        this.visitors = visitors;
        this.date = LocalDate.now().minusDays(1);
    }

    public static ConsumerDailyVisitors toEntity(long visitors) {
        return new ConsumerDailyVisitors(visitors);
    }
}
