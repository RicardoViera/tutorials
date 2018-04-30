package com.galileo.services;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.galileo.datafetcher.AllBooksDataFetcher;
import com.galileo.datafetcher.BookDataFetcher;
import com.galileo.model.Book;
import com.galileo.repository.BookRepository;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;

@Service
public class GraphQLService {
	 
	@Value("classpath:books.graphql")
	Resource resource;
	private GraphQL graphQL;
	
	@Autowired
	BookRepository bookRepo;
	
	@Autowired
    private AllBooksDataFetcher allBooksDataFetcher;
	@Autowired
    private BookDataFetcher bookDataFetcher;
	
	@PostConstruct
	public void loadSchema()throws IOException{
		loadDataIntoHSQL();
		File schemaFile =resource.getFile();
		TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(schemaFile);
		RuntimeWiring wiring = buildRuntimeWiring();
		GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(typeRegistry, wiring);
		graphQL =GraphQL.newGraphQL(schema).build();
	}

	private void loadDataIntoHSQL() {
		Stream.of(
				new Book("123","One","Kindle",new String[]{"Chloe"},
						"Nov 2017"),
				new Book("222","Two","Kindle",new String[]{"rick"},
						"Nov 2017"),
				new Book("333","One","Kindle",new String[]{"Praveen"},
						"Nov 2017")
				).forEach(book->{
			bookRepo.save(book);
		});
		
	}

	private RuntimeWiring buildRuntimeWiring() {
		return RuntimeWiring.newRuntimeWiring()
				.type("Query", typeWiring->
					typeWiring
					.dataFetcher("allBooks", allBooksDataFetcher )
					.dataFetcher("book", bookDataFetcher)
				)
				.build();
	}
	
	public GraphQL getGraphQL() {
		return graphQL;
	}
}
