//package com.walmart.mvnplugin;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.lang.annotation.Annotation;
//import java.lang.reflect.Method;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.net.URLClassLoader;
//import java.nio.charset.Charset;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Set;
//
///*
// * Copyright 2001-2005 The Apache Software Foundation.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//import org.apache.maven.plugin.AbstractMojo;
//import org.apache.maven.plugin.MojoExecutionException;
//import org.apache.maven.plugins.annotations.Component;
//import org.apache.maven.plugins.annotations.LifecyclePhase;
//import org.apache.maven.plugins.annotations.Mojo;
//import org.apache.maven.plugins.annotations.Parameter;
//import org.apache.maven.project.MavenProject;
//import org.reflections.Reflections;
//import org.reflections.scanners.MethodAnnotationsScanner;
//import org.reflections.util.ClasspathHelper;
//import org.reflections.util.ConfigurationBuilder;
//
//import com.github.javaparser.JavaParser;
//import com.github.javaparser.ast.CompilationUnit;
//import com.github.javaparser.ast.PackageDeclaration;
//import com.src.walmart.Event;
//
///**
// * Goal which touches a timestamp file.
// *
// * @goal touch
// * 
// * @phase process-sources
// */
//
//@Mojo(name = "hello", defaultPhase=LifecyclePhase.VERIFY)
//public class MyMojo extends AbstractMojo {
//
//	@Component
//	MavenProject project;
//
//	@Parameter(property = "msg", defaultValue = "from maven")
//	private String msg;
//
//	public void execute() throws MojoExecutionException {
//		
//		List<String> compileSourceRoots = project.getCompileSourceRoots();
//		List<File> allSourceFiles = new ArrayList<File>();
//		HashMap<File, String> filePackageMap = new HashMap<>();
//
//		
//		String outputDirectory = project.getBuild().getOutputDirectory();
//		allSourceFiles = getAllSourceFilesAsList(compileSourceRoots);
//		filePackageMap = getSourceFilePackageMapping(allSourceFiles, outputDirectory);
//
////		File file = new File(outputDirectory);
////
////		getLog().info("The output directory is : " + outputDirectory);
////
////		List<File> listOfFiles = getListOfFiles(file.toString());
////
////		for (Iterator iterator2 = listOfFiles.iterator(); iterator2.hasNext();) {
////			File f = (File) iterator2.next();
////
////			try {
////
////				String canonicalPath = f.getCanonicalPath();
////				CompilationUnit cu = JavaParser.parse(f, Charset.forName("ISO8859_1"));
////
////				PackageDeclaration package1 = cu.getPackageDeclaration().get();
////
////				String packName = "gotPackageName";
////
////				getLog().info("The package name is: " + packName);
////
////				String javaFileName = f.getName();
////				String fileAbsolutePath = f.getAbsolutePath().substring(0, f.getAbsolutePath().lastIndexOf("/"));
////				fileAbsolutePath = fileAbsolutePath.substring(0, fileAbsolutePath.lastIndexOf("/"));
////
////				getLog().info("The name of the class is :" + javaFileName);
////				getLog().info("The absolute path of the file is :" + "file://" + fileAbsolutePath + "/");
////
////				URLClassLoader urlClassLoader;
////				try {
////
////					getLog().info("The class loader path is: " + packName + "." + javaFileName.split("\\.")[0]);
////
////					urlClassLoader = URLClassLoader
////							.newInstance(new URL[] { new URL("file://" + fileAbsolutePath + "/") });
////					Class clazz = Class.forName(javaFileName.split("\\.")[0]);
////					// Class clazz = urlClassLoader.loadClass(packName + "." +
////					// javaFileName.split("\\.")[0]);
////					getLog().info("The type of the object is : " + clazz.getCanonicalName());
////
////					for (Method m : clazz.getMethods()) {
////						getLog().info("The methods of the classes are : " + m.getName());
////						// Event mXY = (Event) m.getAnnotation(Event.class);
////						Annotation[] annotations = m.getAnnotations();
////						if (annotations.length > 0) {
////							Annotation annotation = m.getAnnotations()[0];
////							if (annotation != null) {
////								getLog().info("The annotations available are :" + annotation.toString());
////							}
////						}
////
////						if (m.isAnnotationPresent(Event.class)) {
////							getLog().info("The methods implementing the interface is :" + m);
////						}
////					}
////				} catch (ClassNotFoundException e) {
////					e.printStackTrace();
////				}
////			} catch (IOException e) {
////				e.printStackTrace();
////			}
////
////		}
//
//	}
//	
//	private HashMap<File, String> getSourceFilePackageMapping(List<File> allSourceFiles, String outputDirectory) {
//		
//		HashMap<File, String> filePackageMap = new HashMap<>();
//		
//		for (Iterator iterator = allSourceFiles.iterator(); iterator.hasNext();) {
//			File file = (File) iterator.next();
//			CompilationUnit cu;
//			try {
//				cu = JavaParser.parse(file, Charset.forName("ISO8859_1"));
//				PackageDeclaration packageDecl = cu.getPackageDeclaration().get();
//				String packageName = packageDecl!=null?packageDecl.getName().asString():"";
//
//				String javaFileName = file.getName();
//				String fileAbsolutePath = file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf("/"));
//				fileAbsolutePath = fileAbsolutePath.substring(0, fileAbsolutePath.lastIndexOf("/"));
//				
//				URLClassLoader urlClassLoader = URLClassLoader
//						.newInstance(new URL[] { new URL("file://" + outputDirectory + "/") });
//				
//				Class clazz = urlClassLoader.loadClass(packageName+ "." + javaFileName.split("\\.")[0]);
//				
////				Reflections reflections = new Reflections(new ConfigurationBuilder().setUrls(ClasspathHelper.forJavaClassPath())
////		                .setScanners(new MethodAnnotationsScanner()));
////		        Set<Method> methods = reflections.getMethodsAnnotatedWith(Event.class);
//
//				for (Method m : clazz.getMethods()) {
//					
//					Method method = clazz.newInstance().getClass().getMethod(m.getName());
//					
//					System.out.println();
//					
//					Method declaredMethod = clazz.getDeclaredMethod(m.getName());
//					
//					
//					System.out.println();
//					
//					getLog().info("The methods of the classes are : " + m.getName());
//					// Event mXY = (Event) m.getAnnotation(Event.class);
//					Annotation[] declaredAnnotations = m.getDeclaredAnnotations();
////					if (annotations.length > 0) {
////						Annotation annotation = m.getAnnotations()[0];
////						if (annotation != null) {
////							getLog().info("The annotations available are :" + annotation.toString());
////						}
////					}
//
//					if (m.isAnnotationPresent(Event.class)) {
//						getLog().info("The methods implementing the interface is :" + m);
//					}
//				}
//				
//				filePackageMap.put(file, packageName);
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//			} catch (MalformedURLException e) {
//				e.printStackTrace();
//			} catch (ClassNotFoundException e) {
//				e.printStackTrace();
//			} catch (NoSuchMethodException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (SecurityException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (InstantiationException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IllegalAccessException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		
//		return filePackageMap;
//	}
//
//	private List<File> getAllSourceFilesAsList(List<String> compileSourceRoots) {
//		List<File> allSourceFiles = new ArrayList<File>();
//		
//		for (Iterator iterator = compileSourceRoots.iterator(); iterator.hasNext();) {
//			String sourceDir = (String) iterator.next();
//			List<File> listOfFiles = getListOfFiles(sourceDir);
//			allSourceFiles.addAll(listOfFiles);
//		}
//		
//		return allSourceFiles;
//	}
//
//	@Event(x = 5, y = 5)
//	public void myMethodA() {
//		System.out.println("boo");
//	}
//
//	@Event(x = 3, y = 2)
//	public void myMethodB() {
//		System.out.println("foo");
//	}
//	
//	public static List<File> getListOfFiles(String directoryName) {
//		File directory = new File(directoryName);
//
//		List<File> resultList = new ArrayList<File>();
//
//		// get all the files from a directory
//		File[] fList = directory.listFiles();
//		
//		for (int i = 0; i < fList.length; i++) {
//			File file = fList[i];
//			if(file.getName().contains(".class")||file.getName().contains(".java")) {
//				resultList.addAll(Arrays.asList(fList));
//			}if(file.isDirectory()) {
//				resultList.addAll(getListOfFiles(file.getAbsolutePath()));
//			}
//		}
//		
//		return resultList;
//	}
//
//	public String getMsg() {
//		return msg;
//	}
//
//	public void setMsg(String msg) {
//		this.msg = msg;
//	}
//	
//	
//	
//	/*@Retention(RetentionPolicy.RUNTIME)
//	@Target(ElementType.METHOD)
//	static public @interface Event {
//		public int x();
//
//		public int y();
//	}*/
//
//}
