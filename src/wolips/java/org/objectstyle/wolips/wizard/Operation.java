/* ====================================================================
 * 
 * The ObjectStyle Group Software License, Version 1.0 
 *
 * Copyright (c) 2002 The ObjectStyle Group 
 * and individual authors of the software.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer. 
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:  
 *       "This product includes software developed by the 
 *        ObjectStyle Group (http://objectstyle.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "ObjectStyle Group" and "Cayenne" 
 *    must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written 
 *    permission, please contact andrus@objectstyle.org.
 *
 * 5. Products derived from this software may not be called "ObjectStyle"
 *    nor may "ObjectStyle" appear in their names without prior written
 *    permission of the ObjectStyle Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE OBJECTSTYLE GROUP OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the ObjectStyle Group.  For more
 * information on the ObjectStyle Group, please see
 * <http://objectstyle.org/>.
 *
 */
 
 package org.objectstyle.wolips.wizard;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.*;
import org.eclipse.ui.dialogs.IOverwriteQuery;
import org.eclipse.ui.wizards.datatransfer.FileSystemStructureProvider;
import org.eclipse.ui.wizards.datatransfer.ImportOperation;
import org.objectstyle.wolips.WOLipsPlugin;
import org.objectstyle.wolips.io.FileStringScanner;

/**
 * @author uli
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class Operation {

	/**
	 * Imports file from the plugin to a project.
	 */
	protected void importFilesFromDirectory(String pluginRelativePath, IPath destPath, IProgressMonitor monitor, IOverwriteQuery aOverwriteQuery) throws InvocationTargetException, InterruptedException, CoreException {		
		File file;
		try {
			URL starterURL= new URL(WOLipsPlugin.getDefault().getDescriptor().getInstallURL(), pluginRelativePath);
		    String folder = Platform.asLocalURL(starterURL).getFile();
			file = new File(folder);
			}		
		 catch (IOException e) {
			String message= pluginRelativePath + ": " + e.getMessage(); //$NON-NLS-1$
			Status status= new Status(IStatus.ERROR, WOLipsPlugin.getPluginId(), IStatus.ERROR, message, e);
			throw new CoreException(status);
		}
		ImportOperation op= new ImportOperation(destPath, file, FileSystemStructureProvider.INSTANCE, aOverwriteQuery);
		op.setCreateContainerStructure(false);
		op.run(monitor);
	}
	
	/**
	 * Renames the directory WOComponent.wo and the related files to the given string.
	 * The given IProject is uses.
	 */
	protected void renameNewWOComponentTo(IProject project, String aComponentName, String aFullyQualifiedComponentName, File aResourcesPath, IProgressMonitor monitor) throws CoreException, IOException {
		IFolder resources = project.getFolder("Resources");
		IFile aApiFile = resources.getFile("wocomponent.api");
		IFile aNewApiFile = resources.getFile(aComponentName + ".api");
		aApiFile.move(aNewApiFile.getFullPath(), true, monitor);
		FileStringScanner.fileOpenReplaceWith(aNewApiFile.getLocation().toOSString(), "wocomponenttemplate", aFullyQualifiedComponentName);
	
		IFolder afolder = resources.getFolder("wocomponent.wo");
		IFolder aNewFolder = resources.getFolder(aComponentName + ".wo");
		afolder.move(aNewFolder.getFullPath(), true, monitor);
		
		
		IFile aHtmlFile = aNewFolder.getFile("wocomponent.html");
		IFile aNewHtmlFile = aNewFolder.getFile(aComponentName + ".html");
		aHtmlFile.move(aNewHtmlFile.getFullPath(), true, monitor);
		
		IFile aWodFile = aNewFolder.getFile("wocomponent.wod");
		IFile aNewWodFile = aNewFolder.getFile(aComponentName + ".wod");
		aWodFile.move(aNewWodFile.getFullPath(), true, monitor);
		
		IFile aWooFile = aNewFolder.getFile("wocomponent.woo");
		IFile aNewWooFile = aNewFolder.getFile(aComponentName + ".woo");
		aWooFile.move(aNewWooFile.getFullPath(), true, monitor);
	} 
}
