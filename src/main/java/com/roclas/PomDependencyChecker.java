package com.roclas;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.rmi.server.LoaderHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.util.logging.Logger;

public class PomDependencyChecker {

    final static ClassLoader loader =  PomDependencyChecker.class.getClassLoader();
    
	public static Object[] getDependencies(String filename) {
		SAXBuilder builder = new SAXBuilder();
		File xmlFile = new File(filename);
		HashMap<String, String> file_id = new HashMap<String, String>();
		ArrayList dependencies = new ArrayList<String>();
		try {
			Document document = (Document) builder.build(xmlFile);
			Element rootNode = document.getRootElement();
			List list = rootNode.getChildren();
			for (int i = 0; i < list.size(); i++) {
				Element node = (Element) list.get(i);
				String name = node.getName();
				//if (name.equalsIgnoreCase("groupId") || name.equalsIgnoreCase("artifactId") || name.equalsIgnoreCase("version")) { file_id.put(name, node.getTextTrim()); }
				if (name.equalsIgnoreCase("dependencies")) {
					List list2 = node.getChildren();
					for (int j = 0; j < list2.size(); j++) {
						Element node2 = (Element) list2.get(j);
						String name2 = node2.getName();
						if (name2.equalsIgnoreCase("dependency")) {
							HashMap<String, String> dependency = new HashMap<String, String>();
							List list3 = node2.getChildren();
							for (int k = 0; k < list3.size(); k++) {
								Element node3 = (Element) list3.get(k);
								String name3 = node3.getName();
								if(name3.equalsIgnoreCase("groupId")
										|| name3.equalsIgnoreCase("artifactId")
										|| name3.equalsIgnoreCase("version")) {
									dependency.put(name3, node3.getTextTrim());
								}
							}
							dependencies.add(dependency.get("groupId") + "__" + dependency.get("artifactId") );//+depencency.get("version")
						}
					}
				}
			}
		} catch (Exception io) { System.out.println(io.getMessage()); } 
		return dependencies.toArray();
	}

	private static String getProjectName(String filename) throws JDOMException, IOException {
		SAXBuilder builder = new SAXBuilder();
		File xmlFile = new File(filename);
		HashMap<String, String> file_id = new HashMap<String, String>();
		ArrayList<HashMap<String, String>> dependencies = new ArrayList<HashMap<String, String>>();
		ArrayList<String> result = new ArrayList<String>();
		Document document = (Document) builder.build(xmlFile);
		Element rootNode = document.getRootElement();
		List list = rootNode.getChildren();
		for (int i = 0; i < list.size(); i++) {
			Element node = (Element) list.get(i);
			String name = node.getName();
			if (name.equalsIgnoreCase("groupId")
					|| name.equalsIgnoreCase("artifactId")
					|| name.equalsIgnoreCase("version")) {
				file_id.put(name, node.getTextTrim());
			}
		}
		String id = file_id.get("groupId") + "__" + file_id.get("artifactId");
		return id;
	}

	public static void printDependencyTree(String input_dir, String output_dir) throws IOException {
        File[] files = new File(input_dir).listFiles();
        BufferedWriter out = null;
        
        String output_file=output_dir+"/dependencies.html";
        
        
        
        FileWriter fw= new FileWriter(output_file);
        out = new BufferedWriter(fw);
        
        FileInputStream fstream0 = new FileInputStream("src/main/static/header_template.html.tmpl");
        DataInputStream in0 = new DataInputStream(fstream0);
        BufferedReader br0 = new BufferedReader(new InputStreamReader(in0));
        String strLine0;
        while ((strLine0 = br0.readLine()) != null)   { out.write(strLine0+"\n"); }
        in0.close();
        
        
        
		HashMap<String,Object[]> dependencies=new HashMap<String,Object[]>() ;
		HashMap<String,ArrayList<String>> reverse_dependencies=new HashMap<String,ArrayList<String>>() ;
		for (File file : files) {
			if (file.isDirectory()) {
				//System.out.println("Directory: " + file.getName());
				// showFiles(file.listFiles()); // Calls same method again.
			} else {
				Object[] dep = getDependencies(file.getAbsolutePath());
				try { String id = getProjectName(file.getAbsolutePath());
					dependencies.put(id, dep);
					for(int i=dep.length;i-->0;){
						String key = (String)dep[i];
						if(reverse_dependencies.get(key)==null){
							reverse_dependencies.put(key, new ArrayList<String>());
						}else{ reverse_dependencies.get(key).add(id); }
					}
				} catch (Exception e) {System.out.println(e.getStackTrace()); }
			}
		}
        System.out.println("tres");
		for (String k: dependencies.keySet()) {
		    Object[] deps= dependencies.get(k);
		    ArrayList<String> reverse_deps = reverse_dependencies.get(k);
		    int size=0;
		    if(reverse_deps!=null){ size=reverse_deps.toArray().length;}
			out.write("\n<br /><strong id='" + k + "'>" + k + "</strong> (depends on "+deps.length+") ("+size+" depend on it):<br />\n");
		    if(reverse_deps!=null){
		      for (String d: reverse_deps) {
				out.write("&nbsp;&nbsp;&nbsp;&nbsp;<a class='IDependOn "+d+"' href='#" + d + "'>" + d + "</a><br />\n");
		      }
		    }
			for(int i=deps.length; i-->0;){
				String link=(String)deps[i];
				if(dependencies.get(link)!=null){
					try {
						out.write("&nbsp;&nbsp;&nbsp;&nbsp;<a class='"+k+" dependsOnMe' href='#" + link + "'>" + link + "</a><br />\n");
					} catch (IOException e) {System.out.println(e.getStackTrace());}
				}else{
					out.write("\t&nbsp;&nbsp;&nbsp;&nbsp;<span class='dependsOnMe'>" + link + "</span><br />\n");
				}
			}
		}
        FileInputStream fstream1 = new FileInputStream("src/main/static/footer_template.html.tmpl");
        DataInputStream in1 = new DataInputStream(fstream1);
        BufferedReader br1 = new BufferedReader(new InputStreamReader(in1));
        String strLine1;
        while ((strLine1 = br1.readLine()) != null)   { out.write(strLine1+"\n"); }
        in1.close();
    	out.close();
	}
}
