package com.impacus.maketplace.repository.consumer;

import com.impacus.maketplace.entity.consumer.ConsumerHourlyVisitors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumerHourlyVisitorsRepository extends JpaRepository<ConsumerHourlyVisitors, Long> {
}
