package views;

import controllers.CaseImporter;
import controllers.CaseSolver;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import models.Location;
import models.ModelCase;
import models.Node;

public class App {
    
    private static CaseImporter caseImporter;
   
    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        caseImporter = new CaseImporter(
                "C:\\Users\\Asus\\Desktop\\TesisModel\\data15.txt");
        ModelCase modelCase = caseImporter.extract();
        CaseSolver caseSolver = new CaseSolver(modelCase);
        caseSolver.runIteration();
       // caseSolver.runIteration();
    }

}
