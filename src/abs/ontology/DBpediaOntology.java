package abs.ontology;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashSet;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.FileManager;

public class DBpediaOntology {
	public static void main(String[] args){
		findCategories();
	}

	public static void findCategories()
	{
		// create an empty model
		Model model = ModelFactory.createDefaultModel();

		String inputFileName = "data/dbpedia/dbpedia_3.6.owl";
		// use the FileManager to find the input file
		InputStream in = FileManager.get().open( inputFileName );
		if (in == null) {
			throw new IllegalArgumentException(
					"File: " + inputFileName + " not found");
		}

		// read the RDF/XML file
		model.read(in, null);

		StmtIterator iter = model.listStatements();//.listObjects();
		HashSet<String> level1Classes = new HashSet<String>(), level2Classes = new HashSet<String>();
		while (iter.hasNext()){
			//RDFNode node = iter.next();
			Statement node = iter.next();
			if (node.getPredicate().getURI().equals("http://www.w3.org/2000/01/rdf-schema#subClassOf")){
				//				System.out.println(node.getSubject()+"\t"+node.getObject().toString());
				if (node.getObject().toString().equals("http://www.w3.org/2002/07/owl#Thing"))
					level1Classes.add(node.getSubject().toString());
				else if (node.getObject().toString().equals("http://dbpedia.org/ontology/Person")){
					level2Classes.add(node.getSubject().toString()/*+"\tPerson"*/);
				}
				else if (node.getObject().toString().equals("http://dbpedia.org/ontology/Organisation")){
					level2Classes.add(node.getSubject().toString()/*+"\tOrganisation"*/);
				}
				else if (node.getObject().toString().equals("http://dbpedia.org/ontology/Place")){
					level2Classes.add(node.getSubject().toString()/*+"\tPlace"*/);
				}
					
			}
		}
		System.out.println(level1Classes.size()+" "+level2Classes.size());
		try{
			System.setOut(new PrintStream(new FileOutputStream("data/dbpedia/level1.class")));
			for (String cls : level1Classes){
				System.out.println(cls);
			}
			System.setOut(new PrintStream(new FileOutputStream("data/dbpedia/level2.class")));
			for (String cls : level2Classes){
				System.out.println(cls);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		// write it to standard out
		//		model.write(System.out);
	}
}
