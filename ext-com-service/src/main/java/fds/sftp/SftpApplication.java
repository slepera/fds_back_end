package fds.sftp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SftpApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(SftpApplication.class, args);
	}

}
