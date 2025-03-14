package com.impacus.maketplace.repository.consumer;

import com.impacus.maketplace.entity.consumer.ConsumerDailyVisitors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumerDailyVisitorsRepository extends JpaRepository<ConsumerDailyVisitors, Long> {
}
