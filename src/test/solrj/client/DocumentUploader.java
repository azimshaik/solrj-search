package test.solrj.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;

public class DocumentUploader
{
	private static final String SolrServerUrl = "http://localhost:8983/solr/test";

	private StringBuilder sb;

	public static void main(String[] args)
	{
		String documentName = "Tableau.txt";
		String fullPath = "C:/Solr/documents";
		
		//begin-get all the file names in the folder
		File folder = new File("C:/Solr/documents");
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
		      if (listOfFiles[i].isFile()) {
		        System.out.println("File " + listOfFiles[i].getName());
		      } else if (listOfFiles[i].isDirectory()) {
		        System.out.println("Directory " + listOfFiles[i].getName());
		      }
		    }
		//end-get all the file names in the folder
		DocumentUploader documentUploader = new DocumentUploader();

		try
		{
			documentUploader.uploadDocument(documentName, fullPath);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void uploadDocument(String documentName, String fullPath) throws Exception
	{
		SolrInputDocument doc = new SolrInputDocument();

		doc.addField("id", documentName.hashCode());
		doc.addField("title", documentName);

		readDocumentContent(documentName, fullPath);

		doc.addField("text", sb.toString());

		HttpSolrClient client = new HttpSolrClient(SolrServerUrl);

		client.add(doc);

		client.commit();

		client.close();
	}

	/*
	 * private void readDocumentContent(String documentName, String fullPath) { sb = new StringBuilder();
	 * 
	 * Path path = Paths.get(fullPath, documentName);
	 * 
	 * try(Stream<String> stream = Files.lines(path)) { stream.forEach(s -> append(s)); } catch(Exception ex) {
	 * ex.printStackTrace(); } }
	 */

	private void readDocumentContent(String documentName, String fullPath) throws Exception
	{
		sb = new StringBuilder();
		
		InputStream is = new FileInputStream(fullPath + "/" + documentName);

		InputStreamReader isr = new InputStreamReader(is);

		BufferedReader br = new BufferedReader(isr);

		while (true)
		{
			String line = br.readLine();
			if (line == null)
			{
				break;
			}
			
			append(line);
		}
		
		br.close();
	}

	private void append(String s)
	{
		sb.append(s);
		sb.append("\n");
	}
}
