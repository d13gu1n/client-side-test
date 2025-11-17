package com.wyutani.demo;

import java.time.Duration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

@SpringBootApplication
@RestController
public class ClientSideTestApplication {
	
	@GetMapping("/user")
	public String user() {
		return "Dieguin";
	}

	@PostMapping("/greet")
	public String greet(@ModelAttribute Greeting values) {
		return "Hello " + values.getValue() + "!";
	}

	@GetMapping(path = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<String> stream() {
		return Flux.interval(Duration.ofSeconds(5)).map(
			value -> value + ":" + System.currentTimeMillis()
		);
	}

	@GetMapping(path = "/test")
	public String test() {
		return "<div id=\"hello\" hx-swap-oob=\"true\">Hello</div>\n"
			+ "<div id=\"world\" hx-swap-oob=\"true\">World</div>";
	}
	public static void main(String[] args) {
		SpringApplication.run(ClientSideTestApplication.class, args);
	}

	static class Greeting {
		private String value;
		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}

}
