package com.roclas;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException
    {
    	String input_dir="",output_dir="";
        for (int i=0; i<2; i++) {
            	switch(i){
            	case 0: input_dir=args[i];
            	break;
            	case 1: output_dir=args[i];
            	break;
            	default:return;
            	}
        }
        if((input_dir.length()<1) || (output_dir.length()<1)){
        		String path= App.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        		String[] broken_path = path.split("/");
        		String jar=broken_path[broken_path.length-1];
            	System.out.println("usage: java -jar "+jar+" <input dir> <output dir>");
            	System.out.println("being: <input dir> the directory where all your poms are located, and <output dir> the directory where you want to locate the result file");
            	return;
        }
        PomDependencyChecker.printDependencyTree(input_dir, output_dir);
    }
}
