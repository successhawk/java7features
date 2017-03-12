package io.github.successhawk.java7.trywithresources;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TryWithResourcesExample {

	/* These are from the Java Tutorials
	 * http://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html
	 * 
	 */
	
	
/* Java 6- */	
public String readFirstLineFromFileWithFinallyBlock(String path)
		throws IOException {
	BufferedReader br = null;
	try {
		br = new BufferedReader(new FileReader(path));
		return br.readLine();
	} finally {
		if (br != null)
			br.close();
	}
}

/* Java 7+ */
public String readFirstLineFromFile(String path) throws IOException {
    try (BufferedReader br = new BufferedReader(new FileReader(path))) {
        return br.readLine();
    }
}
	
/* Java 6- No telling what exception will be thrown and some could be lost. */
public void transferFirstLine(String fromPath, String toPath) throws IOException {
	BufferedReader br = null;
	BufferedWriter bw = null;
	try {
		br = new BufferedReader(new FileReader(fromPath));
    	bw = new BufferedWriter(new FileWriter(toPath));
        bw.write(br.readLine());
        bw.write('\n');
    } finally {
    	// Must close in try to ensure closing of the other resource
		try {
	    	if ( bw != null )
	    		bw.close(); 
		} finally {
	    	if ( br != null )
	    		br.close();
		}
    }
}

/* Java 7+ */
public void transferFirstLine_Java7(String fromPath, String toPath) throws IOException {
    try (BufferedReader br = new BufferedReader(new FileReader(fromPath));
    	 BufferedWriter bw = new BufferedWriter(new FileWriter(toPath))) {
        bw.write(br.readLine());
        bw.write('\n');
    }
}


public static void main(String[] args) {
	
}
}
