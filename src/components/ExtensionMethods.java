package components;

import java.io.File;

public class ExtensionMethods {

      public static String[] getMapsList(){
        
        if(!new File(SharedVariables.MapsDirectory).exists())
          return new String[0];
        
        File[] fileList = new File(SharedVariables.MapsDirectory).listFiles();
        String[] fileName = new String[fileList.length];

        for(int i=0; i<fileList.length;i++) 
            if(fileList[i].getName().endsWith(".xml"))
              fileName[i] = fileList[i].getName().replaceFirst("[.][^.]+$", "");
        
        return fileName;
      }
  
}