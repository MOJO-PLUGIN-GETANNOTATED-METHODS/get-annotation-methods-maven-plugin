package com.walmart.mvnplugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;

/**
 * Goal finds all the methods in the project that are annotated with the arguments supplied.
 *
 * @goal find.
 * 
 * @phase VERIFY
*/

@Mojo(name = "find", defaultPhase=LifecyclePhase.VERIFY)
public class MyMojo extends AbstractMojo {

	@Component
	MavenProject project;

	@Parameter(property = "interfaceNames", defaultValue = "")
	private String interfaceNames;
	
	private String[] interfaceNamesArr;

	public void execute() throws MojoExecutionException {
		
		interfaceNamesArr = interfaceNames.split(",");
		
		System.out.println();
		
		List<String> compileSourceRoots = project.getCompileSourceRoots();
		List<File> allSourceFiles = new ArrayList<File>();
		HashMap<File, String> filePackageMap = new HashMap<>();
		
		String outputDirectory = project.getBuild().getOutputDirectory();
		allSourceFiles = getAllSourceFilesAsList(compileSourceRoots);
		filePackageMap = getSourceFilePackageMapping(allSourceFiles, outputDirectory);

	}
	
	private HashMap<File, String> getSourceFilePackageMapping(List<File> allSourceFiles, String outputDirectory) {
		
		HashMap<File, String> filePackageMap = new HashMap<>();
		File outputFileDir = new File(outputDirectory); 
		
		for (Iterator iterator = allSourceFiles.iterator(); iterator.hasNext();) {
			File file = (File) iterator.next();
			CompilationUnit cu;
			try {
				cu = JavaParser.parse(file, Charset.forName("ISO8859_1"));
				PackageDeclaration packageDecl = cu.getPackageDeclaration().get();
				String packageName = packageDecl!=null?packageDecl.getName().asString():"";

				String javaFileName = file.getName();
				String fileAbsolutePath = file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf("/"));
				fileAbsolutePath = fileAbsolutePath.substring(0, fileAbsolutePath.lastIndexOf("/"));
				
				URL classesUrl = outputFileDir.toURI().toURL();
				URL[] classesUrls = new URL[]{classesUrl}; 

				// Make sure to use the URLClassLoader, using the simple ClassLoader WILL NOT WORK for reading the annotations       
				URLClassLoader classLoader = URLClassLoader.newInstance(classesUrls, getClass().getClassLoader());
				
//				URLClassLoader urlClassLoader = URLClassLoader
//						.newInstance(new URL[] { new URL("file://" + outputDirectory + "/") });
				
				Class clazz = classLoader.loadClass(packageName+ "." + javaFileName.split("\\.")[0]);
				
				for (Method m : clazz.getMethods()) {
					
					for (int i = 0; i < interfaceNamesArr.length; i++) {
						Annotation[] annotations = m.getAnnotations();
						for (int j = 0; j < annotations.length; j++) {
							if(annotations[j].annotationType().getCanonicalName().contains(interfaceNamesArr[i])) {
								getLog().info("The methods implementing the interface " + interfaceNamesArr[i] + " is :" + m);
								getLog().info("The fully qualified interface name is : " + annotations[j].annotationType().getCanonicalName());
							}
						}
					}
				}
				
				filePackageMap.put(file, packageName);
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}
		
		return filePackageMap;
	}

	private List<File> getAllSourceFilesAsList(List<String> compileSourceRoots) {
		List<File> allSourceFiles = new ArrayList<File>();
		
		for (Iterator iterator = compileSourceRoots.iterator(); iterator.hasNext();) {
			String sourceDir = (String) iterator.next();
			List<File> listOfFiles = getListOfFiles(sourceDir);
			allSourceFiles.addAll(listOfFiles);
		}
		
		return allSourceFiles;
	}

	public static List<File> getListOfFiles(String directoryName) {
		File directory = new File(directoryName);
		List<File> resultList = new ArrayList<File>();
		File[] fList = directory.listFiles();
		
		for (int i = 0; i < fList.length; i++) {
			File file = fList[i];
			if(file.getName().contains(".class")||file.getName().contains(".java")) {
				resultList.addAll(Arrays.asList(file));
			}if(file.isDirectory()) {
				resultList.addAll(getListOfFiles(file.getAbsolutePath()));
			}
		}
		
		return resultList;
	}

}
