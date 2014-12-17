package com.roclas;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.roclas.PomDependencyChecker;

/**
 * Unit test for simple App.
 */
public class AppTest {

    
	@Test
    public void testApp() throws IOException {
		//String input_dir="D:/MyCode/Mypoms";
		//String output_dir="D:\\MyCode";
        String input_dir = "src/main/static/test_files/input";
        String output_dir = "src/main/static/test_files/output";
        PomDependencyChecker.printDependencyTree(input_dir,output_dir);
        //assertTrue( true );
    }

}
