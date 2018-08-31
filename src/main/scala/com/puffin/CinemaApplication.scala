package com.puffin

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class CinemaApplication

object CinemaApplication extends App {
	SpringApplication.run(classOf[CinemaApplication], args: _*)
}
