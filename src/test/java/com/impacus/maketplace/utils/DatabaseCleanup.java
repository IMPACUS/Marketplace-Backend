package com.impacus.maketplace.utils;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ActiveProfiles("test")
@Service
public class DatabaseCleanup implements InitializingBean {
	@PersistenceContext
	private EntityManager entityManager;

	private List<String> tableNames;

	@Override
	public void afterPropertiesSet() {
		tableNames = entityManager.getMetamodel().getEntities().stream()
				.filter(entity -> entity.getJavaType().getAnnotation(Entity.class) != null)
				.map(entity -> entity.getName().replaceAll("([a-z])([A-Z])", "$1_$2"))
				.collect(Collectors.toList());
		entityManager.getMetamodel().getEntities().stream()
				.filter(entity -> entity.getJavaType().getAnnotation(Entity.class) != null)
				.map(entity -> entity.getName().replaceAll("([a-z])([A-Z])", "$1_$2"))
				.collect(Collectors.toList());


		// table 이름 안맞는경우
		tableNames = removeNameFromList( tableNames,"Product","User","Temporary_Product");
		tableNames.add("Product_Info");
		tableNames.add("User_Info");
		tableNames.add("temporary_product_info");
		tableNames.forEach(System.out::println);
	}


	@Transactional
	public void execute() {
		entityManager.flush();
		entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
		for (String tableName : tableNames) {
			entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();

			// 테이블 아이디값 초기화
			if (tableName.equals("Dormant_User")||tableName.equals("Attach_File_Group")||tableName.equals("Attach_File")) {

			} else if (tableName.equals("Email_History")) {
				entityManager.createNativeQuery("ALTER TABLE " + tableName + " ALTER COLUMN history_id RESTART WITH 1").executeUpdate();
			} else if (tableName.equals("User_Info")) {
				entityManager.createNativeQuery("ALTER TABLE " + tableName + " ALTER COLUMN user_id RESTART WITH 1").executeUpdate();
			}  else if (tableName.equals("Coupon_Issuance_Classification_Data")) {
				entityManager.createNativeQuery("ALTER TABLE " + tableName + " ALTER COLUMN cic_id RESTART WITH 1").executeUpdate();
			} else {
				entityManager.createNativeQuery("ALTER TABLE " + tableName + " ALTER COLUMN " + tableName + "_ID RESTART WITH 1").executeUpdate();
			}
		}
		entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
	}

	private static List<String> removeNameFromList( List<String> stringList, String name1, String name2, String name3) {
		List<String> modifiedList = new ArrayList<>();
		for (String str : stringList) {
			if (!str.equals(name1) && !str.equals(name2) && !str.equals(name3)) {
				modifiedList.add(str);
			}
		}
		return modifiedList;
	}
}
