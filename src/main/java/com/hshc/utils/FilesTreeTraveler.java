package com.hshc.utils;

import java.io.File;
import java.util.Collection;
import java.util.Stack;

public abstract class FilesTreeTraveler
{
  protected abstract Collection getChildrenLeaves(File paramFile, String paramString);
  
  protected abstract Object leafOperations(File paramFile, String paramString);
  
  public void travel(File pRoot, String pLaunchCmd)
  {
    Stack aStack = new Stack();
    aStack.push(pRoot);
    while (!aStack.empty())
    {
      File aTleaf = (File)aStack.pop();
      Collection aTleaves = getChildrenLeaves(aTleaf, pLaunchCmd);
      if (aTleaves != null) {
        aStack.addAll(aTleaves);
      }
      leafOperations(aTleaf, pLaunchCmd);
    }
  }
}

