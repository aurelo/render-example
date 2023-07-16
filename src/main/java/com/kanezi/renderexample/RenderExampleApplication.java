package com.kanezi.renderexample;

import lombok.Value;
import lombok.experimental.NonFinal;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

@SpringBootApplication
public class RenderExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(RenderExampleApplication.class, args);
	}




	@RestController
	@RequestMapping("/")
	@Value
	public static class PostgresController {

		PostgresRepository postgresRepository;

		@GetMapping
		public String home() {
			return postgresRepository.getTimeOfDay();
		}
	}


	@Repository
	@Value
	@NonFinal
	public static class PostgresRepository {

		DataSource dataSource;

		String getTimeOfDay() {

			String result;

			try (CallableStatement timeOfDay = dataSource
					.getConnection()
					.prepareCall("{? = call timeofday()}")) {

				timeOfDay.registerOutParameter(1, Types.VARCHAR);

				timeOfDay.execute();
				result = timeOfDay.getString(1);

			} catch (SQLException e) {
				throw new RuntimeException(e);
			}

			return result;
		}
	}


}
