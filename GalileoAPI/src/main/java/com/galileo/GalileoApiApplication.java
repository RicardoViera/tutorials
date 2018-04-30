package com.galileo;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.galileo.client.GalileoClient;

@SpringBootApplication
public class GalileoApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(GalileoApiApplication.class, args);
		
		GalileoClient gc = new GalileoClient();
		try {
			gc.sendRequest();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
