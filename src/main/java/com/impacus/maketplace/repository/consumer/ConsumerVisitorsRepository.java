package com.impacus.maketplace.repository.consumer;

import com.impacus.maketplace.entity.consumer.ConsumerVisitors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumerVisitorsRepository extends JpaRepository<ConsumerVisitors, Long> {
}
