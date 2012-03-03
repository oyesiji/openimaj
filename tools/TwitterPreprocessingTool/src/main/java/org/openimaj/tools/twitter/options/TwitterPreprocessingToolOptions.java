/**
 * Copyright (c) 2012, The University of Southampton and the individual contributors.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *   * 	Redistributions of source code must retain the above copyright notice,
 * 	this list of conditions and the following disclaimer.
 *
 *   *	Redistributions in binary form must reproduce the above copyright notice,
 * 	this list of conditions and the following disclaimer in the documentation
 * 	and/or other materials provided with the distribution.
 *
 *   *	Neither the name of the University of Southampton nor the names of its
 * 	contributors may be used to endorse or promote products derived from this
 * 	software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.openimaj.tools.twitter.options;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.kohsuke.args4j.CmdLineException;
import org.openimaj.tools.FileToolsUtil;
import org.openimaj.twitter.TwitterStatus;
import org.openimaj.twitter.collection.FileTwitterStatusList;
import org.openimaj.twitter.collection.TwitterStatusList;

/**
 * The single processing command line version of the twitter tool
 * 
 * @author Jonathon Hare <jsh2@ecs.soton.ac.uk>, Sina Samangooei <ss@ecs.soton.ac.uk>
 *
 */
public class TwitterPreprocessingToolOptions extends  AbstractTwitterPreprocessingToolOptions{
	
	File inputFile;
	File outputFile;
	private PrintWriter outWriter = null;
	private boolean stdout;
	
	/**
	 * See: {@link AbstractTwitterPreprocessingToolOptions#AbstractTwitterPreprocessingToolOptions(String[])}
	 * @param args 
	 */
	public TwitterPreprocessingToolOptions(String[] args) {
		super(args);
	}

	@Override
	public boolean validate() throws CmdLineException {
		this.inputFile = FileToolsUtil.validateLocalInput(this);
		if(FileToolsUtil.isStdout(this)){
			this.stdout = true;
		}
		else
		{
			this.outputFile = FileToolsUtil.validateLocalOutput(this);
		}
		return true;
	}

	/**
	 * @return the list of tweets from the input file
	 * @throws IOException
	 */
	public TwitterStatusList<TwitterStatus> getTwitterStatusList() throws IOException {
		if(this.nTweets == -1){
			return FileTwitterStatusList.read(this.inputFile,this.encoding);
		}
		else{
			return FileTwitterStatusList.read(this.inputFile,this.encoding,this.nTweets);
		}
		
	}

	/**
	 * @return a print writer to the output file or stdout
	 * @throws UnsupportedEncodingException
	 * @throws FileNotFoundException
	 */
	public PrintWriter outputWriter() throws UnsupportedEncodingException, FileNotFoundException {
		if(this.outWriter == null){
			if(this.stdout){
				this.outWriter = new PrintWriter(System.out);
			}else{
				this.outWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.outputFile),this.encoding)),true);
			}
		}
			
		return this.outWriter;
	} 
}
