package com.dbbyte.kotlinstart

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import java.util.stream.Stream

@SpringBootApplication
class KotlinStartApplication (val personRepository: PersonRepository): CommandLineRunner //should this be open? ; repository is provided as constructor
{
    override fun run(vararg p0: String?) {

        //in case i have values in db, delete them
        personRepository.deleteAll()

        // start the stream for demo population
        Stream.of("Adrian,Curia","Andreea,Curia","Ionut,Firca")
                .map { fn -> fn.split(",") } // now i have touples
                .forEach{ tpl -> personRepository.save( Person ( tpl[0], tpl[1])) }

        personRepository.all().forEach{ println(it) }
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(KotlinStartApplication::class.java, *args)
}

@Document
data class Person (var first: String? = null, var last: String? = null, @Id var id: String? = null) // data tells that this is a POJO, so getters, setters, toString... are automatically

// repository
interface PersonRepository: MongoRepository<Person, String> {

    @Query("{}")
    fun all(): Stream<Person>
}


// sample data implementing command line runner