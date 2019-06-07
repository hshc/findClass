package com.hshc.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ClassFinder
  extends FilesTreeTraveler
{
  protected Collection getChildrenLeaves(File pLeaf, String pLaunchCmd)
  {
    File[] children = pLeaf.listFiles();
    if (children != null) {
      return Arrays.asList(children);
    }
    return null;
  }
  
  protected Object leafOperations(File leaf, String launchCmd)
  {
    try
    {
      if (leaf.isFile())
      {
        String path = leaf.getCanonicalPath();
        if ((path.endsWith(".zip")) || (path.endsWith(".jar"))) {
          lookInArchive(leaf, launchCmd);
        }
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    return null;
  }
  
  private void lookInArchive(File zipFile, String longClassName)
    throws IOException
  {
    URL zipFileUrl = null;
    InputStream in = null;
    ZipInputStream zin = null;
    ZipEntry entry = null;
    String separatorPattern = "\\\\|/";
    zipFileUrl = zipFile.toURL();
    in = zipFileUrl.openStream();
    zin = new ZipInputStream(new BufferedInputStream(in));
    while ((entry = zin.getNextEntry()) != null)
    {
      String entryName = entry.getName();
      if (entryName.length() == longClassName.length())
      {
        entryName = entryName.replaceAll("\\\\|/", ".");
        longClassName = longClassName.replaceAll("\\\\|/", ".");
        if (entryName.equals(longClassName))
        {
          System.out.println("-->\t" + zipFile.getAbsolutePath());
          break;
        }
      }
    }
    zin.close();
  }
  
  public static void main(String[] args)
  {
    FilesTreeTraveler ftt = new ClassFinder();
    
    int length = args.length;
    if (length == 1)
    {
      System.out.println("not yet implemented ...");
      System.exit(0);
    }
    else if (length == 2)
    {
      ftt.travel(new File(args[0]), args[1]);
    }
    else
    {
      System.out.println("usage: java -jar findClass.jar {[optional]rootSearchPath} {className} \nex: \n\t*java -jar findClass.jar \"C:/TEMP\" \"com.foo.bar.class\" \n\t*java -jar findClass.jar \"com.foo.bar.class\"");
      System.exit(0);
    }
  }
}

