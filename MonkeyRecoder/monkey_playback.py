#!/usr/bin/env monkeyrunner
# Copyright 2010, The Android Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

import sys
import os
from com.android.monkeyrunner import MonkeyRunner as mr
device = mr.waitForConnection();
files = sys.argv[1:];
k = len(files);

path = 'D:/img/'
logPath = 'E:/Module/Monkeyrunner/05-21/log.txt'
title = ''
flag = 'img1';
flag2 = ''

num = 2;
total = num*k;
#print total;
j = 0;
z = 0;

print '\n' + 'Starting . . . . . .' + '\n';

# The format of the file we are parsing is very carfeully constructed.
# Each line corresponds to a single command.  The line is split into 2
# parts with a | character.  Text to the left of the pipe denotes
# which command to run.  The text to the right of the pipe is a python
# dictionary (it can be evaled into existence) that specifies the
# arguments for the command.  In most cases, this directly maps to the
# keyword argument dictionary that could be passed to the underlying
# command. 

# Lookup table to map command strings to functions that implement that
# command.
CMD_MAP = {
	'TOUCH': lambda dev, arg: dev.touch(**arg),
	'DRAG': lambda dev, arg: dev.drag(**arg),
	'PRESS': lambda dev, arg: dev.press(**arg),
	'TYPE': lambda dev, arg: dev.type(**arg),
	'WAIT': lambda dev, arg: mr.sleep(**arg),
	#'IMG': lambda dev, arg: screenShot(**arg)
	}

# Process a single file for the specified device.
def process_file(fp, device):
    row = 1;
    for line in fp:
	
        lines = str(row) + ' lines:'
        print lines;
        outputLog(lines);
        (cmd, rest) = line.split('|')
        command = cmd + ' ' + rest
        print command
        outputLog(command);
        #f = open(r'E:\Module\Monkeyrunner\0520\log.txt','a')
        #l = f.writelines(command);
        #f.close();
		
        try:
            # Parse the pydict
            rest = eval(rest)
        except:
            print 'the command is IMG' + '\n';
            #continue

        if cmd not in CMD_MAP:
			#print 'unknown command: ' + cmd;
		 	screenShot();
			row = row + 1;
			continue

        CMD_MAP[cmd](device, rest)
        row = row +1;
		
def createDirectory(path,title):

	new_path = os.path.join(path, title);
	if not os.path.isdir(new_path):    
		os.makedirs(new_path);
		
def outputLog(content):
    f = open(logPath,'a')
    l = f.writelines(content);
    f.close();	
		

def screenShot():
	global title; 
	# print 'it is screenShot--------------- '
    # Takes a screenshot
	result = device.takeSnapshot();
	# Create a directory
	name = path + title;
	#print name;
	global z;
	global flag;
	if flag != title:
		z = 0;
		flag = title;
	z = z + 1;
	# Writes the screenshot to a file that exist in your PC.
	result.writeToFile(name+'/'+str(z)+'.png','png');

def main():
	
	global k;
	global num;
	global flag2;
	global total;
	global title;
	global files;
	f = open(logPath,'w')
	l = f.writelines("")
	f.close();	
	file = files[0]
	filelist = file.split('|')
	k = len(filelist)
	outputLog("Script Path :\n");
	for n in range(k):
		#print filelist[n];
		outputLog(filelist[n]+'\n');
		
	#print k;
	total = num*k;
	m = 0;
	for ffile in filelist:
		#print ffile
		for i in range(num):
			title = 'img' + `m + 1`+'_'+`i + 1`;
			flag2 = '\n' + '*************************************  Script ' + `m + 1`+' _ '+`i + 1` + ' times  ***************' + '\n';
			outputLog(flag2);
			print flag2;
			createDirectory(path,title);
			fp = open(ffile, 'r');
				
			process_file(fp, device);
			# screenShot();
			fp.close();
			global flag;
			flag = 'img1';
			global j;
			j = j + 1;
		m = m + 1;
	if j >= total:
		mr.alert('the case is finishing','Result','OK')

#MonkeyRunner.alert('the same','result message','OK')
	
if __name__ == '__main__':
    main()
